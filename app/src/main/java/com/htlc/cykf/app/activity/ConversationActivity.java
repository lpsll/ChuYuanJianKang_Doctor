package com.htlc.cykf.app.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.htlc.cykf.R;
import com.htlc.cykf.app.App;
import com.htlc.cykf.app.util.ImageLoaderCfg;
import com.htlc.cykf.app.util.LogUtil;
import com.htlc.cykf.app.util.ToastUtil;
import com.htlc.cykf.core.ActionCallbackListener;
import com.htlc.cykf.model.DischargeSummaryBean;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.Locale;

import io.rong.imkit.fragment.ConversationFragment;
import io.rong.imlib.model.Conversation;

/**
 * Created by sks on 2016/2/15.
 */
public class ConversationActivity extends BaseActivity implements View.OnClickListener {
    private View mImageBack;

    private TextView mTextTitle, mTextDischarge;
    private View mLinearDischargeAbstract, mTextDischargeShow, mImageDischargeGone,
            mTextButtonDrugInfo, mTextButtonBodyInfo,mScrollDischarge;

    private ImageView mImageHead;
    private TextView mTextName, mTextDischargeIntroduce, mTextIllness;
    private DischargeSummaryBean mDischargeSummaryBean;
    /**
     * 目标 Id
     */
    private String mTargetId;
    private String mTitle;

    /**
     * 刚刚创建完讨论组后获得讨论组的id 为targetIds，需要根据 为targetIds 获取 targetId
     */
    private String mTargetIds;

