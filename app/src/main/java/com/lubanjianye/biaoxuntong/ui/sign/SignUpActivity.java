package com.lubanjianye.biaoxuntong.ui.sign;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v7.widget.AppCompatTextView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.lubanjianye.biaoxuntong.R;
import com.lubanjianye.biaoxuntong.app.BiaoXunTongApi;
import com.lubanjianye.biaoxuntong.base.BaseActivity;
import com.lubanjianye.biaoxuntong.util.dialog.PromptDialog;
import com.lubanjianye.biaoxuntong.util.parser.RichTextParser;
import com.lubanjianye.biaoxuntong.util.sp.AppSharePreferenceMgr;
import com.lubanjianye.biaoxuntong.util.toast.ToastUtil;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


@Route(path = "/com/SignUpActivity")
public class SignUpActivity extends BaseActivity {


    @BindView(R.id.ll_iv_back)
    LinearLayout llIvBack;
    @BindView(R.id.main_bar_name)
    AppCompatTextView mainBarName;
    @BindView(R.id.message_num)
    AppCompatTextView messageNum;
    @BindView(R.id.ll_message)
    LinearLayout llMessage;
    @BindView(R.id.et_register_username)
    EditText etRegisterUsername;
    @BindView(R.id.et_register_code)
    EditText etRegisterCode;
    @BindView(R.id.tv_register_sms_call)
    TextView tvRegisterSmsCall;
    @BindView(R.id.et_register_pwd)
    EditText etRegisterPwd;
    @BindView(R.id.et_register_pwd_sure)
    EditText etRegisterPwdSure;
    @BindView(R.id.bt_register_submit)
    AppCompatTextView btRegisterSubmit;
    @BindView(R.id.tv_zhucexieyi)
    TextView tvZhucexieyi;

