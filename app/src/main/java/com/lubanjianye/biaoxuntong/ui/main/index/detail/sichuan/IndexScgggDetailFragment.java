package com.lubanjianye.biaoxuntong.ui.main.index.detail.sichuan;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatTextView;
import android.text.TextUtils;
import android.util.Log;
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
import com.lubanjianye.biaoxuntong.base.BaseFragment1;
import com.lubanjianye.biaoxuntong.database.DatabaseManager;
import com.lubanjianye.biaoxuntong.database.UserProfile;
import com.lubanjianye.biaoxuntong.eventbus.EventMessage;
import com.lubanjianye.biaoxuntong.ui.main.result.detail.sichuan.ResultSggjyzbjgDetailActivity;
import com.lubanjianye.biaoxuntong.ui.main.result.detail.sichuan.ResultXjgggDetailActivity;
import com.lubanjianye.biaoxuntong.ui.share.OpenConstant;
import com.lubanjianye.biaoxuntong.ui.sign.SignInActivity;
import com.lubanjianye.biaoxuntong.ui.share.OpenBuilder;
import com.lubanjianye.biaoxuntong.ui.share.Share;
import com.lubanjianye.biaoxuntong.util.AppConfig;
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


public class IndexScgggDetailFragment extends BaseFragment1 implements View.OnClickListener, OpenBuilder.Callback {

    LinearLayout llIvBack = null;
    AppCompatTextView mainBarName = null;
    MultipleStatusView xcgggDetailStatusView = null;
    private AppCompatTextView tvMainTitle = null;
    private AppCompatTextView tvMainArea = null;
    private AppCompatTextView tvMainCaigouMethod = null;
    private AppCompatTextView tvMainPubTime = null;
    private AppCompatTextView tvPubNum = null;
    private AppCompatTextView tvOwerCainame = null;
    private AppCompatTextView tvOwerName = null;
    private AppCompatTextView tvOwerDaili = null;
    private AppCompatTextView tvOwerBaoshu = null;
    private AppCompatTextView tvOwerJine = null;
    private AppCompatTextView tvOwerBaojia = null;
    private AppCompatTextView tvOwerMingdan = null;
    private AppCompatTextView tvOwerLianxi = null;
    private AppCompatTextView tvOwerLianxi2 = null;
    private AppCompatTextView tvOwerLianxiNumber = null;
    private AppCompatTextView tvOwerLianxiLink = null;
    private AppCompatTextView tvOwerPinshen = null;
    private AppCompatTextView tvOwerA = null;
    private AppCompatTextView tvOwerB = null;
    private AppCompatTextView tvOwerC = null;
    private AppCompatTextView tvOwerD = null;
    private AppCompatTextView tvOwerE = null;
    private AppCompatTextView tvOwerF = null;
    private AppCompatTextView tvOwerG = null;
    ImageView ivFav = null;
    LinearLayout llFav = null;
    LinearLayout llMainCaigouMethod = null;
    LinearLayout llMainPubTime = null;
    LinearLayout llPubNum = null;
    LinearLayout llOwerCainame = null;
    LinearLayout llOwerName = null;
    LinearLayout llOwerDaili = null;
    LinearLayout llOwerBaoshu = null;
    LinearLayout llOwerJine = null;
    LinearLayout llOwerBaojia = null;
    LinearLayout llOwerMingdan = null;
    LinearLayout llOwerLianxi = null;
    LinearLayout llOwerLianxi2 = null;
    LinearLayout llOwerLianxiNumber = null;
    LinearLayout llOwerLianxiLink = null;
    LinearLayout llOwerPinshen = null;
    LinearLayout llOwerA = null;
    LinearLayout llOwerB = null;
    LinearLayout llOwerC = null;
    LinearLayout llOwerD = null;
    LinearLayout llOwerE = null;
    LinearLayout llOwerF = null;
    LinearLayout llOwerG = null;
    LinearLayout llBucai = null;
    private AppCompatTextView tvBucai = null;
    LinearLayout llShare = null;


    private AppCompatTextView tvGz = null;
    private AppCompatTextView tvJg = null;


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
    private String ajaxlogtype = "";


