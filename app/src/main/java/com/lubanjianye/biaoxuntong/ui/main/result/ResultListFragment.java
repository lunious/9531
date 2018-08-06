package com.lubanjianye.biaoxuntong.ui.main.result;

import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import com.alibaba.android.arouter.launcher.ARouter;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.classic.common.MultipleStatusView;
import com.lubanjianye.biaoxuntong.R;
import com.lubanjianye.biaoxuntong.app.BiaoXunTong;
import com.lubanjianye.biaoxuntong.base.BaseFragment1;
import com.lubanjianye.biaoxuntong.bean.ResultListBean;
import com.lubanjianye.biaoxuntong.database.DatabaseManager;
import com.lubanjianye.biaoxuntong.database.UserProfile;
import com.lubanjianye.biaoxuntong.eventbus.EventMessage;
import com.lubanjianye.biaoxuntong.app.BiaoXunTongApi;
import com.lubanjianye.biaoxuntong.ui.detail.ResultArticleDetailActivity;
import com.lubanjianye.biaoxuntong.ui.main.result.detail.chongqing.ResultCqsggjyzbjgDetailActivity;
import com.lubanjianye.biaoxuntong.ui.main.result.detail.sichuan.ResultXjgggDetailActivity;
import com.lubanjianye.biaoxuntong.ui.search.activity.SearchActivity;
import com.lubanjianye.biaoxuntong.ui.view.loadmore.CustomLoadMoreView;
import com.lubanjianye.biaoxuntong.ui.main.result.detail.sichuan.ResultSggjyzbjgDetailActivity;
import com.lubanjianye.biaoxuntong.util.aes.AesUtil;
import com.lubanjianye.biaoxuntong.util.netStatus.NetUtil;
import com.lubanjianye.biaoxuntong.util.netStatus.AppSysMgr;
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


public class ResultListFragment extends BaseFragment1 {

    private RecyclerView resultRecycler = null;
    private SmartRefreshLayout resultRefresh = null;
    private MultipleStatusView loadingStatus = null;

    private String mTitle = null;
    private String mType = null;

    private ResultListAdapter mAdapter;
    private ArrayList<ResultListBean> mDataList = new ArrayList<>();


    private String deviceId = AppSysMgr.getPsuedoUniqueID();
    private boolean isInitCache = false;
    private long id = 0;
    private int page = 1;


    private void initRefreshLayout() {


        resultRefresh.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshlayout) {

                if (!NetUtil.isNetworkConnected(getActivity())) {
                    ToastUtil.shortBottonToast(getContext(), "请检查网络设置");
                    resultRefresh.finishRefresh(2000, false);
                    mAdapter.setEnableLoadMore(false);
                } else {
                    requestData(true);
                }
            }
        });

