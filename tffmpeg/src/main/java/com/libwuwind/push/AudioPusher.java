package com.libwuwind.push;

import android.media.AudioRecord;
import android.media.MediaRecorder;
import android.util.Log;

import com.libwuwind.jni.NativePusher;
import com.libwuwind.push.param.AudioParam;

import static android.media.AudioFormat.CHANNEL_IN_MONO;
import static android.media.AudioFormat.CHANNEL_IN_STEREO;
import static android.media.AudioFormat.ENCODING_PCM_16BIT;
import static android.media.AudioRecord.STATE_UNINITIALIZED;

public class AudioPusher implements Pusher {
    private String TAG = AudioPusher.class.getSimpleName();
    private AudioRecord audioRecord;
    private boolean start;
    private int bufferSizeInBytes;
    private AudioParam audioParam;
    private NativePusher nativePusher;
    public AudioPusher(AudioParam audioParam, NativePusher nativePusher) {
        this.audioParam = audioParam;
        this.nativePusher = nativePusher;
        int audioSource = MediaRecorder.AudioSource.MIC;
        int sampleRateInHz = audioParam.sampleRateInHz;
        int channelConfig = audioParam.channel == 1 ? CHANNEL_IN_MONO : CHANNEL_IN_STEREO;
        int audioFormat = ENCODING_PCM_16BIT;
        bufferSizeInBytes = AudioRecord.getMinBufferSize(sampleRateInHz, channelConfig, audioFormat);
        audioRecord = new AudioRecord(audioSource, sampleRateInHz, channelConfig, audioFormat,
                bufferSizeInBytes);
    }

    @Override
    public void starPush() {
        if (start)
            return;
        if (audioRecord.getState() == STATE_UNINITIALIZED) {
            Log.e(TAG, "mic error ");
            return;
        }
        nativePusher.setAudioOptions(audioParam.sampleRateInHz, audioParam.channel);
        start = true;
        final byte[] buff = new byte[bufferSizeInBytes];
        new Thread(new Runnable() {
            @Override
            public void run() {
                audioRecord.startRecording();
                while (start) {
                    int read = audioRecord.read(buff, 0, bufferSizeInBytes);
                    if (read <= 0)
                        break;
                    Log.e(TAG, "mic read " + read);
                    nativePusher.fireAudio(buff, read);
                }
            }
        }).start();
    }

    @Override
    public void stopPush() {
        start = false;
    }
}
