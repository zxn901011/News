package com.zxn.news.pager;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;

import com.google.gson.Gson;
import com.zxn.news.activity.MainActivity;
import com.zxn.news.base.BasePager;
import com.zxn.news.base.MenuDetailBasePager;
import com.zxn.news.bean.NewsBean;
import com.zxn.news.fragment.LeftMenuFragment;
import com.zxn.news.menudetailpager.InteractMenuDetailPager;
import com.zxn.news.menudetailpager.NewsMenuDetailPager;
import com.zxn.news.menudetailpager.PhotosMenuDetailPager;
import com.zxn.news.menudetailpager.TopicMenuDetailPager;
import com.zxn.news.menudetailpager.TouPiaoMenuDetailPager;
import com.zxn.news.utils.CacheUtils;
import com.zxn.news.utils.ConstantUtils;
import com.zxn.news.utils.LogUtil;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zxn on 2017-08-02.
 * 新闻
 */

public class NewsContentPager extends BasePager {
    private RequestParams params;
    private NewsBean bean;
    private List<NewsBean.DataEntity> datas;
    private ArrayList<MenuDetailBasePager> menuDetailBasePagers;
    private MainActivity mainActivity;

    public NewsContentPager(Context context) {
        super(context);
    }
    @Override
    public View initView() {
        return super.initView();
    }

    @Override
    public void initData() {
        super.initData();
        ib_menu.setVisibility(View.VISIBLE);
//        TextView textView=new TextView(context);
//        textView.setTextColor(Color.RED);
//        textView.setGravity(Gravity.CENTER);
//        textView.setTextSize(25);
//        //将视图加入到BasePager的FrameLayout中
//        fl_base_pager.addView(textView);
//        textView.setText("我是新闻的内容");
        //联网请求数据用xutils3
        String result=CacheUtils.getString(context,ConstantUtils.NEWS_CENTER_URL);
        if (!TextUtils.isEmpty(result)){
            parseData(result);
        }
        //联网请求数据，运行两次
        getDataFromNet();
    }

    private void getDataFromNet() {
        params=new RequestParams(ConstantUtils.NEWS_CENTER_URL);
        x.http().get(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
//                LogUtil.e("使用xutils3联网请求成功"+result);
                //数据缓存
                CacheUtils.putString(context,ConstantUtils.NEWS_CENTER_URL,result);
                parseData(result);
                //设置适配器
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                LogUtil.e("使用xutils3联网请求失败"+ex.getMessage());
            }

            @Override
            public void onCancelled(CancelledException cex) {
                LogUtil.e("使用xutils3联网请求取消"+cex.getMessage());
            }

            @Override
            public void onFinished() {
                LogUtil.e("使用xutils3联网请求结束");
            }
        });
    }

    private void parseData(String result) {
        bean=parseJson(result);
        String str=bean.getData().get(0).getChildren().get(1).getTitle();
        LogUtil.e(str);
        //给左侧菜单传递数据
        datas=bean.getData();
        mainActivity= (MainActivity) context;
        LeftMenuFragment leftMenuFragment=mainActivity.getLeftMemuFragment();
        menuDetailBasePagers=new ArrayList<>();
        menuDetailBasePagers.add(new NewsMenuDetailPager(context,datas.get(0)));
        menuDetailBasePagers.add(new TopicMenuDetailPager(context,datas.get(0)));
        menuDetailBasePagers.add(new PhotosMenuDetailPager(context));
        menuDetailBasePagers.add(new InteractMenuDetailPager(context));
        menuDetailBasePagers.add(new TouPiaoMenuDetailPager(context));
        //给左侧菜单传递数据
        leftMenuFragment.setData(datas);

    }

    /**
     * 解析json数据
     * @param result
     */
    private NewsBean parseJson(String result) {
        return new Gson().fromJson(result,NewsBean.class);
    }

    public void SwitchPager(int position) {
        //设置标题
        tv_title.setText(datas.get(position).getTitle());
        //2.移除之前的内容
        fl_base_pager.removeAllViews();
        //3.添加新的内容
        MenuDetailBasePager detailBasePager=menuDetailBasePagers.get(position);
        detailBasePager.initData();
        View view=detailBasePager.rootView;
        fl_base_pager.addView(view);
    }
}
