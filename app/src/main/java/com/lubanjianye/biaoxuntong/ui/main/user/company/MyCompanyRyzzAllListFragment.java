package com.lubanjianye.biaoxuntong.ui.main.user.company;

import android.content.DialogInterface;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemLongClickListener;
import com.classic.common.MultipleStatusView;
import com.lubanjianye.biaoxuntong.R;
import com.lubanjianye.biaoxuntong.base.BaseFragment1;
import com.lubanjianye.biaoxuntong.bean.MyCompanyRyzzAllListBean;
import com.lubanjianye.biaoxuntong.database.DatabaseManager;
import com.lubanjianye.biaoxuntong.database.UserProfile;
import com.lubanjianye.biaoxuntong.app.BiaoXunTongApi;
import com.lubanjianye.biaoxuntong.ui.view.loadmore.CustomLoadMoreView;
import com.lubanjianye.biaoxuntong.util.dialog.DialogHelper;
import com.lubanjianye.biaoxuntong.util.dialog.PromptDialog;
import com.lubanjianye.biaoxuntong.util.netStatus.NetUtil;
import com.lubanjianye.biaoxuntong.util.toast.ToastUtil;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import java.util.ArrayList;
import java.util.List;

/**
 * 项目名:   LBBXT
 * 包名:     com.lubanjianye.biaoxuntong.ui.main.fragment.user.company
 * 文件名:   MyCompanyRyzzAllListFragment
 * 创建者:   lunious
 * 创建时间: 2017/12/15  14:54
 * 描述:     TODO
 */

public class MyCompanyRyzzAllListFragment extends BaseFragment1 implements View.OnClickListener {

    private LinearLayout llIvBack = null;
    private AppCompatTextView mainBarName = null;

    private RecyclerView companyRyzzRecycler = null;
    private SmartRefreshLayout companyRyzzRefresh = null;
    private MultipleStatusView loadingStatus = null;

    private MyCompanyRyzzAllListAdapter mAdapter;
    private ArrayList<MyCompanyRyzzAllListBean> mDataList = new ArrayList<>();

    private int page = 1;

    PromptDialog promptDialog = null;

    private List<String> zzbm = new ArrayList<String>();
    private List<String> zgzy_code = new ArrayList<String>();
    private List<String> ryname = new ArrayList<String>();
    private String zzbm_id = "";
    private String zgzy_id = "";
    private String name = "";

    @Override
    public Object setLayout() {
        return R.layout.fragment_company_ryzz_more;
    }

    @Override
    public void initView() {
        llIvBack = getView().findViewById(R.id.ll_iv_back);
        mainBarName = getView().findViewById(R.id.main_bar_name);
        companyRyzzRecycler = getView().findViewById(R.id.company_ryzz_recycler);
        companyRyzzRefresh = getView().findViewById(R.id.company_ryzz_refresh);
        loadingStatus = getView().findViewById(R.id.mycompany_all_ryzz_list_status_view);
        llIvBack.setOnClickListener(this);

    }

    @Override
    public void initData() {
        llIvBack.setVisibility(View.VISIBLE);
        mainBarName.setText("全部人员资质");
        //创建对象
        promptDialog = new PromptDialog(getActivity());
        //设置自定义属性
        promptDialog.getDefaultBuilder().touchAble(true).round(3).loadingDuration(3000);

    }

    @Override
    public void initEvent() {
        initRecyclerView();
        initAdapter();
        initRefreshLayout();

        if (!NetUtil.isNetworkConnected(getActivity())) {
            ToastUtil.shortBottonToast(getContext(), "请检查网络设置");
            requestData(true);
            mAdapter.setEnableLoadMore(false);
        } else {
            requestData(true);
        }
    }

    private void initRefreshLayout() {

        companyRyzzRefresh.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshlayout) {

                if (!NetUtil.isNetworkConnected(getActivity())) {
                    ToastUtil.shortBottonToast(getContext(), "请检查网络设置");
                    companyRyzzRefresh.finishRefresh(2000, false);
                    mAdapter.setEnableLoadMore(false);
                } else {
                    requestData(true);
                }
            }
        });


//        resultRefresh.autoRefresh();

    }


    private void initRecyclerView() {


        companyRyzzRecycler.setLayoutManager(new LinearLayoutManager(getContext()));

        companyRyzzRecycler.addOnItemTouchListener(new OnItemLongClickListener() {
            @Override
            public void onSimpleItemLongClick(BaseQuickAdapter adapter, View view, int position) {

                List<String> operators = new ArrayList<>();
                operators.add("删除");

                final String[] os = new String[operators.size()];
                operators.toArray(os);

                zzbm_id = zzbm.get(position);
                zgzy_id = zgzy_code.get(position);
                name = ryname.get(position);

                DialogHelper.getSelectDialog(getContext(), os, getString(R.string.cancel),
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                DialogHelper.getConfirmDialog(getActivity(), "是否删除该条人员资质?",
                                        new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialogInterface, int i) {

                                                promptDialog.showLoading("正在删除……");

                                                OkGo.<String>post(BiaoXunTongApi.URL_DELERYZZ)
                                                        .params("userId", id)
                                                        .params("zg_mcdj_id", zzbm_id)
                                                        .params("zg_zy_id", zgzy_id)
                                                        .params("ryname", name)
                                                        .execute(new StringCallback() {
                                                            @Override
                                                            public void onSuccess(Response<String> response) {
                                                                final JSONObject object = JSON.parseObject(response.body());
                                                                final String status = object.getString("status");

                                                                if ("200".equals(status)) {
                                                                    promptDialog.showSuccess("删除成功！");

                                                                    if (mDataList != null) {
                                                                        mDataList.clear();
                                                                    }
                                                                    requestData(true);
                                                                } else {
                                                                    promptDialog.showError("删除失败！");
                                                                }
                                                            }
                                                        });


                                            }
                                        }).show();
                            }
                        }).show();
            }
        });


    }


    private void initAdapter() {
        mAdapter = new MyCompanyRyzzAllListAdapter(R.layout.fragment_company_ryzz, mDataList);

        mAdapter.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
            @Override
            public void onLoadMoreRequested() {
                //TODO 去加载更多数据
                requestData(false);
            }
        });
        //设置列表动画
