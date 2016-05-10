package com.htlc.cykf.app.fragment;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.bigkoo.pickerview.OptionsPickerView;
import com.htlc.cykf.R;
import com.htlc.cykf.app.App;
import com.htlc.cykf.app.adapter.GroupContactListAdapter;
import com.htlc.cykf.app.adapter.SecondAdapter;
import com.htlc.cykf.app.util.CommonUtil;
import com.htlc.cykf.app.util.LogUtil;
import com.htlc.cykf.app.util.ToastUtil;
import com.htlc.cykf.app.widget.SideBar;
import com.htlc.cykf.core.ActionCallbackListener;
import com.htlc.cykf.core.ErrorEvent;
import com.htlc.cykf.model.ContactBean;
import com.htlc.cykf.model.ContactGroupBean;
import com.htlc.cykf.model.PayStatusBean;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.GregorianCalendar;

import de.greenrobot.event.EventBus;
import io.rong.imkit.RongIM;

/**
 * Created by sks on 2016/1/27.
 * 付费用户
 */
public class ContactFragment1 extends BaseFragment implements AdapterView.OnItemClickListener, AdapterView.OnItemLongClickListener {

    private ListView mListView, mGroupListView;
    private ArrayList<ContactBean> mList = new ArrayList();
    private ArrayList<ContactBean> mGroupList = new ArrayList();
    private BaseAdapter mAdapter;
    private GroupContactListAdapter mGroupAdapter;

    private ArrayList<ContactGroupBean> mGroups = new ArrayList<>();
    private OptionsPickerView mPickViePwOptions;

    private TextView mTextDialog;
    private SideBar mSideBar;

