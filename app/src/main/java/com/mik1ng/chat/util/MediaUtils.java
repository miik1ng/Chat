package com.mik1ng.chat.util;

import android.media.MediaPlayer;
import android.media.MediaRecorder;

import java.io.IOException;

public class MediaUtils {

    private static MediaRecorder mediaRecorder;

    /**
     * 开始录音
     * @param filePath
     */
    public static void startRecording(String filePath) {
        if (mediaRecorder == null) {
            mediaRecorder = new MediaRecorder();
        }
        mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);
        mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AAC);
        mediaRecorder.setOutputFile(filePath);
        try {
            mediaRecorder.prepare();
            mediaRecorder.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 停止录音
     */
    public static void stopRecording() {
        if (mediaRecorder != null) {
            mediaRecorder.stop();
            mediaRecorder.release();
            mediaRecorder = null;
        }
    }

    private static MediaPlayer mediaPlayer;
    /**
     * 播放录音文件
     * @param filePath
     */
    public static void playRecording(String filePath) {
        if (mediaPlayer == null) {
            mediaPlayer = new MediaPlayer();
            try {
                // 设置要播放的音频文件路径
                mediaPlayer.setDataSource(filePath);
                //完成播放监听
                mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                    @Override
                    public void onCompletion(MediaPlayer mediaPlayer) {
                        MediaUtils.mediaPlayer = null;
                    }
                });
                // 准备播放音频文件
                mediaPlayer.prepare();
                // 播放音频文件
                mediaPlayer.start();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
