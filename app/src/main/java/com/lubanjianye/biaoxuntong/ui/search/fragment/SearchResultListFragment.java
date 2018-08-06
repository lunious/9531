package com.lubanjianye.biaoxuntong.ui.search.fragment;

import android.content.Intent;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;

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
import com.lubanjianye.biaoxuntong.base.BaseFragment1;
import com.lubanjianye.biaoxuntong.bean.ResultListBean;
import com.lubanjianye.biaoxuntong.database.DatabaseManager;
import com.lubanjianye.biaoxuntong.database.UserProfile;
import com.lubanjianye.biaoxuntong.eventbus.EventMessage;
import com.lubanjianye.biaoxuntong.ui.detail.ResultArticleDetailActivity;
import com.lubanjianye.biaoxuntong.ui.main.result.ResultListAdapter;
import com.lubanjianye.biaoxuntong.ui.main.result.detail.chongqing.ResultCqsggjyzbjgDetailActivity;
import com.lubanjianye.biaoxuntong.ui.main.result.detail.sichuan.ResultSggjyzbjgDetailActivity;
import com.lubanjianye.biaoxuntong.ui.main.result.detail.sichuan.ResultXjgggDetailActivity;
import com.lubanjianye.biaoxuntong.ui.view.loadmore.CustomLoadMoreView;
import com.lubanjianye.biaoxuntong.util.aes.AesUtil;
import com.lubanjianye.biaoxuntong.util.netStatus.NetUtil;
import com.lubanjianye.biaoxuntong.util.sp.AppSharePreferenceMgr;
import com.lubanjianye.biaoxuntong.util.toast.ToastUtil;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lunious on 2018/3/28.
 * Desc:
 */
public class SearchResultListFragment extends BaseFragment1 {


    private RecyclerView resultRecycler = null;
    private SwipeRefreshLayout resultRefresh = null;
    private MultipleStatusView loadingStatus = null;

    private ResultListAdapter mAdapter;
    private ArrayList<ResultListBean> mDataList = new ArrayList<>();

    private int page = 1;

    private String mKeyword = "";

    private String mDiqu = "";

    private static String keyWorld = "";


    public static SearchResultListFragment newInstance(String contentt) {
        SearchResultListFragment fragment = new SearchResultListFragment();
        keyWorld = contentt;
        return fragment;
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
                requestData(2, mKeyword);
            }
        });
        //设置列表动画
//        mAdapter.openLoadAnimation(BaseQuickAdapter.SLIDEIN_BOTTOM);
        mAdapter.setLoadMoreView(new CustomLoadMoreView());
        resultRecycler.setAdapter(mAdapter);


    }


    @Override
    public Object setLayout() {
        return R.layout.fragment_search_list;
    }

    @Override
    public void initView() {
        resultRecycler = getView().findViewById(R.id.result_search_recycler);
        resultRefresh = getView().findViewById(R.id.result_search_refresh);
        loadingStatus = getView().findViewById(R.id.result_search_list_status_view);

        if (AppSharePreferenceMgr.contains(getContext(), EventMessage.LOCA_AREA)) {
            String area = (String) AppSharePreferenceMgr.get(getContext(), EventMessage.LOCA_AREA, "");

            mDiqu = area;
        } else {
            mDiqu = "四川";
        }

    }

    @Override
    public void initData() {
        initRecyclerView();
        initAdapter();
        resultRefresh.setRefreshing(false);
        resultRefresh.setEnabled(false);
    }

    @Override
    public void initEvent() {

        if (!NetUtil.isNetworkConnected(getActivity())) {
            ToastUtil.shortBottonToast(getContext(), "请检查网络设置");
            mAdapter.setEnableLoadMore(false);
            requestData(1, keyWorld);
        } else {
            requestData(0, keyWorld);
        }
    }

    private long id = 0;

    public void requestData(final int isRefresh, final String keyword) {

        if (!NetUtil.isNetworkConnected(getActivity())) {
            loadingStatus.showNoNetwork();
            resultRefresh.setEnabled(false);
        } else {
            if (isRefresh == 0) {
                loadingStatus.showLoading();
            }
            if (isRefresh == 0 || isRefresh == 1) {
                page = 1;
            }

            if (AppSharePreferenceMgr.contains(getContext(), EventMessage.LOGIN_SUCCSS)) {
                //已登录的数据请求
                List<UserProfile> users = DatabaseManager.getInstance().getDao().loadAll();

                for (int i = 0; i < users.size(); i++) {
                    id = users.get(0).getId();
                }

                OkGo.<String>post(BiaoXunTongApi.URL_GETRESULTLIST)
                        .params("keyWord", keyword)
                        .params("userid", id)
                        .params("page", page)
                        .params("diqu", mDiqu)
                        .params("size", 10)
                        .execute(new StringCallback() {
                            @Override
                            public void onSuccess(Response<String> response) {
                                String jiemi = AesUtil.aesDecrypt(response.body(), BiaoXunTongApi.PAS_KEY);

                                final JSONObject object = JSON.parseObject(jiemi);
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
                                    resultRefresh.setEnabled(false);
                                }
                            }
                        });

            } else {
                //未登录的数据请求

                OkGo.<String>post(BiaoXunTongApi.URL_GETRESULTLIST)
                        .params("keyWord", keyword)
                        .params("page", page)
                        .params("diqu", mDiqu)
                        .params("size", 10)
                        .execute(new StringCallback() {
                            @Override
                            public void onSuccess(Response<String> response) {
                                String jiemi = AesUtil.aesDecrypt(response.body(), BiaoXunTongApi.PAS_KEY);

                                final JSONObject object = JSON.parseObject(jiemi);
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
                                    resultRefresh.setEnabled(false);
                                }
                            }
                        });
            }

        }


    }

    private void setData(int isRefresh, JSONArray data, boolean nextPage) {
        page++;
        final int size = data == null ? 0 : data.size();
        if (isRefresh == 0 || isRefresh == 1) {
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
            resultRefresh.setRefreshing(false);
            mAdapter.setEnableLoadMore(true);
            mAdapter.notifyDataSetChanged();
        } else {
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
                mAdapter.notifyDataSetChanged();
            }
        }
        if (!nextPage) {
            //第一页如果不够一页就不显示没有更多数据布局
            mAdapter.loadMoreEnd();
        } else {
            mAdapter.loadMoreComplete();
        }


    }
}
