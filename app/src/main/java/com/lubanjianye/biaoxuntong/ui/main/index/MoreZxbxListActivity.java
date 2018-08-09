package com.lubanjianye.biaoxuntong.ui.main.index;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.LinearLayout;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
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
import com.lubanjianye.biaoxuntong.bean.IndexListBean;
import com.lubanjianye.biaoxuntong.database.DatabaseManager;
import com.lubanjianye.biaoxuntong.database.UserProfile;
import com.lubanjianye.biaoxuntong.eventbus.EventMessage;
import com.lubanjianye.biaoxuntong.ui.detail.IndexArticleDetailActivity;
import com.lubanjianye.biaoxuntong.ui.main.index.detail.sichuan.IndexBxtgdjDetailActivity;
import com.lubanjianye.biaoxuntong.ui.main.index.detail.sichuan.IndexScgggDetailActivity;
import com.lubanjianye.biaoxuntong.ui.main.index.detail.sichuan.IndexSggjyDetailActivity;
import com.lubanjianye.biaoxuntong.ui.main.index.detail.sichuan.IndexSggjycgrowDetailActivity;
import com.lubanjianye.biaoxuntong.ui.main.index.detail.sichuan.IndexSggjycgtableDetailActivity;
import com.lubanjianye.biaoxuntong.ui.main.index.detail.sichuan.IndexXcgggDetailActivity;
import com.lubanjianye.biaoxuntong.ui.view.loadmore.CustomLoadMoreView;
import com.lubanjianye.biaoxuntong.util.aes.AesUtil;
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

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


/**
 * Author: lunious
 * Date: 2018/8/9 21:36
 * Description:
 */
@Route(path = "/com/MoreZxbxListActivity")
public class MoreZxbxListActivity extends BaseActivity {
    @BindView(R.id.index_recycler)
    RecyclerView indexRecycler;
    @BindView(R.id.index_list_status_view)
    MultipleStatusView loadingStatus;
    @BindView(R.id.index_refresh)
    SmartRefreshLayout indexRefresh;

    private static String mDiqu = null;
    @BindView(R.id.ll_iv_back)
    LinearLayout llIvBack;
    @BindView(R.id.main_bar_name)
    AppCompatTextView mainBarName;


    private String deviceId = AppSysMgr.getPsuedoUniqueID();


    private IndexListAdapter mAdapter = null;
    private ArrayList<IndexListBean> mDataList = new ArrayList<>();

    private int page = 1;
    private boolean isInitCache = false;
    private boolean showStatus = true;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_more_zxbx;
    }

    @Override
    protected void initData(Bundle savedInstanceState) {
        initRecyclerView();
        initAdapter();
        initRefreshLayout();
        llIvBack.setVisibility(View.VISIBLE);
        mainBarName.setText("最新标讯");
    }

    @Override
    protected void initEvent(Bundle savedInstanceState) {
        if (!NetUtil.isNetworkConnected(MoreZxbxListActivity.this)) {
            ToastUtil.shortBottonToast(MoreZxbxListActivity.this, "请检查网络设置");
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


        indexRefresh.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshlayout) {

                if (!NetUtil.isNetworkConnected(MoreZxbxListActivity.this)) {
                    ToastUtil.shortBottonToast(MoreZxbxListActivity.this, "请检查网络设置");
                    indexRefresh.finishRefresh(2000, false);
                    mAdapter.setEnableLoadMore(false);
                } else {
                    requestData(true, 2);
                }
            }
        });

