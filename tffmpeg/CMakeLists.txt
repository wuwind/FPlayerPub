#CMake版本
cmake_minimum_required(VERSION 3.6.0)
#set 设置变量 变量的使用 ${DIR}
set(INCLUDE_DIR src/main/jni/include)

#头文件的加载
include_directories(${INCLUDE_DIR} ${INCLUDE_DIR}/ffmpeg ${INCLUDE_DIR}/libyuv ${INCLUDE_DIR}/x264 ${INCLUDE_DIR}/librtmp ${INCLUDE_DIR}/faac)

#加载动态库配置 add_library
set(DIR ${CMAKE_SOURCE_DIR}/src/main/jni/${ANDROID_ABI})

add_library(x264 STATIC IMPORTED)
set_target_properties(x264
        PROPERTIES IMPORTED_LOCATION
        ${DIR}/libx264.a)

add_library(rtmp STATIC IMPORTED)
set_target_properties(rtmp
        PROPERTIES IMPORTED_LOCATION
        ${DIR}/librtmp.a)

add_library(faac STATIC IMPORTED)
set_target_properties(faac
        PROPERTIES IMPORTED_LOCATION
        ${DIR}/libfaac.a)

add_library(avcodec-56 SHARED IMPORTED)
set_target_properties(avcodec-56
        PROPERTIES IMPORTED_LOCATION
        ${DIR}/libavcodec-56.so)

add_library(avdevice-56 SHARED IMPORTED)
set_target_properties(avdevice-56
        PROPERTIES IMPORTED_LOCATION
        ${DIR}/libavdevice-56.so)

add_library(avfilter-5 SHARED IMPORTED)
set_target_properties(avfilter-5
        PROPERTIES IMPORTED_LOCATION
        ${DIR}/libavfilter-5.so)

add_library(avformat-56 SHARED IMPORTED)
set_target_properties(avformat-56
        PROPERTIES IMPORTED_LOCATION
        ${DIR}/libavformat-56.so)

add_library(avutil-54 SHARED IMPORTED)
set_target_properties(avutil-54
        PROPERTIES IMPORTED_LOCATION
        ${DIR}/libavutil-54.so)

add_library(postproc-53 SHARED IMPORTED)
set_target_properties(postproc-53
        PROPERTIES IMPORTED_LOCATION
        ${DIR}/libpostproc-53.so)

add_library(swresample-1 SHARED IMPORTED)
set_target_properties(swresample-1
        PROPERTIES IMPORTED_LOCATION
        ${DIR}/libswresample-1.so)

add_library(swscale-3 SHARED IMPORTED)
set_target_properties(swscale-3
        PROPERTIES IMPORTED_LOCATION
        ${DIR}/libswscale-3.so)

add_library(yuv SHARED IMPORTED)
set_target_properties(yuv
        PROPERTIES IMPORTED_LOCATION
        ${DIR}/libyuv.so)

#添加的cpp文件
#add_library(myffmpeg SHARED src/main/jni/myffmpeg.c)

# 设置加载源文件路径 my_source
file(GLOB my_source src/main/jni/*.cpp src/main/jni/*.c)

add_library(
        myffmpeg
        SHARED
        src/main/jni/queue.c
        src/main/jni/m_player.c
        src/main/jni/media_player.c
        src/main/jni/push.c
        src/main/jni/pull.c
        src/main/jni/PushStream.c
        src/main/jni/queue.c
        src/main/jni/queue0.c
#        ${my_source}
)

#log
find_library(log-lib log)
find_library(android-lib android)

#生成的目标so
target_link_libraries(
        myffmpeg
        x264
        rtmp
        faac
        avcodec-56
        avdevice-56
        avfilter-5
        avformat-56
        avutil-54
        postproc-53
        swresample-1
        swscale-3
        yuv
        ${log-lib}
        ${android-lib})