//        resultRefresh.autoRefresh();

    }

    private void initRecyclerView() {
        resultRecycler.setLayoutManager(new LinearLayoutManager(getActivity()));

        resultRecycler.addOnItemTouchListener(new OnItemClickListener() {
            @Override
            public void onSimpleItemClick(BaseQuickAdapter adapter, View view, int position) {
                final ResultListBean data = (ResultListBean) adapter.getData().get(position);
                final int entityId = data.getEntityid();
                final String entity = data.getEntity();
                final String entityUrl = data.getEntityUrl();

                Log.d("JASBHDBHSABDSADSAD", entityId + "___" + entity+"____"+entityUrl);

                Intent intent = null;

                if (!TextUtils.isEmpty(entityUrl)) {
                    intent = new Intent(BiaoXunTong.getApplicationContext(), ResultArticleDetailActivity.class);
                    intent.putExtra("entityId", entityId);
                    intent.putExtra("entity", entity);
                    intent.putExtra("ajaxlogtype", "0");
                    intent.putExtra("mId", "");
                    startActivity(intent);

                } else {
                    if ("xjggg".equals(entity) || "sjggg".equals(entity) || "sggjy".equals(entity) || "sggjycgjgtable".equals(entity)) {
                        intent = new Intent(getActivity(), ResultXjgggDetailActivity.class);
                        intent.putExtra("entityId", entityId);
                        intent.putExtra("entity", entity);
                        intent.putExtra("ajaxlogtype", "0");
                        intent.putExtra("mId", "");
                        startActivity(intent);

                    } else if ("sggjyzbjg".equals(entity) || "sggjycgjgrow".equals(entity) || "sggjyjgcgtable".equals(entity)) {
                        intent = new Intent(getActivity(), ResultSggjyzbjgDetailActivity.class);
                        intent.putExtra("entityId", entityId);
                        intent.putExtra("entity", entity);
                        intent.putExtra("ajaxlogtype", "0");
                        intent.putExtra("mId", "");
                        startActivity(intent);
                    } else if ("cqcggg".equals(entity)) {
                        final String title = data.getEntryName();
                        ARouter.getInstance().build("/com/BrowserDetailActivity").withString("mApi",BiaoXunTongApi.URL_GETRESULTLISTDETAIL)
                                .withString("mTitle",title).withString("mEntity",entity).withInt("mEntityid",entityId).navigation();
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
        mAdapter = new ResultListAdapter(R.layout.fragment_result_item, mDataList);

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
        resultRecycler.setAdapter(mAdapter);


    }

    public static ResultListFragment getInstance(String title) {
        ResultListFragment sf = new ResultListFragment();
        sf.mTitle = title;
        return sf;
    }

    @Override
    public Object setLayout() {
        return R.layout.fragment_result_simple;
    }

    @Override
    public void initView() {

        //注册EventBus
        EventBus.getDefault().register(this);

        resultRecycler = getView().findViewById(R.id.result_recycler);
        resultRefresh = getView().findViewById(R.id.result_refresh);
        loadingStatus = getView().findViewById(R.id.result_list_status_view);

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

        //取消注册EventBus
        EventBus.getDefault().unregister(this);
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void XXXXXX(EventMessage message) {

        if (EventMessage.LOCA_AREA_CHANGE.equals(message.getMessage())) {

            requestData(true);
        }

    }

    @Override
    public void initData() {
        if ("政府采购结果公告".equals(mTitle)) {
            mType = "采购";
        } else if ("工程招标中标公示".equals(mTitle)) {
            mType = "工程";
        }
    }

    @Override
    public void initEvent() {
        initRecyclerView();
        initAdapter();
        addHeadView();
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

    private void addHeadView() {
        View headView = getLayoutInflater().inflate(R.layout.result_search_header_view, (ViewGroup) resultRecycler.getParent(), false);
        headView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //点击搜索
                Intent intent = new Intent(getActivity(), SearchActivity.class);
                intent.putExtra("searchTye", 2);
                startActivity(intent);
            }
        });
        mAdapter.addHeaderView(headView);
    }


    private String mDiqu = "";

    public void requestData(final boolean isRefresh) {


        if (AppSharePreferenceMgr.contains(getContext(), EventMessage.LOCA_AREA)) {
            mDiqu = (String) AppSharePreferenceMgr.get(getContext(), EventMessage.LOCA_AREA, "");
        }

        if (AppSharePreferenceMgr.contains(getContext(), EventMessage.LOGIN_SUCCSS)) {
            //已登录的数据请求
            List<UserProfile> users = DatabaseManager.getInstance().getDao().loadAll();

            for (int i = 0; i < users.size(); i++) {
                id = users.get(0).getId();
            }

            if (isRefresh) {
                page = 1;
                OkGo.<String>post(BiaoXunTongApi.URL_GETRESULTLIST)
                        .params("type", mType)
                        .params("userid", id)
                        .params("page", page)
                        .params("diqu", mDiqu)
                        .params("size", 10)
                        .params("deviceId", deviceId)
                        .cacheKey("result_login_cache" + mTitle + mDiqu)
                        .cacheMode(CacheMode.FIRST_CACHE_THEN_REQUEST)
                        .cacheTime(3600 * 72000)
                        .execute(new StringCallback() {
                            @Override
                            public void onSuccess(Response<String> response) {
                                String jiemi = AesUtil.aesDecrypt(response.body(), BiaoXunTongApi.PAS_KEY);

                                final JSONObject object = JSON.parseObject(jiemi);
                                final String status = object.getString("status");
                                final String message = object.getString("message");

                                Log.d("ASDSADSAD",id+"");
                                Log.d("JABNDSDASDA",jiemi);

                                if ("200".equals(status)) {
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
                                        resultRefresh.setEnableRefresh(false);
                                    }
                                } else {
                                    ToastUtil.shortToast(getActivity(), message);
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
                                            setData(isRefresh, array, nextPage);
                                        } else {
                                            if (mDataList != null) {
                                                mDataList.clear();
                                                mAdapter.notifyDataSetChanged();
                                            }
                                            //TODO 内容为空的处理
                                            loadingStatus.showEmpty();
                                            resultRefresh.setEnableRefresh(false);
                                        }
                                    } else {
                                        ToastUtil.shortToast(getActivity(), message);
                                    }

                                    isInitCache = true;
                                }
                            }
                        });

            } else {
                OkGo.<String>post(BiaoXunTongApi.URL_GETRESULTLIST)
                        .params("type", mType)
                        .params("userid", id)
                        .params("page", page)
                        .params("diqu", mDiqu)
                        .params("size", 10)
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
                                        setData(isRefresh, array, nextPage);
                                    } else {
                                        if (mDataList != null) {
                                            mDataList.clear();
                                            mAdapter.notifyDataSetChanged();
                                        }
                                        //TODO 内容为空的处理
                                        loadingStatus.showEmpty();
                                        resultRefresh.setEnableRefresh(false);
                                    }
                                } else {
                                    ToastUtil.shortToast(getActivity(), message);
                                }


                            }
                        });
            }


        } else {
            //未登录的数据请求
            if (isRefresh) {
                page = 1;
                OkGo.<String>post(BiaoXunTongApi.URL_GETRESULTLIST)
                        .params("type", mType)
                        .params("page", page)
                        .params("size", 10)
                        .params("diqu", mDiqu)
                        .params("deviceId", deviceId)
                        .cacheKey("result_no_login_cache" + mTitle + mDiqu)
                        .cacheMode(CacheMode.FIRST_CACHE_THEN_REQUEST)
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
                                        setData(isRefresh, array, nextPage);
                                    } else {
                                        if (mDataList != null) {
                                            mDataList.clear();
                                            mAdapter.notifyDataSetChanged();
                                        }
                                        //TODO 内容为空的处理
                                        loadingStatus.showEmpty();
                                        resultRefresh.setEnableRefresh(false);
                                    }
                                } else {
                                    ToastUtil.shortToast(getActivity(), message);
                                }


                            }

                            @Override
                            public void onCacheSuccess(Response<String> response) {
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
                                        setData(isRefresh, array, nextPage);
                                    } else {
                                        if (mDataList != null) {
                                            mDataList.clear();
                                            mAdapter.notifyDataSetChanged();
                                        }
                                        //TODO 内容为空的处理
                                        loadingStatus.showEmpty();
                                        resultRefresh.setEnableRefresh(false);
                                    }
                                } else {
                                    ToastUtil.shortToast(getActivity(), message);
                                }


                            }
                        });
            } else {
                OkGo.<String>post(BiaoXunTongApi.URL_GETRESULTLIST)
                        .params("type", mType)
                        .params("page", page)
                        .params("diqu", mDiqu)
                        .params("size", 10)
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
                                        setData(isRefresh, array, nextPage);
                                    } else {
                                        if (mDataList != null) {
                                            mDataList.clear();
                                            mAdapter.notifyDataSetChanged();
                                        }
                                        //TODO 内容为空的处理
                                        loadingStatus.showEmpty();
                                        resultRefresh.setEnableRefresh(false);
                                    }
                                } else {
                                    ToastUtil.shortToast(getActivity(), message);
                                }


                            }
                        });
            }


        }


    }

    private void setData(boolean isRefresh, JSONArray data, boolean nextPage) {
        final int size = data == null ? 0 : data.size();
        if (isRefresh) {
            loadingStatus.showContent();
            mDataList.clear();
            for (int i = 0; i < data.size(); i++) {
                ResultListBean bean = new ResultListBean();
                JSONObject list = data.getJSONObject(i);
                bean.setEntryName(list.getString("entryName"));
                bean.setSysTime(list.getString("sysTime"));
                bean.setEntityid(list.getInteger("entityid"));
                bean.setEntity(list.getString("entity"));
                bean.setEntityUrl(list.getString("entityUrl"));
                mDataList.add(bean);
            }

            resultRefresh.finishRefresh(0, true);
            mAdapter.setEnableLoadMore(true);

        } else {
            page++;
            loadingStatus.showContent();
            if (size > 0) {
                for (int i = 0; i < data.size(); i++) {
                    ResultListBean bean = new ResultListBean();
                    JSONObject list = data.getJSONObject(i);
                    bean.setEntryName(list.getString("entryName"));
                    bean.setType(list.getString("type"));
                    bean.setSysTime(list.getString("sysTime"));
                    bean.setEntityid(list.getInteger("entityid"));
                    bean.setEntity(list.getString("entity"));
                    bean.setEntityUrl(list.getString("entityUrl"));
                    mDataList.add(bean);
                }
            }

            resultRefresh.finishLoadmore(0, true);

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
