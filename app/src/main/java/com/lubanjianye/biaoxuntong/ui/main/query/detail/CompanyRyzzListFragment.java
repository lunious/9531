package com.lubanjianye.biaoxuntong.ui.main.query.detail;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.classic.common.MultipleStatusView;
import com.lubanjianye.biaoxuntong.R;
import com.lubanjianye.biaoxuntong.app.BiaoXunTongApi;
import com.lubanjianye.biaoxuntong.base.BaseFragment1;
import com.lubanjianye.biaoxuntong.bean.CompanyRyzzListBean;
import com.lubanjianye.biaoxuntong.database.DatabaseManager;
import com.lubanjianye.biaoxuntong.database.UserProfile;
import com.lubanjianye.biaoxuntong.ui.view.loadmore.CustomLoadMoreView;
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


public class CompanyRyzzListFragment extends BaseFragment1 implements View.OnClickListener {


    private LinearLayout llIvBack = null;
    private AppCompatTextView mainBarName = null;
    private RecyclerView companyRyzzRecycler = null;
    private SmartRefreshLayout companyRyzzRefresh = null;
    private MultipleStatusView loadingStatus = null;

    private View noDataView;

    private CompanyRyzzListAdapter mAdapter;
    private ArrayList<CompanyRyzzListBean> mDataList = new ArrayList<>();

    private int page = 1;
    private int pageSize = 20;

    private static final String ARG_SFID = "ARG_SFID";
    private String sfId = "";

    @Override
    public Object setLayout() {
        return R.layout.fragment_company_ryzz_more;
    }

    public static CompanyRyzzListFragment create(@NonNull String entity) {
        final Bundle args = new Bundle();
        args.putString(ARG_SFID, entity);
        final CompanyRyzzListFragment fragment = new CompanyRyzzListFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final Bundle args = getArguments();
        if (args != null) {
            sfId = args.getString(ARG_SFID);
        }
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
    }

    @Override
    public void initEvent() {
        initRecyclerView();
        initAdapter();
        initRefreshLayout();

        if (!NetUtil.isNetworkConnected(getActivity())) {
            ToastUtil.shortBottonToast(getContext(), "请检查网络设置");
            requestData();
            mAdapter.setEnableLoadMore(false);
        } else {
            requestData();
        }
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

    private void initRefreshLayout() {

        companyRyzzRefresh.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshlayout) {

                if (!NetUtil.isNetworkConnected(getActivity())) {
                    ToastUtil.shortBottonToast(getContext(), "请检查网络设置");
                    companyRyzzRefresh.finishRefresh(2000, false);
                    mAdapter.setEnableLoadMore(false);
                } else {
                    requestData();
                }
            }
        });

//        indexRefresh.autoRefresh();


    }

    private void initRecyclerView() {
        companyRyzzRecycler.setLayoutManager(new LinearLayoutManager(getContext()));

        noDataView = getActivity().getLayoutInflater().inflate(R.layout.custom_empty_view, (ViewGroup) companyRyzzRecycler.getParent(), false);
        noDataView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                requestData();
            }
        });

    }

    private void initAdapter() {
        mAdapter = new CompanyRyzzListAdapter(R.layout.fragment_company_ryzz, mDataList);
        //设置列表动画
//        mAdapter.openLoadAnimation(BaseQuickAdapter.SLIDEIN_BOTTOM);
        mAdapter.setLoadMoreView(new CustomLoadMoreView());
        companyRyzzRecycler.setAdapter(mAdapter);


    }


    public void requestData() {

        List<UserProfile> users = DatabaseManager.getInstance().getDao().loadAll();
        long id = 0;
        String token = "";
        for (int i = 0; i < users.size(); i++) {
            id = users.get(0).getId();
            token = users.get(0).getToken();
        }

        if (!NetUtil.isNetworkConnected(getActivity())) {
            loadingStatus.showNoNetwork();
        } else {
            loadingStatus.showLoading();

            OkGo.<String>post(BiaoXunTongApi.URL_COMPANYRYZZ + sfId)
                    .params("userId", id)
                    .params("token", token)
                    .execute(new StringCallback() {
                        @Override
                        public void onSuccess(Response<String> response) {
                            final JSONObject object = JSON.parseObject(response.body());
                            final JSONArray array = object.getJSONArray("data");

                            if (array.size() > 0) {
                                setData(array);
                            } else {
                                if (mDataList != null) {
                                    mDataList.clear();
                                    mAdapter.notifyDataSetChanged();
                                }
                                //TODO 内容为空的处理
                                mAdapter.setEmptyView(noDataView);
                                if (companyRyzzRefresh != null) {
                                    companyRyzzRefresh.setEnableRefresh(false);
                                }
                            }
                        }
                    });

        }

    }

    private void setData(JSONArray data) {
        final int size = data == null ? 0 : data.size();
        mDataList.clear();
        for (int i = 0; i < data.size(); i++) {
            CompanyRyzzListBean bean = new CompanyRyzzListBean();
            JSONObject list = data.getJSONObject(i);
            bean.setRy(list.getString("ry"));
            bean.setZgMcdj(list.getString("zgMcdj"));
            bean.setZgZy(list.getString("zgZy"));
            mDataList.add(bean);
        }
        mAdapter.setEnableLoadMore(true);
        mAdapter.notifyDataSetChanged();
        companyRyzzRefresh.finishRefresh(0, true);
        if (size < pageSize) {
            //第一页如果不够一页就不显示没有更多数据布局
            mAdapter.loadMoreEnd();
        } else {
            mAdapter.loadMoreComplete();
        }

        loadingStatus.showContent();
    }
}
