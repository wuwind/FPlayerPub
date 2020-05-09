#include <jni.h>
#include <stdio.h>
#include <x264.h>
#include <x264/x264.h>
#include <android/log.h>
#include <string.h>
#include <librtmp/rtmp.h>
#include <faac/faac.h>
#include <pthread.h>
#include <malloc.h>
#include "queue.h"

#define LOGE(FORMAT, ...) __android_log_print(ANDROID_LOG_ERROR,"wuhf",FORMAT,##__VA_ARGS__)
#define LOGI(FORMAT, ...) __android_log_print(ANDROID_LOG_INFO,"jason",FORMAT,##__VA_ARGS__)

x264_param_t param;
x264_picture_t pic_in;
x264_t *video_encode_handler;
int y_len, u_len;
FILE *file;
unsigned int start_time;
//线程处理
pthread_mutex_t mutex;
pthread_cond_t cond;
//rtmp流媒体地址
char *rtmp_path;
//是否直播
int is_pushing = FALSE;

unsigned long inputSamples;
unsigned long maxOutputBytes;
faacEncHandle audio_encode_handler;

/**
 * 加入RTMPPacket队列，等待发送线程发送
 */
void add_rtmp_packet(RTMPPacket *packet) {
    pthread_mutex_lock(&mutex);
    if (is_pushing) {
        queue_append_last(packet);
        LOGE("%s", "queue_append_last");
    }
    pthread_cond_signal(&cond);
    pthread_mutex_unlock(&mutex);
}

/**
 * 添加AAC头信息
 */
void add_aac_sequence_header() {
    //获取aac头信息的长度
    unsigned char *buf;
    unsigned long len; //长度
    faacEncGetDecoderSpecificInfo(audio_encode_handler, &buf, &len);
    int body_size = 2 + len;
    RTMPPacket *packet = malloc(sizeof(RTMPPacket));
    //RTMPPacket初始化
    RTMPPacket_Alloc(packet, body_size);
    RTMPPacket_Reset(packet);
    unsigned char *body = packet->m_body;
    //头信息配置
    /*AF 00 + AAC RAW data*/
    body[0] = 0xAF;//10 5 SoundFormat(4bits):10=AAC,SoundRate(2bits):3=44kHz,SoundSize(1bit):1=16-bit samples,SoundType(1bit):1=Stereo sound
    body[1] = 0x00;//AACPacketType:0表示AAC sequence header
    memcpy(&body[2], buf, len); /*spec_buf是AAC sequence header数据*/
    packet->m_packetType = RTMP_PACKET_TYPE_AUDIO;
    packet->m_nBodySize = body_size;
    packet->m_nChannel = 0x04;
    packet->m_hasAbsTimestamp = 0;
    packet->m_nTimeStamp = 0;
    packet->m_headerType = RTMP_PACKET_SIZE_MEDIUM;
    add_rtmp_packet(packet);
    free(buf);
}

/**
 * 添加AAC rtmp packet
 */
void add_aac_body(unsigned char *buf, int len) {
    LOGE("add_aac_body");
    int body_size = 2 + len;
    RTMPPacket *packet = malloc(sizeof(RTMPPacket));
    //RTMPPacket初始化
    RTMPPacket_Alloc(packet, body_size);
    RTMPPacket_Reset(packet);
    unsigned char *body = packet->m_body;
    //头信息配置
    /*AF 00 + AAC RAW data*/
    body[0] = 0xAF;//10 5 SoundFormat(4bits):10=AAC,SoundRate(2bits):3=44kHz,SoundSize(1bit):1=16-bit samples,SoundType(1bit):1=Stereo sound
    body[1] = 0x01;//AACPacketType:1表示AAC raw
    memcpy(&body[2], buf, len); /*spec_buf是AAC raw数据*/
    packet->m_packetType = RTMP_PACKET_TYPE_AUDIO;
    packet->m_nBodySize = body_size;
    packet->m_nChannel = 0x04;
    packet->m_hasAbsTimestamp = 0;
    packet->m_headerType = RTMP_PACKET_SIZE_LARGE;
    packet->m_nTimeStamp = RTMP_GetTime() - start_time;
    add_rtmp_packet(packet);
}

