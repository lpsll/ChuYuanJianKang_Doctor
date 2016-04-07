
package com.htlc.cykf.app.activity;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;

import com.htlc.cykf.R;
import com.htlc.cykf.app.App;
import com.htlc.cykf.app.util.AppManager;
import com.htlc.cykf.app.util.CommonUtil;
import com.htlc.cykf.app.util.Constant;
import com.htlc.cykf.app.util.LogUtil;
import com.htlc.cykf.app.util.ToastUtil;
import com.htlc.cykf.core.AppAction;
import com.kaopiz.kprogresshud.KProgressHUD;
import com.nostra13.universalimageloader.core.ImageLoader;

public abstract class BaseActivity extends AppCompatActivity {
    // 上下文实例
    public Context context;
    // 应用全局的实例
    public App application;
    // 核心层的Action实例
    public AppAction appAction;
    protected KProgressHUD mProgressHUD;
    protected Dialog mTipsDialog;

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

    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        AppManager.getAppManager().removeActivity(this);
    }
    private void showReloginDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("登录过期");//设置对话框标题
        builder.setMessage("请重新登录！");//设置显示的内容
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {//添加确定按钮
            @Override
            public void onClick(DialogInterface dialog, int which) {//确定按钮的响应事件
                LoginActivity.start(BaseActivity.this,null);
                finish();
            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
        Button positiveButton = alertDialog.getButton(AlertDialog.BUTTON_POSITIVE);
        positiveButton.setTextColor(CommonUtil.getResourceColor(R.color.text_blue));
    }
    public boolean handleNetworkOnFailure(String errorEvent, String message){
        if("请重新登录".equals(message)){
            application.setIsLogin(false);
            application.clearUserBean();
            showReloginDialog();
            return true;
        }
        return false;
    }
    public void showProgressHUD(){
        mProgressHUD = KProgressHUD.create(this)
                .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                .setDimAmount(0.5f).show();
    }
    public void dismissProgressHUD(){
        if(mProgressHUD!=null){
            mProgressHUD.dismiss();
        }
    }

    public void showTipsDialog(View view, int width, int height,boolean cancelable) {
        mTipsDialog = new Dialog(this, R.style.CenterDialog);
        mTipsDialog.setContentView(view);
        mTipsDialog.setCancelable(cancelable);
        Window dialogWindow = mTipsDialog.getWindow();
        dialogWindow.setGravity(Gravity.CENTER);
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        WindowManager m = getWindowManager();
        Display d = m.getDefaultDisplay(); // 获取屏幕宽、高用
        LogUtil.e("屏幕宽高：","width="+d.getWidth()+";height="+d.getHeight());
        if(width!=0){
            lp.width = width * d.getWidth() / Constant.ScreenWidth; // 宽度
        }
        if(height!=0){
            lp.height = height * d.getHeight() / Constant.ScreenHeight; // 高度
        }
        lp.alpha = 1.0f; // 透明度
        // 当Window的Attributes改变时系统会调用此函数,可以直接调用以应用上面对窗口参数的更改,也可以用setAttributes
        // dialog.onWindowAttributesChanged(lp);
        dialogWindow.setAttributes(lp);
        mTipsDialog.show();
    }
    public void dismissTipsDialog(){
        if(mTipsDialog!=null){
            mTipsDialog.dismiss();
        }
    }
}
