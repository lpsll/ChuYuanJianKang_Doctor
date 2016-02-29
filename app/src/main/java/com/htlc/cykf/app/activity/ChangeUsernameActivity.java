package com.htlc.cykf.app.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.htlc.cykf.R;

/**
 * Created by sks on 2016/2/15.
 */
public class ChangeUsernameActivity extends BaseActivity implements View.OnClickListener {
    private TextView mTextUsername,mTextButton;
    private EditText mEditText;
    private LinearLayout mLinearUsername;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_username);
        setupView();
    }
    private void setupView() {
        findViewById(R.id.imageBack).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        mLinearUsername = (LinearLayout) findViewById(R.id.linearUsername);
        mTextUsername = (TextView) findViewById(R.id.textUsername);
        mTextButton = (TextView) findViewById(R.id.textButton);
        mEditText = (EditText) findViewById(R.id.editInput);
        mTextButton.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.textButton:
                execute();
                break;
        }
    }

    private void execute() {
        String buttonName = mTextButton.getText().toString();
        if("更换".equals(buttonName)){
            mLinearUsername.setVisibility(View.GONE);
            mEditText.setVisibility(View.VISIBLE);
            mTextButton.setText("下一步");
        }else if("下一步".equals(buttonName)){
            checkPassword();
            mEditText.setHint("请输入新手机号");
            mTextButton.setText("绑定");
        }else if("绑定".equals(buttonName)){
            bindNewTel();
        }
    }

    private void checkPassword() {

    }

    private void bindNewTel() {

    }
}
