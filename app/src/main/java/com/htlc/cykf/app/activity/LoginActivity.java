
package com.htlc.cykf.app.activity;


import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.htlc.cykf.R;
import com.htlc.cykf.app.App;
import com.htlc.cykf.app.util.LogUtil;
import com.htlc.cykf.app.util.ToastUtil;
import com.htlc.cykf.core.ActionCallbackListener;
import com.htlc.cykf.model.UserBean;

import io.rong.imkit.RongIM;
import io.rong.imlib.RongIMClient;

public class LoginActivity extends BaseActivity implements View.OnClickListener {


    private TextView mTextButtonLogin, mTextButtonRegister, mTextButtonForget;
    private EditText mEditUsername,mEditPassword;
    @Override
    protected boolean isRegisterRongIMConnectionListener() {
        return false;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        setupView();
    }

    private void setupView() {
        mTextButtonLogin = (TextView) findViewById(R.id.textButtonLogin);
        mTextButtonRegister = (TextView) findViewById(R.id.textButtonRegister);
        mTextButtonForget = (TextView) findViewById(R.id.textButtonForget);
        mEditUsername = (EditText) findViewById(R.id.editUsername);
        mEditPassword = (EditText) findViewById(R.id.editPassword);

        mTextButtonLogin.setOnClickListener(this);
        mTextButtonRegister.setOnClickListener(this);
        mTextButtonForget.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.textButtonLogin:
                login();
                break;
            case R.id.textButtonRegister:
                goRegister(true);
                break;
            case R.id.textButtonForget:
                goRegister(false);
                break;
        }
    }

    private void login() {
        final String username = mEditUsername.getText().toString().trim();
        final String password = mEditPassword.getText().toString().trim();
        appAction.login(username, password, new ActionCallbackListener<UserBean>() {
            @Override
            public void onSuccess(UserBean data) {
                data.username = username;
                data.password = password;
                application.setUserBean(data);
                connect(data.token);
//                goMainOrGuide();
            }

            @Override
            public void onFailure(String errorEvent, String message) {
                ToastUtil.showToast(LoginActivity.this, message);
            }
        });
    }

    private void goRegister(boolean isRegister) {
        Intent intent = null;
        if(isRegister){
            intent = new Intent(this, RegisterActivity.class);
        }else {
            intent = new Intent(this, RegisterActivity.class);
            intent.putExtra(RegisterActivity.IsRegister,false);
        }
        startActivity(intent);
    }

    private void goMainOrGuide() {
        Intent intent = null;
        String flag = application.getUserBean().flag;
        switch (flag){
            case "0":
            case "3":
                intent = new Intent(LoginActivity.this, MainActivity.class);
                break;
            case "1":
                intent = new Intent(LoginActivity.this, RecommendationActivity.class);
                intent.putExtra(RecommendationActivity.IsLoginBind,true);
                break;
            case "2":
                intent = new Intent(LoginActivity.this, PerfectInfoActivity.class);
                break;
            default:
                intent = new Intent(LoginActivity.this, MainActivity.class);
        }
        startActivity(intent);
        finish();
    }

    /**
     * 建立与融云服务器的连接
     *
     * @param token
     */
    private void connect(String token) {
        LogUtil.e("RongIMUtil", "connect");
        if (App.app.getApplicationInfo().packageName.equals(App.getCurProcessName(App.app.getApplicationContext()))) {

            /**
             * IMKit SDK调用第二步,建立与服务器的连接
             */
            RongIM.connect(token, new RongIMClient.ConnectCallback() {

                /**
                 * Token 错误，在线上环境下主要是因为 Token 已经过期，您需要向 App Server 重新请求一个新的 Token
                 */
                @Override
                public void onTokenIncorrect() {
                    Log.d("LoginActivity", "--onTokenIncorrect");
                }

                /**
                 * 连接融云成功
                 * @param userid 当前 token
                 */
                @Override
                public void onSuccess(String userid) {
                    Log.d("LoginActivity", "--onSuccess" + userid);
                    App.app.setIsOnline(true);
                    goMainOrGuide();
                }

                /**
                 * 连接融云失败
                 * @param errorCode 错误码，可到官网 查看错误码对应的注释
                 */
                @Override
                public void onError(RongIMClient.ErrorCode errorCode) {
                    LogUtil.e("RongIM TOKEN connect", "error:" + errorCode);
                    Log.d("LoginActivity", "--onError" + errorCode);
                }
            });
        }
    }
}
