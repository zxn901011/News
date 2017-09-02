package com.zxn.news.menudetailpager;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.StringRequest;
import com.google.gson.Gson;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;
import com.zxn.news.R;
import com.zxn.news.activity.ShowImageActivity;
import com.zxn.news.base.MenuDetailBasePager;
import com.zxn.news.bean.NewsBean;
import com.zxn.news.bean.PhotosMenuDetailPagerBean;
import com.zxn.news.utils.CacheUtils;
import com.zxn.news.utils.ConstantUtils;
import com.zxn.news.utils.LogUtil;
import com.zxn.news.volley.VolleyManager;

import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.util.List;

import okhttp3.Call;

/**
 * Created by zxn on 2017-08-02.
 */

public class PhotosMenuDetailPager extends MenuDetailBasePager {

    private final NewsBean.DataEntity dataEntity;
    private ListView lv_photos_menu;
    private GridView gv_photos_menu;
    /**
     * 联网请求数据的url
     */
    private String photosUrl;
    private MyPhotoListAdapter adapter;
    private List<PhotosMenuDetailPagerBean.DataEntity.NewsEntity> photoNews;
    public PhotosMenuDetailPager(Context context, NewsBean.DataEntity dataEntity) {
        super(context);
        this.dataEntity=dataEntity;
    }

    @Override
    public View initView() {
        View view=View.inflate(context, R.layout.photos_menu_detail_pager,null);
        lv_photos_menu= (ListView) view.findViewById(R.id.lv_photos_menu);
        gv_photos_menu= (GridView) view.findViewById(R.id.gv_photos_menu);
        lv_photos_menu.setOnItemClickListener(new MyItemClickListener());
        gv_photos_menu.setOnItemClickListener(new MyItemClickListener());
        return view;
    }

    @Override
    public void initData() {
        super.initData();
        photosUrl= ConstantUtils.BASE_URL+dataEntity.getUrl();
        String saveJson=CacheUtils.getString(context,photosUrl);
        if (!TextUtils.isEmpty(saveJson)){
            parseData(saveJson);
        }
        getDataFromNetByOkHttpUtils();
//        getDataFromNet();
    }

    private void getDataFromNetByOkHttpUtils() {
        OkHttpUtils
                .get()
                .url(photosUrl)
                .id(100)
                .build()
                .execute(new MyStringCallback());
    }

