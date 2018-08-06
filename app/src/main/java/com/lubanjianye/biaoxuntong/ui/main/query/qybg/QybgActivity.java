package com.lubanjianye.biaoxuntong.ui.main.query.qybg;

import android.os.Bundle;
import android.support.v7.widget.AppCompatTextView;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Autowired;
import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.lubanjianye.biaoxuntong.R;
import com.lubanjianye.biaoxuntong.app.BiaoXunTongApi;
import com.lubanjianye.biaoxuntong.base.BaseActivity;
import com.lubanjianye.biaoxuntong.database.DatabaseManager;
import com.lubanjianye.biaoxuntong.database.UserProfile;
import com.lubanjianye.biaoxuntong.util.dialog.PromptDialog;
import com.lubanjianye.biaoxuntong.util.toast.ToastUtil;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;

import java.util.List;
import java.util.regex.Pattern;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Author: lunious
 * Date: 2018/7/11 10:24
 * Description:
 */
@Route(path = "/com/QybgActivity")
public class QybgActivity extends BaseActivity {

    @BindView(R.id.ll_iv_back)
    LinearLayout llIvBack;
    @BindView(R.id.main_bar_name)
    AppCompatTextView mainBarName;
    @BindView(R.id.message_num)
    AppCompatTextView messageNum;
    @BindView(R.id.ll_message)
    LinearLayout llMessage;
    @BindView(R.id.tv_bgcompany)
    TextView tvBgcompany;
    @BindView(R.id.et_bgemail)
    EditText etBgemail;
    @BindView(R.id.btn_sure_send)
    TextView btnSureSend;
    @Autowired
    String sfId;
    @Autowired
    String companyName;


    private PromptDialog promptDialog;
    private String email = "";
    private long userId = 0;
    private String mobile = "";
    private String token = "";


    /**
     * 正则表达式：验证邮箱
     */
    public static final String REGEX_EMAIL = "^([a-z0-9A-Z]+[-|\\.]?)+[a-z0-9A-Z]@([a-z0-9A-Z]+(-[a-z0-9A-Z]+)?\\.)+[a-zA-Z]{2,}$";


    @Override
    protected int getLayoutId() {
        return R.layout.fragment_qybg;
    }

    @Override
    protected void initData(Bundle savedInstanceState) {
        llIvBack.setVisibility(View.VISIBLE);
        mainBarName.setText("确认企业报告");
        //创建对象
        promptDialog = new PromptDialog(this);
        //设置自定义属性
        promptDialog.getDefaultBuilder().touchAble(true).round(3).loadingDuration(3000);
    }

    @Override
    protected void initEvent(Bundle savedInstanceState) {

        tvBgcompany.setText(companyName);
        List<UserProfile> users = DatabaseManager.getInstance().getDao().loadAll();

        for (int i = 0; i < users.size(); i++) {
            userId = users.get(0).getId();
            token = users.get(0).getToken();
            mobile = users.get(0).getMobile();
        }
    }


    @OnClick({R.id.ll_iv_back, R.id.btn_sure_send})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.ll_iv_back:
                finish();
                break;
            case R.id.btn_sure_send:
                email = etBgemail.getText().toString();

                Log.d("NBAJISDASDDASD",sfId+"___"+email+"__"+mobile+"__"+token+"____"+userId);

                if (Pattern.matches(REGEX_EMAIL, email)) {
                    promptDialog.showLoading("正在发送...");
                    OkGo.<String>post(BiaoXunTongApi.URL_SENDPDF)
                            .params("sfId",sfId)
                            .params("email", email)
                            .params("mobile", mobile)
                            .params("token", token)
                            .params("userId", userId)
                            .execute(new StringCallback() {
                                @Override
                                public void onSuccess(Response<String> response) {
                                    final JSONObject object = JSON.parseObject(response.body());
                                    final String status = object.getString("status");
                                    final String message = object.getString("message");
                                    if ("200".equals(status)){
                                        promptDialog.dismissImmediately();
                                        ToastUtil.shortBottonToast(QybgActivity.this,"发送成功，请注意查收邮箱!");
                                        finish();
                                    }else {
                                        promptDialog.dismissImmediately();
                                        ToastUtil.shortBottonToast(QybgActivity.this,"请检查邮箱地址是否正确!");
                                    }
                                }
                            });
                } else {

                    ToastUtil.shortBottonToast(QybgActivity.this, "请输入正确的邮箱地址");
                }
                break;
        }
    }
}
