package com.lubanjianye.biaoxuntong.ui.main.query;

import android.content.Intent;
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
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.classic.common.MultipleStatusView;
import com.lubanjianye.biaoxuntong.R;
import com.lubanjianye.biaoxuntong.app.BiaoXunTongApi;
import com.lubanjianye.biaoxuntong.base.BaseFragment1;
import com.lubanjianye.biaoxuntong.bean.CompanySearchResultListBean;
import com.lubanjianye.biaoxuntong.database.DatabaseManager;
import com.lubanjianye.biaoxuntong.database.UserProfile;
import com.lubanjianye.biaoxuntong.eventbus.EventMessage;
import com.lubanjianye.biaoxuntong.ui.main.query.detail.CompanyDetailActivity;
import com.lubanjianye.biaoxuntong.ui.sign.SignInActivity;
import com.lubanjianye.biaoxuntong.util.netStatus.NetUtil;
import com.lubanjianye.biaoxuntong.util.sp.AppSharePreferenceMgr;
import com.lubanjianye.biaoxuntong.util.toast.ToastUtil;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadmoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import java.util.ArrayList;
import java.util.List;


public class CompanySearchResultFragment extends BaseFragment1 implements View.OnClickListener {

    private LinearLayout llIvBack = null;
    private AppCompatTextView mainBarName = null;
    private RecyclerView companySearchResultRecycler = null;
    private SmartRefreshLayout companySearchResultRefresh = null;

    private static final String ARG_PROVINCECODE = "ARG_PROVINCECODE";
    private static final String ARG_QYID = "ARG_QYID";
    private static final String ARG_SHOWSIGN = "ARG_SHOWSIGN";
    private View noDataView;
    private MultipleStatusView loadingStatus = null;


    private String mProvinceCode = "";
    private String mqyIds = "";
    private String mShowSign = "";

    private CompanySearchResultListAdapter mAdapter;
    private ArrayList<CompanySearchResultListBean> mDataList = new ArrayList<>();

    private int page = 1;

    @Override
    public Object setLayout() {
        return R.layout.fragment_company_search_result;
    }

    public static CompanySearchResultFragment create(@NonNull String provinceCode, String qyId, String showSign) {
        final Bundle args = new Bundle();
        args.putString(ARG_PROVINCECODE, provinceCode);
        args.putString(ARG_QYID, qyId);
        args.putString(ARG_SHOWSIGN, showSign);
        final CompanySearchResultFragment fragment = new CompanySearchResultFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        final Bundle args = getArguments();
        if (args != null) {
            mProvinceCode = args.getString(ARG_PROVINCECODE);
            mqyIds = args.getString(ARG_QYID);
            mShowSign = args.getString(ARG_SHOWSIGN);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

        //取消注册EventBus
        EventBus.getDefault().unregister(this);
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void XXXXXX(EventMessage message) {

        if (EventMessage.LOGIN_SUCCSS.equals(message.getMessage())) {
            //登陆成功后更新UI
            requestData(true);

        } else {

        }
    }

    @Override
    public void initView() {

        //注册EventBus
        EventBus.getDefault().register(this);

        llIvBack = getView().findViewById(R.id.ll_iv_back);
        mainBarName = getView().findViewById(R.id.main_bar_name);
        companySearchResultRecycler = getView().findViewById(R.id.company_search_result_recycler);
        companySearchResultRefresh = getView().findViewById(R.id.company_search_result_refresh);
        loadingStatus = getView().findViewById(R.id.result_search_company_status_view);
        llIvBack.setOnClickListener(this);


    }

    @Override
    public void initData() {
        llIvBack.setVisibility(View.VISIBLE);
        mainBarName.setText("查询结果");
    }

    @Override
    public void initEvent() {
        initRecyclerView();
        initAdapter();
        initRefreshLayout();

        if (!NetUtil.isNetworkConnected(getActivity())) {
            ToastUtil.shortBottonToast(getContext(), "请检查网络设置");
        } else {
            requestData(true);
        }
    }


    private void initRefreshLayout() {


        companySearchResultRefresh.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshlayout) {

                if (!NetUtil.isNetworkConnected(getActivity())) {
                    ToastUtil.shortBottonToast(getContext(), "请检查网络设置");
                    companySearchResultRefresh.finishRefresh(2000, false);
                } else {
                    requestData(true);
                }
            }
        });

