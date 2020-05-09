//#include <stdlib.h>
//#include <stdio.h>
//#include <unistd.h>
//#include <pthread.h>
//#include <android/log.h>
//#include <android/log.h>
//
////编码
//#include "libavcodec/avcodec.h"
////封装格式处理
//#include "libavformat/avformat.h"
////像素处理
//#include "libswscale/swscale.h"
//
//#include "libavfilter/avfilter.h"
//#include "libavfilter/buffersrc.h"
//#include "libavfilter/buffersink.h"
//#include "libavutil/opt.h"
//
//#include "android/native_window.h"
//#include "android/native_window_jni.h"
//
//#include "libyuv.h"
//
//#define LOGE(FORMAT, ...) __android_log_print(ANDROID_LOG_ERROR, "wuhf",  FORMAT, ##__VA_ARGS__)
//#define LOGI(FORMAT, ...) __android_log_print(ANDROID_LOG_INFO, "wuhf",  FORMAT, ##__VA_ARGS__)
//
//#define MAX_STREAM 4
//
//
//
//struct Player {
//	AVFormatContext *input_format_ctx;
//	int video_stream_idx;
//	int audio_stream_idx;
//	AVCodecContext *codecCtx[MAX_STREAM];
//	ANativeWindow* window;
//	ANativeWindow* window2;
//	char useFilter;
//	AVFilterContext *buffersrc_ctx;
//	AVFilterContext *buffersink_ctx;
//	int showWidth;
//	int showHeight;
//	int rate;
//};
//
//struct Player *player;
//
//void init_input_format_ctx(struct Player *player,const char* input_cstr) {
//	//1.注册所有组件
//	av_register_all();
//	//封装格式上下文，统领全局的结构体，保存了视频文件封装格式的相关信息
//	AVFormatContext *pFormatCtx = avformat_alloc_context();
//    LOGE("%s", input_cstr);
//	//2.打开输入视频文件
//	if (avformat_open_input(&pFormatCtx, input_cstr, NULL, NULL) != 0) {
//		LOGE("%s", "无法打开输入视频文件");
//		return;
//	}
//
//	//3.获取视频文件信息
//	if (avformat_find_stream_info(pFormatCtx, NULL) < 0) {
//		LOGE("%s", "无法获取视频文件信息");
//		return;
//	}
//	int i = 0;
//	//number of streams
//	LOGE("nb_streams: %d", pFormatCtx->nb_streams);
//	for (; i < pFormatCtx->nb_streams; i++) {
//		LOGE("index: %d, codec_type:%d", i, pFormatCtx->streams[i]->codec->codec_type);
//		//流的类型
//		if (pFormatCtx->streams[i]->codec->codec_type == AVMEDIA_TYPE_VIDEO) {
//			player->video_stream_idx = i;
//			player->showWidth = pFormatCtx->streams[i]->codec->width;
//			player->showHeight = pFormatCtx->streams[i]->codec->height;
//			player->rate = pFormatCtx->streams[i]->r_frame_rate.num;
//			LOGE("w:%d, h:%d index:%d, rate:%d", player->showWidth, player->showHeight, i, player->rate);
//		} else if (pFormatCtx->streams[i]->codec->codec_type == AVMEDIA_TYPE_AUDIO ) {
//			player->audio_stream_idx = i;
//		}
//	}
//	player->input_format_ctx = pFormatCtx;
//	player->useFilter = 0;
//}
//
//void init_codec_ctx(struct Player* player, int index) {
//	AVCodecContext *codecCtx = player->input_format_ctx->streams[index]->codec;
//	AVCodec *codec = avcodec_find_decoder(codecCtx->codec_id);
//	if (NULL == codec) {
//		LOGE("%s", "找不到解码器\n");
//		return;
//	}
//	//5.打开解码器
//	if (avcodec_open2(codecCtx, codec, NULL) < 0) {
//		LOGE("%s", "解码器无法打开\n");
//		return;
//	}
//	player->codecCtx[index] = codecCtx;
//	LOGE("%s", "获取解码器\n");
//}
//
//void showAVFrame(AVFrame *filt_frame, struct Player *player) {
//	ANativeWindow *window = player->window;
//	ANativeWindow_Buffer outBuffer;
//	AVFrame *pFrameRGBA = av_frame_alloc();
//	ANativeWindow_setBuffersGeometry(window, player->showWidth, player->showHeight, WINDOW_FORMAT_RGBA_8888);
//	ANativeWindow_lock(window, &outBuffer, NULL);
//						//初始化缓冲区
//				//		uint8_t *out_buffer = (uint8_t *) av_malloc(
//				//					avpicture_get_size(AV_PIX_FMT_RGBA, pCodecCtx->width,
//				//							pCodecCtx->height));
//	//LOGE("width:%d,height:%d\n",  pCodecCtx->width, pCodecCtx->height);
//	avpicture_fill((AVPicture *) pFrameRGBA, outBuffer.bits,
//			AV_PIX_FMT_RGBA, player->showWidth, player->showHeight);
//	I420ToARGB(filt_frame->data[0], filt_frame->linesize[0], filt_frame->data[2],
//			filt_frame->linesize[2], filt_frame->data[1], filt_frame->linesize[1],
//			pFrameRGBA->data[0], pFrameRGBA->linesize[0], player->showWidth,
//			player->showHeight);
//	ANativeWindow_unlockAndPost(window);
//}
//
//void decode_video(struct Player *player, AVPacket *packet) {
//
//	AVCodecContext *pCodecCtx = player->codecCtx[player->video_stream_idx];
//
//	int got_picture, ret;
//	int frame_count = 0;
//	//AVFrame用于存储解码后的像素数据(YUV)
//	//内存分配
//	AVFrame *pFrame = av_frame_alloc();
//	AVFrame *filt_frame = av_frame_alloc();
//	//YUV420
//
//	AVFrame *pFrameRGBA2 = av_frame_alloc();
//
//	ANativeWindow_Buffer outBuffer2;
//	//7.解码一帧视频压缩数据，得到视频像素数据
//	ret = avcodec_decode_video2(pCodecCtx, pFrame, &got_picture, packet);
//	if (ret < 0) {
//		LOGE("%s", "解码错误");
//		return;
//	}
//	//为0说明解码完成，非0正在解码  0没有可以解压的数据帧
//	if (got_picture) {
//		frame_count++;
//		LOGI("解码第%d帧 got_picture  %d", frame_count, got_picture);
//		//把解码后视频帧添加到filter graph
//		if(player->useFilter) {
//			LOGI("showAVFrame filt_frame");
//			if (av_buffersrc_add_frame_flags(player->buffersrc_ctx, pFrame, AV_BUFFERSRC_FLAG_KEEP_REF) < 0) {
//				LOGE("Error while feeding the filter_graph\n");
//				return;
//			}
//			//把滤波后的视频帧从filter graph取出来
//			ret = av_buffersink_get_frame(player->buffersink_ctx, filt_frame);
//			if (ret >= 0){
//				showAVFrame(filt_frame, player);
//			}
//		} else {
//			showAVFrame(pFrame, player);
//		}
////		if(NULL != player->window2) {
////			ANativeWindow *window2 = player->window2;
////			ANativeWindow_setBuffersGeometry(window2, pCodecCtx->width, pCodecCtx->height, WINDOW_FORMAT_RGBA_8888);
////			ANativeWindow_lock(window2, &outBuffer2, NULL);
////			memcpy(outBuffer2.bits, outBuffer.bits, avpicture_get_size(AV_PIX_FMT_RGBA, pCodecCtx->width,
////							pCodecCtx->height));
////			ANativeWindow_unlockAndPost(window2);
////		}
//		usleep(1000 * 1000 / player->rate);
//	}
//	av_frame_free(&pFrame);
//	av_frame_free(&filt_frame);
//	//av_frame_free(&pFrameRGBA);
//	av_frame_free(&pFrameRGBA2);
//}
//
//
////子线程解码
//void* decode_data(void* arg) {
//	struct Player* player = arg;
//	//缓冲区，开辟空间
//	AVPacket* packet = av_malloc(sizeof(AVPacket));
//	//6.一帧一帧的读取压缩数据
//	int ret = av_read_frame(player->input_format_ctx, packet);
//	if(ret < 0) {
//		LOGI("读取压缩数据错误");
//		return -1;
//	}
//	do {
//		if (packet->stream_index == 0) {
//			decode_video(player, packet);
//		}
//		av_free_packet(packet);
//	} while (av_read_frame(player->input_format_ctx, packet) >= 0);
//	return 0;
//}
//
//void decode_video_prepare(JNIEnv *env, struct Player *player, jobject surface, jobject surface2) {
//	ANativeWindow* window = ANativeWindow_fromSurface(env, surface);
//	player->window = window;
//	if(NULL != surface2){
//		ANativeWindow* window2 = ANativeWindow_fromSurface(env, surface2);
//		player->window2 = window2;
//	}
//}
//
//void destroy_player(struct Player *player) {
//
//	ANativeWindow_release(player->window);
//	int i = 0;
//	for(i=0; i<MAX_STREAM; i++) {
//		if(NULL != player->codecCtx[i]) {
//			LOGE("%d", player->codecCtx[i]);
////			avcodec_close(player->codecCtx[i]);
//		}
//	}
//
//	avformat_free_context(player->input_format_ctx);
//}
//
//JNIEXPORT void JNICALL Java_com_libwuwind_player_VideoUtils_play(JNIEnv * env,
//		jclass jcls, jstring input_jstr, jobject surface) {
//    //需要转码的视频文件(输入的视频文件)
//    const char* input_cstr = (*env)->GetStringUTFChars(env, input_jstr, NULL);
//
//    player = malloc(sizeof(struct Player));
//
//    init_input_format_ctx(player, input_cstr);
//    init_codec_ctx(player, player->video_stream_idx);
////	init_codec_ctx(player, player->audio_stream_idx);
//    decode_video_prepare(env, player, surface, NULL);
//
//    //初始化滤波器
////	const char *filter_descr = "scale=iw/2:ih/2";
////	char *filter_descr = "rotate=90";//旋转90°
//    avfilter_register_all();
////	const char *filter_descr = "crop=iw/2:ih/2:iw/4:ih/4";
////	AVCodecContext *pCodecCtx = player->codecCtx[player->video_stream_idx];
////	player->showWidth = pCodecCtx->width / 2;
////	player->showHeight = pCodecCtx->height / 2;
////	if(init_filters(filter_descr, player) < 0) {
////		LOGE("%s", "初始化滤镜失败");
////		return;
////	}
//
//    pthread_t tid;
//    pthread_create(&tid, NULL, decode_data, player);
//
////	pthread_join(tid, NULL);
////
////
////	(*env)->ReleaseStringUTFChars(env, input_jstr, input_cstr);
////	destroy_player(player);
//
//    LOGI("avcodec_close");
//}
//
//
//JNIEXPORT void  Java_com_libwuwind_player_VideoUtils_play2(JNIEnv * env,
//		jclass jcls, jstring input_jstr, jobject surface, jobject surface2) {
//	//需要转码的视频文件(输入的视频文件)
//	const char* input_cstr = (*env)->GetStringUTFChars(env, input_jstr, NULL);
//
//	player = malloc(sizeof(struct Player));
//
//	init_input_format_ctx(player, input_cstr);
//	init_codec_ctx(player, player->video_stream_idx);
////	init_codec_ctx(player, player->audio_stream_idx);
//	decode_video_prepare(env, player, surface, surface2);
//
//	//初始化滤波器
////	const char *filter_descr = "scale=iw/2:ih/2";
////	char *filter_descr = "rotate=90";//旋转90°
//	avfilter_register_all();
////	const char *filter_descr = "crop=iw/2:ih/2:iw/4:ih/4";
////	AVCodecContext *pCodecCtx = player->codecCtx[player->video_stream_idx];
////	player->showWidth = pCodecCtx->width / 2;
////	player->showHeight = pCodecCtx->height / 2;
////	if(init_filters(filter_descr, player) < 0) {
////		LOGE("%s", "初始化滤镜失败");
////		return;
////	}
//
//	pthread_t tid;
//	pthread_create(&tid, NULL, decode_data, player);
//
////	pthread_join(tid, NULL);
////
////
////	(*env)->ReleaseStringUTFChars(env, input_jstr, input_cstr);
////	destroy_player(player);
//
//	LOGI("avcodec_close");
//}
//
//JNIEXPORT void JNICALL Java_com_libwuwind_player_VideoUtils_VideoUtils_crop
//(JNIEnv *env, jclass jcls, jint left, jint top, jint right, jint bottom, jfloat rotate) {
//
//	crop(left, top, right, bottom, rotate);
//}
//
//int crop(int left, int top, int right, int bottom, double rotate) {
//	AVCodecContext *pCodecCtx = player->codecCtx[player->video_stream_idx];
//	int width = pCodecCtx->width;
//	int height = pCodecCtx->height;
//	int x = left;
//	int y = top;
//	width -= (left + right);
//	height -= (top + bottom);
//	char filter_descr[512];
//	snprintf(filter_descr, sizeof(filter_descr), "crop=%d:%d:%d:%d,rotate=%lf:ow=iw:oh=ih:c=none",
//			width, height, x, y, rotate);
//	LOGE("crop=%d:%d:%d:%d", width, height, x, y);
//	player->showWidth = width;
//	player->showHeight = height;
//	if(init_filters(filter_descr, player) < 0) {
//	    LOGE("%s", "初始化滤镜失败");
//	    return -1;
//	}
//	player->useFilter = 1;
//	return 0;
//}
//
//
////初始化滤波器
//int init_filters(const char *filters_descr, struct Player *player) {
//	AVFilterGraph *filter_graph;
//	AVFilterContext *buffersink_ctx;
//	AVFilterContext *buffersrc_ctx;
//
//    char args[512];
//    int ret = 0;
//    AVFilter *buffersrc  = avfilter_get_by_name("buffer");
//    AVFilter *buffersink = avfilter_get_by_name("buffersink");
//    AVFilterInOut *outputs = avfilter_inout_alloc();
//    AVFilterInOut *inputs  = avfilter_inout_alloc();
//    AVRational time_base = player->input_format_ctx->streams[player->video_stream_idx]->time_base;
//    enum AVPixelFormat pix_fmts[] = { AV_PIX_FMT_YUV420P, AV_PIX_FMT_NONE };
//
//    filter_graph = avfilter_graph_alloc();
//    if (!outputs || !inputs || !filter_graph) {
//        ret = AVERROR(ENOMEM);
//        goto end;
//    }
//    AVCodecContext *pCodecCtx = player->codecCtx[player->video_stream_idx];
//
//    /* buffer video source: the decoded frames from the decoder will be inserted here. */
//    snprintf(args, sizeof(args),
//                 "video_size=%dx%d:pix_fmt=%d:time_base=%d/%d:pixel_aspect=%d/%d",
//                 pCodecCtx->width, pCodecCtx->height, pCodecCtx->pix_fmt,
//                 time_base.num, time_base.den,
//                 pCodecCtx->sample_aspect_ratio.num, pCodecCtx->sample_aspect_ratio.den);
//
//    ret = avfilter_graph_create_filter(&buffersrc_ctx, buffersrc, "in",
//                                       args, NULL, filter_graph);
//    if (ret < 0) {
//        LOGE("Cannot create buffer source ret:%d\n", ret);
//        goto end;
//    }
//
//    /* buffer video sink: to terminate the filter chain. */
//    ret = avfilter_graph_create_filter(&buffersink_ctx, buffersink, "out",
//                                       NULL, NULL, filter_graph);
//    if (ret < 0) {
//        LOGE("Cannot create buffer sink\n");
//        goto end;
//    }
//
//    ret = av_opt_set_int_list(buffersink_ctx, "pix_fmts", pix_fmts,
//                              AV_PIX_FMT_NONE, AV_OPT_SEARCH_CHILDREN);
//    if (ret < 0) {
//        LOGE("Cannot set output pixel format\n");
//        goto end;
//    }
//
//    outputs->name       = av_strdup("in");
//    outputs->filter_ctx = buffersrc_ctx;
//    outputs->pad_idx    = 0;
//    outputs->next       = NULL;
//
//    inputs->name       = av_strdup("out");
//    inputs->filter_ctx = buffersink_ctx;
//    inputs->pad_idx    = 0;
//    inputs->next       = NULL;
//
//    if ((ret = avfilter_graph_parse_ptr(filter_graph, filters_descr,
//                                        &inputs, &outputs, NULL)) < 0)
//        goto end;
//
//    if ((ret = avfilter_graph_config(filter_graph, NULL)) < 0)
//        goto end;
//
//    end:
//    avfilter_inout_free(&inputs);
//    avfilter_inout_free(&outputs);
//
//    player->buffersrc_ctx = buffersrc_ctx;
//    player->buffersink_ctx = buffersink_ctx;
//    return ret;
//}
