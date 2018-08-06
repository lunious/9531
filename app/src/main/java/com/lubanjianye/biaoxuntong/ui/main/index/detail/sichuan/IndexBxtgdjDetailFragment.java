package com.lubanjianye.biaoxuntong.ui.main.index.detail.sichuan;

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


public class IndexBxtgdjDetailFragment extends BaseFragment1 implements View.OnClickListener, OpenBuilder.Callback {

    private LinearLayout llIvBack = null;
    private AppCompatTextView mainBarName = null;
    private MultipleStatusView bxtgdjDetailStatusView = null;
    private AppCompatTextView tvMainTitle = null;
    private LinearLayout llType = null;
    private AppCompatTextView tvType = null;
    private AppCompatTextView tvMainArea = null;
    private AppCompatTextView tvMainPubTime = null;
    private AppCompatTextView tvMainDeadTime = null;
    private AppCompatTextView tvPuNum = null;
    private AppCompatTextView tv1 = null;
    private AppCompatTextView tv2 = null;
    private AppCompatTextView tv3 = null;
    private AppCompatTextView tv4 = null;
    private AppCompatTextView tv5 = null;
    private AppCompatTextView tv6 = null;
    private AppCompatTextView tv7 = null;
    private AppCompatTextView endSam = null;
    private ImageView ivFav = null;
    private LinearLayout llFav = null;
    private LinearLayout llShare = null;

    private LinearLayout llWeiBoShare_bottom = null;
    private LinearLayout llQQBoShare_bottom = null;
    private LinearLayout llWeixinBoShare_bottom = null;
    private LinearLayout llPyqShare_bottom = null;


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
    private String ajaxType = "0";


