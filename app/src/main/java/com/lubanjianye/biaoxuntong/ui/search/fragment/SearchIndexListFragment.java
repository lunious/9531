package com.lubanjianye.biaoxuntong.ui.search.fragment;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.PopupWindow;
import android.widget.TextView;

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
import com.lubanjianye.biaoxuntong.bean.IndexListBean;
import com.lubanjianye.biaoxuntong.database.DatabaseManager;
import com.lubanjianye.biaoxuntong.database.UserProfile;
import com.lubanjianye.biaoxuntong.eventbus.EventMessage;
import com.lubanjianye.biaoxuntong.ui.detail.IndexArticleDetailActivity;
import com.lubanjianye.biaoxuntong.ui.dropdown.SpinerPopWindow;
import com.lubanjianye.biaoxuntong.ui.main.index.IndexListAdapter;
import com.lubanjianye.biaoxuntong.ui.main.index.detail.sichuan.IndexBxtgdjDetailActivity;
import com.lubanjianye.biaoxuntong.ui.main.index.detail.sichuan.IndexScgggDetailActivity;
import com.lubanjianye.biaoxuntong.ui.main.index.detail.sichuan.IndexSggjyDetailActivity;
import com.lubanjianye.biaoxuntong.ui.main.index.detail.sichuan.IndexSggjycgrowDetailActivity;
import com.lubanjianye.biaoxuntong.ui.main.index.detail.sichuan.IndexSggjycgtableDetailActivity;
import com.lubanjianye.biaoxuntong.ui.main.index.detail.sichuan.IndexXcgggDetailActivity;
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

public class SearchIndexListFragment extends BaseFragment1 {


    private TextView tvArea = null;
    private TextView tvType = null;
    private View view = null;
    private RecyclerView searchRecycler = null;
    private SwipeRefreshLayout resultRefresh = null;
    private MultipleStatusView loadingStatus = null;

    private IndexListAdapter mAdapter = null;
    private ArrayList<IndexListBean> mDataList = new ArrayList<>();
    private View noDataView = null;

    private int page = 1;

    private long id = 0;

    String mArea = "";
    String mType = "";

    private String mDiqu = "";


    private SpinerPopWindow<String> mSpinerArea = null;
    private SpinerPopWindow<String> mSpinerType = null;
    private List<String> Arealist = new ArrayList<String>();
    private List<String> Typelist = new ArrayList<String>();


    private static String keyWorld = "";

    public static SearchIndexListFragment newInstance(String contentt) {
        SearchIndexListFragment fragment = new SearchIndexListFragment();
        keyWorld = contentt;
        return fragment;
    }

    @Override
    public Object setLayout() {
        return R.layout.fragment_index_search;
    }

    @Override
    public void initView() {
        tvArea = getView().findViewById(R.id.tv_area);
        tvType = getView().findViewById(R.id.tv_type);
        view = getView().findViewById(R.id.view);
        searchRecycler = getView().findViewById(R.id.search_recycler);
        resultRefresh = getView().findViewById(R.id.search_refresh);
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
        Typelist.add("全部");
        Arealist.add("全部");
        loadArea();
        loadType();
    }

    @Override
    public void initEvent() {
        initRecyclerView();
        initAdapter();
        resultRefresh.setRefreshing(false);
        resultRefresh.setEnabled(false);

        if (!NetUtil.isNetworkConnected(getActivity())) {
            ToastUtil.shortBottonToast(getContext(), "请检查网络设置");
            mAdapter.setEnableLoadMore(false);
            requestData(1);
        } else {
            requestData(0);
        }
    }


