package com.zxn.news.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;

/**
 * Created by zxn on 2017-08-06.
 */

public class LocalCacheUtils {
    private final MemoryCacheUtils memoryCacheUtils;

    public LocalCacheUtils(MemoryCacheUtils memoryCacheUtils) {
        this.memoryCacheUtils=memoryCacheUtils;
    }

    /**
     * 根据url获取图片
     * @param imageUrl
     * @return
     */
    public Bitmap getBitmapFromUrl(String imageUrl) {
        //判断sd卡是否挂载
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            //保存图片在/mnt/sdcard/News
            try {
                String fileName = MD5Encoder.encode(imageUrl);//fnlkajdfjasfjasdf
                File file=new File(Environment.getExternalStorageDirectory()+"/News",fileName);
                if (file.exists()){
                    FileInputStream is=new FileInputStream(file);
                    Bitmap bitmap=BitmapFactory.decodeStream(is);
                    if (bitmap!=null){
                        memoryCacheUtils.putBitmap(imageUrl,bitmap);
                        LogUtil.e("把图片从本地保存到内存中");
                    }
                    return bitmap;
                }
            } catch (Exception e) {
                e.printStackTrace();
                LogUtil.e("图片获取失败");
            }
        }
        return null;
    }

    /**
     * 根据url保存图片
     * @param imageUrl url
     * @param bitmap 图片
     */
    public void putBitmap(String imageUrl, Bitmap bitmap) {
        //判断sd卡是否挂载
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            //保存图片在/mnt/sdcard/News
            try {
                String fileName = MD5Encoder.encode(imageUrl);//fnlkajdfjasfjasdf
                File file=new File(Environment.getExternalStorageDirectory()+"/News",fileName);
                //本地没有这个目录要先进行创建
                File parentFile =  file.getParentFile();//mnt/sdcard/News
                if(!parentFile.exists()){
                    //创建目录
                    parentFile.mkdirs();
                }
                if (!file.exists()){
                    file.createNewFile();
                }
                //保存图片
                bitmap.compress(Bitmap.CompressFormat.PNG,100, new FileOutputStream(file));
            } catch (Exception e) {
                e.printStackTrace();
                LogUtil.e("图片本地缓存失败");
            }
        }
    }
}
