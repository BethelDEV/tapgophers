package funs.gamez;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;

import com.gamez.tap_gopher.GameSurfaceView;
import com.gamez.tap_gopher.R;

import java.lang.ref.WeakReference;

import funs.gamez.Logz;

public class MainActivity extends Activity {
    private static final String TAG = "MainActivity";

    public static WeakReference<Activity> activitySRef;
    private GameSurfaceView gameView;

    final Handler mHandler = new Handler();

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activitySRef = new WeakReference<>(this);

        setContentView(R.layout.activity_main);
        gameView = findViewById(R.id.gameView);

    }

    private void gameOver() {
        if (isFinishing()) return;
        gameView.setGameState(GameSurfaceView.GAME_OVER);
    }

    @Override
    public void onBackPressed() {
        Logz.i(TAG, "onBackPressed()");
        if (null != gameView && View.VISIBLE == gameView.getVisibility() && gameView.onPageBackPressed()) {
            return;
        }
        super.onBackPressed();
    }

    @Override
    protected void onStop() {
        Logz.i(TAG, "onStop()");
        super.onStop();
    }

    @Override
    protected void onDestroy() {
//        dismissLoadingDialog();
        Logz.i(TAG, "onDestroy()");
        activitySRef.clear();
        mHandler.removeCallbacksAndMessages(null);
        super.onDestroy();
    }

}
