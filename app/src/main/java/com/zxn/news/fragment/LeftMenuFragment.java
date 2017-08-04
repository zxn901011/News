package com.zxn.news.fragment;

import android.graphics.Color;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.zxn.news.R;
import com.zxn.news.activity.MainActivity;
import com.zxn.news.base.BaseFragment;
import com.zxn.news.bean.NewsBean;
import com.zxn.news.pager.NewsContentPager;
import com.zxn.news.utils.LogUtil;

import java.util.List;

/**
 * 作者:zxn
 * 作用：左侧菜单的Fragment
 */
public class LeftMenuFragment extends BaseFragment {

    private List<NewsBean.DataEntity> datas;
    private View view;
    private ListView lv_left_content;
    private int screenHeight;

    /**
     * 记录上一个被点击的位置
     */
    private int prePosition;
    private LeftMenuFragmentAdapter adapter;
    private MainActivity activity;

    @Override
    public View initView() {
        LogUtil.e("左侧菜单的Fragment被初始化");
        lv_left_content=new ListView(context);
        DisplayMetrics outmetrics = new DisplayMetrics();
        context.getWindowManager().getDefaultDisplay().getMetrics(outmetrics);
        screenHeight = outmetrics.widthPixels;
        lv_left_content.setPadding(0, (int) (screenHeight*0.3),0,0);
        //设置按下listview按下不变色
        lv_left_content.setDividerHeight(0);
        lv_left_content.setSelector(android.R.color.transparent);
        lv_left_content.setCacheColorHint(Color.TRANSPARENT);
        lv_left_content.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //1.记录点击的位置，变成红色
                prePosition=position;
                adapter.notifyDataSetChanged();
                //2.把左侧菜单关闭
                activity= (MainActivity) context;
                activity.getSlidingMenu().toggle();//关--->开
                //3.切换到对应的详情页面：新闻详情页面，专题详情页面·····
                selectPager(prePosition);
            }
        });
        return lv_left_content;
    }

    private void selectPager(int position) {
        activity= (MainActivity) context;
        ContentFragment contentFragment= activity.getContentFragment();
        NewsContentPager newsContentPager = contentFragment.getNewsCenterPager();
        newsContentPager.SwitchPager(position);
    }
    @Override
    public void initData() {
        super.initData();
        LogUtil.e("左侧页面数据初始化");

    }
    /**
     * 接收数据
     * @param datas
     */
    public void setData(List<NewsBean.DataEntity> datas) {
        this.datas=datas;
        for (int i=0;i<datas.size();i++){
            LogUtil.e(datas.get(i).getTitle());
        }
        adapter=new LeftMenuFragmentAdapter();
        lv_left_content.setAdapter(adapter);
        //设置默认页面
        selectPager(prePosition);
    }


    /**
     * Created by zxn on 2017-08-02.
     */

    class LeftMenuFragmentAdapter extends BaseAdapter {
        @Override
        public int getCount() {
            return datas.size();
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            TextView textView= (TextView) View.inflate(context,R.layout.item_left_menu,null);
            textView.setText(datas.get(position).getTitle());
            textView.setEnabled(position==prePosition);
            return textView;
        }

    }

}
