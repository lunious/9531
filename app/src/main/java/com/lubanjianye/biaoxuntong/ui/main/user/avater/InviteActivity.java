package com.lubanjianye.biaoxuntong.ui.main.user.avater;

import android.os.Bundle;
import android.support.v7.widget.AppCompatTextView;
import android.view.View;
import android.widget.LinearLayout;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.lubanjianye.biaoxuntong.R;
import com.lubanjianye.biaoxuntong.base.BaseActivity;
import com.lubanjianye.biaoxuntong.util.toast.ToastUtil;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Author: lunious
 * Date: 2018/6/25 16:34
 * Description:
 */
@Route(path = "/com/InviteActivity")
public class InviteActivity extends BaseActivity {

    @BindView(R.id.ll_back)
    LinearLayout llBack;
    @BindView(R.id.ll_wenhao)
    LinearLayout llWenhao;
    @BindView(R.id.btn_wyqdyh)
    AppCompatTextView btnWyqdyh;
    @BindView(R.id.btn_msyqhy)
    AppCompatTextView btnMsyqhy;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_invite;
    }

    @Override
    protected void initData(Bundle savedInstanceState) {

    }

    @Override
    protected void initEvent(Bundle savedInstanceState) {

    }


    @OnClick({R.id.ll_back, R.id.ll_wenhao})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.ll_back:
                finish();
                break;
            case R.id.ll_wenhao:
                ToastUtil.shortBottonToast(this, "说明");
                break;
        }
    }
}
