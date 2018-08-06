package com.lubanjianye.biaoxuntong.ui.main.user.avater;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.widget.AppCompatTextView;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.just.agentweb.AgentWebConfig;
import com.just.agentweb.AgentWebUtils;
import com.lubanjianye.biaoxuntong.R;
import com.lubanjianye.biaoxuntong.base.BaseActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Author: lunious
 * Date: 2018/6/25 14:57
 * Description:
 */
@Route(path = "/com/AccountActivity")
public class AccountActivity extends BaseActivity {


    @BindView(R.id.ll_iv_back)
    LinearLayout llIvBack;
    @BindView(R.id.main_bar_name)
    AppCompatTextView mainBarName;
    @BindView(R.id.message_num)
    AppCompatTextView messageNum;
    @BindView(R.id.ll_message)
    LinearLayout llMessage;
    @BindView(R.id.wv_huodong)
    WebView webView;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_account;
    }

    @Override
    protected void initData(Bundle savedInstanceState) {
        llIvBack.setVisibility(View.VISIBLE);
        mainBarName.setText("我的账户");
    }

    @Override
    protected void initEvent(Bundle savedInstanceState) {
        initWebView();
        webView.loadUrl("http://www.lubanjianye.com/");
    }


    @OnClick(R.id.ll_iv_back)
    public void onViewClicked() {
        finish();
    }

    private void initWebView() {
        WebSettings mWebSettings = webView.getSettings();
        mWebSettings = webView.getSettings();
        mWebSettings.setJavaScriptEnabled(true);
        mWebSettings.setSupportZoom(true);
        mWebSettings.setBuiltInZoomControls(false);
        mWebSettings.setSavePassword(false);
        if (AgentWebUtils.checkNetwork(webView.getContext())) {
            //根据cache-control获取数据。
            mWebSettings.setCacheMode(android.webkit.WebSettings.LOAD_DEFAULT);
        } else {
            //没网，则从本地获取，即离线加载
            mWebSettings.setCacheMode(android.webkit.WebSettings.LOAD_CACHE_ELSE_NETWORK);
        }

//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//            //适配5.0不允许http和https混合使用情况
//            mWebSettings.setMixedContentMode(android.webkit.WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
//            webView.setLayerType(View.LAYER_TYPE_HARDWARE, null);
//        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
//            webView.setLayerType(View.LAYER_TYPE_HARDWARE, null);
//        } else if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) {
//            webView.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
//        }

        webView.setLayerType(View.LAYER_TYPE_NONE, null);

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

        webView.setWebViewClient(new MyWebViewClient());


    }


    private class MyWebViewClient extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            if (url == null) {
                return false;
            }
            try {
                if (url.startsWith("weixin://") //微信
                        || url.startsWith("mailto://") //邮件
                        || url.startsWith("tel://")//电话
                        || url.startsWith("tel:")//电话
                        //其他自定义的scheme
                        || url.startsWith("mqqwpa://")) {
                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                    startActivity(intent);
                    return true;
                }
            } catch (Exception e) { //防止crash (如果手机上没有安装处理某个scheme开头的url的APP, 会导致crash)
                return true;//没有安装该app时，返回true，表示拦截自定义链接，但不跳转，避免弹出上面的错误页面
            }

            //处理http和https开头的url
            if (url.startsWith("http://") || url.startsWith("https://")) {
//                webView.loadUrl(url);
                return false;
            }
            return true;

        }

        @Override
        public void onPageFinished(WebView view, String url) {
            //加载完成

        }

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            //加载开始
        }

        @Override
        public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
            super.onReceivedError(view, errorCode, description, failingUrl);
            //加载失败
        }

    }
}
