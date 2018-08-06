package com.lubanjianye.biaoxuntong.ui.main.query.detail;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.LinearLayout;

import com.alibaba.android.arouter.launcher.ARouter;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.classic.common.MultipleStatusView;
import com.lubanjianye.biaoxuntong.R;
import com.lubanjianye.biaoxuntong.app.BiaoXunTongApi;
import com.lubanjianye.biaoxuntong.base.BaseFragment1;
import com.lubanjianye.biaoxuntong.bean.CompanyQyzzListBean;
import com.lubanjianye.biaoxuntong.bean.CompanyRyzzListBean;
import com.lubanjianye.biaoxuntong.bean.CompanySgyjListBean;
import com.lubanjianye.biaoxuntong.database.DatabaseManager;
import com.lubanjianye.biaoxuntong.database.UserProfile;
import com.lubanjianye.biaoxuntong.ui.view.loadmore.CustomLoadMoreView;
import com.lubanjianye.biaoxuntong.util.toast.ToastUtil;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;

import java.util.ArrayList;
import java.util.List;


public class CompanyDetailFragment extends BaseFragment1 implements View.OnClickListener {

    private LinearLayout llIvBack = null;
    private AppCompatTextView mainBarName = null;
    private AppCompatTextView tvMainTitle = null;
    private AppCompatTextView tvFaren = null;
    private AppCompatTextView tvZcData = null;
    private AppCompatTextView tvZcType = null;
    private AppCompatTextView tvJyArea = null;
    private AppCompatTextView tvPuNum = null;
    private AppCompatTextView tvLxr = null;
    private MultipleStatusView companyDetailStatusView = null;
    private LinearLayout llQybg = null;

    private RecyclerView rlvQyzz = null;
    private RecyclerView rlvRyzz = null;
    private RecyclerView rlvQyyj = null;

    private AppCompatTextView tvQyzzTip = null;
    private AppCompatTextView tvRyzzTip = null;
    private AppCompatTextView tvQyyjTip = null;

    private LinearLayout llMoreQyzz = null;
    private LinearLayout llMoreRyzz = null;
    private LinearLayout llMoreQyyj = null;

    private AppCompatTextView qyzzCount = null;
    private AppCompatTextView ryzzCount = null;
    private AppCompatTextView qyyjCount = null;


    private CompanyQyzzListAdapter mAdapter;
    private ArrayList<CompanyQyzzListBean> mDataList = new ArrayList<>();

    private CompanyRyzzListAdapter mRyzzAdapter = null;
    private ArrayList<CompanyRyzzListBean> mRyzzDataList = new ArrayList<>();


    private CompanySgyjListAdapter mQyyjAdapter = null;
    private ArrayList<CompanySgyjListBean> mQyyjDataList = new ArrayList<>();

    private static final String ARG_SFID = "ARG_SFID";
    private static final String ARG_LXR = "ARG_LXR";
    private String sfId = "";
    private String lxr = "";
    private String companyName = "";

    @Override
    public Object setLayout() {
        return R.layout.fragment_company_detail;
    }

    public static CompanyDetailFragment create(@NonNull String sfid, String lxr) {
        final Bundle args = new Bundle();
        args.putString(ARG_SFID, sfid);
        args.putString(ARG_LXR, lxr);
        final CompanyDetailFragment fragment = new CompanyDetailFragment();
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final Bundle args = getArguments();
        if (args != null) {
            sfId = args.getString(ARG_SFID);
            lxr = args.getString(ARG_LXR);
        }
    }