    public static IndexScgggDetailFragment create(@NonNull int entityId, String entity, String ajaxlogtype) {
        final Bundle args = new Bundle();
        args.putInt(ARG_ENTITYID, entityId);
        args.putString(ARG_ENTITY, entity);
        args.putString(ARG_AJAXTYPE, ajaxlogtype);
        final IndexScgggDetailFragment fragment = new IndexScgggDetailFragment();
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
        return R.layout.fragment_index_xcggg_detail;
    }

    @Override
    public void initView() {
        llIvBack = getView().findViewById(R.id.ll_iv_back);
        mainBarName = getView().findViewById(R.id.main_bar_name);
        xcgggDetailStatusView = getView().findViewById(R.id.xcggg_detail_status_view);
        tvMainTitle = getView().findViewById(R.id.tv_main_title);
        tvMainArea = getView().findViewById(R.id.tv_main_area);
        tvMainCaigouMethod = getView().findViewById(R.id.tv_main_caigou_method);
        tvMainPubTime = getView().findViewById(R.id.tv_main_pub_time);
        tvPubNum = getView().findViewById(R.id.tv_pub_num);
        tvOwerCainame = getView().findViewById(R.id.tv_ower_cainame);
        tvOwerName = getView().findViewById(R.id.tv_ower_name);
        tvOwerDaili = getView().findViewById(R.id.tv_ower_daili);
        tvOwerBaoshu = getView().findViewById(R.id.tv_ower_baoshu);
        tvOwerJine = getView().findViewById(R.id.tv_ower_jine);
        tvOwerBaojia = getView().findViewById(R.id.tv_ower_baojia);
        tvOwerMingdan = getView().findViewById(R.id.tv_ower_mingdan);
        tvOwerLianxi = getView().findViewById(R.id.tv_ower_lianxi);
        tvOwerLianxi2 = getView().findViewById(R.id.tv_ower_lianxi2);
        tvOwerLianxiNumber = getView().findViewById(R.id.tv_ower_lianxi_number);
        tvOwerLianxiLink = getView().findViewById(R.id.tv_ower_lianxi_link);
        tvOwerPinshen = getView().findViewById(R.id.tv_ower_pinshen);
        tvOwerA = getView().findViewById(R.id.tv_ower_a);
        tvOwerB = getView().findViewById(R.id.tv_ower_b);
        tvOwerC = getView().findViewById(R.id.tv_ower_c);
        tvOwerD = getView().findViewById(R.id.tv_ower_d);
        tvOwerE = getView().findViewById(R.id.tv_ower_e);
        tvOwerF = getView().findViewById(R.id.tv_ower_f);
        tvOwerG = getView().findViewById(R.id.tv_ower_g);
        ivFav = getView().findViewById(R.id.iv_fav);
        llFav = getView().findViewById(R.id.ll_fav);
        llMainCaigouMethod = getView().findViewById(R.id.ll_main_caigou_method);
        llMainPubTime = getView().findViewById(R.id.ll_main_pub_time);
        llPubNum = getView().findViewById(R.id.ll_pub_num);
        llOwerCainame = getView().findViewById(R.id.ll_ower_cainame);
        llOwerName = getView().findViewById(R.id.ll_ower_name);
        llOwerDaili = getView().findViewById(R.id.ll_ower_daili);
        llOwerBaoshu = getView().findViewById(R.id.ll_ower_baoshu);
        llOwerJine = getView().findViewById(R.id.ll_ower_jine);
        llOwerBaojia = getView().findViewById(R.id.ll_ower_baojia);
        llOwerMingdan = getView().findViewById(R.id.ll_ower_mingdan);
        llOwerLianxi = getView().findViewById(R.id.ll_ower_lianxi);
        llOwerLianxi2 = getView().findViewById(R.id.ll_ower_lianxi2);
        llOwerLianxiNumber = getView().findViewById(R.id.ll_ower_lianxi_number);
        llOwerLianxiLink = getView().findViewById(R.id.ll_ower_lianxi_link);
        llOwerPinshen = getView().findViewById(R.id.ll_ower_pinshen);
        llOwerA = getView().findViewById(R.id.ll_ower_a);
        llOwerB = getView().findViewById(R.id.ll_ower_b);
        llOwerC = getView().findViewById(R.id.ll_ower_c);
        llOwerD = getView().findViewById(R.id.ll_ower_d);
        llOwerE = getView().findViewById(R.id.ll_ower_e);
        llOwerF = getView().findViewById(R.id.ll_ower_f);
        llOwerG = getView().findViewById(R.id.ll_ower_g);
        llShare = getView().findViewById(R.id.ll_share);
        llBucai = getView().findViewById(R.id.ll_bucai);
        tvBucai = getView().findViewById(R.id.tv_bucai);

        tvGz = getView().findViewById(R.id.tv_gzgg);
        tvJg = getView().findViewById(R.id.tv_jggg);
        tvGz.setOnClickListener(this);
        tvJg.setOnClickListener(this);

        llIvBack.setOnClickListener(this);
        llFav.setOnClickListener(this);
        llShare.setOnClickListener(this);

        llWeiBoShare_bottom = getView().findViewById(R.id.ll_weibo_share_bottom);
        llQQBoShare_bottom = getView().findViewById(R.id.ll_qq_share_bottom);
        llWeixinBoShare_bottom = getView().findViewById(R.id.ll_chat_share_bottom);
        llPyqShare_bottom = getView().findViewById(R.id.ll_pyq_share_bottom);

        llWeiBoShare_bottom.setOnClickListener(this);
        llQQBoShare_bottom.setOnClickListener(this);
        llWeixinBoShare_bottom.setOnClickListener(this);
        llPyqShare_bottom.setOnClickListener(this);




    }

