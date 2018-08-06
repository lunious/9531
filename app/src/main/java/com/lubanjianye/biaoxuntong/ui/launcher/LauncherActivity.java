package com.lubanjianye.biaoxuntong.ui.launcher;

import android.content.Intent;
import android.os.Bundle;

import com.alibaba.android.arouter.launcher.ARouter;
import com.lubanjianye.biaoxuntong.R;
import com.lubanjianye.biaoxuntong.app.BiaoXunTong;
import com.lubanjianye.biaoxuntong.app.BiaoXunTongApi;
import com.lubanjianye.biaoxuntong.base.BaseActivity;
import com.lubanjianye.biaoxuntong.base.MainActivity;
import com.lubanjianye.biaoxuntong.database.DatabaseManager;
import com.lubanjianye.biaoxuntong.database.UserProfile;
import com.lubanjianye.biaoxuntong.eventbus.EventMessage;
import com.lubanjianye.biaoxuntong.ui.main.user.avater.AccountActivity;
import com.lubanjianye.biaoxuntong.util.sp.AppSharePreferenceMgr;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;

import java.util.List;


public class LauncherActivity extends BaseActivity {


    private long userId = 0;
    private String token = null;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_launcher;
    }

    @Override
    protected void initData(Bundle savedInstanceState) {
        //如果登录，检查token是否有效
        if (AppSharePreferenceMgr.contains(this, EventMessage.LOGIN_SUCCSS)) {
            //得到用个户userId
            List<UserProfile> users = DatabaseManager.getInstance().getDao().loadAll();
            for (int i = 0; i < users.size(); i++) {
                userId = users.get(0).getId();
                token = users.get(0).getToken();
            }


            OkGo.<String>post(BiaoXunTongApi.URL_CHECKTOKEN)
                    .params("userId", userId)
                    .params("token", token)
                    .execute(new StringCallback() {
                        @Override
                        public void onSuccess(Response<String> response) {


                            if ("200".equals(response.body()) || "400".equals(response.body())) {
                                //token有效
                                BiaoXunTong.getHandler().postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        checkIsShowScroll();
                                    }
                                }, 1000);

                            } else {
                                //清除登录信息
                                DatabaseManager.getInstance().getDao().deleteAll();
                                AppSharePreferenceMgr.remove(LauncherActivity.this, EventMessage.LOGIN_SUCCSS);
                                AppSharePreferenceMgr.put(LauncherActivity.this, EventMessage.TOKEN_FALSE, "true");
                                BiaoXunTong.getHandler().postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        checkIsShowScroll();
                                    }
                                }, 1000);
                            }
                        }

                        @Override
                        public void onError(Response<String> response) {
                            BiaoXunTong.getHandler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    checkIsShowScroll();
                                }
                            }, 1000);
                        }

                    });

        } else {
            BiaoXunTong.getHandler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    AppSharePreferenceMgr.remove(LauncherActivity.this, EventMessage.TOKEN_FALSE);
                    checkIsShowScroll();
                }
            }, 1000);
        }

    }

    @Override
    protected void initEvent(Bundle savedInstanceState) {

    }

    //判断是否显示滑动启动页
    private void checkIsShowScroll() {

        if (!AppSharePreferenceMgr.contains(this, "first_into_app")) {
            //进入引导页
            ARouter.getInstance().build("/com/LauncherScrollActivity").navigation();
            finish();
        } else {
            //进入主页
//            ARouter.getInstance().build("/com/MainActivity").navigation();
            startActivity(new Intent(LauncherActivity.this, MainActivity.class));
            finish();
        }


    }
}
