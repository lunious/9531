package com.lubanjianye.biaoxuntong.ui.main.index.detail.sichuan;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatTextView;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.classic.common.MultipleStatusView;
import com.lubanjianye.biaoxuntong.R;
import com.lubanjianye.biaoxuntong.base.BaseFragment1;
import com.lubanjianye.biaoxuntong.database.DatabaseManager;
import com.lubanjianye.biaoxuntong.database.UserProfile;
import com.lubanjianye.biaoxuntong.eventbus.EventMessage;
import com.lubanjianye.biaoxuntong.app.BiaoXunTongApi;
import com.lubanjianye.biaoxuntong.ui.sign.SignInActivity;
import com.lubanjianye.biaoxuntong.ui.share.OpenBuilder;
import com.lubanjianye.biaoxuntong.ui.share.OpenConstant;
import com.lubanjianye.biaoxuntong.ui.share.Share;
import com.lubanjianye.biaoxuntong.util.aes.AesUtil;
import com.lubanjianye.biaoxuntong.util.dialog.PromptDialog;
import com.lubanjianye.biaoxuntong.util.netStatus.NetUtil;
import com.lubanjianye.biaoxuntong.util.netStatus.AppSysMgr;
import com.lubanjianye.biaoxuntong.util.sp.AppSharePreferenceMgr;
import com.lubanjianye.biaoxuntong.util.toast.ToastUtil;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.UiError;

import org.greenrobot.eventbus.EventBus;

import java.util.List;


public class IndexSggjycgrowDetailFragment extends BaseFragment1 implements View.OnClickListener, OpenBuilder.Callback {

    private LinearLayout llIvBack = null;
    private AppCompatTextView mainBarName = null;
    private ImageView ivFav = null;
    private LinearLayout llFav = null;
    private LinearLayout llShare = null;
    private MultipleStatusView sggjycgrowDetailStatusView = null;
    private AppCompatTextView tvMainTitle = null;
    private AppCompatTextView tvMainResource = null;
    private AppCompatTextView tvPuNum = null;
    private AppCompatTextView tv1 = null;
    private AppCompatTextView tv2 = null;
    private AppCompatTextView tv3 = null;
    private AppCompatTextView tv4 = null;
    private AppCompatTextView tv5 = null;
    private AppCompatTextView tv6 = null;
    private AppCompatTextView tv7 = null;
    private AppCompatTextView tv8 = null;

    private LinearLayout llWeiBoShare = null;
    private LinearLayout llQQBoShare = null;
    private LinearLayout llWeixinBoShare = null;
    private LinearLayout llPyqShare = null;


    private static final String ARG_ENTITYID = "ARG_ENTITYID";
    private static final String ARG_ENTITY = "ARG_ENTITY";
    private static final String ARG_AJAXTYPE = "ARG_AJAXTYPE";


    private int myFav = -1;
    private int mEntityId = -1;
    private String mEntity = "";
    private String ajaxlogtype = "";

    private String shareTitle = "";
    private String shareContent = "";
    private String shareUrl = "";

    private String deviceId = AppSysMgr.getPsuedoUniqueID();



    public static IndexSggjycgrowDetailFragment create(@NonNull int entityId, String entity, String ajaxlogtype) {
        final Bundle args = new Bundle();
        args.putInt(ARG_ENTITYID, entityId);
        args.putString(ARG_ENTITY, entity);
        args.putString(ARG_AJAXTYPE, ajaxlogtype);
        final IndexSggjycgrowDetailFragment fragment = new IndexSggjycgrowDetailFragment();
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
        return R.layout.fragment_index_sggjycgrow_detail;
    }

