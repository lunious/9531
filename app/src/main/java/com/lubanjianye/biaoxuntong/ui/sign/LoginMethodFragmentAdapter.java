package com.lubanjianye.biaoxuntong.ui.sign;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 11645 on 2018/1/15.
 */

public class LoginMethodFragmentAdapter extends FragmentPagerAdapter {

    private List<String> mList = new ArrayList<>();
    private ArrayList<Fragment> mFragment = new ArrayList<>();

    public LoginMethodFragmentAdapter(List<String> list, FragmentManager fm) {
        super(fm);
        this.mList = list;
        mFragment.add(new ZhLoginFragment());
        mFragment.add(new YzmLoginFragment());
    }

    @Override
    public Fragment getItem(int position) {
        return mFragment.get(position);
    }

    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return mList.get(position);
    }
}
