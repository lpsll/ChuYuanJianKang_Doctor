package com.htlc.cykf.app.adapter;

import android.app.Activity;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.TextView;

import com.htlc.cykf.R;
import com.htlc.cykf.model.PriceBean;
import com.zhy.autolayout.utils.AutoUtils;

import java.util.ArrayList;

/**
 * Created by sks on 2016/1/28.
 */
public class LengthAdapter extends BaseAdapter {
    private Activity mActivity;
    private ArrayList mList;

    public LengthAdapter(ArrayList list, Activity activity) {
        this.mList = list;
        this.mActivity = activity;
    }

    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View view = View.inflate(mActivity, R.layout.adapter_length_item, null);
        //对于listview，注意添加这一行，即可在item上使用高度
        AutoUtils.autoSize(view);
        //具体数据处理
        TextView textLength = (TextView) view.findViewById(R.id.textLength);
        final EditText editMoney = (EditText) view.findViewById(R.id.editMoney);
        PriceBean bean = (PriceBean) mList.get(position);
        textLength.setText(bean.durationDes);
        editMoney.setText(bean.price);
        editMoney.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                PriceBean temp = (PriceBean) mList.get(position);
                if (TextUtils.isEmpty(s.toString())) {
                    editMoney.setText("0");
                    temp.price = "0";
                }else {
                    temp.price = s.toString();
                }
            }



        });
        return view;
    }
}
