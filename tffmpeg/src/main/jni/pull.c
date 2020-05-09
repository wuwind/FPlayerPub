#include <jni.h>
#include <stdio.h>
#include <stdlib.h>
#include <pthread.h>
#include <rtmp.h>
#include <android/log.h>

#define LOGE(FORMAT, ...) __android_log_print(ANDROID_LOG_ERROR,"wuhf",FORMAT,##__VA_ARGS__)
#define LOGI(FORMAT, ...) __android_log_print(ANDROID_LOG_INFO,"jason",FORMAT,##__VA_ARGS__)

char *rtmp_path;

/**
 * 从队列中不断拉取RTMPPacket发送给流媒体服务器）
 */
void *pull_thread(void *arg) {
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
//    RTMP_EnableWrite(rtmp);
    //建立连接
    if (!RTMP_Connect(rtmp, NULL)) {
        LOGE("%s", "RTMP 连接失败");
        goto end;
    }
    if (!RTMP_ConnectStream(rtmp, 0)) { //连接流
        goto end;
    }
    LOGE("%s", "RTMP_ConnectStream");

    //拉流
    int nRead = 0, NRead = 0;
    int bufsize = 1024 * 1024;
    char *buf = (char *) malloc(bufsize);
//    FILE *fp_save = fopen("save.flv", "wb");
//    while (1) {
//        nRead = RTMP_Read(rtmp, buf, bufsize);
////        fwrite(buf, 1, nRead, fp_save);
//        NRead += nRead;
//        LOGE("Receive: %5dByte, Total: %5.2fkB\n",nRead,NRead*1.0/1024);
//    }
    RTMPPacket *packet = malloc(sizeof(RTMPPacket));
////    RTMPPacket_Alloc(packet, packet);
    while (1) {
        //接收

        int i = RTMP_ReadPacket(rtmp, packet);
        if (!i) {
            LOGE("RTMP 断开");
//            RTMPPacket_Free(packet);
            goto end;
        }
        if (packet) {
            LOGE("packet m_nBodySize:%d", packet->m_nBodySize);
//            LOGE("packet m_packetType:%d", packet->m_packetType);
            if(packet->m_packetType == 9) {
                LOGE("packet m_packetType:%d", packet->m_packetType);

            }
            RTMPPacket_Free(packet);
        }
    }
    end:
    LOGI("%s", "释放资源");
    RTMP_Close(rtmp);
    RTMP_Free(rtmp);
    return 0;
}


JNIEXPORT void JNICALL
Java_com_libwuwind_jni_NativePuller_startPull(JNIEnv *env, jobject instance, jstring url_) {
    const char *url = (*env)->GetStringUTFChars(env, url_, 0);

    rtmp_path = malloc(strlen(url) + 1);
    memset(rtmp_path, 0, strlen(url) + 1);
    memcpy(rtmp_path, url, strlen(url));
    //启动消费者线程（从队列中不断拉取RTMPPacket发送给流媒体服务器）
    pthread_t push_thread_id;
    pthread_create(&push_thread_id, NULL, pull_thread, NULL);


    (*env)->ReleaseStringUTFChars(env, url_, url);
}
