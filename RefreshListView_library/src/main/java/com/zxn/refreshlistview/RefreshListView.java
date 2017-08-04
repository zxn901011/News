package com.zxn.refreshlistview;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.AbsListView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Date;


/**
 * Created by zxn on 2017-08-04.
 */

public class RefreshListView extends ListView {

    private LinearLayout headerView;

    /**
     * 下拉刷新控件
     */
    private View ll_pull_down_refresh;
    private ImageView iv_refresh_image;
    private TextView tv_time;
    private TextView tv_status;
    private ProgressBar pb_status;
    private ProgressBar pb_status_bottom;
    private TextView tv_status_bottom;
    private int viewHeight;
    private float startY=-1;
    /**
     下拉刷新
     */
    public static final int PULL_DOWN_REFRESH = 0;
    /**
     手松刷新
     */
    public static final int RELEASE_REFRESH = 1;
    /**
     正在刷新
     */
    public static final int REFRESHING = 2;
    /**
     * 当前状态
     */
    private int currentStatus = PULL_DOWN_REFRESH;
    private OnRefreshListener mOnRefreshListener;
    private View footView;
    private int footViewHeight;
    /**
     * 是否已经加载更多
     */
    private boolean isLoadMore=false;
    /**
     * 顶部轮播图
     */
    private View topNewsView;
    /**
     * listview在Y轴上的坐标
     */
    private int listviewScreenY=-1;


    public RefreshListView(Context context) {
        this(context, null);
    }

