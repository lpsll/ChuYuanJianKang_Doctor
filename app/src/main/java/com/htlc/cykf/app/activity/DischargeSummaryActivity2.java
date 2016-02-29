package com.htlc.cykf.app.activity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;

import com.bigkoo.pickerview.OptionsPickerView;
import com.bigkoo.pickerview.TimePickerView;
import com.htlc.cykf.R;
import com.htlc.cykf.app.util.CommonUtil;
import com.htlc.cykf.app.util.DateFormat;
import com.htlc.cykf.app.util.ToastUtil;
import com.htlc.cykf.core.ActionCallbackListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by sks on 2016/2/15.
 */
public class DischargeSummaryActivity2 extends BaseActivity implements View.OnClickListener {
    public static final String Name = "Name";
    public static final String Age = "Age";
    public static final String Sex = "Sex";
    private boolean isFemale;
    private String name;
    private String age;
    private String job;

    private boolean isEditable = false;
    private TextView mTextLeft, mTextRight;
    private View mImageBack;
    private ArrayList<String> jobs = new ArrayList<String>();

    private EditText mEditName, mEditAge;
    private RadioButton mRadioButton0, mRadioButton1;
    private TextView mTextStartTime, mTextEndTime, mTextTotalTime, mEditJob;
    private EditText mEditInDiagnosis, mEditOutDiagnosis, mEditEffect, mEditSpecialItem,
            mEditInStatus, mEditAtStatus, mEditOutStatus, mEditOutConsideration;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        isFemale = getIntent().getBooleanExtra(Sex, false);
        name = getIntent().getStringExtra(Name);
        age = getIntent().getStringExtra(Age);
        setContentView(R.layout.activity_discharge_summary2);

