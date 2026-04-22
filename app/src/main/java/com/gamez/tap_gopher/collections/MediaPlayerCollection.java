package com.gamez.tap_gopher.collections;

import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.media.MediaPlayer;

import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;

import funs.gamez.Consts;
import funs.gamez.Logz;

public class MediaPlayerCollection {
    private static final String TAG = "MediaPlayerCollection";

    private final HashMap<String, MediaPlayer> map;
    //    private Intent intent;
    private float mLeftVolume = Consts.defVolume;
    private float mRightVolume = Consts.defVolume;

    public MediaPlayerCollection() {
        map = new HashMap<String, MediaPlayer>();
    }

    public void load(Context context, String[] fileNames, String[] keys) throws IOException {
        AssetManager assetManager = context.getAssets();
        MediaPlayer player;
        for (int i = 0; i < fileNames.length; i++) {
            player = new MediaPlayer();
            // https://codeantenna.com/a/Nt6OzFJeC0   报错：java.io.IOException: Prepare failed.: status=0x1。
            AssetFileDescriptor fileDescriptor = assetManager.openFd(fileNames[i]);
            player.setDataSource(fileDescriptor.getFileDescriptor(), fileDescriptor.getStartOffset(), fileDescriptor.getLength());
//            player.setDataSource(assetManager.openFd(fileNames[i]).getFileDescriptor());
            player.setLooping(true);
            map.put(keys[i], player);
            try {
                player.prepare();
                player.setVolume(mLeftVolume, mRightVolume);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        //
//		assetManager.close();
    }

    public void load(Context context, String fileName) throws IOException {
        AssetManager assetManager = context.getAssets();
        MediaPlayer player = new MediaPlayer();
        // https://codeantenna.com/a/Nt6OzFJeC0   报错：java.io.IOException: Prepare failed.: status=0x1。
        AssetFileDescriptor fileDescriptor = assetManager.openFd(fileName);
        player.setDataSource(fileDescriptor.getFileDescriptor(), fileDescriptor.getStartOffset(), fileDescriptor.getLength());
        player.setLooping(true);
        map.put(fileName, player);
        try {
            player.prepare();
            player.setVolume(mLeftVolume, mRightVolume);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 设置背景音乐的音量
     */
    public void setBackgroundVolume(float volume) {
        this.mLeftVolume = this.mRightVolume = volume;

        Collection<MediaPlayer> list = map.values();
        if (!list.isEmpty()) {
            for (MediaPlayer player : list) {
                player.setVolume(this.mLeftVolume, this.mRightVolume);
            }
        }
    }

    public void play(String key) {
        MediaPlayer player;
        player = map.get(key);
        if (player == null) {
            Logz.i(TAG, "play, MediaPlayer is null: " + key);
            return;
        }
        player.setLooping(true);
        player.start();
//        Logz.i(TAG, "play: "+key);
    }

    public void pause(String key) {
        MediaPlayer player;
        player = map.get(key);
        if (null != player) {
            try {
                if (player.isPlaying()) {
                    player.pause();
                }
            } catch (IllegalStateException ignored) {
            }
        }
//        if (null != player && player.isPlaying()) {
//            player.pause();
//        }

//        Logz.i(TAG, "pause: "+key);
    }

    public void release() {
        Collection<MediaPlayer> list = map.values();
        if (!list.isEmpty()) {
            for (MediaPlayer player : list) {
                if (null != player) {
                    if (player.isPlaying()) player.stop();

                    player.release();
                }
            }
        }
        map.clear();
    }

    public void stop(String key) {
        MediaPlayer player;
        player = map.get(key);
        if (null != player && player.isPlaying()) {
            player.stop();
        }

    }


}