//        mAdapter.openLoadAnimation(BaseQuickAdapter.SLIDEIN_BOTTOM);
        mAdapter.setLoadMoreView(new CustomLoadMoreView());
        companyRyzzRecycler.setAdapter(mAdapter);


    }

    private long id = 0;

    public void requestData(final boolean isRefresh) {

        List<UserProfile> users = DatabaseManager.getInstance().getDao().loadAll();
        long id = 0;
        String token = "";
        for (int i = 0; i < users.size(); i++) {
            id = users.get(0).getId();
            token = users.get(0).getToken();
        }


        if (isRefresh) {
            page = 1;
            OkGo.<String>post(BiaoXunTongApi.URL_GETALLCOMPANYRYZZ)
                    .params("userId", id)
                    .params("type", 0)
                    .params("size", 20)
                    .params("page", page)
                    .execute(new StringCallback() {
                        @Override
                        public void onSuccess(Response<String> response) {
                            final JSONObject object = JSON.parseObject(response.body());
                            String status = object.getString("status");

                            if ("200".equals(status)) {
                                final JSONObject dataObj = object.getJSONObject("data");

                                int pageCount = dataObj.getInteger("pageCount");
                                final JSONArray array = dataObj.getJSONArray("list");
                                final boolean nextPage = dataObj.getBoolean("nextpage");


                                for (int i = 0; i < array.size(); i++) {
                                    final JSONObject data = array.getJSONObject(i);
                                    zzbm.add(data.getString("zzbm"));
                                    zgzy_code.add(data.getString("zgzy_code"));
                                    ryname.add(data.getString("ryname"));
                                }

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
                                    companyRyzzRefresh.setEnableRefresh(false);
                                }
                            } else {
                                //TODO 请求数据失败的处理
                            }
                        }
                    });
        } else {
            OkGo.<String>post(BiaoXunTongApi.URL_GETALLCOMPANYRYZZ)
                    .params("userId", id)
                    .params("type", 0)
                    .params("size", 20)
                    .params("page", page)
                    .execute(new StringCallback() {
                        @Override
                        public void onSuccess(Response<String> response) {
                            final JSONObject object = JSON.parseObject(response.body());
                            String status = object.getString("status");

                            if ("200".equals(status)) {
                                final JSONObject dataObj = object.getJSONObject("data");

                                int pageCount = dataObj.getInteger("pageCount");
                                final JSONArray array = dataObj.getJSONArray("list");
                                final boolean nextPage = dataObj.getBoolean("nextpage");


                                for (int i = 0; i < array.size(); i++) {
                                    final JSONObject data = array.getJSONObject(i);
                                    zzbm.add(data.getString("zzbm"));
                                    zgzy_code.add(data.getString("zgzy_code"));
                                    ryname.add(data.getString("ryname"));
                                }

                                if (array.size() > 0) {
                                    setData(isRefresh, array, nextPage);
                                } else {
                                    if (mDataList != null) {
                                        mDataList.clear();
                                        mAdapter.notifyDataSetChanged();
                                    }
                                    //TODO 内容为空的处理
                                    loadingStatus.showEmpty();
                                    companyRyzzRefresh.setEnableRefresh(false);
                                }
                            } else {
                                //TODO 请求数据失败的处理
                            }
                        }
                    });
        }


    }

    private void setData(boolean isRefresh, JSONArray data, boolean nextPage) {

        final int size = data == null ? 0 : data.size();
        if (isRefresh) {
            mDataList.clear();
            loadingStatus.showContent();
            for (int i = 0; i < data.size(); i++) {
                MyCompanyRyzzAllListBean bean = new MyCompanyRyzzAllListBean();
                JSONObject list = data.getJSONObject(i);
                bean.setLx_name(list.getString("lx_name"));
                bean.setZgzy(list.getString("zgzy"));
                bean.setZg_name(list.getString("zg_name"));
                bean.setZg_mcdj(list.getString("zg_mcdj"));
                bean.setRyname(list.getString("ryname"));
                mDataList.add(bean);
            }
            companyRyzzRefresh.finishRefresh(0, true);
            mAdapter.setEnableLoadMore(true);
        } else {
            page++;
            loadingStatus.showContent();
            if (size > 0) {
                for (int i = 0; i < data.size(); i++) {
                    MyCompanyRyzzAllListBean bean = new MyCompanyRyzzAllListBean();
                    JSONObject list = data.getJSONObject(i);
                    bean.setLx_name(list.getString("lx_name"));
                    bean.setZgzy(list.getString("zgzy"));
                    bean.setZg_name(list.getString("zg_name"));
                    bean.setZg_mcdj(list.getString("zg_mcdj"));
                    bean.setRyname(list.getString("ryname"));
                    mDataList.add(bean);
                }
            }
            companyRyzzRefresh.finishLoadmore(0, true);

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
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ll_iv_back:
                getActivity().onBackPressed();
                break;
            default:
                break;
        }
    }
}