/**
 * 从队列中不断拉取RTMPPacket发送给流媒体服务器）
 */
void *push_thread(void *arg) {
    //建立RTMP连接
    RTMP *rtmp = RTMP_Alloc();
    if (!rtmp) {
        LOGE("rtmp初始化失败");
        goto end;
    }
    RTMP_Init(rtmp);
    rtmp->Link.timeout = 5; //连接超时的时间
    //设置流媒体地址
    RTMP_SetupURL(rtmp, rtmp_path);
    //发布rtmp数据流
    RTMP_EnableWrite(rtmp);
    //建立连接
    if (!RTMP_Connect(rtmp, NULL)) {
        LOGE("%s", "RTMP 连接失败");
        goto end;
    }
    if (!RTMP_ConnectStream(rtmp, 0)) { //连接流
        goto end;
    }
    LOGE("%s", "RTMP_ConnectStream");
    is_pushing = TRUE;
    //计时
    start_time = RTMP_GetTime();
    add_aac_sequence_header();
    while (is_pushing) {
        //发送
        pthread_mutex_lock(&mutex);
        pthread_cond_wait(&cond, &mutex);
        //取出队列中的RTMPPacket
        RTMPPacket *packet = queue_get_first();
        if (packet) {
            queue_delete_first(); //移除
            packet->m_nInfoField2 = rtmp->m_stream_id; //RTMP协议，stream_id数据
            LOGE("queue size:%d", queue_size());
            int i = RTMP_SendPacket(rtmp, packet, 1); //TRUE放入librtmp队列中，并不是立即发送
            if (!i) {
                LOGE("RTMP 断开");
                RTMPPacket_Free(packet);
                pthread_mutex_unlock(&mutex);
                goto end;
            }
            RTMPPacket_Free(packet);
        }
        pthread_mutex_unlock(&mutex);
    }
    end:
    LOGI("%s", "释放资源");
    RTMP_Close(rtmp);
    RTMP_Free(rtmp);
    return 0;
}

JNIEXPORT void JNICALL
Java_com_libwuwind_jni_NativePusher_startPush(JNIEnv *env, jobject instance, jstring url_) {
    const char *url = (*env)->GetStringUTFChars(env, url_, 0);

    rtmp_path = malloc(strlen(url) + 1);
    memset(rtmp_path, 0, strlen(url) + 1);
    memcpy(rtmp_path, url, strlen(url));

    //初始化互斥锁与条件变量
    pthread_mutex_init(&mutex, NULL);
    pthread_cond_init(&cond, NULL);

    //创建队列
    create_queue();
    //启动消费者线程（从队列中不断拉取RTMPPacket发送给流媒体服务器）
    pthread_t push_thread_id;
    pthread_create(&push_thread_id, NULL, push_thread, NULL);

    (*env)->ReleaseStringUTFChars(env, url_, url);
}

JNIEXPORT void JNICALL
Java_com_libwuwind_jni_NativePusher_stopPush(JNIEnv *env, jobject instance) {
    free(rtmp_path);
    is_pushing = FALSE;
    if (file)
        fclose(file);
}

JNIEXPORT void JNICALL
Java_com_libwuwind_jni_NativePusher_release(JNIEnv *env, jobject instance) {

    // TODO

}


JNIEXPORT void JNICALL
Java_com_libwuwind_jni_NativePusher_setVideoOptions(JNIEnv *env, jobject instance, jint width,
                                                    jint height, jint bitrate, jint fps) {
    x264_param_default_preset(&param, "ultrafast", "zerolatency");

    param.i_csp = X264_CSP_I420;
    param.i_width = width;
    param.i_height = height;

    y_len = width * height;
    u_len = y_len / 4;

    param.rc.i_rc_method = X264_RC_CRF;
    param.rc.i_bitrate = bitrate / 1000;
    param.rc.i_vbv_max_bitrate = (int) (bitrate / 1000 * 1.2);

    param.b_vfr_input = 0;
    param.i_fps_num = (uint32_t) fps;
    param.i_fps_den = 1;
    param.i_timebase_num = param.i_fps_den;
    param.i_timebase_den = param.i_fps_num;
    param.i_threads = 1;

    param.b_repeat_headers = 1;
    param.i_level_idc = 51;

    x264_param_apply_profile(&param, "baseline");

    x264_picture_alloc(&pic_in, param.i_csp, param.i_width, param.i_height);
    pic_in.i_pts = 0;

    video_encode_handler = x264_encoder_open(&param);
    if (video_encode_handler) {
        LOGE("%s", "打开编码器成功");
    } else {
        LOGE("%s", "打开编码器失败");
    }
//    file = fopen("/storage/emulated/0/a.h264", "wb+");
}