    private void initAdapter() {

        mAdapter = new CompanyQyzzListAdapter(R.layout.fragment_company_qyzz, mDataList);
        mAdapter.setLoadMoreView(new CustomLoadMoreView());
        rlvQyzz.setAdapter(mAdapter);

        mRyzzAdapter = new CompanyRyzzListAdapter(R.layout.fragment_company_ryzz, mRyzzDataList);
        mRyzzAdapter.setLoadMoreView(new CustomLoadMoreView());
        rlvRyzz.setAdapter(mRyzzAdapter);

        mQyyjAdapter = new CompanySgyjListAdapter(R.layout.fragment_company_sgyj_list_item, mQyyjDataList);
        mAdapter.setLoadMoreView(new CustomLoadMoreView());
        rlvQyyj.setAdapter(mQyyjAdapter);


    }


    private void initRecyclerView() {
        LinearLayoutManager layoutManager1 = new LinearLayoutManager(getContext());
        layoutManager1.setSmoothScrollbarEnabled(true);
        layoutManager1.setAutoMeasureEnabled(true);

        LinearLayoutManager layoutManager2 = new LinearLayoutManager(getContext());
        layoutManager2.setSmoothScrollbarEnabled(true);
        layoutManager2.setAutoMeasureEnabled(true);

        LinearLayoutManager layoutManager3 = new LinearLayoutManager(getContext());
        layoutManager3.setSmoothScrollbarEnabled(true);
        layoutManager3.setAutoMeasureEnabled(true);
        llQybg = getView().findViewById(R.id.ll_qibg);
        rlvQyzz.setLayoutManager(layoutManager1);
        rlvRyzz.setLayoutManager(layoutManager2);
        rlvQyyj.setLayoutManager(layoutManager3);

        rlvQyzz.setHasFixedSize(true);
        rlvQyzz.setNestedScrollingEnabled(false);
        rlvRyzz.setHasFixedSize(true);
        rlvRyzz.setNestedScrollingEnabled(false);
        rlvQyyj.setHasFixedSize(true);
        rlvQyyj.setNestedScrollingEnabled(false);
        llQybg.setOnClickListener(this);


    }

    @Override
    public void initView() {
        llIvBack = getView().findViewById(R.id.ll_iv_back);
        mainBarName = getView().findViewById(R.id.main_bar_name);
        tvMainTitle = getView().findViewById(R.id.tv_main_title);
        tvFaren = getView().findViewById(R.id.tv_faren);
        tvZcData = getView().findViewById(R.id.tv_zc_data);
        tvZcType = getView().findViewById(R.id.tv_zc_type);
        tvJyArea = getView().findViewById(R.id.tv_jy_area);
        tvPuNum = getView().findViewById(R.id.tv_pu_num);
        tvLxr = getView().findViewById(R.id.tv_lxr);
        companyDetailStatusView = getView().findViewById(R.id.company_detail_status_view);

        rlvQyzz = getView().findViewById(R.id.rlv_qyzz);
        rlvRyzz = getView().findViewById(R.id.rlv_ryzz);
        rlvQyyj = getView().findViewById(R.id.rlv_qyyj);

        tvQyzzTip = getView().findViewById(R.id.tv_qyzz_tip);
        tvRyzzTip = getView().findViewById(R.id.tv_ryzz_tip);
        tvQyyjTip = getView().findViewById(R.id.tv_qyyj_tip);

        llMoreQyzz = getView().findViewById(R.id.ll_more_qyzz);
        llMoreRyzz = getView().findViewById(R.id.ll_more_ryzz);
        llMoreQyyj = getView().findViewById(R.id.ll_more_qyyj);

        qyzzCount = getView().findViewById(R.id.qyzz_count);
        ryzzCount = getView().findViewById(R.id.ryzz_count);
        qyyjCount = getView().findViewById(R.id.qyyj_count);

        llIvBack.setOnClickListener(this);
        llMoreQyzz.setOnClickListener(this);
        llMoreRyzz.setOnClickListener(this);
        llMoreQyyj.setOnClickListener(this);

    }

    @Override
    public void initData() {
        llIvBack.setVisibility(View.VISIBLE);
        mainBarName.setText("企业详情");
        companyDetailStatusView.setOnRetryClickListener(mRetryClickListener);
    }

