package com.zxn.news.fragment;


import android.support.annotation.IdRes;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.RadioGroup;

import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.zxn.news.R;
import com.zxn.news.activity.MainActivity;
import com.zxn.news.adapter.ContentFragmentAdapter;
import com.zxn.news.base.BaseFragment;
import com.zxn.news.base.BasePager;
import com.zxn.news.pager.GovaffairPager;
import com.zxn.news.pager.HomePager;
import com.zxn.news.pager.NewsContentPager;
import com.zxn.news.pager.SettingPager;
import com.zxn.news.pager.SmartPager;
import com.zxn.news.utils.LogUtil;
import com.zxn.news.view.NoScrollViewPager;

import java.util.ArrayList;

/**
 * 作者：zxn
 */
public final class ContentFragment extends BaseFragment {


    private NoScrollViewPager contentViewpager;
    private RadioGroup rgContentBottom;

    /**
     * 建立五个页面的集合
     */
    private ArrayList<BasePager> basePagers;
    private MainActivity activity;


    @Override
    public View initView() {
        LogUtil.e("正文的Fragment被初始化");
        View view = View.inflate(context, R.layout.fragment_content, null);
        contentViewpager = (NoScrollViewPager) view.findViewById(R.id.content_viewpager);
        rgContentBottom = (RadioGroup) view.findViewById(R.id.rg_content_bottom);
        return view;
    }

    @Override
    public void initData() {
        super.initData();
        LogUtil.e("正文的数据被初始化");
        basePagers=new ArrayList<>();
        basePagers.add(new HomePager(context));
        basePagers.add(new NewsContentPager(context));
        basePagers.add(new SmartPager(context));
        basePagers.add(new GovaffairPager(context));
        basePagers.add(new SettingPager(context));
        rgContentBottom.check(R.id.rb_home);
        contentViewpager.setAdapter(new ContentFragmentAdapter(basePagers));
        //设置RadioGroup的选中状态改变的监听
        rgContentBottom.setOnCheckedChangeListener(new MyOnCheckedChangeListener());
        //监听我们的页面改变
        contentViewpager.addOnPageChangeListener(new MyOnPageChangeListener());
        basePagers.get(0).initData();
        setSlingMenuEnable(SlidingMenu.TOUCHMODE_NONE);
    }

    class MyOnCheckedChangeListener implements RadioGroup.OnCheckedChangeListener {
        @Override
        public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
            switch (checkedId){
                case R.id.rb_home:
                    contentViewpager.setCurrentItem(0,false);
                    setSlingMenuEnable(SlidingMenu.TOUCHMODE_NONE);
                    break;
                case R.id.rb_news_center:
                    contentViewpager.setCurrentItem(1,false);
                    setSlingMenuEnable(SlidingMenu.TOUCHMODE_FULLSCREEN);
                    break;
                case R.id.rb_smart_service:
                    contentViewpager.setCurrentItem(2,false);
                    setSlingMenuEnable(SlidingMenu.TOUCHMODE_NONE);

                    break;
                case R.id.rb_govaf_fair:
                    contentViewpager.setCurrentItem(3,false);
                    setSlingMenuEnable(SlidingMenu.TOUCHMODE_NONE);
                    break;
                case R.id.rb_setting:
                    contentViewpager.setCurrentItem(4,false);
                    setSlingMenuEnable(SlidingMenu.TOUCHMODE_NONE);
                    break;
            }
        }
    }

    private void setSlingMenuEnable(int TouchMode) {
        activity= (MainActivity) context;
        activity.getSlidingMenu().setTouchModeAbove(TouchMode);
    }
    //nativeViewPager可以设置来回滑动，选中按钮被选择的状态！！重写之后就没法滑动了

    class MyOnPageChangeListener implements ViewPager.OnPageChangeListener {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }
        @Override
        public void onPageSelected(int position) {
//            if (position==0){
//                rgContentBottom.check(R.id.rb_home);
//            }else if (position==1){
//                rgContentBottom.check(R.id.rb_news_center);
//            }else if (position==2){
//                rgContentBottom.check(R.id.rb_smart_service);
//            }else if (position==3){
//                rgContentBottom.check(R.id.rb_govaf_fair);
//            }else if (position==4){
//                rgContentBottom.check(R.id.rb_setting);
//            }
            basePagers.get(position).initData();//各个页面的实例
        }
        @Override
        public void onPageScrollStateChanged(int state) {

        }
    }
}
