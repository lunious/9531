package com.lubanjianye.biaoxuntong.ui.main.user.company;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.LinearLayout;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.classic.common.MultipleStatusView;
import com.lubanjianye.biaoxuntong.R;
import com.lubanjianye.biaoxuntong.base.BaseFragment1;
import com.lubanjianye.biaoxuntong.bean.CompanySgyjListBean;
import com.lubanjianye.biaoxuntong.bean.MyCompanyQyzzAllListBean;
import com.lubanjianye.biaoxuntong.bean.MyCompanyRyzzAllListBean;
import com.lubanjianye.biaoxuntong.database.DatabaseManager;
import com.lubanjianye.biaoxuntong.database.UserProfile;
import com.lubanjianye.biaoxuntong.app.BiaoXunTongApi;
import com.lubanjianye.biaoxuntong.ui.main.query.detail.CompanySgyjListAdapter;
import com.lubanjianye.biaoxuntong.util.dialog.PromptButton;
import com.lubanjianye.biaoxuntong.util.dialog.PromptButtonListener;
import com.lubanjianye.biaoxuntong.util.dialog.PromptDialog;
import com.lubanjianye.biaoxuntong.util.netStatus.NetUtil;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.cache.CacheMode;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;

import java.util.ArrayList;
import java.util.List;


public class MyCompanyFragment extends BaseFragment1 implements View.OnClickListener {

    private LinearLayout llIvBack = null;
    private AppCompatTextView mainBarName = null;
    private LinearLayout llIbAdd = null;

    private AppCompatTextView tvMyCompany = null;
    private LinearLayout llCompanyName = null;
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

    private MultipleStatusView loadingStatus = null;


    private PromptDialog promptDialog = null;

    private MyCompanyQyzzAllListAdapter mQyzzAdapter = null;
    private ArrayList<MyCompanyQyzzAllListBean> mQyzzDataList = new ArrayList<>();

    private MyCompanyRyzzAllListAdapter mRyzzAdapter = null;
    private ArrayList<MyCompanyRyzzAllListBean> mRyzzDataList = new ArrayList<>();


    private CompanySgyjListAdapter mQyyjAdapter = null;
    private ArrayList<CompanySgyjListBean> mQyyjDataList = new ArrayList<>();


    private int page = 1;

    private long id = 0;
    private String nickName = "";
    private String mobile = "";
    private String token = "";
    private String comid = "";
    private String imageUrl = "";
    private String companyName = "";


    private List<String> zzbm = new ArrayList<String>();
    private List<String> zgzy_code = new ArrayList<String>();
    private List<String> ryname = new ArrayList<String>();
    private String zzbm_id = "";
    private String zgzy_id = "";
    private String name = "";

    private boolean isQyzzCache = false;
    private boolean isRyzzCache = false;


    private List<String> zy_code = new ArrayList<String>();
    private String zy_id = "";

    @Override
    public Object setLayout() {
        return R.layout.fragment_my_company;
    }

    @Override
    public void initView() {
        llIvBack = getView().findViewById(R.id.ll_iv_back);
        mainBarName = getView().findViewById(R.id.main_bar_name);
        llIbAdd = getView().findViewById(R.id.ll_ib_add);
        tvMyCompany = getView().findViewById(R.id.tv_my_company);
        llCompanyName = getView().findViewById(R.id.ll_company_name);
        rlvQyzz = getView().findViewById(R.id.rlv_qyzz);
        rlvRyzz = getView().findViewById(R.id.rlv_ryzz);
        rlvQyyj = getView().findViewById(R.id.rlv_qyyj);
        tvRyzzTip = getView().findViewById(R.id.tv_ryzz_tip);
        tvQyzzTip = getView().findViewById(R.id.tv_qyzz_tip);
        tvQyyjTip = getView().findViewById(R.id.tv_qyyj_tip);
        llMoreQyzz = getView().findViewById(R.id.ll_more_qyzz);
        llMoreRyzz = getView().findViewById(R.id.ll_more_ryzz);
        llMoreQyyj = getView().findViewById(R.id.ll_more_qyyj);
        qyzzCount = getView().findViewById(R.id.qyzz_count);
        ryzzCount = getView().findViewById(R.id.ryzz_count);
        qyyjCount = getView().findViewById(R.id.qyyj_count);
        loadingStatus = getView().findViewById(R.id.company_status_view);
        llIvBack.setOnClickListener(this);
        llMoreQyzz.setOnClickListener(this);
        llMoreRyzz.setOnClickListener(this);
        llMoreQyyj.setOnClickListener(this);
        llIbAdd.setOnClickListener(this);

    }

