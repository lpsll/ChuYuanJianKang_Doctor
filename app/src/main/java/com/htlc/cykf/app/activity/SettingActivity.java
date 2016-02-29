package com.htlc.cykf.app.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;

import com.htlc.cykf.R;
import com.htlc.cykf.app.adapter.FourthAdapter;
import com.htlc.cykf.app.bean.FourthAdapterBean;
import com.htlc.cykf.app.util.AppManager;
import com.htlc.cykf.app.util.CommonUtil;
import com.htlc.cykf.app.util.LogUtil;

import java.util.ArrayList;

/**
 * Created by sks on 2016/2/15.
 */
public class SettingActivity extends BaseActivity implements AdapterView.OnItemClickListener {

    private ListView mListView;
    private ArrayList mList = new ArrayList();
    private BaseAdapter mAdapter;
    private int[] itemImageIds;
    private String[] itemNames;
    private boolean[] itemHaveEmptys;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        setupView();
    }
    private void setupView() {
        findViewById(R.id.imageBack).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        mListView = (ListView) findViewById(R.id.listView);
        mAdapter = new FourthAdapter(mList, this);
        mListView.setAdapter(mAdapter);
        mListView.setOnItemClickListener(this);
        initData();
    }

    private void initData() {
        itemImageIds = new int[]{R.mipmap.activity_setting_1, R.mipmap.activity_setting_2, R.mipmap.activity_setting_3};
        itemNames = CommonUtil.getResourceStringArray(R.array.activity_setting_children);
        itemHaveEmptys = new boolean[]{true,false,true};
        for(int i=0; i<itemImageIds.length;i++){
            FourthAdapterBean bean = new FourthAdapterBean();
            bean.imageId = itemImageIds[i];
            bean.name = itemNames[i];
            bean.haveEmpty = itemHaveEmptys[i];
            mList.add(bean);
        }
        mAdapter.notifyDataSetChanged();
    }


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        FourthAdapterBean bean = (FourthAdapterBean) mList.get(position);
        LogUtil.e(this, bean.name);
        switch (position){
            case 0:

                break;
            case 1:

                break;
            case 2:
                logout();
                break;
        }
    }

    private void logout() {
        application.clearUserBean();
        startActivity(new Intent(this, LoginActivity.class));
        finish();
        AppManager.getAppManager().finishAllActivity();
    }
}
