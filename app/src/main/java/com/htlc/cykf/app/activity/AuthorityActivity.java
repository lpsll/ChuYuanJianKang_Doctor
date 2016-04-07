package com.htlc.cykf.app.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshScrollView;
import com.htlc.cykf.R;
import com.htlc.cykf.app.App;
import com.htlc.cykf.app.adapter.AuthorityAdapter;
import com.htlc.cykf.app.util.LogUtil;
import com.htlc.cykf.app.util.ToastUtil;
import com.htlc.cykf.core.ActionCallbackListener;
import com.htlc.cykf.model.AuthorityBean;

import java.util.ArrayList;

/**
 * Created by sks on 2016/2/15.
 */
public class AuthorityActivity extends BaseActivity implements View.OnClickListener {
    private PullToRefreshScrollView mScrollView;

    private ListView mListView;
    private ArrayList mList = new ArrayList();
    private BaseAdapter mAdapter;
    private TextView mTextButton;

    private String mAuthority = "0";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_authority);
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
        mScrollView.setMode(PullToRefreshBase.Mode.DISABLED);

        mListView = (ListView) findViewById(R.id.listView);
        mTextButton = (TextView) findViewById(R.id.textButton);

        mTextButton.setOnClickListener(this);
        mAdapter = new AuthorityAdapter(mList, this);
        mListView.setAdapter(mAdapter);
        mListView.setItemChecked(0, true);

        initData();

    }

    private void initData() {
        String[] authorityArray = {"未交费病人可以进行沟通","未交费病人不可以进行沟通"};
        for(int i=0; i<authorityArray.length; i++){
            mList.add(authorityArray[i]);
        }
        appAction.getAuthorityStatus(new ActionCallbackListener<AuthorityBean>() {
            @Override
            public void onSuccess(AuthorityBean data) {
                mAuthority = data.flag;
                int position = Integer.parseInt(data.flag)-1;
                mListView.setItemChecked(position,true);
            }

            @Override
            public void onFailure(String errorEvent, String message) {
                if(handleNetworkOnFailure(errorEvent, message)) return;
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.textButton:
                commit();
                break;
        }
    }

    private void commit() {
        int position = mListView.getCheckedItemPosition();
        LogUtil.e(this,"getCheckedItemPosition:"+position);
        final String authority = (position + 1) + "";
        if(mAuthority.equals(authority)){
            return;
        }
        appAction.setAuthorityStatus(authority, new ActionCallbackListener<Void>() {
            @Override
            public void onSuccess(Void data) {
                ToastUtil.showToast(App.app,"权限设置成功！");
                mAuthority = authority;
            }

            @Override
            public void onFailure(String errorEvent, String message) {
                if(handleNetworkOnFailure(errorEvent, message)) return;
                ToastUtil.showToast(App.app,message);
            }
        });
    }
}
