package funs.gamez;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;

import java.util.HashMap;
import java.util.Map;

public class ResourceKeeper {
    private final Resources resources;
    private final Context context;

//    private final Map<Character, Bitmap> bitmaps;
    private final Map<String, Bitmap> bitmaps;
    private final HashMap<String, Typeface> typefaces;

    public ResourceKeeper(Context context) {
        resources = context.getResources();
        this.context = context;

        bitmaps = new HashMap<>();
        typefaces = new HashMap<>();
    }

    public boolean loadBitmap(String bitmapName) {
        int resourceId = resources.getIdentifier(bitmapName, "drawable", context.getPackageName());
        if (resourceId > 0) {
            Bitmap bitmap = BitmapFactory.decodeResource(resources, resourceId);
            bitmaps.put(bitmapName, bitmap);
            return true;
        } else {
            return false;
        }
    }

    public void unloadBitmap(String bitmapName) {
        Bitmap bitmap = bitmaps.get(bitmapName);
        if (bitmap != null) {
            bitmap.recycle();
            bitmaps.remove(bitmapName);
        }
    }

    public boolean loadTypeface(String fontName, String fileExtension) {
        String fontFileName = fontName + "." + fileExtension;
        Typeface typeface = Typeface.createFromAsset(resources.getAssets(), fontFileName);
        if (typeface != null) {
            typefaces.put(fontName, typeface);
            return true;
        } else {
            return false;
        }
    }

    public void unloadTypeface(String fontName) {
        typefaces.remove(fontName);
    }

    public Bitmap getBitmap(String bitmapName) {
        return bitmaps.get(bitmapName);
    }

    public Bitmap putBitmap(String bitmapName, Bitmap bitmap) {
        return bitmaps.put(bitmapName, bitmap);
    }

    public Typeface getTypeface(String fontName) {
        return typefaces.get(fontName);
    }

    public void unloadEverything() {
        for (Bitmap bitmap : bitmaps.values()) {
            bitmap.recycle();
        }
        bitmaps.clear();
        typefaces.clear();
    }

    public Resources getResources() {
        return this.resources;
    }
}
