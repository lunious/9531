package com.lubanjianye.biaoxuntong.ui.main.result.detail.sichuan;

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

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.classic.common.MultipleStatusView;
import com.lubanjianye.biaoxuntong.R;
import com.lubanjianye.biaoxuntong.app.BiaoXunTong;
import com.lubanjianye.biaoxuntong.base.BaseFragment1;
import com.lubanjianye.biaoxuntong.database.DatabaseManager;
import com.lubanjianye.biaoxuntong.database.UserProfile;
import com.lubanjianye.biaoxuntong.eventbus.EventMessage;
import com.lubanjianye.biaoxuntong.app.BiaoXunTongApi;
import com.lubanjianye.biaoxuntong.ui.main.index.detail.sichuan.IndexBxtgdjDetailActivity;
import com.lubanjianye.biaoxuntong.ui.main.index.detail.sichuan.IndexScgggDetailActivity;
import com.lubanjianye.biaoxuntong.ui.main.index.detail.sichuan.IndexSggjyDetailActivity;
import com.lubanjianye.biaoxuntong.ui.main.index.detail.sichuan.IndexSggjycgrowDetailActivity;
import com.lubanjianye.biaoxuntong.ui.main.query.CompanySearchResultActivity;
import com.lubanjianye.biaoxuntong.ui.main.user.avater.AvaterActivity;
import com.lubanjianye.biaoxuntong.ui.sign.SignInActivity;
import com.lubanjianye.biaoxuntong.ui.main.index.detail.sichuan.IndexSggjycgtableDetailActivity;
import com.lubanjianye.biaoxuntong.ui.main.index.detail.sichuan.IndexXcgggDetailActivity;
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


public class ResultSggjyzbjgDetailFragment extends BaseFragment1 implements View.OnClickListener, OpenBuilder.Callback {

    private LinearLayout llIvBack = null;
    private AppCompatTextView mainBarName = null;
    private LinearLayout llShare = null;
    private LinearLayout llFav = null;
    private AppCompatTextView tvMainTitle = null;
    private AppCompatTextView tvDataTime = null;
    private AppCompatTextView tvOwerBiaoduanname = null;
    private AppCompatTextView tvOwerXiangmuyezhu = null;
    private AppCompatTextView tvOwerXiangmuyezhuNumb = null;
    private AppCompatTextView tvOwerZhaobiaoren = null;
    private AppCompatTextView tvOwerZhaobiaorenNumb = null;
    private AppCompatTextView tvOwerZhaobiaodaili = null;
    private AppCompatTextView tvOwerZhaobiaodailiNumb = null;
    private AppCompatTextView tvOwerKaibiaodidian = null;
    private AppCompatTextView tvOwerKaibiaoshijian = null;
    private AppCompatTextView tvOwerGongshiqi = null;
    private AppCompatTextView tvOwerToubiaoxianjia = null;
    private AppCompatTextView tvOwerDiyi = null;
    private AppCompatTextView tvOwerDier = null;
    private AppCompatTextView tvOwerDisan = null;
    private ImageView ivFav = null;
    private MultipleStatusView sggjyDetailStatusView = null;

    private LinearLayout llWeiBoShare_bottom = null;
    private LinearLayout llQQBoShare_bottom = null;
    private LinearLayout llWeixinBoShare_bottom = null;
    private LinearLayout llPyqShare_bottom = null;


    private AppCompatTextView diyiBj = null;
    private AppCompatTextView diyiTbj = null;
    private AppCompatTextView diyiPf = null;
    private AppCompatTextView dierBj = null;
    private AppCompatTextView dierTbj = null;
    private AppCompatTextView dierPf = null;
    private AppCompatTextView disanBj = null;
    private AppCompatTextView disanTbj = null;
    private AppCompatTextView disanPf = null;


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


    @Override
    public Object setLayout() {
        return R.layout.fragment_result_sggjyzbjg_detail;
    }

