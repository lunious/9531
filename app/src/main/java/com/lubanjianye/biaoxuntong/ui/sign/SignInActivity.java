package com.lubanjianye.biaoxuntong.ui.sign;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.AppCompatTextView;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.flyco.tablayout.SlidingTabLayout;
import com.igexin.sdk.PushManager;
import com.lubanjianye.biaoxuntong.R;
import com.lubanjianye.biaoxuntong.app.BiaoXunTong;
import com.lubanjianye.biaoxuntong.app.BiaoXunTongApi;
import com.lubanjianye.biaoxuntong.base.BaseActivity;
import com.lubanjianye.biaoxuntong.database.DatabaseManager;
import com.lubanjianye.biaoxuntong.database.UserProfile;
import com.lubanjianye.biaoxuntong.eventbus.EventMessage;
import com.lubanjianye.biaoxuntong.util.dialog.PromptDialog;
import com.lubanjianye.biaoxuntong.util.sp.AppSharePreferenceMgr;
import com.lubanjianye.biaoxuntong.util.toast.ToastUtil;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import me.shaohui.shareutil.LoginUtil;
import me.shaohui.shareutil.login.LoginListener;
import me.shaohui.shareutil.login.LoginPlatform;
import me.shaohui.shareutil.login.LoginResult;
import me.shaohui.shareutil.login.result.QQToken;
import me.shaohui.shareutil.login.result.QQUser;
import me.shaohui.shareutil.login.result.WxToken;
import me.shaohui.shareutil.login.result.WxUser;


@Route(path = "/com/SignInActivity")
public class SignInActivity extends BaseActivity {

    @BindView(R.id.ll_iv_back)
    LinearLayout llIvBack;
    @BindView(R.id.main_bar_name)
    AppCompatTextView mainBarName;
    @BindView(R.id.message_num)
    AppCompatTextView messageNum;
    @BindView(R.id.ll_message)
    LinearLayout llMessage;
    @BindView(R.id.login_stl_tab)
    SlidingTabLayout loginStlTab;
    @BindView(R.id.result_vp)
    ViewPager resultVp;
    @BindView(R.id.bt_login_register)
    AppCompatTextView btLoginRegister;
    @BindView(R.id.tv_login_forget_pwd)
    AppCompatTextView tvLoginForgetPwd;
    @BindView(R.id.ll_login_qq)
    LinearLayout llLoginQq;
    @BindView(R.id.ll_login_wx)
    LinearLayout llLoginWx;


    private LoginListener mLoginListener = null;
    private PromptDialog promptDialog = null;

    private long id = 0;
    private String mobile = "";
    private String nickName = "";
    private String token = "";
    private String comid = "";
    private String imageUrl = "";
    private String companyName = "";

    private String clientID = PushManager.getInstance().getClientid(BiaoXunTong.getApplicationContext());

