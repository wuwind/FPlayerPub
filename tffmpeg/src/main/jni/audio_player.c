#include <stdio.h>
#include <stdlib.h>
#include <unistd.h>
#include "com_fg_tffmpeg_VideoUtils.h"
#include "libavformat/avformat.h"
#include "libavcodec/avcodec.h"
#include <android/log.h>
//#include "libswscale/swscale.h"
#include "libswresample/swresample.h"


#define LOGE(FORMAT, ...) __android_log_print(ANDROID_LOG_ERROR, "wuhf", FORMAT, ##__VA_ARGS__);

#define MAX_AUDIO_FRAME_SIZE 44100*2

JNIEXPORT void JNICALL Java_com_fg_tffmpeg_VideoUtils_decodeAudio
  (JNIEnv *env, jclass jcls, jstring input, jstring output) {

	const char *input_c = (*env)->GetStringUTFChars(env, input, JNI_FALSE);
	const char *output_c = (*env)->GetStringUTFChars(env, output, JNI_FALSE);

	av_register_all();
	AVFormatContext *fmctx = avformat_alloc_context();
	if(avformat_open_input(&fmctx, input_c, NULL, NULL) < 0 ) {
		LOGE("%s", "打开文件失败");
		return;
	}
	if(avformat_find_stream_info(fmctx, NULL) < 0) {
		LOGE("%s", "读取流信息失败");
		return;
	}
	int i;
	int audio_index = -1;
	for(i=0; i<fmctx->nb_streams; i++) {
		if(fmctx->streams[i]->codec->codec_type == AVMEDIA_TYPE_AUDIO) {
			audio_index = i;
			break;
		}
	}
	if(audio_index == -1) {
		LOGE("%s", "没有找到音频流");
		return;
	}
	AVCodec *codec;
	AVCodecContext *codecCtx;
	codecCtx = fmctx->streams[audio_index]->codec;
	codec = avcodec_find_decoder(codecCtx->codec_id);
	if(codec == NULL) {
		LOGE("%s", "没有找到解码器");
		return;
	}
	if(avcodec_open2(codecCtx, codec, NULL) !=0) {
		LOGE("%s", "打开解码器失败");
		return;
	}

	//-------------------重采样参数设置
	SwrContext *swrCtx = swr_alloc();

	int64_t out_ch_layout = AV_CH_LAYOUT_STEREO;
	int64_t in_ch_layout = codecCtx->channel_layout;
	enum AVSampleFormat out_sample_fmt = AV_SAMPLE_FMT_S16;
	enum AVSampleFormat in_sample_fmt = codecCtx->sample_fmt;
	int out_sample_rate = 44100;
	int in_sample_rate = codecCtx->sample_rate;
	swr_alloc_set_opts(swrCtx, out_ch_layout, out_sample_fmt, out_sample_rate,
            in_ch_layout, in_sample_fmt, in_sample_rate,
            0, NULL);
	swr_init(swrCtx);
	//-------------------end
	AVPacket *packet = av_malloc(sizeof(AVPacket));
	AVFrame *frame = av_frame_alloc();
	int got_frame;
	int ret;
	int count = 0;
	//输出buf
	uint8_t *outbuf = av_malloc(MAX_AUDIO_FRAME_SIZE);
	//打开文件
	FILE *file = fopen(output_c, "wb");
	int out_buf_size = 0;
	int nb_channels = av_get_channel_layout_nb_channels(out_ch_layout);
	while(av_read_frame(fmctx, packet) == 0){
		LOGE("%s", "读取到一个Packet");
		ret = avcodec_decode_audio4(codecCtx,frame, &got_frame, packet);
		if(ret < 0 ) {
			LOGE("%s", "解码完成");
		}
		if(got_frame != 0) {
			count ++;
			LOGE("%s, %d", "解码", count);
			swr_convert(swrCtx, &outbuf, MAX_AUDIO_FRAME_SIZE, (const uint8_t **)frame->data, frame->nb_samples);
			//获取sample的size
			out_buf_size = av_samples_get_buffer_size(NULL, nb_channels, frame->nb_samples,
                               out_sample_fmt, 1);
			fwrite(outbuf, 1, out_buf_size, file);
		} else {
			LOGE("%s", "没有帧可以被解码");
		}
		av_free_packet(packet);
	}
	fclose(file);
	av_frame_free(&frame);
	av_free(outbuf);

	swr_free(&swrCtx);
	avcodec_close(codecCtx);
	avformat_close_input(&fmctx);

	(*env)->ReleaseStringUTFChars(env, input, input_c);
	(*env)->ReleaseStringUTFChars(env, output, output_c);

}
