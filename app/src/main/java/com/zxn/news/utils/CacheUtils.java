package com.zxn.news.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Environment;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;

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
        preferences.edit().putBoolean(key,value).commit();
    }

    /**
     * 将选择的模式进行存储
     * @param context
     * @param key
     * @param value
     */
    public static void putInt(Context context, String key, int value) {
        SharedPreferences preferences=context.getSharedPreferences("zxn",Context.MODE_PRIVATE);
        preferences.edit().putInt(key,value).commit();
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
        //判断sd卡是否挂载
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            //保存图片在/mnt/sdcard/News/files下
            try {
                String fileName = MD5Encoder.encode(key);//fnlkajdfjasfjasdf
                File file=new File(Environment.getExternalStorageDirectory()+"/News/files",fileName);
                //本地没有这个目录要先进行创建
                File parentFile =  file.getParentFile();//mnt/sdcard/News
                if(!parentFile.exists()){
                    //创建目录
                    parentFile.mkdirs();
                }
                if (!file.exists()){
                    file.createNewFile();
                }
                //保存文本数据
                FileOutputStream fos=new FileOutputStream(file);
                fos.write(value.getBytes());
                fos.close();
            } catch (Exception e) {
                e.printStackTrace();
                LogUtil.e("文本数据缓存失败");
            }
        }else {
            SharedPreferences preferences = context.getSharedPreferences("zxn", Context.MODE_PRIVATE);
            preferences.edit().putString(key, value).commit();
        }
    }

    /**
     * 获取缓存的数据
     * @param context
     * @param key
     * @return
     */
    public static String getString(Context context, String key) {
        //判断sd卡是否挂载
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            //保存图片在/mnt/sdcard/News
            try {
                String fileName = MD5Encoder.encode(key);//fnlkajdfjasfjasdf
                File file = new File(Environment.getExternalStorageDirectory() + "/News/files", fileName);
                if (file.exists()) {
                    FileInputStream is = new FileInputStream(file);
                    ByteArrayOutputStream stream=new ByteArrayOutputStream();
                    byte[] buffer=new byte[1024];
                    int length;
                    while ((length=is.read(buffer))!=-1){//读不出来就是-1
                        stream.write(buffer,0,length);
                    }
                    String result=stream.toString();
                    is.close();
                    stream.close();
                    return result;
                }
            } catch (Exception e) {
                e.printStackTrace();
                LogUtil.e("文本获取失败");
            }
        } else {
            SharedPreferences preferences = context.getSharedPreferences("zxn", Context.MODE_PRIVATE);
            String result = preferences.getString(key, "");
            return result;
        }
        return "";
    }
}
