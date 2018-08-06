package com.lubanjianye.biaoxuntong.base;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.FrameLayout;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.lubanjianye.biaoxuntong.R;
import com.lubanjianye.biaoxuntong.app.BiaoXunTongApi;
import com.lubanjianye.biaoxuntong.database.DatabaseManager;
import com.lubanjianye.biaoxuntong.database.UserProfile;
import com.lubanjianye.biaoxuntong.eventbus.EventMessage;
import com.lubanjianye.biaoxuntong.ui.main.collection.CollectionTabFragment;
import com.lubanjianye.biaoxuntong.ui.main.index.IndexTabFragment;
import com.lubanjianye.biaoxuntong.ui.main.query.QueryFragment;
import com.lubanjianye.biaoxuntong.ui.main.result.ResultTabFragment;
import com.lubanjianye.biaoxuntong.ui.main.user.UserTabFragment;
import com.lubanjianye.biaoxuntong.ui.view.botton.BottomBar;
import com.lubanjianye.biaoxuntong.ui.view.botton.BottomBarTab;
import com.lubanjianye.biaoxuntong.util.sp.AppSharePreferenceMgr;
import com.lubanjianye.biaoxuntong.util.toast.ToastUtil;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;

import butterknife.BindView;

import butterknife.Unbinder;


public class MainFragment extends BaseFragment {

    public static final int FIRST = 0;
    public static final int SECOND = 1;
    public static final int THIRD = 2;
    public static final int FOUR = 3;
    public static final int FIVE = 4;
    @BindView(R.id.main_tab_container)
    FrameLayout mainTabContainer;
    @BindView(R.id.bottomBar)
    BottomBar bottomBar;
    Unbinder unbinder;

    private BaseFragment1[] mFragments = new BaseFragment1[5];



    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        BaseFragment1 firstFragment = findChildFragment(IndexTabFragment.class);
        if (firstFragment == null) {
            mFragments[FIRST] = new IndexTabFragment();
            mFragments[SECOND] = new QueryFragment();
            mFragments[THIRD] = new CollectionTabFragment();
            mFragments[FOUR] = new ResultTabFragment();
            mFragments[FIVE] = new UserTabFragment();

            loadMultipleRootFragment(R.id.main_tab_container, FIRST,
                    mFragments[FIRST],
                    mFragments[SECOND],
                    mFragments[THIRD],
                    mFragments[FOUR],
                    mFragments[FIVE]);
        } else {
            // 这里库已经做了Fragment恢复,所有不需要额外的处理了, 不会出现重叠问题

            // 这里我们需要拿到mFragments的引用
            mFragments[FIRST] = firstFragment;
            mFragments[SECOND] = findChildFragment(QueryFragment.class);
            mFragments[THIRD] = findChildFragment(CollectionTabFragment.class);
            mFragments[FOUR] = findChildFragment(ResultTabFragment.class);
            mFragments[FIVE] = findChildFragment(UserTabFragment.class);
        }

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

        //取消注册EventBus
        EventBus.getDefault().unregister(this);
        unbinder.unbind();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_main;
    }

    @Override
    protected void initData(Bundle savedInstanceState) {
        //注册EventBus
        EventBus.getDefault().register(this);

        bottomBar
                .addItem(new BottomBarTab(_mActivity, R.mipmap.main_index_tab, getString(R.string.first)))
                .addItem(new BottomBarTab(_mActivity, R.mipmap.main_query_tab, getString(R.string.second)))
                .addItem(new BottomBarTab(_mActivity, R.mipmap.main_collection_tab, getString(R.string.third)))
                .addItem(new BottomBarTab(_mActivity, R.mipmap.main_result_tab, getString(R.string.four)))
                .addItem(new BottomBarTab(_mActivity, R.mipmap.main_user_tab, getString(R.string.five)));


        bottomBar.setOnTabSelectedListener(new BottomBar.OnTabSelectedListener() {
            @Override
            public void onTabSelected(int position, int prePosition) {
                showHideFragment(mFragments[position], mFragments[prePosition]);

            }

            @Override
            public void onTabUnselected(int position) {

            }

            @Override
            public void onTabReselected(int position) {
                // 在FirstPagerFragment,FirstHomeFragment中接收, 因为是嵌套的Fragment
                // 主要为了交互: 重选tab 如果列表不在顶部则移动到顶部,如果已经在顶部,则刷新
                if (position == 0) {
                    EventBus.getDefault().post(new EventMessage("sx"));
                }
            }
        });


        if (AppSharePreferenceMgr.contains(getContext(), EventMessage.LOGIN_SUCCSS)) {
            showMessageCount();
        }
    }

    @Override
    protected void initEnvent(Bundle savedInstanceState) {

    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void XXXXXX(EventMessage message) {

        if (EventMessage.READ_STATUS.equals(message.getMessage()) || EventMessage.LOGIN_SUCCSS.equals(message.getMessage())) {
            showMessageCount();
        } else if (EventMessage.LOGIN_OUT.equals(message.getMessage())) {
            bottomBar.getItem(4).setUnreadCount(-1);
        }

    }



    private long id = 0;

    private void showMessageCount() {
        List<UserProfile> users = DatabaseManager.getInstance().getDao().loadAll();
        for (int i = 0; i < users.size(); i++) {
            id = users.get(0).getId();
        }

        OkGo.<String>post(BiaoXunTongApi.URL_GETUSERINFO)
                .params("Id", id)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        final JSONObject userInfo = JSON.parseObject(response.body());
                        final String status = userInfo.getString("status");
                        final String message = userInfo.getString("message");

                        if ("200".equals(status)) {
                            final JSONObject data = userInfo.getJSONObject("data");
                            final int messNum = data.getInteger("mesCount");
                            if (messNum > 0) {
                                bottomBar.getItem(4).setUnreadCount(0);
                            } else {
                                bottomBar.getItem(4).setUnreadCount(-1);
                            }
                        } else {
                            bottomBar.getItem(4).setUnreadCount(-1);
                        }
                    }
                });
    }


    // 再点一次退出程序时间设置
    private static final long WAIT_TIME = 3000L;
    private long TOUCH_TIME = 0;

    /**
     * 处理回退事件
     *
     * @return
     */
    @Override
    public boolean onBackPressedSupport() {
        if (System.currentTimeMillis() - TOUCH_TIME < WAIT_TIME) {
            _mActivity.finish();
        } else {
            TOUCH_TIME = System.currentTimeMillis();
            ToastUtil.shortToast(getContext(), "再按一次退出程序");
        }
        return true;
    }


}
