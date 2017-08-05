package com.zxn.news.menudetailpager.tabdetailpager;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.gson.Gson;
import com.zxn.news.R;
import com.zxn.news.activity.NewsWebActivity;
import com.zxn.news.base.MenuDetailBasePager;
import com.zxn.news.bean.NewsBean;
import com.zxn.news.bean.TabDetailPagerBean;
import com.zxn.news.utils.CacheUtils;
import com.zxn.news.utils.ConstantUtils;
import com.zxn.news.utils.DensityUtil;
import com.zxn.news.utils.LogUtil;
import com.zxn.news.view.HorizontalScrollViewPager;
import com.zxn.refreshlistview.RefreshListView;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.image.ImageOptions;
import org.xutils.x;

import java.util.List;


/**
 * Created by zxn on 2017-08-03.
 * 页签详情页面基类
 */

public class TabDetailPager extends MenuDetailBasePager {
    private static final String CHILDREN_DATA_URL = "children_data_url";
    public static final String READ_ARRAY_ID = "read_array_id";
    private final NewsBean.DataEntity.ChildrenData childrenData;
    private final ImageOptions imageOptions;
    private String url;
    private HorizontalScrollViewPager view_pager;
    private TextView tv_tab_detile_title;
    private LinearLayout ll_point_group;
    private RefreshListView lv_news_content;
    /**
     * 顶部轮播图数据
     */
    private List<TabDetailPagerBean.DataEntity.TopnewsData> topnewsDataList;

    /**
     * 新闻列表的数据
     */
    private List<TabDetailPagerBean.DataEntity.NewsData> news;
    private MyListViewAdapter adapter;
    /**
     * 加载更多的连接
     */
    private String moreUrl;
    private boolean isLoadMore=false;
    private InternalHandler handler;


    public TabDetailPager(Context context, NewsBean.DataEntity.ChildrenData childrenData) {
        super(context);
        this.childrenData = childrenData;
        imageOptions = new ImageOptions.Builder()
                .setSize(DensityUtil.dip2px(context, 130), DensityUtil.dip2px(context, 100))
                .setRadius(DensityUtil.dip2px(context, 5))
                .setCrop(true)
                .setImageScaleType(ImageView.ScaleType.CENTER_CROP)
                .setLoadingDrawableId(R.drawable.news_pic_default)
                .setFailureDrawableId(R.drawable.news_pic_default)
                .build();
    }

    @Override
    public View initView() {
        View view = View.inflate(context, R.layout.tab_detail_pager, null);
        lv_news_content = (RefreshListView) view.findViewById(R.id.lv_news_content);
        View topNewsView = View.inflate(context, R.layout.topnews, null);
        view_pager = (HorizontalScrollViewPager) topNewsView.findViewById(R.id.view_pager);
        tv_tab_detile_title = (TextView) topNewsView.findViewById(R.id.tv_tab_detile_title);
        ll_point_group = (LinearLayout) topNewsView.findViewById(R.id.ll_point_group);
        //而且要把顶部轮播图部分，以子布局的形式加入到listview中
//        lv_news_content.addHeaderView(topNewsView);
        lv_news_content.addTopNewsView(topNewsView);
        //设置listview的下拉刷新监听事件
        lv_news_content.setOnRefreshListenenr(new MyOnRefreshListener());
        //设置listview的点击监听
        lv_news_content.setOnItemClickListener(new MyItemClickListener());
        return view;
    }

    @Override
    public void initData() {
        super.initData();
        url = ConstantUtils.BASE_URL + childrenData.getUrl();
        prePosition = 0;
        String saveJson = CacheUtils.getString(context, CHILDREN_DATA_URL);
        if (!TextUtils.isEmpty(saveJson)) {
            parseData(saveJson);
        }
        getDataFromNet();
    }

    /**
     * 之前高亮的位置
     */
    private int prePosition;

