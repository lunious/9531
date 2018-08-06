package com.lubanjianye.biaoxuntong.ui.main.result.detail.chongqing;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatTextView;
import android.text.TextUtils;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.classic.common.MultipleStatusView;
import com.just.agentweb.AgentWebConfig;
import com.just.agentweb.AgentWebUtils;
import com.lubanjianye.biaoxuntong.R;
import com.lubanjianye.biaoxuntong.app.BiaoXunTongApi;
import com.lubanjianye.biaoxuntong.app.BiaoXunTong;
import com.lubanjianye.biaoxuntong.base.BaseFragment1;
import com.lubanjianye.biaoxuntong.database.DatabaseManager;
import com.lubanjianye.biaoxuntong.database.UserProfile;
import com.lubanjianye.biaoxuntong.eventbus.EventMessage;
import com.lubanjianye.biaoxuntong.ui.main.query.CompanySearchResultActivity;
import com.lubanjianye.biaoxuntong.ui.main.user.avater.AvaterActivity;
import com.lubanjianye.biaoxuntong.ui.sign.SignInActivity;
import com.lubanjianye.biaoxuntong.ui.main.index.detail.chongqing.IndexCqsggjyDetailActivity;
import com.lubanjianye.biaoxuntong.ui.share.OpenBuilder;
import com.lubanjianye.biaoxuntong.ui.share.OpenConstant;
import com.lubanjianye.biaoxuntong.ui.share.Share;
import com.lubanjianye.biaoxuntong.util.aes.AesUtil;
import com.lubanjianye.biaoxuntong.util.netStatus.AppSysMgr;
import com.lubanjianye.biaoxuntong.util.netStatus.NetUtil;
import com.lubanjianye.biaoxuntong.util.sp.AppSharePreferenceMgr;
import com.lubanjianye.biaoxuntong.util.toast.ToastUtil;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.UiError;

import org.greenrobot.eventbus.EventBus;

import java.util.List;


public class ResultCqsggjyzbjgDetailFragment extends BaseFragment1 implements View.OnClickListener, OpenBuilder.Callback {


    private LinearLayout llIvBack = null;
    private AppCompatTextView mainBarName = null;
    private LinearLayout llShare = null;
    private AppCompatTextView tvMainTitle = null;
    private AppCompatTextView tvDataTime = null;
    private AppCompatTextView tvOwerDiyi = null;
    private AppCompatTextView tvOwerDier = null;
    private AppCompatTextView tvOwerDisan = null;
    private ImageView ivFav = null;
    private LinearLayout llFav = null;
    private MultipleStatusView sggjyDetailStatusView = null;
    private AppCompatTextView tvOwerZhaobiaodaili = null;
    private WebView webView = null;


    private LinearLayout llWeiBoShare = null;
    private LinearLayout llQQBoShare = null;
    private LinearLayout llWeixinBoShare = null;
    private LinearLayout llPyqShare = null;


    private AppCompatTextView tvGg = null;

    private static final String ARG_ENTITYID = "ARG_ENTITYID";
    private static final String ARG_ENTITY = "ARG_ENTITY";
    private static final String ARG_AJAXTYPE = "ARG_AJAXTYPE";


    private int myFav = -1;
    private int mEntityId = -1;
    private String mEntity = "";

    private String ggEntity = "";
    private String ggEntityId = "";

    private String shareTitle = "";
    private String shareContent = "";
    private String shareUrl = "";

    private String deviceId = AppSysMgr.getPsuedoUniqueID();
    private String ajaxType = "0";

    private String mDiqu = "";

    @Override
    public Object setLayout() {
        return R.layout.fragment_result_cqsggjyzbjg_detail;
    }

    public static ResultCqsggjyzbjgDetailFragment create(@NonNull int entityId, String entity, String ajaxlogtype) {
        final Bundle args = new Bundle();
        args.putInt(ARG_ENTITYID, entityId);
        args.putString(ARG_ENTITY, entity);
        args.putString(ARG_AJAXTYPE, ajaxlogtype);
        final ResultCqsggjyzbjgDetailFragment fragment = new ResultCqsggjyzbjgDetailFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final Bundle args = getArguments();
        if (args != null) {
            mEntityId = args.getInt(ARG_ENTITYID);
            mEntity = args.getString(ARG_ENTITY);
            ajaxType = args.getString(ARG_AJAXTYPE);
        }
    }


