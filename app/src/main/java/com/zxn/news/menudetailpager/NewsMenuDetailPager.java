package com.zxn.news.menudetailpager;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.viewpagerindicator.TabPageIndicator;
import com.zxn.news.R;
import com.zxn.news.activity.MainActivity;
import com.zxn.news.base.MenuDetailBasePager;
import com.zxn.news.bean.NewsBean;
import com.zxn.news.menudetailpager.tabdetailpager.TabDetailPager;
import com.zxn.news.utils.LogUtil;

import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zxn on 2017-08-02.
 */

public class NewsMenuDetailPager extends MenuDetailBasePager {
    private final List<NewsBean.DataEntity.ChildrenData> childData;
    //xutils加载布局
    @ViewInject(R.id.view_pager)
    private ViewPager view_pager;
    @ViewInject(R.id.tpi_tab)
    private TabPageIndicator tpi_tab;
    @ViewInject(R.id.iv_tpi_select_next)
    private ImageButton iv_tpi_select_next;
    /**
     * 页签页面的集合
     */
    private ArrayList<TabDetailPager> tabDetailPagers;
    private MainActivity activity;
    private int tempPosition=0;

    public NewsMenuDetailPager(Context context, NewsBean.DataEntity dataEntity) {
        super(context);
        this.childData=dataEntity.getChildren();
    }

    @Override
    public View initView() {
        View view=View.inflate(context, R.layout.news_menu_detail_pager,null);
        x.view().inject(NewsMenuDetailPager.this,view);
        //设置点击事件
        iv_tpi_select_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                view_pager.setCurrentItem(view_pager.getCurrentItem()+1);
            }
        });
        return view;
    }
    @Override
    public void initData() {
        super.initData();
        //新闻页面被初始化了
        LogUtil.e("新闻详情页面被初始化了");
        //准备新闻详情页面的数据
        tabDetailPagers=new ArrayList<>();
        for (int i=0;i<childData.size();i++){
            tabDetailPagers.add(new TabDetailPager(context,childData.get(i)));
        }
        //设置ViewPager的适配器
        view_pager.setAdapter(new MyNewsMenuDetailPagerAdapter());
        //viewpager和TabPagerIndicator关联起来
        tpi_tab.setViewPager(view_pager);
        tpi_tab.setOnPageChangeListener(new MyOnPageChangeListener());
        view_pager.setCurrentItem(tempPosition);
    }
    class MyNewsMenuDetailPagerAdapter extends PagerAdapter{
        @Override
        public CharSequence getPageTitle(int position) {
            return childData.get(position).getTitle();
        }

        @Override
        public int getCount() {
            return tabDetailPagers.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view==object;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            TabDetailPager tabDetailPager= tabDetailPagers.get(position);
            View rootView=tabDetailPager.rootView;
            //一定要初始化数据，不然页面肯定没有数据，只能滑动
            tabDetailPager.initData();
            container.addView(rootView);
            return rootView;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }
    }

    class MyOnPageChangeListener implements ViewPager.OnPageChangeListener {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        }

        @Override
        public void onPageSelected(int position) {
            if(position==0){
                //slidingmenu可以全屏滑动
                setSlingMenuEnable(SlidingMenu.TOUCHMODE_FULLSCREEN);
            }else {
                //slidingmenu不能滑动
                setSlingMenuEnable(SlidingMenu.TOUCHMODE_NONE);
            }
            tempPosition=position;
        }

        @Override
        public void onPageScrollStateChanged(int state) {
        }
    }
    private void setSlingMenuEnable(int TouchMode) {
        activity= (MainActivity) context;
        activity.getSlidingMenu().setTouchModeAbove(TouchMode);
    }
}