    public static IndexBxtgdjDetailFragment create(@NonNull int entityId, String entity, String ajaxlogtype) {
        final Bundle args = new Bundle();
        args.putInt(ARG_ENTITYID, entityId);
        args.putString(ARG_ENTITY, entity);
        args.putString(ARG_AJAXTYPE, ajaxlogtype);
        final IndexBxtgdjDetailFragment fragment = new IndexBxtgdjDetailFragment();
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
    public Object setLayout() {
        return R.layout.fragment_index_bxtgdj_detail;
    }

    @Override
    public void initView() {
        llIvBack = getView().findViewById(R.id.ll_iv_back);
        mainBarName = getView().findViewById(R.id.main_bar_name);
        bxtgdjDetailStatusView = getView().findViewById(R.id.bxtgdj_detail_status_view);
        tvMainTitle = getView().findViewById(R.id.tv_main_title);
        llType = getView().findViewById(R.id.ll_type);
        tvType = getView().findViewById(R.id.tv_type);
        tvMainArea = getView().findViewById(R.id.tv_main_area);
        tvMainPubTime = getView().findViewById(R.id.tv_main_pub_time);
        tvMainDeadTime = getView().findViewById(R.id.tv_main_dead_time);
        tvPuNum = getView().findViewById(R.id.tv_pu_num);
        tv1 = getView().findViewById(R.id.tv1);
        tv2 = getView().findViewById(R.id.tv2);
        tv3 = getView().findViewById(R.id.tv3);
        tv4 = getView().findViewById(R.id.tv4);
        tv5 = getView().findViewById(R.id.tv5);
        tv6 = getView().findViewById(R.id.tv6);
        tv7 = getView().findViewById(R.id.tv7);
        endSam = getView().findViewById(R.id.endSam);
        ivFav = getView().findViewById(R.id.iv_fav);
        llFav = getView().findViewById(R.id.ll_fav);
        llShare = getView().findViewById(R.id.ll_share);
        llWeiBoShare_bottom = getView().findViewById(R.id.ll_weibo_share_bottom);
        llQQBoShare_bottom = getView().findViewById(R.id.ll_qq_share_bottom);
        llWeixinBoShare_bottom = getView().findViewById(R.id.ll_chat_share_bottom);
        llPyqShare_bottom = getView().findViewById(R.id.ll_pyq_share_bottom);
        llIvBack.setOnClickListener(this);
        llShare.setOnClickListener(this);
        llFav.setOnClickListener(this);
        llWeiBoShare_bottom.setOnClickListener(this);
        llQQBoShare_bottom.setOnClickListener(this);
        llWeixinBoShare_bottom.setOnClickListener(this);
        llPyqShare_bottom.setOnClickListener(this);


    }

    @Override
    public void initData() {
        llIvBack.setVisibility(View.VISIBLE);
        mainBarName.setText("标讯详情");
        bxtgdjDetailStatusView.setOnRetryClickListener(mRetryClickListener);
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

        if ("1".equals(ajaxType)) {
            //改变已读未读状态
            EventBus.getDefault().post(new EventMessage(EventMessage.READ_STATUS));
        }


        if (!NetUtil.isNetworkConnected(getActivity())) {
            bxtgdjDetailStatusView.showNoNetwork();
        } else {
            bxtgdjDetailStatusView.showLoading();
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
                                    String reportTitle = data.getString("reportTitle");
                                    shareUrl = data.getString("url");
                                    shareTitle = reportTitle;
                                    if (!TextUtils.isEmpty(reportTitle)) {
                                        tvMainTitle.setText(reportTitle);
                                    } else {
                                        tvMainTitle.setText("/");
                                    }
                                    String entryType = data.getString("entryType");
                                    if (!TextUtils.isEmpty(entryType)) {
                                        tvType.setText(entryType);
                                    } else {
                                        llType.setVisibility(View.GONE);
                                    }

                                    String area = data.getString("area");
                                    if (!TextUtils.isEmpty(area)) {
                                        tvMainArea.setText(area);
                                    } else {
                                        tvMainArea.setText("/");
                                    }
                                    String beginTime = data.getString("beginTime");
                                    if (!TextUtils.isEmpty(beginTime)) {
                                        tvMainPubTime.setText(beginTime);
                                    } else {
                                        tvMainPubTime.setText("/");
                                    }
                                    String endTime = data.getString("endTime");
                                    if (!TextUtils.isEmpty(endTime)) {
                                        tvMainDeadTime.setText(endTime);
                                    } else {
                                        tvMainDeadTime.setText("/");
                                    }
                                    String entryNum = data.getString("entryNum");
                                    if (!TextUtils.isEmpty(entryNum)) {
                                        tvPuNum.setText(entryNum);
                                    } else {
                                        tvPuNum.setText("/");
                                    }
                                    String one = data.getString("one");
                                    if (one != null) {
                                        //将所有<*a标签替换成html标签
                                        one = one.replace("<*", "<font color='black'><u>").replace("</*", "</u></font>");
                                        one = one.replace("\n", "<br/>");
                                        tv1.setText(Html.fromHtml(one));
                                        shareContent = one;
                                    } else {
                                        tv1.setText("/");
                                    }
                                    String two = data.getString("two");
                                    if (two != null) {
                                        //将所有<*a标签替换成html标签
                                        two = two.replace("<*", "<font color='black'><u>").replace("</*", "</u></font>");
                                        two = two.replace("\n", "<br/>");
                                        tv2.setText(Html.fromHtml(two));
                                    } else {
                                        tv2.setText("/");
                                    }
                                    String three = data.getString("three");
                                    if (three != null) {
                                        //将所有<*a标签替换成html标签
                                        three = three.replace("<*", "<font color='black'><u>").replace("</*", "</u></font>");
                                        three = three.replace("\n", "<br/>");
                                        tv3.setText(Html.fromHtml(three));
                                    } else {
                                        tv3.setText("/");
                                    }
                                    String four = data.getString("four");
                                    if (four != null) {
                                        //将所有<*a标签替换成html标签
                                        four = four.replace("<*", "<font color='black'><u>").replace("</*", "</u></font>");
                                        four = four.replace("\n", "<br/>");
                                        tv4.setText(Html.fromHtml(four));
                                    } else {
                                        tv4.setText("/");
                                    }
                                    String five = data.getString("five");
                                    if (five != null) {
                                        //将所有<*a标签替换成html标签
                                        five = five.replace("<*", "<font color='black'><u>").replace("</*", "</u></font>");
                                        five = five.replace("\n", "<br/>");
                                        tv5.setText(Html.fromHtml(five));
                                    } else {
                                        tv5.setText("/");
                                    }
                                    String six = data.getString("six");
                                    if (six != null) {
                                        //将所有<*a标签替换成html标签
                                        six = six.replace("<*", "<font color='black'><u>").replace("</*", "</u></font>");
                                        six = six.replace("\n", "<br/>");
                                        tv6.setText(Html.fromHtml(six));
                                    } else {
                                        tv6.setText("/");
                                    }
                                    String seven = data.getString("seven");
                                    if (seven != null) {
                                        //将所有<*a标签替换成html标签
                                        seven = seven.replace("<*", "<font color='black'><u>").replace("</*", "</u></font>");
                                        seven = seven.replace("\n", "<br/>");
                                        tv7.setText(Html.fromHtml(seven));
                                    } else {
                                        tv7.setText("/");
                                    }
                                    String endsam = data.getString("endsam");
                                    if (endsam != null) {
                                        //将所有<*a标签替换成html标签
                                        endsam = endsam.replace("<*", "<font color='black'><u>").replace("</*", "</u></font>");
                                        endsam = endsam.replace("\n", "<br/>");
                                        endSam.setText(Html.fromHtml(endsam));
                                    } else {
                                        endSam.setText("/");
                                    }
                                    bxtgdjDetailStatusView.showContent();
                                } else {
                                    bxtgdjDetailStatusView.showError();
                                }
                            }
                        });

            } else {

                OkGo.<String>post(BiaoXunTongApi.URL_GETCOLLECTIONLISTDETAIL)
                        .params("entityId", mEntityId)
                        .params("entity", mEntity)
                        .params("deviceId", deviceId)
                        .params("ajaxlogtype", ajaxType)
                        .execute(new StringCallback() {
                            @Override
                            public void onSuccess(Response<String> response) {
                                String jiemi = AesUtil.aesDecrypt(response.body(), BiaoXunTongApi.PAS_KEY);

                                final JSONObject object = JSON.parseObject(jiemi);
                                String status = object.getString("status");

                                if ("200".equals(status)) {
                                    bxtgdjDetailStatusView.showContent();
                                    final JSONObject data = object.getJSONObject("data");
                                    String reportTitle = data.getString("reportTitle");
                                    shareUrl = data.getString("url");
                                    shareTitle = reportTitle;
                                    if (!TextUtils.isEmpty(reportTitle)) {
                                        tvMainTitle.setText(reportTitle);
                                    } else {
                                        tvMainTitle.setText("/");
                                    }

                                    String entryType = data.getString("entryType");
                                    if (!TextUtils.isEmpty(entryType)) {
                                        tvType.setText(entryType);
                                    } else {
                                        llType.setVisibility(View.GONE);
                                    }


                                    String area = data.getString("area");
                                    if (!TextUtils.isEmpty(area)) {
                                        tvMainArea.setText(area);
                                    } else {
                                        tvMainArea.setText("/");
                                    }
                                    String beginTime = data.getString("beginTime");
                                    if (!TextUtils.isEmpty(beginTime)) {
                                        tvMainPubTime.setText(beginTime);
                                    } else {
                                        tvMainPubTime.setText("/");
                                    }
                                    String endTime = data.getString("endTime");
                                    if (!TextUtils.isEmpty(endTime)) {
                                        tvMainDeadTime.setText(endTime);
                                    } else {
                                        tvMainDeadTime.setText("/");
                                    }
                                    String entryNum = data.getString("entryNum");
                                    if (!TextUtils.isEmpty(entryNum)) {
                                        tvPuNum.setText(entryNum);
                                    } else {
                                        tvPuNum.setText("/");
                                    }
                                    String one = data.getString("one");
                                    if (one != null) {
                                        //将所有<*a标签替换成html标签
                                        one = one.replace("<*", "<font color='black'><u>").replace("</*", "</u></font>");
                                        one = one.replace("\n", "<br/>");
                                        tv1.setText(Html.fromHtml(one));
                                        shareContent = one;
                                    } else {
                                        tv1.setText("/");
                                    }
                                    String two = data.getString("two");
                                    if (two != null) {
                                        two = two.replace("<*", "<font color='black'><u>").replace("</*", "</u></font>");
                                        two = two.replace("\n", "<br/>");
                                        tv2.setText(Html.fromHtml(two));
                                    } else {
                                        tv2.setText("/");
                                    }
                                    String three = data.getString("three");
                                    if (three != null) {
                                        three = three.replace("<*", "<font color='black'><u>").replace("</*", "</u></font>");
                                        three = three.replace("\n", "<br/>");
                                        tv3.setText(Html.fromHtml(three));
                                    } else {
                                        tv3.setText("/");
                                    }
                                    String four = data.getString("four");
                                    if (four != null) {
                                        four = four.replace("<*", "<font color='black'><u>").replace("</*", "</u></font>");
                                        four = four.replace("\n", "<br/>");
                                        tv4.setText(Html.fromHtml(four));
                                    } else {
                                        tv4.setText("/");
                                    }
                                    String five = data.getString("five");
                                    if (five != null) {
                                        five = five.replace("<*", "<font color='black'><u>").replace("</*", "</u></font>");
                                        five = five.replace("\n", "<br/>");
                                        tv5.setText(Html.fromHtml(five));
                                    } else {
                                        tv5.setText("/");
                                    }
                                    String six = data.getString("six");
                                    if (six != null) {
                                        six = six.replace("<*", "<font color='black'><u>").replace("</*", "</u></font>");
                                        six = six.replace("\n", "<br/>");
                                        tv6.setText(Html.fromHtml(six));
                                    } else {
                                        tv6.setText("/");
                                    }
                                    String seven = data.getString("seven");
                                    if (seven != null) {
                                        seven = seven.replace("<*", "<font color='black'><u>").replace("</*", "</u></font>");
                                        seven = seven.replace("\n", "<br/>");
                                        tv7.setText(Html.fromHtml(seven));
                                    } else {
                                        tv7.setText("/");
                                    }
                                    String endsam = data.getString("endsam");
                                    if (endsam != null) {
                                        endsam = endsam.replace("<*", "<font color='black'><u>").replace("</*", "</u></font>");
                                        endsam = endsam.replace("\n", "<br/>");
                                        endSam.setText(Html.fromHtml(endsam));
                                    } else {
                                        endSam.setText("/");
                                    }
                                } else {
                                    bxtgdjDetailStatusView.showError();
                                }
                            }
                        });
            }

        }


    }

    private Share mShare = new Share();

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
            case R.id.ll_weibo_share_bottom:
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
            case R.id.ll_qq_share_bottom:
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
            case R.id.ll_chat_share_bottom:
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
            case R.id.ll_pyq_share_bottom:
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