    @Override
    public void initView() {
        llIvBack = getView().findViewById(R.id.ll_iv_back);
        mainBarName = getView().findViewById(R.id.main_bar_name);

        llShare = getView().findViewById(R.id.ll_share);
        tvMainTitle = getView().findViewById(R.id.tv_main_title);
        tvDataTime = getView().findViewById(R.id.tv_data_time);
        tvOwerDiyi = getView().findViewById(R.id.tv_ower_diyi);
        tvOwerDier = getView().findViewById(R.id.tv_ower_dier);
        tvOwerDisan = getView().findViewById(R.id.tv_ower_disan);
        ivFav = getView().findViewById(R.id.iv_fav);
        llFav = getView().findViewById(R.id.ll_fav);
        sggjyDetailStatusView = getView().findViewById(R.id.sggjy_detail_status_view);

        llWeiBoShare = getView().findViewById(R.id.ll_weibo_share);
        llQQBoShare = getView().findViewById(R.id.ll_qq_share);
        llWeixinBoShare = getView().findViewById(R.id.ll_chat_share);
        llPyqShare = getView().findViewById(R.id.ll_pyq_share);
        tvOwerZhaobiaodaili = getView().findViewById(R.id.tv_ower_zhaobiaodaili);
        webView = getView().findViewById(R.id.wv_content);


        llIvBack.setOnClickListener(this);
        llShare.setOnClickListener(this);
        llFav.setOnClickListener(this);

        tvGg = getView().findViewById(R.id.tv_data_gg);
        tvGg.setOnClickListener(this);

        tvOwerDiyi.setOnClickListener(this);
        tvOwerDier.setOnClickListener(this);
        tvOwerDisan.setOnClickListener(this);

        llWeiBoShare.setOnClickListener(this);
        llQQBoShare.setOnClickListener(this);
        llWeixinBoShare.setOnClickListener(this);
        llPyqShare.setOnClickListener(this);


        if (AppSharePreferenceMgr.contains(getContext(), EventMessage.LOCA_AREA)) {
            mDiqu = (String) AppSharePreferenceMgr.get(getContext(), EventMessage.LOCA_AREA, "");
        }


    }

    private void setWebView() {
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

    @Override
    public void initData() {
        llIvBack.setVisibility(View.VISIBLE);
        mainBarName.setText("工程招标中标公示详情");
        sggjyDetailStatusView.setOnRetryClickListener(mRetryClickListener);

        setWebView();

        webView.setWebViewClient(new WebViewClient() {

            @Override
            public void onPageFinished(WebView view, String url) {
                //加载完成
                sggjyDetailStatusView.showContent();
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

        });

    }

    //点击重试
    final View.OnClickListener mRetryClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            requestData();
        }
    };

    @Override
    public void initEvent() {
        requestData();

    }

    private long id = 0;

