package com.lubanjianye.biaoxuntong.ui.main.result.detail.sichuan;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatTextView;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.alibaba.android.arouter.launcher.ARouter;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.classic.common.MultipleStatusView;
import com.lubanjianye.biaoxuntong.R;
import com.lubanjianye.biaoxuntong.app.BiaoXunTong;
import com.lubanjianye.biaoxuntong.base.BaseFragment1;
import com.lubanjianye.biaoxuntong.database.DatabaseManager;
import com.lubanjianye.biaoxuntong.database.UserProfile;
import com.lubanjianye.biaoxuntong.eventbus.EventMessage;
import com.lubanjianye.biaoxuntong.app.BiaoXunTongApi;
import com.lubanjianye.biaoxuntong.ui.main.index.detail.sichuan.IndexScgggDetailActivity;
import com.lubanjianye.biaoxuntong.ui.main.index.detail.sichuan.IndexSggjyDetailActivity;
import com.lubanjianye.biaoxuntong.ui.main.index.detail.sichuan.IndexSggjycgrowDetailActivity;
import com.lubanjianye.biaoxuntong.ui.main.index.detail.sichuan.IndexSggjycgtableDetailActivity;
import com.lubanjianye.biaoxuntong.ui.main.index.detail.sichuan.IndexXcgggDetailActivity;
import com.lubanjianye.biaoxuntong.ui.sign.SignInActivity;
import com.lubanjianye.biaoxuntong.ui.main.index.detail.sichuan.IndexBxtgdjDetailActivity;
import com.lubanjianye.biaoxuntong.ui.share.OpenBuilder;
import com.lubanjianye.biaoxuntong.ui.share.OpenConstant;
import com.lubanjianye.biaoxuntong.ui.share.Share;
import com.lubanjianye.biaoxuntong.util.AppConfig;
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


public class ResultXjgggDetailFragment extends BaseFragment1 implements View.OnClickListener, OpenBuilder.Callback {

    private LinearLayout llIvBack = null;
    private AppCompatTextView mainBarName = null;
    private MultipleStatusView xjgggDetailStatusView = null;
    private AppCompatTextView tvMainTitle = null;
    private AppCompatTextView tvMainArea = null;
    private AppCompatTextView tvMainPubMethod = null;
    private AppCompatTextView tvMainPubData = null;
    private AppCompatTextView tvMainPubTime = null;
    private AppCompatTextView tvPuNum = null;
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
    private ImageView ivFav = null;
    private LinearLayout llFav = null;
    private LinearLayout llShare = null;
    private LinearLayout llType = null;
    private LinearLayout llBucai = null;
    private AppCompatTextView tvBucai = null;




    private LinearLayout llWeiBoShare_bottom = null;
    private LinearLayout llQQBoShare_bottom = null;
    private LinearLayout llWeixinBoShare_bottom = null;
    private LinearLayout llPyqShare_bottom = null;

    private AppCompatTextView tvGg = null;

    private static final String ARG_ENTITYID = "ARG_ENTITYID";
    private static final String ARG_ENTITY = "ARG_ENTITY";
    private static final String ARG_AJAXTYPE = "ARG_AJAXTYPE";

    private String ggEntity = "";
    private String ggEntityId = "";

    private int myFav = -1;
    private int mEntityId = -1;
    private String mEntity = "";
    private String shareTitle = "";
    private String shareContent = "";
    private String shareUrl = "";
    private String deviceId = AppSysMgr.getPsuedoUniqueID();
    private String ajaxType = "0";
    private String ywUrl = "";


    @Override
    public Object setLayout() {
        return R.layout.fragment_result_xjggg_detail;
    }


