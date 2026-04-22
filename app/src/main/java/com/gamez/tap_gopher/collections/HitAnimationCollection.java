package com.gamez.tap_gopher.collections;

import android.graphics.Canvas;
import android.graphics.Paint;

import com.gamez.tap_gopher.objs.HitAnimation;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;


public class HitAnimationCollection {

    private static List<HitAnimation> collection;

    public HitAnimationCollection() {
        collection = new LinkedList<HitAnimation>();
    }

    public void draw(Canvas canvas, Paint paint) {
        for (HitAnimation ha : collection) {
            ha.draw(canvas, paint);
        }
    }

    public void clear() {
        if (!collection.isEmpty()) {
            collection.clear();
        }
    }

    public void logic() {
        Iterator<HitAnimation> i = collection.iterator();
        List<HitAnimation> dle_list = new LinkedList<HitAnimation>();
        while (i.hasNext()) {
            HitAnimation ha = i.next();
            if (ha.finish) {
                dle_list.add(ha);
            }
        }

        // fix java.util.ConcurrentModificationException
        if (!dle_list.isEmpty()) {
            synchronized (HitAnimationCollection.class) {
                collection.removeAll(dle_list);
            }
        }

    }

    public void add(HitAnimation ha) {
        collection.add(ha);
        ha.play();
    }
}