    private void initRecyclerView() {

        searchRecycler.setLayoutManager(new LinearLayoutManager(getContext()));

        searchRecycler.addOnItemTouchListener(new OnItemClickListener() {
            @Override
            public void onSimpleItemClick(BaseQuickAdapter adapter, View view, int position) {
                final IndexListBean data = (IndexListBean) adapter.getData().get(position);
                final int entityId = data.getEntityId();
                final String entity = data.getEntity();
                final String entityUrl = data.getEntityUrl();

                Log.d("UBHDASBDSADAS", entityId + "---" + entity);

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
                        ARouter.getInstance().build("/com/BrowserDetailActivity").withString("mApi",BiaoXunTongApi.URL_GETCOLLECTIONLISTDETAIL)
                                .withString("mTitle",title).withString("mEntity",entity).withInt("mEntityid",entityId).navigation();
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

        mAdapter = new IndexListAdapter(R.layout.fragment_index_item, mDataList);

        mAdapter.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
            @Override
            public void onLoadMoreRequested() {
                //TODO 去加载更多数据
                requestData(2);
            }
        });
        //设置列表动画
//        mAdapter.openLoadAnimation(BaseQuickAdapter.SLIDEIN_BOTTOM);
        mAdapter.setLoadMoreView(new CustomLoadMoreView());
        searchRecycler.setAdapter(mAdapter);


    }


    public void requestData(final int isRefresh) {


        if ("地区".equals(tvArea.getText().toString().trim()) || "全部".equals(tvArea.getText().toString().trim())) {
            mArea = "";
        } else {
            mArea = tvArea.getText().toString().trim();
        }
        if ("类型".equals(tvType.getText().toString()) || "全部".equals(tvType.getText().toString().trim())) {
            mType = "";
        } else {
            mType = tvType.getText().toString().trim();
        }

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

            OkGo.<String>post(BiaoXunTongApi.URL_GETINDEXLIST)
                    .params("userid", id)
                    .params("area", mArea)
                    .params("type", mType)
                    .params("page", page)
                    .params("diqu", mDiqu)
                    .params("keyWord", keyWorld)
                    .params("size", 10)
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
                            } else {
                                ToastUtil.shortToast(getContext(), message);

                            }
                        }
                    });

        } else {
            //未登录的数据请求
            OkGo.<String>post(BiaoXunTongApi.URL_GETINDEXLIST)
                    .params("page", page)
                    .params("area", mArea)
                    .params("type", mType)
                    .params("diqu", mDiqu)
                    .params("keyWord", keyWorld)
                    .params("size", 10)
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
                            } else {
                                ToastUtil.shortToast(getContext(), message);
                            }

                        }
                    });

        }


    }

    private void setData(int isRefresh, JSONArray data, boolean nextPage) {
        page++;
        final int size = data == null ? 0 : data.size();
        if (isRefresh == 0 || isRefresh == 1) {
            loadingStatus.showContent();
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
                bean.setSignstauts(list.getString("signstauts"));
                bean.setIsResult(list.getString("isResult"));
                bean.setIsCorrections(list.getString("isCorrections"));
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
                    IndexListBean bean = new IndexListBean();
                    JSONObject list = data.getJSONObject(i);
                    bean.setEntryName(list.getString("entryName"));
                    bean.setSysTime(list.getString("sysTime"));
                    bean.setEntityId(list.getInteger("entityId"));
                    bean.setEntity(list.getString("entity"));
                    bean.setDeadTime(list.getString("deadTime"));
                    bean.setAddress(list.getString("address"));
                    bean.setSignstauts(list.getString("signstauts"));
                    bean.setIsResult(list.getString("isResult"));
                    bean.setIsCorrections(list.getString("isCorrections"));
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

        mAdapter.notifyDataSetChanged();


    }

    /**
     * 给TextView右边设置图片
     *
     * @param resId
     */
    public void setTextImage(int id, int resId) {
        Drawable drawable = getResources().getDrawable(resId);
        // 必须设置图片大小，否则不显示
        drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
        switch (id) {
            case R.id.tv_area:
                tvArea.setCompoundDrawables(null, null, drawable, null);
                break;
            case R.id.tv_type:
                tvType.setCompoundDrawables(null, null, drawable, null);
                break;
            default:
                break;
        }

    }

    public void loadArea() {

        OkGo.<String>post(BiaoXunTongApi.URL_GETALLTAB)
                .params("diqu", mDiqu)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        final JSONObject object = JSON.parseObject(response.body());
                        final JSONObject data = object.getJSONObject("data");
                        final JSONArray areaList = data.getJSONArray("areaList");

                        for (int i = 0; i < areaList.size(); i++) {
                            final JSONObject list = areaList.getJSONObject(i);
                            String name = list.getString("name");
                            Arealist.add(name);
                        }


                        mSpinerArea = new SpinerPopWindow<String>(getActivity(), Arealist, new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                mSpinerArea.dismiss();
                                tvArea.setText(Arealist.get(position));
                                mArea = tvArea.getText().toString();
                                mType = tvType.getText().toString();

                                requestData(1);

                            }
                        });

                        tvArea.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                if (mSpinerArea == null) {
                                    return;
                                }
                                mSpinerArea.setWidth(resultRefresh.getWidth());
                                mSpinerArea.setHeight(resultRefresh.getHeight() / 2);
                                mSpinerArea.showAsDropDown(view);
                                setTextImage(R.id.tv_area, R.drawable.icon_up);
                            }
                        });

                        mSpinerArea.setOnDismissListener(new PopupWindow.OnDismissListener() {
                            @Override
                            public void onDismiss() {
                                //TODO
                                setTextImage(R.id.tv_area, R.drawable.icon_down);
                            }
                        });
                    }
                });


    }

    public void loadType() {

        OkGo.<String>post(BiaoXunTongApi.URL_GETALLTAB)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        final JSONObject object = JSON.parseObject(response.body());
                        final JSONObject data = object.getJSONObject("data");
                        final JSONArray ownerList = data.getJSONArray("ownerList");
                        final JSONArray typeList = data.getJSONArray("typeList");

                        for (int i = 0; i < ownerList.size(); i++) {
                            final JSONObject list = ownerList.getJSONObject(i);
                            String name = list.getString("name");
                            Typelist.add(name);
                        }

                        for (int i = 0; i < typeList.size(); i++) {
                            final JSONObject list = typeList.getJSONObject(i);
                            String name = list.getString("name");
                            Typelist.add(name);
                        }

                        mSpinerType = new SpinerPopWindow<String>(getActivity(), Typelist, new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                mSpinerType.dismiss();

                                tvType.setText(Typelist.get(position));
                                mArea = tvArea.getText().toString();
                                mType = tvType.getText().toString();

                                requestData(1);

                            }
                        });

                        tvType.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                if (mSpinerType == null) {
                                    return;
                                }
                                mSpinerType.setWidth(resultRefresh.getWidth());
                                mSpinerType.setHeight(resultRefresh.getHeight() / 2);
                                mSpinerType.showAsDropDown(view);
                                setTextImage(R.id.tv_type, R.drawable.icon_up);
                            }
                        });

                        mSpinerType.setOnDismissListener(new PopupWindow.OnDismissListener() {
                            @Override
                            public void onDismiss() {
                                //TODO
                                setTextImage(R.id.tv_type, R.drawable.icon_down);
                            }
                        });
                    }
                });

    }
}
