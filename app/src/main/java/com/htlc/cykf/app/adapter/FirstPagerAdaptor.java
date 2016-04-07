package com.htlc.cykf.app.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.view.ViewGroup;

import com.htlc.cykf.R;
import com.htlc.cykf.app.fragment.BaseFragment;
import com.htlc.cykf.app.util.CommonUtil;
import com.htlc.cykf.app.util.LogUtil;

import java.util.ArrayList;

/**
 * Created by sks on 2016/1/27.
 */
public class FirstPagerAdaptor extends FragmentPagerAdapter {

    private ArrayList<BaseFragment> mList;
    public FirstPagerAdaptor(FragmentManager fm, ArrayList<BaseFragment> list) {
        super(fm);
        mList = list;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        super.destroyItem(container, position, object);
    }

    @Override
    public Fragment getItem(int position) {
        return mList.get(position);
    }

    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        if(position>=3) return "";
        return CommonUtil.getResourceStringArray(R.array.fragment_first_children)[position];
    }
}
