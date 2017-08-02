package com.zxn.news.fragment;

import android.graphics.Color;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;
import com.zxn.news.base.BaseFragment;
import com.zxn.news.utils.LogUtil;

/**
 * A simple {@link Fragment} subclass.
 * 作者:zxn
 * 作用：左侧菜单的Fragment
 */
public class LeftMenuFragment extends BaseFragment {

    private TextView textView;
    @Override
    public View initView() {
        LogUtil.e("左侧菜单的Fragment被初始化");
        textView=new TextView(context);
        textView.setTextSize(23);
        textView.setGravity(Gravity.CENTER);
        textView.setTextColor(Color.RED);
        return textView;
    }

    @Override
    public void initData() {
        super.initData();
        LogUtil.e("左侧页面数据初始化");
        //初始化数据
        textView.setText("我是左侧页面");
    }
}
