package com.lubanjianye.biaoxuntong.ui.main.index.detail.chongqing;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatTextView;
import android.text.Html;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.alibaba.android.arouter.launcher.ARouter;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.classic.common.MultipleStatusView;
import com.lubanjianye.biaoxuntong.R;
import com.lubanjianye.biaoxuntong.app.BiaoXunTongApi;
import com.lubanjianye.biaoxuntong.app.BiaoXunTong;
import com.lubanjianye.biaoxuntong.base.BaseFragment1;
import com.lubanjianye.biaoxuntong.database.DatabaseManager;
import com.lubanjianye.biaoxuntong.database.UserProfile;
import com.lubanjianye.biaoxuntong.eventbus.EventMessage;
import com.lubanjianye.biaoxuntong.ui.main.result.detail.chongqing.ResultCqsggjyzbjgDetailActivity;
import com.lubanjianye.biaoxuntong.ui.sign.SignInActivity;
import com.lubanjianye.biaoxuntong.ui.share.OpenBuilder;
import com.lubanjianye.biaoxuntong.ui.share.OpenConstant;
import com.lubanjianye.biaoxuntong.ui.share.Share;
import com.lubanjianye.biaoxuntong.util.aes.AesUtil;
import com.lubanjianye.biaoxuntong.util.dialog.PromptDialog;
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


public class IndexCqsggjyDetailFragment extends BaseFragment1 implements View.OnClickListener, OpenBuilder.Callback {
    private LinearLayout llIvBack = null;
    private AppCompatTextView mainBarName = null;
    private MultipleStatusView indexSggjyDetailStatusView = null;
    private AppCompatTextView tvMainTitle = null;
    private AppCompatTextView tvMainArea = null;
    private AppCompatTextView tvPubTime = null;
    private AppCompatTextView tvDeadTime = null;
    private AppCompatTextView tvFbt = null;
    private AppCompatTextView tvYw = null;
    private AppCompatTextView tv1 = null;
    private AppCompatTextView tv2 = null;
    private AppCompatTextView tv3 = null;
    private AppCompatTextView tv4 = null;
    private AppCompatTextView tv5 = null;
    private AppCompatTextView tv6 = null;
    private AppCompatTextView tv7 = null;
    private ImageView ivFav = null;
    private LinearLayout llFav = null;
    private LinearLayout llShare = null;

    private LinearLayout llWeiBoShare = null;
    private LinearLayout llQQBoShare = null;
    private LinearLayout llWeixinBoShare = null;
    private LinearLayout llPyqShare = null;

    private AppCompatTextView tvGz = null;
    private AppCompatTextView tvJg = null;


    private static final String ARG_ENTITYID = "ARG_ENTITYID";
    private static final String ARG_ENTITY = "ARG_ENTITY";
    private static final String ARG_AJAXTYPE = "ARG_AJAXTYPE";


    private int myFav = -1;
    private int mEntityId = -1;
    private String mEntity = "";

    private String shareTitle = "";
    private String shareContent = "";
    private String shareUrl = "";

    private String deviceId = AppSysMgr.getPsuedoUniqueID();
    private String ajaxlogtype = "";

    private String zbjgId = "";

    private String mDiqu = "";


