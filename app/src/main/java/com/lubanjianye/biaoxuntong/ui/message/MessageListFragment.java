package com.lubanjianye.biaoxuntong.ui.message;

import android.content.Intent;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;

import com.alibaba.android.arouter.launcher.ARouter;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.classic.common.MultipleStatusView;
import com.lubanjianye.biaoxuntong.R;
import com.lubanjianye.biaoxuntong.app.BiaoXunTongApi;
import com.lubanjianye.biaoxuntong.app.BiaoXunTong;
import com.lubanjianye.biaoxuntong.base.BaseFragment1;
import com.lubanjianye.biaoxuntong.bean.MessageListBean;
import com.lubanjianye.biaoxuntong.database.DatabaseManager;
import com.lubanjianye.biaoxuntong.database.UserProfile;
import com.lubanjianye.biaoxuntong.eventbus.EventMessage;
import com.lubanjianye.biaoxuntong.ui.main.index.detail.chongqing.IndexCqsggjyDetailActivity;
import com.lubanjianye.biaoxuntong.ui.main.index.detail.sichuan.IndexBxtgdjDetailActivity;
import com.lubanjianye.biaoxuntong.ui.main.index.detail.sichuan.IndexSggjyDetailActivity;
import com.lubanjianye.biaoxuntong.ui.main.index.detail.sichuan.IndexSggjycgrowDetailActivity;
import com.lubanjianye.biaoxuntong.ui.main.index.detail.sichuan.IndexSggjycgtableDetailActivity;
import com.lubanjianye.biaoxuntong.ui.main.index.detail.sichuan.IndexXcgggDetailActivity;
import com.lubanjianye.biaoxuntong.ui.main.result.detail.chongqing.ResultCqsggjyzbjgDetailActivity;
import com.lubanjianye.biaoxuntong.ui.main.result.detail.sichuan.ResultXjgggDetailActivity;
import com.lubanjianye.biaoxuntong.ui.sign.SignInActivity;
import com.lubanjianye.biaoxuntong.ui.view.loadmore.CustomLoadMoreView;
import com.lubanjianye.biaoxuntong.ui.main.result.detail.sichuan.ResultSggjyzbjgDetailActivity;
import com.lubanjianye.biaoxuntong.util.netStatus.AppSysMgr;
import com.lubanjianye.biaoxuntong.util.netStatus.NetUtil;
import com.lubanjianye.biaoxuntong.util.sp.AppSharePreferenceMgr;
import com.lubanjianye.biaoxuntong.util.toast.ToastUtil;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.cache.CacheMode;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 11645 on 2018/3/21.
 */

public class MessageListFragment extends BaseFragment1 implements View.OnClickListener {


    private RecyclerView messageRecycler = null;
    private SmartRefreshLayout messageRefresh = null;
    private MultipleStatusView loadingStatus = null;

    private AppCompatButton btnToLogin = null;
    private LinearLayout llShow = null;


    private String mTitle = null;
    private int mType = -1;


    private MessageListAdapter mAdapter;
    private ArrayList<MessageListBean> mDataList = new ArrayList<>();


    private String deviceId = AppSysMgr.getPsuedoUniqueID();
    private boolean isInitCache = false;
    private long id = 0;
    private int page = 1;

    public static MessageListFragment getInstance(String title) {
        MessageListFragment sf = new MessageListFragment();
        sf.mTitle = title;
        return sf;
    }


    private void initRefreshLayout() {


        messageRefresh.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshlayout) {

                if (!NetUtil.isNetworkConnected(getActivity())) {
                    ToastUtil.shortBottonToast(getContext(), "请检查网络设置");
                    messageRefresh.finishRefresh(2000, false);
                    mAdapter.setEnableLoadMore(false);
                } else {
                    requestData(true);
                }
            }
        });

