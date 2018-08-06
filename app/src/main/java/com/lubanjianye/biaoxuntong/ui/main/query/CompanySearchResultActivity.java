package com.lubanjianye.biaoxuntong.ui.main.query;

import android.content.Intent;

import com.lubanjianye.biaoxuntong.base.BaseActivity1;
import com.lubanjianye.biaoxuntong.base.BaseFragment1;


public class CompanySearchResultActivity extends BaseActivity1 {

    private String provinceCode = "";
    private String qyIds = "";
    private String showSign = "";

    @Override
    public BaseFragment1 setRootFragment() {

        Intent intent = getIntent();
        if (intent != null) {
            provinceCode = intent.getStringExtra("provinceCode");
            qyIds = intent.getStringExtra("qyIds");
            showSign = intent.getStringExtra("showSign");
        }

        final CompanySearchResultFragment fragment = CompanySearchResultFragment.create(provinceCode, qyIds, showSign);
        return fragment;

    }
}