    public static IndexCqsggjyDetailFragment create(@NonNull int entityId, String entity, String ajaxlogtype) {
        final Bundle args = new Bundle();
        args.putInt(ARG_ENTITYID, entityId);
        args.putString(ARG_ENTITY, entity);
        args.putString(ARG_AJAXTYPE, ajaxlogtype);
        final IndexCqsggjyDetailFragment fragment = new IndexCqsggjyDetailFragment();
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
            ajaxlogtype = args.getString(ARG_AJAXTYPE);
        }
    }

    @Override
    public Object setLayout() {
        return R.layout.fragment_index_cqsggjy_detail;
    }

    @Override
    public void initView() {

        //创建对象
        promptDialog = new PromptDialog(getActivity());
        //设置自定义属性
        promptDialog.getDefaultBuilder().touchAble(true).round(3).loadingDuration(3000);

        llIvBack = getView().findViewById(R.id.ll_iv_back);
        mainBarName = getView().findViewById(R.id.main_bar_name);
        indexSggjyDetailStatusView = getView().findViewById(R.id.index_sggjy_detail_status_view);
        tvMainTitle = getView().findViewById(R.id.tv_main_title);
        tvMainArea = getView().findViewById(R.id.tv_main_area);
        tvPubTime = getView().findViewById(R.id.tv_pub_time);
        tvDeadTime = getView().findViewById(R.id.tv_dead_time);
        tvFbt = getView().findViewById(R.id.tv_fbt);
        tvYw = getView().findViewById(R.id.tv_yw);
        tv1 = getView().findViewById(R.id.tv1);
        tv2 = getView().findViewById(R.id.tv2);
        tv3 = getView().findViewById(R.id.tv3);
        tv4 = getView().findViewById(R.id.tv4);
        tv5 = getView().findViewById(R.id.tv5);
        tv6 = getView().findViewById(R.id.tv6);
        tv7 = getView().findViewById(R.id.tv7);
        ivFav = getView().findViewById(R.id.iv_fav);
        llFav = getView().findViewById(R.id.ll_fav);
        llShare = getView().findViewById(R.id.ll_share);
        llWeiBoShare = getView().findViewById(R.id.ll_weibo_share);
        llQQBoShare = getView().findViewById(R.id.ll_qq_share);
        llWeixinBoShare = getView().findViewById(R.id.ll_chat_share);
        llPyqShare = getView().findViewById(R.id.ll_pyq_share);

        llIvBack.setOnClickListener(this);
        llShare.setOnClickListener(this);
        llFav.setOnClickListener(this);
        llWeiBoShare.setOnClickListener(this);
        llQQBoShare.setOnClickListener(this);
        llWeixinBoShare.setOnClickListener(this);
        llPyqShare.setOnClickListener(this);
        tvYw.setOnClickListener(this);

        tvGz = getView().findViewById(R.id.tv_gzgg);
        tvJg = getView().findViewById(R.id.tv_jggg);
        tvGz.setOnClickListener(this);
        tvJg.setOnClickListener(this);


        if (AppSharePreferenceMgr.contains(getContext(), EventMessage.LOCA_AREA)) {
            mDiqu = (String) AppSharePreferenceMgr.get(getContext(), EventMessage.LOCA_AREA, "");
        }


    }

    @Override
    public void initData() {
        llIvBack.setVisibility(View.VISIBLE);
        mainBarName.setText("标讯详情");
        indexSggjyDetailStatusView.setOnRetryClickListener(mRetryClickListener);
    }

    @Override
    public void initEvent() {
        requestData();
    }


    //点击重试
    final View.OnClickListener mRetryClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            requestData();
        }
    };


    private long id = 0;

    private String gzUrl = "";
    private String jgEntity = "";
    private String jgEntityId = "";

    private void requestData() {


        if ("1".equals(ajaxlogtype)) {
            //改变已读未读状态
            EventBus.getDefault().post(new EventMessage(EventMessage.READ_STATUS));
        }


        if (!NetUtil.isNetworkConnected(getActivity())) {
            indexSggjyDetailStatusView.showNoNetwork();
        } else {
            indexSggjyDetailStatusView.showLoading();


            if (AppSharePreferenceMgr.contains(getContext(), EventMessage.LOGIN_SUCCSS)) {
                List<UserProfile> users = DatabaseManager.getInstance().getDao().loadAll();

                for (int i = 0; i < users.size(); i++) {
                    id = users.get(0).getId();
                }

                OkGo.<String>post(BiaoXunTongApi.URL_GETCOLLECTIONLISTDETAIL)
                        .params("entityId", mEntityId)
                        .params("entity", mEntity)
                        .params("diqu", mDiqu)
                        .params("userid", id)
                        .params("deviceId", deviceId)
                        .params("ajaxlogtype", ajaxlogtype)
                        .execute(new StringCallback() {
                            @Override
                            public void onSuccess(Response<String> response) {
                                String jiemi = AesUtil.aesDecrypt(response.body(), BiaoXunTongApi.PAS_KEY);

                                //判断是否收藏过
                                final JSONObject object = JSON.parseObject(jiemi);
                                String status = object.getString("status");
                                int favorite = object.getInteger("favorite");

                                if ("200".equals(status)) {
                                    if (favorite == 1) {
                                        myFav = 1;
                                        ivFav.setImageResource(R.mipmap.ic_faved_pressed);
                                    } else if (favorite == 0) {
                                        myFav = 0;
                                        ivFav.setImageResource(R.mipmap.ic_fav_pressed);
                                    }
                                    final JSONObject data = object.getJSONObject("data");

                                    //判断是否有更正和结果
                                    //1、判断有误更正
                                    final JSONArray arrayGz = data.getJSONArray("listGzUrl");
                                    //2、判断有无结果
                                    final JSONArray arrayJg = data.getJSONArray("listJgId");
                                    if (arrayGz != null) {
                                        gzUrl = arrayGz.getString(arrayGz.size() - 1);
                                    } else {
                                        tvGz.setVisibility(View.GONE);
                                    }
                                    if (arrayJg != null) {
                                        JSONObject list = arrayJg.getJSONObject(arrayJg.size() - 1);
                                        jgEntity = list.getString("entity");
                                        jgEntityId = list.getString("entityId");
                                    } else {
                                        tvJg.setVisibility(View.GONE);
                                    }

                                    String reportTitle = data.getString("reportTitle");
                                    shareUrl = data.getString("url");
                                    shareTitle = reportTitle;
                                    if (!TextUtils.isEmpty(reportTitle)) {
                                        tvMainTitle.setText(reportTitle);
                                    } else {
                                        tvMainTitle.setText("/");
                                    }
                                    String area = data.getString("area");
                                    if (!TextUtils.isEmpty(area)) {
                                        tvMainArea.setText(area);
                                    } else {
                                        tvMainArea.setText("/");
                                    }
                                    String sysTime = data.getString("sysTime");
                                    if (!TextUtils.isEmpty(sysTime)) {
                                        tvPubTime.setText(sysTime);
                                    } else {
                                        tvPubTime.setText("/");
                                    }
                                    String deadTime = data.getString("deadTime");
                                    if (!TextUtils.isEmpty(deadTime)) {
                                        tvDeadTime.setText(deadTime);
                                    } else {
                                        tvDeadTime.setText("/");
                                    }
                                    String entryName = data.getString("entryName");
                                    if (!TextUtils.isEmpty(entryName)) {
                                        tvFbt.setText(entryName);
                                    } else {
                                        tvFbt.setText("");
                                    }
                                    String squone = data.getString("squone");
                                    if (!TextUtils.isEmpty(squone)) {
                                        //将所有<*a标签替换成html标签
                                        squone = squone.replace("<*", "<font color='black'><u>").replace("</*", "</u></font>");
                                        squone = squone.replace("\n", "<br/>");
                                        tv1.setText(Html.fromHtml(squone));
                                        shareContent = squone;
                                    }
                                    String squtwo = data.getString("squtwo");
                                    if (!TextUtils.isEmpty(squtwo)) {
                                        //将所有<*a标签替换成html标签
                                        squtwo = squtwo.replace("<*", "<font color='black'><u>").replace("</*", "</u></font>");
                                        squtwo = squtwo.replace("\n", "<br/>");
                                        tv2.setText(Html.fromHtml(squtwo));
                                    }
                                    String squthree = data.getString("squthree");
                                    if (!TextUtils.isEmpty(squthree)) {
                                        //将所有<*a标签替换成html标签
                                        squthree = squthree.replace("<*", "<font color='black'><u>").replace("</*", "</u></font>");
                                        squthree = squthree.replace("\n", "<br/>");
                                        tv3.setText(Html.fromHtml(squthree));
                                    }
                                    String squfour = data.getString("squfour");
                                    if (!TextUtils.isEmpty(squfour)) {
                                        //将所有<*a标签替换成html标签
                                        squfour = squfour.replace("<*", "<font color='black'><u>").replace("</*", "</u></font>");
                                        squfour = squfour.replace("\n", "<br/>");
                                        tv4.setText(Html.fromHtml(squfour));
                                    }
                                    String squfive = data.getString("squfive");
                                    if (!TextUtils.isEmpty(squfive)) {
                                        //将所有<*a标签替换成html标签
                                        squfive = squfive.replace("<*", "<font color='black'><u>").replace("</*", "</u></font>");
                                        squfive = squfive.replace("\n", "<br/>");
                                        tv5.setText(Html.fromHtml(squfive));
                                    }
                                    String squsix = data.getString("squsix");
                                    if (!TextUtils.isEmpty(squsix)) {
                                        //将所有<*a标签替换成html标签
                                        squsix = squsix.replace("<*", "<font color='black'><u>").replace("</*", "</u></font>");
                                        squsix = squsix.replace("\n", "<br/>");
                                        tv6.setText(Html.fromHtml(squsix));
                                    }
                                    String squseven = data.getString("squseven");
                                    if (!TextUtils.isEmpty(squseven)) {
                                        //将所有<*a标签替换成html标签
                                        squseven = squseven.replace("<*", "<font color='black'><u>").replace("</*", "</u></font>");
                                        squseven = squseven.replace("\n", "<br/>");
                                        tv7.setText(Html.fromHtml(squseven));
                                    }

                                    indexSggjyDetailStatusView.showContent();
                                } else {
                                    indexSggjyDetailStatusView.showError();
                                }
                            }
                        });
            } else {
                OkGo.<String>post(BiaoXunTongApi.URL_GETCOLLECTIONLISTDETAIL)
                        .params("entityId", mEntityId)
                        .params("entity", mEntity)
                        .params("diqu", mDiqu)
                        .params("deviceId", deviceId)
                        .params("ajaxlogtype", ajaxlogtype)
                        .execute(new StringCallback() {
                            @Override
                            public void onSuccess(Response<String> response) {
                                String jiemi = AesUtil.aesDecrypt(response.body(), BiaoXunTongApi.PAS_KEY);

                                final JSONObject object = JSON.parseObject(jiemi);
                                String status = object.getString("status");

                                if ("200".equals(status)) {
                                    indexSggjyDetailStatusView.showContent();
                                    final JSONObject data = object.getJSONObject("data");

                                    //判断是否有更正和结果
                                    //1、判断有误更正
                                    final JSONArray arrayGz = data.getJSONArray("listGzUrl");
                                    //2、判断有无结果
                                    final JSONArray arrayJg = data.getJSONArray("listJgId");
                                    if (arrayGz != null) {
                                        gzUrl = arrayGz.getString(arrayGz.size() - 1);
                                    } else {
                                        tvGz.setVisibility(View.GONE);
                                    }
                                    if (arrayJg != null) {
                                        JSONObject list = arrayJg.getJSONObject(arrayJg.size() - 1);
                                        jgEntity = list.getString("entity");
                                        jgEntityId = list.getString("entityId");
                                    } else {
                                        tvJg.setVisibility(View.GONE);
                                    }

                                    String reportTitle = data.getString("reportTitle");
                                    shareUrl = data.getString("url");
                                    shareTitle = reportTitle;
                                    if (!TextUtils.isEmpty(reportTitle)) {
                                        tvMainTitle.setText(reportTitle);
                                    } else {
                                        tvMainTitle.setText("/");
                                    }
                                    String area = data.getString("area");
                                    if (!TextUtils.isEmpty(area)) {
                                        tvMainArea.setText(area);
                                    } else {
                                        tvMainArea.setText("/");
                                    }
                                    String sysTime = data.getString("sysTime");
                                    if (!TextUtils.isEmpty(sysTime)) {
                                        tvPubTime.setText(sysTime);
                                    } else {
                                        tvPubTime.setText("/");
                                    }
                                    String deadTime = data.getString("deadTime");
                                    if (!TextUtils.isEmpty(deadTime)) {
                                        tvDeadTime.setText(deadTime);
                                    } else {
                                        tvDeadTime.setText("/");
                                    }
                                    String entryName = data.getString("entryName");
                                    if (!TextUtils.isEmpty(entryName)) {
                                        tvFbt.setText(entryName);
                                    } else {
                                        tvFbt.setText("");
                                    }
                                    String squone = data.getString("squone");
                                    if (!TextUtils.isEmpty(squone)) {
                                        //将所有<*a标签替换成html标签
                                        squone = squone.replace("<*", "<font color='black'><u>").replace("</*", "</u></font>");
                                        squone = squone.replace("\n", "<br/>");
                                        tv1.setText(Html.fromHtml(squone));
                                        shareContent = squone;
                                    }
                                    String squtwo = data.getString("squtwo");
                                    if (!TextUtils.isEmpty(squtwo)) {
                                        //将所有<*a标签替换成html标签
                                        squtwo = squtwo.replace("<*", "<font color='black'><u>").replace("</*", "</u></font>");
                                        squtwo = squtwo.replace("\n", "<br/>");
                                        tv2.setText(Html.fromHtml(squtwo));
                                    }
                                    String squthree = data.getString("squthree");
                                    if (!TextUtils.isEmpty(squthree)) {
                                        //将所有<*a标签替换成html标签
                                        squthree = squthree.replace("<*", "<font color='black'><u>").replace("</*", "</u></font>");
                                        squthree = squthree.replace("\n", "<br/>");
                                        tv3.setText(Html.fromHtml(squthree));
                                    }
                                    String squfour = data.getString("squfour");
                                    if (!TextUtils.isEmpty(squfour)) {
                                        //将所有<*a标签替换成html标签
                                        squfour = squfour.replace("<*", "<font color='black'><u>").replace("</*", "</u></font>");
                                        squfour = squfour.replace("\n", "<br/>");
                                        tv4.setText(Html.fromHtml(squfour));
                                    }
                                    String squfive = data.getString("squfive");
                                    if (!TextUtils.isEmpty(squfive)) {
                                        //将所有<*a标签替换成html标签
                                        squfive = squfive.replace("<*", "<font color='black'><u>").replace("</*", "</u></font>");
                                        squfive = squfive.replace("\n", "<br/>");
                                        tv5.setText(Html.fromHtml(squfive));
                                    }
                                    String squsix = data.getString("squsix");
                                    if (!TextUtils.isEmpty(squsix)) {
                                        //将所有<*a标签替换成html标签
                                        squsix = squsix.replace("<*", "<font color='black'><u>").replace("</*", "</u></font>");
                                        squsix = squsix.replace("\n", "<br/>");
                                        tv6.setText(Html.fromHtml(squsix));
                                    }
                                    String squseven = data.getString("squseven");
                                    if (!TextUtils.isEmpty(squseven)) {
                                        //将所有<*a标签替换成html标签
                                        squseven = squseven.replace("<*", "<font color='black'><u>").replace("</*", "</u></font>");
                                        squseven = squseven.replace("\n", "<br/>");
                                        tv7.setText(Html.fromHtml(squseven));
                                    }

                                } else {
                                    indexSggjyDetailStatusView.showError();
                                }
                            }
                        });
            }

        }

    }


    private Share mShare = new Share();
    private PromptDialog promptDialog = null;

    @Override
    public void onClick(View view) {
        mShare.setAppName("鲁班标讯通");
        mShare.setAppShareIcon(R.mipmap.ic_share);
        if (mShare.getBitmapResID() == 0) {
            mShare.setBitmapResID(R.mipmap.ic_share);
        }
        mShare.setTitle(shareTitle);
        mShare.setContent(tv1.getText().toString());
        mShare.setSummary(tv1.getText().toString());
        mShare.setDescription(tv1.getText().toString());
        mShare.setImageUrl(null);
        mShare.setUrl(BiaoXunTongApi.SHARE_URL + shareUrl);
        switch (view.getId()) {
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
                getActivity().onBackPressed();
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
                                .params("deviceId", deviceId)
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
                                .params("deviceId", deviceId)
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
            case R.id.tv_yw:
                ARouter.getInstance().build("/com/BrowserSuitActivity").withString("mUrl",shareUrl).withString("mTitle",shareTitle).navigation();
                break;
            case R.id.tv_gzgg:
                ARouter.getInstance().build("/com/BrowserSuitActivity").withString("mUrl",gzUrl).withString("mTitle","更正公告").navigation();
                break;
            case R.id.tv_jggg:
                if ("cqsggjyzbjg".equals(jgEntity)) {
                    Intent intent = new Intent(BiaoXunTong.getApplicationContext(), ResultCqsggjyzbjgDetailActivity.class);
                    intent.putExtra("entityId", Integer.valueOf(jgEntityId));
                    intent.putExtra("entity", jgEntity);
                    intent.putExtra("ajaxlogtype", "0");
                    intent.putExtra("mId", "");
                    startActivity(intent);

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