//        resultRefresh.autoRefresh();

    }

    private void initRecyclerView() {
        messageRecycler.setLayoutManager(new LinearLayoutManager(getActivity()));

        messageRecycler.addOnItemTouchListener(new OnItemClickListener() {
            @Override
            public void onSimpleItemClick(BaseQuickAdapter adapter, View view, int position) {
                final MessageListBean data = (MessageListBean) adapter.getData().get(position);
                final int entityId = data.getEntityId();
                final String entity = data.getEntity();

                Intent intent = null;

                if ("sggjy".equals(entity)) {
                    intent = new Intent(BiaoXunTong.getApplicationContext(), IndexSggjyDetailActivity.class);
                    intent.putExtra("entityId", entityId);
                    intent.putExtra("entity", entity);
                    intent.putExtra("ajaxlogtype", "0");
                    intent.putExtra("mId", "");
                    startActivity(intent);
                } else if ("xcggg".equals(entity)) {
                    intent = new Intent(BiaoXunTong.getApplicationContext(), IndexXcgggDetailActivity.class);
                    intent.putExtra("entityId", entityId);
                    intent.putExtra("entity", entity);
                    intent.putExtra("ajaxlogtype", "0");
                    intent.putExtra("mId", "");
                    startActivity(intent);
                } else if ("bxtgdj".equals(entity)) {
                    intent = new Intent(BiaoXunTong.getApplicationContext(), IndexBxtgdjDetailActivity.class);
                    intent.putExtra("entityId", entityId);
                    intent.putExtra("entity", entity);
                    intent.putExtra("ajaxlogtype", "0");
                    intent.putExtra("mId", "");
                    startActivity(intent);
                } else if ("sggjycgtable".equals(entity)) {

                    intent = new Intent(BiaoXunTong.getApplicationContext(), IndexSggjycgtableDetailActivity.class);
                    intent.putExtra("entityId", entityId);
                    intent.putExtra("entity", entity);
                    intent.putExtra("ajaxlogtype", "0");
                    intent.putExtra("mId", "");
                    startActivity(intent);

                } else if ("xjggg".equals(entity) || "sjggg".equals(entity)) {
                    intent = new Intent(BiaoXunTong.getApplicationContext(), ResultXjgggDetailActivity.class);
                    intent.putExtra("entityId", entityId);
                    intent.putExtra("entity", entity);
                    intent.putExtra("ajaxlogtype", "0");
                    intent.putExtra("mId", "");
                    startActivity(intent);

                } else if ("sggjyzbjg".equals(entity) || "sggjycgjgrow".equals(entity) || "sggjyjgcgtable".equals(entity)) {
                    intent = new Intent(BiaoXunTong.getApplicationContext(), ResultSggjyzbjgDetailActivity.class);
                    intent.putExtra("entityId", entityId);
                    intent.putExtra("entity", entity);
                    intent.putExtra("ajaxlogtype", "0");
                    intent.putExtra("mId", "");
                    startActivity(intent);
                } else if ("t_hyzx".equals(entity)) {

                } else if ("sggjycgrow".equals(entity)) {
                    intent = new Intent(BiaoXunTong.getApplicationContext(), IndexSggjycgrowDetailActivity.class);
                    intent.putExtra("entityId", entityId);
                    intent.putExtra("entity", entity);
                    intent.putExtra("ajaxlogtype", "0");
                    intent.putExtra("mId", "");
                    startActivity(intent);
                } else if ("cqcggg".equals(entity)) {
                    final String title = data.getEntityName();
                    ARouter.getInstance().build("/com/BrowserDetailActivity").withString("mApi",BiaoXunTongApi.URL_GETCOLLECTIONLISTDETAIL)
                            .withString("mTitle",title).withString("mEntity",entity).withInt("mEntityid",entityId).navigation();
                } else if ("cqsggjy".equals(entity)) {
                    intent = new Intent(BiaoXunTong.getApplicationContext(), IndexCqsggjyDetailActivity.class);
                    intent.putExtra("entityId", entityId);
                    intent.putExtra("entity", entity);
                    intent.putExtra("ajaxlogtype", "0");
                    intent.putExtra("mId", "");
                    startActivity(intent);
                } else if ("cqsggjyzbjg".equals(entity)) {
                    intent = new Intent(BiaoXunTong.getApplicationContext(), ResultCqsggjyzbjgDetailActivity.class);
                    intent.putExtra("entityId", entityId);
                    intent.putExtra("entity", entity);
                    intent.putExtra("ajaxlogtype", "0");
                    intent.putExtra("mId", "");
                    startActivity(intent);
                }

                if ("0".equals(data.getIsRead())) {
                    BiaoXunTong.getHandler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            //改变已读未读状态
                            EventBus.getDefault().post(new EventMessage(EventMessage.READ_STATUS));
                        }
                    }, 1000);
                }

            }
        });

    }


    private void initAdapter() {
        mAdapter = new MessageListAdapter(R.layout.fragment_message_item, mDataList);

        mAdapter.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
            @Override
            public void onLoadMoreRequested() {
                //TODO 去加载更多数据
                if (!NetUtil.isNetworkConnected(getActivity())) {
                    ToastUtil.shortBottonToast(getContext(), "请检查网络设置");
                } else {
                    requestData(false);
                }
            }
        });

        //设置列表动画
