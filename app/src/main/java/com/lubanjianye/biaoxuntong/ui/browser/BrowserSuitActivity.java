package com.lubanjianye.biaoxuntong.ui.browser;


import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.widget.AppCompatTextView;
import android.text.TextUtils;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.alibaba.android.arouter.facade.annotation.Autowired;
import com.alibaba.android.arouter.facade.annotation.Route;
import com.just.agentweb.AgentWeb;
import com.just.agentweb.AgentWebConfig;
import com.just.agentweb.AgentWebUtils;
import com.just.agentweb.IAgentWebSettings;
import com.lubanjianye.biaoxuntong.R;
import com.lubanjianye.biaoxuntong.base.BaseActivity;
import com.lubanjianye.biaoxuntong.util.toast.ToastUtil;

import butterknife.BindView;
import butterknife.OnClick;


/**
 * Created by 11645 on 2018/3/22.
 */

@Route(path = "/com/BrowserSuitActivity")
public class BrowserSuitActivity extends BaseActivity {
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
    private android.webkit.WebSettings mWebSettings;



    @Autowired
    String mUrl;
    @Autowired
    String mTitle;



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
                .useDefaultIndicator(getResources().getColor(R.color.main_status_red), 3)
                .setAgentWebWebSettings(iAgentWebSettings)
                .createAgentWeb()
                .ready()
                .go(mUrl);
    }

    private IAgentWebSettings iAgentWebSettings = new IAgentWebSettings() {
        @Override
        public IAgentWebSettings toSetting(WebView webView) {
            settings(webView);
            return this;
        }

        @Override
        public WebSettings getWebSettings() {
            return mWebSettings;
        }
    };

    private void settings(WebView webView) {

        mWebSettings = webView.getSettings();
        mWebSettings.setJavaScriptEnabled(true);
        mWebSettings.setSupportZoom(true);
        mWebSettings.setBuiltInZoomControls(true);
        mWebSettings.setDisplayZoomControls(false);
        mWebSettings.setSavePassword(false);
        if (AgentWebUtils.checkNetwork(webView.getContext())) {
            //根据cache-control获取数据。
            mWebSettings.setCacheMode(android.webkit.WebSettings.LOAD_DEFAULT);
        } else {
            //没网，则从本地获取，即离线加载
            mWebSettings.setCacheMode(android.webkit.WebSettings.LOAD_CACHE_ELSE_NETWORK);
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            //适配5.0不允许http和https混合使用情况
            mWebSettings.setMixedContentMode(android.webkit.WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
            webView.setLayerType(View.LAYER_TYPE_HARDWARE, null);
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            webView.setLayerType(View.LAYER_TYPE_HARDWARE, null);
        } else if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) {
            webView.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
        }

        mWebSettings.setTextZoom(100);
        mWebSettings.setDatabaseEnabled(true);
        mWebSettings.setAppCacheEnabled(true);
        mWebSettings.setLoadsImagesAutomatically(true);
        mWebSettings.setSupportMultipleWindows(false);
        mWebSettings.setBlockNetworkImage(false);//是否阻塞加载网络图片  协议http or https
        mWebSettings.setAllowFileAccess(true); //允许加载本地文件html  file协议
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            mWebSettings.setAllowFileAccessFromFileURLs(false); //通过 file url 加载的 Javascript 读取其他的本地文件 .建议关闭
            mWebSettings.setAllowUniversalAccessFromFileURLs(false);//允许通过 file url 加载的 Javascript 可以访问其他的源，包括其他的文件和 http，https 等其他的源
        }
        mWebSettings.setJavaScriptCanOpenWindowsAutomatically(true);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {

            mWebSettings.setLayoutAlgorithm(android.webkit.WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        } else {
            mWebSettings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NORMAL);
        }
        mWebSettings.setLoadWithOverviewMode(true);
        mWebSettings.setUseWideViewPort(true);
        mWebSettings.setDomStorageEnabled(true);
        mWebSettings.setNeedInitialFocus(true);
        mWebSettings.setDefaultTextEncodingName("utf-8");//设置编码格式
        mWebSettings.setDefaultFontSize(16);
        mWebSettings.setMinimumFontSize(12);//设置 WebView 支持的最小字体大小，默认为 8
        mWebSettings.setGeolocationEnabled(true);
        //
        String dir = AgentWebConfig.getCachePath(webView.getContext());

        //设置数据库路径  api19 已经废弃,这里只针对 webkit 起作用
        mWebSettings.setGeolocationDatabasePath(dir);
        mWebSettings.setDatabasePath(dir);
        mWebSettings.setAppCachePath(dir);

        //缓存文件最大值
        mWebSettings.setAppCacheMaxSize(Long.MAX_VALUE);


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
