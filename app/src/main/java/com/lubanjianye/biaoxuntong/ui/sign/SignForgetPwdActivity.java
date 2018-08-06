package com.lubanjianye.biaoxuntong.ui.sign;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.AppCompatTextView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.LinearLayout;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.lubanjianye.biaoxuntong.R;
import com.lubanjianye.biaoxuntong.app.BiaoXunTongApi;
import com.lubanjianye.biaoxuntong.base.BaseActivity;
import com.lubanjianye.biaoxuntong.util.parser.RichTextParser;
import com.lubanjianye.biaoxuntong.util.toast.ToastUtil;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;

import butterknife.BindView;

import butterknife.OnClick;


@Route(path = "/com/SignForgetPwdActivity")
public class SignForgetPwdActivity extends BaseActivity {


    @BindView(R.id.ll_iv_back)
    LinearLayout llIvBack;
    @BindView(R.id.main_bar_name)
    AppCompatTextView mainBarName;
    @BindView(R.id.message_num)
    AppCompatTextView messageNum;
    @BindView(R.id.ll_message)
    LinearLayout llMessage;
    @BindView(R.id.et_new_username)
    AppCompatEditText etNewUsername;
    @BindView(R.id.et_new_code)
    AppCompatEditText etNewCode;
    @BindView(R.id.tv_new_sms_call)
    AppCompatTextView tvNewSmsCall;
    @BindView(R.id.et_new_pwd)
    AppCompatEditText etNewPwd;
    @BindView(R.id.et_new_pwd_sure)
    AppCompatEditText etNewPwdSure;
    @BindView(R.id.bt_new_submit)
    AppCompatTextView btNewSubmit;

    private boolean mMachPhoneNum = false;
    private CountDownTimer mTimer = null;


    @Override
    protected int getLayoutId() {
        return R.layout.fragment_forget_pwd;
    }