    @Override
    public void initView() {
        llIvBack = getView().findViewById(R.id.ll_iv_back);
        mainBarName = getView().findViewById(R.id.main_bar_name);
        ivFav = getView().findViewById(R.id.iv_fav);
        llFav = getView().findViewById(R.id.ll_fav);
        llShare = getView().findViewById(R.id.ll_share);
        sggjycgrowDetailStatusView = getView().findViewById(R.id.sggjycgrow_detail_status_view);
        tvMainTitle = getView().findViewById(R.id.tv_main_title);
        tvMainResource = getView().findViewById(R.id.tv_main_resource);
        tvPuNum = getView().findViewById(R.id.tv_pu_num);
        tv1 = getView().findViewById(R.id.tv1);
        tv2 = getView().findViewById(R.id.tv2);
        tv3 = getView().findViewById(R.id.tv3);
        tv4 = getView().findViewById(R.id.tv4);
        tv5 = getView().findViewById(R.id.tv5);
        tv6 = getView().findViewById(R.id.tv6);
        tv7 = getView().findViewById(R.id.tv7);
        tv8 = getView().findViewById(R.id.tv8);
        llWeiBoShare = getView().findViewById(R.id.ll_weibo_share);
        llQQBoShare = getView().findViewById(R.id.ll_qq_share);
        llWeixinBoShare = getView().findViewById(R.id.ll_chat_share);
        llPyqShare = getView().findViewById(R.id.ll_pyq_share);


        llIvBack.setOnClickListener(this);
        llFav.setOnClickListener(this);
        llShare.setOnClickListener(this);

        llWeiBoShare.setOnClickListener(this);
        llQQBoShare.setOnClickListener(this);
        llWeixinBoShare.setOnClickListener(this);
        llPyqShare.setOnClickListener(this);

    }

