package com.lubanjianye.biaoxuntong.ui.sign;

import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.AppCompatTextView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.igexin.sdk.PushManager;
import com.lubanjianye.biaoxuntong.R;
import com.lubanjianye.biaoxuntong.app.BiaoXunTong;
import com.lubanjianye.biaoxuntong.base.BaseFragment1;
import com.lubanjianye.biaoxuntong.database.DatabaseManager;
import com.lubanjianye.biaoxuntong.database.UserProfile;
import com.lubanjianye.biaoxuntong.eventbus.EventMessage;
import com.lubanjianye.biaoxuntong.app.BiaoXunTongApi;
import com.lubanjianye.biaoxuntong.util.dialog.PromptDialog;
import com.lubanjianye.biaoxuntong.util.sp.AppSharePreferenceMgr;
import com.lubanjianye.biaoxuntong.util.toast.ToastUtil;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;

import org.greenrobot.eventbus.EventBus;



public class ZhLoginFragment extends BaseFragment1 implements View.OnClickListener {

    private AppCompatEditText etLoginUsername = null;
    private AppCompatEditText etLoginPwd = null;
    private AppCompatTextView btLoginSubmit = null;

    private PromptDialog promptDialog = null;

    private long id = 0;
    private String mobile = "";
    private String nickName = "";
    private String token = "";
    private String comid = "";
    private String imageUrl = "";
    private String companyName = "";

    private String clientID = PushManager.getInstance().getClientid(BiaoXunTong.getApplicationContext());

    @Override
    public Object setLayout() {
        return R.layout.zh_login;
    }

    @Override
    public void initView() {
        etLoginUsername = getView().findViewById(R.id.et_login_username);
        etLoginPwd = getView().findViewById(R.id.et_login_pwd);
        btLoginSubmit = getView().findViewById(R.id.bt_login_submit);
        btLoginSubmit.setOnClickListener(this);


    }

    @Override
    public void initData() {
        //初始化控件状态数据
        String holdUsername = (String) AppSharePreferenceMgr.get(getContext(), "username", "");
        etLoginUsername.setText(holdUsername);
        etLoginUsername.setSelection(holdUsername.length());
        //创建对象
        promptDialog = new PromptDialog(getActivity());
        //设置自定义属性
        promptDialog.getDefaultBuilder().touchAble(true).round(3).loadingDuration(3000);

        etLoginUsername.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @SuppressWarnings("deprecation")
            @Override
            public void afterTextChanged(Editable s) {
                String username = s.toString().trim();
                if (username.length() > 0) {

                } else {

                }


            }
        });

        etLoginPwd.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @SuppressWarnings("deprecation")
            @Override
            public void afterTextChanged(Editable s) {
                int length = s.length();
                if (length > 0) {

                } else {
                }

                String username = etLoginUsername.getText().toString().trim();
                if (TextUtils.isEmpty(username)) {
                    ToastUtil.shortBottonToast(getContext(), "用户名未填写!");
                }

            }
        });
    }

    @Override
    public void initEvent() {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bt_login_submit:
                //账号密码登录
                loginRequest();
                break;
            default:
                break;
        }
    }


    @SuppressWarnings("ConstantConditions")
    private void loginRequest() {

        final String username = etLoginUsername.getText().toString().trim();
        final String password = etLoginPwd.getText().toString().trim();

        if (!TextUtils.isEmpty(password) && !TextUtils.isEmpty(username)) {


            promptDialog.showLoading("正在登陆");
            //登录
            OkGo.<String>post(BiaoXunTongApi.URL_LOGIN)
                    .params("username", username)
                    .params("password", password)
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
                                token = userInfo.getString("token");
                                comid = userInfo.getString("comid");

                                //登录成功，获取用户信息
                                getUserInfo(id);
                                promptDialog.dismissImmediately();

                            } else {
                                promptDialog.dismissImmediately();
                                ToastUtil.shortBottonToast(getContext(), message);

                            }
                        }
                    });


        } else {
            ToastUtil.shortBottonToast(getContext(), "账号或密码错误");
        }

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
                            holdAccount();
                            AppSharePreferenceMgr.put(BiaoXunTong.getApplicationContext(), EventMessage.LOGIN_SUCCSS, true);
                            EventBus.getDefault().post(new EventMessage(EventMessage.LOGIN_SUCCSS));
                            getActivity().onBackPressed();
                            ToastUtil.shortBottonToast(getContext(), "登陆成功");

                        } else {
                            ToastUtil.shortToast(getContext(), message);
                        }
                    }
                });


    }

    //保存账号信息
    private void holdAccount() {
        String username = etLoginUsername.getText().toString().trim();
        if (!TextUtils.isEmpty(username)) {
            if (AppSharePreferenceMgr.contains(getContext(), "username")) {
                AppSharePreferenceMgr.remove(getContext(), "username");
            }
            AppSharePreferenceMgr.put(getContext(), "username", username);
        }
    }

}
