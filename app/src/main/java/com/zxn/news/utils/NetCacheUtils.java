package com.zxn.news.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Message;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by zxn on 2017-08-06.
 * 网络缓存工具类
 */

public class NetCacheUtils {

    /**
     * 请求图片成功
     */
    public static final int SUCCESS =1;
    /**
     * 请求图片失败
     */
    public static final int FAILURE = 2;
    private final Handler handler;
    /**
     * 本地缓存工具类
     */
    private final LocalCacheUtils localCacheUtils;
    /**
     * 内存缓存工具类
     */
    private final MemoryCacheUtils memoryCacheUtils;
    private ExecutorService service;

    public NetCacheUtils(Handler handler, LocalCacheUtils localCacheUtils, MemoryCacheUtils memoryCacheUtils) {
        this.handler=handler;
        this.service=Executors.newFixedThreadPool(10);//创建一个固定大小的线程池，返回一个线程池服务类
        this.localCacheUtils=localCacheUtils;
        this.memoryCacheUtils=memoryCacheUtils;
    }

    public void getBitmapFromNet(String imageUrl, int position) {
//        new Thread(new MyRunnable(imageUrl,position)).start();
        service.execute(new MyRunnable(imageUrl,position));
    }

    private class MyRunnable implements Runnable {
        private final String imageUrl;
        private final int position;

        public MyRunnable(String imageUrl, int position) {
            this.imageUrl=imageUrl;
            this.position=position;
        }

        @Override
        public void run() {
            //子线程
            //请求网络图片
            try {
                URL url=new URL(imageUrl);
                HttpURLConnection connection= (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("GET");//只能大写
                connection.setConnectTimeout(5000);
                connection.setReadTimeout(4000);
                connection.connect();//可写可不写
                int code=connection.getResponseCode();
                if (code==200){
                    LogUtil.e("请求成功");
                    InputStream is=connection.getInputStream();//获取一个输入流
                    Bitmap bitmap=BitmapFactory.decodeStream(is);//把输入流转成一个Bitmap对象
                    //显示在控件上，发消息把Bitmap发出去和position
                    Message msg=Message.obtain();//从队列中取一个空的消息
                    msg.what=SUCCESS;//请求图片成功
                    msg.arg1=position;
                    msg.obj=bitmap;
                    handler.sendMessage(msg);
                    //然后在内村中缓存一份
                    memoryCacheUtils.putBitmap(imageUrl,bitmap);
                    //然后在本地缓存一份
                    localCacheUtils.putBitmap(imageUrl,bitmap);
                }else {
                    LogUtil.e("请求异常，请及时处理");
                }
            } catch (IOException e) {
                e.printStackTrace();
                Message msg=Message.obtain();
                msg.what=FAILURE;
                msg.arg1=position;
                handler.sendMessage(msg);
            }
        }
    }
}
