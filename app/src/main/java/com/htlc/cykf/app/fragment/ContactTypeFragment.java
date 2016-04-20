package com.htlc.cykf.app.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.astuetz.PagerSlidingTabStrip;
import com.htlc.cykf.R;
import com.htlc.cykf.app.adapter.FirstPagerAdaptor;
import com.htlc.cykf.app.util.CommonUtil;
import com.htlc.cykf.app.util.LogUtil;
import com.htlc.cykf.model.ContactBean;

import java.util.ArrayList;

import de.greenrobot.event.EventBus;

/**
 * Created by sks on 2016/1/27.
 */
public class ContactTypeFragment extends BaseFragment {

    private PagerSlidingTabStrip mIndicator;
    private ViewPager mViewPager;
    private FirstPagerAdaptor mAdapter;
    private ArrayList<BaseFragment> mList = new ArrayList();
    private View mView;
    public boolean[] childNeedRefresh = new boolean[]{true,true,true};


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LogUtil.e(this, "onCreate");
        EventBus.getDefault().register(this);
        if(mList.size()<1){
            mList.clear();
            mList.add(new ContactFragment1());
            mList.add(new ContactFragment2());
            mList.add(new ContactFragment0());
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);//反注册EventBus
    }
    public void onEventMainThread(ContactBean event) {
        LogUtil.e("OrdersFragment","onEventMainThread");
        for(int i=0; i<childNeedRefresh.length; i++){
            childNeedRefresh[i] = true;
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        LogUtil.e(this, "onCreateView");
        mView = inflater.inflate(R.layout.fragment_contact_type, null);
        setupView(mView);
        return mView;
    }

    private void setupView(View view) {
        //------------------------------------------
        mIndicator = (PagerSlidingTabStrip) view.findViewById(R.id.indicator);
        mViewPager = (ViewPager) view.findViewById(R.id.viewPager);
        initTab();
        mAdapter = new FirstPagerAdaptor(getChildFragmentManager(), mList);
        mViewPager.setAdapter(mAdapter);
        mViewPager.setOffscreenPageLimit(3);
        mIndicator.setViewPager(mViewPager);
        mIndicator.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                mIndicator.setTranslationX(0);
            }

            @Override
            public void onPageSelected(int position) {
                LogUtil.i(ContactTypeFragment.this, "onPageSelected    " + position);
                LinearLayout tabsLinearLayout = (LinearLayout) (mIndicator.getChildAt(0));
                for (int i = 0; i < tabsLinearLayout.getChildCount(); i++) {
                    TextView textView = (TextView) tabsLinearLayout.getChildAt(i);
                    if (i == position) {
                        LogUtil.i("OrdersFragment", "i == position    " + position);
                        textView.setTextColor(getResources().getColor(R.color.text_blue));
                        if(childNeedRefresh[position]){
                            childNeedRefresh[position] = false;
                            LogUtil.e("OrdersFragment","position:"+position+childNeedRefresh);
                            mList.get(position).getContactList();
                        }
                    } else {
                        textView.setEnabled(true);
                        textView.setTextColor(getResources().getColor(R.color.text_black));
                    }
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        LinearLayout tabsLinearLayout = (LinearLayout) mIndicator.getChildAt(0);
        for (int i = 0; i < tabsLinearLayout.getChildCount(); i++) {
            TextView textView = (TextView) tabsLinearLayout.getChildAt(i);
            if (i == 0) {
                textView.setTextColor(getResources().getColor(R.color.text_blue));
            } else {
                textView.setTextColor(getResources().getColor(R.color.text_black));
            }
        }
    }

    public void initTab() {

        //tab 宽度均分
        mIndicator.setShouldExpand(true);

        /**
         * pstsIndicatorColor   滑动条的颜色
         pstsUnderlineColor  滑动条所在的那个全宽线的颜色
         pstsDividerColor  每个标签的分割线的颜色
         pstsIndicatorHeight       滑动条的高度
         pstsUnderlineHeight 滑动条所在的那个全宽线的高度
         pstsDividerPadding 分割线底部和顶部的填充宽度
         pstsTabPaddingLeftRight   每个标签左右填充宽度
         pstsScrollOffset       Scroll offset of the selected tab
         pstsTabBackground   每个标签的背景，应该是一个StateListDrawable
         pstsShouldExpand   如果设置为true，每个标签是相同的控件，均匀平分整个屏幕，默认是false
         pstsTextAllCaps    如果为true，所有标签都是大写字母，默认为true
         */
        mIndicator.setTextSize(CommonUtil.dp2px(getActivity(), 14));
//        mIndicator.setTextColor(this.getResources().getColor(R.color.black_blue_color_selector));
//        mIndicator.setTextColorResource(R.color.text_color_red_gray_selector);
        //设置下划线
        mIndicator.setUnderlineColor(this.getResources().getColor(R.color.divider_gray));
        mIndicator.setUnderlineHeight(0);
        //设置滑动指示线
        mIndicator.setIndicatorColor(this.getResources().getColor(R.color.text_blue));
        mIndicator.setIndicatorHeight(0);
        //设置tab间分割线
        mIndicator.setDividerColor(this.getResources().getColor(R.color.divider_gray));
        //设置背景颜色
        mIndicator.setBackgroundColor(this.getResources().getColor(android.R.color.white));
    }
}
