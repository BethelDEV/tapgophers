package com.gamez.tap_gopher;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.view.MotionEvent;

import com.gamez.tap_gopher.objs.GameButton;

import funs.gamez.Consts;
import funs.gamez.Logz;
import funs.gamez.ResourceKeeper;

class GameMenu {

    private final Bitmap bg_start_bmg, bg_over_bmg, but1_up_bmg, but1_down_bmg, but2_up_bmg, but2_down_bmg;
    private final GameSurfaceView gsv;
    //    private HashMap<String, Bitmap> map;
    private final GameButton start_but, back_but, settingBtn;

    public GameMenu(ResourceKeeper resKeeper, GameSurfaceView gsv) {
        this.gsv = gsv;
        this.bg_start_bmg = resKeeper.getBitmap(Consts.menu_bg_start_bmp);
        this.bg_over_bmg = resKeeper.getBitmap(Consts.menu_bg_start_bmp);
        this.but1_up_bmg = resKeeper.getBitmap(Consts.but1_up_bmp);
        this.but1_down_bmg = resKeeper.getBitmap(Consts.but1_down_bmp);
        this.but2_up_bmg = resKeeper.getBitmap(Consts.but2_up_bmp);
        this.but2_down_bmg = resKeeper.getBitmap(Consts.but2_down_bmp);

        // 按钮 Y 座标占屏幕高度的比例
        start_but = new GameButton(GameSurfaceView.SCREEN_W / 2 - but1_up_bmg.getWidth() / 2
                , GameSurfaceView.SCREEN_H * 3 / 5 - but1_up_bmg.getHeight() / 2, but1_up_bmg, but1_down_bmg, true);
//		start_but = new GameButton(GameSurfaceView.SCREEN_W/2-but1_up_bmg.getWidth()/2
//				,GameSurfaceView.SCREEN_H/2-but1_up_bmg.getHeight()/2,but1_up_bmg,but1_down_bmg,true);
        back_but = new GameButton(GameSurfaceView.SCREEN_W / 2 - but2_up_bmg.getWidth() / 2
                , GameSurfaceView.SCREEN_H * 3 / 5 - but2_up_bmg.getHeight() / 2, but2_up_bmg, but2_down_bmg, true);
//back_but = new GameButton(GameSurfaceView.SCREEN_W-but2_up_bmg.getWidth()
//				,GameSurfaceView.SCREEN_H-but2_up_bmg.getHeight(),but2_up_bmg,but2_down_bmg,true);

        Bitmap aboutBmg = resKeeper.getBitmap(Consts.about_bmp);
        settingBtn = new GameButton(GameSurfaceView.SCREEN_W - aboutBmg.getWidth() / 2 * 3,
                aboutBmg.getHeight()/2, aboutBmg, aboutBmg, true);
    }

    public void draw(Canvas canvas, Paint paint) {
        if (gsv.getGameState() == GameSurfaceView.GAME_MENU) {
            canvas.drawBitmap(bg_start_bmg, 0, 0, paint);
            start_but.draw(canvas, paint);
        } else if (gsv.getGameState() == GameSurfaceView.GAME_OVER) {
            canvas.drawBitmap(bg_over_bmg, 0, 0, paint);
            back_but.draw(canvas, paint);
//			Logz.i("GameMenu", " ");
        }

        if (gsv.getGameState() == GameSurfaceView.GAME_OVER || gsv.getGameState() == GameSurfaceView.GAME_MENU) {
            settingBtn.draw(canvas, paint);
        }
    }

    public void onTouchEvent(MotionEvent event) {
        if (gsv.getGameState() == GameSurfaceView.GAME_MENU) {
            if (start_but.onTouch(event)) {
                gsv.reinitializeGame();
                gsv.setGameState(GameSurfaceView.GAME_ING);
//                GameSurfaceView.gameState = GameSurfaceView.GAME_ING;
                gsv.gs_collection.play(Consts.SOUND_BUT);
            }
        } else if (gsv.getGameState() == GameSurfaceView.GAME_OVER) {
            if (back_but.onTouch(event)) {
                gsv.setGameState(GameSurfaceView.GAME_MENU);
//                GameSurfaceView.gameState = GameSurfaceView.GAME_MENU;
                gsv.gs_collection.play(Consts.SOUND_BUT);
            }
        }

        if (settingBtn.onTouch(event)) {
            Logz.i("GameMenu", "settingBtn--onTouch");
            gsv.onSettingsBtnClicked();
            gsv.gs_collection.play(Consts.SOUND_BUT);
        }
    }


}