    @Override
    public void initData() {
        llIvBack.setVisibility(View.VISIBLE);
        mainBarName.setText("标讯详情");
        sggjycgrowDetailStatusView.setOnRetryClickListener(mRetryClickListener);
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

    private void requestData() {

        if ("1".equals(ajaxlogtype)) {
            //改变已读未读状态
            EventBus.getDefault().post(new EventMessage(EventMessage.READ_STATUS));
        }


        if (!NetUtil.isNetworkConnected(getActivity())) {
            sggjycgrowDetailStatusView.showNoNetwork();
        } else {
            sggjycgrowDetailStatusView.showLoading();

            if (AppSharePreferenceMgr.contains(getContext(), EventMessage.LOGIN_SUCCSS)) {
                List<UserProfile> users = DatabaseManager.getInstance().getDao().loadAll();
                for (int i = 0; i < users.size(); i++) {
                    id = users.get(0).getId();
                }

                OkGo.<String>post(BiaoXunTongApi.URL_GETCOLLECTIONLISTDETAIL)
                        .params("entityId", mEntityId)
                        .params("entity", mEntity)
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
                                if (favorite == 1) {
                                    myFav = 1;
                                    ivFav.setImageResource(R.mipmap.ic_faved_pressed);
                                } else if (favorite == 0) {
                                    myFav = 0;
                                    ivFav.setImageResource(R.mipmap.ic_fav_pressed);
                                }


                                if ("200".equals(status)) {
                                    final JSONObject data = object.getJSONObject("data");
                                    String reportTitle = data.getString("reportTitle");
                                    shareUrl = data.getString("url");
                                    shareTitle = reportTitle;

                                    String squ0 = data.getString("squ0");
                                    shareContent = squ0;
                                    if (!TextUtils.isEmpty(squ0)) {
                                        tvMainTitle.setText(squ0);
                                    } else {
                                        tvMainTitle.setText(reportTitle);
                                    }
                                    String resource = data.getString("resource");
                                    if (!TextUtils.isEmpty(resource)) {
                                        tvMainResource.setText(resource);
                                    } else {
                                        tvMainResource.setText("/");
                                    }
                                    String sysTime = data.getString("sysTime");
                                    if (!TextUtils.isEmpty(sysTime)) {
                                        tvPuNum.setText(sysTime);
                                    } else {
                                        tvPuNum.setText("/");
                                    }

                                    String squ1 = data.getString("squ1");
                                    if (!TextUtils.isEmpty(squ1)) {
                                        tv1.setText(squ1);
                                    } else {
                                        tv1.setText("/");
                                    }

                                    String squ2 = data.getString("squ2");
                                    if (!TextUtils.isEmpty(squ2)) {
                                        tv2.setText(squ2);
                                    } else {
                                        tv2.setText("/");
                                    }

                                    String squ3 = data.getString("squ3");
                                    if (!TextUtils.isEmpty(squ3)) {
                                        tv3.setText(squ3);
                                    } else {
                                        tv3.setText("/");
                                    }

                                    String squ4 = data.getString("squ4");
                                    if (!TextUtils.isEmpty(squ4)) {
                                        tv4.setText(squ4);
                                    } else {
                                        tv4.setText("/");
                                    }

                                    String squ5 = data.getString("squ5");
                                    if (!TextUtils.isEmpty(squ5)) {
                                        tv5.setText(squ5);
                                    } else {
                                        tv5.setText("/");
                                    }

                                    String squ6 = data.getString("squ6");
                                    if (!TextUtils.isEmpty(squ6)) {
                                        tv6.setText(squ6);
                                    } else {
                                        tv6.setText("/");
                                    }

                                    String squ7 = data.getString("squ7");
                                    if (!TextUtils.isEmpty(squ7)) {
                                        tv7.setText(squ7);
                                    } else {
                                        tv7.setText("/");
                                    }

                                    String squ8 = data.getString("squ8");
                                    if (!TextUtils.isEmpty(squ8)) {
                                        tv8.setText(squ8);
                                    } else {
                                        tv8.setText("/");
                                    }
                                    sggjycgrowDetailStatusView.showContent();
                                } else {
                                    sggjycgrowDetailStatusView.showError();
                                }
                            }
                        });

            } else {

                OkGo.<String>post(BiaoXunTongApi.URL_GETCOLLECTIONLISTDETAIL)
                        .params("entityId", mEntityId)
                        .params("entity", mEntity)
                        .params("deviceId", deviceId)
                        .params("ajaxlogtype", ajaxlogtype)
                        .execute(new StringCallback() {
                            @Override
                            public void onSuccess(Response<String> response) {

                                String jiemi = AesUtil.aesDecrypt(response.body(), BiaoXunTongApi.PAS_KEY);

                                final JSONObject object = JSON.parseObject(jiemi);
                                String status = object.getString("status");

                                if ("200".equals(status)) {
                                    final JSONObject data = object.getJSONObject("data");
                                    String reportTitle = data.getString("reportTitle");
                                    shareUrl = data.getString("url");
                                    shareTitle = reportTitle;

                                    String squ0 = data.getString("squ0");
                                    shareContent = squ0;
                                    if (!TextUtils.isEmpty(squ0)) {
                                        tvMainTitle.setText(squ0);
                                    } else {
                                        tvMainTitle.setText(reportTitle);
                                    }
                                    String resource = data.getString("resource");
                                    if (!TextUtils.isEmpty(resource)) {
                                        tvMainResource.setText(resource);
                                    } else {
                                        tvMainResource.setText("/");
                                    }
                                    String sysTime = data.getString("sysTime");
                                    if (!TextUtils.isEmpty(sysTime)) {
                                        tvPuNum.setText(sysTime);
                                    } else {
                                        tvPuNum.setText("/");
                                    }

                                    String squ1 = data.getString("squ1");
                                    if (!TextUtils.isEmpty(squ1)) {
                                        tv1.setText(squ1);
                                    } else {
                                        tv1.setText("/");
                                    }

                                    String squ2 = data.getString("squ2");
                                    if (!TextUtils.isEmpty(squ2)) {
                                        tv2.setText(squ2);
                                    } else {
                                        tv2.setText("/");
                                    }

                                    String squ3 = data.getString("squ3");
                                    if (!TextUtils.isEmpty(squ3)) {
                                        tv3.setText(squ3);
                                    } else {
                                        tv3.setText("/");
                                    }

                                    String squ4 = data.getString("squ4");
                                    if (!TextUtils.isEmpty(squ4)) {
                                        tv4.setText(squ4);
                                    } else {
                                        tv4.setText("/");
                                    }

                                    String squ5 = data.getString("squ5");
                                    if (!TextUtils.isEmpty(squ5)) {
                                        tv5.setText(squ5);
                                    } else {
                                        tv5.setText("/");
                                    }

                                    String squ6 = data.getString("squ6");
                                    if (!TextUtils.isEmpty(squ6)) {
                                        tv6.setText(squ6);
                                    } else {
                                        tv6.setText("/");
                                    }

                                    String squ7 = data.getString("squ7");
                                    if (!TextUtils.isEmpty(squ7)) {
                                        tv7.setText(squ7);
                                    } else {
                                        tv7.setText("/");
                                    }

                                    String squ8 = data.getString("squ8");
                                    if (!TextUtils.isEmpty(squ8)) {
                                        tv8.setText(squ8);
                                    } else {
                                        tv8.setText("/");
                                    }

                                    sggjycgrowDetailStatusView.showContent();
                                } else {
                                    sggjycgrowDetailStatusView.showError();
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
        mShare.setContent(shareContent);
        mShare.setSummary(shareContent);
        mShare.setDescription(shareContent);
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
