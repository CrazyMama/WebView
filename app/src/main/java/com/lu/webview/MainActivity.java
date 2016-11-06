package com.lu.webview;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewStub;
import android.webkit.JavascriptInterface;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

public class MainActivity extends Activity implements View.OnClickListener {

    private EditText et_text;
    private Button bt_flush, bt_advance, bt_back;

    private Button mLoad;
    private WebView mWebView;
    private String mText;

    private WebSettings mWebViewSettings;
    private ViewStub mViewStub;
    private ProgressBar mProgressBar;
    private Button mButtonloadjs;
    private Button mAndroidForJs;

    @SuppressLint("JavascriptInterface")

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        init();


    }


    private void init() {

        et_text = (EditText) findViewById(R.id.et_text);
        mWebView = (WebView) findViewById(R.id.webview);

        //web view 初始化设置
        mViewStub = (ViewStub) findViewById(R.id.stub_import);


        initWebWiew();

        mLoad = (Button) findViewById(R.id.bt_load);
        bt_flush = (Button) findViewById(R.id.bt_flush);
        bt_advance = (Button) findViewById(R.id.bt_advance);
        bt_back = (Button) findViewById(R.id.bt_back);
        mButtonloadjs = (Button) findViewById(R.id.bt_load_js);
        mAndroidForJs = (Button) findViewById(R.id.bt_android_js);
        initWebViewSetting();
        mLoad.setOnClickListener(this);
        bt_advance.setOnClickListener(this);
        bt_flush.setOnClickListener(this);
        bt_back.setOnClickListener(this);
        mButtonloadjs.setOnClickListener(this);
        mAndroidForJs.setOnClickListener(this);

    }


    private void initWebViewSetting() {

        mWebViewSettings = mWebView.getSettings();
        //支持js
        mWebViewSettings.setJavaScriptEnabled(true);
        //支持缩放
        mWebViewSettings.setSupportZoom(true);

        // mWebViewSettings.setDefaultZoom(WebSettings.ZoomDensity.CLOSE);
        //只加载文本

        //mWebViewSettings.setBlockNetworkImage(true);
        jsForAndroid();


    }



    private void initWebWiew() {

        //焦点起作用
        mWebView.requestFocus();
        //取消滚动条
        mWebView.setHorizontalScrollBarEnabled(false);//水平不显示
        mWebView.setVerticalScrollBarEnabled(false); //垂直不显示
        //mWebView.setScrollBarStyle(View.SCROLLBARS_OUTSIDE_OVERLAY);//滚动条在WebView内侧显示
        //mWebView.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);//滚动条在WebView外侧显示


        //设置一些webview 的属性

        // mWebView.setWebViewClient(new WebViewClient());
        //和下面的方法一样

        mWebView.setWebViewClient(new WebViewClient() {

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }
        });


        //自身UI 改变的时候调用
        mWebView.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                mViewStub.setVisibility(View.VISIBLE);
                mProgressBar = (ProgressBar) findViewById(R.id.progressbar);
                mProgressBar.setProgress(newProgress);
                if (newProgress == 100) {
                    mViewStub.setVisibility(View.GONE);
                }

                super.onProgressChanged(view, newProgress);
            }
        });


    }


    @Override
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.bt_load:
                mText = et_text.getText().toString().trim();
                final String murl = "http://" + mText;
                if (TextUtils.isEmpty(mText)) {
                    Toast.makeText(getApplicationContext(), "输入不能为空", Toast.LENGTH_SHORT).show();
                    return;
                }
                mWebView.loadUrl(murl);

                break;

            case R.id.bt_flush:
                mWebView.reload();

                break;
            case R.id.bt_advance:

                if (mWebView.canGoForward()) {
                    mWebView.goForward();
                } else {
                    Toast.makeText(getApplicationContext(), "最新网页了不能前进了", Toast.LENGTH_SHORT).show();
                }

                break;
            case R.id.bt_back:

                if (mWebView.canGoBack()) {
                    mWebView.goBack();
                } else {
                    Toast.makeText(getApplicationContext(), "最后了,不能在退了", Toast.LENGTH_SHORT).show();
                }

                break;

            //加载js
            case R.id.bt_load_js:

                mWebView.loadUrl("file:///android_asset/test.html");
                break;

            //安卓调用js
            case R.id.bt_android_js:
                mWebView.loadUrl("javascript:showAndroidForJs('安卓调用了Js')");
                break;
        }
    }
    private void jsForAndroid() {
        //js调用Android
        mWebView.addJavascriptInterface(new Object() {
            @JavascriptInterface
            public void showToast(String text) {
                Toast.makeText(MainActivity.this, text, Toast.LENGTH_SHORT).show();
            }

        }, "Android");
    }



}
