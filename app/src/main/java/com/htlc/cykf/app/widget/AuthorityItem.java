package com.htlc.cykf.app.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.CheckBox;
import android.widget.Checkable;
import android.widget.TextView;

import com.htlc.cykf.R;
import com.zhy.autolayout.AutoLinearLayout;

/**
 * Created by sks on 2016/2/16.
 */
public class AuthorityItem extends AutoLinearLayout implements Checkable {
    private boolean mChecked;
    private CheckBox mCheckBox;
    private TextView mTextAuthority;

    public AuthorityItem(Context context) {
        this(context, null);
    }
    public AuthorityItem(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.addView(View.inflate(context, R.layout.adapter_authority, null));
        mCheckBox = (CheckBox) findViewById(R.id.checkBox);
        mTextAuthority = (TextView) findViewById(R.id.textAuthority);
    }

    @Override
    public void setChecked(boolean checked) {
        mChecked = checked;
        mCheckBox.setChecked(mChecked);
    }

    @Override
    public boolean isChecked() {
        return mChecked;
    }

    @Override
    public void toggle() {
        setChecked(!mChecked);
    }

    public TextView getTextAuthority() {
        return mTextAuthority;
    }
}
