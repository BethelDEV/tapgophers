package com.gamez.tap_gopher.collections;

import android.content.Context;
import android.content.res.AssetManager;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Build;

import java.io.IOException;
import java.util.HashMap;

import funs.gamez.Consts;
import funs.gamez.Logz;

public class GameSoundCollection {
    private static final int MAXSIZE = 10;
    public SoundPool collection;
    private final HashMap<String, Integer> map;

    public GameSoundCollection() {
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            AudioAttributes attr = new AudioAttributes.Builder()
                    .setUsage(AudioAttributes.USAGE_GAME) // 设置音效使用场景
                    .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC).build(); // 设置音效的类型

            SoundPool.Builder spb = new SoundPool.Builder();
            spb.setMaxStreams(MAXSIZE);
            spb.setAudioAttributes(attr);    //转换音频格式
            collection = spb.build();      //创建SoundPool对象
        } else {
            collection = new SoundPool(MAXSIZE, AudioManager.STREAM_MUSIC, 0);
        }
//		collection = new SoundPool(MAXSIZE,AudioManager.STREAM_MUSIC,0);
        map = new HashMap<String, Integer>();
    }

    public void load(Context context, String[] fileNames, String keys[]) throws IOException {
        int id;
        AssetManager assetManager = context.getAssets();
        for (int i = 0; i < fileNames.length; i++) {
            id = collection.load(assetManager.openFd(fileNames[i]), 1);
            map.put(keys[i], id);
        }
    }

    public void load(Context context, String fileName) throws IOException {
        AssetManager assetManager = context.getAssets();
        int id = collection.load(assetManager.openFd(fileName), 1);
        map.put(fileName, id);
    }

    public void play(String key) {
        Integer id = map.get(key);
        if (id == null) {
            Logz.i("GameSoundCollection", "play id is null");
            return;
        }
        Logz.i("GameSoundCollection", "play id : "+ id);
        collection.play(id, Consts.defVolume, Consts.defVolume, 0, 0, 1);
    }

    public void release() {
        if (collection != null) {
            collection.autoPause();
            collection.release();
        }
        map.clear();
    }
}
