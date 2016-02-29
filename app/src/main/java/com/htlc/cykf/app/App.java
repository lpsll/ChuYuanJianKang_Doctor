package com.htlc.cykf.app;

import android.app.ActivityManager;
import android.app.Application;
import android.content.Context;
import android.text.TextUtils;

import com.htlc.cykf.app.util.CommonUtil;
import com.htlc.cykf.app.util.Constant;
import com.htlc.cykf.app.util.LogUtil;
import com.htlc.cykf.app.util.RongIMUtil;
import com.htlc.cykf.app.util.SharedPreferenceUtil;
import com.htlc.cykf.core.AppAction;
import com.htlc.cykf.core.AppActionImpl;
import com.htlc.cykf.model.UserBean;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;

import io.rong.imkit.RongIM;

/**
 * Created by sks on 2015/12/29.
 */
public class App extends Application {
    private AppAction appAction;
    public static App app;
    private boolean isOnline = false;//用户是否在线
    private boolean isLogin = false;//用户是否登录了
    private UserBean userBean;

    @Override
    public void onCreate() {
        super.onCreate();
        app = this;
        appAction = new AppActionImpl(this);
        initImageLoader(this);
        initRongIM();
        initLoginStatus();

    }

    /**
     * 初始化登录状态
     */
    private void initLoginStatus() {
        String username = SharedPreferenceUtil.getString(CommonUtil.getApplication(), Constant.USERNAME, "");
        String password = SharedPreferenceUtil.getString(CommonUtil.getApplication(), Constant.PASSWORD, "");
        String user_id = SharedPreferenceUtil.getString(CommonUtil.getApplication(), Constant.USER_ID, "");
        String token = SharedPreferenceUtil.getString(CommonUtil.getApplication(), Constant.TOKEN, "ss");
        if (TextUtils.isEmpty(username)) {
            return;
        }
        if (TextUtils.isEmpty(password)) {
            return;
        }
        if (TextUtils.isEmpty(user_id)) {
            return;
        }
        if (TextUtils.isEmpty(token)) {
            return;
        }
        RongIMUtil.connect(token);
        LogUtil.e(this, "isLogin = true");
        isLogin = true;
    }


    /**
     * 融云初始化
     */
    private void initRongIM() {
        /**
         * OnCreate 会被多个进程重入，这段保护代码，确保只有您需要使用 RongIM 的进程和 Push 进程执行了 init。
         * io.rong.push 为融云 push 进程名称，不可修改。
         */
        if (getApplicationInfo().packageName.equals(getCurProcessName(getApplicationContext())) ||
                "io.rong.push".equals(getCurProcessName(getApplicationContext()))) {

            /**
             * IMKit SDK调用第一步 初始化
             */
            RongIM.init(this);
        }
    }

    /**
     * 获得当前进程的名字
     *
     * @param context
     * @return 进程号
     */
    public static String getCurProcessName(Context context) {

        int pid = android.os.Process.myPid();

        ActivityManager activityManager = (ActivityManager) context
                .getSystemService(Context.ACTIVITY_SERVICE);

        for (ActivityManager.RunningAppProcessInfo appProcess : activityManager
                .getRunningAppProcesses()) {

            if (appProcess.pid == pid) {
                return appProcess.processName;
            }
        }
        return null;
    }

    /**
     * 初始化ImageLoader
     *
     * @param context
     */
    public static void initImageLoader(Context context) {
        // This configuration tuning is custom. You can tune every option, you may tune some of them,
        // or you can create default configuration by
        //  ImageLoaderConfiguration.createDefault(this);
        // method.
        ImageLoaderConfiguration.Builder config = new ImageLoaderConfiguration.Builder(context);
        config.threadPriority(Thread.NORM_PRIORITY - 2);
        config.denyCacheImageMultipleSizesInMemory();
        config.diskCacheFileNameGenerator(new Md5FileNameGenerator());
        config.diskCacheSize(50 * 1024 * 1024); // 50 MiB
        config.tasksProcessingOrder(QueueProcessingType.LIFO);
        config.writeDebugLogs(); // Remove for release app

        // Initialize ImageLoader with configuration.
        ImageLoader.getInstance().init(config.build());
    }

    public AppAction getAppAction() {
        return appAction;
    }

    public boolean isOnline() {
        return isOnline;
    }

    public void setIsOnline(boolean isOnline) {
        this.isOnline = isOnline;
    }

    public boolean isLogin() {
        return isLogin;
    }

    public void setIsLogin(boolean isLogin) {
        this.isLogin = isLogin;
    }

    public void setUserBean(UserBean userBean) {
        if (!TextUtils.isEmpty(userBean.username)) {
            SharedPreferenceUtil.putString(this, Constant.USERNAME, userBean.username);
        }
        if (!TextUtils.isEmpty(userBean.password)) {
            SharedPreferenceUtil.putString(this, Constant.PASSWORD, userBean.password);
        }
        if (!TextUtils.isEmpty(userBean.token)) {
            SharedPreferenceUtil.putString(this, Constant.TOKEN, userBean.token);
        }
        if (!TextUtils.isEmpty(userBean.userid)) {
            SharedPreferenceUtil.putString(this, Constant.USER_ID, userBean.userid);
        }
        if (!TextUtils.isEmpty(userBean.name)) {
            SharedPreferenceUtil.putString(this, Constant.NAME, userBean.name);
        }
        if (!TextUtils.isEmpty(userBean.photo)) {
            SharedPreferenceUtil.putString(this, Constant.IMAGE, userBean.photo);
        }
        if (!TextUtils.isEmpty(userBean.flag)) {
            SharedPreferenceUtil.putString(this, Constant.USER_INFO_FLAG, userBean.flag);
        }


    }

    public void clearUserBean() {
        SharedPreferenceUtil.remove(this, Constant.USERNAME);
        SharedPreferenceUtil.remove(this, Constant.PASSWORD);
        SharedPreferenceUtil.remove(this, Constant.TOKEN);
        SharedPreferenceUtil.remove(this, Constant.USER_ID);
        SharedPreferenceUtil.remove(this, Constant.NAME);
        SharedPreferenceUtil.remove(this, Constant.IMAGE);
        SharedPreferenceUtil.remove(this, Constant.USER_INFO_FLAG);
    }

    public UserBean getUserBean() {
        UserBean bean = new UserBean();
        bean.username = SharedPreferenceUtil.getString(this,Constant.USERNAME,"");
        bean.password = SharedPreferenceUtil.getString(this,Constant.PASSWORD,"");
        bean.userid = SharedPreferenceUtil.getString(this,Constant.USER_ID,"");
        bean.name = SharedPreferenceUtil.getString(this,Constant.NAME,"");
        bean.photo = SharedPreferenceUtil.getString(this,Constant.IMAGE,"");
        bean.token = SharedPreferenceUtil.getString(this,Constant.TOKEN,"");
        bean.flag = SharedPreferenceUtil.getString(this,Constant.USER_INFO_FLAG,"");
        return bean;
    }
}