    private void requestData() {

        if ("1".equals(ajaxType)) {
            //改变已读未读状态
            EventBus.getDefault().post(new EventMessage(EventMessage.READ_STATUS));
        }

        if (!NetUtil.isNetworkConnected(getActivity())) {
            sggjyDetailStatusView.showNoNetwork();
        } else {
            sggjyDetailStatusView.showLoading();
            if (AppSharePreferenceMgr.contains(getContext(), EventMessage.LOGIN_SUCCSS)) {
                //已登陆时的数据请求

                List<UserProfile> users = DatabaseManager.getInstance().getDao().loadAll();

                for (int i = 0; i < users.size(); i++) {
                    id = users.get(0).getId();
                }

                OkGo.<String>post(BiaoXunTongApi.URL_GETRESULTLISTDETAIL)
                        .params("entityId", mEntityId)
                        .params("entity", mEntity)
                        .params("diqu", mDiqu)
                        .params("userid", id)
                        .params("deviceId", deviceId)
                        .params("ajaxlogtype", ajaxType)
                        .execute(new StringCallback() {
                            @Override
                            public void onSuccess(Response<String> response) {
                                String jiemi = AesUtil.aesDecrypt(response.body(), BiaoXunTongApi.PAS_KEY);


                                //判断是否收藏过
                                final JSONObject object = JSON.parseObject(jiemi);
                                String status = object.getString("status");
                                int favorite = object.getInteger("favorite");
                                if (favorite == 1) {
                                    myFav = 1;
                                    ivFav.setImageResource(R.mipmap.ic_faved_pressed);
                                } else if (favorite == 0) {
                                    myFav = 0;
                                    ivFav.setImageResource(R.mipmap.ic_fav_pressed);
                                }
                                if ("200".equals(status)) {
                                    final JSONObject data = object.getJSONObject("data");
                                    //判断有无相关公告
                                    final JSONObject arrayGg = data.getJSONObject("ggId");
                                    if (arrayGg != null) {
                                        ggEntity = arrayGg.getString("entity");
                                        ggEntityId = arrayGg.getString("entityId");
                                        if (TextUtils.isEmpty(ggEntity)) {
                                            tvGg.setVisibility(View.GONE);
                                        }
                                    }

                                    final String url = data.getString("url");
                                    shareUrl = url;


                                    String reportTitle = data.getString("reportTitle");
                                    shareTitle = reportTitle;
                                    if (!TextUtils.isEmpty(reportTitle)) {
                                        tvMainTitle.setText(reportTitle);
                                    } else {
                                        tvMainTitle.setText("暂无");
                                    }
                                    String sysTime = data.getString("sysTime");
                                    if (!TextUtils.isEmpty(sysTime)) {
                                        tvDataTime.setText(sysTime.substring(0, 10));
                                    } else {
                                        tvDataTime.setText("暂无");
                                    }

                                    String entityUrl = data.getString("entityUrl");
                                    if (!TextUtils.isEmpty(entityUrl)) {
                                        webView.loadUrl(entityUrl);
                                    } else {
                                    }

                                    String zbdlqy = data.getString("zbdlqy");
                                    if (!TextUtils.isEmpty(zbdlqy)) {
                                        tvOwerZhaobiaodaili.setText(zbdlqy);
                                    } else {
                                        tvOwerZhaobiaodaili.setText("暂无");
                                    }

                                    String oneCompany = data.getString("oneCompany");
                                    if (!TextUtils.isEmpty(oneCompany)) {
                                        tvOwerDiyi.setText(oneCompany);
                                    } else {
                                        tvOwerDiyi.setText("暂无");
                                    }
                                    String twoCompany = data.getString("twoCompany");
                                    if (!TextUtils.isEmpty(twoCompany)) {
                                        tvOwerDier.setText(twoCompany);
                                    } else {
                                        tvOwerDier.setText("暂无");
                                    }
                                    String threeCompany = data.getString("threeCompany");
                                    if (!TextUtils.isEmpty(threeCompany)) {
                                        tvOwerDisan.setText(threeCompany);
                                    } else {
                                        tvOwerDisan.setText("暂无");
                                    }

                                } else {
                                    sggjyDetailStatusView.showError();
                                }

                            }
                        });

            } else {
                //未登陆时的数据请求

                OkGo.<String>post(BiaoXunTongApi.URL_GETRESULTLISTDETAIL)
                        .params("entityId", mEntityId)
                        .params("entity", mEntity)
                        .params("diqu", mDiqu)
                        .params("deviceId", deviceId)
                        .params("ajaxlogtype", ajaxType)
                        .execute(new StringCallback() {
                            @Override
                            public void onSuccess(Response<String> response) {
                                String jiemi = AesUtil.aesDecrypt(response.body(), BiaoXunTongApi.PAS_KEY);

                                final JSONObject object = JSON.parseObject(jiemi);
                                String status = object.getString("status");
                                final JSONObject data = object.getJSONObject("data");

                                if ("200".equals(status)) {
                                    final String url = data.getString("url");

                                    //判断有无相关公告
                                    final JSONObject arrayGg = data.getJSONObject("ggId");
                                    if (arrayGg != null) {
                                        ggEntity = arrayGg.getString("entity");
                                        ggEntityId = arrayGg.getString("entityId");
                                        if (TextUtils.isEmpty(ggEntity)) {
                                            tvGg.setVisibility(View.GONE);
                                        }
                                    }

                                    shareUrl = url;

                                    String reportTitle = data.getString("reportTitle");
                                    shareTitle = reportTitle;
                                    if (!TextUtils.isEmpty(reportTitle)) {
                                        tvMainTitle.setText(reportTitle);
                                    } else {
                                        tvMainTitle.setText("暂无");
                                    }
                                    String sysTime = data.getString("sysTime");
                                    if (!TextUtils.isEmpty(sysTime)) {
                                        tvDataTime.setText(sysTime.substring(0, 10));
                                    } else {
                                        tvDataTime.setText("暂无");
                                    }

                                    String entityUrl = data.getString("entityUrl");
                                    if (!TextUtils.isEmpty(entityUrl)) {
                                        webView.loadUrl(entityUrl);
                                    } else {
                                    }
                                    String zbdlqy = data.getString("zbdlqy");
                                    if (!TextUtils.isEmpty(zbdlqy)) {
                                        tvOwerZhaobiaodaili.setText(zbdlqy);
                                    } else {
                                        tvOwerZhaobiaodaili.setText("暂无");
                                    }
                                    String oneCompany = data.getString("oneCompany");
                                    if (!TextUtils.isEmpty(oneCompany)) {
                                        tvOwerDiyi.setText(oneCompany);
                                    } else {
                                        tvOwerDiyi.setText("暂无");
                                    }
                                    String twoCompany = data.getString("twoCompany");
                                    if (!TextUtils.isEmpty(twoCompany)) {
                                        tvOwerDier.setText(twoCompany);
                                    } else {
                                        tvOwerDier.setText("暂无");
                                    }
                                    String threeCompany = data.getString("threeCompany");
                                    if (!TextUtils.isEmpty(threeCompany)) {
                                        tvOwerDisan.setText(threeCompany);
                                    } else {
                                        tvOwerDisan.setText("暂无");
                                    }

                                } else {
                                    sggjyDetailStatusView.showError();
                                }
                            }
                        });
            }
        }


    }

