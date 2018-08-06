package com.lubanjianye.biaoxuntong.ui.search.activity;

import android.content.Intent;

import com.lubanjianye.biaoxuntong.base.BaseActivity1;
import com.lubanjianye.biaoxuntong.base.BaseFragment1;
import com.lubanjianye.biaoxuntong.ui.search.fragment.SearchFragment;

public class SearchActivity extends BaseActivity1 {

    private int searchTye = -1;

    @Override
    public BaseFragment1 setRootFragment() {

        Intent intent = getIntent();
        if (intent != null) {
            searchTye = intent.getIntExtra("searchTye", -1);
        }

        final SearchFragment fragment = SearchFragment.create(searchTye);
        return fragment;
    }
}
