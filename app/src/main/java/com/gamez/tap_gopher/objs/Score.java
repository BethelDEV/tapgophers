package com.gamez.tap_gopher.objs;

import android.content.res.Resources;
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


public class Score {
    public final static float TEXT_SPE = GameSize.getNewY(2);
    //CHANGE_FLAG_FORDURATION�ı������������̵߳ļ���ķ�ֵ��׼
    public final static int SCORE_ADD = 10, CHANGE_FLAG_FORDURATION = 10;
    //CHANGE_FLAG_FORSPE�ı�����ٶȵķ����仯��ֵ��׼
    public final static int CHANGE_FLAG_FORSPE = 30, TEXT_MOVE = 0, TEXT_STAND = 1;
    private final static int SIZE = (int) GameSize.getNewY(24);
    private final static int SIZE_SCORE = (int) GameSize.getNewY(40);
    private int x, y, y_score;
    private Rect rect;
    public int color = Color.RED;
    public boolean islive;
    private final int state;
    private int text_w, text_h;
    private int score = 0, f_score1 = 0, f_score2 = 0;
    private String text = getScoreText();
    private final GameSurfaceView gsv;

    private final TextPaint paint = new TextPaint();
    private final int SCREEN_W;
    private final HashMap<String, StaticLayout> textCache = new HashMap<>();
    private Typeface typeface;
    private Resources resources;

    public Score(GameSurfaceView gsv) {
        this.gsv = gsv;
        SCREEN_W = GameSurfaceView.SCREEN_W;
//		x=0;
//		y=GameSurfaceView.SCREEN_H*9/10;
        x = SCREEN_W / 3;
        y = GameSurfaceView.SCREEN_H / 9;
        y_score = y+SIZE*6/5;
//        y_score = y+SIZE;
//        y_score = y+SIZE_SCORE;
//        y_score = 2*y;
        islive = true;
//		state = TEXT_MOVE;
        state = TEXT_STAND;
    }

    public void draw(Canvas canvas, Paint paint0) {
        if (rect == null) {
            rect = new Rect();
//            paint.getTextBounds(text, 0, text.length(), rect);
            text_w = rect.width();
            text_h = rect.height();
        }
        paint.setAntiAlias(true);
        paint.setTextSize(SIZE);
//        paint.setTypeface(Typeface.DEFAULT_BOLD);
        paint.setTypeface(null!=typeface? typeface: Typeface.DEFAULT_BOLD);
        int c = paint.getColor();
        paint.setColor(color);
        if (islive) {
//            canvas.drawText(text, x, y, paint);
//            paint.setTextSize(SIZE_SCORE);
//            canvas.drawText(String.valueOf(score), x, y_score, paint);
            String titleText = "Current Score";
            StaticLayout layout = textCache.get(titleText);
            if (layout == null) {
                layout = new StaticLayout(titleText, paint, SCREEN_W, Layout.Alignment.ALIGN_CENTER, 1.0F, 0.0F, true);
                textCache.put(titleText, layout);
            }
            canvas.save();
            canvas.translate(0, y);
            layout.draw(canvas);
            canvas.restore();

            paint.setTextSize(SIZE_SCORE);
            layout = textCache.get(text);
            if (layout == null) {
                layout = new StaticLayout(text, paint, SCREEN_W, Layout.Alignment.ALIGN_CENTER, 1.0F, 0.0F, true);
                textCache.put(text, layout);
            }
            canvas.save();
            canvas.translate(0, y_score);
            layout.draw(canvas);
            canvas.restore();
        }
        paint.setColor(c);
//		Log.i("Score"," size "+size);
    }

    public void logic() {
        if (islive) {
            if (state == TEXT_MOVE) {
                if (x <= GameSurfaceView.SCREEN_W) {
                    x += TEXT_SPE;
                } else {
                    x = -text_w;
                }
            }  //nothing

        }
        onChange();
    }

    /**
     * �������仯ʱִ�еķ���
     */
    private void onChange() {
        if (score - f_score1 >= CHANGE_FLAG_FORDURATION) {
            gsv.up_thread.decreadDuration();
            f_score1 += CHANGE_FLAG_FORDURATION;
        }
        if (score - f_score2 >= CHANGE_FLAG_FORSPE) {
            gsv.m_collection.increaSpe();
            f_score2 += CHANGE_FLAG_FORSPE;
        }
    }

    public int getScore() {
        return score;
    }

    public void reset() {
//        x = 0;
        x = SCREEN_W / 3;
        score = 0;
        text = getScoreText();
        gsv.up_thread.resetDuration();
        gsv.m_collection.resetSpe();

//        textCache.clear();
    }

    public void increase() {
        score += SCORE_ADD;
        text = getScoreText();
    }

//    private String format = null;
    private String getScoreText() {
        return String.valueOf(score);
//        if (format == null) {
//            if (resources == null) {
//                return String.valueOf(score);
//            }
//
//            format = resources.getString(R.string.format_score_current);
//        }
//        return String.format(format, score);
    }

    public void setTypeface(Typeface tf) {
        this.typeface = tf;
    }

    public void setResources(Resources res) {
        this.resources = res;
    }
}
