package com.lubanjianye.biaoxuntong.ui.browser;


import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.AppCompatTextView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import com.alibaba.android.arouter.facade.annotation.Autowired;
import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.just.agentweb.AgentWeb;
import com.lubanjianye.biaoxuntong.R;
import com.lubanjianye.biaoxuntong.app.BiaoXunTongApi;
import com.lubanjianye.biaoxuntong.base.BaseActivity;
import com.lubanjianye.biaoxuntong.database.DatabaseManager;
import com.lubanjianye.biaoxuntong.database.UserProfile;
import com.lubanjianye.biaoxuntong.eventbus.EventMessage;
import com.lubanjianye.biaoxuntong.ui.share.Share;
import com.lubanjianye.biaoxuntong.ui.sign.SignInActivity;
import com.lubanjianye.biaoxuntong.util.aes.AesUtil;
import com.lubanjianye.biaoxuntong.util.netStatus.AppSysMgr;
import com.lubanjianye.biaoxuntong.util.sp.AppSharePreferenceMgr;
import com.lubanjianye.biaoxuntong.util.toast.ToastUtil;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;
import org.greenrobot.eventbus.EventBus;
import java.util.List;
import butterknife.BindView;
import butterknife.OnClick;


/**
 * Created by 11645 on 2018/3/22.
 */

@Route(path = "/com/BrowserDetailActivity")
public class BrowserDetailActivity extends BaseActivity {


    @BindView(R.id.ll_iv_back)
    LinearLayout llIvBack;
    @BindView(R.id.main_bar_name)
    AppCompatTextView mainBarName;
    @BindView(R.id.iv_fav)
    ImageView ivFav;
    @BindView(R.id.ll_fav)
    LinearLayout llFav;
    @BindView(R.id.ll_share)
    LinearLayout llShare;
    @BindView(R.id.ll_webview)
    LinearLayout llWebview;
    private AgentWeb webView = null;

    @Autowired
    String mApi;
    @Autowired
    String mTitle;
    @Autowired
    String mEntity;
    @Autowired
    int mEntityid;

