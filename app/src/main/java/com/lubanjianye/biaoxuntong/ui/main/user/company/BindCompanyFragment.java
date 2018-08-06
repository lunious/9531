package com.lubanjianye.biaoxuntong.ui.main.user.company;

import android.graphics.Color;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.text.TextUtils;
import android.util.TypedValue;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.classic.common.MultipleStatusView;
import com.lubanjianye.biaoxuntong.R;
import com.lubanjianye.biaoxuntong.base.BaseFragment1;
import com.lubanjianye.biaoxuntong.bean.BindCompanyBean;
import com.lubanjianye.biaoxuntong.database.DatabaseManager;
import com.lubanjianye.biaoxuntong.database.UserProfile;
import com.lubanjianye.biaoxuntong.eventbus.EventMessage;
import com.lubanjianye.biaoxuntong.app.BiaoXunTongApi;
import com.lubanjianye.biaoxuntong.util.dialog.PromptButton;
import com.lubanjianye.biaoxuntong.util.dialog.PromptButtonListener;
import com.lubanjianye.biaoxuntong.util.dialog.PromptDialog;
import com.lubanjianye.biaoxuntong.util.netStatus.NetUtil;
import com.lubanjianye.biaoxuntong.util.toast.ToastUtil;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

/**
 * 项目名:   9527
 * 包名:     com.lubanjianye.biaoxuntong.ui.main.user.company
 * 文件名:   BindCompanyFragment
 * 创建者:   lunious
 * 创建时间: 2017/12/21  11:47
 * 描述:     TODO
 */

public class BindCompanyFragment extends BaseFragment1 implements View.OnClickListener {

    private LinearLayout llIvBack = null;
    private AppCompatTextView mainBarName = null;
    private SearchView viewSearcher = null;
    RecyclerView bindCompanyRecycler = null;
    private MultipleStatusView bindCimpanyStatusView = null;

    private String mProvinceCode = "510000";
    private String mqyIds = "";


    private long userId = 0;
    private String token = "";
    private String mobile = "";
    private String nickName = "";
    private String comid = "";
    private String imageUrl = "";
    private String companyName = "";

    private PromptDialog promptDialog;
    String mKeyWord = "";
    private List<String> companyList = new ArrayList<>();

    private BindCompanyAdapter mAdapter;
    private ArrayList<BindCompanyBean> mDataList = new ArrayList<>();


    @Override
    public Object setLayout() {
        return R.layout.fragment_bind_company;
    }

    @Override
    public void initView() {
        llIvBack = getView().findViewById(R.id.ll_iv_back);
        mainBarName = getView().findViewById(R.id.main_bar_name);
        viewSearcher = getView().findViewById(R.id.view_bind);
        bindCompanyRecycler = getView().findViewById(R.id.bind_company_recycler);
        bindCimpanyStatusView = getView().findViewById(R.id.bind_company_status_view);
        llIvBack.setOnClickListener(this);

    }

    @Override
    public void initData() {
        mainBarName.setText("绑定企业");
        llIvBack.setVisibility(View.VISIBLE);
        promptDialog = new PromptDialog(getActivity());


        //根据id-search_src_text获取TextView
        SearchView.SearchAutoComplete searchText = (SearchView.SearchAutoComplete) viewSearcher.findViewById(R.id.search_src_text);
        //修改字体大小
        searchText.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 14);

        //修改字体颜色
        searchText.setTextColor(ContextCompat.getColor(getContext(), R.color.main_theme_color));
        searchText.setHintTextColor(ContextCompat.getColor(getContext(), R.color.search_hint));

        //根据id-search_mag_icon获取ImageView
        ImageView searchButton = (ImageView) viewSearcher.findViewById(R.id.search_mag_icon);

