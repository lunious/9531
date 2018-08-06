package com.lubanjianye.biaoxuntong.ui.main.query.detail;

import android.content.Intent;

import com.lubanjianye.biaoxuntong.base.BaseActivity1;
import com.lubanjianye.biaoxuntong.base.BaseFragment1;


public class CompanySgyjListActivity extends BaseActivity1 {

    private String sfId = "";
    @Override
    public BaseFragment1 setRootFragment() {
        Intent intent = getIntent();
        if (intent != null) {
            sfId = intent.getStringExtra("sfId");
        }

        final CompanySgyjListFragment fragment = CompanySgyjListFragment.create(sfId);
        return fragment;
    }
}
