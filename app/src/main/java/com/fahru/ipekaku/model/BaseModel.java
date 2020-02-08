package com.fahru.ipekaku.model;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.DisplayMetrics;

import androidx.annotation.Nullable;

public abstract class BaseModel extends ToastModel {
    protected SharedPreferences userPrefs;
    protected boolean login;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        userPrefs = getSharedPreferences("userPrefs", 0);
        login = userPrefs.getBoolean("login", false);
    }



    protected Typeface setTypefaceClicker(Context context) {
        return Typeface.createFromAsset(context.getAssets(), "clicker.ttf");
    }
    protected Typeface setTypefaceBomb(Context context) {
        return Typeface.createFromAsset(context.getAssets(), "bomb.otf");
    }

    protected Typeface setTypefaceAgenB(Context context) {
        return Typeface.createFromAsset(context.getAssets(), "comicbd.ttf");
    }
    protected Typeface setTypefaceAgenR(Context context) {
        return Typeface.createFromAsset(context.getAssets(), "comic.ttf");
    }

    public static int convertDpToPixel(int dp, Context context){
        return dp * ( context.getResources().getDisplayMetrics().densityDpi / DisplayMetrics.DENSITY_DEFAULT);
    }
}
