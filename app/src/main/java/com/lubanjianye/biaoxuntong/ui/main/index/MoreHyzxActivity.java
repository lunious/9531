package com.lubanjianye.biaoxuntong.ui.main.index;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.classic.common.MultipleStatusView;
import com.lubanjianye.biaoxuntong.R;
import com.lubanjianye.biaoxuntong.app.BiaoXunTong;
import com.lubanjianye.biaoxuntong.app.BiaoXunTongApi;
import com.lubanjianye.biaoxuntong.base.BaseActivity;
import com.lubanjianye.biaoxuntong.bean.IndexHyzxListBean;
import com.lubanjianye.biaoxuntong.database.DatabaseManager;
import com.lubanjianye.biaoxuntong.database.UserProfile;
import com.lubanjianye.biaoxuntong.eventbus.EventMessage;
import com.lubanjianye.biaoxuntong.ui.main.index.detail.sichuan.IndexHyzxDetailActivity;
import com.lubanjianye.biaoxuntong.ui.view.loadmore.CustomLoadMoreView;
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

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Author: lunious
 * Date: 2018/8/9 22:20
 * Description:
 */
@Route(path = "/com/MoreHyzxActivity")
public class MoreHyzxActivity extends BaseActivity {
    @BindView(R.id.ll_iv_back)
    LinearLayout llIvBack;
    @BindView(R.id.main_bar_name)
    AppCompatTextView mainBarName;
    @BindView(R.id.index_hyzx_recycler)
    RecyclerView indexHyzxRecycler;
    @BindView(R.id.index_hyzx_list_status_view)
    MultipleStatusView loadingStatus;
    @BindView(R.id.index_hyzx_refresh)
    SmartRefreshLayout indexHyzxRefresh;

    private IndexHyzxListAdapter mAdapter;
    private ArrayList<IndexHyzxListBean> mDataList = new ArrayList<>();

    private int page = 1;
    private boolean isInitCache = false;
    private boolean showStatus = true;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_more_hyzx;
    }

    @Override
    protected void initData(Bundle savedInstanceState) {
        initRecyclerView();
        initAdapter();
        initRefreshLayout();
        llIvBack.setVisibility(View.VISIBLE);
        mainBarName.setText("行业资讯");
    }

    @Override
    protected void initEvent(Bundle savedInstanceState) {
        if (!NetUtil.isNetworkConnected(MoreHyzxActivity.this)) {
            ToastUtil.shortBottonToast(MoreHyzxActivity.this, "请检查网络设置");
            mAdapter.setEnableLoadMore(false);
            if (!isInitCache) {
                loadingStatus.showLoading();
            }
            BiaoXunTong.getHandler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    requestData(true, 0);
                }
            }, 500);
        } else {
            loadingStatus.showLoading();
            BiaoXunTong.getHandler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    requestData(true, 1);
                }
            }, 500);
        }
    }

    private void initRefreshLayout() {

        indexHyzxRefresh.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshlayout) {

                if (!NetUtil.isNetworkConnected(MoreHyzxActivity.this)) {
                    ToastUtil.shortBottonToast(MoreHyzxActivity.this, "请检查网络设置");
                    indexHyzxRefresh.finishRefresh(2000, false);
                    mAdapter.setEnableLoadMore(false);
                } else {
                    requestData(true, 2);
                }
            }
        });


//        indexHyzxRefresh.autoRefresh();


    }


    private void initRecyclerView() {

        indexHyzxRecycler.setLayoutManager(new LinearLayoutManager(MoreHyzxActivity.this));

        indexHyzxRecycler.addOnItemTouchListener(new OnItemClickListener() {
            @Override
            public void onSimpleItemClick(BaseQuickAdapter adapter, View view, int position) {
                final IndexHyzxListBean data = (IndexHyzxListBean) adapter.getData().get(position);
                final String createTime = data.getCreate_time();
                final String title = data.getTitle();
                final String mobile_context = data.getMobile_context();

                Intent intent = new Intent(BiaoXunTong.getApplicationContext(), IndexHyzxDetailActivity.class);
                intent.putExtra("title", title);
                intent.putExtra("createTime", createTime);
                intent.putExtra("mobile_context", mobile_context);
                startActivity(intent);
            }
        });

    }

    private void initAdapter() {
        mAdapter = new IndexHyzxListAdapter(R.layout.fragment_index_hyzx_item, mDataList);
        mAdapter.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
            @Override
            public void onLoadMoreRequested() {
                //TODO 去加载更多数据
                if (!NetUtil.isNetworkConnected(MoreHyzxActivity.this)) {
                    ToastUtil.shortBottonToast(MoreHyzxActivity.this, "请检查网络设置");
                } else {
                    requestData(false, 0);
                }
            }
        });
        //设置列表动画
