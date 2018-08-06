package com.lubanjianye.biaoxuntong.ui.main.user.about;

import android.os.Bundle;
import android.support.v7.widget.AppCompatTextView;
import android.view.View;
import android.widget.LinearLayout;
import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.lubanjianye.biaoxuntong.R;
import com.lubanjianye.biaoxuntong.base.BaseActivity;
import com.lubanjianye.biaoxuntong.util.appinfo.AppApplicationMgr;
import butterknife.BindView;
import butterknife.OnClick;


@Route(path = "/com/AboutActivity")
public class AboutActivity extends BaseActivity {

    @BindView(R.id.ll_iv_back)
    LinearLayout llIvBack;
    @BindView(R.id.main_bar_name)
    AppCompatTextView mainBarName;
    @BindView(R.id.message_num)
    AppCompatTextView messageNum;
    @BindView(R.id.ll_message)
    LinearLayout llMessage;
    @BindView(R.id.tv_version_name)
    AppCompatTextView tvVersionName;
    @BindView(R.id.tv_luban)
    AppCompatTextView tvLuban;
    @BindView(R.id.ll_tj)
    LinearLayout llTj;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_about;
    }

    @Override
    protected void initData(Bundle savedInstanceState) {
        llIvBack.setVisibility(View.VISIBLE);
        mainBarName.setText("关于我们");
        tvVersionName.setText(AppApplicationMgr.getVersionName(this));
    }

    @Override
    protected void initEvent(Bundle savedInstanceState) {

    }


    @OnClick({R.id.ll_iv_back, R.id.tv_luban, R.id.ll_tj})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.ll_iv_back:
                finish();
                break;
            case R.id.tv_luban:
                ARouter.getInstance().build("/com/BrowserActivity").withString("mUrl","http://www.lubanjianye.com/").withString("mTitle","鲁班建业通-招投标神器").navigation();
                break;
            case R.id.ll_tj:
                toShare(0, "我正在使用【鲁班标讯通】,推荐给你", "企业资质、人员资格、业绩、信用奖惩、经营风险、法律诉讼一键查询！", "http://api.lubanjianye.com/bxtajax/Entryajax/sharehtml?src=share");
                break;
        }
    }
}
