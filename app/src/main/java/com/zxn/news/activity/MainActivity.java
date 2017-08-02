package com.zxn.news.activity;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.DisplayMetrics;
import android.widget.FrameLayout;

import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.jeremyfeinstein.slidingmenu.lib.app.SlidingFragmentActivity;
import com.zxn.news.R;
import com.zxn.news.fragment.ContentFragment;
import com.zxn.news.fragment.LeftMenuFragment;

public final class MainActivity extends SlidingFragmentActivity {

    private static final String MAIN_CONTENT_TAG = "main_content_tag";
    private static final String LEFT_MENU_TAG = "left_menu_tag";
    private FrameLayout fl_content;
    private FrameLayout fl_left;
    private int screenWidth;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initMenu();
        initViews();
        initFragment();
    }

    private void initViews() {
        fl_content=(FrameLayout) findViewById(R.id.fl_content);
        fl_left=(FrameLayout) findViewById(R.id.fl_left);
    }

    private void initMenu() {
        SlidingMenu slidingMenu=getSlidingMenu();//获取一个slidingmenu对象，用getSlidingMenu()!!!!!
        //1.设置主页面
        setContentView(R.layout.activity_main);
        //2.设置左侧菜单,只要是左侧菜单，都要用帧布局
        setBehindContentView(R.layout.activity_left_menu);
        //4.设置显示模式，三种模式，左侧菜单+主页，左侧菜单+主页面+右侧菜单，主页+右侧菜单
        slidingMenu.setMode(SlidingMenu.LEFT);
        //5.设置滑动模式：滑动边缘，全屏滑动，不可以滑动
        slidingMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_MARGIN);
        //6.设置主页占据的宽度
        DisplayMetrics outmetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(outmetrics);
        screenWidth = outmetrics.widthPixels;
        slidingMenu.setBehindOffset((int) (screenWidth*0.6));
    }

    private void initFragment() {
        FragmentManager manager=getSupportFragmentManager();
        FragmentTransaction ft=manager.beginTransaction();
        ft.replace(R.id.fl_content,new ContentFragment(),MAIN_CONTENT_TAG);//主页布局
        ft.replace(R.id.fl_left,new LeftMenuFragment(),LEFT_MENU_TAG);//左侧布局
        ft.commit();
    }
}
