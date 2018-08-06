package com.lubanjianye.biaoxuntong.ui.sign;

import android.annotation.SuppressLint;
import android.os.CountDownTimer;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.AppCompatTextView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.lubanjianye.biaoxuntong.R;
import com.lubanjianye.biaoxuntong.base.BaseFragment1;
import com.lubanjianye.biaoxuntong.database.DatabaseManager;
import com.lubanjianye.biaoxuntong.database.UserProfile;
import com.lubanjianye.biaoxuntong.eventbus.EventMessage;
import com.lubanjianye.biaoxuntong.app.BiaoXunTongApi;
import com.lubanjianye.biaoxuntong.util.dialog.PromptDialog;
import com.lubanjianye.biaoxuntong.util.parser.RichTextParser;
import com.lubanjianye.biaoxuntong.util.sp.AppSharePreferenceMgr;
import com.lubanjianye.biaoxuntong.util.toast.ToastUtil;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;

import org.greenrobot.eventbus.EventBus;


public class YzmLoginFragment extends BaseFragment1 implements View.OnClickListener {

    private AppCompatEditText etRetrieveTel = null;
    private AppCompatEditText etRetrieveCodeInput = null;
    private AppCompatTextView retrieveSmsCall = null;
    private AppCompatButton btRetrieveSubmit = null;

    private PromptDialog promptDialog = null;

    private long id = 0;
    private String mobile = "";
    private String nickName = "";
    private String token = "";
    private String comid = "";
    private String imageUrl = "";
    private String companyName = "";

    private boolean mMachPhoneNum = false;
    private CountDownTimer mTimer = null;


    @Override
    public Object setLayout() {
        return R.layout.yzm_login;
    }

    @Override
    public void initView() {

        etRetrieveTel = getView().findViewById(R.id.et_retrieve_tel);
        etRetrieveCodeInput = getView().findViewById(R.id.et_retrieve_code_input);
        retrieveSmsCall = getView().findViewById(R.id.retrieve_sms_call);
        btRetrieveSubmit = getView().findViewById(R.id.bt_retrieve_submit);
        retrieveSmsCall.setOnClickListener(this);
        btRetrieveSubmit.setOnClickListener(this);


    }

    @Override
    public void initData() {
        //创建对象
        promptDialog = new PromptDialog(getActivity());
        //设置自定义属性
        promptDialog.getDefaultBuilder().touchAble(true).round(3).loadingDuration(3000);

    }

    @Override
    public void initEvent() {

        etRetrieveTel.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                int length = s.length();
                if (length > 0) {

                } else {

                }

            }

            @SuppressWarnings("deprecation")
            @Override
            public void afterTextChanged(Editable s) {
                int length = s.length();
                String input = s.toString();
                mMachPhoneNum = RichTextParser.machPhoneNum(input);

                if (length > 0 && length < 11) {

                    retrieveSmsCall.setAlpha(0.4f);

                } else if (length == 11) {
                    if (mMachPhoneNum) {

                        if (retrieveSmsCall.getTag() == null) {
                            retrieveSmsCall.setAlpha(1.0f);
                        } else {
                            retrieveSmsCall.setAlpha(0.4f);
                        }
                    } else {

                        ToastUtil.shortBottonToast(getContext(), "请输入正确的手机号码");
                        retrieveSmsCall.setAlpha(0.4f);
                    }
                } else if (length > 11) {
                    retrieveSmsCall.setAlpha(0.4f);

                } else if (length <= 0) {
                    retrieveSmsCall.setAlpha(0.4f);

                }

            }
        });

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.retrieve_sms_call:
                requestSmsCode();
                break;
            case R.id.bt_retrieve_submit:
                loginRequest();
                break;
            default:
                break;
        }
    }

    private void requestSmsCode() {

        if (!mMachPhoneNum) {
            return;
        }

        if (retrieveSmsCall.getTag() == null) {
            retrieveSmsCall.setAlpha(0.6f);
            retrieveSmsCall.setTag(true);

            mTimer = new CountDownTimer(60 * 1000, 1000) {

                @SuppressLint("DefaultLocale")
                @Override
                public void onTick(long millisUntilFinished) {
                    retrieveSmsCall.setText(String.format("%s%s%d%s",
                            getResources().getString(R.string.register_sms_hint), "(", millisUntilFinished / 1000, ")"));
                }

                @Override
                public void onFinish() {
                    retrieveSmsCall.setTag(null);
                    retrieveSmsCall.setText(getResources().getString(R.string.register_sms_hint));
                    retrieveSmsCall.setAlpha(1.0f);
                }
            }.start();

            final String phone = etRetrieveTel.getText().toString().trim();
            //获取验证码
            OkGo.<String>post(BiaoXunTongApi.URL_GETCODE)
                    .params("phone", phone)
                    .params("type", "3")
                    .execute(new StringCallback() {
                        @Override
                        public void onSuccess(Response<String> response) {
                            final JSONObject profileJson = JSON.parseObject(response.body());
                            final String status = profileJson.getString("status");
                            final String message = profileJson.getString("message");
                            if ("200".equals(status)) {
                                ToastUtil.shortToast(getContext(), "验证码发送成功");
                            } else {
                                if (mTimer != null) {
                                    mTimer.onFinish();
                                    mTimer.cancel();
                                }
                                ToastUtil.shortToast(getContext(), message);
                            }
                        }

                        @Override
                        public void onError(Response<String> response) {
                            if (mTimer != null) {
                                mTimer.onFinish();
                                mTimer.cancel();
                            }
                            super.onError(response);
                        }
                    });

        } else {
            ToastUtil.shortBottonToast(getContext(), "别激动，休息一下吧...");
        }
    }

    @SuppressWarnings("ConstantConditions")
    private void loginRequest() {

        final String username = etRetrieveTel.getText().toString().trim();
        final String password = etRetrieveCodeInput.getText().toString().trim();


        if (!TextUtils.isEmpty(password) && !TextUtils.isEmpty(username)) {
            //登录
            promptDialog.showLoading("正在登陆");
            OkGo.<String>post(BiaoXunTongApi.URL_FASTLOGIN)
                    .params("mobile", username)
                    .params("code", username + "_" + password)
                    .execute(new StringCallback() {
                        @Override
                        public void onSuccess(Response<String> response) {


                            Log.d("YHBADSDASDASD",response.body());

                            final JSONObject profileJson = JSON.parseObject(response.body());
                            final String status = profileJson.getString("status");
                            final String message = profileJson.getString("message");
                            if ("200".equals(status)) {
                                if (mTimer != null) {
                                    mTimer.onFinish();
                                    mTimer.cancel();
                                }

                                final JSONObject userInfo = JSON.parseObject(response.body()).getJSONObject("data");
                                id = userInfo.getLong("id");
                                token = response.headers().get("token");
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
            ToastUtil.shortBottonToast(getContext(), "手机号或验证码有误");
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
                            promptDialog.dismissImmediately();
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
                            AppSharePreferenceMgr.put(getContext(), EventMessage.LOGIN_SUCCSS, true);
                            EventBus.getDefault().post(new EventMessage(EventMessage.LOGIN_SUCCSS));
                            getActivity().onBackPressed();
                            ToastUtil.shortBottonToast(getContext(), "登陆成功");

                        } else {
                            ToastUtil.shortToast(getContext(), message);
                        }
                    }
                });


    }


    @Override
    public void onDestroyView() {
        if (mTimer != null) {
            mTimer.onFinish();
            mTimer.cancel();
        }
        super.onDestroyView();
    }
}