    private View mView;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (mView == null) {
            mView = inflater.inflate(R.layout.fragment_contact_1, null);
            setupView(mView);
        }
        return mView;
    }
    @Override
    public void onStart() {
        super.onStart();
        getContactList();
        LogUtil.e(this, "onStart refresh contact List  0");
    }

    @Override
    public void onResume() {
        super.onResume();
        LogUtil.e(this, "onResume refresh contact List  0");
    }

    private void setupView(View view) {
        mGroupListView = (ListView) view.findViewById(R.id.groupListView);
        mGroupAdapter = new GroupContactListAdapter(mGroupList, getActivity());
        mGroupListView.setAdapter(mGroupAdapter);
        mGroupListView.setOnItemClickListener(this);
        mGroupListView.setOnItemLongClickListener(this);

        mListView = (ListView) view.findViewById(R.id.listView);
        mAdapter = new SecondAdapter(mList, getActivity());
        mListView.setAdapter(mAdapter);
        mListView.setOnItemClickListener(this);
        mListView.setOnItemLongClickListener(this);


        mTextDialog = (TextView) view.findViewById(R.id.textDialog);
        mSideBar = (SideBar) view.findViewById(R.id.sideBar);
        mSideBar.setTextView(mTextDialog);
        mSideBar.setOnTouchingLetterChangedListener(new SideBar.OnTouchingLetterChangedListener() {
            @Override
            public void onTouchingLetterChanged(String s) {
                int position = ((SecondAdapter) mAdapter).getFirstPositionOfType(s);
                LogUtil.e(ContactFragment1.this, "onTouchingLetterChanged---position=?" + position + ";---type=?" + s);
                if (position != -1) {
                    mListView.setSelection(position + 1);
                }
            }
        });

    }

    /**
     * 付费，获取好友列表
     */
    @Override
    public void getContactList() {
        baseActivity.appAction.contactListByType("0", new ActionCallbackListener<ArrayList<ContactBean>>() {
            @Override
            public void onSuccess(ArrayList<ContactBean> data) {
                mList.clear();
                mGroupList.clear();
                for (ContactBean bean : data) {
                    LogUtil.e(this, "group:" + bean.group + ";");
                    if (TextUtils.isEmpty(bean.group)) {
                        mList.add(bean);
                    } else {
                        mGroupList.add(bean);
                    }
                    Collections.sort(mGroupList, new Comparator<ContactBean>() {
                        @Override
                        public int compare(ContactBean lhs, ContactBean rhs) {
                            if ("重点患者".equals(lhs.group)) {
                                return -1;
                            } else if ("重点患者".equals(rhs.group)) {
                                return 1;
                            } else {
                                return lhs.group.compareTo(rhs.group);
                            }
                        }
                    });
                }
                mAdapter.notifyDataSetChanged();
                mGroupAdapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(String errorEvent, String message) {
                if (handleNetworkOnFailure(errorEvent, message)) return;
            }
        });
        queryGroupList();
    }

    /**
     * 查询当前分组列表
     */
    private void queryGroupList() {
        baseActivity.appAction.queryGroup(new ActionCallbackListener<ArrayList<ContactGroupBean>>() {
            @Override
            public void onSuccess(ArrayList<ContactGroupBean> data) {
                mGroups.clear();
                mGroups.addAll(data);
            }

            @Override
            public void onFailure(String errorEvent, String message) {
                if (handleNetworkOnFailure(errorEvent, message)) return;
                if (!ErrorEvent.NETWORK_ERROR.equals(errorEvent)) {
                    mGroups.clear();
                }
            }
        });
    }

    /**
     * 点击进入会话界面
     * @param parent
     * @param view
     * @param position
     * @param id
     */
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        ContactBean bean;
        if (parent == mListView) {
            bean = (ContactBean) mList.get(position);
        } else {
            bean = (ContactBean) mGroupList.get(position);
        }
        if (RongIM.getInstance() != null) {
            /**
             * 启动单聊界面。
             *
             * @param context      应用上下文。
             * @param targetUserId 要与之聊天的用户 Id。
             * @param title        聊天的标题，如果传入空值，则默认显示与之聊天的用户名称。
             */
            RongIM.getInstance().startPrivateChat(getActivity(), bean.userid, bean.name);
        }
    }


    /**
     * 长按进行对用户分组操作
     * @param parent
     * @param view
     * @param position
     * @param id
     * @return
     */
    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
        if (parent == mListView) {
            LogUtil.e(this, "listview");
            showContactOptions(position);
        } else {
            LogUtil.e(this, "Group listview");
            showGroupOptions(position);
        }
        return true;
    }

    /**
     *对没有在分组中的用户，进行分组操作，1.移动到重点患者，2.弹出分组列表
     * @param position
     */
    private void showContactOptions(int position) {
        final ContactBean bean = (ContactBean) mList.get(position);
        LogUtil.e(this, "被长按的位置:" + position + ";id:" + bean.userid);
        View dialogOptions = View.inflate(getActivity(), R.layout.dialog_options_2, null);
        TextView textOption1 = (TextView) dialogOptions.findViewById(R.id.textOption1);
        textOption1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                moveToExperienceGroup(bean);
                baseActivity.dismissTipsDialog();
            }
        });
        TextView textOption2 = (TextView) dialogOptions.findViewById(R.id.textOption2);
        textOption2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectGroup(bean);
                baseActivity.dismissTipsDialog();
            }
        });
        baseActivity.showTipsDialog(dialogOptions, 400, 280, true);
    }

    /**
     * 对在分组中的用户，进行分组操作，1.从当前分组移除，2.弹出分组列表(如果为重点患者组，判断付费状态)
     * @param position
     */
    private void showGroupOptions(int position) {
        final ContactBean bean = (ContactBean) mGroupList.get(position);
        LogUtil.e(this, "被长按的位置:" + position + ";id:" + bean.userid);
        View dialogOptions = View.inflate(getActivity(), R.layout.dialog_options_1, null);
        TextView textOption1 = (TextView) dialogOptions.findViewById(R.id.textOption1);
        textOption1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if ("重点患者".equals(bean.group)) {
                    getPayStatus(bean, null);
                } else {
                    removeFromGroup(bean);
                }
                baseActivity.dismissTipsDialog();
            }
        });
        TextView textOption2 = (TextView) dialogOptions.findViewById(R.id.textOption2);
        textOption2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectGroup(bean);
                baseActivity.dismissTipsDialog();
            }
        });
        baseActivity.showTipsDialog(dialogOptions, 400, 280, true);
    }

    /**
     * 判断付费状态
     * @param bean
     * @param groupId
     */
    private void getPayStatus(final ContactBean bean, final String groupId) {

        baseActivity.appAction.getContactPayStatus(bean.userid, new ActionCallbackListener<PayStatusBean>() {
            @Override
            public void onSuccess(PayStatusBean data) {
                if ("1".equals(data.flag)) {
                    if (TextUtils.isEmpty(groupId)) {
                        removeFromExperienceGroup(bean);
                    } else {
                        moveToGroup(bean, groupId);
                    }
                } else if ("2".equals(data.flag)) {
                    showConfirmDialog("该用户未付费，你确定将用户移动到未付费中？", bean, groupId);
                } else if ("3".equals(data.flag)) {
                    showConfirmDialog("该用户费用已到期，你确定将用户移动到续费中？", bean, groupId);
                }
            }

            @Override
            public void onFailure(String errorEvent, String message) {
                ToastUtil.showToast(App.app, message);
            }
        });
    }

    /**
     * 对未付费用户操作，进行确认弹窗
     * @param msg
     * @param bean
     * @param groupId
     */
    private void showConfirmDialog(String msg, final ContactBean bean, final String groupId) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("温馨提示");//设置对话框标题
        builder.setMessage(msg);//设置显示的内容

        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {//添加确定按钮
            @Override
            public void onClick(DialogInterface dialog, int which) {//确定按钮的响应事件
                if(TextUtils.isEmpty(groupId)){
                    removeFromExperienceGroup(bean);
                }else {
                    moveToGroup(bean, groupId);
                }
            }
        });
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {//添加返回按钮
            @Override
            public void onClick(DialogInterface dialog, int which) {//响应事件

            }

        });//在按键响应事件中显示此对话框
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
        Button positiveButton = alertDialog.getButton(AlertDialog.BUTTON_POSITIVE);
        positiveButton.setTextColor(CommonUtil.getResourceColor(R.color.text_blue));
        Button negativeButton = alertDialog.getButton(AlertDialog.BUTTON_NEGATIVE);
        negativeButton.setTextColor(CommonUtil.getResourceColor(R.color.text_blue));
    }

    private void selectGroup(final ContactBean bean) {
        if(mGroups.size()<1){
            ToastUtil.showToast(App.app, "当前无分组！");
            return;
        }
        mPickViePwOptions = new OptionsPickerView(getActivity());
        //三级不联动效果  false
        mPickViePwOptions.setPicker(mGroups);
        mPickViePwOptions.setCyclic(false);
        //设置默认选中的三级项目
        //监听确定选择按钮
        mPickViePwOptions.setSelectOptions(0);
        mPickViePwOptions.setOnOptionsSelectListener(new OptionsPickerView.OnOptionsSelectListener() {
            @Override
            public void onOptionsSelect(int options1, int options2, int options3) {
                ContactGroupBean contactGroupBean = mGroups.get(options1);
                if ("重点患者".equals(bean.group)) {
                    getPayStatus(bean, contactGroupBean.id);
                } else {
                    moveToGroup(bean, contactGroupBean.id);
                }
            }
        });
        mPickViePwOptions.show();
    }


    private void removeFromGroup(ContactBean bean) {
        baseActivity.appAction.groupOption(bean.userid, "1", bean.group, new ActionCallbackListener<Void>() {
            @Override
            public void onSuccess(Void data) {
                EventBus.getDefault().post(new ContactBean());
                getContactList();
            }

            @Override
            public void onFailure(String errorEvent, String message) {
                if (handleNetworkOnFailure(errorEvent, message)) return;
                ToastUtil.showToast(App.app, message);
            }
        });
    }

    private void moveToGroup(ContactBean bean, String groupId) {
        baseActivity.appAction.groupOption(bean.userid, "0", groupId, new ActionCallbackListener<Void>() {
            @Override
            public void onSuccess(Void data) {
                EventBus.getDefault().post(new ContactBean());
                getContactList();
            }

            @Override
            public void onFailure(String errorEvent, String message) {
                if (handleNetworkOnFailure(errorEvent, message)) return;
                ToastUtil.showToast(App.app, message);
            }
        });
    }

    /**
     * 从重点患者组移除
     *
     * @param bean
     */
    private void removeFromExperienceGroup(ContactBean bean) {
        String flag = "0";
        experienceGroupOptions(bean, flag);
    }

    private void moveToExperienceGroup(ContactBean bean) {
        String flag = "3";
        experienceGroupOptions(bean, flag);
    }

    private void experienceGroupOptions(ContactBean bean, String flag) {
        baseActivity.appAction.groupExperienceOption(bean.userid, flag, new ActionCallbackListener<Void>() {
            @Override
            public void onSuccess(Void data) {
                EventBus.getDefault().post(new ContactBean());
                getContactList();
            }

            @Override
            public void onFailure(String errorEvent, String message) {
                if (handleNetworkOnFailure(errorEvent, message)) return;
                ToastUtil.showToast(App.app, message);
            }
        });
    }
}