//        indexRefresh.autoRefresh();

    }

    private void initRecyclerView() {

        indexRecycler.setLayoutManager(new LinearLayoutManager(MoreZxbxListActivity.this));

        indexRecycler.addOnItemTouchListener(new OnItemClickListener() {
            @Override
            public void onSimpleItemClick(BaseQuickAdapter adapter, View view, int position) {
                final IndexListBean data = (IndexListBean) adapter.getData().get(position);
                final int entityId = data.getEntityId();
                final String entity = data.getEntity();
                final String entityUrl = data.getEntityUrl();


                Intent intent = null;

                if (!TextUtils.isEmpty(entityUrl)) {
                    intent = new Intent(BiaoXunTong.getApplicationContext(), IndexArticleDetailActivity.class);
                    intent.putExtra("entityId", entityId);
                    intent.putExtra("entity", entity);
                    intent.putExtra("ajaxlogtype", "0");
                    intent.putExtra("mId", "");
                    startActivity(intent);
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
                    } else if ("sggjycgrow".equals(entity)) {
                        intent = new Intent(BiaoXunTong.getApplicationContext(), IndexSggjycgrowDetailActivity.class);
                        intent.putExtra("entityId", entityId);
                        intent.putExtra("entity", entity);
                        intent.putExtra("ajaxlogtype", "0");
                        intent.putExtra("mId", "");
                        startActivity(intent);
                    } else if ("scggg".equals(entity)) {
                        intent = new Intent(BiaoXunTong.getApplicationContext(), IndexScgggDetailActivity.class);
                        intent.putExtra("entityId", entityId);
                        intent.putExtra("entity", entity);
                        intent.putExtra("ajaxlogtype", "0");
                        intent.putExtra("mId", "");
                        startActivity(intent);
                    } else if ("cqcggg".equals(entity)) {
                        final String title = data.getEntryName();
                        ARouter.getInstance().build("/com/BrowserDetailActivity").withString("mApi", BiaoXunTongApi.URL_GETCOLLECTIONLISTDETAIL)
                                .withString("mTitle", title).withString("mEntity", entity).withInt("mEntityid", entityId).navigation();

                    } else if ("cqsggjy".equals(entity)) {
                        intent = new Intent(BiaoXunTong.getApplicationContext(), IndexArticleDetailActivity.class);
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


        mAdapter = new IndexListAdapter(R.layout.fragment_index_zxbx_item, mDataList);

        mAdapter.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
            @Override
            public void onLoadMoreRequested() {
                //TODO 去加载更多数据
                if (!NetUtil.isNetworkConnected(MoreZxbxListActivity.this)) {
                    ToastUtil.shortBottonToast(MoreZxbxListActivity.this, "请检查网络设置");
                } else {
                    requestData(false, 0);
                }
            }
        });

        //设置列表动画
//        mAdapter.openLoadAnimation(BaseQuickAdapter.SLIDEIN_LEFT);
        mAdapter.setLoadMoreView(new CustomLoadMoreView());
        indexRecycler.setAdapter(mAdapter);


    }


    private long id = 0;


    public void requestData(final boolean isRefresh, final int n) {


        int size = 10 + (int) (Math.random() * 5);

        if (AppSharePreferenceMgr.contains(MoreZxbxListActivity.this, EventMessage.LOCA_AREA)) {
            mDiqu = (String) AppSharePreferenceMgr.get(MoreZxbxListActivity.this, EventMessage.LOCA_AREA, "");
        }

        if (AppSharePreferenceMgr.contains(MoreZxbxListActivity.this, EventMessage.LOGIN_SUCCSS)) {
            //已登录的数据请求
            List<UserProfile> users = DatabaseManager.getInstance().getDao().loadAll();
            for (int i = 0; i < users.size(); i++) {
                id = users.get(0).getId();
            }

            if (isRefresh) {
                page = 1;
                OkGo.<String>post(BiaoXunTongApi.URL_GETINDEXLIST)
                        .params("type", "最新标讯")
                        .params("userid", id)
                        .params("page", page)
                        .params("diqu", mDiqu)
                        .params("size", size)
                        .params("deviceId", deviceId)
                        .cacheKey("index_list_login_cache" + id + mDiqu)
                        .cacheMode(CacheMode.REQUEST_FAILED_READ_CACHE)
                        .cacheTime(3600 * 72000)
                        .execute(new StringCallback() {
                            @Override
                            public void onSuccess(Response<String> response) {

                                String jiemi = AesUtil.aesDecrypt(response.body(), BiaoXunTongApi.PAS_KEY);

                                final JSONObject object = JSON.parseObject(jiemi);
                                final String status = object.getString("status");
                                final String message = object.getString("message");

                                if ("200".equals(status)) {
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
                                        indexRefresh.setEnableRefresh(false);
                                    }
                                } else {
                                    ToastUtil.shortToast(MoreZxbxListActivity.this, message);
                                }
                            }

                            @Override
                            public void onCacheSuccess(Response<String> response) {
                                if (!isInitCache) {
                                    String jiemi = AesUtil.aesDecrypt(response.body(), BiaoXunTongApi.PAS_KEY);

                                    final JSONObject object = JSON.parseObject(jiemi);
                                    final String status = object.getString("status");
                                    final String message = object.getString("message");

                                    if ("200".equals(status)) {
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
                                            indexRefresh.setEnableRefresh(false);
                                        }
                                    } else {
                                        ToastUtil.shortToast(MoreZxbxListActivity.this, message);
                                    }
                                    isInitCache = true;
                                }
                            }
                        });
            } else {
                OkGo.<String>post(BiaoXunTongApi.URL_GETINDEXLIST)
                        .params("type", "最新标讯")
                        .params("userid", id)
                        .params("page", page)
                        .params("diqu", mDiqu)
                        .params("size", size)
                        .params("deviceId", deviceId)
                        .execute(new StringCallback() {
                            @Override
                            public void onSuccess(Response<String> response) {

                                String jiemi = AesUtil.aesDecrypt(response.body(), BiaoXunTongApi.PAS_KEY);

                                final JSONObject object = JSON.parseObject(jiemi);
                                final String status = object.getString("status");
                                final String message = object.getString("message");

                                if ("200".equals(status)) {
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
                                        indexRefresh.setEnableRefresh(false);
                                    }
                                } else {
                                    ToastUtil.shortToast(MoreZxbxListActivity.this, message);
                                }
                            }
                        });
            }


        } else {
            //未登录的数据请求

            if (isRefresh) {
                page = 1;
                OkGo.<String>post(BiaoXunTongApi.URL_GETINDEXLIST)
                        .params("type", "最新标讯")
                        .params("page", page)
                        .params("size", size)
                        .params("diqu", mDiqu)
                        .params("deviceId", deviceId)
                        .cacheKey("index_list_no_login_cache" + id + mDiqu)
                        .cacheMode(CacheMode.REQUEST_FAILED_READ_CACHE)
                        .cacheTime(3600 * 72000)
                        .execute(new StringCallback() {
                            @Override
                            public void onSuccess(Response<String> response) {
                                String jiemi = AesUtil.aesDecrypt(response.body(), BiaoXunTongApi.PAS_KEY);

                                final JSONObject object = JSON.parseObject(jiemi);
                                final String status = object.getString("status");
                                final String message = object.getString("message");

                                if ("200".equals(status)) {
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
                                        indexRefresh.setEnableRefresh(false);
                                    }
                                } else {
                                    ToastUtil.shortToast(MoreZxbxListActivity.this, message);
                                }

                            }

                            @Override
                            public void onCacheSuccess(Response<String> response) {
                                if (!isInitCache) {
                                    String jiemi = AesUtil.aesDecrypt(response.body(), BiaoXunTongApi.PAS_KEY);

                                    final JSONObject object = JSON.parseObject(jiemi);
                                    final String status = object.getString("status");
                                    final String message = object.getString("message");

                                    if ("200".equals(status)) {
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
                                            indexRefresh.setEnableRefresh(false);
                                        }
                                    } else {
                                        ToastUtil.shortToast(MoreZxbxListActivity.this, message);
                                    }
                                    isInitCache = true;
                                }
                            }
                        });
            } else {
                OkGo.<String>post(BiaoXunTongApi.URL_GETINDEXLIST)
                        .params("type", "最新标讯")
                        .params("page", page)
                        .params("size", size)
                        .params("diqu", mDiqu)
                        .params("deviceId", deviceId)
                        .execute(new StringCallback() {
                            @Override
                            public void onSuccess(Response<String> response) {
                                String jiemi = AesUtil.aesDecrypt(response.body(), BiaoXunTongApi.PAS_KEY);

                                final JSONObject object = JSON.parseObject(jiemi);
                                final String status = object.getString("status");
                                final String message = object.getString("message");

                                if ("200".equals(status)) {
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
                                        indexRefresh.setEnableRefresh(false);
                                    }
                                } else {
                                    ToastUtil.shortToast(MoreZxbxListActivity.this, message);
                                }
                            }
                        });
            }

        }

    }

    private void setData(boolean isRefresh, JSONArray data, boolean nextPage, int n) {
        final int size = data == null ? 0 : data.size();
        if (isRefresh) {
            mDataList.clear();
            for (int i = 0; i < data.size(); i++) {
                IndexListBean bean = new IndexListBean();
                JSONObject list = data.getJSONObject(i);
                bean.setEntryName(list.getString("entryName"));
                bean.setSysTime(list.getString("sysTime"));
                bean.setEntityId(list.getInteger("entityId"));
                bean.setEntity(list.getString("entity"));
                bean.setDeadTime(list.getString("deadTime"));
                bean.setAddress(list.getString("address"));
                bean.setEntityUrl(list.getString("entityUrl"));
                bean.setType(list.getString("type"));
                bean.setSignstauts(list.getString("signstauts"));
                bean.setIsResult(list.getString("isResult"));
                bean.setIsCorrections(list.getString("isCorrections"));
                mDataList.add(bean);
            }

            if (showStatus) {
                indexRefresh.finishRefresh();
            }
            mAdapter.setEnableLoadMore(true);

        } else {
            page++;
            if (size > 0) {
                for (int i = 0; i < data.size(); i++) {
                    IndexListBean bean = new IndexListBean();
                    JSONObject list = data.getJSONObject(i);
                    bean.setEntryName(list.getString("entryName"));
                    bean.setSysTime(list.getString("sysTime"));
                    bean.setEntityId(list.getInteger("entityId"));
                    bean.setEntity(list.getString("entity"));
                    bean.setDeadTime(list.getString("deadTime"));
                    bean.setAddress(list.getString("address"));
                    bean.setEntityUrl(list.getString("entityUrl"));
                    bean.setType(list.getString("type"));
                    bean.setSignstauts(list.getString("signstauts"));
                    bean.setIsResult(list.getString("isResult"));
                    bean.setIsCorrections(list.getString("isCorrections"));
                    mDataList.add(bean);
                }

            }
            indexRefresh.finishLoadmore(0, true);
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
