package com.gamez.tap_gopher;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceHolder.Callback;
import android.view.SurfaceView;

import com.gamez.tap_gopher.collections.GameSoundCollection;
import com.gamez.tap_gopher.collections.HitAnimationCollection;
import com.gamez.tap_gopher.collections.HoleCollection;
import com.gamez.tap_gopher.collections.MediaPlayerCollection;
import com.gamez.tap_gopher.collections.GopherCollection;
import com.gamez.tap_gopher.collections.StarCollection;
import com.gamez.tap_gopher.objs.GameText;
import com.gamez.tap_gopher.objs.HitAnimation;
import com.gamez.tap_gopher.objs.Gopher;
import com.gamez.tap_gopher.objs.Score;
import com.gamez.tap_gopher.threads.GopherUpThread;

import java.io.IOException;

import funs.gamez.AboutActivity;
import funs.gamez.Consts;
import funs.gamez.IGameController;
import funs.gamez.Logz;
import funs.gamez.MainActivity;
import funs.gamez.ResourceKeeper;

public class GameSurfaceView extends SurfaceView implements Callback, Runnable {

    public static final int GAME_MENU = 0;
    public static final int GAME_ING = 1;
    public static final int GAME_OVER = 2;
    public static final int GAME_PAUSE = 3;

    //    private static Context context;
//    public static int gameState = GAME_MENU;
    private int gameState = GAME_MENU;
    public static int SCREEN_W, SCREEN_H;
    private static final int thread_time = 50;

    private Score score;

    private GameMenu menu;
    private GameText over_text;
    private Canvas canvas;
    private Paint paint;
    private SurfaceHolder holder;
    public GopherUpThread up_thread;
    private boolean game_th_on;
    private HoleCollection h_collection;
    public GopherCollection m_collection;
    private HitAnimationCollection hit_collection;
    private StarCollection t_collection;
    public GameSoundCollection gs_collection;
    public MediaPlayerCollection mp_collection;
    private int media_flag;
    protected ResourceKeeper mResKeeper;

    private IGameController iGameController;

    public GameSurfaceView(Context context) {
        super(context);
        __init(context);
    }

    public GameSurfaceView(Context context, AttributeSet attrs) {
        super(context, attrs);
        __init(context);
    }

