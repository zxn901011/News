package com.zxn.news.fragment;


import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.zxn.news.R;
import com.zxn.news.base.BaseFragment;
import com.zxn.news.utils.LogUtil;

/**
 * A simple {@link Fragment} subclass.
 * 作者：zxn
 */
public class ContentFragment extends BaseFragment {


    private ViewPager contentViewpager;
    private RadioGroup rgContentBottom;
    private RadioButton rbHome;
    private RadioButton rbNewsCenter;
    private RadioButton rbSmartService;
    private RadioButton rbGovafFair;
    private RadioButton rbSetting;

    private void setListener() {
        rbHome.setOnClickListener(new MyOnClickListener());
        rbNewsCenter.setOnClickListener(new MyOnClickListener());
        rbSmartService.setOnClickListener(new MyOnClickListener());
        rbGovafFair.setOnClickListener(new MyOnClickListener());
        rbSetting.setOnClickListener(new MyOnClickListener());
    }


    @Override
    public View initView() {
        LogUtil.e("正文的Fragment被初始化");
        View view = View.inflate(context, R.layout.fragment_content, null);
        contentViewpager = (ViewPager) view.findViewById(R.id.content_viewpager);
        rgContentBottom = (RadioGroup) view.findViewById(R.id.rg_content_bottom);
        rbHome = (RadioButton) view.findViewById(R.id.rb_home);
        rbNewsCenter = (RadioButton) view.findViewById(R.id.rb_news_center);
        rbSmartService = (RadioButton) view.findViewById(R.id.rb_smart_service);
        rbGovafFair = (RadioButton) view.findViewById(R.id.rb_govaf_fair);
        rbSetting = (RadioButton) view.findViewById(R.id.rb_setting);
        setListener();
        return view;
    }

    @Override
    public void initData() {
        super.initData();
        LogUtil.e("正文的数据被初始化");
        rgContentBottom.check(R.id.rb_home);
    }
    class MyOnClickListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.rb_home:

                    break;
                case R.id.rb_news_center:

                    break;
                case R.id.rb_smart_service:

                    break;
                case R.id.rb_govaf_fair:

                    break;
                case R.id.rb_setting:

                    break;
            }
        }
    }
}
