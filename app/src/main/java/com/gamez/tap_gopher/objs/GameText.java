package com.gamez.tap_gopher.objs;


import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.text.Layout;
import android.text.StaticLayout;
import android.text.TextPaint;

import com.gamez.tap_gopher.GameSize;
import com.gamez.tap_gopher.GameSurfaceView;

import java.util.HashMap;

public class GameText {
    private static int COLOR = Color.BLACK, TEXTSIZE = (int) GameSize.getNewY(40);
    private String text;
    private final float x, y;
    private Bitmap bmp = null;
    private Rect rect;
    private int text_w, text_h;
    private boolean adaptive;

    private final TextPaint paint = new TextPaint();
        private final HashMap<String, StaticLayout> textCache = new HashMap<>();
//    private StaticLayout layout;
    private Typeface typeface;
    private Resources resources;

    public GameText(String text, int x, int y, boolean adaptive) {
        this.adaptive = adaptive;
        if (!adaptive) {
            this.x = GameSize.getNewX(x);
            this.y = GameSize.getNewY(y);
        } else {
            this.x = x;
            this.y = y;
        }
        this.text = text;

    }

    public GameText(String text, int x, int y, Bitmap bmp, boolean adaptive) {
        this.adaptive = adaptive;
        if (!adaptive) {
            this.x = GameSize.getNewX(x);
            this.y = GameSize.getNewY(y);
        } else {
            this.x = x;
            this.y = y;
        }
        this.bmp = bmp;
        this.text = text;
    }

    public GameText(String text, int x, int y, int color, int front_size, boolean adaptive) {
        this.adaptive = adaptive;
        if (!adaptive) {
            this.x = GameSize.getNewX(x);
            this.y = GameSize.getNewY(y);
        } else {
            this.x = x;
            this.y = y;
        }
        this.text = text;
        COLOR = color;
        TEXTSIZE = front_size;
    }

    public void draw(Canvas canvas, Paint paint0) {
        paint.setColor(COLOR);
        paint.setTextSize(TEXTSIZE);
        paint.setTypeface(null!=typeface? typeface: Typeface.DEFAULT_BOLD);

        if (rect == null) {
            rect = new Rect();
            paint.getTextBounds(text, 0, text.length(), rect);
            text_w = rect.width();
            text_h = rect.height();
        }

        if (bmp != null) {
            canvas.drawBitmap(bmp, (x + text_w / 2) - bmp.getWidth() * 1 / 2,
                    (y - text_h / 2) - bmp.getHeight() * 1 / 3, paint);
        }
//		canvas.drawText(text, x, y, paint);
//		canvas.drawRect(x, y, x+text_w,y+text_h, paint);
//        StaticLayout layout = new StaticLayout(text, paint, GameSurfaceView.SCREEN_W, Layout.Alignment.ALIGN_CENTER, 1.0F, 0.0F, true);
        StaticLayout layout = textCache.get(text);
        if (layout == null) {
            layout = new StaticLayout(text, paint, GameSurfaceView.SCREEN_W, Layout.Alignment.ALIGN_CENTER, 1.0F, 0.0F, true);
            textCache.put(text, layout);
        }
        canvas.save();
        canvas.translate(0, y);
        layout.draw(canvas);
        canvas.restore();
    }

    public void logic() {

    }

    public void setText(String text) {
        this.text = text;
    }

    public void setTypeface(Typeface tf) {
        this.typeface = tf;
    }

    public void setResources(Resources res) {
        this.resources = res;
    }
}
