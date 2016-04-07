package com.htlc.cykf.app.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.htlc.cykf.R;
import com.htlc.cykf.app.App;
import com.htlc.cykf.app.util.ToastUtil;
import com.htlc.cykf.core.ActionCallbackListener;
import com.htlc.cykf.model.TotalMoneyBean;
import com.nostra13.universalimageloader.core.ImageLoader;

/**
 * Created by sks on 2016/3/24.
 */
public class MyAccountActivity extends BaseActivity{
    private TextView mTextAccount;
    private View mTextButton;
    private ImageView mImageHead;
    private TotalMoneyBean mTotalMoneyBean;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_account);
        setupView();
    }
    private void setupView() {
        findViewById(R.id.imageBack).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        mImageHead = (ImageView) findViewById(R.id.imageHead);
        ImageLoader.getInstance().displayImage(application.getUserBean().photo,mImageHead);
        mTextAccount = (TextView) findViewById(R.id.textAccount);
        mTextButton =  findViewById(R.id.textButton);
        mTextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                withdraw();
            }
        });
        getAccountMoney();
    }

    private void getAccountMoney() {
        appAction.getTotalMoney(new ActionCallbackListener<TotalMoneyBean>() {
            @Override
            public void onSuccess(TotalMoneyBean data) {
                mTotalMoneyBean = data;
                setMoney();
            }

            @Override
            public void onFailure(String errorEvent, String message) {
                ToastUtil.showToast(App.app,message);
            }
        });
    }

    private void setMoney() {
        if(mTotalMoneyBean!=null){
            mTextAccount.setText("账户余额: "+mTotalMoneyBean.money+"￥");
        }

    }

    private void withdraw() {
        appAction.withdraw(new ActionCallbackListener<Void>() {
            @Override
            public void onSuccess(Void data) {
                ToastUtil.showToast(App.app,"申请提交成功！请耐心等待回复");
            }

            @Override
            public void onFailure(String errorEvent, String message) {
                if(handleNetworkOnFailure(errorEvent, message)) return;
                ToastUtil.showToast(App.app, message);
            }
        });
    }
}
