package com.htlc.cykf.app.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.ScrollView;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshScrollView;
import com.htlc.cykf.R;
import com.htlc.cykf.app.App;
import com.htlc.cykf.app.adapter.MyPatientAdapter;
import com.htlc.cykf.app.util.LogUtil;
import com.htlc.cykf.app.util.ToastUtil;
import com.htlc.cykf.core.ActionCallbackListener;
import com.htlc.cykf.model.PatientBean;

import java.util.ArrayList;

/**
 * Created by sks on 2016/2/15.
 */
public class MyPatientActivity extends BaseActivity{
    private PullToRefreshScrollView mScrollView;

    private ListView mListView;
    private BaseAdapter mAdapter;
    private ArrayList mList = new ArrayList();
    private int mPage = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_patient);
        setupView();
    }
    private void setupView() {
        findViewById(R.id.imageBack).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        mScrollView = (PullToRefreshScrollView) findViewById(R.id.scrollView);
        mScrollView.getRefreshableView().post(new Runnable() {
            @Override
            public void run() {
                mScrollView.getRefreshableView().smoothScrollTo(0, 0);
            }
        });
        mScrollView.setMode(PullToRefreshBase.Mode.BOTH);
        mScrollView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener<ScrollView>() {
            @Override
            public void onRefresh(PullToRefreshBase<ScrollView> refreshView) {
                LogUtil.i(MyPatientActivity.this, "刷新。。。。。。");
                if (refreshView.isShownHeader()) {
                    LogUtil.i("refreshView", "pull-to-refresh-------------------------------------------");
                    initData();
                } else if (refreshView.isShownFooter()) {//上拉加载
                    LogUtil.i("refreshView", "pull-to-load-more------------------------------------------");
                    getMoreData();
                }
            }
        });

        mListView = (ListView) findViewById(R.id.listView);
        mAdapter = new MyPatientAdapter(mList, this);
        mListView.setAdapter(mAdapter);
        initData();
    }

    private void initData() {
        mPage = 0;
        appAction.myPatients(mPage, new ActionCallbackListener<ArrayList<PatientBean>>() {
            @Override
            public void onSuccess(ArrayList<PatientBean> data) {
                mList.clear();
                mList.addAll(data);
                mAdapter.notifyDataSetChanged();
                mPage++;
                mScrollView.onRefreshComplete();
            }

            @Override
            public void onFailure(String errorEvent, String message) {
                if(handleNetworkOnFailure(errorEvent, message)) return;
                ToastUtil.showToast(App.app,message);
                mScrollView.onRefreshComplete();
            }
        });



    }

    public void getMoreData() {
        appAction.myPatients(mPage, new ActionCallbackListener<ArrayList<PatientBean>>() {
            @Override
            public void onSuccess(ArrayList<PatientBean> data) {
                mList.addAll(data);
                mAdapter.notifyDataSetChanged();
                mPage++;
                mScrollView.onRefreshComplete();
            }

            @Override
            public void onFailure(String errorEvent, String message) {
                if(handleNetworkOnFailure(errorEvent, message)) return;
                ToastUtil.showToast(App.app,message);
                mScrollView.onRefreshComplete();
            }
        });

    }
}
