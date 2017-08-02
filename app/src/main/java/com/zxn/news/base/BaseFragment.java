package com.zxn.news.base;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by zxn on 2017-08-01.
 * 基类的Fragment，LeftMenuFragment和ContentFragement继承它
 */

public abstract class BaseFragment extends Fragment {
    public Activity context;//MainActivity
    /**
     * 当我们的fragment被创建的时候回调
     * @param savedInstanceState
     */
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context=getActivity();
    }

    /**
     * 当我们的视图被创建的时候调用
     * @param inflater
     * @param container
     * @param savedInstanceState
     * 创建了视图
     * @return
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return initView();
    }

    /**
     * 让孩子实现自己的视图
     * @return
     */
    public abstract View initView();

    /**
     * 当我们的activity被创建的时候被调用
     * @param savedInstanceState
     */
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initData();
    }

    /**
     * 1.如果自页面没有数据，联网请求数据，并且绑定到initView初始化的视图上
     */
    public void initData() {}
}
