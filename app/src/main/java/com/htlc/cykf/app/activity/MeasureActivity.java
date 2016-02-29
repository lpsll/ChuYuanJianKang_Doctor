package com.htlc.cykf.app.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.JavascriptInterface;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.bigkoo.pickerview.TimePickerView;
import com.htlc.cykf.R;
import com.htlc.cykf.app.util.DateFormat;
import com.htlc.cykf.app.util.LogUtil;
import com.htlc.cykf.app.util.ToastUtil;

import java.util.Calendar;
import java.util.Date;

/**
 * Created by sks on 2016/1/5.
 */
public class MeasureActivity extends BaseActivity{
    public static final String Title = "Title";
    public static final String Url = "Url";

    private String mTitle, mUrl,mUserId;
    private WebView mWebView;
    private ViewGroup rootView;
    private JavascriptObject javascriptObject;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        mTitle = intent.getStringExtra(Title);
        mUrl = intent.getStringExtra(Url);
        mUserId = application.getUserBean().userid;
        LogUtil.e(this,"Url="+mUrl);
        rootView = (ViewGroup) View.inflate(this, R.layout.activity_measure, null);
        setContentView(rootView);

        setupView();
        initData();
    }

    private void setupView() {
        mWebView = (WebView) findViewById(R.id.webView);
        initWebView();
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mWebView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mWebView.onPause(); // 暂停网页中正在播放的视频

    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        mWebView.stopLoading();
        rootView.removeView(mWebView);
        mWebView.destroy();
        super.onDestroy();

    }

    private void initData() {
        mWebView.loadUrl(mUrl);
    }


    private void initWebView() {
        mWebView.getSettings().setSupportZoom(true);          //支持缩放
        mWebView.getSettings().setBuiltInZoomControls(true);  //启用内置缩放装置
//        mWebView.getSettings().setUseWideViewPort(true);//设置此属性，可任意比例缩放
        mWebView.getSettings().setLoadWithOverviewMode(true);
        mWebView.getSettings().setDisplayZoomControls(false);//隐藏缩放图标
        mWebView.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);//缓存
        mWebView.getSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NORMAL); //支持内容重新布局
        //运行js代码
        mWebView.getSettings().setJavaScriptEnabled(true);
        javascriptObject = new JavascriptObject();
        mWebView.addJavascriptInterface(javascriptObject, "bridge");
        //先不加载图片
        if (Build.VERSION.SDK_INT >= 19) {
            mWebView.getSettings().setLoadsImagesAutomatically(true);
        } else {
            mWebView.getSettings().setLoadsImagesAutomatically(false);
        }
        //给web view设置客户端
        mWebView.setWebViewClient(new WebViewClient() {
            //当点击链接时,希望覆盖而不是打开新窗口
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
//                view.loadUrl(url);
                return false;
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                if (!view.getSettings().getLoadsImagesAutomatically()) {
                    view.getSettings().setLoadsImagesAutomatically(true);
                }
            }


        });
        //点击后退按钮,让WebView后退一页(也可以覆写Activity的onKeyDown方法)
        mWebView.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_DOWN) {
                    if (keyCode == KeyEvent.KEYCODE_BACK && mWebView.canGoBack()) {
                        mWebView.goBack();   //后退
                        return true;    //已处理
                    }
                }
                return false;
            }
        });
        //不显示滚动条
        mWebView.setScrollBarStyle(WebView.SCROLLBARS_OUTSIDE_OVERLAY);


    }
    private void selectTime() {
        //时间选择器
        TimePickerView timePickerView = new TimePickerView(MeasureActivity.this, TimePickerView.Type.ALL);
        //控制时间范围
        Calendar calendar = Calendar.getInstance();
        timePickerView.setRange(calendar.get(Calendar.YEAR) - 20, calendar.get(Calendar.YEAR));
        timePickerView.setTime(new Date());
        timePickerView.setCyclic(true);
        timePickerView.setCancelable(true);
        //时间选择后回调
        timePickerView.setOnTimeSelectListener(new TimePickerView.OnTimeSelectListener() {

            @Override
            public void onTimeSelect(Date date) {
                javascriptObject.setTimeToHTML(DateFormat.getTime(date));
            }
        });
        timePickerView.show();
    }
    @SuppressLint("NewApi")
    public class JavascriptObject {
        /**
         * 定义接口提供给js调用
         */
        @JavascriptInterface
        public void jsInvokeAndroid(){
            LogUtil.e(this, "jsInvokeAndroid()");
        }
        @JavascriptInterface
        public void alert(final String msg){
            LogUtil.e(this, "jsInvokeAndroid()");
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    ToastUtil.showToast(MeasureActivity.this,msg);
                }
            });
        }

        @JavascriptInterface
        public void back(){
            LogUtil.e(this, "back()");
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    finish();
                }
            });
        }
        @JavascriptInterface
        public String getUserId(){
            LogUtil.e(this, "getUserId=" + mUserId);
            return "4";
        }
        @JavascriptInterface
        public void textTime() {
            LogUtil.e(this, "textTime()");
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    selectTime();
                }
            });

        }


        /**
         * 调用js
         * @param time
         */
        public void setTimeToHTML(final String time){
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    mWebView.loadUrl("javascript: SetDateValue('"+time+"')");
                }
            });
        }
    }
}