    @Override
    public void initData() {
        llIvBack.setVisibility(View.VISIBLE);
        mainBarName.setText("标讯详情");
        xcgggDetailStatusView.setOnRetryClickListener(mRetryClickListener);
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
            xcgggDetailStatusView.showNoNetwork();
        } else {
            xcgggDetailStatusView.showLoading();

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
                                    xcgggDetailStatusView.showContent();
                                    final JSONObject data = object.getJSONObject("data");

                                    //判断是否有更正和结果
                                    //1、判断有误更正
                                    final JSONArray arrayGz = data.getJSONArray("listGzUrl");
                                    //2、判断有无结果
                                    final JSONArray arrayJg = data.getJSONArray("listJgId");
                                    if (arrayGz != null) {
                                        gzUrl = arrayGz.getString(arrayGz.size() - 1);
                                    } else {
                                        tvGz.setText("无");
                                        tvGz.setTextColor(getResources().getColor(R.color.main_text_color));
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
                                    String administrativeDivision = data.getString("administrativeAddress");
                                    if (!TextUtils.isEmpty(administrativeDivision)) {
                                        tvMainArea.setText(administrativeDivision);
                                    } else {
                                        tvMainArea.setText("/");
                                    }
                                    String purchasingType = data.getString("purchasingType");
                                    shareContent = purchasingType;
                                    if (!TextUtils.isEmpty(purchasingType)) {
                                        tvMainCaigouMethod.setText(purchasingType);
                                    } else {
                                        tvMainCaigouMethod.setText("/");
                                        llMainCaigouMethod.setVisibility(View.GONE);
                                    }
                                    String noticeTime = data.getString("noticeTime");
                                    if (!TextUtils.isEmpty(noticeTime)) {
                                        tvMainPubTime.setText(noticeTime.substring(0, 10));
                                    } else {
                                        tvMainPubTime.setText("/");
                                        llMainPubTime.setVisibility(View.GONE);
                                    }
                                    String entryNum = data.getString("entryNum");
                                    if (!TextUtils.isEmpty(entryNum)) {
                                        tvPubNum.setText(entryNum);
                                    } else {
                                        tvPubNum.setText("/");
                                        llPubNum.setVisibility(View.GONE);
                                    }
                                    String entryName = data.getString("entryName");
                                    if (!TextUtils.isEmpty(entryName)) {
                                        tvOwerCainame.setText(entryName);
                                    } else {
                                        tvOwerCainame.setText("/");
                                        llOwerCainame.setVisibility(View.GONE);
                                    }
                                    String purchaser = data.getString("purchaser");
                                    if (!TextUtils.isEmpty(purchaser)) {
                                        tvOwerName.setText(purchaser);
                                    } else {
                                        tvOwerName.setText("/");
                                        llOwerName.setVisibility(View.GONE);
                                    }
                                    String purchasingAgent = data.getString("purchasingAgent");
                                    if (!TextUtils.isEmpty(purchasingAgent)) {
                                        tvOwerDaili.setText(purchasingAgent);
                                    } else {
                                        tvOwerDaili.setText("/");
                                        llOwerDaili.setVisibility(View.GONE);
                                    }
                                    String noticeCount = data.getString("noticeCount");
                                    if (!TextUtils.isEmpty(noticeCount)) {
                                        tvOwerBaoshu.setText(noticeCount);
                                    } else {
                                        tvOwerBaoshu.setText("/");
                                        llOwerBaoshu.setVisibility(View.GONE);
                                    }
                                    final String descriptionPackages = data.getString("descriptionPackages");
                                    if (!TextUtils.isEmpty(descriptionPackages)) {
                                        tvOwerJine.setText("点击查看或下载附件");

                                        llOwerJine.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                AppConfig.openExternalBrowser(getContext(), descriptionPackages);
                                            }
                                        });
                                    } else {
                                        tvOwerJine.setText("不详");
                                        llOwerJine.setVisibility(View.GONE);
                                    }
                                    String qualificationsMaterials = data.getString("qualificationsMaterials");
                                    if (!TextUtils.isEmpty(qualificationsMaterials)) {
                                        tvOwerBaojia.setText(qualificationsMaterials);
                                    } else {
                                        tvOwerBaojia.setText("/");
                                        llOwerBaojia.setVisibility(View.GONE);
                                    }
                                    String offeringMethod = data.getString("offeringMethod");
                                    if (!TextUtils.isEmpty(offeringMethod)) {
                                        tvOwerMingdan.setText(offeringMethod);
                                    } else {
                                        tvOwerMingdan.setText("/");
                                        llOwerMingdan.setVisibility(View.GONE);
                                    }
                                    String signTime = data.getString("signTime");
                                    if (!TextUtils.isEmpty(signTime)) {
                                        tvOwerLianxi.setText(signTime);
                                    } else {
                                        tvOwerLianxi.setText("/");
                                        llOwerLianxi.setVisibility(View.GONE);
                                    }
                                    String signAddress = data.getString("signAddress");
                                    if (!TextUtils.isEmpty(signAddress)) {
                                        tvOwerLianxi2.setText(signAddress);
                                    } else {
                                        tvOwerLianxi2.setText("/");
                                        llOwerLianxi2.setVisibility(View.GONE);
                                    }
                                    String price = data.getString("price");
                                    if (!TextUtils.isEmpty(price)) {
                                        tvOwerLianxiNumber.setText(price);
                                    } else {
                                        tvOwerLianxiNumber.setText("/");
                                        llOwerLianxiNumber.setVisibility(View.GONE);
                                    }
                                    String saleAddress = data.getString("saleAddress");
                                    if (!TextUtils.isEmpty(saleAddress)) {
                                        tvOwerLianxiLink.setText(saleAddress);
                                    } else {
                                        tvOwerLianxiLink.setText("/");
                                        llOwerLianxiLink.setVisibility(View.GONE);
                                    }
                                    String signType = data.getString("signType");
                                    if (!TextUtils.isEmpty(signType)) {
                                        tvOwerPinshen.setText(signType);
                                    } else {
                                        tvOwerPinshen.setText("/");
                                        llOwerPinshen.setVisibility(View.GONE);
                                    }
                                    String startStopTime = data.getString("startStopTime");
                                    if (!TextUtils.isEmpty(startStopTime)) {
                                        tvOwerA.setText(startStopTime);
                                    } else {
                                        tvOwerA.setText("/");
                                        llOwerA.setVisibility(View.GONE);
                                    }
                                    String fileAddress = data.getString("fileAddress");
                                    if (!TextUtils.isEmpty(fileAddress)) {
                                        tvOwerB.setText(fileAddress);
                                    } else {
                                        tvOwerB.setText("/");
                                        llOwerB.setVisibility(View.GONE);
                                    }
                                    String inquiryTime = data.getString("inquiryTime");
                                    if (!TextUtils.isEmpty(inquiryTime)) {
                                        tvOwerC.setText(inquiryTime);
                                    } else {
                                        tvOwerC.setText("/");
                                        llOwerC.setVisibility(View.GONE);
                                    }
                                    String payAndtype = data.getString("payAndtype");
                                    if (!TextUtils.isEmpty(payAndtype)) {
                                        tvOwerD.setText(payAndtype);
                                    } else {
                                        tvOwerD.setText("/");
                                        llOwerD.setVisibility(View.GONE);
                                    }
                                    String purchaserContact = data.getString("purchaserContact");
                                    if (!TextUtils.isEmpty(purchaserContact)) {
                                        tvOwerE.setText(purchaserContact);
                                    } else {
                                        tvOwerE.setText("/");
                                        llOwerE.setVisibility(View.GONE);
                                    }
                                    String purchasingAgentContact = data.getString("purchasingAgentContact");
                                    if (!TextUtils.isEmpty(purchasingAgentContact)) {
                                        tvOwerF.setText(purchasingAgentContact);
                                    } else {
                                        tvOwerF.setText("/");
                                        llOwerF.setVisibility(View.GONE);
                                    }
                                    String nameAndphone = data.getString("nameAndphone");
                                    if (!TextUtils.isEmpty(nameAndphone)) {
                                        tvOwerG.setText(nameAndphone);
                                    } else {
                                        tvOwerG.setText("/");
                                        llOwerG.setVisibility(View.GONE);
                                    }

                                    String specialFields = data.getString("specialFields");
                                    if (!TextUtils.isEmpty(specialFields)) {
                                        llBucai.setVisibility(View.VISIBLE);
                                        String s = specialFields.replace("*", "").replace("</", "\n").replace("<", "\n\n");
                                        tvBucai.setText(s);
                                    } else {
                                        llBucai.setVisibility(View.GONE);
                                    }

                                } else {
                                    xcgggDetailStatusView.showError();
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
                                    xcgggDetailStatusView.showContent();
                                    final JSONObject data = object.getJSONObject("data");

                                    //判断是否有更正和结果
                                    //1、判断有误更正
                                    final JSONArray arrayGz = data.getJSONArray("listGzUrl");
                                    //2、判断有无结果
                                    final JSONArray arrayJg = data.getJSONArray("listJgId");
                                    if (arrayGz != null) {
                                        gzUrl = arrayGz.getString(arrayGz.size() - 1);
                                    } else {
                                        tvGz.setText("无");
                                        tvGz.setTextColor(getResources().getColor(R.color.main_text_color));
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
                                    String administrativeDivision = data.getString("administrativeAddress");
                                    if (!TextUtils.isEmpty(administrativeDivision)) {
                                        tvMainArea.setText(administrativeDivision);
                                    } else {
                                        tvMainArea.setText("/");
                                    }
                                    String purchasingType = data.getString("purchasingType");
                                    shareContent = purchasingType;
                                    if (!TextUtils.isEmpty(purchasingType)) {
                                        tvMainCaigouMethod.setText(purchasingType);
                                    } else {
                                        tvMainCaigouMethod.setText("/");
                                        llMainCaigouMethod.setVisibility(View.GONE);
                                    }
                                    String noticeTime = data.getString("noticeTime");
                                    if (!TextUtils.isEmpty(noticeTime)) {
                                        tvMainPubTime.setText(noticeTime.substring(0, 10));
                                    } else {
                                        tvMainPubTime.setText("/");
                                        llMainPubTime.setVisibility(View.GONE);
                                    }
                                    String entryNum = data.getString("entryNum");
                                    if (!TextUtils.isEmpty(entryNum)) {
                                        tvPubNum.setText(entryNum);
                                    } else {
                                        tvPubNum.setText("/");
                                        llPubNum.setVisibility(View.GONE);
                                    }
                                    String entryName = data.getString("entryName");
                                    if (!TextUtils.isEmpty(entryName)) {
                                        tvOwerCainame.setText(entryName);
                                    } else {
                                        tvOwerCainame.setText("/");
                                        llOwerCainame.setVisibility(View.GONE);
                                    }
                                    String purchaser = data.getString("purchaser");
                                    if (!TextUtils.isEmpty(purchaser)) {
                                        tvOwerName.setText(purchaser);
                                    } else {
                                        tvOwerName.setText("/");
                                        llOwerName.setVisibility(View.GONE);
                                    }
                                    String purchasingAgent = data.getString("purchasingAgent");
                                    if (!TextUtils.isEmpty(purchasingAgent)) {
                                        tvOwerDaili.setText(purchasingAgent);
                                    } else {
                                        tvOwerDaili.setText("/");
                                        llOwerDaili.setVisibility(View.GONE);
                                    }
                                    String noticeCount = data.getString("noticeCount");
                                    if (!TextUtils.isEmpty(noticeCount)) {
                                        tvOwerBaoshu.setText(noticeCount);
                                    } else {
                                        tvOwerBaoshu.setText("/");
                                        llOwerBaoshu.setVisibility(View.GONE);
                                    }
                                    final String descriptionPackages = data.getString("descriptionPackages");
                                    if (!TextUtils.isEmpty(descriptionPackages)) {
                                        tvOwerJine.setText("点击查看或下载附件");

                                        llOwerJine.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                AppConfig.openExternalBrowser(getContext(), descriptionPackages);
                                            }
                                        });
                                    } else {
                                        tvOwerJine.setText("不详");
                                        llOwerJine.setVisibility(View.GONE);
                                    }
                                    String qualificationsMaterials = data.getString("qualificationsMaterials");
                                    if (!TextUtils.isEmpty(qualificationsMaterials)) {
                                        tvOwerBaojia.setText(qualificationsMaterials);
                                    } else {
                                        tvOwerBaojia.setText("/");
                                        llOwerBaojia.setVisibility(View.GONE);
                                    }
                                    String offeringMethod = data.getString("offeringMethod");
                                    if (!TextUtils.isEmpty(offeringMethod)) {
                                        tvOwerMingdan.setText(offeringMethod);
                                    } else {
                                        tvOwerMingdan.setText("/");
                                        llOwerMingdan.setVisibility(View.GONE);
                                    }
                                    String signTime = data.getString("signTime");
                                    if (!TextUtils.isEmpty(signTime)) {
                                        tvOwerLianxi.setText(signTime);
                                    } else {
                                        tvOwerLianxi.setText("/");
                                        llOwerLianxi.setVisibility(View.GONE);
                                    }
                                    String signAddress = data.getString("signAddress");
                                    if (!TextUtils.isEmpty(signAddress)) {
                                        tvOwerLianxi2.setText(signAddress);
                                    } else {
                                        tvOwerLianxi2.setText("/");
                                        llOwerLianxi2.setVisibility(View.GONE);
                                    }
                                    String price = data.getString("price");
                                    if (!TextUtils.isEmpty(price)) {
                                        tvOwerLianxiNumber.setText(price);
                                    } else {
                                        tvOwerLianxiNumber.setText("/");
                                        llOwerLianxiNumber.setVisibility(View.GONE);
                                    }
                                    String saleAddress = data.getString("saleAddress");
                                    if (!TextUtils.isEmpty(saleAddress)) {
                                        tvOwerLianxiLink.setText(saleAddress);
                                    } else {
                                        tvOwerLianxiLink.setText("/");
                                        llOwerLianxiLink.setVisibility(View.GONE);
                                    }
                                    String signType = data.getString("signType");
                                    if (!TextUtils.isEmpty(signType)) {
                                        tvOwerPinshen.setText(signType);
                                    } else {
                                        tvOwerPinshen.setText("/");
                                        llOwerPinshen.setVisibility(View.GONE);
                                    }
                                    String startStopTime = data.getString("startStopTime");
                                    if (!TextUtils.isEmpty(startStopTime)) {
                                        tvOwerA.setText(startStopTime);
                                    } else {
                                        tvOwerA.setText("/");
                                        llOwerA.setVisibility(View.GONE);
                                    }
                                    String fileAddress = data.getString("fileAddress");
                                    if (!TextUtils.isEmpty(fileAddress)) {
                                        tvOwerB.setText(fileAddress);
                                    } else {
                                        tvOwerB.setText("/");
                                        llOwerB.setVisibility(View.GONE);
                                    }
                                    String inquiryTime = data.getString("inquiryTime");
                                    if (!TextUtils.isEmpty(inquiryTime)) {
                                        tvOwerC.setText(inquiryTime);
                                    } else {
                                        tvOwerC.setText("/");
                                        llOwerC.setVisibility(View.GONE);
                                    }
                                    String payAndtype = data.getString("payAndtype");
                                    if (!TextUtils.isEmpty(payAndtype)) {
                                        tvOwerD.setText(payAndtype);
                                    } else {
                                        tvOwerD.setText("/");
                                        llOwerD.setVisibility(View.GONE);
                                    }
                                    String purchaserContact = data.getString("purchaserContact");
                                    if (!TextUtils.isEmpty(purchaserContact)) {
                                        tvOwerE.setText(purchaserContact);
                                    } else {
                                        tvOwerE.setText("/");
                                        llOwerE.setVisibility(View.GONE);
                                    }
                                    String purchasingAgentContact = data.getString("purchasingAgentContact");
                                    if (!TextUtils.isEmpty(purchasingAgentContact)) {
                                        tvOwerF.setText(purchasingAgentContact);
                                    } else {
                                        tvOwerF.setText("/");
                                        llOwerF.setVisibility(View.GONE);
                                    }
                                    String nameAndphone = data.getString("nameAndphone");
                                    if (!TextUtils.isEmpty(nameAndphone)) {
                                        tvOwerG.setText(nameAndphone);
                                    } else {
                                        tvOwerG.setText("/");
                                        llOwerG.setVisibility(View.GONE);
                                    }
                                    String specialFields = data.getString("specialFields");
                                    if (!TextUtils.isEmpty(specialFields)) {
                                        llBucai.setVisibility(View.VISIBLE);
                                        String s = specialFields.replace("*", "").replace("</", "\n").replace("<", "\n\n");
                                        tvBucai.setText(s);
                                    } else {
                                        llBucai.setVisibility(View.GONE);
                                    }

                                } else {
                                    xcgggDetailStatusView.showError();
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
        mShare.setContent(shareContent);
        mShare.setSummary(shareContent);
        mShare.setDescription(shareContent);
        mShare.setImageUrl(null);
        mShare.setUrl(BiaoXunTongApi.SHARE_URL + shareUrl);
        Intent intent = null;

        switch (view.getId()) {

            case R.id.tv_gzgg:
                if (!tvGz.getText().toString().equals("无")){
                    ARouter.getInstance().build("/com/BrowserSuitActivity").withString("mUrl",shareUrl).withString("mTitle","更正公告").navigation();
                }
                break;
            case R.id.tv_jggg:
                Log.d("HIUASDSABDBSADA", "哈哈哈为：" + jgEntity + "___" + jgEntityId);
                if ("xjggg".equals(jgEntity) || "sjggg".equals(jgEntity) || "sggjy".equals(jgEntity) || "sggjycgjgtable".equals(jgEntity)) {
                    intent = new Intent(getActivity(), ResultXjgggDetailActivity.class);
                    intent.putExtra("entityId", Integer.valueOf(jgEntityId));
                    intent.putExtra("entity", jgEntity);
                    intent.putExtra("ajaxlogtype", "0");
                    intent.putExtra("mId", "");
                    startActivity(intent);

                } else if ("sggjyzbjg".equals(jgEntity) || "sggjycgjgrow".equals(jgEntity) || "sggjyjgcgtable".equals(jgEntity)) {
                    intent = new Intent(getActivity(), ResultSggjyzbjgDetailActivity.class);
                    intent.putExtra("entityId", Integer.valueOf(jgEntityId));
                    intent.putExtra("entity", jgEntity);
                    intent.putExtra("ajaxlogtype", "0");
                    intent.putExtra("mId", "");
                    startActivity(intent);
                }
                break;
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
            case R.id.ll_share:
                toShare(mEntityId, shareTitle, shareContent, BiaoXunTongApi.SHARE_URL + shareUrl);
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
