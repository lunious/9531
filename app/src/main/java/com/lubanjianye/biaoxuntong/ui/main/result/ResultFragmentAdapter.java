package com.lubanjianye.biaoxuntong.ui.main.result;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * 项目名:   AppLunious
 * 包名:     com.lubanjianye.biaoxuntong.ui.main.fragment.result
 * 文件名:   ResultFragmentAdapter
 * 创建者:   lunious
 * 创建时间: 2017/12/12  0:24
 * 描述:     TODO
 */

public class ResultFragmentAdapter extends FragmentPagerAdapter {

    private List<String> mList = new ArrayList<>();
    private ArrayList<Fragment> mFragment = new ArrayList<>();

    public ResultFragmentAdapter(List<String> list, FragmentManager fm) {
        super(fm);
        this.mList = list;

        for (int i = 0; i < mList.size(); i++) {
            mFragment.add(ResultListFragment.getInstance(mList.get(i)));
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
