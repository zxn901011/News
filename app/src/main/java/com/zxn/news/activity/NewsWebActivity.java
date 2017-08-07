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

import cn.sharesdk.onekeyshare.OnekeyShare;

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
            showShare();
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

    private void showShare() {
        OnekeyShare oks = new OnekeyShare();
        //关闭sso授权
        oks.disableSSOWhenAuthorize();

        // 分享时Notification的图标和文字  2.5.9以后的版本不     调用此方法
        //oks.setNotification(R.drawable.ic_launcher, getString(R.string.app_name));
        // title标题，印象笔记、邮箱、信息、微信、人人网和QQ空间使用
        oks.setTitle(getString(R.string.app_name));
        // titleUrl是标题的网络链接，仅在人人网和QQ空间使用
        oks.setTitleUrl(url);
        // text是分享文本，所有平台都需要这个字段
        oks.setText("吴京算什么，渣渣一个，我给大家简单说下吴京为何渣渣。第一，他酷不过李易峰，因为吴京表情太丰富，没李易峰一僵到底。第二，他牛不过鹿晗，因为鹿晗永远都是在娘的道路上越走越远。第三，吴京片酬太低，看看人家鹿晗，片酬一个多亿，李易峰片酬几千万。第四，吴京傻不拉几的自己拍什么电影，看看同是中国的那些小鲜肉们，随随便便弄几个替身，再拍几个脸就能拿影帝影后，还万千粉丝拥护。第五，请问吴京渣渣能一个打4个李易峰吗？5个权志龙吗？6个吴亦凡吗？7个鹿晗吗？8个张艺兴吗？9个黄子稻吗？当然不能，因为吴京从不打女人！");
        // imagePath是图片的本地路径，Linked-In以外的平台都支持此参数
        oks.setImagePath("/sdcard/1.jpg");//确保SDcard下面存在此张图片
        // url仅在微信（包括好友和朋友圈）中使用
        oks.setUrl(url);
        // comment是我对这条分享的评论，仅在人人网和QQ空间使用
        oks.setComment("我是hao123");
        // site是分享此内容的网站名称，仅在QQ空间使用
        oks.setSite(getString(R.string.app_name));
        // siteUrl是分享此内容的网站地址，仅在QQ空间使用
        oks.setSiteUrl(url);

        // 启动分享GUI
        oks.show(this);
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