    private PromptDialog promptDialog = null;
    private boolean mMachPhoneNum;
    private CountDownTimer mTimer;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_sign_up;
    }

    @Override
    protected void initData(Bundle savedInstanceState) {
        llIvBack.setVisibility(View.VISIBLE);
        mainBarName.setText("注册");

        //创建对象
        promptDialog = new PromptDialog(this);
        //设置自定义属性
        promptDialog.getDefaultBuilder().touchAble(true).round(3).loadingDuration(3000);

    }

    @Override
    protected void initEvent(Bundle savedInstanceState) {
        etRegisterUsername.addTextChangedListener(
                new TextWatcher() {

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
                        String input = s.toString();
                        mMachPhoneNum = RichTextParser.machPhoneNum(input);


                        if (length > 0 && length < 11) {
                            tvRegisterSmsCall.setAlpha(0.4f);
                        } else if (length == 11) {
                            if (mMachPhoneNum) {
                                if (tvRegisterSmsCall.getTag() == null) {
                                    tvRegisterSmsCall.setAlpha(1.0f);
                                } else {
                                    tvRegisterSmsCall.setAlpha(0.4f);
                                }
                            } else {
                                ToastUtil.shortToast(SignUpActivity.this, "请输入正确的手机号码");
                                tvRegisterSmsCall.setAlpha(0.4f);
                            }
                        } else if (length > 11) {
                            tvRegisterSmsCall.setAlpha(0.4f);
                        } else if (length <= 0) {
                            tvRegisterSmsCall.setAlpha(0.4f);
                        }


                    }
                }
        );

        etRegisterCode.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @SuppressWarnings("deprecation")
            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        etRegisterPwd.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                int length = s.length();
                if (length > 0) {
                    ;
                } else {

                }

                String username = etRegisterUsername.getText().toString().trim();
                String smsCode = etRegisterCode.getText().toString().trim();
                if (TextUtils.isEmpty(username)) {
                    ToastUtil.shortBottonToast(SignUpActivity.this, "用户名未填写");
                }
                if (TextUtils.isEmpty(smsCode)) {
                    ToastUtil.shortBottonToast(SignUpActivity.this, "验证码未填写");
                }
            }
        });
    }

    //获取验证码
    private void requestSmsCode() {

        if (!mMachPhoneNum) {
            return;
        }
        if (tvRegisterSmsCall.getTag() == null) {
            tvRegisterSmsCall.setAlpha(0.6f);
            tvRegisterSmsCall.setTag(true);

            mTimer = new CountDownTimer(60 * 1000, 1000) {

                @SuppressLint("DefaultLocale")
                @Override
                public void onTick(long millisUntilFinished) {
                    tvRegisterSmsCall.setText(String.format("%s%s%d%s",
                            getResources().getString(R.string.register_sms_hint), "(", millisUntilFinished / 1000, ")"));
                }

                @Override
                public void onFinish() {
                    tvRegisterSmsCall.setTag(null);
                    tvRegisterSmsCall.setText(getResources().getString(R.string.register_sms_hint));
                    tvRegisterSmsCall.setAlpha(1.0f);
                }
            }.start();

            final String phone = etRegisterUsername.getText().toString().trim();

            OkGo.<String>post(BiaoXunTongApi.URL_GETCODE)
                    .params("phone", phone)
                    .params("type", "1")
                    .execute(new StringCallback() {
                        @Override
                        public void onSuccess(Response<String> response) {
                            final JSONObject profileJson = JSON.parseObject(response.body());
                            final String status = profileJson.getString("status");
                            final String message = profileJson.getString("message");
                            if ("200".equals(status)) {
                                ToastUtil.shortBottonToast(SignUpActivity.this, "验证码发送成功");

                            } else {
                                if (mTimer != null) {
                                    mTimer.onFinish();
                                    mTimer.cancel();
                                }
                                ToastUtil.shortBottonToast(SignUpActivity.this, message);
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
            ToastUtil.shortBottonToast(SignUpActivity.this, "别激动，休息一下吧...");
        }
    }

    //请求注册
    private void requestRegister() {

        final String mobile = etRegisterUsername.getText().toString().trim();
        final String code = etRegisterCode.getText().toString().trim();
        final String pass = etRegisterPwd.getText().toString().trim();
        final String pass_sure = etRegisterPwdSure.getText().toString().trim();
        if (TextUtils.isEmpty(mobile)) {
            ToastUtil.shortBottonToast(SignUpActivity.this, "请输入手机号");

            return;
        }
        if (!mMachPhoneNum || TextUtils.isEmpty(code)) {
            ToastUtil.shortBottonToast(SignUpActivity.this, "验证码不正确");

            return;
        }
        if (TextUtils.isEmpty(pass) || pass.length() < 5 || pass.length() > 12) {
            ToastUtil.shortBottonToast(SignUpActivity.this, "密码格式不对");
            return;
        }

        if (!pass.equals(pass_sure)) {
            ToastUtil.shortBottonToast(SignUpActivity.this, "两次输入密码不一致");
            return;
        }

        if (!TextUtils.isEmpty(mobile) && !TextUtils.isEmpty(code) && !TextUtils.isEmpty(pass)) {
            promptDialog.showLoading("正在注册...");

            OkGo.<String>post(BiaoXunTongApi.URL_REGISTER)
                    .params("mobile", mobile)
                    .params("code", mobile + "_" + code)
                    .params("pass", pass)
                    .execute(new StringCallback() {
                        @Override
                        public void onSuccess(Response<String> response) {
                            final JSONObject profileJson = JSON.parseObject(response.body());
                            final String status = profileJson.getString("status");
                            final String message = profileJson.getString("message");
                            if ("200".equals(status)) {
                                if (mTimer != null) {
                                    mTimer.onFinish();
                                    mTimer.cancel();
                                }
                                promptDialog.dismissImmediately();
                                ToastUtil.shortBottonToast(SignUpActivity.this, "注册成功，请登录！");
                                //跳到登陆页面
                                ARouter.getInstance().build("/com/SignUpActivity").navigation();
                                finish();
                                holdAccount();
                            } else {
                                promptDialog.dismissImmediately();
                                ToastUtil.shortBottonToast(SignUpActivity.this, message);
                            }
                        }

                        @Override
                        public void onError(Response<String> response) {
                            if (mTimer != null) {
                                mTimer.onFinish();
                                mTimer.cancel();
                            }
                            ToastUtil.shortBottonToast(SignUpActivity.this, "注册失败！");
                            super.onError(response);
                        }
                    });
        }

    }

    //保存账号信息
    private void holdAccount() {
        String username = etRegisterUsername.getText().toString().trim();
        if (!TextUtils.isEmpty(username)) {
            if (AppSharePreferenceMgr.contains(SignUpActivity.this, "username")) {
                AppSharePreferenceMgr.remove(SignUpActivity.this, "username");
            }
            AppSharePreferenceMgr.put(SignUpActivity.this, "username", username);
        }
    }

    @Override
    protected void onDestroy() {
        if (mTimer != null) {
            mTimer.onFinish();
            mTimer.cancel();
        }
        super.onDestroy();
    }

    @OnClick({R.id.ll_iv_back, R.id.tv_register_sms_call, R.id.bt_register_submit, R.id.tv_zhucexieyi})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.ll_iv_back:
                if (mTimer != null) {
                    mTimer.onFinish();
                    mTimer.cancel();
                }
                finish();
                break;
            case R.id.tv_register_sms_call:
                //请求验证码
                requestSmsCode();
                break;
            case R.id.bt_register_submit:
                //注册
                requestRegister();
                break;
            case R.id.tv_zhucexieyi:
                ARouter.getInstance().build("/com/ZhuCeXYActivity").navigation();
                break;
        }
    }
}