    private final List<String> mList = new ArrayList<String>();
    private LoginMethodFragmentAdapter mAdapter;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_sign_in;
    }

    @Override
    protected void initData(Bundle savedInstanceState) {
        llIvBack.setVisibility(View.VISIBLE);
        mainBarName.setText("");

        mList.add("密码登陆");
        mList.add("验证码登陆");

        mAdapter = new LoginMethodFragmentAdapter(mList, getSupportFragmentManager());
        resultVp.setAdapter(mAdapter);
        loginStlTab.setViewPager(resultVp);

        //创建对象
        promptDialog = new PromptDialog(this);
        //设置自定义属性
        promptDialog.getDefaultBuilder().touchAble(true).round(3).loadingDuration(3000);
    }

    @Override
    protected void initEvent(Bundle savedInstanceState) {
        mLoginListener = new LoginListener() {

            @Override
            public void loginSuccess(LoginResult result) {
                promptDialog.dismissImmediately();
                promptDialog.showLoading("正在登录...");

                // 处理result
                switch (result.getPlatform()) {
                    case LoginPlatform.QQ:
                        QQUser user = (QQUser) result.getUserInfo();
                        QQToken mToken = (QQToken) result.getToken();
                        nickName = user.getNickname();
                        String openid = mToken.getOpenid();
                        imageUrl = user.getqZoneHeadImageLarge();


                        OkGo.<String>post(BiaoXunTongApi.URL_QQLOGIN)
                                .params("Source", "3")
                                .params("qq", openid)
                                .params("clientId", clientID)
                                .execute(new StringCallback() {
                                    @Override
                                    public void onSuccess(Response<String> response) {
                                        final JSONObject profileJson = JSON.parseObject(response.body());
                                        final String status = profileJson.getString("status");
                                        final String message = profileJson.getString("message");


                                        Log.d("HYABSDASDASDASD",response.body());

                                        if ("200".equals(status)) {
                                            final JSONObject userInfo = JSON.parseObject(response.body()).getJSONObject("data");
                                            id = userInfo.getLong("id");
                                            token = response.headers().get("token");
                                            comid = userInfo.getString("comid");

                                            getUserInfo(id);

                                            promptDialog.dismissImmediately();

                                        } else {
                                            promptDialog.dismissImmediately();
                                            ToastUtil.shortToast(SignInActivity.this, message);
                                        }
                                    }
                                });
                        break;
                    case LoginPlatform.WX:
                        WxUser wxUser = (WxUser) result.getUserInfo();
                        WxToken wxToken = (WxToken) result.getToken();

                        nickName = wxUser.getNickname();
                        String Oppenid = wxUser.getOpenId();
                        imageUrl = wxUser.getHeadImageUrl();

                        OkGo.<String>post(BiaoXunTongApi.URL_WEIXINLOGIN)
                                .params("Source", 1)
                                .params("Oppenid", Oppenid)
                                .params("clientId", clientID)
                                .execute(new StringCallback() {
                                    @Override
                                    public void onSuccess(Response<String> response) {
                                        final JSONObject profileJson = JSON.parseObject(response.body());
                                        final String status = profileJson.getString("status");
                                        final String message = profileJson.getString("message");
                                        if ("200".equals(status)) {
                                            final JSONObject userInfo = JSON.parseObject(response.body()).getJSONObject("data");
                                            id = userInfo.getLong("id");
                                            token = response.headers().get("token");
                                            comid = userInfo.getString("comid");

                                            getUserInfo(id);

                                            promptDialog.dismissImmediately();
                                        } else {
                                            promptDialog.dismissImmediately();
                                            ToastUtil.shortToast(SignInActivity.this, message);
                                        }
                                    }
                                });

                        break;
                    default:
                        break;
                }
            }

            @Override
            public void loginFailure(Exception e) {
                promptDialog.dismissImmediately();
                ToastUtil.shortBottonToast(SignInActivity.this, "登录失败");

            }

            @Override
            public void loginCancel() {
                promptDialog.dismissImmediately();
                ToastUtil.shortBottonToast(SignInActivity.this, "登录取消");
            }
        };
    }

    public void getUserInfo(long userId) {

        OkGo.<String>post(BiaoXunTongApi.URL_GETUSERINFO)
                .params("Id", userId)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        final JSONObject userInfo = JSON.parseObject(response.body());
                        final String status = userInfo.getString("status");
                        final String message = userInfo.getString("message");

                        if ("200".equals(status)) {
                            final JSONObject data = userInfo.getJSONObject("data");
                            final JSONObject qy = data.getJSONObject("qy");
                            final JSONObject user = data.getJSONObject("user");


                            String name = user.getString("nickName");
                            if (!name.isEmpty()) {
                                nickName = name;
                            }
                            if (qy != null) {
                                String mQy = qy.getString("qy");
                                if (!mQy.isEmpty()) {
                                    companyName = mQy;
                                }
                            }
                            String phone = user.getString("mobile");
                            if (!phone.isEmpty()) {
                                mobile = phone;
                            }
                            String headUrl = user.getString("headUrl");
                            if (!headUrl.isEmpty()) {
                                imageUrl = headUrl;
                            }

                            final UserProfile profile = new UserProfile(id, mobile, nickName, token, comid, imageUrl, companyName);
                            DatabaseManager.getInstance().getDao().insert(profile);
                            AppSharePreferenceMgr.put(BiaoXunTong.getApplicationContext(), EventMessage.LOGIN_SUCCSS, true);
                            EventBus.getDefault().post(new EventMessage(EventMessage.LOGIN_SUCCSS));
                            finish();
                            ToastUtil.shortBottonToast(SignInActivity.this, "登陆成功");

                        } else {
                            ToastUtil.shortToast(SignInActivity.this, message);
                        }
                    }
                });


    }


    @Override
    public void onResume() {
        super.onResume();
        if (loginStlTab != null) {
            loginStlTab.setCurrentTab(0);
            loginStlTab.setViewPager(resultVp);
            loginStlTab.notifyDataSetChanged();
        }
    }



    @OnClick({R.id.ll_iv_back, R.id.bt_login_register, R.id.tv_login_forget_pwd, R.id.ll_login_qq, R.id.ll_login_wx})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.ll_iv_back:
                if (promptDialog != null) {
                    promptDialog.dismissImmediately();
                }
                finish();
                break;
            case R.id.bt_login_register:
                //用户注册
                ARouter.getInstance().build("/com/SignUpActivity").navigation();
                break;
            case R.id.tv_login_forget_pwd:
                //重置密码
                ARouter.getInstance().build("/com/SignForgetPwdActivity").navigation();
                break;
            case R.id.ll_login_qq:
                //QQ登陆
                promptDialog.showLoading("请稍后...");
                LoginUtil.login(this, LoginPlatform.QQ, mLoginListener);
                break;
            case R.id.ll_login_wx:
                //微信登陆
                promptDialog.showLoading("请稍后...");
                LoginUtil.login(this, LoginPlatform.WX, mLoginListener);
                break;
        }
    }
}
