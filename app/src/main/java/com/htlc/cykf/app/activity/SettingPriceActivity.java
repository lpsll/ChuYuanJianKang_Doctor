package com.htlc.cykf.app.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.htlc.cykf.R;
import com.htlc.cykf.app.App;
import com.htlc.cykf.app.adapter.LengthAdapter;
import com.htlc.cykf.app.util.LogUtil;
import com.htlc.cykf.app.util.ToastUtil;
import com.htlc.cykf.core.ActionCallbackListener;
import com.htlc.cykf.model.PriceBean;

import java.util.ArrayList;

/**
 * Created by sks on 2016/3/23.
 */
public class SettingPriceActivity extends BaseActivity implements View.OnClickListener {
    private ListView mLengthListView;
    private ArrayList mLengthList = new ArrayList();
    private BaseAdapter mLengthAdapter;
    private TextView mTextButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting_price);
        setupView();
    }

    private void setupView() {
        findViewById(R.id.imageBack).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        mLengthListView = (ListView) findViewById(R.id.lengthListView);
        mTextButton = (TextView) findViewById(R.id.textButton);

        mTextButton.setOnClickListener(this);
        mLengthAdapter = new LengthAdapter(mLengthList, this);
        mLengthListView.setAdapter(mLengthAdapter);
        mLengthListView.setItemChecked(0, true);
        initData();

    }

    private void initData() {
        for(int i=0; i<4;i++){
            PriceBean bean = new PriceBean();
            bean.price = "0";
            mLengthList.add(bean);
        }
        appAction.getPriceList(new ActionCallbackListener<ArrayList<PriceBean>>() {
            @Override
            public void onSuccess(ArrayList<PriceBean> data) {
                notifyDataSetChanged(data);
            }

            @Override
            public void onFailure(String errorEvent, String message) {
                if(handleNetworkOnFailure(errorEvent, message)) return;
                ToastUtil.showToast(App.app, message);
            }
        });
    }

    private void notifyDataSetChanged(ArrayList<PriceBean> data) {
        for (PriceBean bean : data) {
            if("1".equals(bean.duration)){
                bean.durationDes = "一个月";
            }else if("3".equals(bean.duration)){
                bean.durationDes = "三个月";
            }else if("6".equals(bean.duration)){
                bean.durationDes = "半年";
            }else if("12".equals(bean.duration)){
                bean.durationDes = "一年";
            }
        }
        mLengthList.clear();
        mLengthList.addAll(data);
        mLengthAdapter.notifyDataSetChanged();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.textButton:
                setPrice();
                break;
        }
    }

    private void setPrice() {
        String one = "0",three="0",six="0",twelve="0";
        for(int i=0; i<mLengthList.size();i++){
            PriceBean bean = (PriceBean) mLengthList.get(i);
            if("1".equals(bean.duration)){
                one = bean.price;
            }else if("3".equals(bean.duration)){
                three = bean.price;
            }else if("6".equals(bean.duration)){
                six = bean.price;
            }else if("12".equals(bean.duration)){
                twelve = bean.price;
            }
            LogUtil.e(this,bean.duration+":"+bean.price);
        }
        appAction.setPriceList(one, three, six, twelve, new ActionCallbackListener<Void>() {
            @Override
            public void onSuccess(Void data) {
                ToastUtil.showToast(App.app,"设置成功");
            }

            @Override
            public void onFailure(String errorEvent, String message) {
                if(handleNetworkOnFailure(errorEvent, message)) return;
                ToastUtil.showToast(App.app,message);
            }
        });
    }
}