    public static ResultXjgggDetailFragment create(@NonNull int entityId, String entity, String ajaxlogtype) {
        final Bundle args = new Bundle();
        args.putInt(ARG_ENTITYID, entityId);
        args.putString(ARG_ENTITY, entity);
        args.putString(ARG_AJAXTYPE, ajaxlogtype);
        final ResultXjgggDetailFragment fragment = new ResultXjgggDetailFragment();
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
        xjgggDetailStatusView = getView().findViewById(R.id.xjggg_detail_status_view);
        tvMainTitle = getView().findViewById(R.id.tv_main_title);
        tvMainArea = getView().findViewById(R.id.tv_main_area);
        tvMainPubMethod = getView().findViewById(R.id.tv_main_pub_method);
        tvMainPubData = getView().findViewById(R.id.tv_main_pub_data);
        tvMainPubTime = getView().findViewById(R.id.tv_main_pub_time);
        tvPuNum = getView().findViewById(R.id.tv_pu_num);
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
        ivFav = getView().findViewById(R.id.iv_fav);
        llFav = getView().findViewById(R.id.ll_fav);
        llShare = getView().findViewById(R.id.ll_share);
        llBucai = getView().findViewById(R.id.ll_bucai);
        tvBucai = getView().findViewById(R.id.tv_bucai);
        llType = getView().findViewById(R.id.ll_type);


        tvGg = getView().findViewById(R.id.tv_data_gg);
        tvGg.setOnClickListener(this);

        llWeiBoShare_bottom = getView().findViewById(R.id.ll_weibo_share_bottom);
        llQQBoShare_bottom = getView().findViewById(R.id.ll_qq_share_bottom);
        llWeixinBoShare_bottom = getView().findViewById(R.id.ll_chat_share_bottom);
        llPyqShare_bottom = getView().findViewById(R.id.ll_pyq_share_bottom);


        llWeiBoShare_bottom.setOnClickListener(this);
        llQQBoShare_bottom.setOnClickListener(this);
        llWeixinBoShare_bottom.setOnClickListener(this);
        llPyqShare_bottom.setOnClickListener(this);

        llIvBack.setOnClickListener(this);
        llShare.setOnClickListener(this);
        llFav.setOnClickListener(this);


    }

