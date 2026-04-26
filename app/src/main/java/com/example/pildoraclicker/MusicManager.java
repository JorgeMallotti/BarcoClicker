package com.example.pildoraclicker;

import android.content.Context;
import android.media.MediaPlayer;

public class MusicManager {
    private static MediaPlayer mediaPlayer;
    private static int currentResId = -1;

    public static void play(Context context, int resId) {
        // If same music is already playing, do nothing to avoid restart/overlap
        if (mediaPlayer != null && currentResId == resId) {
            if (!mediaPlayer.isPlaying()) {
                mediaPlayer.start();
            }
            return;
        }

        // If different music or no music, stop current and start new
        stop();

        try {
            mediaPlayer = MediaPlayer.create(context.getApplicationContext(), resId);
            if (mediaPlayer != null) {
                mediaPlayer.setLooping(true);
                mediaPlayer.start();
                currentResId = resId;
            }
        } catch (Exception ignored) {
        }
    }

    public static void stop() {
        if (mediaPlayer != null) {
            try {
                if (mediaPlayer.isPlaying()) {
                    mediaPlayer.stop();
                }
            } catch (Exception e) {}
            try {
                mediaPlayer.release();
            } catch (Exception e) {}
            mediaPlayer = null;
            currentResId = -1;
        }
    }

    public static boolean isPlaying() {
        return mediaPlayer != null && mediaPlayer.isPlaying();
    }

    public static int getCurrentResId() {
        return currentResId;
    }
}