    public GameSurfaceView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        __init(context);
    }

    @TargetApi(21)
    public GameSurfaceView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        __init(context);
    }

    private void __init(Context context) {
        holder = this.getHolder();
        holder.addCallback(this);
        paint = new Paint();
        paint.setColor(Color.WHITE);
        paint.setAntiAlias(true);

        setFocusableInTouchMode(true);
        setFocusable(true);
        this.setKeepScreenOn(true);

        mResKeeper = new ResourceKeeper(context);
        Logz.i("GameSurfaceView", "__init()");
    }

    public void surfaceCreated(SurfaceHolder holder) {
        GameSize.setView(this);
        SCREEN_W = this.getWidth();
        SCREEN_H = this.getHeight();
        // 初始化，图片、声音等
        initializeGameX();
        // 游戏线程
        Thread main_thread = new Thread(this);
        main_thread.start();
        game_th_on = true;

    }

    public void surfaceChanged(SurfaceHolder holder, int format, int width,
                               int height) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        Logz.i("GameSurfaceView", "surfaceDestroyed(_)");
        // 关闭音乐
        if (mp_collection != null) {
            mp_collection.release();
        }
        if (gs_collection != null) {
            gs_collection.release();
        }
        up_thread.flag = false;
        game_th_on = false;
        gameState = GAME_MENU;
        mResKeeper.unloadEverything();
    }

    protected void initializeGameX() {
        mResKeeper.loadBitmap(Consts.menu_bg_start_bmp);
        Bitmap menu_bg_start_bmp = mResKeeper.getBitmap(Consts.menu_bg_start_bmp);
        menu_bg_start_bmp = GameUtils.resizeBitmap(menu_bg_start_bmp, SCREEN_W, SCREEN_H);
        mResKeeper.putBitmap(Consts.menu_bg_start_bmp, menu_bg_start_bmp);
        mResKeeper.putBitmap(Consts.menu_bg_over_bmp, menu_bg_start_bmp);

        mResKeeper.loadBitmap(Consts.but1_up_bmp);
        mResKeeper.loadBitmap(Consts.but1_down_bmp);
        mResKeeper.loadBitmap(Consts.but2_up_bmp);
        mResKeeper.loadBitmap(Consts.but2_down_bmp);

        mResKeeper.loadBitmap(Consts.about_bmp);

        Bitmap game_bg_bmp = menu_bg_start_bmp;
        mResKeeper.putBitmap(Consts.game_bg_bmp, game_bg_bmp);

        mResKeeper.loadBitmap(Consts.hole_bmp);
        mResKeeper.loadBitmap(Consts.mole_live_bmp);
        mResKeeper.loadBitmap(Consts.mole_die_bmp);
        mResKeeper.loadBitmap(Consts.turnip_bmg);
        mResKeeper.loadBitmap(Consts.hit3_bmg);
        mResKeeper.loadBitmap(Consts.hit4_bmg);
        menu = new GameMenu(mResKeeper, this);

        //
        Rect mole_rect = new Rect(0, SCREEN_H * 2 / 7, SCREEN_W, SCREEN_H * 5 / 6);
        h_collection = new HoleCollection(mResKeeper.getBitmap(Consts.hole_bmp), mole_rect);
        h_collection.initialize();
        m_collection = new GopherCollection(mResKeeper.getBitmap(Consts.mole_live_bmp), mResKeeper.getBitmap(Consts.mole_die_bmp), h_collection, this);
//        m_collection = new GopherCollection(mole_live_bmp, mole_die_bmp, h_collection, this);
        m_collection.initialize();
        t_collection = new StarCollection(mResKeeper.getBitmap(Consts.turnip_bmg), this);
        t_collection.initialize();

        hit_collection = new HitAnimationCollection();

        // sound
        gs_collection = new GameSoundCollection();
        try {
            gs_collection.load(this.getContext(), Consts.SOUND_EAT);
            gs_collection.load(this.getContext(), Consts.SOUND_BUT);
            gs_collection.load(this.getContext(), Consts.SOUND_HIT);
            gs_collection.load(this.getContext(), Consts.SOUND_BEHIT);
            gs_collection.load(this.getContext(), Consts.SOUND_OVER);
        } catch (IOException e) {
            e.printStackTrace();
        }

        mp_collection = new MediaPlayerCollection();
        try {
            mp_collection.load(getContext(), Consts.MUSIC_GAME);
        } catch (IOException e) {
            e.printStackTrace();
        }

        up_thread = new GopherUpThread(this, m_collection);
        up_thread.start();

        mResKeeper.loadTypeface(Consts.TYPEFACE_IFr, "ttf");
        score = new Score(this);
        score.setTypeface(mResKeeper.getTypeface(Consts.TYPEFACE_IFr));
        score.setResources(mResKeeper.getResources());

        Bitmap overtext_bg_bmg = mResKeeper.getBitmap(Consts.overtext_bg_bmg);
        over_text = new GameText("Result:\n" + score.getScore(),
                SCREEN_W / 5, SCREEN_H / 5, overtext_bg_bmg, true);
//               SCREEN_W / 4,  SCREEN_H * 2 / 3, overtext_bg_bmg, true);
        over_text.setTypeface(mResKeeper.getTypeface(Consts.TYPEFACE_IFr));
        over_text.setResources(mResKeeper.getResources());
        media_flag = -1;
        Logz.i("GameSurfaceView", "initializeGameX()");
    }

    public void reinitializeGame() {
        m_collection.reinitialize();
        t_collection.reinitialize();
        score.reset();
        Logz.i("GameSurfaceView", "reinitializeGame(), reset");
    }

    /**
     * 游戏续命 —— 奖励
     */
    public void rewardLivesInGame() {
        m_collection.reinitialize();
        t_collection.reinitialize();
        up_thread.balanceDuration();
        setGameState(GAME_ING);
        Logz.i("GameSurfaceView", "rewardLivesInGame()");
    }

    public boolean onTouchEvent(MotionEvent event) {
        switch (gameState) {
            case GAME_MENU:
            case GAME_OVER:
                menu.onTouchEvent(event);
                break;
            case GAME_ING:
                this.gameingOntouch(event, m_collection);
                return false;
//            case GAME_OVER:
//                menu.onTouchEvent(event);
//                break;
            case GAME_PAUSE:
                break;
        }
        return true;
    }

    public void onSettingsBtnClicked() {
        Activity mainActivity = MainActivity.activitySRef.get();
        if (mainActivity != null && !mainActivity.isFinishing()) {
            mainActivity.runOnUiThread(() -> {
                AboutActivity.startMe(mainActivity);
//                Toast.makeText(mainActivity, "Settings~", Toast.LENGTH_SHORT).show();
                Logz.i("GameSurfaceView", "onSettingsBtnClicked .....");
            });
        }

    }

    public int getGameState() {
        return gameState;
    }

    public void setGameState(int gameState) {
        this.gameState = gameState;
    }

    public boolean onPageBackPressed() {
        Logz.i("GameSurfaceView", "onBackPressed .....");
        if (gameState == GAME_ING || gameState == GAME_OVER) {
            gameState = GAME_MENU;
        } else if (gameState == GAME_MENU) {
            up_thread.flag = false;
            Logz.i("GameSurfaceView", "onBackPressed: gameState==GAME_MENU");
            return false;
        } else if (gameState == GAME_PAUSE) {
            gameState = GAME_MENU;
        }
        return true;
    }

    // 游戏画面的绘制
    private void gameDraw() {

        try {
            canvas = holder.lockCanvas();
            if (canvas != null) {
                switch (gameState) {
                    case GAME_MENU:
                        menu.draw(canvas, paint);
                        break;
                    case GAME_PAUSE:
                    case GAME_ING:
                        canvas.drawBitmap(mResKeeper.getBitmap(Consts.game_bg_bmp), 0, 0, paint);
//                        canvas.drawBitmap(game_bg_bmp, 0, 0, paint);
                        h_collection.draw(canvas, paint);
                        m_collection.draw(canvas, paint);
                        hit_collection.draw(canvas, paint);
                        t_collection.draw(canvas, paint);
                        score.draw(canvas, paint);
                        break;
                    case GAME_OVER:
                        menu.draw(canvas, paint);
                        over_text.draw(canvas, paint);
                        break;
//                    case GAME_PAUSE:
//                        break;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (canvas != null) {
                holder.unlockCanvasAndPost(canvas);
            }
        }
    }

    // 游戏逻辑
    private void gameLogic() {
        switch (gameState) {
            case GAME_MENU:
                break;
            case GAME_ING:
                m_collection.logic();
                hit_collection.logic();
                t_collection.logic();
                score.logic();
                break;
            case GAME_OVER:
                String r = mResKeeper.getResources().getString(R.string.format_score_result, score.getScore());
                over_text.setText(r);
                break;
            case GAME_PAUSE:
                break;
        }
    }

    // 游戏音乐的播放控制
    public void gamePlay() {

        switch (gameState) {
            case GAME_MENU:
                mp_collection.pause(Consts.MUSIC_GAME);
                if (media_flag != GAME_MENU) media_flag = GAME_MENU;
                break;
            case GAME_ING:
                if (media_flag != GAME_ING) {
                    mp_collection.play(Consts.MUSIC_GAME);
                    media_flag = GAME_ING;
                }
                break;
            case GAME_OVER:
                if (media_flag != GAME_OVER) {
                    mp_collection.pause(Consts.MUSIC_GAME);
                    gs_collection.play(Consts.SOUND_OVER);
                    media_flag = GAME_OVER;
                }
                break;
            case GAME_PAUSE:
//                if (media_flag != GAME_PAUSE) {
//                    mp_collection.pause(Consts.MUSIC_GAME);
//                    gs_collection.play(Consts.SOUND_OVER);
//                    media_flag = GAME_PAUSE;
//                }
                break;
        }
    }

    public void pauseGameBgm() {
        if (media_flag != GAME_PAUSE) {
            mp_collection.pause(Consts.MUSIC_GAME);
//                    gs_collection.play(Consts.SOUND_OVER);
            media_flag = GAME_PAUSE;
        }
    }

    private void gameingOntouch(MotionEvent event, GopherCollection mc) {
        int x, y;
        x = (int) event.getX();
        y = (int) event.getY();
        for (Gopher m : GopherCollection.collection) {
            Rect rect = m.getTouchRect();
            if (GameUtils.contains(rect, x, y) && m.getState() != Gopher.ACT.DIE) {
                hit_collection.add(new HitAnimation(x, y, this, mResKeeper));
                m.die();
                score.increase();
            }
        }
    }

    /**
     * run in "main_thread"
     */
    @Override
    public void run() {
        while (game_th_on) {
            long starttime = System.currentTimeMillis();
            gameDraw();  // 游戏画面的绘制
            gameLogic(); // 游戏逻辑
            gamePlay();  // 游戏音乐的播放控制
            long endtime = System.currentTimeMillis();
            long delta = endtime - starttime;
            if (delta < thread_time) {
                try {
                    Thread.sleep(delta);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public IGameController getGameController() {
        return iGameController;
    }

    public void setGameController(IGameController iGameController) {
        this.iGameController = iGameController;
    }
}
