package com.zxn.news.utils;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by zxn on 2017-08-01.
 */

public class CacheUtils {

    private Context context;
    public CacheUtils(Context context){
        this.context=context;
    }
    public static boolean getBoolean(Context context, String key) {
        SharedPreferences preferences=context.getSharedPreferences("zxn",Context.MODE_PRIVATE);
        boolean isStartMain=preferences.getBoolean(key,false);
        return isStartMain;
    }

    public static void putBoolean(Context context, String key, boolean value) {
        SharedPreferences preferences=context.getSharedPreferences("zxn",Context.MODE_PRIVATE);
        preferences.edit().putBoolean(key,value).apply();

    }

    public static void putInt(Context context, String key, int value) {
        SharedPreferences preferences=context.getSharedPreferences("zxn",Context.MODE_PRIVATE);
        preferences.edit().putInt(key,value).apply();
    }

    public static int getInt(Context context,String key) {
        SharedPreferences preferences=context.getSharedPreferences("zxn",Context.MODE_PRIVATE);
        int btnMode=preferences.getInt(key,1);
        return btnMode;
    }
}
