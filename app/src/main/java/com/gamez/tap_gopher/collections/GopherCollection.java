package com.gamez.tap_gopher.collections;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;

import com.gamez.tap_gopher.objs.Hole;
import com.gamez.tap_gopher.GameSurfaceView;
import com.gamez.tap_gopher.objs.Gopher;

import java.util.LinkedList;
import java.util.List;
import java.util.Random;


public class GopherCollection {
    //	public static final int MOLECOUNT=HoleCollection.HOLECOUNT;
    public static List<Gopher> collection;
    private final Bitmap mole_live_bmp, mole_die_bmp;
    private final HoleCollection h_col;

    private final GameSurfaceView gsv;
    private final Random random = new Random();

    public GopherCollection(Bitmap mole_live_bmp, Bitmap mole_die_bmp, HoleCollection h_col, GameSurfaceView gsv) {
        this.mole_live_bmp = mole_live_bmp;
        this.mole_die_bmp = mole_die_bmp;
        this.h_col = h_col;
        this.gsv = gsv;

        collection = new LinkedList<Gopher>();
    }

    public void initialize() {
        for (Hole hole : HoleCollection.collection) {
            collection.add(new Gopher(hole, mole_live_bmp, mole_die_bmp, gsv));
        }
    }

    public void reinitialize() {
        for (Gopher mole : collection) {
            mole.reset();
        }
    }


    public void draw(Canvas canvas, Paint paint) {
        for (Gopher gopher : collection) {
            gopher.draw(canvas, paint);
        }
    }

    public void logic() {
        for (Gopher mole : collection) {
            mole.logic();
        }
    }

    public void ran_up() {
        int num = random.nextInt(collection.size() - 1);//0~MOLECOUNT-1
        Gopher gopher = collection.get(num);
        if (gopher.islive && gopher.getState() == Gopher.ACT.STAND) {
            gopher.setState(Gopher.ACT.UP);
//			Log.i("moleCollection"," "+num);
        } else {
//			ran_up();//
        }
    }

    public void increaSpe() {
        Gopher.increaSpe();
    }

    public void resetSpe() {
        Gopher.resetSpe();
    }

}