    /**
     * 会话类型
     */
    private Conversation.ConversationType mConversationType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_conversation);
        Intent intent = getIntent();
        setupView();
        getIntentDate(intent);
        getContactInfo();
    }

    private void setupView() {
        mTextTitle = (TextView) findViewById(R.id.textTitle);
        mImageBack = findViewById(R.id.imageBack);
        mImageBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        mImageHead = (ImageView) findViewById(R.id.imageHead);
        mTextButtonDrugInfo = findViewById(R.id.textButtonDrugInfo);
        mTextButtonBodyInfo = findViewById(R.id.textButtonBodyInfo);
        mImageHead.setOnClickListener(this);
        mTextButtonDrugInfo.setOnClickListener(this);
        mTextButtonBodyInfo.setOnClickListener(this);

        mTextName = (TextView) findViewById(R.id.textName);
        mTextIllness = (TextView) findViewById(R.id.textIllness);
        mTextDischargeIntroduce = (TextView) findViewById(R.id.textDischargeIntroduce);

        mTextDischarge = (TextView) findViewById(R.id.textDischarge);
        mTextDischargeShow = findViewById(R.id.textDischargeShow);
        mImageDischargeGone = findViewById(R.id.imageDischargeGone);
        mScrollDischarge = findViewById(R.id.scrollDischarge);
        mLinearDischargeAbstract = findViewById(R.id.linearDischargeAbstract);
        mTextDischargeShow.setOnClickListener(this);
        mImageDischargeGone.setOnClickListener(this);

    }

    private void refreshView() {
        ImageLoader.getInstance().displayImage(mDischargeSummaryBean.photo, mImageHead, ImageLoaderCfg.options);
        mTextName.setText(mDischargeSummaryBean.name);
        mTextIllness.setText(mDischargeSummaryBean.subjectName);

        String introduce = "出院时间：%1$s 住院时间：%2$s";
        introduce = String.format(introduce, mDischargeSummaryBean.hospitalize, mDischargeSummaryBean.discharged);
        mTextDischargeIntroduce.setText(introduce);
        String html = "<font color=\"#3E3A39\">出院小结</font>" +
                "<br/>" +
                "<font color=\"#3E3A39\">姓 名：</font>" +
                "<font color=\"#CCCCCC\">%1$s</font>" +
                "<br/>" +
                "<font color=\"#3E3A39\">姓 别：</font>" +
                "<font color=\"#CCCCCC\">%2$s</font>" +
                "<br/>" +
                "<font color=\"#3E3A39\">年 龄：</font>" +
                "<font color=\"#CCCCCC\">%3$s</font>" +
                "<br/>" +
                "<font color=\"#3E3A39\">职 业：</font>" +
                "<font color=\"#CCCCCC\">%4$s</font>" +
                "<br/>" +
                "<font color=\"#3E3A39\">入院日期：</font>" +
                "<font color=\"#CCCCCC\">%5$s</font>" +
                "<br/>" +
                "<font color=\"#3E3A39\">出院日期：</font>" +
                "<font color=\"#CCCCCC\">%6$s</font>" +
                "<br/>" +
                "<font color=\"#3E3A39\">住院天数：</font>" +
                "<font color=\"#CCCCCC\">%7$s</font>" +
                "<br/>" +
                "<font color=\"#3E3A39\">入院诊断：</font>" +
                "<font color=\"#CCCCCC\">%8$s</font>" +
                "<br/>" +
                "<font color=\"#3E3A39\">出院诊断：</font>" +
                "<font color=\"#CCCCCC\">%9$s</font>" +
                "<br/>" +
                "<font color=\"#3E3A39\">治疗结果：</font>" +
                "<font color=\"#CCCCCC\">%10$s</font>" +
                "<br/>" +
                "<font color=\"#3E3A39\">各种特殊检查号：</font>" +
                "<font color=\"#CCCCCC\">%11$s</font>" +
                "<br/><br/>" +
                "<font color=\"#3E3A39\">入院情况：</font>" +
                "<br/>" +
                "<font color=\"#CCCCCC\">%12$s</font>" +
                "<br/><br/>" +
                "<font color=\"#3E3A39\">住院情况：</font>" +
                "<br/>" +
                "<font color=\"#CCCCCC\">%13$s</font>" +
                "<br/><br/>" +
                "<font color=\"#3E3A39\">出院情况：</font>" +
                "<br/>" +
                "<font color=\"#CCCCCC\">%14$s</font>" +
                "<br/><br/>" +
                "<font color=\"#3E3A39\">出院医嘱：</font>" +
                "<br/>" +
                "<font color=\"#CCCCCC\">%15$s</font>" +
                "<br/>";
        String sex = "男";
        if ("1".equals(mDischargeSummaryBean.sex)) {
            sex = "女";
        }
        html = String.format(html,
                mDischargeSummaryBean.name, sex, mDischargeSummaryBean.age, mDischargeSummaryBean.profession,
                mDischargeSummaryBean.hospitalize, mDischargeSummaryBean.discharged, mDischargeSummaryBean.inDay,
                mDischargeSummaryBean.inDiagnose, mDischargeSummaryBean.outDiagnose, mDischargeSummaryBean.result, mDischargeSummaryBean.checkNum,
                mDischargeSummaryBean.inInfo, mDischargeSummaryBean.onIfo, mDischargeSummaryBean.outInfo, mDischargeSummaryBean.advice);
        mTextDischarge.setText(Html.fromHtml(html));

    }

    /**
     * 展示如何从 Intent 中得到 融云会话页面传递的 Uri
     */
    private void getIntentDate(Intent intent) {

        mTargetId = intent.getData().getQueryParameter("targetId");
        mTitle = intent.getData().getQueryParameter("title");
        mTextTitle.setText(mTitle);
        mTargetIds = intent.getData().getQueryParameter("targetIds");
        //intent.getData().getLastPathSegment();//获得当前会话类型

        mConversationType = Conversation.ConversationType.valueOf(intent.getData().getLastPathSegment().toUpperCase(Locale.getDefault()));

        enterFragment(mConversationType, mTargetId);
    }

    /**
     * 加载会话页面 ConversationFragment
     *
     * @param mConversationType 会话类型
     * @param mTargetId         目标 Id
     */
    private void enterFragment(Conversation.ConversationType mConversationType, String mTargetId) {

        ConversationFragment fragment = (ConversationFragment) getSupportFragmentManager().findFragmentById(R.id.conversation);

        Uri uri = Uri.parse("rong://" + getApplicationInfo().packageName).buildUpon()
                .appendPath("conversation").appendPath(mConversationType.getName().toLowerCase())
                .appendQueryParameter("targetId", mTargetId).build();

        fragment.setUri(uri);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.textDischargeShow:
                showDischarge();
                break;
            case R.id.imageDischargeGone:
                goneDischarge();
                break;
            case R.id.imageHead:
                goMeasure("file:///android_asset/h5/html/doctor/patienter.html","");
                break;
            case R.id.textButtonDrugInfo:
                goMeasure("file:///android_asset/h5/html/doctor/yaopinInfo.html", "");
                break;
            case R.id.textButtonBodyInfo:
                goMeasure("file:///android_asset/h5/html/doctor/tiZhengInfo.html", "");
                break;
        }
    }

    private void goMeasure(String url, String name){
        Intent intent = new Intent(this, MeasureActivity.class);
        intent.putExtra(MeasureActivity.Url, url);
        intent.putExtra(MeasureActivity.Title, name);
        intent.putExtra(MeasureActivity.PatientId, mTargetId);
        startActivity(intent);
    }

    private void goneDischarge() {
        mLinearDischargeAbstract.setVisibility(View.VISIBLE);
        mScrollDischarge.setVisibility(View.GONE);
    }

    private void showDischarge() {
        mLinearDischargeAbstract.setVisibility(View.GONE);
        mScrollDischarge.setVisibility(View.VISIBLE);
    }

    public void getContactInfo() {
        appAction.conversationDetail(mTargetId, new ActionCallbackListener<DischargeSummaryBean>() {
            @Override
            public void onSuccess(DischargeSummaryBean data) {
                LogUtil.e(this, "请求成功");
                LogUtil.e(this, data.toString());
                mDischargeSummaryBean = data;
                refreshView();
            }

            @Override
            public void onFailure(String errorEvent, String message) {
                if(handleNetworkOnFailure(errorEvent, message)) return;
                ToastUtil.showToast(App.app, message);
            }
        });
    }
}