//        mAdapter.openLoadAnimation(BaseQuickAdapter.SLIDEIN_LEFT);
        mAdapter.setLoadMoreView(new CustomLoadMoreView());
        indexHyzxRecycler.setAdapter(mAdapter);


    }

    private long id = 0;

    public void requestData(final boolean isRefresh, final int n) {

        int size = 10 + (int) (Math.random() * 10);

        if (AppSharePreferenceMgr.contains(MoreHyzxActivity.this, EventMessage.LOGIN_SUCCSS)) {
            //已登录的数据请求
            List<UserProfile> users = DatabaseManager.getInstance().getDao().loadAll();

            for (int i = 0; i < users.size(); i++) {
                id = users.get(0).getId();
            }

            if (isRefresh) {
                page = 1;
                OkGo.<String>post(BiaoXunTongApi.URL_GETINDEXHYZXLIST)
                        .params("userid", id)
                        .params("page", page)
                        .params("size", size)
                        .cacheKey("index_hyzx_login_cache" + id)
                        .cacheMode(CacheMode.FIRST_CACHE_THEN_REQUEST)
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
                                    setData(isRefresh, array, nextPage, n);
                                } else {
                                    if (mDataList != null) {
                                        mDataList.clear();
                                        mAdapter.notifyDataSetChanged();
                                    }
                                    //TODO 内容为空的处理
                                    loadingStatus.showEmpty();
                                    indexHyzxRefresh.setEnableRefresh(false);
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
                                        setData(isRefresh, array, nextPage, n);
                                    } else {
                                        if (mDataList != null) {
                                            mDataList.clear();
                                            mAdapter.notifyDataSetChanged();
                                        }
                                        //TODO 内容为空的处理
                                        loadingStatus.showEmpty();
                                        indexHyzxRefresh.setEnableRefresh(false);
                                    }
                                    isInitCache = true;
                                }

                            }
                        });
            } else {
                OkGo.<String>post(BiaoXunTongApi.URL_GETINDEXHYZXLIST)
                        .params("userid", id)
                        .params("page", page)
                        .params("size", size)
                        .execute(new StringCallback() {
                            @Override
                            public void onSuccess(Response<String> response) {
                                final JSONObject object = JSON.parseObject(response.body());
                                final JSONObject data = object.getJSONObject("data");
                                final JSONArray array = data.getJSONArray("list");
                                final boolean nextPage = data.getBoolean("nextpage");


                                if (array.size() > 0) {
                                    setData(isRefresh, array, nextPage, n);
                                } else {
                                    if (mDataList != null) {
                                        mDataList.clear();
                                        mAdapter.notifyDataSetChanged();
                                    }
                                    //TODO 内容为空的处理
                                    loadingStatus.showEmpty();
                                    indexHyzxRefresh.setEnableRefresh(false);
                                }
                            }
                        });
            }


        } else {
            //未登录的数据请求

            if (isRefresh) {
                page = 1;
                OkGo.<String>post(BiaoXunTongApi.URL_GETINDEXHYZXLIST)
                        .params("page", page)
                        .params("size", size)
                        .cacheKey("index_hyzx_no_login_cache" + id)
                        .cacheMode(CacheMode.FIRST_CACHE_THEN_REQUEST)
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
                                    setData(isRefresh, array, nextPage, n);
                                } else {
                                    if (mDataList != null) {
                                        mDataList.clear();
                                        mAdapter.notifyDataSetChanged();
                                    }
                                    //TODO 内容为空的处理
                                    loadingStatus.showEmpty();
                                    indexHyzxRefresh.setEnableRefresh(false);
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
                                        page = 2;
                                        setData(isRefresh, array, nextPage, n);
                                    } else {
                                        if (mDataList != null) {
                                            mDataList.clear();
                                            mAdapter.notifyDataSetChanged();
                                        }
                                        //TODO 内容为空的处理
                                        loadingStatus.showEmpty();
                                        indexHyzxRefresh.setEnableRefresh(false);
                                    }
                                    isInitCache = true;
                                }


                            }
                        });
            } else {
                OkGo.<String>post(BiaoXunTongApi.URL_GETINDEXHYZXLIST)
                        .params("page", page)
                        .params("size", size)
                        .execute(new StringCallback() {
                            @Override
                            public void onSuccess(Response<String> response) {
                                final JSONObject object = JSON.parseObject(response.body());
                                final JSONObject data = object.getJSONObject("data");
                                final JSONArray array = data.getJSONArray("list");
                                final boolean nextPage = data.getBoolean("nextpage");


                                if (array.size() > 0) {
                                    setData(isRefresh, array, nextPage, n);
                                } else {
                                    if (mDataList != null) {
                                        mDataList.clear();
                                        mAdapter.notifyDataSetChanged();
                                    }
                                    //TODO 内容为空的处理
                                    loadingStatus.showEmpty();
                                    indexHyzxRefresh.setEnableRefresh(false);
                                }
                            }
                        });
            }

        }

    }


    private void setData(boolean isRefresh, JSONArray data, boolean nextPage, int n) {
        List<Integer> imgs = new ArrayList<>();
        final int size = data == null ? 0 : data.size();
        if (isRefresh) {
            mDataList.clear();
            for (int i = 0; i < data.size(); i++) {

                imgs.add(R.mipmap.hyzx_1);
                imgs.add(R.mipmap.hyzx_2);
                imgs.add(R.mipmap.hyzx_3);
                imgs.add(R.mipmap.hyzx_4);
                imgs.add(R.mipmap.hyzx_5);

                IndexHyzxListBean bean = new IndexHyzxListBean();
                JSONObject list = data.getJSONObject(i);
                bean.setId(list.getInteger("id"));
                bean.setTitle(list.getString("title"));
                bean.setCreate_time(list.getString("create_time"));
                bean.setMobile_img(list.getString("mobile_img"));
                bean.setMobile_context(list.getString("mobile_context"));
                bean.setImg(imgs.get(i));
                mDataList.add(bean);
            }
            if (showStatus){
                indexHyzxRefresh.finishRefresh();
            }
            mAdapter.setEnableLoadMore(true);

        } else {
            page++;
            if (size > 0) {
                for (int i = 0; i < data.size(); i++) {
                    imgs.add(R.mipmap.hyzx_1);
                    imgs.add(R.mipmap.hyzx_2);
                    imgs.add(R.mipmap.hyzx_3);
                    imgs.add(R.mipmap.hyzx_4);
                    imgs.add(R.mipmap.hyzx_5);

                    IndexHyzxListBean bean = new IndexHyzxListBean();
                    JSONObject list = data.getJSONObject(i);
                    bean.setId(list.getInteger("id"));
                    bean.setTitle(list.getString("title"));
                    bean.setCreate_time(list.getString("create_time"));
                    bean.setMobile_img(list.getString("mobile_img"));
                    bean.setMobile_context(list.getString("mobile_context"));
                    bean.setImg(imgs.get(i));
                    mDataList.add(bean);
                }
            }

            indexHyzxRefresh.finishLoadmore(0, true);
        }
        if (!nextPage) {
            //第一页如果不够一页就不显示没有更多数据布局
            mAdapter.loadMoreEnd();
        } else {
            mAdapter.loadMoreComplete();
        }

        mAdapter.notifyDataSetChanged();
        if (showStatus) {
            loadingStatus.showContent();
        }

    }

    @OnClick(R.id.ll_iv_back)
    public void onViewClicked() {
        finish();
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        showStatus = false;
    }

}