        searchButton.setImageResource(R.mipmap.img_search);

    }

    @Override
    public void initEvent() {
        initRecyclerView();
        initAdapter();
        //搜索功能
        viewSearcher.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {

                if (TextUtils.isEmpty(query.trim())) {
                    if (mDataList != null) {
                        mDataList.clear();
                    }
                    mAdapter.notifyDataSetChanged();
                    return false;
                } else {
                    mKeyWord = query.trim();
                    requestCompanyData(true);
                    hideSoftInput();

                }
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if (TextUtils.isEmpty(newText.trim())) {
                    mKeyWord = "";
                    if (mDataList != null) {
                        mDataList.clear();
                    }
                    mAdapter.notifyDataSetChanged();
                } else {
                    mKeyWord = newText.trim();
                    requestCompanyData(true);
                }
                return true;
            }
        });

    }

    public void requestCompanyData(final boolean isRefresh) {

        List<UserProfile> users = DatabaseManager.getInstance().getDao().loadAll();

        for (int i = 0; i < users.size(); i++) {
            userId = users.get(0).getId();
            token = users.get(0).getToken();
        }

        if (isRefresh) {

            OkGo.<String>post(BiaoXunTongApi.URL_GETSUITCOMPANY)
                    .params("name", mKeyWord)
                    .execute(new StringCallback() {
                        @Override
                        public void onSuccess(Response<String> response) {
                            final JSONObject object = JSON.parseObject(response.body());
                            String status = object.getString("status");

                            if ("200".equals(status)) {

                                JSONArray data = object.getJSONArray("data");

                                if (data.size() > 0) {
                                    mqyIds = data.toString();
                                    requestData(true);
                                } else {
                                    requestData(true);
                                }

                            } else if ("500".equals(status)) {
                                ToastUtil.shortToast(getContext(), "请输入关键字！");
                            } else {
                                ToastUtil.shortToast(getContext(), "服务器错误！");
                            }
                        }
                    });


        } else {
            requestData(false);
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

    private void initRecyclerView() {

        bindCompanyRecycler.setLayoutManager(new LinearLayoutManager(getContext()));

        bindCompanyRecycler.addOnItemTouchListener(new OnItemClickListener() {
            @Override
            public void onSimpleItemClick(BaseQuickAdapter adapter, View view, int position) {

                final BindCompanyBean data = (BindCompanyBean) adapter.getData().get(position);
                final String sfId = data.getSfId();

                List<UserProfile> users = DatabaseManager.getInstance().getDao().loadAll();

                for (int i = 0; i < users.size(); i++) {
                    userId = users.get(0).getId();
                    token = users.get(0).getToken();
                    mobile = users.get(0).getMobile();
                    nickName = users.get(0).getNickName();
                    comid = users.get(0).getComid();
                    imageUrl = users.get(0).getImageUrl();

                }
                companyName = companyList.get(position);

                final PromptButton cancel = new PromptButton("确定", new PromptButtonListener() {
                    @Override
                    public void onClick(PromptButton button) {

                        OkGo.<String>post(BiaoXunTongApi.URL_USERBINDCOMPANY)
                                .params("userId", userId)
                                .params("sf_id", sfId)
                                .execute(new StringCallback() {
                                    @Override
                                    public void onSuccess(Response<String> response) {

                                    }
                                });
                        final UserProfile profile = new UserProfile(userId, mobile, nickName, token, comid, imageUrl, companyName);
                        DatabaseManager.getInstance().getDao().update(profile);
                        EventBus.getDefault().post(new EventMessage(EventMessage.BIND_COMPANY_SUCCESS));
                        ToastUtil.shortBottonToast(getContext(), "绑定成功");
                        getActivity().onBackPressed();

                    }
                });
                cancel.setTextColor(Color.parseColor("#00bfdc"));
                cancel.setTextSize(16);

                final PromptButton toLogin = new PromptButton("再看看", new PromptButtonListener() {
                    @Override
                    public void onClick(PromptButton button) {
                    }
                });
                toLogin.setTextColor(Color.parseColor("#cccc33"));
                toLogin.setTextSize(16);
                promptDialog.getAlertDefaultBuilder().withAnim(false).cancleAble(false).touchAble(false);
                promptDialog.showWarnAlert("确定要绑定该企业吗？", toLogin, cancel, false);


            }
        });

    }

    private void initAdapter() {
        mAdapter = new BindCompanyAdapter(R.layout.fragment_company_bind_item, mDataList);
        bindCompanyRecycler.setAdapter(mAdapter);

    }

    public void requestData(final boolean isRefresh) {

        if (!NetUtil.isNetworkConnected(getActivity())) {
            bindCimpanyStatusView.showNoNetwork();
        } else {
            bindCimpanyStatusView.showLoading();
            List<UserProfile> users = DatabaseManager.getInstance().getDao().loadAll();
            long id = 0;
            String token = "";
            for (int i = 0; i < users.size(); i++) {
                id = users.get(0).getId();
                token = users.get(0).getToken();
            }

            OkGo.<String>post(BiaoXunTongApi.URL_SUITRESULT)
                    .params("userId", id)
                    .params("token", token)
                    .params("provinceCode", mProvinceCode)
                    .params("qyIds", mqyIds)
                    .execute(new StringCallback() {
                        @Override
                        public void onSuccess(Response<String> response) {
                            final JSONObject object = JSON.parseObject(response.body());
                            String status = object.getString("status");

                            if ("200".equals(status)) {

                                final JSONArray array = object.getJSONArray("data");

                                String cpn = "";

                                if (companyList.size() > 0) {
                                    companyList.clear();
                                }

                                for (int i = 0; i < array.size(); i++) {
                                    final JSONObject data = array.getJSONObject(i);
                                    cpn = data.getString("qy");
                                    companyList.add(cpn);
                                }

                                if (array.size() > 0) {
                                    bindCimpanyStatusView.showContent();
                                    setData(isRefresh, array);
                                } else {
                                    if (mDataList != null) {
                                        mDataList.clear();
                                        mAdapter.notifyDataSetChanged();
                                    }
                                    //TODO 内容为空的处理
                                    bindCimpanyStatusView.showEmpty();
                                }

                            } else {
                                bindCimpanyStatusView.showError();
                            }
                        }
                    });

        }

    }

    private void setData(boolean isRefresh, JSONArray data) {

        if (isRefresh) {
            mDataList.clear();
            for (int i = 0; i < data.size(); i++) {
                BindCompanyBean bean = new BindCompanyBean();
                JSONObject list = data.getJSONObject(i);
                bean.setQy(list.getString("qy"));
                bean.setLxr(list.getString("lxr"));
                bean.setEntrySign(list.getString("entrySign"));
                bean.setSfId(list.getString("sfId"));
                mDataList.add(bean);
            }
            mAdapter.setEnableLoadMore(true);
            mAdapter.notifyDataSetChanged();
        }
    }

}