        companySearchResultRefresh.setOnLoadmoreListener(new OnLoadmoreListener() {
            @Override
            public void onLoadmore(RefreshLayout refreshlayout) {

                //TODO 去加载更多数据
                if (!NetUtil.isNetworkConnected(getActivity())) {
                    ToastUtil.shortBottonToast(getContext(), "请检查网络设置");
                } else {
                    requestData(false);
                }
            }
        });

//        indexRefresh.autoRefresh();

    }

    private void initRecyclerView() {

        companySearchResultRecycler.setLayoutManager(new LinearLayoutManager(getContext()));

        companySearchResultRecycler.addOnItemTouchListener(new OnItemClickListener() {
            @Override
            public void onSimpleItemClick(BaseQuickAdapter adapter, View view, int position) {

                final CompanySearchResultListBean data = (CompanySearchResultListBean) adapter.getData().get(position);
                final String sfId = data.getSfId();
                final String lxr = data.getLxr();

                Intent intent = new Intent(getActivity(), CompanyDetailActivity.class);
                intent.putExtra("sfId", sfId);
                intent.putExtra("lxr", lxr);
                startActivity(intent);
            }
        });

        noDataView = getActivity().getLayoutInflater().inflate(R.layout.custom_empty_view, (ViewGroup) companySearchResultRecycler.getParent(), false);


    }

    private void initAdapter() {
        mAdapter = new CompanySearchResultListAdapter(R.layout.fragment_company_search_item, mDataList);

        //设置列表动画
//        mAdapter.openLoadAnimation(BaseQuickAdapter.SLIDEIN_BOTTOM);
        companySearchResultRecycler.setAdapter(mAdapter);


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

    public void requestData(final boolean isRefresh) {


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

            if (isRefresh) {
                page = 1;
                OkGo.<String>post(BiaoXunTongApi.URL_SUITRESULT)
                        .params("userId", id)
                        .params("token", token)
                        .params("provinceCode", mProvinceCode)
                        .params("qyIds", mqyIds)
                        .execute(new StringCallback() {
                            @Override
                            public void onSuccess(Response<String> response) {
                                final JSONObject object = JSON.parseObject(response.body());
                                final String status = object.getString("status");
                                final String message = object.getString("message");
                                final JSONArray array = object.getJSONArray("data");


                                if ("LIMIT_REACHED".equals(message)) {
                                    ToastUtil.shortBottonToast(getContext(), "你今天已达到最大查询次数，请明天再试！");
                                    mAdapter.setEmptyView(noDataView);
                                    companySearchResultRefresh.setEnableRefresh(false);
                                    companySearchResultRefresh.setEnabled(false);
                                } else if ("INVALID_TOKEN".equals(message)) {
                                    ToastUtil.shortToast(getContext(), "Token失效，请重新登录!");

                                    DatabaseManager.getInstance().getDao().deleteAll();
                                    AppSharePreferenceMgr.remove(getContext(), EventMessage.LOGIN_SUCCSS);
                                    EventBus.getDefault().post(new EventMessage(EventMessage.LOGIN_OUT));
                                    startActivity(new Intent(getActivity(), SignInActivity.class));
                                } else {
                                    if ("200".equals(status)) {
                                        if (array.size() > 0) {
                                            page = 2;
                                            setData(isRefresh, array);
                                        } else {
                                            if (mDataList != null) {
                                                mDataList.clear();
                                                mAdapter.notifyDataSetChanged();
                                            }
                                            //TODO 内容为空的处理
                                            mAdapter.setEmptyView(noDataView);
                                        }
                                    } else {
                                        ToastUtil.shortToast(getContext(), message);
                                    }

                                }
                            }
                        });
            } else {
                OkGo.<String>post(BiaoXunTongApi.URL_SUITRESULT)
                        .params("userId", id)
                        .params("token", token)
                        .params("provinceCode", mProvinceCode)
                        .params("qyIds", mqyIds)
                        .execute(new StringCallback() {
                            @Override
                            public void onSuccess(Response<String> response) {
                                final JSONObject object = JSON.parseObject(response.body());
                                final String status = object.getString("status");
                                final String message = object.getString("message");
                                final JSONArray array = object.getJSONArray("data");


                                if ("LIMIT_REACHED".equals(message)) {
                                    ToastUtil.shortBottonToast(getContext(), "你今天已达到最大查询次数，请明天再试！");
                                    mAdapter.setEmptyView(noDataView);
                                    companySearchResultRefresh.setEnableRefresh(false);
                                    companySearchResultRefresh.setEnabled(false);
                                } else if ("INVALID_TOKEN".equals(message)) {
                                    ToastUtil.shortToast(getContext(), "Token失效，请重新登录!");

                                    DatabaseManager.getInstance().getDao().deleteAll();
                                    AppSharePreferenceMgr.remove(getContext(), EventMessage.LOGIN_SUCCSS);
                                    EventBus.getDefault().post(new EventMessage(EventMessage.LOGIN_OUT));
                                    startActivity(new Intent(getActivity(), SignInActivity.class));
                                } else {
                                    if ("200".equals(status)) {
                                        if (array.size() > 0) {
                                            setData(isRefresh, array);
                                        } else {
                                            if (mDataList != null) {
                                                mDataList.clear();
                                                mAdapter.notifyDataSetChanged();
                                            }
                                            //TODO 内容为空的处理
                                            mAdapter.setEmptyView(noDataView);
                                        }
                                    } else {
                                        ToastUtil.shortToast(getContext(), message);
                                    }

                                }
                            }
                        });
            }

        }


    }

    private void setData(boolean isRefresh, JSONArray data) {
        final int size = data == null ? 0 : data.size();
        if (isRefresh) {
            mDataList.clear();
            for (int i = 0; i < data.size(); i++) {
                CompanySearchResultListBean bean = new CompanySearchResultListBean();
                JSONObject list = data.getJSONObject(i);
                bean.setQy(list.getString("qy"));
                bean.setLxr(list.getString("lxr"));
                bean.setEntrySign(list.getString("entrySign"));
                bean.setSfId(list.getString("sfId"));
                bean.setProvinceCode(mProvinceCode);
                if ("1".equals(mShowSign)) {
                    bean.setShowSign("1");
                } else {
                    bean.setShowSign("0");
                }
                mDataList.add(bean);
            }
            companySearchResultRefresh.finishRefresh(0, true);
        } else {
            page++;
            if (size > 0) {
                for (int i = 0; i < data.size(); i++) {
                    CompanySearchResultListBean bean = new CompanySearchResultListBean();
                    JSONObject list = data.getJSONObject(i);
                    bean.setQy(list.getString("qy"));
                    bean.setLxr(list.getString("lxr"));
                    bean.setEntrySign(list.getString("entrySign"));
                    bean.setSfId(list.getString("sfId"));
                    bean.setProvinceCode(mProvinceCode);
                    if ("1".equals(mShowSign)) {
                        bean.setShowSign("1");
                    } else {
                        bean.setShowSign("0");
                    }
                    mDataList.add(bean);
                }
            }
            companySearchResultRefresh.finishLoadmore(0, true);
        }

        companySearchResultRefresh.setLoadmoreFinished(true);
        mAdapter.notifyDataSetChanged();


        loadingStatus.showContent();

    }
}
