package com.zxn.news.activity;

import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;

import com.github.chrisbanes.photoview.PhotoView;
import com.github.chrisbanes.photoview.PhotoViewAttacher;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;
import com.zxn.news.R;
import com.zxn.news.bean.PhotosMenuDetailPagerBean;
import com.zxn.news.utils.ConstantUtils;

import java.util.List;


public class ShowImageActivity extends AppCompatActivity {


    private ViewPager vpPager;
    private List<PhotosMenuDetailPagerBean.DataEntity.NewsEntity> photoNews;
    private MyViewPagerAdapter adapter;
    private int tempPosition;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_simple);
        getData();
        vpPager= (ViewPager) findViewById(R.id.vp_pager);
        adapter=new MyViewPagerAdapter();
        vpPager.setAdapter(adapter);
        vpPager.setCurrentItem(tempPosition);
    }
    private void getData() {
        photoNews = (List<PhotosMenuDetailPagerBean.DataEntity.NewsEntity>) getIntent().getSerializableExtra("photoNews");
        tempPosition = getIntent().getIntExtra("position", 0);
    }

    class MyViewPagerAdapter extends PagerAdapter {
        @Override
        public int getCount() {
            return photoNews.size();
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            PhotoView ivPhoto = new PhotoView(ShowImageActivity.this);
            PhotosMenuDetailPagerBean.DataEntity.NewsEntity newsData = photoNews.get(position);
            String imageUrl = ConstantUtils.BASE_URL + newsData.getLargeimage();
            container.addView(ivPhoto, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            final PhotoViewAttacher attacher = new PhotoViewAttacher(ivPhoto);
            Picasso.with(ShowImageActivity.this)
                    .load(imageUrl)
                    .into(ivPhoto, new Callback() {
                        @Override
                        public void onSuccess() {
                            attacher.update();
                        }

                        @Override
                        public void onError() {
                        }
                    });
            return ivPhoto;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }
    }
}
