
package com.htlc.cykf.app.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.htlc.cykf.app.App;
import com.htlc.cykf.app.util.AppManager;
import com.htlc.cykf.app.util.LogUtil;
import com.htlc.cykf.app.util.RongIMUtil;
import com.htlc.cykf.core.AppAction;

public abstract class BaseActivity extends AppCompatActivity {
    // 上下文实例
    public Context context;
    // 应用全局的实例
    public App application;
    // 核心层的Action实例
    public AppAction appAction;
    private RongIMUtil.RongIMConnectStatusListener listener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        AppManager.getAppManager().addActivity(this);
        super.onCreate(savedInstanceState);
        context = getApplicationContext();
        application = (App) this.getApplication();
        appAction = application.getAppAction();
    }

    @Override
    protected void onStart() {
        super.onStart();
        if(isRegisterRongIMConnectionListener()){
            listener = new RongIMUtil.RongIMConnectStatusListener() {
                @Override
                public void onError() {
                    LogUtil.e(BaseActivity.this, "登录过期");
                    Intent intent = new Intent(BaseActivity.this, LoginActivity.class);
                    startActivity(intent);
                    AppManager.getAppManager().finishAllActivity();
                    finish();
                }
            };
            RongIMUtil.setListener(listener);
        }

    }

    @Override
    protected void onStop() {
        super.onStop();
        if(isRegisterRongIMConnectionListener()){
            RongIMUtil.setListener(null);
            listener = null;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        AppManager.getAppManager().removeActivity(this);
    }

    protected  boolean isRegisterRongIMConnectionListener(){
        return true;
    }
}
