package com.htlc.cykf.app.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.JavascriptInterface;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.bigkoo.pickerview.OptionsPickerView;
import com.bigkoo.pickerview.TimePickerView;
import com.bigkoo.pickerview.adapter.ArrayWheelAdapter;
import com.bigkoo.pickerview.listener.OnItemSelectedListener;
import com.htlc.cykf.R;
import com.htlc.cykf.app.bean.CityBean;
import com.htlc.cykf.app.db.ProvinceDao;
import com.htlc.cykf.app.util.CommonUtil;
import com.htlc.cykf.app.util.DateFormat;
import com.htlc.cykf.app.util.LogUtil;
import com.htlc.cykf.app.util.ToastUtil;
import com.htlc.cykf.core.ActionCallbackListener;
import com.htlc.cykf.model.DrugBean;
import com.htlc.cykf.model.IllnessBean;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import de.greenrobot.event.EventBus;

/**
 * Created by sks on 2016/1/5.
 */
public class MeasureActivity extends BaseActivity {
    public static final String Title = "Title";
    public static final String Url = "Url";
    public static final String PatientId = "PatientId";

    private String mTitle, mUrl, mUserId, mUserToken,mPatientId;
    private WebView mWebView;
    private ViewGroup rootView;
    private JavascriptObject javascriptObject;

    private OptionsPickerView mPickViePwOptions;
    private ArrayList<CityBean> provinces = new ArrayList<CityBean>();
    private ArrayList<CityBean> citys = new ArrayList<CityBean>();
    private ArrayList<CityBean> countys = new ArrayList<CityBean>();
    private ArrayList<String> jobs = new ArrayList<String>();
    private ArrayList<IllnessBean> mIllnessBeans = new ArrayList<>();

    public void onEventMainThread(DrugBean event) {
        String msg = "onEventMainThread收到了消息：";
        javascriptObject.setMecListToHTML(event.medicine,event.id);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
        Intent intent = getIntent();
        mTitle = intent.getStringExtra(Title);
        mUrl = intent.getStringExtra(Url);
        mPatientId = intent.getStringExtra(PatientId);
        mUserId = application.getUserBean().userid;
        mUserToken = application.getUserBean().token;
        LogUtil.e(this, "Url=" + mUrl);
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
        EventBus.getDefault().unregister(this);//反注册EventBus

    }