JNIEXPORT void JNICALL
Java_com_libwuwind_jni_NativePusher_setAudioOptions(JNIEnv *env, jobject instance, jint sampleRate,
                                                    jint channel) {
    // TODO
    audio_encode_handler = faacEncOpen(sampleRate, channel, &inputSamples, &maxOutputBytes);
    if (!audio_encode_handler) {
        LOGE("%s", "打开音频编码器失败");
        return;
    }
    faacEncConfigurationPtr p_config = faacEncGetCurrentConfiguration(audio_encode_handler);
    p_config->mpegVersion = MPEG4;
    p_config->aacObjectType = LOW;
    p_config->allowMidside = 1;
    p_config->outputFormat = 0; //输出是否包含ADTS头
    p_config->useTns = 1; //时域噪音控制,大概就是消爆音
    p_config->useLfe = 0; //Low Frequency Element 低频元素
//	p_config->inputFormat = FAAC_INPUT_16BIT;
    p_config->quantqual = 100;
    p_config->bandWidth = 0; //频宽
    p_config->shortctl = SHORTCTL_NORMAL;

    if (faacEncSetConfiguration(audio_encode_handler, p_config) == 0) {
        LOGE("%s", "音频编码器配置失败");
        return;
    }
    LOGE("%s", "音频编码器配置成功");

}


/**
 * 发送h264 SPS与PPS参数集
 */
void add_264_sequence_header(unsigned char *pps, unsigned char *sps, int pps_len, int sps_len) {
    int body_size = 16 + sps_len + pps_len; //按照H264标准配置SPS和PPS，共使用了16字节
    RTMPPacket *packet = malloc(sizeof(RTMPPacket));
    //RTMPPacket初始化
    RTMPPacket_Alloc(packet, body_size);
    RTMPPacket_Reset(packet);

    unsigned char *body = packet->m_body;
    int i = 0;
    //二进制表示：00010111
    body[i++] = 0x17;//VideoHeaderTag:FrameType(1=key frame)+CodecID(7=AVC)
    body[i++] = 0x00;//AVCPacketType = 0表示设置AVCDecoderConfigurationRecord
    //composition time 0x000000 24bit ?
    body[i++] = 0x00;
    body[i++] = 0x00;
    body[i++] = 0x00;

    /*AVCDecoderConfigurationRecord*/
    body[i++] = 0x01;//configurationVersion，版本为1
    body[i++] = sps[1];//AVCProfileIndication
    body[i++] = sps[2];//profile_compatibility
    body[i++] = sps[3];//AVCLevelIndication
    //?
    body[i++] = 0xFF;//lengthSizeMinusOne,H264 视频中 NALU的长度，计算方法是 1 + (lengthSizeMinusOne & 3),实际测试时发现总为FF，计算结果为4.

    /*sps*/
    body[i++] = 0x01;
//    body[i++] = 0xE1;//numOfSequenceParameterSets:SPS的个数，计算方法是 numOfSequenceParameterSets & 0x1F,实际测试时发现总为E1，计算结果为1.
    body[i++] = (sps_len >> 8) & 0xff;//sequenceParameterSetLength:SPS的长度
    body[i++] = sps_len & 0xff;//sequenceParameterSetNALUnits
    memcpy(&body[i], sps, sps_len);
    i += sps_len;

    /*pps*/
    body[i++] = 0x01;//numOfPictureParameterSets:PPS 的个数,计算方法是 numOfPictureParameterSets & 0x1F,实际测试时发现总为E1，计算结果为1.
    body[i++] = (pps_len >> 8) & 0xff;//pictureParameterSetLength:PPS的长度
    body[i++] = (pps_len) & 0xff;//PPS
    memcpy(&body[i], pps, pps_len);
    i += pps_len;

    //Message Type，RTMP_PACKET_TYPE_VIDEO：0x09
    packet->m_packetType = RTMP_PACKET_TYPE_VIDEO;
    //Payload Length
    packet->m_nBodySize = body_size;
    //Time Stamp：4字节
    //记录了每一个tag相对于第一个tag（File Header）的相对时间。
    //以毫秒为单位。而File Header的time stamp永远为0。
    packet->m_nTimeStamp = 0;
    packet->m_hasAbsTimestamp = 0;
    packet->m_nChannel = 0x04; //Channel ID，Audio和Vidio通道
    packet->m_headerType = RTMP_PACKET_SIZE_MEDIUM; //?
    //将RTMPPacket加入队列
    add_rtmp_packet(packet);

}