    @Override
    public void initData() {
        llIbAdd.setVisibility(View.VISIBLE);
        llIvBack.setVisibility(View.VISIBLE);
        mainBarName.setText("我的资质");
        //创建对象
        promptDialog = new PromptDialog(getActivity());
        //设置自定义属性
        promptDialog.getDefaultBuilder().touchAble(true).round(3).loadingDuration(3000);
        List<UserProfile> users = DatabaseManager.getInstance().getDao().loadAll();

        for (int i = 0; i < users.size(); i++) {
            id = users.get(0).getId();
            nickName = users.get(0).getNickName();
            mobile = users.get(0).getMobile();
            token = users.get(0).getToken();
            comid = users.get(0).getComid();
            imageUrl = users.get(0).getImageUrl();
            companyName = users.get(0).getCompanyName();
        }


    }

    @Override
    public void initEvent() {
        initRecyclerView();
        initAdapter();

        if (!NetUtil.isNetworkConnected(getActivity())){
            loadingStatus.showNoNetwork();
        }else {
            loadingStatus.showLoading();
            getCompanyName();
        }

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ll_iv_back:
                getActivity().onBackPressed();
                break;
            case R.id.ll_ib_add:
                PromptButton cancle = new PromptButton("取消", null);
                cancle.setTextColor(getResources().getColor(R.color.main_theme_color));
                cancle.setTextSize(16);

                PromptButton xzryzz = new PromptButton("新增人员资质", new PromptButtonListener() {
                    @Override
                    public void onClick(PromptButton promptButton) {
//                promptButton.setTextColor(Color.RED);
                        //新增人员资质界面
                        startActivity(new Intent(getActivity(), AddRyzzActivity.class));
                    }
                });
                xzryzz.setTextColor(Color.parseColor("#6d6d6d"));
                xzryzz.setTextSize(16);
                PromptButton xzqyzz = new PromptButton("新增企业资质", new PromptButtonListener() {
                    @Override
                    public void onClick(PromptButton promptButton) {
//                promptButton.setTextColor(Color.RED);
                        //新增企业资质界面
                        startActivity(new Intent(getActivity(), AddQyzzActivity.class));
                    }
                });
                xzqyzz.setTextColor(Color.parseColor("#6d6d6d"));
                xzqyzz.setTextSize(16);
                //跳转到选择页面
                promptDialog.showAlertSheet("", true, cancle, xzryzz, xzqyzz);
                break;
            case R.id.ll_more_qyzz:
                startActivity(new Intent(getActivity(), MyCompanyQyzzAllListActivity.class));
                break;
            case R.id.ll_more_ryzz:
                startActivity(new Intent(getActivity(), MyCompanyRyzzAllListActivity.class));
                break;
            case R.id.ll_more_qyyj:
                Intent intent = new Intent(getActivity(), MyCompanyQyyjAllListActivity.class);
                intent.putExtra("sfId", sfId);
                startActivity(intent);
                break;
            default:
                break;

        }
    }

    private void getCompanyName() {


        if (!TextUtils.isEmpty(companyName)) {
            tvMyCompany.setText(companyName);
        } else {
            tvMyCompany.setText("");
        }

        requestQyzzData();
        requestRyzzData();
        requestQyyjData();

    }

    private void initAdapter() {
        mQyzzAdapter = new MyCompanyQyzzAllListAdapter(R.layout.fragment_company_qyzz, mQyzzDataList);
        rlvQyzz.setAdapter(mQyzzAdapter);

        mRyzzAdapter = new MyCompanyRyzzAllListAdapter(R.layout.fragment_company_ryzz, mRyzzDataList);
        rlvRyzz.setAdapter(mRyzzAdapter);

        mQyyjAdapter = new CompanySgyjListAdapter(R.layout.fragment_company_sgyj_list_item, mQyyjDataList);
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

        rlvQyzz.setLayoutManager(layoutManager1);
        rlvRyzz.setLayoutManager(layoutManager2);
        rlvQyyj.setLayoutManager(layoutManager3);

        rlvQyzz.setHasFixedSize(true);
        rlvQyzz.setNestedScrollingEnabled(false);
        rlvRyzz.setHasFixedSize(true);
        rlvRyzz.setNestedScrollingEnabled(false);
        rlvQyyj.setHasFixedSize(true);
        rlvQyyj.setNestedScrollingEnabled(false);


    }


    public void requestQyzzData() {

        List<UserProfile> users = DatabaseManager.getInstance().getDao().loadAll();
        long id = 0;
        for (int i = 0; i < users.size(); i++) {
            id = users.get(0).getId();
        }

        OkGo.<String>post(BiaoXunTongApi.URL_GETALLCOMPANYQYZZ)
                .params("userId", id)
                .params("type", 0)
                .params("size", 5)
                .params("page", page)
                .cacheKey("my_qyzz_cache" + id)
                .cacheMode(CacheMode.FIRST_CACHE_THEN_REQUEST)
                .cacheTime(3600 * 48000)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {

                        final JSONObject object = JSON.parseObject(response.body());
                        String status = object.getString("status");

                        if ("200".equals(status)) {

                            final JSONArray array = object.getJSONObject("data").getJSONArray("list");
                            final int count = object.getJSONObject("data").getInteger("count");
                            for (int i = 0; i < array.size(); i++) {
                                final JSONObject data = array.getJSONObject(i);
                                zy_code.add(data.getString("zy_code"));
                            }

                            if (array.size() >= 5) {
                                if (llMoreQyzz != null) {
                                    llMoreQyzz.setVisibility(View.VISIBLE);
                                }
                                qyzzCount.setText(count + "");
                            } else {
                                if (llMoreQyzz != null) {
                                    llMoreQyzz.setVisibility(View.GONE);
                                }

                            }

                            if (array.size() > 0) {
                                if (tvQyzzTip != null) {
                                    tvQyzzTip.setVisibility(View.GONE);
                                }

                                if (mQyzzDataList.size() > 0) {
                                    mQyzzDataList.clear();
                                }

                                for (int i = 0; i < array.size(); i++) {
                                    MyCompanyQyzzAllListBean bean = new MyCompanyQyzzAllListBean();
                                    JSONObject list = array.getJSONObject(i);
                                    bean.setLx_name(list.getString("lx_name"));
                                    bean.setDl_name(list.getString("dl_name"));
                                    bean.setXl_name(list.getString("xl_name"));
                                    bean.setZy_name(list.getString("zy_name"));
                                    bean.setDj(list.getString("dj"));
                                    bean.setDq(list.getString("dq"));
                                    mQyzzDataList.add(bean);
                                }

                                mQyzzAdapter.notifyDataSetChanged();

                            } else {
                                if (tvQyzzTip != null) {
                                    tvQyzzTip.setVisibility(View.VISIBLE);
                                }

                            }

                        } else {
                        }

                    }

                    @Override
                    public void onCacheSuccess(Response<String> response) {

                        if (!isQyzzCache) {
                            final JSONObject object = JSON.parseObject(response.body());
                            String status = object.getString("status");

                            if ("200".equals(status)) {

                                final JSONArray array = object.getJSONObject("data").getJSONArray("list");
                                final int count = object.getJSONObject("data").getInteger("count");
                                for (int i = 0; i < array.size(); i++) {
                                    final JSONObject data = array.getJSONObject(i);
                                    zy_code.add(data.getString("zy_code"));
                                }

                                if (array.size() >= 5) {
                                    if (llMoreQyzz != null) {
                                        llMoreQyzz.setVisibility(View.VISIBLE);
                                    }
                                    qyzzCount.setText(count + "");
                                } else {
                                    if (llMoreQyzz != null) {
                                        llMoreQyzz.setVisibility(View.GONE);
                                    }

                                }

                                if (array.size() > 0) {
                                    if (tvQyzzTip != null) {
                                        tvQyzzTip.setVisibility(View.GONE);
                                    }

                                    for (int i = 0; i < array.size(); i++) {
                                        MyCompanyQyzzAllListBean bean = new MyCompanyQyzzAllListBean();
                                        JSONObject list = array.getJSONObject(i);
                                        bean.setLx_name(list.getString("lx_name"));
                                        bean.setDl_name(list.getString("dl_name"));
                                        bean.setXl_name(list.getString("xl_name"));
                                        bean.setZy_name(list.getString("zy_name"));
                                        bean.setDj(list.getString("dj"));
                                        bean.setDq(list.getString("dq"));
                                        mQyzzDataList.add(bean);
                                    }

                                    mQyzzAdapter.notifyDataSetChanged();

                                } else {
                                    if (tvQyzzTip != null) {
                                        tvQyzzTip.setVisibility(View.VISIBLE);
                                    }

                                }


                            } else {
                            }
                            isQyzzCache = true;
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

        OkGo.<String>post(BiaoXunTongApi.URL_GETALLCOMPANYRYZZ)
                .params("userId", id)
                .params("type", 0)
                .params("size", 5)
                .params("page", page)
                .cacheKey("my_ryzz_cache" + id)
                .cacheMode(CacheMode.FIRST_CACHE_THEN_REQUEST)
                .cacheTime(3600 * 48000)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {

                        final JSONObject object = JSON.parseObject(response.body());
                        String status = object.getString("status");

                        if ("200".equals(status)) {

                            final JSONArray array = object.getJSONObject("data").getJSONArray("list");
                            final int count = object.getJSONObject("data").getInteger("count");

                            for (int i = 0; i < array.size(); i++) {
                                final JSONObject data = array.getJSONObject(i);
                                zzbm.add(data.getString("zzbm"));
                                zgzy_code.add(data.getString("zgzy_code"));
                                ryname.add(data.getString("ryname"));
                            }

                            if (array.size() >= 5) {
                                if (llMoreRyzz != null) {
                                    llMoreRyzz.setVisibility(View.VISIBLE);
                                }

                                ryzzCount.setText(count + "");

                            } else {
                                if (llMoreRyzz != null) {
                                    llMoreRyzz.setVisibility(View.GONE);
                                }

                            }


                            if (array.size() > 0) {

                                if (tvRyzzTip != null) {
                                    tvRyzzTip.setVisibility(View.GONE);
                                }

                                if (mRyzzDataList.size() > 0) {
                                    mRyzzDataList.clear();
                                }

                                for (int i = 0; i < array.size(); i++) {
                                    MyCompanyRyzzAllListBean bean = new MyCompanyRyzzAllListBean();
                                    JSONObject list = array.getJSONObject(i);
                                    bean.setLx_name(list.getString("lx_name"));
                                    bean.setRyname(list.getString("ryname"));
                                    bean.setZg_mcdj(list.getString("zg_mcdj"));
                                    bean.setZg_name(list.getString("zg_name"));
                                    bean.setZgzy(list.getString("zgzy"));
                                    mRyzzDataList.add(bean);
                                }

                                mRyzzAdapter.notifyDataSetChanged();
                            } else {
                                if (tvRyzzTip != null) {
                                    tvRyzzTip.setVisibility(View.VISIBLE);
                                }

                            }
                        } else {
                        }
                    }

                    @Override
                    public void onCacheSuccess(Response<String> response) {

                        if (!isRyzzCache) {
                            final JSONObject object = JSON.parseObject(response.body());
                            String status = object.getString("status");

                            if ("200".equals(status)) {

                                final JSONArray array = object.getJSONObject("data").getJSONArray("list");
                                final int count = object.getJSONObject("data").getInteger("count");

                                for (int i = 0; i < array.size(); i++) {
                                    final JSONObject data = array.getJSONObject(i);
                                    zzbm.add(data.getString("zzbm"));
                                    zgzy_code.add(data.getString("zgzy_code"));
                                    ryname.add(data.getString("ryname"));
                                }

                                if (array.size() >= 5) {
                                    if (llMoreRyzz != null) {
                                        llMoreRyzz.setVisibility(View.VISIBLE);
                                    }

                                    ryzzCount.setText(count + "");

                                } else {
                                    if (llMoreRyzz != null) {
                                        llMoreRyzz.setVisibility(View.GONE);
                                    }

                                }


                                if (array.size() > 0) {

                                    if (tvRyzzTip != null) {
                                        tvRyzzTip.setVisibility(View.GONE);
                                    }
                                    for (int i = 0; i < array.size(); i++) {
                                        MyCompanyRyzzAllListBean bean = new MyCompanyRyzzAllListBean();
                                        JSONObject list = array.getJSONObject(i);
                                        bean.setLx_name(list.getString("lx_name"));
                                        bean.setRyname(list.getString("ryname"));
                                        bean.setZg_mcdj(list.getString("zg_mcdj"));
                                        bean.setZg_name(list.getString("zg_name"));
                                        bean.setZgzy(list.getString("zgzy"));
                                        mRyzzDataList.add(bean);
                                    }

                                    mRyzzAdapter.notifyDataSetChanged();
                                } else {
                                    if (tvRyzzTip != null) {
                                        tvRyzzTip.setVisibility(View.VISIBLE);
                                    }

                                }
                            } else {
                            }
                            isRyzzCache = true;
                        }


                    }
                });

    }


    private String qyId = "";
    private String sfId = "";

    public void requestQyyjData() {

        //得到绑定公司的qyId

        String companyname = tvMyCompany.getText().toString().trim();

        OkGo.<String>post(BiaoXunTongApi.URL_GETSUITCOMPANY)
                .params("name", companyname)
                .params("userid", id)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {

                        final JSONArray data = JSON.parseObject(response.body()).getJSONArray("data");
                        if (data.size() > 0) {
                            //根据返回的id去查询公司名称
                            qyId = data.toString();

                            //得到绑定公司的sfId
                            OkGo.<String>post(BiaoXunTongApi.URL_SUITRESULT)
                                    .params("userId", id)
                                    .params("token", token)
                                    .params("provinceCode", "510000")
                                    .params("qyIds", qyId)
                                    .execute(new StringCallback() {
                                        @Override
                                        public void onSuccess(Response<String> response) {
                                            final JSONObject object = JSON.parseObject(response.body());
                                            final String status = object.getString("status");
                                            final String message = object.getString("message");
                                            final JSONArray array = object.getJSONArray("data");

                                            if ("200".equals(status)) {
                                                for (int i = 0; i < array.size(); i++) {
                                                    JSONObject data = (JSONObject) array.get(i);
                                                    sfId = data.getString("sfId");

                                                    OkGo.<String>post(BiaoXunTongApi.URL_COMPANYSGYJ + sfId)
                                                            .params("userId", id)
                                                            .params("token", token)
                                                            .execute(new StringCallback() {
                                                                @Override
                                                                public void onSuccess(Response<String> response) {
                                                                    final JSONObject object = JSON.parseObject(response.body());
                                                                    String status = object.getString("status");

                                                                    if ("200".equals(status)) {
                                                                        final JSONArray array = object.getJSONArray("data");

                                                                        if (array.size() > 0) {
                                                                            if (tvQyyjTip != null) {
                                                                                tvQyyjTip.setVisibility(View.GONE);
                                                                            }

                                                                            mQyyjDataList.clear();

                                                                            if (array.size() >= 5) {
                                                                                qyyjCount.setText(array.size() + "");
                                                                                if (llMoreQyyj != null) {
                                                                                    llMoreQyyj.setVisibility(View.VISIBLE);
                                                                                }
                                                                                for (int i = 0; i < 5; i++) {
                                                                                    CompanySgyjListBean bean = new CompanySgyjListBean();
                                                                                    JSONObject list = array.getJSONObject(i);
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
                                                                                    mQyyjAdapter.notifyDataSetChanged();
                                                                                }
                                                                            } else {
                                                                                if (llMoreQyyj != null) {
                                                                                    llMoreQyyj.setVisibility(View.GONE);
                                                                                }
                                                                                for (int i = 0; i < array.size(); i++) {
                                                                                    CompanySgyjListBean bean = new CompanySgyjListBean();
                                                                                    JSONObject list = array.getJSONObject(i);
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
                                                                                    mQyyjAdapter.notifyDataSetChanged();
                                                                                }
                                                                            }


                                                                        } else {
                                                                            //TODO 内容为空的处理
                                                                            if (tvQyyjTip != null) {
                                                                                tvQyyjTip.setVisibility(View.VISIBLE);
                                                                            }
                                                                            if (llMoreQyyj != null) {
                                                                                llMoreQyyj.setVisibility(View.GONE);
                                                                            }
                                                                        }
                                                                    }
                                                                }
                                                            });

                                                }

                                                loadingStatus.showContent();
                                            } else {

                                            }
                                        }
                                    });


                        }
                    }
                });

    }


    @Override
    public void onSupportInvisible() {
        super.onSupportInvisible();
        if (promptDialog != null) {
            promptDialog.dismissImmediately();
        }

    }

}
