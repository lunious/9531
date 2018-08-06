package com.lubanjianye.biaoxuntong.ui.main.index;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;


/**
 * 项目名:   LBBXT
 * 包名:     com.lubanjianye.biaoxuntong.ui.main.index
 * 文件名:   IndexFragmentAdapter
 * 创建者:   lunious
 * 创建时间: 2017/12/16  9:37
 * 描述:     TODO
 */

public class IndexFragmentAdapter extends FragmentPagerAdapter {


    public IndexFragmentAdapter(FragmentManager fm) {
        super(fm);

    }

    @Override
    public int getCount() {
        return 1;
    }

    @Override
    public Fragment getItem(int position) {
        IndexListFragment columnFragment = new IndexListFragment();
        return columnFragment;
    }

}

