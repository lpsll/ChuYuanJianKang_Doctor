package com.htlc.cykf.app.adapter;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.htlc.cykf.R;
import com.htlc.cykf.app.App;
import com.htlc.cykf.app.activity.BaseActivity;
import com.htlc.cykf.app.activity.GroupManagerActivity;
import com.htlc.cykf.app.util.ToastUtil;
import com.htlc.cykf.core.ActionCallbackListener;
import com.htlc.cykf.model.ContactBean;
import com.htlc.cykf.model.ContactGroupBean;
import com.zhy.autolayout.utils.AutoUtils;

import java.util.ArrayList;

import de.greenrobot.event.EventBus;

/**
 * Created by sks on 2016/1/28.
 */
public class GroupManagerAdapter extends BaseAdapter{
    private GroupManagerActivity mActivity;
    private ArrayList mList;

    public GroupManagerAdapter(ArrayList list, GroupManagerActivity activity) {
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
        ViewHolder holder = null;
        if(convertView == null){
            holder = new ViewHolder();
            convertView = View.inflate(mActivity, R.layout.adapter_group_manager, null);
            holder.imageView = (ImageView) convertView.findViewById(R.id.imageView);
            holder.textView = (TextView) convertView.findViewById(R.id.textView);
            convertView.setTag(holder);
            //对于listview，注意添加这一行，即可在item上使用高度
            AutoUtils.autoSize(convertView);
        }else {
            holder = (ViewHolder) convertView.getTag();
        }
        //具体数据处理
        ContactGroupBean bean = (ContactGroupBean) mList.get(position);
        holder.textView.setText(bean.name);
        holder.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String groupId = ((ContactGroupBean)mList.get(position)).id;
                deleteGroupById(groupId);
            }
        });
        return convertView;
    }

    private void deleteGroupById(String groupId) {
        mActivity.appAction.deleteGroup(groupId, new ActionCallbackListener<Void>() {
            @Override
            public void onSuccess(Void data) {
                EventBus.getDefault().post(new ContactBean());
                mActivity.getGroupList();
            }

            @Override
            public void onFailure(String errorEvent, String message) {
                ToastUtil.showToast(App.app,message);
            }
        });
    }

    class ViewHolder{
        ImageView imageView;
        TextView textView;
    }
}
