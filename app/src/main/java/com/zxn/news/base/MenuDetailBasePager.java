package com.zxn.news.base;

import android.content.Context;
import android.view.View;

/**
 * Created by zxn on 2017-08-02.
 */

public abstract class MenuDetailBasePager {
    /**
     * 上下文
     */
    public final Context context;

    /**
     * 调用的这个视图
     */
    public View rootView;

    public MenuDetailBasePager(Context context){
        this.context=context;
        rootView=initView();
    }

    /**
     * 强制孩子实现视图
     * @return
     */
    public abstract View initView();

    /**
     * 子页面需要绑定数据，联网请求数据等的时候
     */
    public void initData(){

    }
}
