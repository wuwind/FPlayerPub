#include <stdlib.h>
#include <stdio.h>
#include <unistd.h>
#include <pthread.h>
#include <android/log.h>
#include "com_fg_tffmpeg_InitUtil.h"

#define LOGE(FORMAT, ...) __android_log_print(ANDROID_LOG_ERROR, "wuhf",  FORMAT, ##__VA_ARGS__)
JavaVM* javaVM;
jmethodID getUuidMd;
jclass initCls;
JNIEXPORT jint JNICALL JNI_OnLoad(JavaVM* vm, void* reserved) {
	LOGE("%s", "JNI_OnLoad");
	javaVM = vm;
	JNIEnv *env;
	(*javaVM)->GetEnv(javaVM, (void**) &env, JNI_VERSION_1_4);
	jclass tmpCls = (*env)->FindClass(env, "com/fg/tffmpeg/InitUtil");
	initCls = (*env)->NewGlobalRef(env, tmpCls);
	getUuidMd = (*env)->GetStaticMethodID(env, initCls, "getUuid",
			"()Ljava/lang/String;");

//	jobject uid = (*env)->CallStaticObjectMethod(env, initCls, getUuidMd);
//	const char *cuid = (const char *) ((*env)->GetStringUTFChars(env, uid,
//			JNI_FALSE));
//	LOGE("%s", cuid);
	return JNI_VERSION_1_4;
}

void* t_fun(void* arg) {
	//LOGE("%s", arg);
	JNIEnv *env;

	(*javaVM)->AttachCurrentThread(javaVM, &env, NULL);

	if (NULL == getUuidMd) {
		LOGE("%s", "getUuidMd is null");
	} else {
		LOGE("%s", "getUuidMd is not null");
	}

	jobject uid = (*env)->CallStaticObjectMethod(env, initCls, getUuidMd);
	const char *cuid = (const char *) ((*env)->GetStringUTFChars(env, uid,
			JNI_FALSE));
	LOGE("%s", cuid);
	(*javaVM)->DetachCurrentThread(javaVM);
}

JNIEXPORT void JNICALL Java_com_fg_tffmpeg_InitUtil_init(JNIEnv *env,
		jclass jcls) {
//	LOGE("%s", "init");
	jclass tmpCls = (*env)->FindClass(env, "com/fg/tffmpeg/InitUtil");
	initCls = (*env)->NewGlobalRef(env,tmpCls);
//		getUuidMd = (*env)->GetStaticMethodID(env, initCls, "getUuid", "()Ljava/lang/String;");
//	initCls = (*env)->FindClass(env, "com/fg/tffmpeg/InitUtil");
	pthread_t tid;
	pthread_create(&tid, NULL, t_fun, (void*) initCls);
	pthread_join(tid, NULL);
}

