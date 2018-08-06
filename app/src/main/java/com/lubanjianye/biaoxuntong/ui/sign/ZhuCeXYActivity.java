package com.lubanjianye.biaoxuntong.ui.sign;
import android.os.Bundle;
import android.support.v7.widget.AppCompatTextView;
import android.view.View;
import android.widget.LinearLayout;
import com.alibaba.android.arouter.facade.annotation.Route;
import com.lubanjianye.biaoxuntong.R;
import com.lubanjianye.biaoxuntong.base.BaseActivity;
import butterknife.BindView;
import butterknife.OnClick;

@Route(path = "/com/ZhuCeXYActivity")
public class ZhuCeXYActivity extends BaseActivity {

    @BindView(R.id.ll_iv_back)
    LinearLayout llIvBack;
    @BindView(R.id.main_bar_name)
    AppCompatTextView mainBarName;
    @BindView(R.id.message_num)
    AppCompatTextView messageNum;
    @BindView(R.id.ll_message)
    LinearLayout llMessage;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_zhucexieyi;
    }

    @Override
    protected void initData(Bundle savedInstanceState) {
        llIvBack.setVisibility(View.VISIBLE);
        mainBarName.setText("服务协议");
    }

    @Override
    protected void initEvent(Bundle savedInstanceState) {

    }


    @OnClick(R.id.ll_iv_back)
    public void onViewClicked() {
        finish();
    }
}