    private void initData() {
        mWebView.loadUrl(mUrl);
        getIllnessList();
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
        TimePickerView timePickerView = new TimePickerView(MeasureActivity.this, TimePickerView.Type.YEAR_MONTH_DAY);
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
    private void selectJob() {
        //选项选择器
        mPickViePwOptions = new OptionsPickerView(this);
        jobs.clear();
        String[] jobArray = CommonUtil.getResourceStringArray(R.array.activity_perfect_info_jobs);
        for (int i = 0; i < jobArray.length; i++) {
            jobs.add(jobArray[i]);
        }
        //三级不联动效果  false
        mPickViePwOptions.setPicker(jobs);
        mPickViePwOptions.setCyclic(false);
        //设置默认选中的三级项目
        //监听确定选择按钮
        mPickViePwOptions.setSelectOptions(0);
        mPickViePwOptions.setOnOptionsSelectListener(new OptionsPickerView.OnOptionsSelectListener() {
            @Override
            public void onOptionsSelect(int options1, int options2, int options3) {
                javascriptObject.setZhiYeToHTML(jobs.get(options1));
            }
        });
        mPickViePwOptions.show();
    }
    private void selectIllness() {
        if (mIllnessBeans.size() < 1) {
            appAction.illnessList(new ActionCallbackListener<ArrayList<IllnessBean>>() {
                @Override
                public void onSuccess(ArrayList<IllnessBean> data) {
                    mIllnessBeans.clear();
                    mIllnessBeans.addAll(data);
                    selectIllness();
                }

                @Override
                public void onFailure(String errorEvent, String message) {
                    if(handleNetworkOnFailure(errorEvent, message)) return;
                    LogUtil.e(MeasureActivity.this, message);
                }
            });
        }
        //选项选择器
        mPickViePwOptions = new OptionsPickerView(this);
        //三级不联动效果  false
        mPickViePwOptions.setPicker(mIllnessBeans);
        mPickViePwOptions.setCyclic(false);
        //设置默认选中的三级项目
        //监听确定选择按钮
        mPickViePwOptions.setSelectOptions(0);
        mPickViePwOptions.setOnOptionsSelectListener(new OptionsPickerView.OnOptionsSelectListener() {
            @Override
            public void onOptionsSelect(int options1, int options2, int options3) {
                javascriptObject.setJiBingToHTML(mIllnessBeans.get(options1).disease, mIllnessBeans.get(options1).id);
            }
        });
        mPickViePwOptions.show();
    }
    private void selectAddress() {
        //选项选择器
        mPickViePwOptions = new OptionsPickerView(this);
        provinces.clear();
        citys.clear();
        countys.clear();
        provinces.addAll(new ProvinceDao().getProvinces());
        citys.addAll(new ProvinceDao().getCities(provinces.get(0).area_code));
        countys.addAll(new ProvinceDao().getCounties(citys.get(0).area_code));

        //三级不联动效果  false
        mPickViePwOptions.setPicker(provinces, citys, countys, false);
        mPickViePwOptions.getWheelOptions().getWv_option1().setOnItemSelectedListener(new OnItemSelectedListener() {
            @Override
            public void onItemSelected(int index) {
                ArrayList<CityBean> temp = new ProvinceDao().getCities(provinces.get(index).area_code);
                ArrayWheelAdapter adapter = new ArrayWheelAdapter(temp);
                citys.clear();
                citys.addAll(temp);
                mPickViePwOptions.getWheelOptions().getWv_option2().setAdapter(adapter);

                if (citys.size() == 0) {
                    mPickViePwOptions.getWheelOptions().getWv_option3().setAdapter(new ArrayWheelAdapter(new ArrayList<CityBean>()));
                    return;
                }
                ArrayList<CityBean> temp1 = new ProvinceDao().getCounties(citys.get(0).area_code);
                ArrayWheelAdapter adapter1 = new ArrayWheelAdapter(temp1);
                countys.clear();
                countys.addAll(temp1);
                mPickViePwOptions.getWheelOptions().getWv_option3().setAdapter(adapter1);

            }
        });
        mPickViePwOptions.getWheelOptions().getWv_option2().setOnItemSelectedListener(new OnItemSelectedListener() {
            @Override
            public void onItemSelected(int index) {
                ArrayList<CityBean> temp = new ProvinceDao().getCounties(citys.get(index).area_code);
                ArrayWheelAdapter adapter = new ArrayWheelAdapter(temp);
                countys.clear();
                countys.addAll(temp);
                mPickViePwOptions.getWheelOptions().getWv_option3().setAdapter(adapter);
            }
        });
        //设置选择的三级单位
//        pwOptions.setLabels("省", "市", "区");
//        mPickViePwOptions.setTitle("选择城市");
        mPickViePwOptions.setCyclic(false, false, false);
        //设置默认选中的三级项目
        //监听确定选择按钮
        mPickViePwOptions.setSelectOptions(0, 0, 0);
        mPickViePwOptions.setOnOptionsSelectListener(new OptionsPickerView.OnOptionsSelectListener() {

            @Override
            public void onOptionsSelect(int options1, int option2, int options3) {
                //返回的分别是三个级别的选中位置
                String address = provinces.get(options1).area_name + citys.get(option2).area_name + countys.get(options3).area_name;
                String addressId = countys.get(options3).area_code + "";
                javascriptObject.setDiQuToHTML(address, addressId);
            }
        });
        mPickViePwOptions.show();
    }
    public void getIllnessList() {
        appAction.illnessList(new ActionCallbackListener<ArrayList<IllnessBean>>() {
            @Override
            public void onSuccess(ArrayList<IllnessBean> data) {
                mIllnessBeans.clear();
                mIllnessBeans.addAll(data);
            }

            @Override
            public void onFailure(String errorEvent, String message) {
                if (handleNetworkOnFailure(errorEvent, message)) return;
                LogUtil.e(MeasureActivity.this, message);
            }
        });
    }
    private void selectDrug() {
        Intent intent = new Intent(this, DrugsActivity.class);
        startActivity(intent);
    }

    @SuppressLint("NewApi")
    public class JavascriptObject {
        /**
         * 定义接口提供给js调用
         */
        @JavascriptInterface
        public void jsInvokeAndroid() {
            LogUtil.e(this, "jsInvokeAndroid()");
        }

        @JavascriptInterface
        public void alert(final String msg) {
            LogUtil.e(this, "alert()");
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    ToastUtil.showToast(MeasureActivity.this, msg);
                }
            });
        }

        @JavascriptInterface
        public void back() {
            LogUtil.e(this, "back()");
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    finish();
                }
            });
        }

        @JavascriptInterface
        public String getUserId() {
            LogUtil.e(this, "getUserId=" + mUserId);
            return mUserId;
        }
        @JavascriptInterface
        public String getPatientId() {
            LogUtil.e(this, "getPatientId=" + mPatientId);
            return mPatientId;
        }

        @JavascriptInterface
        public String getToken() {
            LogUtil.e(this, "getToken()");
            return mUserToken;

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
        public void setTimeToHTML(final String time) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    mWebView.loadUrl("javascript: SetDateValue('" + time + "')");
                }
            });
        }
        @JavascriptInterface
        public void getJiBing() {
            LogUtil.e(this, "textTime()");
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    selectIllness();
                }
            });

        }
        public void setJiBingToHTML(final String jiBing, final String jiBingId) {
            LogUtil.e(this, "设置疾病" + jiBing + "Id" + jiBingId);
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    mWebView.loadUrl("javascript: setJiBing('" + jiBing + "','" + jiBingId + "')");
                }
            });
        }
        @JavascriptInterface
        public void getZhiYe() {
            LogUtil.e(this, "textTime()");
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    selectJob();
                }
            });

        }
        public void setZhiYeToHTML(final String zhiYe) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    mWebView.loadUrl("javascript: setZhiYe('" + zhiYe + "')");
                }
            });
        }
        @JavascriptInterface
        public void getDiQu() {
            LogUtil.e(this, "textTime()");
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    selectAddress();
                }
            });

        }
        public void setDiQuToHTML(final String diQu, final String diQuId) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    mWebView.loadUrl("javascript: setDiQu('" + diQu + "','" + diQuId + "')");
                }
            });
        }
        @JavascriptInterface
        public void getMecList() {
            LogUtil.e(this, "textTime()");
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    selectDrug();
                }
            });

        }
        public void setMecListToHTML(final String yaoPin, final String yaoPinId) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    mWebView.loadUrl("javascript: setMec('" + yaoPin + "','" + yaoPinId + "')");
                }
            });
        }
        @JavascriptInterface
        public void callTel(final String tel) {
            LogUtil.e(this, "callTel()" + tel);
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    call(tel);
                }
            });

        }
    }

    private void call(String tel) {
        Intent intent = new Intent(Intent.ACTION_DIAL);
        Uri data = Uri.parse("tel:" + tel);
        intent.setData(data);
        startActivity(intent);
    }

}
