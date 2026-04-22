package com.gamez.tap_gopher.threads;

import com.gamez.tap_gopher.collections.GopherCollection;
import com.gamez.tap_gopher.GameSurfaceView;

import funs.gamez.Logz;

public class GopherUpThread extends Thread {

    public boolean flag = true;
    private final GameSurfaceView main_view;
    private final GopherCollection m_collection;
    private static final int INDURATION = 1000, DE_SPE = 10, MIN_DURATION = 165;// 15, 100;
    private static int ran_duration = INDURATION;

    public GopherUpThread(GameSurfaceView main_view, GopherCollection m_collection) {
        this.main_view = main_view;
        this.m_collection = m_collection;
    }

    public void run() {
        Logz.i("GopherUpThread", "run()");
        while (flag) {
            if (main_view.getGameState() == GameSurfaceView.GAME_ING) {
                long start_time, end_time;
                start_time = System.currentTimeMillis();

                m_collection.ran_up();
                end_time = System.currentTimeMillis();
                if (end_time - start_time < ran_duration) {
                    try {
                        Thread.sleep(ran_duration - (end_time - start_time));
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    public void decreadDuration() {
        if (ran_duration >= MIN_DURATION) {
//        if (ran_duration - DE_SPE >= MIN_DURATION) {
            ran_duration -= DE_SPE;
        }
        Logz.i("GopherUpThread", "decreadDuration:  " + ran_duration);
    }

    public void resetDuration() {
        ran_duration = INDURATION;
    }

    public void balanceDuration() {
        ran_duration = INDURATION/2 + ran_duration/2;
    }
}
