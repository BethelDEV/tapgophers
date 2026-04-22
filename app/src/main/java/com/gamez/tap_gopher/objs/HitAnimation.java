package com.gamez.tap_gopher.objs;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;

import com.gamez.tap_gopher.GameSurfaceView;

import funs.gamez.Consts;
import funs.gamez.ResourceKeeper;

public class HitAnimation {

//    private HashMap<String, Bitmap> map;
    public int draw_step;
    public float x, y;
    private final float bmg_w, bmg_h, draw_x, draw_y;
    public boolean finish;
    private final GameSurfaceView gsv;

    private ResourceKeeper mResKeeper;

    public HitAnimation(int x, int y, GameSurfaceView gsv, ResourceKeeper resKeeper) {
//		this.x=GameSize.getNewX(x);
//		this.y=GameSize.getNewY(y);
        this.x = x;
        this.y = y;
        this.gsv = gsv;
        this.mResKeeper = resKeeper;
        final Bitmap hit = resKeeper.getBitmap(Consts.hit3_bmg);
        bmg_w = hit.getWidth();
        bmg_h = hit.getHeight();

        draw_x = x - bmg_w / 2;
        draw_y = y - bmg_h / 2;
        draw_step = 1;
        finish = false;
    }

    public void draw(Canvas canvas, Paint paint) {
        if (finish) return;

//        if (draw_step <= 4) {
        if (draw_step <= 3) {
            canvas.drawBitmap(mResKeeper.getBitmap(Consts.hit3_bmg), draw_x, draw_y, paint);
            draw_step++;
        } else if (draw_step <= 4) {
            canvas.drawBitmap(mResKeeper.getBitmap(Consts.hit4_bmg), draw_x, draw_y, paint);
            draw_step++;
        } else {
            canvas.drawBitmap(mResKeeper.getBitmap(Consts.hit4_bmg), draw_x, draw_y, paint);
            draw_step = 1;
            finish = true;
        }
    }

    public void play() {
//		gsv.gs_collection.play("HIT");
    }

}