/**
 * 发送h264帧信息
 */
void add_264_body(unsigned char *buf, int len) {
    //去掉起始码(界定符)
    if (buf[2] == 0x00) {  //00 00 00 01
        buf += 4;
        len -= 4;
    } else if (buf[2] == 0x01) { // 00 00 01
        buf += 3;
        len -= 3;
    }
    int body_size = len + 9;
    RTMPPacket *packet = malloc(sizeof(RTMPPacket));
    RTMPPacket_Alloc(packet, body_size);

    unsigned char *body = packet->m_body;
    //当NAL头信息中，type（5位）等于5，说明这是关键帧NAL单元
    //buf[0] NAL Header与运算，获取type，根据type判断关键帧和普通帧
    //00000101 & 00011111(0x1f) = 00000101
    int type = buf[0] & 0x1f;
    //Inter Frame 帧间压缩
    body[0] = 0x27;//VideoHeaderTag:FrameType(2=Inter Frame)+CodecID(7=AVC)
    //IDR I帧图像
    if (type == NAL_SLICE_IDR) {
        body[0] = 0x17;//VideoHeaderTag:FrameType(1=key frame)+CodecID(7=AVC)
    }
    //AVCPacketType = 1
    body[1] = 0x01; /*nal unit,NALUs（AVCPacketType == 1)*/
    body[2] = 0x00; //composition time 0x000000 24bit
    body[3] = 0x00;
    body[4] = 0x00;

    //写入NALU信息，右移8位，一个字节的读取？
    body[5] = (len >> 24) & 0xff;
    body[6] = (len >> 16) & 0xff;
    body[7] = (len >> 8) & 0xff;
    body[8] = (len) & 0xff;

    /*copy data*/
    memcpy(&body[9], buf, len);

    packet->m_hasAbsTimestamp = 0;
    packet->m_nBodySize = body_size;
    packet->m_packetType = RTMP_PACKET_TYPE_VIDEO;//当前packet的类型：Video
    packet->m_nChannel = 0x04;
    packet->m_headerType = RTMP_PACKET_SIZE_LARGE;
//	packet->m_nTimeStamp = -1;
    packet->m_nTimeStamp = RTMP_GetTime() - start_time;//记录了每一个tag相对于第一个tag（File Header）的相对时间
    add_rtmp_packet(packet);

}

