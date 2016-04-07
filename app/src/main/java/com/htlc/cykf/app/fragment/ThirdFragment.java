package com.htlc.cykf.app.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshScrollView;
import com.htlc.cykf.R;
import com.htlc.cykf.api.Api;
import com.htlc.cykf.app.activity.MeasureActivity;
import com.htlc.cykf.app.activity.WebActivity;
import com.htlc.cykf.app.adapter.ThirdAdapter;
import com.htlc.cykf.app.util.LogUtil;
import com.htlc.cykf.app.util.ToastUtil;
import com.htlc.cykf.core.ActionCallbackListener;
import com.htlc.cykf.model.InformationBean;

import java.util.ArrayList;

/**
 * Created by sks on 2016/1/27.
 */
public class ThirdFragment extends HomeFragment implements View.OnClickListener {
    private RelativeLayout mRelativeIllnessAndDrug, mRelativeDrugAndSign;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_third, null);
        setupView(view);
        return view;
    }

    private void setupView(View view) {
        mRelativeIllnessAndDrug = (RelativeLayout) view.findViewById(R.id.relativeIllnessAndDrug);
        mRelativeDrugAndSign = (RelativeLayout) view.findViewById(R.id.relativeDrugAndSign);

        mRelativeIllnessAndDrug.setOnClickListener(this);
        mRelativeDrugAndSign.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.relativeIllnessAndDrug:
                goMeasure("file:///android_asset/h5/html/doctor/bingQingTJ.html","");
                break;
            case R.id.relativeDrugAndSign:
                goMeasure("file:///android_asset/h5/html/doctor/tiZhengTJ.html","");
                break;
        }
    }
    private void goMeasure(String url, String name){
        Intent intent = new Intent(getActivity(), MeasureActivity.class);
        intent.putExtra(MeasureActivity.Url, url);
        intent.putExtra(MeasureActivity.Title, name);
        startActivity(intent);
    }
}
