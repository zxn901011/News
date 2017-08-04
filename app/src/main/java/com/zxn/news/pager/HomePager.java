package com.zxn.news.pager;

import android.content.Context;
import android.graphics.Color;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;

import com.zxn.news.base.BasePager;

/**
 * Created by zxn on 2017-08-02.
 * 热点
 */

public class HomePager extends BasePager {
    public HomePager(Context context) {
        super(context);
    }

    @Override
    public View initView() {
        return super.initView();
    }

    @Override
    public void initData() {
        super.initData();
        tv_title.setText("热点");
        TextView textView=new TextView(context);
        textView.setTextColor(Color.RED);
        textView.setGravity(Gravity.CENTER);
        textView.setTextSize(25);
        //将视图加入到BasePager的FrameLayout中
        fl_base_pager.addView(textView);
        textView.setText("我是热点的内容");
    }
}
