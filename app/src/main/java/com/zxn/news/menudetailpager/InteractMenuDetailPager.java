package com.zxn.news.menudetailpager;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.StringRequest;
import com.google.gson.Gson;
import com.zxn.news.R;
import com.zxn.news.base.MenuDetailBasePager;
import com.zxn.news.bean.NewsBean;
import com.zxn.news.bean.PhotosMenuDetailPagerBean;
import com.zxn.news.utils.BitmapCacheUtils;
import com.zxn.news.utils.CacheUtils;
import com.zxn.news.utils.ConstantUtils;
import com.zxn.news.utils.LogUtil;
import com.zxn.news.utils.NetCacheUtils;
import com.zxn.news.volley.VolleyManager;

import java.io.UnsupportedEncodingException;
import java.util.List;

/**
 * Created by zxn on 2017-08-02.
 */

public class InteractMenuDetailPager extends MenuDetailBasePager {

    private final NewsBean.DataEntity dataEntity;
    private ListView lv_photos_menu;
    private GridView gv_photos_menu;
    /**
     * 联网请求数据的url
     */
    private String photosUrl;
    private MyPhotoListAdapter adapter;
    private List<PhotosMenuDetailPagerBean.DataEntity.NewsEntity> photoNews;

    /**
     * 创建一个图片三级缓存类
     */
    private BitmapCacheUtils bitmapCacheUtils;
    private Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case NetCacheUtils.SUCCESS://图片请求成功
                    int position=msg.arg1;
                    Bitmap bitmap= (Bitmap) msg.obj;
                    LogUtil.e("联网请求图片成功=="+position);
                    if (lv_photos_menu.isShown()) {
                        ImageView imageView = (ImageView) lv_photos_menu.findViewWithTag(position);
                        if (imageView != null && bitmap != null) {
                            imageView.setImageBitmap(bitmap);
                        }
                    }else if (gv_photos_menu.isShown()){
                        ImageView imageView = (ImageView) gv_photos_menu.findViewWithTag(position);
                        if (imageView != null && bitmap != null) {
                            imageView.setImageBitmap(bitmap);
                        }
                    }
                    break;
                case NetCacheUtils.FAILURE://图片请求失败
                    position=msg.arg1;
                    LogUtil.e("联网请求图片成功=="+position);
                    break;
            }
        }
    };
    public InteractMenuDetailPager(Context context, NewsBean.DataEntity dataEntity) {
        super(context);
        this.dataEntity=dataEntity;
        //实例化三级缓存类
        bitmapCacheUtils=new BitmapCacheUtils(handler);
    }

    @Override
    public View initView() {
        View view=View.inflate(context, R.layout.photos_menu_detail_pager,null);
        lv_photos_menu= (ListView) view.findViewById(R.id.lv_photos_menu);
        gv_photos_menu= (GridView) view.findViewById(R.id.gv_photos_menu);
        return view;
    }

    @Override
    public void initData() {
        super.initData();
        LogUtil.e("互动详情页面被初始化了");
        photosUrl= ConstantUtils.BASE_URL+dataEntity.getUrl();
        String saveJson= CacheUtils.getString(context,photosUrl);
        if (!TextUtils.isEmpty(saveJson)){
            parseData(saveJson);
        }
        getDataFromNet();
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
            adapter= new MyPhotoListAdapter();
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
            //给图片设置一个tag
            holder.iv_photos_default_menu.setTag(position);
            //使用自定义的三级缓存请求图片
            Bitmap bitmap=bitmapCacheUtils.getBitmap(imageUrl,position);//从内存或者本地获取
            if (bitmap!=null){
                holder.iv_photos_default_menu.setImageBitmap(bitmap);
            }
            return convertView;
        }
    }
    static class ViewHolder{
        ImageView iv_photos_default_menu;
        TextView tv_photo_title;
    }
    /**
     *
     * @param viewHolder
     * @param imageUrl
     */
    private void loaderImager(final PhotosMenuDetailPager.ViewHolder viewHolder, String imageUrl) {
        //设置一个tag
        viewHolder.iv_photos_default_menu.setTag(imageUrl);
        //直接在这里请求会乱位置
        ImageLoader.ImageListener listener = new ImageLoader.ImageListener() {
            @Override
            public void onResponse(ImageLoader.ImageContainer imageContainer, boolean b) {
                if (imageContainer != null) {
                    if (viewHolder.iv_photos_default_menu != null) {
                        if (imageContainer.getBitmap() != null) {
                            //设置图片
                            viewHolder.iv_photos_default_menu.setImageBitmap(imageContainer.getBitmap());
                        } else {
                            //设置默认图片
                            viewHolder.iv_photos_default_menu.setImageResource(R.drawable.home_scroll_default);
                        }
                    }
                }
            }
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                //如果出错，则说明都不显示（简单处理），最好准备一张出错图片
                viewHolder.iv_photos_default_menu.setImageResource(R.drawable.home_scroll_default);
            }
        };
        VolleyManager.getImageLoader().get(imageUrl, listener);
    }
}
