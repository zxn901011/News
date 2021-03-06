package com.zxn.news.adapter;

import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;

import com.zxn.news.base.BasePager;

import java.util.ArrayList;

/**
 * Created by zxn on 2017-08-02.
 */
public class ContentFragmentAdapter extends PagerAdapter {
    private final ArrayList<BasePager> basePagers;

    public ContentFragmentAdapter(ArrayList<BasePager> basePagers){
        this.basePagers = basePagers;
    }

    @Override
    public int getCount() {
        return basePagers.size();
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        BasePager basePager = basePagers.get(position);//各个页面的实例
        View rootView = basePager.rootView;//各个子页面
        container.addView(rootView);
        return rootView;
    }
    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view ==object;
    }



    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }
}