    public static ResultSggjyzbjgDetailFragment create(@NonNull int entityId, String entity, String ajaxlogtype) {
        final Bundle args = new Bundle();
        args.putInt(ARG_ENTITYID, entityId);
        args.putString(ARG_ENTITY, entity);
        args.putString(ARG_AJAXTYPE, ajaxlogtype);
        final ResultSggjyzbjgDetailFragment fragment = new ResultSggjyzbjgDetailFragment();
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
        tvOwerBiaoduanname = getView().findViewById(R.id.tv_ower_biaoduanname);
        tvOwerXiangmuyezhu = getView().findViewById(R.id.tv_ower_xiangmuyezhu);
        tvOwerXiangmuyezhuNumb = getView().findViewById(R.id.tv_ower_xiangmuyezhu_numb);
        tvOwerZhaobiaoren = getView().findViewById(R.id.tv_ower_zhaobiaoren);
        tvOwerZhaobiaorenNumb = getView().findViewById(R.id.tv_ower_zhaobiaoren_numb);
        tvOwerZhaobiaodaili = getView().findViewById(R.id.tv_ower_zhaobiaodaili);
        tvOwerZhaobiaodailiNumb = getView().findViewById(R.id.tv_ower_zhaobiaodaili_numb);
        tvOwerKaibiaodidian = getView().findViewById(R.id.tv_ower_kaibiaodidian);
        tvOwerKaibiaoshijian = getView().findViewById(R.id.tv_ower_kaibiaoshijian);
        tvOwerGongshiqi = getView().findViewById(R.id.tv_ower_gongshiqi);
        tvOwerToubiaoxianjia = getView().findViewById(R.id.tv_ower_toubiaoxianjia);
        tvOwerDiyi = getView().findViewById(R.id.tv_ower_diyi);
        tvOwerDier = getView().findViewById(R.id.tv_ower_dier);
        tvOwerDisan = getView().findViewById(R.id.tv_ower_disan);
        ivFav = getView().findViewById(R.id.iv_fav);
        sggjyDetailStatusView = getView().findViewById(R.id.sggjy_detail_status_view);
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
        tvGg = getView().findViewById(R.id.tv_data_gg);
        tvGg.setOnClickListener(this);
        tvOwerDiyi.setOnClickListener(this);
        tvOwerDier.setOnClickListener(this);
        tvOwerDisan.setOnClickListener(this);
        llFav = getView().findViewById(R.id.ll_fav);
        llFav.setOnClickListener(this);


        diyiBj = getView().findViewById(R.id.diyi_bj);
        diyiTbj = getView().findViewById(R.id.diyi_tbj);
        diyiPf = getView().findViewById(R.id.diyi_pf);
        dierBj = getView().findViewById(R.id.dier_bj);
        dierTbj = getView().findViewById(R.id.dier_tbj);
        dierPf = getView().findViewById(R.id.dier_pf);
        disanBj = getView().findViewById(R.id.disan_bj);
        disanTbj = getView().findViewById(R.id.disan_tbj);
        disanPf = getView().findViewById(R.id.disan_pf);


    }