JNIEXPORT void JNICALL
Java_com_libwuwind_jni_NativePusher_fireVideo(JNIEnv *env, jobject instance, jbyteArray data_) {
    jbyte *nv21_buffer = (*env)->GetByteArrayElements(env, data_, NULL);

    //NV21->YUV420P
    jbyte *u = (jbyte *) pic_in.img.plane[1];
    jbyte *v = (jbyte *) pic_in.img.plane[2];
    //nv21 4:2:0 Formats, 12 Bits per Pixel
    //nv21与yuv420p，y个数一致，uv位置对调
    //nv21转yuv420p  y = w*h,u/v=w*h/4
    //nv21 = yvu yuv420p=yuv y=y u=y+1+1 v=y+1
    memcpy(pic_in.img.plane[0], nv21_buffer, y_len);
    int i;
    for (i = 0; i < u_len; i++) {
        *(u + i) = *(nv21_buffer + y_len + i * 2 + 1);
        *(v + i) = *(nv21_buffer + y_len + i * 2);
    }
    x264_nal_t *pp_nal;
    int pi_nal;
    x264_picture_t pic_out;
    //int     x264_encoder_encode( x264_t *, x264_nal_t **pp_nal, int *pi_nal, x264_picture_t *pic_in, x264_picture_t *pic_out );
    int i_frame_size = x264_encoder_encode(video_encode_handler, &pp_nal, &pi_nal, &pic_in,
                                           &pic_out);
    if (i_frame_size < 0) {
        LOGE("%s", "编码失败");
        return;
    }
    LOGE("i_frame_size %d ", i_frame_size);
    LOGE("pi_nal %d ", pi_nal);

    //fwrite(pp_nal->p_payload, i_frame_size, 1, file);

    for (int i = 0; i < pi_nal; ++i) {
        LOGE("i_type %d ", pp_nal->i_type);
    }

    //使用rtmp协议将h264编码的视频数据发送给流媒体服务器
    //帧分为关键帧和普通帧，为了提高画面的纠错率，关键帧应包含SPS和PPS数据
    int sps_len, pps_len;
    unsigned char sps[100];
    unsigned char pps[100];
    memset(sps, 0, 100);
    memset(pps, 0, 100);

    //遍历NALU数组，根据NALU的类型判断
    for (i = 0; i < pi_nal; i++) {
        if (pp_nal[i].i_type == NAL_SPS) {
            //复制SPS数据
            sps_len = pp_nal[i].i_payload - 4;
            memcpy(sps, pp_nal[i].p_payload + 4, sps_len); //不复制四字节起始码
        } else if (pp_nal[i].i_type == NAL_PPS) {
            //复制PPS数据
            pps_len = pp_nal[i].i_payload - 4;
            memcpy(pps, pp_nal[i].p_payload + 4, pps_len); //不复制四字节起始码

            //发送序列信息
            //h264关键帧会包含SPS和PPS数据
            add_264_sequence_header(pps, sps, pps_len, sps_len);

        } else {
            //发送帧信息
            add_264_body(pp_nal[i].p_payload, pp_nal[i].i_payload);
        }
    }

    (*env)->ReleaseByteArrayElements(env, data_, nv21_buffer, 0);
}


JNIEXPORT void JNICALL
Java_com_libwuwind_jni_NativePusher_fireAudio(JNIEnv *env, jobject instance, jbyteArray data_,
                                              jint len) {
    jbyte *b_buffer = (*env)->GetByteArrayElements(env, data_, NULL);

    // TODO
    int *pcmbuf;
    unsigned char *bitbuf;
    pcmbuf = (short *) malloc(inputSamples * sizeof(int));
    bitbuf = (unsigned char *) malloc(maxOutputBytes * sizeof(unsigned char));
    int nByteCount = 0;
    unsigned int nBufferSize = (unsigned int) len / 2;
    unsigned short *buf = (unsigned short *) b_buffer;
    while (nByteCount < nBufferSize) {
        int audioLength = inputSamples;
        if ((nByteCount + inputSamples) >= nBufferSize) {
            audioLength = nBufferSize - nByteCount;
        }
        int i;
        for (i = 0; i < audioLength; i++) {//每次从实时的pcm音频队列中读出量化位数为8的pcm数据。
            int s = ((int16_t *) buf + nByteCount)[i];
            pcmbuf[i] = s << 8;//用8个二进制位来表示一个采样量化点（模数转换）
        }
        nByteCount += inputSamples;
        //利用FAAC进行编码，pcmbuf为转换后的pcm流数据，audioLength为调用faacEncOpen时得到的输入采样数，bitbuf为编码后的数据buff，nMaxOutputBytes为调用faacEncOpen时得到的最大输出字节数
        int byteslen = faacEncEncode(audio_encode_handler, pcmbuf, audioLength, bitbuf,
                                     maxOutputBytes);
        if (byteslen < 1) {
            continue;
        }
        add_aac_body(bitbuf, byteslen);//从bitbuf中得到编码后的aac数据流，放到数据队列
    }
    (*env)->ReleaseByteArrayElements(env, data_, b_buffer, 0);
    if (bitbuf)
        free(bitbuf);
    if (pcmbuf)
        free(pcmbuf);


}


