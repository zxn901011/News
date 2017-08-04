package com.zxn.news.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.zxn.news.R;
import com.zxn.news.utils.CacheUtils;
import com.zxn.news.utils.DensityUtil;

import java.util.ArrayList;

public class GuideActivity extends Activity {

    public static final String BTN_MODE = "btn_mode";
    private ViewPager viewpager;
    private Button btn_guide_main1;
    private Button btn_guide_main2;
    private LinearLayout ll_point_group;
    private ArrayList<ImageView> imageviews;
    private int prePosition = 0;
    private Intent intent;
    private ImageView imageview;
    private ImageView point;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guide);
        findViews();
        initData();
    }

    private void initData() {
        int[] ids = new int[]{
                R.drawable.guide_1,
                R.drawable.guide_2,
                R.drawable.guide_3
        };

        imageviews = new ArrayList<>();
        for (int i = 0; i < ids.length; i++) {
            imageview = new ImageView(this);
            imageview.setBackgroundResource(ids[i]);
            imageviews.add(imageview);
            point = new ImageView(this);
            point.setBackgroundResource(R.drawable.point_color_selector);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(DensityUtil.dip2px(this, 10), DensityUtil.dip2px(this, 10));
            if (i == 0) {
                point.setEnabled(true);
            } else {
                point.setEnabled(false);
            }
            params.leftMargin = DensityUtil.dip2px(this, 10);
            point.setLayoutParams(params);
            ll_point_group.addView(point);
        }
        //设置viewpager的适配器
        viewpager.setAdapter(new MyViewPagerAdapter());
        viewpager.addOnPageChangeListener(new MyOnPageChangeListener());
        btn_guide_main1.setOnClickListener(new MyOnClickListener());
        btn_guide_main2.setOnClickListener(new MyOnClickListener());
    }

    class MyOnPageChangeListener implements ViewPager.OnPageChangeListener {
        /**
         * 当我们的页面滚动了，回调这个方法
         *
         * @param position             当前页面的位置
         * @param positionOffset       滑动页面的百分比
         * @param positionOffsetPixels 在屏幕上滑动的像素
         */
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        }

        /**
         * 当某一个页面被选中的时候回调
         *
         * @param position
         */
        @Override
        public void onPageSelected(int position) {
            if (position == 2) {
                btn_guide_main1.setVisibility(View.VISIBLE);
                btn_guide_main2.setVisibility(View.VISIBLE);
            } else {
                btn_guide_main1.setVisibility(View.GONE);
                btn_guide_main2.setVisibility(View.GONE);
            }
            //把上一个高亮的设置默认-灰色
            //当前的设置为高亮-红色
            ll_point_group.getChildAt(prePosition).setEnabled(false);
            ll_point_group.getChildAt(position).setEnabled(true);
            prePosition = position;
        }

        /**
         * 当页面滑动状态改变的时候回调
         * 静止-->滚动
         * 滑动-->静止
         * 静止-->拖拽
         *
         * @param state
         */
        @Override
        public void onPageScrollStateChanged(int state) {

        }
    }

    private void findViews() {
        viewpager = (ViewPager) findViewById(R.id.vp_pager);
        btn_guide_main1 = (Button) findViewById(R.id.btn_guide_main1);
        btn_guide_main2 = (Button) findViewById(R.id.btn_guide_main2);
        ll_point_group = (LinearLayout) findViewById(R.id.ll_point_group);
    }

    class MyViewPagerAdapter extends PagerAdapter {
        /**
         * 返回数据的总个数
         *
         * @return
         */
        @Override
        public int getCount() {
            return imageviews.size();
        }

        /**
         * 作用：getView
         *
         * @param container viewpager
         * @param position  要创建的位置
         * @return 返回和创建当前页面有关系的值
         */
        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            ImageView imageview = imageviews.get(position);
            container.addView(imageview);
            return imageview;
        }

        /**
         * 销毁视图页面
         *
         * @param container viewpager
         * @param position  销毁的位置
         * @param object    要销毁的页面
         */
        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }

        /**
         * 判断
         *
         * @param view   当前视图
         * @param object 上面instantiateItem返回的结果值
         * @return
         */
        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }
    }

    class MyOnClickListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.btn_guide_main1:
                    //1.保存曾经进入过主界面
                    CacheUtils.putBoolean(GuideActivity.this, SplashActivity.START_MAIN, true);
                    CacheUtils.putInt(GuideActivity.this, GuideActivity.BTN_MODE,1);
                    //进入下一个页面
                    intent = new Intent(GuideActivity.this, HomeActivity.class);
                    startActivity(intent);
                    break;
                case R.id.btn_guide_main2:
                    //1.保存曾经进入过主界面
                    CacheUtils.putBoolean(GuideActivity.this, SplashActivity.START_MAIN, true);
                    CacheUtils.putInt(GuideActivity.this, GuideActivity.BTN_MODE,2);
                    //进入下一个页面
                    intent = new Intent(GuideActivity.this, MainActivity.class);
                    startActivity(intent);
                    break;
            }
        }
    }
}
