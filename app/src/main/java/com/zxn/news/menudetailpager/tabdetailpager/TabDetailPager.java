package com.zxn.news.menudetailpager.tabdetailpager;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.gson.Gson;
import com.zxn.news.R;
import com.zxn.news.base.MenuDetailBasePager;
import com.zxn.news.bean.NewsBean;
import com.zxn.news.bean.TabDetailPagerBean;
import com.zxn.news.utils.CacheUtils;
import com.zxn.news.utils.ConstantUtils;
import com.zxn.news.utils.DensityUtil;
import com.zxn.news.utils.LogUtil;

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
    private final NewsBean.DataEntity.ChildrenData childrenData;
    private String url;
    private ViewPager view_pager;
    private TextView tv_tab_detile_title;
    private LinearLayout ll_point_group;
    private ListView lv_news_content;
    private TabDetailPagerBean bean;
//    private ImageOptions imageOptions;
    /**
     * 顶部轮播图数据
     */
    private List<TabDetailPagerBean.DataEntity.TopnewsData> topnewsDataList;
    private TabDetailPagerBean.DataEntity.TopnewsData topnewsData;
    /**
     * 之前高亮的位置
     */
    private int prePosition=0;
    /**
     * 新闻列表的数据
     */
    private List<TabDetailPagerBean.DataEntity.NewsData> news;
    private MyListViewAdapter adapter;


    public TabDetailPager(Context context, NewsBean.DataEntity.ChildrenData childrenData) {
        super(context);
        this.childrenData=childrenData;
//        imageOptions=new ImageOptions.Builder()
//                .setSize(DensityUtil.dip2px(context,130),DensityUtil.dip2px(context,100))
//                .setRadius(DensityUtil.dip2px(context,5))
//                .setCrop(true)
//                .setImageScaleType(ImageView.ScaleType.CENTER_CROP)
//                .setLoadingDrawableId(R.drawable.news_pic_default)
//                .setFailureDrawableId(R.drawable.news_pic_default)
//                .build();
    }

    @Override
    public View initView() {
        View view=View.inflate(context, R.layout.tab_detail_pager,null);
        lv_news_content= (ListView) view.findViewById(R.id.lv_news_content);
        View topNewsView=View.inflate(context,R.layout.topnews,null);
        view_pager= (ViewPager) topNewsView.findViewById(R.id.view_pager);
        tv_tab_detile_title= (TextView) topNewsView.findViewById(R.id.tv_tab_detile_title);
        ll_point_group= (LinearLayout) topNewsView.findViewById(R.id.ll_point_group);
        //而且要把顶部轮播图部分，以子布局的形式加入到listview中
        lv_news_content.addHeaderView(topNewsView);
        return view;
    }

    @Override
    public void initData() {
        super.initData();
        url= ConstantUtils.BASE_URL+childrenData.getUrl();
        LogUtil.e("childrenData的联网地址："+url);
        String saveJson=CacheUtils.getString(context,CHILDREN_DATA_URL);
        if (!TextUtils.isEmpty(saveJson)){
            parseData(saveJson);
        }
        getDataFromNet();
    }

    /**
     * 解析数据
     * @param saveJson
     */
    private void parseData(String result) {
        bean=parseJson(result);
        LogUtil.e(bean.getData().getNews().get(0).getTitle());
        topnewsDataList=bean.getData().getTopnews();
        view_pager.setAdapter(new MyTopNewsAdapter());

        ll_point_group.removeAllViews();//刚开始移除所有的红点
        for (int i=0;i<topnewsDataList.size();i++){
            ImageView pointImage=new ImageView(context);
            pointImage.setBackgroundResource(R.drawable.point_selector);
            LinearLayout.LayoutParams params=new LinearLayout.LayoutParams(DensityUtil.dip2px(context,8),
                    DensityUtil.dip2px(context,8));
            if (i == 0) {
                pointImage.setEnabled(true);
            }else {
                pointImage.setEnabled(false);
                params.leftMargin=DensityUtil.dip2px(context,8);
            }
            pointImage.setLayoutParams(params);
            ll_point_group.addView(pointImage);
        }
        //监听页面的改变，设置文本变化和红点变化
        view_pager.addOnPageChangeListener(new MyOnPageChangerListener());
        tv_tab_detile_title.setText(topnewsDataList.get(prePosition).getTitle());

        //设置listview的集合数据
        news=bean.getData().getNews();
        //设置listview的适配器
        adapter=new MyListViewAdapter();
        lv_news_content.setAdapter(adapter);
    }

    private TabDetailPagerBean parseJson(String result) {
        return new Gson().fromJson(result,TabDetailPagerBean.class);
    }


    private void getDataFromNet() {
        RequestParams params=new RequestParams(url);
        x.http().get(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                CacheUtils.putString(context,CHILDREN_DATA_URL,result);
//                LogUtil.e("联网成功:"+result);
                parseData(result);
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


    class MyTopNewsAdapter extends PagerAdapter {
        @Override
        public int getCount() {
            return topnewsDataList.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view==object;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            ImageView imageView=new ImageView(context);
            //x轴和Y轴拉伸
            imageView.setScaleType(ImageView.ScaleType.FIT_XY);
            //设置图片默认为背景
            imageView.setBackgroundResource(R.drawable.home_scroll_default);
            //将图片设置到容器中
            container.addView(imageView);
            topnewsData=topnewsDataList.get(position);
            String imageUrl=ConstantUtils.BASE_URL+topnewsData.getTopimage();
            x.image().bind(imageView,imageUrl);//联网请求图片
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
            prePosition=position;
        }

        @Override
        public void onPageScrollStateChanged(int state) {
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
            if (convertView==null){
                convertView=View.inflate(context,R.layout.news_content,null);
                holder=new ViewHolder();
                holder.iv_news_image= (ImageView) convertView.findViewById(R.id.iv_news_image);
                holder.tv_desc= (TextView) convertView.findViewById(R.id.tv_desc);
                holder.tv_date= (TextView) convertView.findViewById(R.id.tv_date);
                convertView.setTag(holder);
            }else{
                holder= (ViewHolder) convertView.getTag();
            }
            TabDetailPagerBean.DataEntity.NewsData newsData=news.get(position);
            String imageUrl=ConstantUtils.BASE_URL+newsData.getListimage();
//            x.image().bind(holder.iv_news_image,imageUrl,imageOptions);
            //请求图片使用glide
            Glide.with(context)
                    .load(imageUrl)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(holder.iv_news_image);

            holder.tv_desc.setText(newsData.getTitle());
            holder.tv_date.setText(newsData.getPubdate());
            return convertView;
        }
    }
    static class ViewHolder{

        ImageView iv_news_image;
        TextView tv_desc;
        TextView tv_date;
    }
}
