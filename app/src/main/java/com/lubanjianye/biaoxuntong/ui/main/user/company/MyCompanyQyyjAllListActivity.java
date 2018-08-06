package com.lubanjianye.biaoxuntong.ui.main.user.company;

import android.content.Intent;

import com.lubanjianye.biaoxuntong.base.BaseActivity1;
import com.lubanjianye.biaoxuntong.base.BaseFragment1;


/**
 * 项目名:   9527
 * 包名:     com.lubanjianye.biaoxuntong.ui.main.user.company
 * 文件名:   MyCompanyQyyjAllListActivity
 * 创建者:   lunious
 * 创建时间: 2017/12/26  20:52
 * 描述:     TODO
 */

public class MyCompanyQyyjAllListActivity extends BaseActivity1 {
    private String sfId = "";
    @Override
    public BaseFragment1 setRootFragment() {
        Intent intent = getIntent();
        if (intent != null) {
            sfId = intent.getStringExtra("sfId");
        }

        final MyCompanyQyyjAllListFragment fragment = MyCompanyQyyjAllListFragment.create(sfId);
        return fragment;

    }
}
