package com.lubanjianye.biaoxuntong.ui.main.query.detail;

import android.content.Intent;

import com.lubanjianye.biaoxuntong.base.BaseActivity1;
import com.lubanjianye.biaoxuntong.base.BaseFragment1;



public class CompanyDetailActivity extends BaseActivity1 {

    private String sfId = "";
    private String lxr = "";


    @Override
    public BaseFragment1 setRootFragment() {

        Intent intent = getIntent();
        if (intent != null) {
            sfId = intent.getStringExtra("sfId");
            lxr = intent.getStringExtra("lxr");
        }

        final CompanyDetailFragment fragment = CompanyDetailFragment.create(sfId,lxr);
        return fragment;

    }
}
