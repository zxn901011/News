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

    /**
     * 获取是否进入过引导页面
     * @param context
     * @param key
     * @return
     */
    public static boolean getBoolean(Context context, String key) {
        SharedPreferences preferences=context.getSharedPreferences("zxn",Context.MODE_PRIVATE);
        boolean isStartMain=preferences.getBoolean(key,false);
        return isStartMain;
    }

    /**
     * 是否进入过引导页面的状态存储
     * @param context
     * @param key
     * @param value
     */
    public static void putBoolean(Context context, String key, boolean value) {
        SharedPreferences preferences=context.getSharedPreferences("zxn",Context.MODE_PRIVATE);
        preferences.edit().putBoolean(key,value).apply();

    }

    /**
     * 将选择的模式进行存储
     * @param context
     * @param key
     * @param value
     */
    public static void putInt(Context context, String key, int value) {
        SharedPreferences preferences=context.getSharedPreferences("zxn",Context.MODE_PRIVATE);
        preferences.edit().putInt(key,value).apply();
    }

    /**
     * 获取进入哪个页面
     * @param context
     * @param key
     * @return
     */
    public static int getInt(Context context,String key) {
        SharedPreferences preferences=context.getSharedPreferences("zxn",Context.MODE_PRIVATE);
        int btnMode=preferences.getInt(key,1);
        return btnMode;
    }

    /**
     * 将数据进行缓存
     * @param context
     * @param key
     * @param value
     */
    public static void putString(Context context, String key, String value) {
        SharedPreferences preferences=context.getSharedPreferences("zxn",Context.MODE_PRIVATE);
        preferences.edit().putString(key,value).apply();
    }

    /**
     * 获取缓存的数据
     * @param context
     * @param key
     * @return
     */
    public static String getString(Context context, String key) {
        SharedPreferences preferences= context.getSharedPreferences("zxn",Context.MODE_PRIVATE);
        String result=preferences.getString(key,"");
        return result;
    }
}
