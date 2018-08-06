package com.lubanjianye.biaoxuntong.ui.message;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 11645 on 2018/3/21.
 */

public class MessageAdapter extends FragmentPagerAdapter {

    private List<String> mList = new ArrayList<>();
    private ArrayList<Fragment> mFragment = new ArrayList<>();


    public MessageAdapter(List<String> list, FragmentManager fm) {
        super(fm);
        this.mList = list;

        for (int i = 0; i < mList.size(); i++) {
            mFragment.add(MessageListFragment.getInstance(mList.get(i)));
        }

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
