package com.htlc.cykf.api.net.okhttp;

import android.util.Log;

import com.htlc.cykf.app.util.Constant;

/**
 * Created by zhy on 15/11/6.
 */
public class L
{

    public static void e(String msg)
    {
        if (Constant.isDebug)
        {
            Log.e("OkHttp", msg);
        }
    }

}

