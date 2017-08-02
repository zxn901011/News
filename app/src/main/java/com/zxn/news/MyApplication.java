package com.zxn.news;

import android.app.Application;

import org.xutils.x;

/**
 * Created by zxn on 2017-08-02.
 * 作用：应用的初始化
 */

public class MyApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        //初始化xutils3
        x.Ext.setDebug(true);
        x.Ext.init(this);
    }
}
