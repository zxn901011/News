package com.zxn.news.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.github.chrisbanes.photoview.PhotoView;
import com.github.chrisbanes.photoview.PhotoViewAttacher;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;
import com.zxn.news.R;


public class ShowImageActivity extends AppCompatActivity {

    private PhotoView photoView;
    private String imageUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_simple);
        photoView = (PhotoView) findViewById(R.id.iv_photo);
        final PhotoViewAttacher attacher=new PhotoViewAttacher(photoView);
        getData();
        Picasso.with(this)
                .load(imageUrl)
                .into(photoView, new Callback() {
                    @Override
                    public void onSuccess() {
                        attacher.update();
                    }
                    @Override
                    public void onError() {
                    }
                });
    }

    private void getData() {
        imageUrl=getIntent().getStringExtra("url");
    }
}