    public String mUrl = "";
    private int myFav = -1;
    private String mDiqu = "";
    private String deviceId = AppSysMgr.getPsuedoUniqueID();
    private String ajaxType = "0";

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_browser_detail;
    }

    @Override
    protected void initData(Bundle savedInstanceState) {
        if (AppSharePreferenceMgr.contains(this, EventMessage.LOCA_AREA)) {
            mDiqu = (String) AppSharePreferenceMgr.get(this, EventMessage.LOCA_AREA, "");
        }
        llIvBack.setVisibility(View.VISIBLE);
        mainBarName.setText("标讯详情");

        requestData();
    }

    @Override
    protected void initEvent(Bundle savedInstanceState) {

    }

    private long id = 0;

    private void requestData() {
        if (AppSharePreferenceMgr.contains(this, EventMessage.LOGIN_SUCCSS)) {
            List<UserProfile> users = DatabaseManager.getInstance().getDao().loadAll();

            for (int i = 0; i < users.size(); i++) {
                id = users.get(0).getId();
            }

            OkGo.<String>post(mApi)
                    .params("entityId", mEntityid)
                    .params("entity", mEntity)
                    .params("userid", id)
                    .params("diqu", mDiqu)
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
                                mUrl = data.getString("url");


                                webView = AgentWeb.with(BrowserDetailActivity.this)
                                        .setAgentWebParent(llWebview, new LinearLayout.LayoutParams(-1, -1))
                                        .useDefaultIndicator(getResources().getColor(R.color.main_status_red), 3)
                                        .createAgentWeb()
                                        .ready()
                                        .go(mUrl);

                            } else {

                            }
                        }
                    });

        } else {

            OkGo.<String>post(mApi)
                    .params("entityId", mEntityid)
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

                            if ("200".equals(status)) {
                                final JSONObject data = object.getJSONObject("data");
                                mUrl = data.getString("url");

                                webView = AgentWeb.with(BrowserDetailActivity.this)
                                        .setAgentWebParent(llWebview, new LinearLayout.LayoutParams(-1, -1))
                                        .useDefaultIndicator(getResources().getColor(R.color.main_status_red), 3)
                                        .createAgentWeb()
                                        .ready()
                                        .go(mUrl);
                            } else {

                            }
                        }
                    });
        }
    }


    private Share mShare = new Share();


    @OnClick({R.id.ll_iv_back, R.id.ll_fav, R.id.ll_share})
    public void onViewClicked(View view) {
        mShare.setAppName("鲁班标讯通");
        mShare.setAppShareIcon(R.mipmap.ic_share);
        if (mShare.getBitmapResID() == 0) {
            mShare.setBitmapResID(R.mipmap.ic_share);
        }
        mShare.setTitle(mTitle);
        mShare.setContent(mTitle);
        mShare.setSummary(mTitle);
        mShare.setDescription(mTitle);
        mShare.setImageUrl(null);
        mShare.setUrl(mUrl);
        switch (view.getId()) {
            case R.id.ll_iv_back:
                finish();
                break;
            case R.id.ll_fav:
                if (AppSharePreferenceMgr.contains(this, EventMessage.LOGIN_SUCCSS)) {
                    //已登录处理事件
                    List<UserProfile> users = DatabaseManager.getInstance().getDao().loadAll();
                    long id = 0;
                    for (int i = 0; i < users.size(); i++) {
                        id = users.get(0).getId();
                    }

                    if (myFav == 1) {

                        OkGo.<String>post(BiaoXunTongApi.URL_DELEFAV)
                                .params("entityid", mEntityid)
                                .params("entity", mEntity)
                                .params("userid", id)
                                .params("diqu", mDiqu)
                                .params("deviceId", deviceId)
                                .execute(new StringCallback() {
                                    @Override
                                    public void onSuccess(Response<String> response) {
                                        final JSONObject object = JSON.parseObject(response.body());
                                        String status = object.getString("status");
                                        if ("200".equals(status)) {
                                            myFav = 0;
                                            ivFav.setImageResource(R.mipmap.ic_fav_pressed);
                                            ToastUtil.shortToast(BrowserDetailActivity.this, "取消收藏");
                                            EventBus.getDefault().post(new EventMessage(EventMessage.CLICK_FAV));
                                        } else if ("500".equals(status)) {
                                            ToastUtil.shortToast(BrowserDetailActivity.this, "服务器异常");
                                        }
                                    }
                                });

                    } else if (myFav == 0) {

                        OkGo.<String>post(BiaoXunTongApi.URL_ADDFAV)
                                .params("entityid", mEntityid)
                                .params("entity", mEntity)
                                .params("userid", id)
                                .params("diqu", mDiqu)
                                .params("deviceId", deviceId)
                                .execute(new StringCallback() {
                                    @Override
                                    public void onSuccess(Response<String> response) {
                                        final JSONObject object = JSON.parseObject(response.body());
                                        String status = object.getString("status");
                                        if ("200".equals(status)) {
                                            myFav = 1;
                                            ivFav.setImageResource(R.mipmap.ic_faved_pressed);
                                            ToastUtil.shortToast(BrowserDetailActivity.this, "收藏成功");
                                            EventBus.getDefault().post(new EventMessage(EventMessage.CLICK_FAV));
                                        } else if ("500".equals(status)) {
                                            ToastUtil.shortToast(BrowserDetailActivity.this, "服务器异常");
                                        }
                                    }
                                });

                    } else {
                        ToastUtil.shortToast(BrowserDetailActivity.this, "未知收藏状态");
                    }
                } else {
                    //未登录去登陆
                    startActivity(new Intent(BrowserDetailActivity.this, SignInActivity.class));
                }
                break;
            case R.id.ll_share:
                toShare(mEntityid, mTitle, mTitle, mUrl);
                break;
        }
    }
}
