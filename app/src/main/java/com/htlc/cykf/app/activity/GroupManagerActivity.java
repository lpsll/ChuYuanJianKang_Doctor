package com.htlc.cykf.app.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshScrollView;
import com.htlc.cykf.R;
import com.htlc.cykf.app.App;
import com.htlc.cykf.app.adapter.GroupManagerAdapter;
import com.htlc.cykf.app.util.LogUtil;
import com.htlc.cykf.app.util.ToastUtil;
import com.htlc.cykf.core.ActionCallbackListener;
import com.htlc.cykf.model.ContactBean;
import com.htlc.cykf.model.ContactGroupBean;

import java.util.ArrayList;

import de.greenrobot.event.EventBus;

/**
 * Created by sks on 2016/2/15.
 */
public class GroupManagerActivity extends BaseActivity {
    private PullToRefreshScrollView mScrollView;

    private ListView mListView;
    private BaseAdapter mAdapter;
    private ArrayList<ContactGroupBean> mList = new ArrayList();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_manager);
        setupView();
    }

    private void setupView() {
        findViewById(R.id.imageBack).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        findViewById(R.id.imageRight).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAddGroupDialog();
            }
        });
        mScrollView = (PullToRefreshScrollView) findViewById(R.id.scrollView);
        mScrollView.getRefreshableView().post(new Runnable() {
            @Override
            public void run() {
                mScrollView.getRefreshableView().smoothScrollTo(0, 0);
            }
        });
        mScrollView.setMode(PullToRefreshBase.Mode.PULL_FROM_START);
        mScrollView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener<ScrollView>() {
            @Override
            public void onRefresh(PullToRefreshBase<ScrollView> refreshView) {
                LogUtil.i(GroupManagerActivity.this, "刷新。。。。。。");
                if (refreshView.isShownHeader()) {
                    LogUtil.i("refreshView", "pull-to-refresh-------------------------------------------");
                    getGroupList();

                } else if (refreshView.isShownFooter()) {//上拉加载
                    LogUtil.i("refreshView", "pull-to-load-more------------------------------------------");
                }
            }
        });


        mListView = (ListView) findViewById(R.id.listView);
        mAdapter = new GroupManagerAdapter(mList, this);
        mListView.setAdapter(mAdapter);
        getGroupList();
    }

    private void showAddGroupDialog() {
        View dialogOptions =  View.inflate(this, R.layout.dialog_add_group,null);
        final EditText editGroupName = (EditText) dialogOptions.findViewById(R.id.editGroupName);
        TextView textConfirm = (TextView) dialogOptions.findViewById(R.id.textConfirm);
        textConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addGroup(editGroupName.getText().toString());
            }
        });
        TextView textCancel = (TextView) dialogOptions.findViewById(R.id.textCancel);
        textCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismissTipsDialog();
            }
        });
        showTipsDialog(dialogOptions, 400, 300, false);
    }

    public void getGroupList() {
        appAction.queryGroup(new ActionCallbackListener<ArrayList<ContactGroupBean>>() {
            @Override
            public void onSuccess(ArrayList<ContactGroupBean> data) {
                mList.clear();
                mList.addAll(data);
                mAdapter.notifyDataSetChanged();
                mScrollView.onRefreshComplete();
            }

            @Override
            public void onFailure(String errorEvent, String message) {
                mList.clear();
                mAdapter.notifyDataSetChanged();
                mScrollView.onRefreshComplete();
                if(handleNetworkOnFailure(errorEvent, message)) return;
                ToastUtil.showToast(App.app, message);
            }
        });
    }
    private void addGroup(String groupName){
        appAction.addGroup(groupName, new ActionCallbackListener<Void>() {
            @Override
            public void onSuccess(Void data) {
                getGroupList();
                EventBus.getDefault().post(new ContactBean());
                dismissTipsDialog();
            }

            @Override
            public void onFailure(String errorEvent, String message) {
                if(handleNetworkOnFailure(errorEvent, message)) return;
                ToastUtil.showToast(App.app,message);
            }
        });
    }
}
