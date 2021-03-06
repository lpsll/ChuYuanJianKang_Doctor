package com.htlc.cykf.app.adapter;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.htlc.cykf.R;
import com.htlc.cykf.model.PatientBean;
import com.zhy.autolayout.utils.AutoUtils;

import java.util.ArrayList;

/**
 * Created by sks on 2016/1/28.
 */
public class MyPatientAdapter extends BaseAdapter{
    private Activity mActivity;
    private ArrayList mList;

    public MyPatientAdapter(ArrayList list, Activity activity) {
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
        ViewHolder holder = null;
        if(convertView == null){
            holder = new ViewHolder();
            convertView = View.inflate(mActivity, R.layout.adapter_my_patient, null);
            holder.textTime = (TextView) convertView.findViewById(R.id.textTime);
            holder.textBindNumber = (TextView) convertView.findViewById(R.id.textBindNumber);
            holder.textUsername = (TextView) convertView.findViewById(R.id.textUsername);
            convertView.setTag(holder);
            //对于listview，注意添加这一行，即可在item上使用高度
            AutoUtils.autoSize(convertView);
        }else {
            holder = (ViewHolder) convertView.getTag();
        }
        //具体数据处理
        PatientBean bean = (PatientBean) mList.get(position);
        holder.textBindNumber.setText("推荐码： "+bean.num);
        holder.textTime.setText(bean.date);
        holder.textUsername.setText("手机： "+bean.phone);
        return convertView;
    }
    class ViewHolder{
        TextView textTime,textBindNumber,textUsername;
    }
}
