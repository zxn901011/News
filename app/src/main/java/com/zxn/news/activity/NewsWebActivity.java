package com.zxn.news.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.zxn.news.R;

public class NewsWebActivity extends Activity implements View.OnClickListener {

    private TextView tvTitle;
    private ImageButton ibMenu;
    private ImageButton ibBack;
    private ImageButton ibShare;
    private ImageButton ibFont;
    private WebView wvNews;
    private ProgressBar pbLoadNews;
    private String url;
    private WebSettings webSettings;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_web);
        findViews();
        getData();
    }

    private void getData() {
        url=getIntent().getStringExtra("url");
        initWebViewSettings();

        wvNews.loadUrl(url);
    }

    private void initWebViewSettings() {
        webSettings=wvNews.getSettings();
        //设置支持javaScript
        webSettings.setJavaScriptEnabled(true);
        //设置双击变大变小
        webSettings.setUseWideViewPort(true);
        //增加缩放按钮
        webSettings.setBuiltInZoomControls(true);
        //设置字体大小
//        webSettings.setTextSize(webSettings.TextSize.NORMAL);
        webSettings.setTextZoom(100);
        //不让从当前网页跳转到系统的浏览器中
        wvNews.setWebViewClient(new WebViewClient(){
            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                pbLoadNews.setVisibility(View.GONE);
            }
        });
    }

    private void findViews() {
        tvTitle = (TextView) findViewById(R.id.tv_title);
        ibMenu = (ImageButton) findViewById(R.id.ib_menu);
        ibBack = (ImageButton) findViewById(R.id.ib_back);
        ibShare = (ImageButton) findViewById(R.id.ib_share);
        ibFont = (ImageButton) findViewById(R.id.ib_font);
        wvNews = (WebView)findViewById( R.id.wv_news );
        pbLoadNews = (ProgressBar)findViewById( R.id.pb_load_news );
        tvTitle.setVisibility(View.GONE);
        ibMenu.setVisibility(View.GONE);
        ibBack.setVisibility(View.VISIBLE);
        ibFont.setVisibility(View.VISIBLE);
        ibShare.setVisibility(View.VISIBLE);
        ibBack.setOnClickListener(this);
        ibShare.setOnClickListener(this);
        ibFont.setOnClickListener(this);
    }
    @Override
    public void onClick(View v) {
        if (v == ibBack) {
            finish();
        } else if (v == ibShare) {
            Toast.makeText(NewsWebActivity.this,"分享此网页",Toast.LENGTH_SHORT).show();
        } else if (v == ibFont) {
//            Toast.makeText(NewsWebActivity.this,"修改字体颜色和大小",Toast.LENGTH_SHORT).show();
            showChangeFontSizeDialog();
        }
    }
    private int tempSize=2;
    private int realSize=tempSize;
    private void showChangeFontSizeDialog() {
        final AlertDialog.Builder builder=new AlertDialog.Builder(this);
        builder.setTitle("设置字体大小");
        String[] items={"超大字体","大字体","正常字体","小字体","超小字体"};
        builder.setSingleChoiceItems(items, 2, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                tempSize=which;
            }
        });
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                realSize=tempSize;
                changeFontSize(realSize);
            }
        });
        builder.setNegativeButton("取消",null);
        builder.show();
    }

    /**
     * 设置字体
     * @param realSize
     */
    private void changeFontSize(int realSize) {
        switch (realSize){
            case 0:
                webSettings.setTextZoom(200);
                break;
            case 1:
                webSettings.setTextZoom(150);
                break;
            case 2:
                webSettings.setTextZoom(100);
                break;
            case 3:
                webSettings.setTextZoom(75);
                break;
            case 4:
                webSettings.setTextZoom(50);
                break;
        }
    }
}
