package com.fpliu.newton.ui.base;

import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Typeface;
import android.util.Log;

import java.util.HashMap;
import java.util.Map;

public final class FontManager {

    private static final String TAG = FontManager.class.getName();

    private static FontManager instance;

    private AssetManager assetManager;

    private Map<String, Typeface> fonts;

    private FontManager(Context context) {
        this.assetManager = context.getAssets();
        this.fonts = new HashMap<>();
    }

    public static FontManager getInstance(Context context) {
        if (instance == null) {
            instance = new FontManager(context);
        }
        return instance;
    }

    public Typeface getFont(String fontPath) {
        if (fonts.containsKey(fontPath)) {
            return fonts.get(fontPath);
        }

        Typeface font = null;

        try {
            font = Typeface.createFromAsset(assetManager, fontPath);
            fonts.put(fontPath, font);
        } catch (RuntimeException e) {
            Log.e(TAG, "getFont: Can't create font from asset.", e);
        }

        return font;
    }
}
