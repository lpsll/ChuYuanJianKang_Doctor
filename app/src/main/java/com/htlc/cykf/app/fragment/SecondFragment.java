package com.htlc.cykf.app.fragment;

import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.htlc.cykf.R;

import io.rong.imkit.fragment.ConversationListFragment;
import io.rong.imlib.model.Conversation;

/**
 * Created by sks on 2016/1/27.
 */
public class SecondFragment extends HomeFragment implements View.OnClickListener {


//    private FrameLayout mFrameContainer;
    private TextView mTextChart, mTextContact;
    private LinearLayout mLinearTitleContainer;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_second, null);
        setupView(view);
        return view;
    }

    private void setupView(View view) {
        mLinearTitleContainer = (LinearLayout) view.findViewById(R.id.linearTitleContainer);
        mTextChart = (TextView) view.findViewById(R.id.textChart);
        mTextContact = (TextView) view.findViewById(R.id.textContact);
        mTextChart.setOnClickListener(this);
        mTextContact.setOnClickListener(this);
//        mFrameContainer = (FrameLayout) view.findViewById(R.id.frameContainer);

        changeFragment(new ContactFragment());
    }

    private void changeFragment(Fragment fragment) {
        FragmentManager manager = getChildFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.replace(R.id.frameContainer, fragment);
        transaction.commit();
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.textChart:
                mTextChart.setEnabled(false);
                mTextContact.setEnabled(true);
                mLinearTitleContainer.setBackgroundResource(R.mipmap.fragment_chat_list_title_bg);
                ConversationListFragment fragment = new ConversationListFragment();
                Uri uri = Uri.parse("rong://" + getActivity().getApplicationInfo().packageName).buildUpon()
                        .appendPath("conversationlist")
                        .appendQueryParameter(Conversation.ConversationType.PRIVATE.getName(), "false") //设置私聊会话非聚合显示
                        .appendQueryParameter(Conversation.ConversationType.GROUP.getName(), "true")//设置群组会话聚合显示
                        .appendQueryParameter(Conversation.ConversationType.DISCUSSION.getName(), "false")//设置讨论组会话非聚合显示
                        .appendQueryParameter(Conversation.ConversationType.SYSTEM.getName(), "false")//设置系统会话非聚合显示
                        .build();
                fragment.setUri(uri);
                changeFragment(fragment);
                break;
            case R.id.textContact:
                mTextChart.setEnabled(true);
                mTextContact.setEnabled(false);
                mLinearTitleContainer.setBackgroundResource(R.mipmap.fragment_contact_title_bg);
                changeFragment(new ContactFragment());
                break;
        }
    }
}