//        mAdapter.openLoadAnimation(BaseQuickAdapter.SLIDEIN_LEFT);
        mAdapter.setLoadMoreView(new CustomLoadMoreView());
        messageRecycler.setAdapter(mAdapter);


    }


    @Override
    public Object setLayout() {
        return R.layout.fragment_list_message;
    }

    @Override
    public void initView() {

        //注册EventBus
        EventBus.getDefault().register(this);

        messageRecycler = getView().findViewById(R.id.message_recycler);
        messageRefresh = getView().findViewById(R.id.message_refresh);
        loadingStatus = getView().findViewById(R.id.message_list_status_view);

        btnToLogin = getView().findViewById(R.id.btn_to_login);
        llShow = getView().findViewById(R.id.ll_show);

        btnToLogin.setOnClickListener(this);


    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

        //取消注册EventBus
        EventBus.getDefault().unregister(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void XXXXXX(EventMessage message) {

        if (EventMessage.LOGIN_SUCCSS.equals(message.getMessage()) || EventMessage.CLICK_FAV.equals(message.getMessage())
                || EventMessage.LOCA_AREA_CHANGE.equals(message.getMessage())) {

            //登陆成功后更新UI
            if (AppSharePreferenceMgr.contains(getContext(), EventMessage.LOGIN_SUCCSS)) {
                if (llShow != null) {
                    llShow.setVisibility(View.GONE);
                }

                initAdapter();
                initRefreshLayout();
                mAdapter.setEnableLoadMore(false);
                requestData(true);
            } else {
                if (llShow != null) {
                    llShow.setVisibility(View.VISIBLE);
                }
            }

        } else if (EventMessage.LOGIN_OUT.equals(message.getMessage())) {
            if (llShow != null) {
                llShow.setVisibility(View.VISIBLE);
            }
        } else if (EventMessage.READ_STATUS.equals(message.getMessage())) {
            requestData(true);
        }

    }


    @Override
    public void initData() {
        if ("告知消息".equals(mTitle)) {
            mType = 2;
        } else if ("历史推送".equals(mTitle)) {
            mType = 1;
        }

    }

    @Override
    public void initEvent() {
        initRecyclerView();
        initAdapter();
        initRefreshLayout();

        if (!NetUtil.isNetworkConnected(getActivity())) {
            ToastUtil.shortBottonToast(getContext(), "请检查网络设置");
            mAdapter.setEnableLoadMore(false);
            if (!isInitCache) {
                loadingStatus.showLoading();
            }
            BiaoXunTong.getHandler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    requestData(true);
                }
            }, 500);
        } else {
            loadingStatus.showLoading();
            BiaoXunTong.getHandler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    requestData(true);
                }
            }, 500);
        }
    }


    public void requestData(final boolean isRefresh) {

        if (mType == 1) {
            if (AppSharePreferenceMgr.contains(getContext(), EventMessage.LOGIN_SUCCSS)) {
                llShow.setVisibility(View.GONE);
                //已登录的数据请求
                List<UserProfile> users = DatabaseManager.getInstance().getDao().loadAll();

                for (int i = 0; i < users.size(); i++) {
                    id = users.get(0).getId();
                }

                if (isRefresh) {
                    page = 1;
                    OkGo.<String>post(BiaoXunTongApi.URL_GETUILIST)
                            .params("type", mType)
                            .params("userId", id)
                            .params("page", page)
                            .params("size", 10)
                            .params("deviceId", deviceId)
                            .cacheKey("message_login_cache" + mTitle + id)
                            .cacheMode(CacheMode.REQUEST_FAILED_READ_CACHE)
                            .cacheTime(3600 * 72000)
                            .execute(new StringCallback() {
                                @Override
                                public void onSuccess(Response<String> response) {

                                    final JSONObject object = JSON.parseObject(response.body());
                                    final JSONObject data = object.getJSONObject("data");
                                    final String status = object.getString("status");
                                    final String message = object.getString("message");


                                    if ("200".equals(status)) {
                                        final JSONArray array = data.getJSONArray("list");
                                        final boolean nextPage = data.getBoolean("nextpage");
                                        if (array.size() > 0) {
                                            page = 2;
                                            setData(isRefresh, array, nextPage);
                                        } else {
                                            if (mDataList != null) {
                                                mDataList.clear();
                                                mAdapter.notifyDataSetChanged();
                                            }
                                            //TODO 内容为空的处理
                                            loadingStatus.showEmpty();
                                            messageRefresh.setEnableRefresh(false);
                                        }
                                    } else {
                                        ToastUtil.shortToast(getContext(), message);
                                    }

                                }

                                @Override
                                public void onCacheSuccess(Response<String> response) {
                                    if (!isInitCache) {
                                        final JSONObject object = JSON.parseObject(response.body());
                                        final JSONObject data = object.getJSONObject("data");
                                        final String status = object.getString("status");
                                        final String message = object.getString("message");

                                        if ("200".equals(status)) {
                                            final JSONArray array = data.getJSONArray("list");
                                            final boolean nextPage = data.getBoolean("nextpage");
                                            if (array.size() > 0) {
                                                setData(isRefresh, array, nextPage);
                                            } else {
                                                if (mDataList != null) {
                                                    mDataList.clear();
                                                    mAdapter.notifyDataSetChanged();
                                                }
                                                //TODO 内容为空的处理
                                                loadingStatus.showEmpty();
                                                messageRefresh.setEnableRefresh(false);
                                            }
                                        } else {
                                            ToastUtil.shortToast(getContext(), message);
                                        }


                                        isInitCache = true;
                                    }
                                }
                            });

                } else {
                    OkGo.<String>post(BiaoXunTongApi.URL_GETUILIST)
                            .params("type", mType)
                            .params("userId", id)
                            .params("page", page)
                            .params("size", 10)
                            .params("deviceId", deviceId)
                            .execute(new StringCallback() {
                                @Override
                                public void onSuccess(Response<String> response) {

                                    final JSONObject object = JSON.parseObject(response.body());
                                    final JSONObject data = object.getJSONObject("data");
                                    final String status = object.getString("status");
                                    final String message = object.getString("message");

                                    if ("200".equals(status)) {
                                        final JSONArray array = data.getJSONArray("list");
                                        final boolean nextPage = data.getBoolean("nextpage");
                                        if (array.size() > 0) {
                                            setData(isRefresh, array, nextPage);
                                        } else {
                                            if (mDataList != null) {
                                                mDataList.clear();
                                                mAdapter.notifyDataSetChanged();
                                            }
                                            //TODO 内容为空的处理
                                            loadingStatus.showEmpty();
                                            messageRefresh.setEnableRefresh(false);
                                        }
                                    } else {
                                        ToastUtil.shortToast(getContext(), message);
                                    }

                                }
                            });
                }


            } else {
                //未登录的数据请求
                if (isRefresh) {
                    page = 1;
                    OkGo.<String>post(BiaoXunTongApi.URL_GETUILIST)
                            .params("type", mType)
                            .params("page", page)
                            .params("size", 10)
                            .params("deviceId", deviceId)
                            .cacheKey("message_login_cache" + mTitle + id)
                            .cacheMode(CacheMode.REQUEST_FAILED_READ_CACHE)
                            .cacheTime(3600 * 72000)
                            .execute(new StringCallback() {
                                @Override
                                public void onSuccess(Response<String> response) {

                                    final JSONObject object = JSON.parseObject(response.body());
                                    final JSONObject data = object.getJSONObject("data");
                                    final JSONArray array = data.getJSONArray("list");
                                    final boolean nextPage = data.getBoolean("nextpage");

                                    if (array.size() > 0) {
                                        page = 2;
                                        setData(isRefresh, array, nextPage);
                                    } else {
                                        if (mDataList != null) {
                                            mDataList.clear();
                                            mAdapter.notifyDataSetChanged();
                                        }
                                        //TODO 内容为空的处理
                                        loadingStatus.showEmpty();
                                        messageRefresh.setEnableRefresh(false);
                                    }

                                }

                                @Override
                                public void onCacheSuccess(Response<String> response) {
                                    if (!isInitCache) {

                                        final JSONObject object = JSON.parseObject(response.body());
                                        final JSONObject data = object.getJSONObject("data");
                                        final JSONArray array = data.getJSONArray("list");
                                        final boolean nextPage = data.getBoolean("nextpage");

                                        if (array.size() > 0) {
                                            setData(isRefresh, array, nextPage);
                                        } else {
                                            if (mDataList != null) {
                                                mDataList.clear();
                                                mAdapter.notifyDataSetChanged();
                                            }
                                            //TODO 内容为空的处理
                                            loadingStatus.showEmpty();
                                            messageRefresh.setEnableRefresh(false);
                                        }

                                        isInitCache = true;
                                    }
                                }
                            });

                } else {
                    OkGo.<String>post(BiaoXunTongApi.URL_GETUILIST)
                            .params("type", mType)
                            .params("page", page)
                            .params("size", 10)
                            .params("deviceId", deviceId)
                            .execute(new StringCallback() {
                                @Override
                                public void onSuccess(Response<String> response) {

                                    final JSONObject object = JSON.parseObject(response.body());
                                    final JSONObject data = object.getJSONObject("data");
                                    final JSONArray array = data.getJSONArray("list");
                                    final boolean nextPage = data.getBoolean("nextpage");

                                    if (array.size() > 0) {
                                        setData(isRefresh, array, nextPage);
                                    } else {
                                        if (mDataList != null) {
                                            mDataList.clear();
                                            mAdapter.notifyDataSetChanged();
                                        }
                                        //TODO 内容为空的处理
                                        loadingStatus.showEmpty();
                                        messageRefresh.setEnableRefresh(false);
                                    }

                                }
                            });
                }
            }
        } else if (mType == 2) {
            if (AppSharePreferenceMgr.contains(getContext(), EventMessage.LOGIN_SUCCSS)) {
                llShow.setVisibility(View.GONE);
                //已登录的数据请求
                List<UserProfile> users = DatabaseManager.getInstance().getDao().loadAll();

                for (int i = 0; i < users.size(); i++) {
                    id = users.get(0).getId();
                }

                if (isRefresh) {
                    page = 1;
                    OkGo.<String>post(BiaoXunTongApi.URL_GETUILIST)
                            .params("type", mType)
                            .params("userId", id)
                            .params("page", page)
                            .params("size", 10)
                            .params("deviceId", deviceId)
                            .cacheKey("message_login_cache" + mTitle + id)
                            .cacheMode(CacheMode.REQUEST_FAILED_READ_CACHE)
                            .cacheTime(3600 * 72000)
                            .execute(new StringCallback() {
                                @Override
                                public void onSuccess(Response<String> response) {

                                    final JSONObject object = JSON.parseObject(response.body());
                                    final JSONObject data = object.getJSONObject("data");

                                    final String status = object.getString("status");
                                    final String message = object.getString("message");


                                    if ("200".equals(status)) {
                                        final JSONArray array = data.getJSONArray("list");
                                        final boolean nextPage = data.getBoolean("nextpage");
                                        if (array.size() > 0) {
                                            page = 2;
                                            setData(isRefresh, array, nextPage);
                                        } else {
                                            if (mDataList != null) {
                                                mDataList.clear();
                                                mAdapter.notifyDataSetChanged();
                                            }
                                            //TODO 内容为空的处理
                                            loadingStatus.showEmpty();
                                            messageRefresh.setEnableRefresh(false);
                                        }
                                    } else {
                                        ToastUtil.shortToast(getContext(), message);
                                    }

                                }

                                @Override
                                public void onCacheSuccess(Response<String> response) {
                                    if (!isInitCache) {

                                        final JSONObject object = JSON.parseObject(response.body());
                                        final JSONObject data = object.getJSONObject("data");
                                        final String status = object.getString("status");
                                        final String message = object.getString("message");

                                        if ("200".equals(status)) {
                                            final JSONArray array = data.getJSONArray("list");
                                            final boolean nextPage = data.getBoolean("nextpage");
                                            if (array.size() > 0) {
                                                setData(isRefresh, array, nextPage);
                                            } else {
                                                if (mDataList != null) {
                                                    mDataList.clear();
                                                    mAdapter.notifyDataSetChanged();
                                                }
                                                //TODO 内容为空的处理
                                                loadingStatus.showEmpty();
                                                messageRefresh.setEnableRefresh(false);
                                            }
                                        } else {
                                            ToastUtil.shortToast(getContext(), message);
                                        }


                                        isInitCache = true;
                                    }
                                }
                            });

                } else {
                    OkGo.<String>post(BiaoXunTongApi.URL_GETUILIST)
                            .params("type", mType)
                            .params("userId", id)
                            .params("page", page)
                            .params("size", 10)
                            .params("deviceId", deviceId)
                            .execute(new StringCallback() {
                                @Override
                                public void onSuccess(Response<String> response) {

                                    final JSONObject object = JSON.parseObject(response.body());
                                    final JSONObject data = object.getJSONObject("data");
                                    final String status = object.getString("status");
                                    final String message = object.getString("message");

                                    if ("200".equals(status)) {
                                        final JSONArray array = data.getJSONArray("list");
                                        final boolean nextPage = data.getBoolean("nextpage");
                                        if (array.size() > 0) {
                                            setData(isRefresh, array, nextPage);
                                        } else {
                                            if (mDataList != null) {
                                                mDataList.clear();
                                                mAdapter.notifyDataSetChanged();
                                            }
                                            //TODO 内容为空的处理
                                            loadingStatus.showEmpty();
                                            messageRefresh.setEnableRefresh(false);
                                        }
                                    } else {
                                        ToastUtil.shortToast(getContext(), message);
                                    }

                                }
                            });
                }


            } else {
                //未登录的数据请求
                llShow.setVisibility(View.VISIBLE);
            }
        }


    }


    private void setData(boolean isRefresh, JSONArray data, boolean nextPage) {
        final int size = data == null ? 0 : data.size();
        if (isRefresh) {
            loadingStatus.showContent();
            mDataList.clear();
            for (int i = 0; i < data.size(); i++) {
                MessageListBean bean = new MessageListBean();
                JSONObject list = data.getJSONObject(i);
                bean.setEntityName(list.getString("entityName"));
                bean.setCreateTime(list.getString("createTime"));
                bean.setEntityType(list.getString("entityType"));
                bean.setEntityId(list.getInteger("entityId"));
                bean.setEntity(list.getString("entity"));
                bean.setIsRead(list.getString("isRead"));
                mDataList.add(bean);
            }

            messageRefresh.finishRefresh(0, true);
            mAdapter.setEnableLoadMore(true);

        } else {
            page++;
            loadingStatus.showContent();
            if (size > 0) {
                for (int i = 0; i < data.size(); i++) {
                    MessageListBean bean = new MessageListBean();
                    JSONObject list = data.getJSONObject(i);
                    bean.setEntityName(list.getString("entityName"));
                    bean.setCreateTime(list.getString("createTime"));
                    bean.setEntityType(list.getString("entityType"));
                    bean.setEntityId(list.getInteger("entityId"));
                    bean.setEntity(list.getString("entity"));
                    bean.setIsRead(list.getString("isRead"));
                    mDataList.add(bean);
                }
            }

            messageRefresh.finishLoadmore(0, true);

        }
        if (!nextPage) {
            //第一页如果不够一页就不显示没有更多数据布局
            mAdapter.loadMoreEnd();
        } else {
            mAdapter.loadMoreComplete();
        }

        mAdapter.notifyDataSetChanged();

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_to_login:
                //未登录去登陆
                startActivity(new Intent(getActivity(), SignInActivity.class));
                break;
            default:
                break;
        }
    }

}
