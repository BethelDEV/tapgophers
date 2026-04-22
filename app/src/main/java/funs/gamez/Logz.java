package funs.gamez;

import android.util.Log;

//import com.gamez.tap_gopher.BuildConfig;

/**
 * @Package: funs.gamez
 * @ClassName: Logz
 * @Description: 日志工具类
 * @Version: 1.0
 */
public final class Logz {
    private final static boolean DEBUG = true;
    public static void i(String tag, String msg) {
        if (DEBUG) {
            Log.i(tag, msg);
        }
    }
}