    @Override
    public void initData() {
        llIvBack.setVisibility(View.VISIBLE);
        mainBarName.setText("工程招标中标公示详情");
        sggjyDetailStatusView.setOnRetryClickListener(mRetryClickListener);

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
                        .params("userid", id)
                        .params("deviceId", deviceId)
                        .params("ajaxlogtype", ajaxType)
                        .execute(new StringCallback() {
                            @Override
                            public void onSuccess(Response<String> response) {
                                String jiemi = AesUtil.aesDecrypt(response.body(), BiaoXunTongApi.PAS_KEY);

                                Log.d("YAGSYGDASDADAS", jiemi);

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
                                            tvGg.setText("无");
                                            tvGg.setTextColor(getResources().getColor(R.color.main_text_color));
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
                                    String entryName = data.getString("entryName");
                                    shareContent = entryName;
                                    if (!TextUtils.isEmpty(entryName)) {
                                        tvOwerBiaoduanname.setText(entryName);
                                    } else {
                                        tvOwerBiaoduanname.setText("暂无");
                                    }
                                    String entryOwner = data.getString("entryOwner");
                                    if (!TextUtils.isEmpty(entryOwner)) {
                                        tvOwerXiangmuyezhu.setText(entryOwner);
                                    } else {
                                        tvOwerXiangmuyezhu.setText("暂无");
                                    }
                                    String ownerTel = data.getString("ownerTel");
                                    if (!TextUtils.isEmpty(ownerTel)) {
                                        tvOwerXiangmuyezhuNumb.setText(ownerTel);
                                    } else {
                                        tvOwerXiangmuyezhuNumb.setText("暂无");
                                    }
                                    String tenderee = data.getString("tenderee");
                                    if (!TextUtils.isEmpty(tenderee)) {
                                        tvOwerZhaobiaoren.setText(tenderee);
                                    } else {
                                        tvOwerZhaobiaoren.setText("暂无");
                                    }
                                    String tendereeTel = data.getString("tendereeTel");
                                    if (!TextUtils.isEmpty(tendereeTel)) {
                                        tvOwerZhaobiaorenNumb.setText(tendereeTel);
                                    } else {
                                        tvOwerZhaobiaorenNumb.setText("暂无");
                                    }
                                    String biddingAgency = data.getString("biddingAgency");
                                    if (!TextUtils.isEmpty(biddingAgency)) {
                                        tvOwerZhaobiaodaili.setText(biddingAgency);
                                    } else {
                                        tvOwerZhaobiaodaili.setText("暂无");
                                    }
                                    String biddingAgencTel = data.getString("biddingAgencTel");
                                    if (!TextUtils.isEmpty(biddingAgencTel)) {
                                        tvOwerZhaobiaodailiNumb.setText(biddingAgencTel);
                                    } else {
                                        tvOwerZhaobiaodailiNumb.setText("暂无");
                                    }
                                    String placeAddress = data.getString("placeAddress");
                                    if (!TextUtils.isEmpty(placeAddress)) {
                                        tvOwerKaibiaodidian.setText(placeAddress);
                                    } else {
                                        tvOwerKaibiaodidian.setText("暂无");
                                    }
                                    String placeTime = data.getString("placeTime");
                                    if (!TextUtils.isEmpty(placeTime)) {
                                        tvOwerKaibiaoshijian.setText(placeTime);
                                    } else {
                                        tvOwerKaibiaoshijian.setText("暂无");
                                    }
                                    String publicityPeriod = data.getString("publicityPeriod");
                                    if (!TextUtils.isEmpty(publicityPeriod)) {
                                        tvOwerGongshiqi.setText(publicityPeriod);
                                    } else {
                                        tvOwerGongshiqi.setText("暂无");
                                    }
                                    String bigPrice = data.getString("bigPrice");
                                    if (!TextUtils.isEmpty(bigPrice)) {
                                        tvOwerToubiaoxianjia.setText(bigPrice);
                                    } else {
                                        tvOwerToubiaoxianjia.setText("暂无");
                                    }
                                    String oneTree = data.getString("oneTree");
                                    if (!TextUtils.isEmpty(oneTree)) {
                                        String regex = "\\_";
                                        String[] arr = oneTree.split(regex);
                                        String s1 = "";
                                        String s2 = "";
                                        String s3 = "";
                                        String s4 = "";
                                        if (arr.length == 4) {
                                            s1 = arr[0];
                                            s2 = arr[1];
                                            s3 = arr[2];
                                            s4 = arr[3];
                                        } else if (arr.length == 3) {
                                            s1 = arr[0];
                                            s2 = arr[1];
                                            s3 = arr[2];
                                            s4 = "0.0";
                                        } else if (arr.length == 1 || arr.length == 2) {
                                            s1 = arr[0];
                                            s2 = "0.0";
                                            s3 = "0.0";
                                            s4 = "0.0";
                                        }
                                        if (oneTree.contains("/")) {
                                            Log.d("ABSDASDASD",arr.length+"");
                                            tvOwerDiyi.setText(s1.substring(0, s1.indexOf("/")));
                                        } else {
                                            tvOwerDiyi.setText(s1);
                                        }
                                        diyiBj.setText("投标报价（元）: " + s2);
                                        diyiTbj.setText("经评审的投标价（元）: " + s3);
                                        diyiPf.setText(s4);

                                    } else {
                                        tvOwerDiyi.setText("暂无");
                                    }

                                    String twoTree = data.getString("twoTree");
                                    if (!TextUtils.isEmpty(twoTree)) {
                                        String regex = "\\_";
                                        String[] arr = twoTree.split(regex);
                                        String s1 = "";
                                        String s2 = "";
                                        String s3 = "";
                                        String s4 = "";
                                        if (arr.length == 4) {
                                            s1 = arr[0];
                                            s2 = arr[1];
                                            s3 = arr[2];
                                            s4 = arr[3];
                                        } else if (arr.length == 3) {
                                            s1 = arr[0];
                                            s2 = arr[1];
                                            s3 = arr[2];
                                            s4 = "0.0";
                                        } else if (arr.length == 1 || arr.length == 2) {
                                            s1 = arr[0];
                                            s2 = "0.0";
                                            s3 = "0.0";
                                            s4 = "0.0";
                                        }
                                        if (oneTree.contains("/")) {
                                            tvOwerDier.setText(s1.substring(0, s1.indexOf("/")));
                                        } else {
                                            tvOwerDier.setText(s1);
                                        }
                                        dierBj.setText("投标报价（元）: " + s2);
                                        dierTbj.setText("经评审的投标价（元）: " + s3);
                                        dierPf.setText(s4);
                                    } else {
                                        tvOwerDier.setText("暂无");
                                    }
                                    String threeTree = data.getString("threeTree");
                                    if (!TextUtils.isEmpty(threeTree)) {
                                        String regex = "\\_";
                                        String[] arr = threeTree.split(regex);
                                        String s1 = "";
                                        String s2 = "";
                                        String s3 = "";
                                        String s4 = "";
                                        if (arr.length == 4) {
                                            s1 = arr[0];
                                            s2 = arr[1];
                                            s3 = arr[2];
                                            s4 = arr[3];
                                        } else if (arr.length == 3) {
                                            s1 = arr[0];
                                            s2 = arr[1];
                                            s3 = arr[2];
                                            s4 = "0.0";
                                        } else if (arr.length == 1 || arr.length == 2) {
                                            s1 = arr[0];
                                            s2 = "0.0";
                                            s3 = "0.0";
                                            s4 = "0.0";
                                        }
                                        if (oneTree.contains("/")) {
                                            tvOwerDisan.setText(s1.substring(0, s1.indexOf("/")));
                                        } else {
                                            tvOwerDisan.setText(s1);
                                        }
                                        disanBj.setText("投标报价（元）: " + s2);
                                        disanTbj.setText("经评审的投标价（元）: " + s3);
                                        disanPf.setText(s4);
                                    } else {
                                        tvOwerDisan.setText("暂无");
                                    }

                                    sggjyDetailStatusView.showContent();
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
                                            tvGg.setText("无");
                                            tvGg.setTextColor(getResources().getColor(R.color.main_text_color));
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
                                    String entryName = data.getString("entryName");
                                    shareContent = entryName;
                                    if (!TextUtils.isEmpty(entryName)) {
                                        tvOwerBiaoduanname.setText(entryName);
                                    } else {
                                        tvOwerBiaoduanname.setText("暂无");
                                    }
                                    String entryOwner = data.getString("entryOwner");
                                    if (!TextUtils.isEmpty(entryOwner)) {
                                        tvOwerXiangmuyezhu.setText(entryOwner);
                                    } else {
                                        tvOwerXiangmuyezhu.setText("暂无");
                                    }
                                    String ownerTel = data.getString("ownerTel");
                                    if (!TextUtils.isEmpty(ownerTel)) {
                                        tvOwerXiangmuyezhuNumb.setText(ownerTel);
                                    } else {
                                        tvOwerXiangmuyezhuNumb.setText("暂无");
                                    }
                                    String tenderee = data.getString("tenderee");
                                    if (!TextUtils.isEmpty(tenderee)) {
                                        tvOwerZhaobiaoren.setText(tenderee);
                                    } else {
                                        tvOwerZhaobiaoren.setText("暂无");
                                    }
                                    String tendereeTel = data.getString("tendereeTel");
                                    if (!TextUtils.isEmpty(tendereeTel)) {
                                        tvOwerZhaobiaorenNumb.setText(tendereeTel);
                                    } else {
                                        tvOwerZhaobiaorenNumb.setText("暂无");
                                    }
                                    String biddingAgency = data.getString("biddingAgency");
                                    if (!TextUtils.isEmpty(biddingAgency)) {
                                        tvOwerZhaobiaodaili.setText(biddingAgency);
                                    } else {
                                        tvOwerZhaobiaodaili.setText("暂无");
                                    }
                                    String biddingAgencTel = data.getString("biddingAgencTel");
                                    if (!TextUtils.isEmpty(biddingAgencTel)) {
                                        tvOwerZhaobiaodailiNumb.setText(biddingAgencTel);
                                    } else {
                                        tvOwerZhaobiaodailiNumb.setText("暂无");
                                    }
                                    String placeAddress = data.getString("placeAddress");
                                    if (!TextUtils.isEmpty(placeAddress)) {
                                        tvOwerKaibiaodidian.setText(placeAddress);
                                    } else {
                                        tvOwerKaibiaodidian.setText("暂无");
                                    }
                                    String placeTime = data.getString("placeTime");
                                    if (!TextUtils.isEmpty(placeTime)) {
                                        tvOwerKaibiaoshijian.setText(placeTime);
                                    } else {
                                        tvOwerKaibiaoshijian.setText("暂无");
                                    }
                                    String publicityPeriod = data.getString("publicityPeriod");
                                    if (!TextUtils.isEmpty(publicityPeriod)) {
                                        tvOwerGongshiqi.setText(publicityPeriod);
                                    } else {
                                        tvOwerGongshiqi.setText("暂无");
                                    }
                                    String bigPrice = data.getString("bigPrice");
                                    if (!TextUtils.isEmpty(bigPrice)) {
                                        tvOwerToubiaoxianjia.setText(bigPrice);
                                    } else {
                                        tvOwerToubiaoxianjia.setText("暂无");
                                    }
                                    String oneTree = data.getString("oneTree");
                                    if (!TextUtils.isEmpty(oneTree)) {
                                        tvOwerDiyi.setText(oneTree.substring(0, oneTree.indexOf("_")));
                                    } else {
                                        tvOwerDiyi.setText("暂无");
                                    }
                                    String twoTree = data.getString("twoTree");
                                    if (!TextUtils.isEmpty(twoTree)) {
                                        tvOwerDier.setText(twoTree.substring(0, twoTree.indexOf("_")));
                                    } else {
                                        tvOwerDier.setText("暂无");
                                    }
                                    String threeTree = data.getString("threeTree");
                                    if (!TextUtils.isEmpty(threeTree)) {
                                        tvOwerDisan.setText(threeTree.substring(0, threeTree.indexOf("_")));
                                    } else {
                                        tvOwerDisan.setText("暂无");
                                    }
                                    sggjyDetailStatusView.showContent();

                                } else {
                                    sggjyDetailStatusView.showError();
                                }
                            }
                        });
            }
        }


    }

    private Share mShare = new Share();

    String provinceCode = "510000";

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