    private Share mShare = new Share();

    String provinceCode = "500000";
    private String mobile = "";

    @Override
    public void onClick(View view) {
        mShare.setAppName("鲁班标讯通");
        mShare.setAppShareIcon(R.mipmap.ic_share);
        if (mShare.getBitmapResID() == 0) {
            mShare.setBitmapResID(R.mipmap.ic_share);
        }
        mShare.setTitle(shareTitle);
        mShare.setContent(shareContent);
        mShare.setSummary(shareContent);
        mShare.setDescription(shareContent);
        mShare.setImageUrl(null);
        mShare.setUrl(BiaoXunTongApi.SHARE_URL + shareUrl);
        Intent intent = null;
        switch (view.getId()) {
            case R.id.tv_data_gg:
                if ("cqsggjy".equals(ggEntity)) {
                    intent = new Intent(BiaoXunTong.getApplicationContext(), IndexCqsggjyDetailActivity.class);
                    intent.putExtra("entityId", Integer.valueOf(ggEntityId));
                    intent.putExtra("entity", ggEntity);
                    intent.putExtra("ajaxlogtype", "0");
                    intent.putExtra("mId", "");
                    startActivity(intent);

                }
                break;
            case R.id.ll_weibo_share:
                OpenBuilder.with(getActivity())
                        .useWeibo(OpenConstant.WB_APP_KEY)
                        .share(mShare, new OpenBuilder.Callback() {
                            @Override
                            public void onFailed() {

                            }

                            @Override
                            public void onSuccess() {

                            }
                        });
                break;
            case R.id.ll_qq_share:
                OpenBuilder.with(getActivity())
                        .useTencent(OpenConstant.QQ_APP_ID)
                        .share(mShare, new IUiListener() {
                            @Override
                            public void onComplete(Object o) {
                                ToastUtil.shortToast(getContext(), "分享成功");
                            }

                            @Override
                            public void onError(UiError uiError) {
                                ToastUtil.shortToast(getContext(), "分享失败");
                            }

                            @Override
                            public void onCancel() {
                                ToastUtil.shortToast(getContext(), "分享取消");
                            }
                        }, this);
                break;
            case R.id.ll_chat_share:
                OpenBuilder.with(getActivity())
                        .useWechat(OpenConstant.WECHAT_APP_ID)
                        .shareSession(mShare, new OpenBuilder.Callback() {
                            @Override
                            public void onFailed() {

                            }

                            @Override
                            public void onSuccess() {

                            }
                        });
                break;
            case R.id.ll_pyq_share:
                OpenBuilder.with(getActivity())
                        .useWechat(OpenConstant.WECHAT_APP_ID)
                        .shareTimeLine(mShare, new OpenBuilder.Callback() {
                            @Override
                            public void onFailed() {

                            }

                            @Override
                            public void onSuccess() {

                            }
                        });
                break;
            case R.id.ll_iv_back:
                getActivity().finish();
                break;
            case R.id.ll_share:
                toShare(mEntityId, shareTitle, shareContent, BiaoXunTongApi.SHARE_URL + shareUrl);
                break;
            case R.id.ll_fav:
                if (AppSharePreferenceMgr.contains(getContext(), EventMessage.LOGIN_SUCCSS)) {
                    //已登录处理事件
                    List<UserProfile> users = DatabaseManager.getInstance().getDao().loadAll();
                    long id = 0;
                    for (int i = 0; i < users.size(); i++) {
                        id = users.get(0).getId();
                    }

                    if (myFav == 1) {

                        OkGo.<String>post(BiaoXunTongApi.URL_DELEFAV)
                                .params("entityid", mEntityId)
                                .params("entity", mEntity)
                                .params("diqu", mDiqu)
                                .params("userid", id)
                                .execute(new StringCallback() {
                                    @Override
                                    public void onSuccess(Response<String> response) {
                                        final JSONObject object = JSON.parseObject(response.body());
                                        String status = object.getString("status");
                                        if ("200".equals(status)) {
                                            myFav = 0;
                                            ivFav.setImageResource(R.mipmap.ic_fav_pressed);
                                            ToastUtil.shortToast(getContext(), "取消收藏");
                                            EventBus.getDefault().post(new EventMessage(EventMessage.CLICK_FAV));
                                        } else if ("500".equals(status)) {
                                            ToastUtil.shortToast(getContext(), "服务器异常");
                                        }
                                    }
                                });

                    } else if (myFav == 0) {

                        OkGo.<String>post(BiaoXunTongApi.URL_ADDFAV)
                                .params("entityid", mEntityId)
                                .params("entity", mEntity)
                                .params("diqu", mDiqu)
                                .params("userid", id)
                                .execute(new StringCallback() {
                                    @Override
                                    public void onSuccess(Response<String> response) {
                                        final JSONObject object = JSON.parseObject(response.body());
                                        String status = object.getString("status");
                                        if ("200".equals(status)) {
                                            myFav = 1;
                                            ivFav.setImageResource(R.mipmap.ic_faved_pressed);
                                            ToastUtil.shortToast(getContext(), "收藏成功");
                                            EventBus.getDefault().post(new EventMessage(EventMessage.CLICK_FAV));
                                        } else if ("500".equals(status)) {
                                            ToastUtil.shortToast(getContext(), "服务器异常");
                                        }
                                    }
                                });

                    }
                } else {
                    //未登录去登陆
                    startActivity(new Intent(getActivity(), SignInActivity.class));
                }
                break;
            case R.id.tv_ower_diyi:

                if (!"暂无".equals(tvOwerDiyi.getText().toString().trim())) {

                    if (AppSharePreferenceMgr.contains(getContext(), EventMessage.LOGIN_SUCCSS)) {
                        List<UserProfile> users = DatabaseManager.getInstance().getDao().loadAll();
                        for (int i = 0; i < users.size(); i++) {
                            id = users.get(0).getId();
                            mobile = users.get(0).getMobile();
                        }

                        if (!TextUtils.isEmpty(mobile)) {

                            final String name = tvOwerDiyi.getText().toString().trim();

                            OkGo.<String>post(BiaoXunTongApi.URL_GETSUITCOMPANY)
                                    .params("name", name)
                                    .params("userid", id)
                                    .execute(new StringCallback() {
                                        @Override
                                        public void onSuccess(Response<String> response) {
                                            final JSONArray data = JSON.parseObject(response.body()).getJSONArray("data");
                                            if (data.size() > 0) {
                                                //根据返回的id去查询公司名称
                                                Intent intent = new Intent(getActivity(), CompanySearchResultActivity.class);
                                                intent.putExtra("provinceCode", provinceCode);
                                                intent.putExtra("qyIds", data.toString());
                                                startActivity(intent);
                                            } else {

                                            }
                                        }
                                    });

                        } else {
                            ToastUtil.shortToast(getContext(), "请先绑定手机号");
                            startActivity(new Intent(getContext(), AvaterActivity.class));
                        }
                    } else {
                        //未登录去登陆
                        startActivity(new Intent(getContext(), SignInActivity.class));
                        ToastUtil.shortBottonToast(getContext(), "请先登录");
                    }
                } else {

                }
                break;
            case R.id.tv_ower_dier:

                if (!"暂无".equals(tvOwerDier.getText().toString().trim())) {

                    if (AppSharePreferenceMgr.contains(getContext(), EventMessage.LOGIN_SUCCSS)) {
                        List<UserProfile> users = DatabaseManager.getInstance().getDao().loadAll();
                        for (int i = 0; i < users.size(); i++) {
                            id = users.get(0).getId();
                            mobile = users.get(0).getMobile();
                        }

                        if (!TextUtils.isEmpty(mobile)) {

                            final String name = tvOwerDier.getText().toString().trim();

                            OkGo.<String>post(BiaoXunTongApi.URL_GETSUITCOMPANY)
                                    .params("name", name)
                                    .params("userid", id)
                                    .execute(new StringCallback() {
                                        @Override
                                        public void onSuccess(Response<String> response) {
                                            final JSONArray data = JSON.parseObject(response.body()).getJSONArray("data");
                                            if (data.size() > 0) {
                                                //根据返回的id去查询公司名称
                                                Intent intent = new Intent(getActivity(), CompanySearchResultActivity.class);
                                                intent.putExtra("provinceCode", provinceCode);
                                                intent.putExtra("qyIds", data.toString());
                                                startActivity(intent);
                                            } else {

                                            }
                                        }
                                    });

                        } else {
                            ToastUtil.shortToast(getContext(), "请先绑定手机号");
                            startActivity(new Intent(getContext(), AvaterActivity.class));
                        }
                    } else {
                        //未登录去登陆
                        startActivity(new Intent(getContext(), SignInActivity.class));
                        ToastUtil.shortBottonToast(getContext(), "请先登录");
                    }
                } else {

                }
                break;
            case R.id.tv_ower_disan:

                if (!"暂无".equals(tvOwerDisan.getText().toString().trim())) {

                    if (AppSharePreferenceMgr.contains(getContext(), EventMessage.LOGIN_SUCCSS)) {
                        List<UserProfile> users = DatabaseManager.getInstance().getDao().loadAll();
                        for (int i = 0; i < users.size(); i++) {
                            id = users.get(0).getId();
                            mobile = users.get(0).getMobile();
                        }

                        if (!TextUtils.isEmpty(mobile)) {

                            final String name = tvOwerDisan.getText().toString().trim();

                            OkGo.<String>post(BiaoXunTongApi.URL_GETSUITCOMPANY)
                                    .params("name", name)
                                    .params("userid", id)
                                    .execute(new StringCallback() {
                                        @Override
                                        public void onSuccess(Response<String> response) {
                                            final JSONArray data = JSON.parseObject(response.body()).getJSONArray("data");
                                            if (data.size() > 0) {
                                                //根据返回的id去查询公司名称
                                                Intent intent = new Intent(getActivity(), CompanySearchResultActivity.class);
                                                intent.putExtra("provinceCode", provinceCode);
                                                intent.putExtra("qyIds", data.toString());
                                                startActivity(intent);
                                            } else {

                                            }
                                        }
                                    });

                        } else {
                            ToastUtil.shortToast(getContext(), "请先绑定手机号");
                            startActivity(new Intent(getContext(), AvaterActivity.class));
                        }
                    } else {
                        //未登录去登陆
                        startActivity(new Intent(getContext(), SignInActivity.class));
                        ToastUtil.shortBottonToast(getContext(), "请先登录");
                    }
                } else {

                }
                break;
            default:
                break;
        }
    }

    @Override
    public void onFailed() {

    }

    @Override
    public void onSuccess() {

    }
}
