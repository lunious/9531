package com.lubanjianye.biaoxuntong.ui.main.index.detail.sichuan;

import android.content.Intent;

import com.lubanjianye.biaoxuntong.base.BaseActivity1;
import com.lubanjianye.biaoxuntong.base.BaseFragment1;

/**
 * 项目名:   LBBXT
 * 包名:     com.lubanjianye.biaoxuntong.ui.main.index.detail
 * 文件名:   IndexHyzxDetailActivity
 * 创建者:   lunious
 * 创建时间: 2017/12/16  10:34
 * 描述:     TODO
 */

public class IndexHyzxDetailActivity extends BaseActivity1 {


    private String mTitle = "";
    private String mTime = "";
    private String mContent = "";

    @Override
    public BaseFragment1 setRootFragment() {
        Intent intent = getIntent();
        if (intent != null) {
            mTitle = intent.getStringExtra("title");
            mTime = intent.getStringExtra("createTime");
            mContent = intent.getStringExtra("mobile_context");

        }

        final IndexHyzxDetailFragment fragment = IndexHyzxDetailFragment.create(mTitle, mTime, mContent);
        return fragment;
    }
}
