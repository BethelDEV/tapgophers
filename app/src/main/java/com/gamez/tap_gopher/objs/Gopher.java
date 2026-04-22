package com.gamez.tap_gopher.objs;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;

import com.gamez.tap_gopher.GameSize;
import com.gamez.tap_gopher.GameSurfaceView;
import com.gamez.tap_gopher.collections.StarCollection;

import funs.gamez.Consts;

public class Gopher {

    public enum ACT {
        UP, DOWN, STAND, DIE
    }

//    public static final float CAMP_H = GameSize.getNewY(60);//
    private static final float DIE_DURATION = 7, //20,
            IN_SPE = GameSize.getNewY(1);
    private static final float SPE_ADD = GameSize.getNewX(0.1f), MAX_SPE = GameSize.getNewX(3);

    public boolean islive;

    private float y;
    private final float x, m_w, m_h;
    private static float spe = IN_SPE;
    private final static float spe_die_down = GameSize.getNewY(4);

    private final Bitmap mole_bmp_live;
    private final Bitmap mole_bmp_die;
    private final Hole hole;
    private ACT state;
    private int die_time;
    private float clip_r_x1, clip_r_x2, clip_r_y1, clip_r_y2;
    public boolean eat_flag;
    private final GameSurfaceView gsv;

    public Gopher(Hole hole, Bitmap mole_bmp_live, Bitmap mole_bmp_die,
                  GameSurfaceView gsv) {

        this.mole_bmp_live = mole_bmp_live;
        this.mole_bmp_die = mole_bmp_die;
        this.hole = hole;
        this.gsv = gsv;

        m_w = mole_bmp_live.getWidth();
        m_h = mole_bmp_live.getHeight();
//        x = hole.getX() + hole.getH_W() / 2 - m_w *3 / 5;
        x = hole.getX() + hole.getH_W() / 2 - m_w / 2;
        y = hole.getY() + hole.getH_H() / 2;

        calculateBorder();

        islive = true;
        state = ACT.STAND;
        die_time = 0;
        eat_flag = false;
    }

    public void draw(Canvas canvas, Paint paint) {
        if (state != ACT.DIE) {
            canvas.save();
            canvas.clipRect(clip_r_x1, clip_r_y1, clip_r_x2, clip_r_y2);
            canvas.drawBitmap(mole_bmp_live, x, y, paint);
            canvas.restore();
            // canvas.drawRect(x,y,clip_r_x2,clip_r_y2, paint);
        } else {
            canvas.save();
            canvas.clipRect(clip_r_x1, clip_r_y1, clip_r_x2, clip_r_y2);
            canvas.drawBitmap(mole_bmp_die, x, y, paint);
            canvas.restore();
        }

    }

    public void logic() {
        if (state == ACT.UP) {
            if (y - spe >= clip_r_y1) {
                y -= spe;
            } else {
                y = clip_r_y1;
                state = ACT.DOWN;
            }
        } else if (state == ACT.DOWN) {
            if (y + spe <= clip_r_y2) {
                y += spe;
            } else {
                y = clip_r_y2;
                state = ACT.STAND;
                StarCollection.reduce();
            }
        } else if (state == ACT.DIE) {
            if (islive == true) {
                if (die_time <= DIE_DURATION) {
                    die_time++;
                    if (y + spe <= clip_r_y2) {
                        y += spe_die_down;
                    }
                } else {
                    die_time = 0;
                    y = clip_r_y2;
                    // islive=false;
                    state = ACT.STAND;
                }
            }
        } else if (state == ACT.STAND) {
        }

        calculateBorder();
    }

    /**
     * clip_r_y1 : 顶部位置
     * clip_r_y2 : 底部位置
     */
    private void calculateBorder() {
        clip_r_x1 = x;
        clip_r_y1 = hole.getY() + hole.getH_H() /4 - (m_h * 2 / 3);
//        clip_r_y1 = hole.getY() + hole.getH_H() / 2 - (m_h * 2 / 3);
        clip_r_x2 = x + m_w;
        clip_r_y2 = hole.getY() + hole.getH_H() *10 / 21;
//        clip_r_y2 = hole.getY() + hole.getH_H() / 2;
    }

    public void die() {
        setState(ACT.DIE);
        play();
    }

    public void play() {
        gsv.gs_collection.play(Consts.SOUND_BEHIT);
    }

    public void setState(ACT state) {
        this.state = state;
    }

    public ACT getState() {
        return state;
    }

    public void reset() {
        y = clip_r_y2;
        state = ACT.STAND;
    }

    public Rect getTouchRect() {
        return new Rect((int) x, (int) y, (int) clip_r_x2, (int) clip_r_y2);
    }

    public static void increaSpe() {
        float result = spe + SPE_ADD;
        if (result <= MAX_SPE) {
            spe = result;
        }
//        if (spe + SPE_ADD <= MAX_SPE) {
//            spe += SPE_ADD;
//        }
    }

    public static void resetSpe() {
        spe = IN_SPE;
    }
}