    private void parseData(String result) {
        TabDetailPagerBean bean = parseJson(result);
        LogUtil.e(bean.getData().getNews().get(0).getTitle());
        if (TextUtils.isEmpty(bean.getData().getMore())) {
            moreUrl = "";
        } else {
            moreUrl = ConstantUtils.BASE_URL + bean.getData().getMore();
        }
        LogUtil.e("加载更多的地址："+moreUrl);
        //默认和加载更多
        if (!isLoadMore){
            topnewsDataList = bean.getData().getTopnews();
            view_pager.setAdapter(new MyTopNewsAdapter());
            addPoint();
            //监听页面的改变，设置文本变化和红点变化
            if (topnewsDataList != null && topnewsDataList.size() > 0) {
                tv_tab_detile_title.setText(topnewsDataList.get(prePosition).getTitle());
            }
            view_pager.addOnPageChangeListener(new MyOnPageChangerListener());
            //设置listview的集合数据
            news = bean.getData().getNews();
            //设置listview的适配器
            adapter = new MyListViewAdapter();
            lv_news_content.setAdapter(adapter);
        }else {
            isLoadMore=false;
            news.addAll(bean.getData().getNews());
            //刷新适配器
            adapter.notifyDataSetChanged();
        }

        //发消息每隔3秒，切换一次viewpager页面
        if (handler==null) {
            handler = new InternalHandler();
        }
        //是把消息队列中所有消息和回调移除
        handler.removeCallbacksAndMessages(null);
        //是3秒后执行这个MyRunnable中的run方法
        handler.postDelayed(new MyRunnable(),3000);//可以发任务
    }

