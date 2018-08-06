package com.lubanjianye.biaoxuntong.ui.browser;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.AppCompatTextView;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.alibaba.android.arouter.facade.annotation.Autowired;
import com.alibaba.android.arouter.facade.annotation.Route;
import com.just.agentweb.AgentWeb;
import com.lubanjianye.biaoxuntong.R;
import com.lubanjianye.biaoxuntong.base.BaseActivity;
import com.lubanjianye.biaoxuntong.util.toast.ToastUtil;

import butterknife.BindView;

import butterknife.OnClick;

/**
 * Created by 11645 on 2018/3/22.
 */

@Route(path = "/com/BrowserActivity")
public class BrowserActivity extends BaseActivity {


    @Autowired
    String mUrl;
    @Autowired
    String mTitle;

    @BindView(R.id.ll_iv_back)
    LinearLayout llIvBack;
    @BindView(R.id.main_bar_name)
    AppCompatTextView mainBarName;
    @BindView(R.id.message_num)
    AppCompatTextView messageNum;
    @BindView(R.id.ll_message)
    LinearLayout llMessage;
    @BindView(R.id.ll_webview)
    LinearLayout llWebview;
    @BindView(R.id.browser_back)
    ImageView browserBack;
    @BindView(R.id.browser_forward)
    ImageView browserForward;
    @BindView(R.id.browser_refresh)
    ImageView browserRefresh;
    @BindView(R.id.browser_system_browser)
    ImageView browserSystemBrowser;
    @BindView(R.id.browser_bottom)
    LinearLayout browserBottom;
    private AgentWeb webView = null;



    @Override
    protected int getLayoutId() {
        return R.layout.fragment_browser;
    }

    @Override
    protected void initData(Bundle savedInstanceState) {
        llIvBack.setVisibility(View.VISIBLE);
        if (!TextUtils.isEmpty(mTitle)) {
            mainBarName.setText(mTitle);
        } else {
            mainBarName.setText("鲁班标讯通");
        }

    }

    @Override
    protected void initEvent(Bundle savedInstanceState) {
        webView = AgentWeb.with(this)
                .setAgentWebParent(llWebview, new LinearLayout.LayoutParams(-1, -1))
                .useDefaultIndicator(getResources().getColor(R.color.main_status_red),3)
                .createAgentWeb()
                .ready()
                .go(mUrl);
    }


    @OnClick({R.id.ll_iv_back, R.id.browser_back, R.id.browser_forward, R.id.browser_refresh, R.id.browser_system_browser})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.ll_iv_back:
                finish();
                break;
            case R.id.browser_back:
                webView.back();
                break;
            case R.id.browser_forward:
                //
                break;
            case R.id.browser_refresh:
                webView.getUrlLoader().reload();
                break;
            case R.id.browser_system_browser:
                try {
                    // 启用外部浏览器
                    Uri uri = Uri.parse(mUrl);
                    Intent it = new Intent(Intent.ACTION_VIEW, uri);
                    startActivity(it);
                } catch (Exception e) {
                    ToastUtil.shortToast(this, "网页地址错误");
                }
                break;
        }
    }
}
