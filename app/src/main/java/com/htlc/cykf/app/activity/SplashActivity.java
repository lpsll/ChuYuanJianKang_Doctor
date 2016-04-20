package com.htlc.cykf.app.activity;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;

import com.htlc.cykf.R;
import com.htlc.cykf.app.App;
import com.htlc.cykf.app.util.LogUtil;
import com.htlc.cykf.app.util.SharedPreferenceUtil;

import cn.jpush.android.api.JPushInterface;

/**
 * Created by sks on 2016/2/15.
 */
public class SplashActivity extends BaseActivity {
    public static final String IsFirstStart = "IsFirstStart";
    private ImageView mImageView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mImageView = new ImageView(this);
        mImageView.setImageResource(R.mipmap.ic_launcher);
        setContentView(mImageView);
        App.app.initDatabase();
    }

    @Override
    protected void onResume() {
        super.onResume();
        JPushInterface.onResume(this);
        ValueAnimator animator = ValueAnimator.ofFloat(0.4f, 1.0f);
        animator.setDuration(1000).start();
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                mImageView.setAlpha((Float) animation.getAnimatedValue());
                mImageView.setScaleX((Float) animation.getAnimatedValue());
            }
        });
        animator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                goNextActivity();
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
    }

    @Override
    protected void onPause() {
        super.onPause();
        JPushInterface.onPause(this);
    }

    private void goNextActivity() {
        int isFirst = SharedPreferenceUtil.getInt(this, IsFirstStart, -1);
        if (isFirst == -1) {
            Intent intent = new Intent(this, GuideActivity.class);
            startActivity(intent);
            finish();
        } else {
            application.initLoginStatus();
            if (application.isLogin()) {
                String flag = application.getUserBean().flag;
                LogUtil.e(this, "isLogin ;flag=" + flag);
                switch (flag) {
                    case "1":
                        LogUtil.e(this, "审核中");
                        LoginActivity.start(this, null);
                        finish();
                        break;
                    case "2":
                        LogUtil.e(this, "被驳回");
                        LoginActivity.start(this, null);
                        finish();
                        break;
                    case "3":
                        LogUtil.e(this, "信息未完善");
                        LoginActivity.start(this, null);
                        finish();
                        break;
                    case "0":
                    default:
                        MainActivity.start(this, null);
                        finish();
                }

            } else {
                LoginActivity.start(this, null);
                finish();
            }
        }
    }
}