    @Override
    public void initData() {
        llIvBack.setVisibility(View.VISIBLE);
        mainBarName.setText("政府采购结果公告详情");
        xjgggDetailStatusView.setOnRetryClickListener(mRetryClickListener);
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

    private void requestData() {

        if ("1".equals(ajaxType)) {
            //改变已读未读状态
            EventBus.getDefault().post(new EventMessage(EventMessage.READ_STATUS));
        }

        if (!NetUtil.isNetworkConnected(getActivity())) {
            xjgggDetailStatusView.showNoNetwork();
        } else {
            xjgggDetailStatusView.showLoading();

            if (AppSharePreferenceMgr.contains(getContext(), EventMessage.LOGIN_SUCCSS)) {
                long id = 0;
                //已登录时的数据请求
                List<UserProfile> users = DatabaseManager.getInstance().getDao().loadAll();
                for (int i = 0; i < users.size(); i++) {
                    id = users.get(0).getId();
                }
                OkGo.<String>post(BiaoXunTongApi.URL_GETRESULTLISTDETAIL)
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
                                    xjgggDetailStatusView.showContent();
                                    final JSONObject data = object.getJSONObject("data");
                                    //判断有无相关公告
                                    final JSONObject arrayGg = data.getJSONObject("ggId");
                                    if (arrayGg != null) {
                                        ggEntity = arrayGg.getString("entity");
                                        ggEntityId = arrayGg.getString("entityId");
                                        if (TextUtils.isEmpty(ggEntity)) {
                                            tvGg.setText("无");
                                            tvGg.setTextColor(getResources().getColor(R.color.main_text_color));
                                        }
                                    }

                                    String reportTitle = data.getString("reportTitle");
                                    shareTitle = reportTitle;
                                    if (!TextUtils.isEmpty(reportTitle)) {
                                        tvMainTitle.setText(reportTitle);
                                    } else {
                                        tvMainTitle.setText("暂无");
                                        mainBarName.setText("政府采购结果公告详情");
                                    }
                                    String area = data.getString("administrativeDivision");
                                    if (!TextUtils.isEmpty(area)) {
                                        tvMainArea.setVisibility(View.VISIBLE);
                                        tvMainArea.setText(area);
                                    } else {
                                        tvMainArea.setText("暂无");
                                    }
                                    String purchasingType = data.getString("purchasingType");
                                    if (!TextUtils.isEmpty(purchasingType)) {
                                        tvMainPubMethod.setText(purchasingType);
                                    } else {
                                        tvMainPubMethod.setText("暂无");
                                        llType.setVisibility(View.GONE);
                                    }
                                    String calibrationTime = data.getString("calibrationTime");
                                    if (!TextUtils.isEmpty(calibrationTime)) {
                                        tvMainPubData.setText(calibrationTime.substring(0, 10));
                                    } else {
                                        tvMainPubData.setText("暂无");
                                    }
                                    String noticeTime = data.getString("noticeTime");
                                    if (!TextUtils.isEmpty(noticeTime)) {
                                        tvMainPubTime.setText(noticeTime.substring(0, 10));
                                    } else {
                                        tvMainPubTime.setText("暂无");
                                    }
                                    String entryNum = data.getString("entryNum");
                                    if (!TextUtils.isEmpty(entryNum)) {
                                        tvPuNum.setText(entryNum);
                                    } else {
                                        tvPuNum.setText("暂无");
                                    }
                                    String entryName = data.getString("entryName");
                                    shareContent = entryName;
                                    if (!TextUtils.isEmpty(entryName)) {
                                        tvOwerCainame.setText(entryName);
                                    } else {
                                        tvOwerCainame.setText("暂无");
                                    }
                                    String purchaser = data.getString("purchaser");
                                    if (!TextUtils.isEmpty(purchaser)) {
                                        tvOwerName.setText(purchaser);
                                    } else {
                                        tvOwerName.setText("暂无");
                                    }
                                    String purchasingAgent = data.getString("purchasingAgent");
                                    if (!TextUtils.isEmpty(purchasingAgent)) {
                                        tvOwerDaili.setText(purchasingAgent);
                                    } else {
                                        tvOwerDaili.setText("暂无");
                                    }
                                    String noticeCount = data.getString("noticeCount");
                                    if (!TextUtils.isEmpty(noticeCount)) {
                                        tvOwerBaoshu.setText(noticeCount);
                                    } else {
                                        tvOwerBaoshu.setText("暂无");
                                    }
                                    String allTotal = data.getString("allTotal");
                                    if (!TextUtils.isEmpty(allTotal)) {
                                        tvOwerJine.setText(allTotal);
                                    } else {
                                        tvOwerJine.setText("暂无");
                                    }
                                    String eachPackage = data.getString("eachPackage");
                                    if (!TextUtils.isEmpty(eachPackage)) {
                                        tvOwerBaojia.setText(eachPackage);
                                    } else {
                                        tvOwerBaojia.setText("暂无");
                                    }
                                    String memberList = data.getString("memberList");
                                    if (!TextUtils.isEmpty(memberList)) {
                                        tvOwerMingdan.setText(memberList);
                                    } else {
                                        tvOwerMingdan.setText("暂无");
                                    }
                                    String purchaserContact = data.getString("purchaserContact");
                                    if (!TextUtils.isEmpty(purchaserContact)) {
                                        tvOwerLianxi.setText(purchaserContact);
                                    } else {
                                        tvOwerLianxi.setText("暂无");
                                    }
                                    String purchasingAgentContact = data.getString("purchasingAgentContact");
                                    if (!TextUtils.isEmpty(purchasingAgentContact)) {
                                        tvOwerLianxi2.setText(purchasingAgentContact);
                                    } else {
                                        tvOwerLianxi2.setText("暂无");
                                    }
                                    String nameAndphone = data.getString("nameAndphone");
                                    if (!TextUtils.isEmpty(nameAndphone)) {
                                        tvOwerLianxiNumber.setText(nameAndphone);
                                    } else {
                                        tvOwerLianxiNumber.setText("暂无");
                                    }
                                    final String link = data.getString("link");
                                    final String url = data.getString("url");
                                    ywUrl = url;
                                    shareUrl = link;
                                    if (!TextUtils.isEmpty(link) && !"/".equals(link)) {
                                        tvOwerLianxiLink.setText("点击查看");
                                        tvOwerLianxiLink.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                ARouter.getInstance().build("/com/BrowserSuitActivity").withString("mUrl",link).withString("mTitle",shareTitle).navigation();
                                            }
                                        });
                                    } else {
                                        tvOwerLianxiLink.setText("暂无");
                                    }
                                    final String reviewSituation = data.getString("reviewSituation");

                                    if (!TextUtils.isEmpty(reviewSituation) && !"/".equals(reviewSituation)) {
                                        tvOwerPinshen.setText("点击查看或下载附件");
                                        tvOwerPinshen.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                AppConfig.openExternalBrowser(getContext(), reviewSituation);
                                            }
                                        });

                                    } else {
                                        tvOwerPinshen.setText("暂无");
                                    }
                                    String specialFields = data.getString("specialFields");
                                    if (!TextUtils.isEmpty(specialFields)) {
                                        llBucai.setVisibility(View.VISIBLE);
                                        String s = specialFields.replace("*", "").replace("</", "\n").replace("<", "\n\n");
                                        if (!TextUtils.isEmpty(s)) {
                                            llBucai.setVisibility(View.VISIBLE);
                                            tvBucai.setText(s);
                                        } else {
                                            llBucai.setVisibility(View.GONE);
                                        }
                                    } else {
                                        llBucai.setVisibility(View.GONE);
                                    }
                                } else {
                                    xjgggDetailStatusView.showError();
                                }
                            }
                        });

            } else {
                //未登录时的数据请求
                OkGo.<String>post(BiaoXunTongApi.URL_GETRESULTLISTDETAIL)
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
                                    xjgggDetailStatusView.showContent();
                                    final JSONObject data = object.getJSONObject("data");
                                    //判断有无相关公告
                                    final JSONObject arrayGg = data.getJSONObject("ggId");
                                    if (arrayGg != null) {
                                        ggEntity = arrayGg.getString("entity");
                                        ggEntityId = arrayGg.getString("entityId");
                                        if (TextUtils.isEmpty(ggEntity)) {
                                            tvGg.setText("无");
                                            tvGg.setTextColor(getResources().getColor(R.color.main_text_color));
                                        }
                                    }

                                    String reportTitle = data.getString("reportTitle");
                                    shareTitle = reportTitle;
                                    if (!TextUtils.isEmpty(reportTitle)) {
                                        tvMainTitle.setText(reportTitle);
                                    } else {
                                        tvMainTitle.setText("暂无");
                                        mainBarName.setText("政府采购结果公告详情");
                                    }
                                    String area = data.getString("administrativeDivision");
                                    if (!TextUtils.isEmpty(area)) {
                                        tvMainArea.setVisibility(View.VISIBLE);
                                        tvMainArea.setText(area);
                                    } else {
                                        tvMainArea.setText("暂无");
                                    }
                                    String purchasingType = data.getString("purchasingType");
                                    if (!TextUtils.isEmpty(purchasingType)) {
                                        tvMainPubMethod.setText(purchasingType);
                                    } else {
                                        tvMainPubMethod.setText("暂无");
                                        llType.setVisibility(View.GONE);
                                    }
                                    String calibrationTime = data.getString("calibrationTime");
                                    if (!TextUtils.isEmpty(calibrationTime)) {
                                        tvMainPubData.setText(calibrationTime.substring(0, 10));
                                    } else {
                                        tvMainPubData.setText("暂无");
                                    }
                                    String noticeTime = data.getString("noticeTime");
                                    if (!TextUtils.isEmpty(noticeTime)) {
                                        tvMainPubTime.setText(noticeTime.substring(0, 10));
                                    } else {
                                        tvMainPubTime.setText("暂无");
                                    }
                                    String entryNum = data.getString("entryNum");
                                    if (!TextUtils.isEmpty(entryNum)) {
                                        tvPuNum.setText(entryNum);
                                    } else {
                                        tvPuNum.setText("暂无");
                                    }
                                    String entryName = data.getString("entryName");
                                    shareContent = entryName;
                                    if (!TextUtils.isEmpty(entryName)) {
                                        tvOwerCainame.setText(entryName);
                                    } else {
                                        tvOwerCainame.setText("暂无");
                                    }
                                    String purchaser = data.getString("purchaser");
                                    if (!TextUtils.isEmpty(purchaser)) {
                                        tvOwerName.setText(purchaser);
                                    } else {
                                        tvOwerName.setText("暂无");
                                    }
                                    String purchasingAgent = data.getString("purchasingAgent");
                                    if (!TextUtils.isEmpty(purchasingAgent)) {
                                        tvOwerDaili.setText(purchasingAgent);
                                    } else {
                                        tvOwerDaili.setText("暂无");
                                    }
                                    String noticeCount = data.getString("noticeCount");
                                    if (!TextUtils.isEmpty(noticeCount)) {
                                        tvOwerBaoshu.setText(noticeCount);
                                    } else {
                                        tvOwerBaoshu.setText("暂无");
                                    }
                                    String allTotal = data.getString("allTotal");
                                    if (!TextUtils.isEmpty(allTotal)) {
                                        tvOwerJine.setText(allTotal);
                                    } else {
                                        tvOwerJine.setText("暂无");
                                    }
                                    String eachPackage = data.getString("eachPackage");
                                    if (!TextUtils.isEmpty(eachPackage)) {
                                        tvOwerBaojia.setText(eachPackage);
                                    } else {
                                        tvOwerBaojia.setText("暂无");
                                    }
                                    String memberList = data.getString("memberList");
                                    if (!TextUtils.isEmpty(memberList)) {
                                        tvOwerMingdan.setText(memberList);
                                    } else {
                                        tvOwerMingdan.setText("暂无");
                                    }
                                    String purchaserContact = data.getString("purchaserContact");
                                    if (!TextUtils.isEmpty(purchaserContact)) {
                                        tvOwerLianxi.setText(purchaserContact);
                                    } else {
                                        tvOwerLianxi.setText("暂无");
                                    }
                                    String purchasingAgentContact = data.getString("purchasingAgentContact");
                                    if (!TextUtils.isEmpty(purchasingAgentContact)) {
                                        tvOwerLianxi2.setText(purchasingAgentContact);
                                    } else {
                                        tvOwerLianxi2.setText("暂无");
                                    }
                                    String nameAndphone = data.getString("nameAndphone");
                                    if (!TextUtils.isEmpty(nameAndphone)) {
                                        tvOwerLianxiNumber.setText(nameAndphone);
                                    } else {
                                        tvOwerLianxiNumber.setText("暂无");
                                    }
                                    final String link = data.getString("link");
                                    final String url = data.getString("url");
                                    ywUrl = url;
                                    shareUrl = link;
                                    if (!TextUtils.isEmpty(link) && !"/".equals(link)) {
                                        tvOwerLianxiLink.setText("点击查看");
                                        tvOwerLianxiLink.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                ARouter.getInstance().build("/com/BrowserSuitActivity").withString("mUrl",link).withString("mTitle",shareTitle).navigation();
                                            }
                                        });
                                    } else {
                                        tvOwerLianxiLink.setText("暂无");
                                    }
                                    final String reviewSituation = data.getString("reviewSituation");

                                    if (!TextUtils.isEmpty(reviewSituation) && !"/".equals(reviewSituation)) {
                                        tvOwerPinshen.setText("点击查看或下载附件");
                                        tvOwerPinshen.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                AppConfig.openExternalBrowser(getContext(), reviewSituation);
                                            }
                                        });

                                    } else {
                                        tvOwerPinshen.setText("暂无");
                                    }
                                    String specialFields = data.getString("specialFields");
                                    if (!TextUtils.isEmpty(specialFields)) {
                                        llBucai.setVisibility(View.VISIBLE);
                                        String s = specialFields.replace("*", "").replace("</", "\n").replace("<", "\n\n");
                                        if (!TextUtils.isEmpty(s)) {
                                            llBucai.setVisibility(View.VISIBLE);
                                            tvBucai.setText(s);
                                        } else {
                                            llBucai.setVisibility(View.GONE);
                                        }
                                    } else {
                                        llBucai.setVisibility(View.GONE);
                                    }
                                } else {
                                    xjgggDetailStatusView.showError();
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

            case R.id.tv_data_gg:
                if (!tvGg.getText().toString().equals("无")) {
                    if ("sggjy".equals(ggEntity)) {
                        intent = new Intent(BiaoXunTong.getApplicationContext(), IndexSggjyDetailActivity.class);
                        intent.putExtra("entityId", Integer.valueOf(ggEntityId));
                        intent.putExtra("entity", ggEntity);
                        intent.putExtra("ajaxlogtype", "0");
                        intent.putExtra("mId", "");
                        startActivity(intent);

                    } else if ("xcggg".equals(ggEntity)) {
                        intent = new Intent(BiaoXunTong.getApplicationContext(), IndexXcgggDetailActivity.class);
                        intent.putExtra("entityId", Integer.valueOf(ggEntityId));
                        intent.putExtra("entity", ggEntity);
                        intent.putExtra("ajaxlogtype", "0");
                        intent.putExtra("mId", "");
                        startActivity(intent);
                    } else if ("bxtgdj".equals(ggEntity)) {
                        intent = new Intent(BiaoXunTong.getApplicationContext(), IndexBxtgdjDetailActivity.class);
                        intent.putExtra("entityId", Integer.valueOf(ggEntityId));
                        intent.putExtra("entity", ggEntity);
                        intent.putExtra("ajaxlogtype", "0");
                        intent.putExtra("mId", "");
                        startActivity(intent);
                    } else if ("sggjycgtable".equals(ggEntity)) {
                        intent = new Intent(BiaoXunTong.getApplicationContext(), IndexSggjycgtableDetailActivity.class);
                        intent.putExtra("entityId", Integer.valueOf(ggEntityId));
                        intent.putExtra("entity", ggEntity);
                        intent.putExtra("ajaxlogtype", "0");
                        intent.putExtra("mId", "");
                        startActivity(intent);
                    } else if ("sggjycgrow".equals(ggEntity)) {
                        intent = new Intent(BiaoXunTong.getApplicationContext(), IndexSggjycgrowDetailActivity.class);
                        intent.putExtra("entityId", Integer.valueOf(ggEntityId));
                        intent.putExtra("entity", ggEntity);
                        intent.putExtra("ajaxlogtype", "0");
                        intent.putExtra("mId", "");
                        startActivity(intent);
                    } else if ("scggg".equals(ggEntity)) {
                        intent = new Intent(BiaoXunTong.getApplicationContext(), IndexScgggDetailActivity.class);
                        intent.putExtra("entityId", Integer.valueOf(ggEntityId));
                        intent.putExtra("entity", ggEntity);
                        intent.putExtra("ajaxlogtype", "0");
                        intent.putExtra("mId", "");
                        startActivity(intent);
                    }
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