        setupView();
    }

    private void setupView() {
        mImageBack = findViewById(R.id.imageBack);
        mImageBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        mTextLeft = (TextView) findViewById(R.id.textLeft);
        mTextLeft.setOnClickListener(this);
        mTextRight = (TextView) findViewById(R.id.textRight);
        mTextRight.setOnClickListener(this);

        mEditName = (EditText) findViewById(R.id.editName);
        mEditAge = (EditText) findViewById(R.id.editAge);
        mEditJob = (TextView) findViewById(R.id.editJob);
        mEditJob.setOnClickListener(this);
        mRadioButton0 = (RadioButton) findViewById(R.id.radioButton0);
        mRadioButton1 = (RadioButton) findViewById(R.id.radioButton1);
        if (!TextUtils.isEmpty(name)) {
            mEditName.setText(name);
        }
        if (!TextUtils.isEmpty(age)) {
            mEditAge.setText(age);
        }
        if (!TextUtils.isEmpty(job)) {
            mEditJob.setText(job);
        }
        mRadioButton0.setChecked(!isFemale);
        mRadioButton1.setChecked(isFemale);

        mTextStartTime = (TextView) findViewById(R.id.textStartTime);
        mTextStartTime.setText(DateFormat.getTimeByDay(new Date(System.currentTimeMillis())));
        mTextEndTime = (TextView) findViewById(R.id.textEndTime);
        mTextEndTime.setText(DateFormat.getTimeByDay(new Date(System.currentTimeMillis())));
        mTextTotalTime = (TextView) findViewById(R.id.textTotalTime);
        mTextTotalTime.setText("1");
        mTextStartTime.setOnClickListener(this);
        mTextEndTime.setOnClickListener(this);

        mEditInDiagnosis = (EditText) findViewById(R.id.editInDiagnosis);
        mEditOutDiagnosis = (EditText) findViewById(R.id.editOutDiagnosis);
        mEditInStatus = (EditText) findViewById(R.id.editInStatus);
        mEditAtStatus = (EditText) findViewById(R.id.editAtStatus);
        mEditOutStatus = (EditText) findViewById(R.id.editOutStatus);
        mEditOutConsideration = (EditText) findViewById(R.id.editOutConsideration);
        mEditEffect = (EditText) findViewById(R.id.editEffect);
        mEditSpecialItem = (EditText) findViewById(R.id.editSpecialItem);


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.textStartTime:
                if (!isEditable) return;
                textTime(true);
                break;
            case R.id.textEndTime:
                if (!isEditable) return;
                textTime(false);
                break;
            case R.id.textLeft:
                commit();
                break;
            case R.id.textRight:
                changeEditStatus();
                break; 
            case R.id.editJob:
                selectJob();
                break;
        }
    }

    private void selectJob() {
        //选项选择器
        OptionsPickerView pickViePwOptions = new OptionsPickerView(this);
        jobs.clear();
        String[] jobArray = CommonUtil.getResourceStringArray(R.array.activity_perfect_info_jobs);
        for (int i=0;i<jobArray.length;i++){
            jobs.add(jobArray[i]);
        }
        //三级不联动效果  false
        pickViePwOptions.setPicker(jobs);
        pickViePwOptions.setCyclic(false);
        //设置默认选中的三级项目
        //监听确定选择按钮
        pickViePwOptions.setSelectOptions(0);
        pickViePwOptions.setOnOptionsSelectListener(new OptionsPickerView.OnOptionsSelectListener() {
            @Override
            public void onOptionsSelect(int options1, int options2, int options3) {
                job = jobs.get(options1);
                mEditJob.setText(job);
            }
        });
        pickViePwOptions.show();
    }

    private void changeEditStatus() {
        if (isEditable) {
            mEditInDiagnosis.setEnabled(false);
            mEditOutDiagnosis.setEnabled(false);
            mEditEffect.setEnabled(false);
            mEditSpecialItem.setEnabled(false);
            mEditAtStatus.setEnabled(false);
            mEditOutStatus.setEnabled(false);
            mEditOutConsideration.setEnabled(false);

            mTextRight.setText("修改");
            mTextLeft.setVisibility(View.GONE);
            mImageBack.setVisibility(View.VISIBLE);
        } else {

            mEditOutDiagnosis.setEnabled(true);
            mEditOutDiagnosis.setFocusable(true);
            mEditOutDiagnosis.setFocusableInTouchMode(true);
            mEditOutDiagnosis.requestFocus();

            mEditEffect.setEnabled(true);
            mEditEffect.setFocusable(true);
            mEditEffect.setFocusableInTouchMode(true);
            mEditEffect.requestFocus();

            mEditSpecialItem.setEnabled(true);
            mEditSpecialItem.setFocusable(true);
            mEditSpecialItem.setFocusableInTouchMode(true);
            mEditSpecialItem.requestFocus();

            mEditInStatus.setEnabled(true);
            mEditInStatus.setFocusable(true);
            mEditInStatus.setFocusableInTouchMode(true);
            mEditInStatus.requestFocus();

            mEditAtStatus.setEnabled(true);
            mEditAtStatus.setFocusable(true);
            mEditAtStatus.setFocusableInTouchMode(true);
            mEditAtStatus.requestFocus();

            mEditOutStatus.setEnabled(true);
            mEditOutStatus.setFocusable(true);
            mEditOutStatus.setFocusableInTouchMode(true);
            mEditOutStatus.requestFocus();

            mEditOutConsideration.setEnabled(true);
            mEditOutConsideration.setFocusable(true);
            mEditOutConsideration.setFocusableInTouchMode(true);
            mEditOutConsideration.requestFocus();

            mEditInDiagnosis.setEnabled(true);
            mEditInDiagnosis.setFocusable(true);
            mEditInDiagnosis.setFocusableInTouchMode(true);
            mEditInDiagnosis.requestFocus();

            mTextRight.setText("取消");
            mTextLeft.setVisibility(View.VISIBLE);
            mImageBack.setVisibility(View.GONE);
        }
        isEditable = !isEditable;
    }

    private void commit() {
        String userId = application.getUserBean().userid;
        String startTime = mTextStartTime.getText().toString();
        String endTime = mTextEndTime.getText().toString();
        String totalTime = mTextTotalTime.getText().toString();
        String inDiagnose = mEditInDiagnosis.getText().toString().trim();
        String outDiagnose = mEditOutDiagnosis.getText().toString().trim();
        String effect = mEditEffect.getText().toString().trim();
        String specialItem = mEditSpecialItem.getText().toString().trim();
        String inStatus = mEditInStatus.getText().toString().trim();
        String atStatus = mEditAtStatus.getText().toString().trim();
        String outStatus = mEditOutStatus.getText().toString().trim();
        String outAdvice = mEditOutConsideration.getText().toString().trim();
        appAction.dischargeSummary(userId, startTime, endTime, totalTime,
                inDiagnose, outDiagnose, effect, specialItem,
                inStatus, atStatus, outStatus, outAdvice, new ActionCallbackListener<Void>() {
                    @Override
                    public void onSuccess(Void data) {
                        ToastUtil.showToast(DischargeSummaryActivity2.this, "提交成功");
                    }

                    @Override
                    public void onFailure(String errorEvent, String message) {
                        ToastUtil.showToast(DischargeSummaryActivity2.this, message);
                    }
                });
    }

    private void textTime(final boolean isStart) {
        //时间选择器
        TimePickerView timePickerView = new TimePickerView(this, TimePickerView.Type.YEAR_MONTH_DAY);
        //控制时间范围
        Calendar calendar = Calendar.getInstance();
        timePickerView.setRange(calendar.get(Calendar.YEAR) - 20, calendar.get(Calendar.YEAR));
        timePickerView.setTime(new Date());
        timePickerView.setCyclic(true);
        timePickerView.setCancelable(true);
        //时间选择后回调
        timePickerView.setOnTimeSelectListener(new TimePickerView.OnTimeSelectListener() {

            @Override
            public void onTimeSelect(Date date) {
                if (isStart) {
                    String endTime = mTextEndTime.getText().toString();
                    String startTime = DateFormat.getTimeByDay(date);
                    long totalTime = DateFormat.getDays(startTime, endTime);
                    if (totalTime >= 0) {
                        totalTime++;
                        mTextStartTime.setText(startTime);
                        mTextTotalTime.setText(totalTime + "");
                    }
                } else {
                    String startTime = mTextStartTime.getText().toString();
                    String endTime = DateFormat.getTimeByDay(date);
                    long totalTime = DateFormat.getDays(startTime, endTime);
                    if (totalTime >= 0) {
                        totalTime++;
                        mTextEndTime.setText(endTime);
                        mTextTotalTime.setText(totalTime + "");
                    }
                }

            }
        });
        timePickerView.show();
    }
}
