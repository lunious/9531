package com.lubanjianye.biaoxuntong.ui.main.collection;


import android.content.Intent;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.LinearLayoutManager;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.alibaba.android.arouter.launcher.ARouter;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.classic.common.MultipleStatusView;
import com.lubanjianye.biaoxuntong.R;
import com.lubanjianye.biaoxuntong.app.BiaoXunTong;
import com.lubanjianye.biaoxuntong.base.BaseFragment1;
import com.lubanjianye.biaoxuntong.bean.CollectionListBean;
import com.lubanjianye.biaoxuntong.database.DatabaseManager;
import com.lubanjianye.biaoxuntong.database.UserProfile;
import com.lubanjianye.biaoxuntong.eventbus.EventMessage;
import com.lubanjianye.biaoxuntong.ui.detail.IndexArticleDetailActivity;
import com.lubanjianye.biaoxuntong.ui.detail.ResultArticleDetailActivity;
import com.lubanjianye.biaoxuntong.ui.main.index.detail.sichuan.IndexBxtgdjDetailActivity;
import com.lubanjianye.biaoxuntong.ui.main.index.detail.sichuan.IndexSggjycgrowDetailActivity;
import com.lubanjianye.biaoxuntong.ui.main.index.detail.sichuan.IndexSggjycgtableDetailActivity;
import com.lubanjianye.biaoxuntong.ui.main.index.detail.sichuan.IndexXcgggDetailActivity;
import com.lubanjianye.biaoxuntong.ui.main.result.detail.chongqing.ResultCqsggjyzbjgDetailActivity;
import com.lubanjianye.biaoxuntong.ui.main.result.detail.sichuan.ResultXjgggDetailActivity;
import com.lubanjianye.biaoxuntong.ui.sign.SignInActivity;
import com.lubanjianye.biaoxuntong.ui.view.loadmore.CustomLoadMoreView;
import com.lubanjianye.biaoxuntong.ui.main.index.detail.chongqing.IndexCqsggjyDetailActivity;
import com.lubanjianye.biaoxuntong.app.BiaoXunTongApi;
import com.lubanjianye.biaoxuntong.ui.main.index.detail.sichuan.IndexSggjyDetailActivity;
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
import com.yanzhenjie.recyclerview.swipe.SwipeItemClickListener;
import com.yanzhenjie.recyclerview.swipe.SwipeMenu;
import com.yanzhenjie.recyclerview.swipe.SwipeMenuBridge;
import com.yanzhenjie.recyclerview.swipe.SwipeMenuCreator;
import com.yanzhenjie.recyclerview.swipe.SwipeMenuItem;
import com.yanzhenjie.recyclerview.swipe.SwipeMenuItemClickListener;
import com.yanzhenjie.recyclerview.swipe.SwipeMenuRecyclerView;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import java.util.ArrayList;
import java.util.List;


public class CollectionTabFragment extends BaseFragment1 implements View.OnClickListener {

    private AppCompatTextView mainBarName = null;
    private AppCompatButton btnToLogin = null;
    private LinearLayout llShow = null;
    private SwipeMenuRecyclerView collectRecycler = null;
    private SmartRefreshLayout collectRefresh = null;
    private MultipleStatusView loadingStatus = null;


    private CollectionListAdapter mAdapter;
    private ArrayList<CollectionListBean> mDataList = new ArrayList<>();

    private int page = 1;
    private boolean isInitCache = false;
    private long id = 0;
    private String deviceId = AppSysMgr.getPsuedoUniqueID();


    @Override
    public Object setLayout() {
        return R.layout.fragment_collection_list;
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
        }
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();

        //取消注册EventBus
        EventBus.getDefault().unregister(this);
    }

    @Override
    public void initView() {
        //注册EventBus
        EventBus.getDefault().register(this);

        mainBarName = getView().findViewById(R.id.main_bar_name);
        btnToLogin = getView().findViewById(R.id.btn_to_login);
        llShow = getView().findViewById(R.id.ll_show);
        collectRecycler = getView().findViewById(R.id.collect_recycler);
        collectRefresh = getView().findViewById(R.id.collect_refresh);
        loadingStatus = getView().findViewById(R.id.collection_list_status_view);

        btnToLogin.setOnClickListener(this);


    }

    @Override
    public void initData() {
        mainBarName.setVisibility(View.VISIBLE);
        mainBarName.setText("我的收藏");


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

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_to_login:
                //未登录去登陆
                startActivity(new Intent(getActivity(), SignInActivity.class));
                break;
            default:
                break;
        }
    }

    private void initRefreshLayout() {


        collectRefresh.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshlayout) {

                if (!NetUtil.isNetworkConnected(getActivity())) {
                    ToastUtil.shortBottonToast(getContext(), "请检查网络设置");
                    collectRefresh.finishRefresh(2000, false);
                    mAdapter.setEnableLoadMore(false);
                } else {
                    requestData(true);
                }
            }
        });