    public RefreshListView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public RefreshListView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initHeaderView(context);
        initAnimation();
        initFooterView(context);
    }

    private void initFooterView(Context context) {

        footView=View.inflate(context,R.layout.refresh_foot_view,null);
        pb_status_bottom= (ProgressBar) footView.findViewById(R.id.pb_status_bottom);
        tv_status_bottom= (TextView) footView.findViewById(R.id.tv_status_bottom);
        footView.measure(0,0);
        footViewHeight=footView.getMeasuredHeight();
        footView.setPadding(0,-footViewHeight,0,0);
        addFooterView(footView);

        setOnScrollListener(new MyOnScrollListener());
    }

    private Animation upAnimation;
    private Animation downAnimation;
    private void initAnimation() {
        upAnimation=new RotateAnimation(0,-180,RotateAnimation.RELATIVE_TO_SELF,0.5f,RotateAnimation.RELATIVE_TO_SELF,0.5f);
        upAnimation.setDuration(500);
        upAnimation.setFillAfter(true);
        downAnimation=new RotateAnimation(-180,-360,RotateAnimation.RELATIVE_TO_SELF,0.5f,RotateAnimation.RELATIVE_TO_SELF,0.5f);
        downAnimation.setDuration(500);
        downAnimation.setFillAfter(true);
    }

    private void initHeaderView(Context context) {
        headerView = (LinearLayout) View.inflate(context, R.layout.refresh_header, null);
        ll_pull_down_refresh = headerView.findViewById(R.id.ll_pull_down_refresh);
        iv_refresh_image = (ImageView) headerView.findViewById(R.id.iv_refresh_image);
        tv_status = (TextView) headerView.findViewById(R.id.tv_status);
        tv_time = (TextView) headerView.findViewById(R.id.tv_time);
        pb_status = (ProgressBar) headerView.findViewById(R.id.pb_status);
        //测量
        ll_pull_down_refresh.measure(0, 0);
        //控件的高
        viewHeight = ll_pull_down_refresh.getMeasuredHeight();
        /**
         * 默认隐藏下拉刷新控件
         */
        ll_pull_down_refresh.setPadding(0, -viewHeight, 0, 0);
        //添加头
        RefreshListView.this.addHeaderView(headerView);
    }
    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        switch (ev.getAction()){
            case MotionEvent.ACTION_DOWN:
                //记录起始坐标
                startY=ev.getY();
                break;
            case MotionEvent.ACTION_MOVE:
                if (startY==-1){
                    startY=ev.getY();
                }
                //判断头部轮播图是否完全显示，只有完全显示才会下拉刷新
                boolean isDisplayTopNews=isDisplayTopNews();
                if (!isDisplayTopNews){
                    //加载更多
                    break;
                }else {

                }
                if (currentStatus==REFRESHING){
                    break;
                }
                //2.来到新的坐标
                float endY=ev.getY();
                //记录滑动的坐标
                float deltaY=endY-startY;
                if (deltaY>0){
                    int paddingTop= (int) (-viewHeight+deltaY);
                    if (paddingTop < 0 && currentStatus != PULL_DOWN_REFRESH) {
                        //下拉刷新状态
                        currentStatus = PULL_DOWN_REFRESH;
                        //更新状态
                        refreshViewState();
                    } else if (paddingTop > 0 && currentStatus != RELEASE_REFRESH) {
                        //手松刷新状态
                        currentStatus = RELEASE_REFRESH;
                        //更新状态
                        refreshViewState();
                    }
                    ll_pull_down_refresh.setPadding(0, paddingTop, 0, 0);
                }
                break;
            case MotionEvent.ACTION_UP:
                startY=-1;
                if (currentStatus == PULL_DOWN_REFRESH) {
//                    View.setPadding(0,-控件高，0,0);//完全隐藏
                    ll_pull_down_refresh.setPadding(0, -viewHeight, 0, 0);
                } else if (currentStatus == RELEASE_REFRESH) {
                    //设置状态为正在刷新
                    currentStatus = REFRESHING;
                    refreshViewState();
                    ll_pull_down_refresh.setPadding(0, 0, 0, 0);
                    //回调接口
                    if (mOnRefreshListener != null) {
                        mOnRefreshListener.onPullDownRefresh();
                    }
                }
                break;
        }
        return super.onTouchEvent(ev);
    }

    /**
     * 判断是否完全显示顶部轮播图
     * @return
     */
    private boolean isDisplayTopNews() {
        if (topNewsView!=null) {
            //1.得到listview在屏幕上的坐标
            int[] location = new int[2];

            if (listviewScreenY == -1) {
                getLocationOnScreen(location);
                listviewScreenY = location[1];
            }
            //2.得到顶部轮播图在屏幕上的坐标
            topNewsView.getLocationOnScreen(location);
            int topNewsViewOnScreenY = location[1];
            if (listviewScreenY <= topNewsViewOnScreenY) {
                return true;
            } else {
                return false;
            }
        }else {
            return true;
        }
    }

    private void refreshViewState() {
        switch (currentStatus){
            case PULL_DOWN_REFRESH://下拉刷新状态
                iv_refresh_image.startAnimation(downAnimation);
                tv_status.setText("下拉刷新....");
                break;
            case RELEASE_REFRESH://手松刷新状态
                iv_refresh_image.startAnimation(upAnimation);
                tv_status.setText("手松刷新....");
                break;
            case REFRESHING://正在刷新状态
                tv_status.setText("正在刷新....");
                pb_status.setVisibility(VISIBLE);
                iv_refresh_image.clearAnimation();
                iv_refresh_image.setVisibility(GONE);
                break;
        }
    }

    /**
     * 当联网成功和失败的时候回调该方法
     * 用户刷新状态的还原
     * @param success
     */
    /**
     * 当联网成功和失败的时候回调该方法
     * 用户刷新状态的还原
     *
     * @param sucess
     */
    public void onRefreshFinish(boolean sucess) {
        if (!isLoadMore) {
            tv_status.setText("下拉刷新...");
            currentStatus = PULL_DOWN_REFRESH;
            iv_refresh_image.clearAnimation();
            pb_status.setVisibility(GONE);
            iv_refresh_image.setVisibility(VISIBLE);
            //隐藏下拉刷新控件
            ll_pull_down_refresh.setPadding(0, -viewHeight, 0, 0);
            if (sucess) {
                //设置最新更新时间
                tv_time.setText("上次更新时间：" + getSystemTime());
            }
        }else {
            isLoadMore=false;
            //隐藏加载更多布局
            footView.setPadding(0,-footViewHeight,0,0);
        }
    }

    /**
     * 得到当前Android系统的时间
     *
     * @return
     */
    private String getSystemTime() {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return format.format(new Date());
    }

    /**
     * 添加顶部轮播图
     * @param topNewsView
     */
    public void addTopNewsView(View topNewsView) {
        if (topNewsView!=null) {
            this.topNewsView = topNewsView;
            headerView.addView(topNewsView);
        }
    }

    /**
     * 监听事件的刷新
     */
    public interface OnRefreshListener{
        /**
         当下拉刷新的时候回调这个方法
         */
        void onPullDownRefresh();
        /**
         当加载更多的时候回调这个方法
         */
         void onLoadMore();
    }

    /**
     * 设置监听刷新，由外部设置
     * @param l
     */
    public void setOnRefreshListenenr(OnRefreshListener l){
        this.mOnRefreshListener=l;
    }

    class MyOnScrollListener implements OnScrollListener {
        @Override
        public void onScrollStateChanged(AbsListView view, int scrollState) {
            //当静止或者惯性滚动的时候
            if (scrollState==OnScrollListener.SCROLL_STATE_IDLE||scrollState==OnScrollListener.SCROLL_STATE_FLING)
            //并且是最后一条的时候
            if (getLastVisiblePosition()>=getCount()-1){
                //1.显示加载更多布局
                footView.setPadding(0,0,0,0);
                //2.状态改变
                isLoadMore=true;
                //3.回调接口
                if (mOnRefreshListener!=null){
                    mOnRefreshListener.onLoadMore();
                }
            }

        }

        @Override
        public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
        }
    }
}


