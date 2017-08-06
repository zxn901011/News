package com.zxn.news.utils;

import android.graphics.Bitmap;

import org.xutils.cache.LruCache;

/**
 * Created by zxn on 2017-08-06.
 * 内存缓存工具类
 */

public class MemoryCacheUtils {
    /**
     * 集合，装图片的
     */
    private LruCache<String,Bitmap> lruCache;

    public MemoryCacheUtils() {
        int maxSize= (int) (Runtime.getRuntime().maxMemory()/8);//如果这里写一个1024;
        lruCache=new LruCache<String,Bitmap>(maxSize){
            @Override
            protected int sizeOf(String key, Bitmap value) {
//                return super.sizeOf(key, value);
                return value.getRowBytes()*value.getHeight();//这里也要写一个1024;
            }
        };
    }

    /**
     * 根据url从内存中获取图片
     * @param imageUrl
     * @return
     */
    public Bitmap getBitmapFromUrl(String imageUrl) {
        return lruCache.get(imageUrl);
    }

    /**
     * 根据url保存图片到lruCache集合中
     * @param imageUrl 图片路径
     * @param bitmap 图片
     */
    public void putBitmap(String imageUrl, Bitmap bitmap) {
        lruCache.put(imageUrl,bitmap);
    }
}
