package com.htlc.cykf.app.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.htlc.cykf.R;
import com.htlc.cykf.app.App;
import com.htlc.cykf.app.adapter.SecondAdapter;
import com.htlc.cykf.app.util.LogUtil;
import com.htlc.cykf.app.util.ToastUtil;
import com.htlc.cykf.app.widget.SideBar;
import com.htlc.cykf.core.ActionCallbackListener;
import com.htlc.cykf.model.ContactBean;

import java.util.ArrayList;

import de.greenrobot.event.EventBus;
import io.rong.imkit.RongIM;

/**
 * Created by sks on 2016/1/27.
 */
public class ContactFragment2 extends BaseFragment implements AdapterView.OnItemClickListener, AdapterView.OnItemLongClickListener {

    private ListView mListView;
    private ArrayList mList = new ArrayList();
    private BaseAdapter mAdapter;
    private TextView mTextDialog;
    private SideBar mSideBar;
    private View mView;
//    @Override
//    public void onCreate(@Nullable Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        EventBus.getDefault().register(this);
//    }
//    @Override
//    public void onDestroy() {
//        super.onDestroy();
//        EventBus.getDefault().unregister(this);//反注册EventBus
//    }
//    public void onEventMainThread(ContactBean event) {
//        String msg = "onEventMainThread收到了消息：";
//        getContactList();
//    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (mView == null) {
            mView = inflater.inflate(R.layout.fragment_contact, null);
            setupView(mView);
        }
        return mView;
    }

    @Override
    public void onStart() {
        super.onStart();
        LogUtil.e(this, "onStart refresh contact List  1");
    }

    @Override
    public void onResume() {
        super.onResume();
        LogUtil.e(this, "onResume refresh contact List  1");
    }


    private void setupView(View view) {
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
                LogUtil.e(ContactFragment2.this, "onTouchingLetterChanged---position=?" + position + ";---type=?" + s);
                if (position != -1) {
                    mListView.setSelection(position + 1);
                }
            }
        });
        getContactList();
    }

    @Override
    public void getContactList() {
        baseActivity.appAction.contactListByType("1", new ActionCallbackListener<ArrayList<ContactBean>>() {
            @Override
            public void onSuccess(ArrayList<ContactBean> data) {
                mList.clear();
                mList.addAll(data);
                mAdapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(String errorEvent, String message) {
                if (handleNetworkOnFailure(errorEvent, message)) return;
            }
        });
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        ContactBean bean = (ContactBean) mList.get(position);
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

    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
        final ContactBean bean = (ContactBean) mList.get(position);
        LogUtil.e(this, "被长按的位置:" + position + ";id:" + bean.userid);
        View dialogOptions = View.inflate(getActivity(), R.layout.dialog_options_0, null);
        TextView textOption1 = (TextView) dialogOptions.findViewById(R.id.textOption1);
        textOption1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                moveToExperienceGroup(bean);
                baseActivity.dismissTipsDialog();
            }
        });
        baseActivity.showTipsDialog(dialogOptions, 400, 180, true);
        return true;
    }

    private void moveToExperienceGroup(ContactBean bean) {
        baseActivity.appAction.groupExperienceOption(bean.userid, "3", new ActionCallbackListener<Void>() {
            @Override
            public void onSuccess(Void data) {
                EventBus.getDefault().post(new ContactBean());
            }

            @Override
            public void onFailure(String errorEvent, String message) {
                if (handleNetworkOnFailure(errorEvent, message)) return;
                ToastUtil.showToast(App.app, message);
            }
        });
    }
}
