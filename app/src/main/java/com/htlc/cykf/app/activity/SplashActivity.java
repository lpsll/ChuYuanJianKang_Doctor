package com.htlc.cykf.app.activity;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;

import com.htlc.cykf.R;

/**
 * Created by sks on 2016/2/15.
 */
public class SplashActivity extends BaseActivity {

    private ImageView mImageView;
    @Override
    protected boolean isRegisterRongIMConnectionListener() {
        return false;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mImageView = new ImageView(this);
        mImageView.setImageResource(R.mipmap.ic_launcher);
        setContentView(mImageView);

    }

    @Override
    protected void onResume() {
        super.onResume();
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

    private void goNextActivity() {
        Intent intent = null;
        if (application.isLogin()) {
            String flag = application.getUserBean().flag;
            switch (flag){
                case "0":
                case "3":
                    intent = new Intent(SplashActivity.this, MainActivity.class);
                    break;
                case "1":
                    intent = new Intent(SplashActivity.this, RecommendationActivity.class);
                    intent.putExtra(RecommendationActivity.IsLoginBind,true);
                    break;
                case "2":
                    intent = new Intent(SplashActivity.this, PerfectInfoActivity.class);
                    break;
                default:
                    intent = new Intent(SplashActivity.this, MainActivity.class);
            }

        }else {
            intent = new Intent(SplashActivity.this, LoginActivity.class);
        }
        startActivity(intent);
        finish();
    }
}
