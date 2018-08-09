package com.lubanjianye.biaoxuntong.ui.main.index.jggg;

import android.os.Bundle;
import android.support.v7.widget.AppCompatTextView;
import android.view.View;
import android.widget.LinearLayout;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.lubanjianye.biaoxuntong.R;
import com.lubanjianye.biaoxuntong.base.BaseActivity;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Author: lunious
 * Date: 2018/8/9 16:31
 * Description:
 */
@Route(path = "/com/JgggActivity")
public class JgggActivity extends BaseActivity {
    @BindView(R.id.ll_iv_back)
    LinearLayout llIvBack;
    @BindView(R.id.main_bar_name)
    AppCompatTextView mainBarName;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_zbgg;
    }

    @Override
    protected void initData(Bundle savedInstanceState) {
        llIvBack.setVisibility(View.VISIBLE);
        mainBarName.setText("结果公告");
    }

    @Override
    protected void initEvent(Bundle savedInstanceState) {

    }


    @OnClick(R.id.ll_iv_back)
    public void onViewClicked() {
        finish();
    }
}
