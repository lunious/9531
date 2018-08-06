package com.lubanjianye.biaoxuntong.ui.message;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.AppCompatTextView;
import android.view.View;
import android.widget.LinearLayout;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.flyco.tablayout.SlidingTabLayout;
import com.lubanjianye.biaoxuntong.R;
import com.lubanjianye.biaoxuntong.app.BiaoXunTongApi;
import com.lubanjianye.biaoxuntong.base.BaseActivity;
import com.lubanjianye.biaoxuntong.database.DatabaseManager;
import com.lubanjianye.biaoxuntong.database.UserProfile;
import com.lubanjianye.biaoxuntong.eventbus.EventMessage;
import com.lubanjianye.biaoxuntong.util.sp.AppSharePreferenceMgr;
import com.lubanjianye.biaoxuntong.util.toast.ToastUtil;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by 11645 on 2018/3/21.
 */

@Route(path = "/com/MessageActivity")
public class MessageActivity extends BaseActivity {

    @BindView(R.id.ll_iv_back)
    LinearLayout llIvBack;
    @BindView(R.id.main_bar_name)
    AppCompatTextView mainBarName;
    @BindView(R.id.atv_yjyd)
    AppCompatTextView atvYjyd;
    @BindView(R.id.ll_ib_add)
    LinearLayout llIbAdd;
    @BindView(R.id.message_stl_tab)
    SlidingTabLayout messageStlTab;
    @BindView(R.id.message_vp)
    ViewPager messageVp;

    private final List<String> mList = new ArrayList<String>();
    private MessageAdapter mAdapter;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_message;
    }

    @Override
    protected void initData(Bundle savedInstanceState) {
        llIvBack.setVisibility(View.VISIBLE);
        mainBarName.setText("消息中心");

        mList.add("历史推送");
        mList.add("告知消息");
    }

    @Override
    protected void initEvent(Bundle savedInstanceState) {
        mAdapter = new MessageAdapter(mList, getSupportFragmentManager());
        messageVp.setAdapter(mAdapter);
        messageStlTab.setViewPager(messageVp);
    }


    private long id = 0;

    @OnClick({R.id.ll_iv_back, R.id.atv_yjyd})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.ll_iv_back:
                finish();
                break;
            case R.id.atv_yjyd:

                if (AppSharePreferenceMgr.contains(MessageActivity.this, EventMessage.LOGIN_SUCCSS)) {
                    String type = messageStlTab.getCurrentTab() + 1 + "";

                    List<UserProfile> users = DatabaseManager.getInstance().getDao().loadAll();

                    for (int i = 0; i < users.size(); i++) {
                        id = users.get(0).getId();
                    }
                    OkGo.<String>post(BiaoXunTongApi.URL_YJYD)
                            .params("type", type)
                            .params("userId", id)
                            .execute(new StringCallback() {
                                @Override
                                public void onSuccess(Response<String> response) {

                                    final JSONObject object = JSON.parseObject(response.body());
                                    final String status = object.getString("status");
                                    final String message = object.getString("message");

                                    if ("200".equals(status)) {
                                        ToastUtil.shortBottonToast(MessageActivity.this, message);
                                        //改变已读未读状态
                                        EventBus.getDefault().post(new EventMessage(EventMessage.READ_STATUS));
                                    } else {
                                        ToastUtil.shortBottonToast(MessageActivity.this, message);
                                    }

                                }
                            });
                } else {
                    ToastUtil.shortToast(MessageActivity.this, "暂无未读标讯");
                }
                break;
        }
    }
}