//        collectRefresh.autoRefresh();
    }

    private void initRecyclerView() {

        collectRecycler.setLayoutManager(new LinearLayoutManager(getActivity()));

        collectRecycler.setSwipeMenuCreator(new SwipeMenuCreator() {
            @Override
            public void onCreateMenu(SwipeMenu swipeLeftMenu, SwipeMenu swipeRightMenu, int viewType) {
                int width = getResources().getDimensionPixelSize(R.dimen.d64);

                // 1. MATCH_PARENT 自适应高度，保持和Item一样高;
                // 2. 指定具体的高，比如80;
                // 3. WRAP_CONTENT，自身高度，不推荐;
                int height = ViewGroup.LayoutParams.MATCH_PARENT;

                SwipeMenuItem deleteItem = new SwipeMenuItem(getActivity())
                        .setBackground(R.drawable.selector_red)
                        .setImage(R.mipmap.ic_action_delete)
                        .setWidth(width)
                        .setHeight(height);
                swipeRightMenu.addMenuItem(deleteItem);

            }
        });

        collectRecycler.setSwipeMenuItemClickListener(new SwipeMenuItemClickListener() {
            @Override
            public void onItemClick(final SwipeMenuBridge menuBridge) {

                // RecyclerView的Item的position。
                int adapterPosition = menuBridge.getAdapterPosition();
                final CollectionListBean data = mAdapter.getData().get(adapterPosition);

                final int mEntityId = data.getEntityId();
                final String mEntity = data.getEntity();

                List<UserProfile> users = DatabaseManager.getInstance().getDao().loadAll();
                for (int i = 0; i < users.size(); i++) {
                    id = users.get(0).getId();
                }
                OkGo.<String>post(BiaoXunTongApi.URL_DELEFAV)
                        .params("entityid", mEntityId)
                        .params("entity", mEntity)
                        .params("userid", id)
                        .params("deviceId", deviceId)
                        .execute(new StringCallback() {
                            @Override
                            public void onSuccess(Response<String> response) {
                                final JSONObject object = JSON.parseObject(response.body());
                                String status = object.getString("status");
                                if ("200".equals(status)) {
                                    EventBus.getDefault().post(new EventMessage(EventMessage.CLICK_FAV));
                                    ToastUtil.shortToast(getContext(), "删除成功");
                                    menuBridge.closeMenu();
                                } else if ("500".equals(status)) {
                                    ToastUtil.shortToast(getContext(), "服务器异常");
                                }
                            }
                        });

            }
        });

        collectRecycler.setSwipeItemClickListener(new SwipeItemClickListener() {
            @Override
            public void onItemClick(View itemView, int position) {
                final CollectionListBean data = mAdapter.getData().get(position);

                final int entityId = data.getEntityId();
                final String entity = data.getEntity();
                final String entityUrl = data.getEntityUrl();
                final String type = data.getType();
                Intent intent = null;

                if (!TextUtils.isEmpty(entityUrl)) {
                    if ("采购中标公示".equals(type) || "工程中标结果".equals(type) || "交易结果公示".equals(type) ||
                            "中标公告".equals(type) || "成交公示".equals(type) || "交易结果".equals(type)) {
                        intent = new Intent(BiaoXunTong.getApplicationContext(), ResultArticleDetailActivity.class);
                        intent.putExtra("entityId", entityId);
                        intent.putExtra("entity", entity);
                        intent.putExtra("ajaxlogtype", "0");
                        intent.putExtra("mId", "");
                        startActivity(intent);

                    } else {
                        intent = new Intent(BiaoXunTong.getApplicationContext(), IndexArticleDetailActivity.class);
                        intent.putExtra("entityId", entityId);
                        intent.putExtra("entity", entity);
                        intent.putExtra("ajaxlogtype", "0");
                        intent.putExtra("mId", "");
                        startActivity(intent);
                    }

                } else {
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
                        final String title = data.getEntryName();
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
                }

            }

        });

    }

    private void initAdapter() {
        mAdapter = new CollectionListAdapter(R.layout.fragment_collection_item, mDataList);
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
        collectRecycler.setAdapter(mAdapter);


    }


    public void requestData(final boolean isRefresh) {


        if (AppSharePreferenceMgr.contains(getContext(), EventMessage.LOGIN_SUCCSS)) {
            llShow.setVisibility(View.GONE);
            List<UserProfile> users = DatabaseManager.getInstance().getDao().loadAll();
            for (int i = 0; i < users.size(); i++) {
                id = users.get(0).getId();
            }

            if (isRefresh) {
                page = 1;
                OkGo.<String>post(BiaoXunTongApi.URL_GETCOLLECTIONLIST)
                        .params("userid", id)
                        .params("page", page)
                        .params("size", 10)
                        .cacheKey("collect_cache" + id)
                        .cacheMode(CacheMode.FIRST_CACHE_THEN_REQUEST)
                        .cacheTime(3600 * 48000)
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
                                    //TODO 内容为空的处理
                                    loadingStatus.showEmpty();
                                    collectRefresh.setEnableRefresh(false);
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
                                        //TODO 内容为空的处理
                                        loadingStatus.showEmpty();
                                        collectRefresh.setEnableRefresh(false);
                                    }
                                    isInitCache = true;

                                }

                            }
                        });
            } else {
                OkGo.<String>post(BiaoXunTongApi.URL_GETCOLLECTIONLIST)
                        .params("userid", id)
                        .params("page", page)
                        .params("size", 10)
                        .cacheMode(CacheMode.NO_CACHE)
                        .execute(new StringCallback() {
                            @Override
                            public void onSuccess(Response<String> response) {
                                final JSONObject object = JSON.parseObject(response.body());
                                final JSONObject data = object.getJSONObject("data");
                                final JSONArray array = data.getJSONArray("list");
                                final int count = data.getInteger("count");
                                final boolean nextPage = data.getBoolean("nextpage");

                                if (array.size() > 0) {
                                    setData(isRefresh, array, nextPage);
                                } else {
                                    //TODO 内容为空的处理
                                    loadingStatus.showEmpty();
                                    collectRefresh.setEnableRefresh(false);
                                }
                            }
                        });
            }

        } else {
            llShow.setVisibility(View.VISIBLE);
        }


    }

    private void setData(boolean isRefresh, JSONArray data, boolean nextPage) {
        final int size = data == null ? 0 : data.size();
        if (isRefresh) {
            loadingStatus.showContent();
            mDataList.clear();
            for (int i = 0; i < data.size(); i++) {
                CollectionListBean bean = new CollectionListBean();
                JSONObject list = data.getJSONObject(i);
                bean.setEntryName(list.getString("entryName"));
                bean.setAddress(list.getString("address"));
                bean.setType(list.getString("type"));
                bean.setSysTime(list.getString("sysTime"));
                bean.setEntityId(list.getInteger("entityId"));
                bean.setEntity(list.getString("entity"));
                bean.setIsResult(list.getString("isResult"));
                bean.setIsCorrections(list.getString("isCorrections"));
                bean.setEntityUrl(list.getString("entityUrl"));
                mDataList.add(bean);
            }

            collectRefresh.finishRefresh(0, true);
            mAdapter.setEnableLoadMore(true);
        } else {
            page++;
            loadingStatus.showContent();
            if (size > 0) {
                for (int i = 0; i < data.size(); i++) {
                    CollectionListBean bean = new CollectionListBean();
                    JSONObject list = data.getJSONObject(i);
                    bean.setEntryName(list.getString("entryName"));
                    bean.setAddress(list.getString("address"));
                    bean.setType(list.getString("type"));
                    bean.setSysTime(list.getString("sysTime"));
                    bean.setEntityId(list.getInteger("entityId"));
                    bean.setEntity(list.getString("entity"));
                    bean.setIsResult(list.getString("isResult"));
                    bean.setIsCorrections(list.getString("isCorrections"));
                    bean.setEntityUrl(list.getString("entityUrl"));
                    mDataList.add(bean);
                }

            }
            collectRefresh.finishLoadmore(0, true);

        }

        if (!nextPage) {
            //第一页如果不够一页就不显示没有更多数据布局
            mAdapter.loadMoreEnd();
        } else {
            mAdapter.loadMoreComplete();
        }

        mAdapter.notifyDataSetChanged();

    }
}
