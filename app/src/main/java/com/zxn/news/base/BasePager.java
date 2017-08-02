package com.zxn.news.base;

import android.content.Context;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.TextView;

import com.zxn.news.R;

/**
 * 作者：zxn
 * 作用：基类，公共类，
 * VideoPager
 * <p/>
 * AudioPager
 * <p/>
 * NetVideoPager
 * <p/>
 * NetAudioPager
 * 都继承该类
 */
public class BasePager{

    /**
     * 上下文
     */
    public final Context context;
    /**
     * 接收各个页面的实例
     */
    public View rootView;
    /**
     * 显示标题
     */
    public TextView tv_title;

    /**
     *点击侧滑
     */
    public ImageButton ib_menu;
    public FrameLayout fl_base_pager;
    public boolean isInitData;

    public BasePager(Context context) {
        this.context = context;
        rootView = initView();
    }

    /**
     * 强制子页面实现该方法，实现想要的特定的效果
     *
     * @return
     */
    public View initView(){
        View view=View.inflate(context, R.layout.base_pager,null);
        tv_title= (TextView) view.findViewById(R.id.tv_title);
        ib_menu= (ImageButton) view.findViewById(R.id.ib_menu);
        fl_base_pager= (FrameLayout) view.findViewById(R.id.fl_base_pager);
        return view;
    }



    /**
     * 当子页面，需要绑定数据，或者联网请求数据并且绑定的时候，重写该方法
     */
    public void initData(){}
}
