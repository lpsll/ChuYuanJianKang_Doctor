package com.htlc.cykf.app.adapter;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.htlc.cykf.R;
import com.htlc.cykf.app.widget.AuthorityItem;
import com.zhy.autolayout.utils.AutoUtils;

import java.util.ArrayList;

/**
 * Created by sks on 2016/1/28.
 */
public class AuthorityAdapter extends BaseAdapter{
    private Activity mActivity;
    private ArrayList mList;

    public AuthorityAdapter(ArrayList list, Activity activity) {
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
    public View getView(int position, View convertView, ViewGroup parent) {
        if(convertView == null){
            convertView = View.inflate(mActivity, R.layout.adapter_authority_item, null);
            //对于listview，注意添加这一行，即可在item上使用高度
            AutoUtils.autoSize(convertView);
        }
        //具体数据处理
        String bean = (String) mList.get(position);
        AuthorityItem authorityItem = (AuthorityItem) convertView;
        authorityItem.getTextAuthority().setText(bean);
        return convertView;
    }
}