    class MyRunnable implements Runnable {
        @Override
        public void run() {
            handler.sendEmptyMessage(0);
        }
    }
    /**
     * 自定义一个Handler类
     */
    class InternalHandler extends Handler{
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            //切换viewpager的下一个页面
            int item=(view_pager.getCurrentItem()+1)%topnewsDataList.size();
            view_pager.setCurrentItem(item);
            handler.sendEmptyMessageDelayed(0,3000);
        }
    }

    private void addPoint() {
        ll_point_group.removeAllViews();//刚开始移除所有的红点
        for (int i = 0; i < topnewsDataList.size(); i++) {
            ImageView pointImage = new ImageView(context);
            pointImage.setBackgroundResource(R.drawable.point_selector);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(DensityUtil.dip2px(context, 8),
                    DensityUtil.dip2px(context, 8));
            if (i == 0) {
                pointImage.setEnabled(true);
            } else {
                pointImage.setEnabled(false);
                params.leftMargin = DensityUtil.dip2px(context, 8);
            }
            pointImage.setLayoutParams(params);
            ll_point_group.addView(pointImage);
        }
    }

    private TabDetailPagerBean parseJson(String result) {
        return new Gson().fromJson(result, TabDetailPagerBean.class);
    }

    private void getDataFromNet() {
        RequestParams params = new RequestParams(url);
        params.setConnectTimeout(4000);
        x.http().get(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                CacheUtils.putString(context, CHILDREN_DATA_URL, result);
//                LogUtil.e("联网成功:"+result);
                parseData(result);
                //隐藏下拉刷新控件-更新时间
                lv_news_content.onRefreshFinish(true);
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                LogUtil.e("联网失败:" + ex.getMessage());
                //不更新时间
                lv_news_content.onRefreshFinish(false);
            }

            @Override
            public void onCancelled(CancelledException cex) {
                LogUtil.e("联网取消:" + cex.getMessage());
            }

            @Override
            public void onFinished() {
                LogUtil.e("联网结束:");
            }
        });
    }


    class MyTopNewsAdapter extends PagerAdapter {
        @Override
        public int getCount() {
            return topnewsDataList.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            ImageView imageView = new ImageView(context);
            //x轴和Y轴拉伸
            imageView.setScaleType(ImageView.ScaleType.FIT_XY);
            //设置图片默认为背景
            imageView.setBackgroundResource(R.drawable.home_scroll_default);
            //将图片设置到容器中
            container.addView(imageView);
            TabDetailPagerBean.DataEntity.TopnewsData topnewsData = topnewsDataList.get(position);
            String imageUrl = ConstantUtils.BASE_URL + topnewsData.getTopimage();
            x.image().bind(imageView, imageUrl, imageOptions);//联网请求图片
            imageView.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    switch (event.getAction()){
                        case MotionEvent.ACTION_DOWN:
                            handler.removeCallbacksAndMessages(null);
                            break;
                        case MotionEvent.ACTION_MOVE:
                            break;
                        case MotionEvent.ACTION_UP:
                            handler.removeCallbacksAndMessages(null);
                            handler.sendEmptyMessageDelayed(0,1000);
                            break;
                    }
                    return true;
                }
            });
            return imageView;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }
    }

    class MyOnPageChangerListener implements ViewPager.OnPageChangeListener {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {
            //1.设置文本
            tv_tab_detile_title.setText(topnewsDataList.get(position).getTitle());
            //2.红点高亮
            //把之前的变成灰色
            ll_point_group.getChildAt(prePosition).setEnabled(false);
            //把当前的设置成红色
            ll_point_group.getChildAt(position).setEnabled(true);
            prePosition = position;
        }

        private boolean isDragging=false;
        @Override
        public void onPageScrollStateChanged(int state) {
            if(state==ViewPager.SCROLL_STATE_DRAGGING){//拖拽
                isDragging=true;
                LogUtil.e("拖拽");
                //拖拽时移除消息
                handler.removeCallbacksAndMessages(null);
            }else if (state==ViewPager.SCROLL_STATE_SETTLING&&isDragging){//惯性
                //发消息
                isDragging=false;
                LogUtil.e("惯性");
                handler.removeCallbacksAndMessages(null);
                handler.sendEmptyMessageDelayed(0,3000);
            }else if (state==ViewPager.SCROLL_STATE_IDLE&&isDragging){//静止
                isDragging=false;
                LogUtil.e("静止");
                handler.removeCallbacksAndMessages(null);
                handler.sendEmptyMessageDelayed(0,3000);
            }
        }
    }

    class MyListViewAdapter extends BaseAdapter {
        @Override
        public int getCount() {
            return news.size();
        }

        @Override
        public Object getItem(int position) {
            return news.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder;
            if (convertView == null) {
                convertView = View.inflate(context, R.layout.news_content, null);
                holder = new ViewHolder();
                holder.iv_news_image = (ImageView) convertView.findViewById(R.id.iv_news_image);
                holder.tv_desc = (TextView) convertView.findViewById(R.id.tv_desc);
                holder.tv_date = (TextView) convertView.findViewById(R.id.tv_date);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            TabDetailPagerBean.DataEntity.NewsData newsData = news.get(position);
            String imageUrl = ConstantUtils.BASE_URL + newsData.getListimage();
//            x.image().bind(holder.iv_news_image,imageUrl,imageOptions);
            //请求图片使用glide
            Glide.with(context)
                    .load(imageUrl)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(holder.iv_news_image);

            holder.tv_desc.setText(newsData.getTitle());
            holder.tv_date.setText(newsData.getPubdate());

            String idArray=CacheUtils.getString(context,READ_ARRAY_ID);
            if (idArray.contains(newsData.getId()+"")){
                //设置灰色
                holder.tv_desc.setTextColor(Color.GRAY);
            }else {
                //还原成黑色
                holder.tv_desc.setTextColor(Color.BLACK);
            }
            return convertView;
        }
    }

    static class ViewHolder {
        ImageView iv_news_image;
        TextView tv_desc;
        TextView tv_date;
    }

    class MyOnRefreshListener implements RefreshListView.OnRefreshListener {
        @Override
        public void onPullDownRefresh() {
            Toast.makeText(context, "你在下拉刷新", Toast.LENGTH_SHORT).show();
            getDataFromNet();
        }

        @Override
        public void onLoadMore() {
            if (TextUtils.isEmpty(moreUrl)){
                Toast.makeText(context, "没有更多数据", Toast.LENGTH_SHORT).show();
                lv_news_content.onRefreshFinish(false);
            }else {
                getMoreDataFromNet();
            }
        }
    }

    private void getMoreDataFromNet() {
        RequestParams params = new RequestParams(moreUrl);
        params.setConnectTimeout(4000);
        x.http().get(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                LogUtil.e("联网成功:"+result);
                //解析数据
                isLoadMore=true;
                //解析数据
                parseData(result);
                lv_news_content.onRefreshFinish(false);
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                LogUtil.e("联网失败:"+ex.getMessage());
            }

            @Override
            public void onCancelled(CancelledException cex) {
                LogUtil.e("联网取消:"+cex.getMessage());
            }

            @Override
            public void onFinished() {
                LogUtil.e("联网结束:");
            }
        });
    }

    class MyItemClickListener implements AdapterView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            int realPosition=position-1;
            TabDetailPagerBean.DataEntity.NewsData newsData = news.get(realPosition);
//            Toast.makeText(context,"newsData==id"+newsData.getId()+"newsData==title"+newsData.getTitle(),Toast.LENGTH_SHORT).show();
            LogUtil.e("newsData==id"+newsData.getId()+"newsData==title"+newsData.getTitle()+"newsData==url"+newsData.getUrl());
            //1.取出保存的id集合
            String idArray=CacheUtils.getString(context, READ_ARRAY_ID);//第一次进来啥也没有，是一个""!!!
            //2.判断是否存在，如果不存在，才保存，并且刷新适配器
            if (!idArray.contains(newsData.getId()+"")){//不包含3511
                CacheUtils.putString(context,READ_ARRAY_ID,idArray+newsData.getId()+",");//第一次什么都没有，所以第一个保存的就是3511
                //刷新适配器
                adapter.notifyDataSetChanged();
            }
            Intent intent=new Intent(context,NewsWebActivity.class);
            intent.putExtra("url",ConstantUtils.BASE_URL+newsData.getUrl());
            context.startActivity(intent);
        }

    }
}
