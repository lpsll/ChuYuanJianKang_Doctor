package com.htlc.cykf.app.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.astuetz.PagerSlidingTabStrip;
import com.bigkoo.pickerview.OptionsPickerView;
import com.htlc.cykf.R;
import com.htlc.cykf.app.App;
import com.htlc.cykf.app.adapter.FirstPagerAdaptor;
import com.htlc.cykf.app.util.CommonUtil;
import com.htlc.cykf.app.util.LogUtil;
import com.htlc.cykf.app.util.ToastUtil;
import com.htlc.cykf.core.ActionCallbackListener;
import com.htlc.cykf.model.BindNumberBean;
import com.htlc.cykf.model.DepartmentBean;
import com.htlc.cykf.model.DoctorBean;
import com.htlc.cykf.model.IllnessBean;

import java.util.ArrayList;

/**
 * Created by sks on 2016/1/27.
 */
public class SecondFragment extends HomeFragment implements View.OnClickListener {


    private TextView mTextDepartment, mTextIllness, mTextBindNumber, mTextButtonSendVerification, mTextButton, mTextTips;
    private EditText mEditTel;
    private DoctorBean mDoctorBean;
    private ArrayList<IllnessBean> mIllnessBeans = new ArrayList<>();
    private OptionsPickerView mPickViePwOptions;
    private String mIllnessId;
    private String patientPhone;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_second, null);
        setupView(view);
        return view;
    }

    private void setupView(View view) {
        mTextDepartment = (TextView) view.findViewById(R.id.textDepartment);
        mTextIllness = (TextView) view.findViewById(R.id.textIllness);
        mTextBindNumber = (TextView) view.findViewById(R.id.textBindNumber);
        mTextButtonSendVerification = (TextView) view.findViewById(R.id.textButtonSendVerification);
        mTextButton = (TextView) view.findViewById(R.id.textButton);
        mTextTips = (TextView) view.findViewById(R.id.textTips);
        mEditTel = (EditText) view.findViewById(R.id.editTel);
        mEditTel.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if(!TextUtils.isEmpty(patientPhone)){
                    patientPhone = "";
                    clearBindNumber();
                }
            }
        });

        mTextIllness.setOnClickListener(this);
        mTextButtonSendVerification.setOnClickListener(this);
        LogUtil.e(mTextButtonSendVerification,mTextButtonSendVerification.getText().toString());
        mTextButton.setOnClickListener(this);
        getDoctorInfo();
        getIllnessList();
    }

    public void getDoctorInfo() {
        baseActivity.appAction.personInfo(new ActionCallbackListener<DoctorBean>() {
            @Override
            public void onSuccess(DoctorBean data) {
                LogUtil.e(SecondFragment.this, "获取医生信息：" + data);
                mDoctorBean = data;
                refreshView();
            }

            @Override
            public void onFailure(String errorEvent, String message) {
                if(handleNetworkOnFailure(errorEvent, message)) return;
                LogUtil.e(SecondFragment.this, message);
            }
        });
    }

    private void refreshView() {
        if (mDoctorBean != null && mDoctorBean.department != null) {
            mTextDepartment.setText(mDoctorBean.department);
        }
    }

    public void getIllnessList() {
        baseActivity.appAction.illnessList(new ActionCallbackListener<ArrayList<IllnessBean>>() {
            @Override
            public void onSuccess(ArrayList<IllnessBean> data) {
                mIllnessBeans.clear();
                mIllnessBeans.addAll(data);
            }

            @Override
            public void onFailure(String errorEvent, String message) {
                if (handleNetworkOnFailure(errorEvent, message)) return;
                LogUtil.e(SecondFragment.this, message);
            }
        });
    }

    private void selectIllness() {
        if (mIllnessBeans.size() < 1) {
            baseActivity.appAction.illnessList(new ActionCallbackListener<ArrayList<IllnessBean>>() {
                @Override
                public void onSuccess(ArrayList<IllnessBean> data) {
                    mIllnessBeans.clear();
                    mIllnessBeans.addAll(data);
                    selectIllness();
                }

                @Override
                public void onFailure(String errorEvent, String message) {
                    if(handleNetworkOnFailure(errorEvent, message)) return;
                    LogUtil.e(SecondFragment.this, message);
                }
            });
        }
        //选项选择器
        mPickViePwOptions = new OptionsPickerView(getActivity());
        //三级不联动效果  false
        mPickViePwOptions.setPicker(mIllnessBeans);
        mPickViePwOptions.setCyclic(false);
        //设置默认选中的三级项目
        //监听确定选择按钮
        mPickViePwOptions.setSelectOptions(0);
        mPickViePwOptions.setOnOptionsSelectListener(new OptionsPickerView.OnOptionsSelectListener() {
            @Override
            public void onOptionsSelect(int options1, int options2, int options3) {
                mTextIllness.setText(mIllnessBeans.get(options1).disease);
                mIllnessId = mIllnessBeans.get(options1).id;
            }
        });
        mPickViePwOptions.show();
    }

    @Override
    public void onClick(View v) {
        LogUtil.e(this, v.getId() + ";");
        switch (v.getId()) {
            case R.id.textIllness:
                selectIllness();
                break;
            case R.id.textButtonSendVerification:
                sendVerificationCode();
                break;
            case R.id.textButton:
                generateBindNumber();
                break;
        }
    }

    private void generateBindNumber() {
        final String phone = mEditTel.getText().toString().trim();
        baseActivity.appAction.generateBindNumber(mDoctorBean.departmentid, mIllnessId, phone, new ActionCallbackListener<BindNumberBean>() {
            @Override
            public void onSuccess(BindNumberBean data) {
                mTextBindNumber.setText(data.num);
                mTextTips.setVisibility(View.VISIBLE);
                patientPhone = phone;
            }

            @Override
            public void onFailure(String errorEvent, String message) {
                if (handleNetworkOnFailure(errorEvent, message)) return;
                ToastUtil.showToast(App.app, message);
            }
        });
    }

    private void sendVerificationCode() {
        LogUtil.e(this, "sendBindNumber");
        String bindNumber = mTextBindNumber.getText().toString().trim();
        if(TextUtils.isEmpty(bindNumber)){
            ToastUtil.showToast(App.app,"请生成推荐码");
            return;
        }
        baseActivity.appAction.sendBindNumber(patientPhone, new ActionCallbackListener<Void>() {
            @Override
            public void onSuccess(Void data) {
                ToastUtil.showToast(App.app, "发送成功！");
                clearBindNumber();
            }

            @Override
            public void onFailure(String errorEvent, String message) {
                if(handleNetworkOnFailure(errorEvent, message)) return;
                ToastUtil.showToast(App.app, message);
            }
        });
    }

    private void clearBindNumber() {
        mTextTips.setVisibility(View.GONE);
        mTextBindNumber.setText("");
        mEditTel.setText("");
    }
}
