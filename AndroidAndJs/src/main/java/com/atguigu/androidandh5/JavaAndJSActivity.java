package com.atguigu.androidandh5;

import android.app.Activity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

/**
 * 作者：尚硅谷-杨光福 on 2016/7/28 11:19
 * 微信：yangguangfu520
 * QQ号：541433511
 * 作用：java和js互调
 */
public class JavaAndJSActivity extends Activity implements View.OnClickListener {
    private EditText etNumber;
    private EditText etPassword;
    private Button btnLogin;
    /**
     * 加载网页或者说H5页面
     */
    private WebView webview;
    private WebSettings webSettings;

    /**
     * Find the Views in the layout<br />
     * <br />
     * Auto-created on 2016-07-28 11:43:37 by Android Layout Finder
     * (http://www.buzzingandroid.com/tools/android-layout-finder)
     */
    private void findViews() {
        setContentView(R.layout.activity_java_and_js);
        etNumber = (EditText) findViewById(R.id.et_number);
        etPassword = (EditText) findViewById(R.id.et_password);
        btnLogin = (Button) findViewById(R.id.btn_login);
        btnLogin.setOnClickListener(this);

    }

    /**
     * Handle button click events<br />
     * <br />
     * Auto-created on 2016-07-28 11:43:37 by Android Layout Finder
     * (http://www.buzzingandroid.com/tools/android-layout-finder)
     */
    @Override
    public void onClick(View v) {
        if (v == btnLogin) {
            // Handle clicks for btnLogin
            login();
        }
    }

    private void login() {
        String numebr = etNumber.getText().toString().trim();
        String password = etPassword.getText().toString().trim();
        if (TextUtils.isEmpty(numebr) || TextUtils.isEmpty(password)) {
            Toast.makeText(JavaAndJSActivity.this, "账号或者密码为空", Toast.LENGTH_SHORT).show();
        } else {
            //把密码传递给html页面
            //登录
            loginWebview(numebr);

        }
    }

    /**
     * 传入账号
     * @param numebr
     */
    private void loginWebview(String numebr) {
        webview.loadUrl("javascript:javaCallJs("+"'"+numebr+"'"+")");
        setContentView(webview);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        findViews();
        initWebView();
    }

    private void initWebView() {
        webview=new WebView(JavaAndJSActivity.this);
        webSettings=webview.getSettings();
        //设置支持javaScript
        webSettings.setJavaScriptEnabled(true);
        //设置双击变大变小
        webSettings.setUseWideViewPort(true);
        //增加缩放按钮
        webSettings.setBuiltInZoomControls(true);
        //设置字体大小
        webSettings.setTextZoom(100);
        webview.setWebViewClient(new WebViewClient(){
            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
            }
        });
        //添加javascript接口
        webview.addJavascriptInterface(new MyJavaScriptInterface(),"android");
        //加载本地的网络的页面，也可以加载应用内置的页面
        webview.loadUrl("file:///android_asset/JavaAndJavaScriptCall.html");
        //加载到布局中
       // setContentView(webview);
    }


    private class MyJavaScriptInterface {
        @JavascriptInterface
        public void showToast(){
            Toast.makeText(JavaAndJSActivity.this,"我被Js调用了",Toast.LENGTH_SHORT).show();
        }

    }
}
