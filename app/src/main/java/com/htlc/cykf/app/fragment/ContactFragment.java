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
import com.htlc.cykf.app.adapter.SecondAdapter;
import com.htlc.cykf.app.util.LogUtil;
import com.htlc.cykf.app.util.ToastUtil;
import com.htlc.cykf.app.widget.SideBar;
import com.htlc.cykf.core.ActionCallbackListener;
import com.htlc.cykf.model.ContactBean;

import java.util.ArrayList;

import io.rong.imkit.RongIM;

/**
 * Created by sks on 2016/1/27.
 */
public class ContactFragment extends BaseFragment implements AdapterView.OnItemClickListener {

    private ListView mListView;
    private ArrayList mList = new ArrayList();
    private BaseAdapter mAdapter;
    private TextView mTextDialog;
    private SideBar mSideBar;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_contact, null);
        setupView(view);

        return view;
    }

    private void setupView(View view) {
        mListView = (ListView) view.findViewById(R.id.listView);
        mAdapter = new SecondAdapter(mList, getActivity());
        mListView.setAdapter(mAdapter);
        mListView.setOnItemClickListener(this);

        mTextDialog = (TextView) view.findViewById(R.id.textDialog);
        mSideBar = (SideBar) view.findViewById(R.id.sideBar);
        mSideBar.setTextView(mTextDialog);
        mSideBar.setOnTouchingLetterChangedListener(new SideBar.OnTouchingLetterChangedListener() {
            @Override
            public void onTouchingLetterChanged(String s) {
                int position = ((SecondAdapter) mAdapter).getFirstPositionOfType(s);
                LogUtil.e(ContactFragment.this, "onTouchingLetterChanged---position=?" + position + ";---type=?" + s);
                if (position != -1) {
                    mListView.setSelection(position+1);
                }
            }
        });
        initData();
    }

    private void initData() {
//        String userName = "Larno";
//        String[] type = {"A", "B", "C"};
//        for (int i = 0; i < 20; i++) {
//            ContactBean bean = new ContactBean();
//            bean.userName = userName;
//            bean.group = type[0];
//            bean.userId = i+"";
//            mList.add(bean);
//        }
//        for (int i = 0; i < 10; i++) {
//            ContactBean bean = new ContactBean();
//            bean.userName = userName;
//            bean.group = type[1];
//            bean.userId = i+"";
//            mList.add(bean);
//        }
//        for (int i = 0; i < 15; i++) {
//            ContactBean bean = new ContactBean();
//            bean.userName = userName;
//            bean.group = type[2];
//            bean.userId = i+"";
//            mList.add(bean);
//        }
//        mAdapter.notifyDataSetChanged();
        String userId = baseActivity.application.getUserBean().userid;
        baseActivity.appAction.contactList(userId, new ActionCallbackListener<ArrayList<ContactBean>>() {
            @Override
            public void onSuccess(ArrayList<ContactBean> data) {
                mList.addAll(data);
                mAdapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(String errorEvent, String message) {
                ToastUtil.showToast(getActivity(),message);
            }
        });

    }

    public void getMoreData() {

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
            RongIM.getInstance().startPrivateChat(getActivity(),bean.userId,bean.userName);
        }
    }
}
