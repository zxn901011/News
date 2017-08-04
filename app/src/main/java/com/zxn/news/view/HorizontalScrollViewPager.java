package com.zxn.news.view;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * Created by zxn on 2017-08-04.
 * 水平方向的ViewPager
 */

public class HorizontalScrollViewPager extends ViewPager {
    /**
     * 记录起始坐标
     */
    private float startX;
    private float startY;

    public HorizontalScrollViewPager(Context context) {
        super(context);
    }

    public HorizontalScrollViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        //请求父层控件不拦截当前控件的事件

        switch (ev.getAction()){
            case MotionEvent.ACTION_DOWN:
                getParent().requestDisallowInterceptTouchEvent(true);//都把事件传给当前事件（HorizontalScrollViewPager）
                //1.记录起始坐标,加上ev
                startX=ev.getX();
                startY=ev.getY();
                break;
            case MotionEvent.ACTION_MOVE:
                //2.得到新的坐标
                float endX=ev.getX();
                float endY=ev.getY();
                //3.计算偏移量
                float deltaX=endX-startX;
                float deltaY=endY-startY;
                //4.判断滑动方向
                if (Math.abs(deltaX)>Math.abs(deltaY)){
                    //水平方向滑动
                    //2.1当滑动到view的第0个页面时，并且是从左往右滑动
                    if (getCurrentItem()==0&&deltaX>0){
                        getParent().requestDisallowInterceptTouchEvent(false);
                    }
                    //2.2当滑动到viewPager的最后一个页面，并且是从右往左滑动
//                    getParent().requestDisallowInterceptTouchEvent(false);
                    else if ((getCurrentItem()==(getAdapter().getCount()-1))&&deltaX<0){
                        getParent().requestDisallowInterceptTouchEvent(false);
                    }
                    //2.3其他情况
//                    getParent().requestDisallowInterceptTouchEvent(true);
                    else{
                        getParent().requestDisallowInterceptTouchEvent(true);
                    }
                }else {
                    //竖直方向滑动
                    getParent().requestDisallowInterceptTouchEvent(false);
                }
                break;
            case MotionEvent.ACTION_UP:
                break;
        }
        return super.dispatchTouchEvent(ev);
    }
}