    private void getDataFromNet() {
        //请求队列，因为已经初始化了volley,所以不用先请求一个消息队列了
//        RequestQueue queue= Volley.newRequestQueue(context);
        //String请求
        StringRequest request=new StringRequest(Request.Method.GET, photosUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String result) {
                LogUtil.e("使用volley联网请求成功"+result);
                //数据缓存
                CacheUtils.putString(context,photosUrl,result);
                parseData(result);
                //设置适配器
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                LogUtil.e("使用volley联网请求失败"+volleyError.getMessage());
            }
        }){
            @Override
            protected Response<String> parseNetworkResponse(NetworkResponse response) {
                try {
                    String parsed=new String(response.data, "utf-8");
                    return Response.success(parsed, HttpHeaderParser.parseCacheHeaders(response));
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
                return super.parseNetworkResponse(response);
            }
        };
        //添加到队列中,封装成了单例
        VolleyManager.getRequestQueue().add(request);
    }

    /**
     * 解析和显示数据
     * @param result
     */
    private void parseData(String result) {
        PhotosMenuDetailPagerBean bean=parseJson(result);
        LogUtil.e("标题==="+bean.getData().getNews().get(0).getTitle());
        isShowListView=true;
        photoNews = bean.getData().getNews();
        adapter=new MyPhotoListAdapter();
        lv_photos_menu.setAdapter(adapter);
    }

    /**
     * true是显示ListView
     * flase显示GridView
     */
    private boolean isShowListView=true;
    public void switchListToGrid(ImageButton ib_switch_list_grid) {
        if (isShowListView){
            //显示GridView
            gv_photos_menu.setVisibility(View.VISIBLE);
            adapter=new MyPhotoListAdapter();
            gv_photos_menu.setAdapter(adapter);
            lv_photos_menu.setVisibility(View.GONE);
            //按钮显示--ListView
            ib_switch_list_grid.setImageResource(R.drawable.icon_pic_list_type);
            isShowListView=false;
        }else {
            //显示GridView
            lv_photos_menu.setVisibility(View.VISIBLE);
            adapter=new MyPhotoListAdapter();
            lv_photos_menu.setAdapter(adapter);
            gv_photos_menu.setVisibility(View.GONE);
            //按钮显示--ListView
            ib_switch_list_grid.setImageResource(R.drawable.icon_pic_grid_type);
            isShowListView=true;
        }
    }

    private PhotosMenuDetailPagerBean parseJson(String result) {
        return new Gson().fromJson(result,PhotosMenuDetailPagerBean.class);
    }
    class MyPhotoListAdapter extends BaseAdapter {
        private final DisplayImageOptions options;

        public MyPhotoListAdapter(){
            options = new DisplayImageOptions.Builder()
                    .showImageOnLoading(R.drawable.home_scroll_default)
                    .showImageForEmptyUri(R.drawable.home_scroll_default)
                    .showImageOnFail(R.drawable.home_scroll_default)
                    .cacheInMemory(true)//缓存在内存中
                    .cacheOnDisk(true)//缓存在sd卡中
                    .considerExifParams(true)
                    .bitmapConfig(Bitmap.Config.RGB_565)
                    .displayer(new RoundedBitmapDisplayer(15))
                    .build();
        }

        @Override
        public int getCount() {
            return photoNews.size();
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder;
            if (convertView==null){
                convertView=View.inflate(context,R.layout.item_photos_menu_pager,null);
                holder=new ViewHolder();
                holder.iv_photos_default_menu= (ImageView) convertView.findViewById(R.id.iv_photos_default_menu);
                holder.tv_photo_title= (TextView) convertView.findViewById(R.id.tv_photo_title);
                convertView.setTag(holder);
            }else {
                holder= (ViewHolder) convertView.getTag();
            }
            PhotosMenuDetailPagerBean.DataEntity.NewsEntity newsData = photoNews.get(position);
            holder.tv_photo_title.setText(newsData.getTitle());
            //使用volley请求图片
            String imageUrl=ConstantUtils.BASE_URL+newsData.getSmallimage();
//            Glide.with(context)
//                    .load(imageUrl)
//                    .diskCacheStrategy(DiskCacheStrategy.ALL)
//                    .into(holder.iv_photos_default_menu);
//            loaderImager(holder, imageUrl);
            ImageLoader.getInstance().displayImage(imageUrl, holder.iv_photos_default_menu, options);

            return convertView;
        }
    }
    static class ViewHolder{
        ImageView iv_photos_default_menu;
        TextView tv_photo_title;
    }

    class MyItemClickListener implements AdapterView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            Intent intent=new Intent(context, ShowImageActivity.class);
            Bundle bundle=new Bundle();
            bundle.putSerializable("photoNews", (Serializable) photoNews);
            intent.putExtras(bundle);
            intent.putExtra("position",position);
            context.startActivity(intent);
        }
    }
    class MyStringCallback extends StringCallback {

        @Override
        public void onBefore(okhttp3.Request request, int id) {}

        @Override
        public void onAfter(int id) {}
        @Override
        public void onError(Call call, Exception e, int id) {
            e.printStackTrace();
            LogUtil.e("使用OkHttpUtils请求数据失败==="+e.getMessage());
        }

        @Override
        public void onResponse(String response, int id) {
            LogUtil.e("使用OkHttpUtils请求数据成功====="+response);
            CacheUtils.putString(context,photosUrl,response);
            parseData(response);
            switch (id)
            {
                case 100:
                    Toast.makeText(context, "http", Toast.LENGTH_SHORT).show();
                    break;
                case 101:
                    Toast.makeText(context, "https", Toast.LENGTH_SHORT).show();
                    break;
            }
        }
        @Override
        public void inProgress(float progress, long total, int id)
        {
            LogUtil.e("inProgress:" + progress);
        }
    }
    /**
     *
     * @param viewHolder
     * @param imageUrl
     */
//    private void loaderImager(final ViewHolder viewHolder, String imageUrl) {
//        //设置一个tag
//        viewHolder.iv_photos_default_menu.setTag(imageUrl);
//        //直接在这里请求会乱位置
//        ImageLoader.ImageListener listener = new ImageLoader.ImageListener() {
//            @Override
//            public void onResponse(ImageLoader.ImageContainer imageContainer, boolean b) {
//                if (imageContainer != null) {
//                    if (viewHolder.iv_photos_default_menu != null) {
//                        if (imageContainer.getBitmap() != null) {
//                            //设置图片
//                            viewHolder.iv_photos_default_menu.setImageBitmap(imageContainer.getBitmap());
//                        } else {
//                            //设置默认图片
//                            viewHolder.iv_photos_default_menu.setImageResource(R.drawable.home_scroll_default);
//                        }
//                    }
//                }
//            }
//            @Override
//            public void onErrorResponse(VolleyError volleyError) {
//                //如果出错，则说明都不显示（简单处理），最好准备一张出错图片
//                viewHolder.iv_photos_default_menu.setImageResource(R.drawable.home_scroll_default);
//            }
//        };
//        VolleyManager.getImageLoader().get(imageUrl, listener);
//    }
}