    @Override
    protected void initData(Bundle savedInstanceState) {
        llIvBack.setVisibility(View.VISIBLE);
        mainBarName.setText("重置密码");

        etNewUsername.addTextChangedListener(
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
                            tvNewSmsCall.setAlpha(0.4f);
                        } else if (length == 11) {
                            if (mMachPhoneNum) {
                                if (tvNewSmsCall.getTag() == null) {
                                    tvNewSmsCall.setAlpha(1.0f);
                                } else {
                                    tvNewSmsCall.setAlpha(0.4f);
                                }
                            } else {
                                ToastUtil.shortBottonToast(SignForgetPwdActivity.this, "请输入正确的手机号码");
                                tvNewSmsCall.setAlpha(0.4f);
                            }
                        } else if (length > 11) {
                            tvNewSmsCall.setAlpha(0.4f);
                        } else if (length <= 0) {
                            tvNewSmsCall.setAlpha(0.4f);
                        }


                    }
                }
        );


        etNewPwd.addTextChangedListener(new TextWatcher() {
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
                } else {

                }

                String username = etNewUsername.getText().toString().trim();
                String smsCode = etNewCode.getText().toString().trim();
                if (TextUtils.isEmpty(username)) {
                    ToastUtil.shortBottonToast(SignForgetPwdActivity.this, "用户名未填写！");
                }
                if (TextUtils.isEmpty(smsCode)) {
                    ToastUtil.shortBottonToast(SignForgetPwdActivity.this, "验证码未填写！");
                }
            }
        });
    }

    @Override
    protected void initEvent(Bundle savedInstanceState) {

    }


    @OnClick({R.id.ll_iv_back, R.id.tv_new_sms_call, R.id.bt_new_submit})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.ll_iv_back:
                if (mTimer != null) {
                    mTimer.onFinish();
                    mTimer.cancel();
                }
                finish();
                break;
            case R.id.tv_new_sms_call:
                requestSmsCode();
                break;
            case R.id.bt_new_submit:
                requestRegister();
                break;
        }
    }

    //获取验证码
    private void requestSmsCode() {

        if (!mMachPhoneNum) {
            return;
        }
        if (tvNewSmsCall.getTag() == null) {
            tvNewSmsCall.setAlpha(0.6f);
            tvNewSmsCall.setTag(true);

            mTimer = new CountDownTimer(60 * 1000, 1000) {

                @SuppressLint("DefaultLocale")
                @Override
                public void onTick(long millisUntilFinished) {
                    tvNewSmsCall.setText(String.format("%s%s%d%s",
                            getResources().getString(R.string.register_sms_hint), "(", millisUntilFinished / 1000, ")"));
                }

                @Override
                public void onFinish() {
                    tvNewSmsCall.setTag(null);
                    tvNewSmsCall.setText(getResources().getString(R.string.register_sms_hint));
                    tvNewSmsCall.setAlpha(1.0f);
                }
            }.start();

            final String phone = etNewUsername.getText().toString().trim();

            OkGo.<String>post(BiaoXunTongApi.URL_GETCODE)
                    .params("phone", phone)
                    .params("type", "2")
                    .execute(new StringCallback() {
                        @Override
                        public void onSuccess(Response<String> response) {
                            final JSONObject profileJson = JSON.parseObject(response.body());
                            final String status = profileJson.getString("status");
                            final String message = profileJson.getString("message");
                            if ("200".equals(status)) {
                                ToastUtil.shortBottonToast(SignForgetPwdActivity.this, "验证码发送成功");
                            } else {
                                if (mTimer != null) {
                                    mTimer.onFinish();
                                    mTimer.cancel();
                                }
                                ToastUtil.shortBottonToast(SignForgetPwdActivity.this, message);
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
            ToastUtil.shortBottonToast(SignForgetPwdActivity.this, "别激动，休息一下吧...");
        }
    }

    //修改密码
    private void requestRegister() {
        final String mobile = etNewUsername.getText().toString().trim();
        final String code = etNewCode.getText().toString().trim();
        final String pass = etNewPwd.getText().toString().trim();
        final String pass_sure = etNewPwdSure.getText().toString().trim();
        if (TextUtils.isEmpty(mobile)) {
            ToastUtil.shortBottonToast(SignForgetPwdActivity.this, "请输入手机号");
            return;
        }
        if (!mMachPhoneNum || TextUtils.isEmpty(code)) {
            ToastUtil.shortBottonToast(SignForgetPwdActivity.this, "验证码不正确");
            return;
        }
        if (TextUtils.isEmpty(pass) || pass.length() < 5 || pass.length() > 12) {
            ToastUtil.shortBottonToast(SignForgetPwdActivity.this, "密码格式不对");
            return;
        }
        if (!pass.equals(pass_sure)) {
            ToastUtil.shortBottonToast(SignForgetPwdActivity.this, "两次输入密码不一致");
            return;
        }

        OkGo.<String>post(BiaoXunTongApi.URL_FORGETPWD)
                .params("code", mobile + "_" + code)
                .params("newPass", pass)
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
                            ToastUtil.shortBottonToast(SignForgetPwdActivity.this, "重置密码成功，请登录！");
                            //跳到登陆页面
                            ARouter.getInstance().build("/com/SignInActivity").navigation();
                            finish();
                        } else {
                            ToastUtil.shortBottonToast(SignForgetPwdActivity.this, message);
                        }
                    }

                    @Override
                    public void onError(Response<String> response) {
                        if (mTimer != null) {
                            mTimer.onFinish();
                            mTimer.cancel();
                        }
                        ToastUtil.shortBottonToast(SignForgetPwdActivity.this, "修改失败！");
                        super.onError(response);
                    }
                });
    }

    @Override
    protected void onDestroy() {
        if (mTimer != null) {
            mTimer.onFinish();
            mTimer.cancel();
        }
        super.onDestroy();
    }
}
