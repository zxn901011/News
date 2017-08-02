package com.zxn.news.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MotionEvent;

import com.zxn.news.R;
import com.zxn.news.utils.CacheUtils;

public final class SplashActivity extends AppCompatActivity {

    public static final String START_MAIN = "start_main";
    private static final String TAG =SplashActivity.class.getSimpleName();

    private Handler handler=new Handler();
    private boolean isStartMain;
    private int btnMode;
    private Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                startMainActivity();
            }
        },2000);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        Log.e(TAG,"onTouchEvent==Action"+event.getAction());
        startMainActivity();
        return super.onTouchEvent(event);
    }

    private void startMainActivity() {
        isStartMain= CacheUtils.getBoolean(SplashActivity.this,START_MAIN);
        btnMode=CacheUtils.getInt(SplashActivity.this,GuideActivity.BTN_MODE);
        if(isStartMain){
                if (btnMode==1){
                    intent=new Intent(SplashActivity.this,HomeActivity.class);
                    startActivity(intent);
                    finish();
                }else if (btnMode==2){
                    intent=new Intent(SplashActivity.this,MainActivity.class);
                    startActivity(intent);
                    finish();
                }
            }else {
            intent=new Intent(SplashActivity.this,GuideActivity.class);
            startActivity(intent);
            finish();
        }
    }


    @Override
    protected void onDestroy() {
        //把所有的消息和回调移除
        handler.removeCallbacksAndMessages(null);
        super.onDestroy();
    }
}
