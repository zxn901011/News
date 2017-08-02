package com.zxn.news.menudetailpager;

import android.content.Context;
import android.graphics.Color;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;

import com.zxn.news.base.MenuDetailBasePager;

/**
 * Created by zxn on 2017-08-02.
 */

public class TouPiaoMenuDetailPager extends MenuDetailBasePager {

    private TextView textView;

    public TouPiaoMenuDetailPager(Context context) {
        super(context);
    }

    @Override
    public View initView() {
        textView=new TextView(context);
        textView.setTextSize(20);
        textView.setTextColor(Color.RED);
        textView.setGravity(Gravity.CENTER);
        return textView;
    }

    @Override
    public void initData() {
        super.initData();
        textView.setText("投票页面内容");
    }
}
