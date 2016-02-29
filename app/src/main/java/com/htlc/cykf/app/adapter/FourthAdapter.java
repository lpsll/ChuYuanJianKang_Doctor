package com.htlc.cykf.app.adapter;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.htlc.cykf.R;
import com.htlc.cykf.app.bean.FourthAdapterBean;
import com.zhy.autolayout.utils.AutoUtils;

import java.util.ArrayList;

/**
 * Created by sks on 2016/1/28.
 */
public class FourthAdapter extends BaseAdapter{
    private Activity mActivity;
    private ArrayList mList;

    public FourthAdapter(ArrayList list, Activity activity) {
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
            convertView = View.inflate(mActivity, R.layout.adapter_fourth_fragment, null);
            holder.imageView = (ImageView) convertView.findViewById(R.id.imageView);
            holder.textView = (TextView) convertView.findViewById(R.id.textView);
            holder.empty = convertView.findViewById(R.id.empty);
            convertView.setTag(holder);
            //对于listview，注意添加这一行，即可在item上使用高度
            AutoUtils.autoSize(convertView);
        }else {
            holder = (ViewHolder) convertView.getTag();
        }
        //具体数据处理
        FourthAdapterBean bean = (FourthAdapterBean) mList.get(position);
        holder.imageView.setImageResource(bean.imageId);
        holder.textView.setText(bean.name);
        if(bean.haveEmpty){
            holder.empty.setVisibility(View.VISIBLE);
        }else {
            holder.empty.setVisibility(View.GONE);
        }
        return convertView;
    }
    class ViewHolder{
        ImageView imageView;
        TextView textView;
        View empty;
    }
}