    @Override
    public void initEvent() {
        initRecyclerView();
        initAdapter();
        requestData();
        requestQyzzData();
        requestRyzzData();
        requestQyyjData();
    }

    //点击重试
    final View.OnClickListener mRetryClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            requestData();
        }
    };

    private long id = 0;
    private String token = "";

    public void requestData() {

        companyDetailStatusView.showLoading();
        List<UserProfile> users = DatabaseManager.getInstance().getDao().loadAll();

        for (int i = 0; i < users.size(); i++) {
            id = users.get(0).getId();
            token = users.get(0).getToken();
        }


        OkGo.<String>post(BiaoXunTongApi.URL_SUITRESULTDETAIl + sfId)
                .params("token", token)
                .params("userId", id)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        final JSONObject object = JSON.parseObject(response.body());
                        String status = object.getString("status");
                        if ("200".equals(status)) {
                            companyDetailStatusView.showContent();
                            final JSONObject data = object.getJSONObject("data");
                            String qy = data.getString("qy");
                            companyName = qy;
                            if (!TextUtils.isEmpty(qy)) {
                                tvMainTitle.setText(qy);
                            } else {
                                tvMainTitle.setText("/");
                            }
                            String fr = data.getString("fr");
                            if (!TextUtils.isEmpty(fr)) {
                                tvFaren.setText(fr);
                            } else {
                                tvFaren.setText("/");
                            }
                            String zcd = data.getString("zcd");
                            if (!TextUtils.isEmpty(zcd)) {
                                tvZcData.setText(zcd);
                            } else {
                                tvZcData.setText("/");
                            }
                            String zclx = data.getString("zclx");
                            if (!TextUtils.isEmpty(zclx)) {
                                tvZcType.setText(zclx);
                            } else {
                                tvZcType.setText("/");
                            }
                            String dz = data.getString("dz");
                            if (!TextUtils.isEmpty(dz)) {
                                tvJyArea.setText(dz);
                            } else {
                                tvJyArea.setText("/");
                            }
                            String tyshxydm = data.getString("tyshxydm");
                            if (!TextUtils.isEmpty(tyshxydm)) {
                                tvPuNum.setText(tyshxydm);
                            } else {
                                tvPuNum.setText("/");
                            }

                            if (!TextUtils.isEmpty(lxr)) {
                                tvLxr.setText(lxr);
                            } else {
                                tvLxr.setText("/");
                            }

                        }
                    }
                });

    }

    @Override
    public void onClick(View view) {

        Intent intent;
        switch (view.getId()) {
            case R.id.ll_iv_back:
                getActivity().onBackPressed();
                break;
            case R.id.ll_more_qyzz:
                intent = new Intent(getActivity(), CompanyQyzzListActivity.class);
                intent.putExtra("sfId", sfId);
                startActivity(intent);
                break;
            case R.id.ll_more_ryzz:
                intent = new Intent(getActivity(), CompanyRyzzListActivity.class);
                intent.putExtra("sfId", sfId);
                startActivity(intent);
                break;
            case R.id.ll_more_qyyj:
                intent = new Intent(getActivity(), CompanySgyjListActivity.class);
                intent.putExtra("sfId", sfId);
                startActivity(intent);
                break;
            case R.id.ll_qibg:
                ARouter.getInstance().build("/com/QybgActivity").withString("sfId",sfId).withString("companyName",companyName).navigation();
                break;
            default:
                break;
        }
    }


    public void requestQyzzData() {

        List<UserProfile> users = DatabaseManager.getInstance().getDao().loadAll();
        long id = 0;
        for (int i = 0; i < users.size(); i++) {
            id = users.get(0).getId();
            token = users.get(0).getToken();
        }

        OkGo.<String>post(BiaoXunTongApi.URL_COMPANYQYZZ + sfId)
                .params("userId", id)
                .params("token", token)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        final JSONObject object = JSON.parseObject(response.body());
                        final String status = object.getString("status");
                        final String message = object.getString("message");

                        if ("200".equals(status)) {
                            final JSONArray array = object.getJSONArray("data");
                            if (array.size() > 0) {
                                if (tvQyzzTip != null) {
                                    tvQyzzTip.setVisibility(View.GONE);
                                }

                                setQyzzData(array);
                                companyDetailStatusView.showContent();
                            } else {
                                if (mDataList != null) {
                                    mDataList.clear();
                                    mAdapter.notifyDataSetChanged();
                                }
                                //TODO 内容为空的处理
                                if (tvQyzzTip != null) {
                                    tvQyzzTip.setVisibility(View.VISIBLE);
                                }
                            }
                        } else {
                            ToastUtil.shortToast(getActivity(), message);
                        }

                    }
                });


    }

    public void requestRyzzData() {
        List<UserProfile> users = DatabaseManager.getInstance().getDao().loadAll();
        long id = 0;
        String token = "";
        for (int i = 0; i < users.size(); i++) {
            id = users.get(0).getId();
            token = users.get(0).getToken();
        }

        OkGo.<String>post(BiaoXunTongApi.URL_COMPANYRYZZ + sfId)
                .params("userId", id)
                .params("token", token)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        final JSONObject object = JSON.parseObject(response.body());
                        final String status = object.getString("status");
                        final String message = object.getString("message");

                        if ("200".equals(status)) {
                            final JSONArray array = object.getJSONArray("data");
                            if (array.size() > 0) {
                                if (tvRyzzTip != null) {
                                    tvRyzzTip.setVisibility(View.GONE);
                                }
                                setRyzzData(array);
                                companyDetailStatusView.showContent();
                            } else {
                                if (mRyzzDataList != null) {
                                    mRyzzDataList.clear();
                                    mRyzzAdapter.notifyDataSetChanged();
                                }
                                //TODO 内容为空的处理
                                if (tvRyzzTip != null) {
                                    tvRyzzTip.setVisibility(View.VISIBLE);
                                }
                            }
                        } else {
                            ToastUtil.shortToast(getActivity(), message);
                        }


                    }
                });

    }

    public void requestQyyjData() {


        List<UserProfile> users = DatabaseManager.getInstance().getDao().loadAll();
        long id = 0;
        for (int i = 0; i < users.size(); i++) {
            id = users.get(0).getId();
            token = users.get(0).getToken();
        }

        OkGo.<String>post(BiaoXunTongApi.URL_COMPANYSGYJ + sfId)
                .params("userId", id)
                .params("token", token)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        final JSONObject object = JSON.parseObject(response.body());
                        final String status = object.getString("status");
                        final String message = object.getString("message");


                        if ("200".equals(status)) {
                            final JSONArray array = object.getJSONArray("data");
                            if (array.size() > 0) {
                                if (tvQyyjTip != null) {
                                    tvQyyjTip.setVisibility(View.GONE);
                                }
                                setQyyjData(array);
                                companyDetailStatusView.showContent();
                            } else {
                                if (mQyyjDataList != null) {
                                    mQyyjDataList.clear();
                                    mRyzzAdapter.notifyDataSetChanged();
                                }
                                //TODO 内容为空的处理
                                if (tvQyyjTip != null) {
                                    tvQyyjTip.setVisibility(View.VISIBLE);
                                }
                            }
                        } else {
                            ToastUtil.shortToast(getActivity(), message);
                        }


                    }
                });

    }


    private void setQyzzData(JSONArray data) {
        mDataList.clear();

        if (data.size() >= 5) {
            qyzzCount.setText(data.size() + "");
            if (llMoreQyzz != null) {
                llMoreQyzz.setVisibility(View.VISIBLE);
            }
            for (int i = 0; i < 5; i++) {
                CompanyQyzzListBean bean = new CompanyQyzzListBean();
                JSONObject list = data.getJSONObject(i);
                bean.setLx(list.getString("lx"));
                bean.setZzmc(list.getString("zzmc"));
                mDataList.add(bean);
            }

            mAdapter.notifyDataSetChanged();
            mAdapter.loadMoreEnd();
        } else {
            if (llMoreQyzz != null) {
                llMoreQyzz.setVisibility(View.GONE);
            }
            for (int i = 0; i < data.size(); i++) {
                CompanyQyzzListBean bean = new CompanyQyzzListBean();
                JSONObject list = data.getJSONObject(i);
                bean.setLx(list.getString("lx"));
                bean.setZzmc(list.getString("zzmc"));
                mDataList.add(bean);
            }

            mAdapter.notifyDataSetChanged();
            mAdapter.loadMoreEnd();
        }

    }

    private void setRyzzData(JSONArray data) {
        mRyzzDataList.clear();

        if (data.size() >= 5) {

            ryzzCount.setText(data.size() + "");

            if (llMoreRyzz != null) {
                llMoreRyzz.setVisibility(View.VISIBLE);
            }
            for (int i = 0; i < 5; i++) {
                CompanyRyzzListBean bean = new CompanyRyzzListBean();
                JSONObject list = data.getJSONObject(i);
                bean.setRy(list.getString("ry"));
                bean.setZgMcdj(list.getString("zgMcdj"));
                bean.setZgZy(list.getString("zgZy"));
                mRyzzDataList.add(bean);
            }

            mRyzzAdapter.notifyDataSetChanged();
            mRyzzAdapter.loadMoreEnd();
        } else {
            if (llMoreRyzz != null) {
                llMoreRyzz.setVisibility(View.GONE);
            }
            for (int i = 0; i < data.size(); i++) {
                CompanyRyzzListBean bean = new CompanyRyzzListBean();
                JSONObject list = data.getJSONObject(i);
                bean.setRy(list.getString("ry"));
                bean.setZgMcdj(list.getString("zgMcdj"));
                bean.setZgZy(list.getString("zgZy"));
                mRyzzDataList.add(bean);
            }

            mRyzzAdapter.notifyDataSetChanged();
            mRyzzAdapter.loadMoreEnd();
        }


    }

    private void setQyyjData(JSONArray data) {
        mQyyjDataList.clear();

        if (data.size() >= 5) {
            qyyjCount.setText(data.size() + "");

            if (llMoreQyyj != null) {
                llMoreQyyj.setVisibility(View.VISIBLE);
            }
            for (int i = 0; i < 5; i++) {
                CompanySgyjListBean bean = new CompanySgyjListBean();
                JSONObject list = data.getJSONObject(i);
                bean.setXmmc(list.getString("xmmc"));
                bean.setZbsj(list.getString("zbsj"));
                bean.setXmfzr(list.getString("xmfzr"));

                String zbje = list.getString("zbje");
                if ("0.0".equals(zbje)) {
                    bean.setZbje("暂无");
                } else {
                    bean.setZbje(list.getString("zbje") + "万元");
                }
                mQyyjDataList.add(bean);
            }

            mQyyjAdapter.notifyDataSetChanged();
            mQyyjAdapter.loadMoreEnd();
        } else {
            if (llMoreQyyj != null) {
                llMoreQyyj.setVisibility(View.GONE);
            }
            for (int i = 0; i < data.size(); i++) {
                CompanySgyjListBean bean = new CompanySgyjListBean();
                JSONObject list = data.getJSONObject(i);
                bean.setXmmc(list.getString("xmmc"));
                bean.setZbsj(list.getString("zbsj"));
                bean.setXmfzr(list.getString("xmfzr"));

                String zbje = list.getString("zbje");
                if ("0.0".equals(zbje)) {
                    bean.setZbje("暂无");
                } else {
                    bean.setZbje(list.getString("zbje"));
                }
                mQyyjDataList.add(bean);
            }

            mQyyjAdapter.notifyDataSetChanged();
            mQyyjAdapter.loadMoreEnd();
        }


    }


}
