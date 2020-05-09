package com.libwuwind.player;

import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioTrack;
import android.view.Surface;

public class VideoUtils {

	public native static void decode(String input,String output);
	public native static void decodeAudio(String input,String output);
	
	public native static void play(String input, Surface surface);
	public native static void play2(String input, Surface surface, Surface surface2);
	public native static void init();
	public native static void crop(int left, int top, int right, int bottom, float rotate);

	public static AudioTrack createAudioTrack() {
		int streamType = AudioManager.STREAM_MUSIC;
		int sampleRateInHz = 44100;
		int channelConfig = AudioFormat.CHANNEL_OUT_STEREO;
		int audioFormat = AudioFormat.ENCODING_PCM_16BIT;
		int bufferSizeInBytes = AudioTrack.getMinBufferSize(sampleRateInHz, channelConfig, audioFormat);
		int mode = AudioTrack.MODE_STREAM;
		AudioTrack audioTrack = new AudioTrack(streamType, sampleRateInHz, channelConfig, audioFormat, bufferSizeInBytes, mode);
//        audioTrack.play();
//        audioTrack.write(audioData, offsetInBytes, sizeInBytes)
		return audioTrack;
	}

	static{
		System.loadLibrary("avutil-54");
		System.loadLibrary("swresample-1");
		System.loadLibrary("avcodec-56");
		System.loadLibrary("avformat-56");
		System.loadLibrary("swscale-3");
		System.loadLibrary("postproc-53");
		System.loadLibrary("avfilter-5");
		System.loadLibrary("avdevice-56");
		System.loadLibrary("myffmpeg");
	}
}
