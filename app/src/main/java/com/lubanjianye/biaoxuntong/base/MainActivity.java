package com.lubanjianye.biaoxuntong.base;


import android.os.Bundle;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.lubanjianye.biaoxuntong.R;

@Route(path = "/com/MainActivity")
public class MainActivity extends BaseActivity {


    @Override
    protected int getLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    protected void initData(Bundle savedInstanceState) {
        //首页不可滑动推出
        setSwipeBackEnable(false);
        if (savedInstanceState == null) {
            loadRootFragment(R.id.fragment_container, new MainFragment());
        }
    }

    @Override
    protected void initEvent(Bundle savedInstanceState) {

    }
}
