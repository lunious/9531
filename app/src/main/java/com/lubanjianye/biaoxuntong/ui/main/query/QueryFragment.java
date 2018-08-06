package com.lubanjianye.biaoxuntong.ui.main.query;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.LinearLayoutManager;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.alibaba.android.arouter.launcher.ARouter;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.lubanjianye.biaoxuntong.R;
import com.lubanjianye.biaoxuntong.app.BiaoXunTongApi;
import com.lubanjianye.biaoxuntong.base.BaseFragment1;
import com.lubanjianye.biaoxuntong.bean.JsonString;
import com.lubanjianye.biaoxuntong.bean.QueryBean;
import com.lubanjianye.biaoxuntong.database.DatabaseManager;
import com.lubanjianye.biaoxuntong.database.UserProfile;
import com.lubanjianye.biaoxuntong.eventbus.EventMessage;
import com.lubanjianye.biaoxuntong.ui.main.user.avater.AvaterActivity;
import com.lubanjianye.biaoxuntong.ui.sign.SignInActivity;
import com.lubanjianye.biaoxuntong.ui.dropdown.SpinerPopWindow;
import com.lubanjianye.biaoxuntong.util.dialog.PromptButton;
import com.lubanjianye.biaoxuntong.util.dialog.PromptButtonListener;
import com.lubanjianye.biaoxuntong.util.dialog.PromptDialog;
import com.lubanjianye.biaoxuntong.util.rx.RxTextViewVertical;
import com.lubanjianye.biaoxuntong.util.sp.AppSharePreferenceMgr;
import com.lubanjianye.biaoxuntong.util.toast.ToastUtil;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.cache.CacheMode;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;
import com.yanzhenjie.recyclerview.swipe.SwipeMenu;
import com.yanzhenjie.recyclerview.swipe.SwipeMenuBridge;
import com.yanzhenjie.recyclerview.swipe.SwipeMenuCreator;
import com.yanzhenjie.recyclerview.swipe.SwipeMenuItem;
import com.yanzhenjie.recyclerview.swipe.SwipeMenuItemClickListener;
import com.yanzhenjie.recyclerview.swipe.SwipeMenuRecyclerView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;


public class QueryFragment extends BaseFragment1 implements View.OnClickListener {

    private AppCompatTextView mainBarName = null;
    private EditText etQuery = null;
    private TextView tvQuery = null;
    private TextView tvZzlx = null;
    private TextView tvDl = null;
    private TextView tvXl = null;
    private TextView tvZy = null;
    private TextView tvDj = null;
    private TextView tvQy = null;
    private AppCompatTextView tvGdQy = null;
    private Button btnAdd = null;
    private View view = null;
    private LinearLayout ll = null;
    private AppCompatTextView btnStartSearch = null;
    private RxTextViewVertical mRxVText = null;
    private AppCompatTextView vipDetail = null;
    private LinearLayout llSearch = null;
    private SwipeMenuRecyclerView rlv_query = null;


    private PromptDialog promptDialog;

    private long id = 0;
    private String mobile = "";

    private SpinerPopWindow<String> mSpinerZzlx;
    private SpinerPopWindow<String> mSpinerDl;
    private SpinerPopWindow<String> mSpinerXl;
    private SpinerPopWindow<String> mSpinerZy;
    private SpinerPopWindow<String> mSpinerDj;
    private SpinerPopWindow<String> mSpinerQy;
    private SpinerPopWindow<String> mSpinerGdQy;

    private List<String> Zzlxlist = new ArrayList<String>();
    private List<String> Dllist = new ArrayList<String>();
    private List<String> Xllist = new ArrayList<String>();
    private List<String> Zylist = new ArrayList<String>();
    private List<String> Djlist = new ArrayList<String>();
    private List<String> Qylist = new ArrayList<String>();
    private List<String> GdQylist = new ArrayList<String>();

    String one = null;
    String two = null;
    String three = null;
    String four = null;
    String five = null;
    String six = null;


    private String provinceCode = "";
    private String lxId = "";
    private String dlId = "";
    private String xlId = "";
    private String zyId = "";
    private String djId = "";
    private String fs = "";
    private String zcd = "";
    private String entrySign = "";


    String qyIds = "";

    List<Object> allids = new ArrayList<Object>();
    List<Object> ids = new ArrayList<Object>();
    List<Object> ids_1 = new ArrayList<Object>();
    List<Object> ids_2 = new ArrayList<Object>();
    List<Object> ids_3 = new ArrayList<Object>();


    int i = 0;

    private QueryAdapter mAdapter;
    private ArrayList<QueryBean> mDataList = new ArrayList<>();

    //跑马灯相关
    private ArrayList<String> titleList = new ArrayList<String>();


    private void initRunText() {
        mRxVText.setTextList(titleList);
        //设置属性
        mRxVText.setText(14, 4, 0xff9e9e9e);
        //设置停留时长间隔
        mRxVText.setTextStillTime(4000);
        //设置进入和退出的时间间隔
        mRxVText.setAnimTime(400);
        mRxVText.setOnItemClickListener(new RxTextViewVertical.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                etQuery.setText(titleList.get(position));
                etQuery.setSelection(titleList.get(position).length());
                llSearch.setVisibility(View.VISIBLE);
            }
        });

        mRxVText.startAutoScroll();
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        mRxVText.stopAutoScroll();
    }

    private void getScrollViewData() {

        OkGo.<String>post("http://www.lubanjianye.com/query/vipNames")
                .cacheMode(CacheMode.IF_NONE_CACHE_REQUEST)
                .cacheTime(3600 * 48000)
                .execute(new StringCallback() {

                    @Override
                    public void onCacheSuccess(Response<String> response) {
                        final JSONObject object = JSON.parseObject(response.body());
                        JSONArray array = object.getJSONArray("data");

                        if (array != null) {
                            for (int j = 0; j < array.size(); j++) {
                                titleList.add(array.get(j).toString());
                            }
                            initRunText();
                        }
                    }

                    @Override
                    public void onSuccess(Response<String> response) {
                        final JSONObject object = JSON.parseObject(response.body());
                        JSONArray array = object.getJSONArray("data");

                        if (array != null) {
                            for (int j = 0; j < array.size(); j++) {
                                titleList.add(array.get(j).toString());
                            }
                            initRunText();
                        }
                    }
                });

    }

    @Override
    public Object setLayout() {
        return R.layout.fragment_query;
    }

    @Override
    public void initView() {

        //注册EventBus
        EventBus.getDefault().register(this);

        mainBarName = getView().findViewById(R.id.main_bar_name);
        etQuery = getView().findViewById(R.id.et_query);
        tvQuery = getView().findViewById(R.id.tv_query);
        tvZzlx = getView().findViewById(R.id.tv_zzlx);
        tvDl = getView().findViewById(R.id.tv_dl);
        tvXl = getView().findViewById(R.id.tv_xl);
        tvZy = getView().findViewById(R.id.tv_zy);
        tvDj = getView().findViewById(R.id.tv_dj);
        tvQy = getView().findViewById(R.id.tv_qy);
        tvGdQy = getView().findViewById(R.id.tv_gd_qy);
        btnAdd = getView().findViewById(R.id.btn_add);
        view = getView().findViewById(R.id.view);
        ll = getView().findViewById(R.id.ll);
        btnStartSearch = getView().findViewById(R.id.btn_start_search);
        mRxVText = getView().findViewById(R.id.scroll_view);
        vipDetail = getView().findViewById(R.id.vip_detail);
        llSearch = getView().findViewById(R.id.ll_search);
        rlv_query = getView().findViewById(R.id.rlv_query);

        vipDetail.setOnClickListener(this);
        btnAdd.setOnClickListener(this);
        btnStartSearch.setOnClickListener(this);
        tvQuery.setOnClickListener(this);


    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void XXXXXX(EventMessage message) {

        if (EventMessage.LOCA_AREA_CHANGE.equals(message.getMessage())) {

            if (ids_1.size() > 0) {
                ids_1.clear();
            }

            if (ids_2.size() > 0) {
                ids_2.clear();
            }

            if (ids_3.size() > 0) {
                ids_3.clear();
            }

            mDataList.clear();
            mAdapter.notifyDataSetChanged();

            if (Qylist.size() > 0) {
                Qylist.clear();
            }
            if (GdQylist.size() > 0) {
                GdQylist.clear();
            }

            //重置后面选项名称
            tvZzlx.setText("资质类型");
            tvDl.setText("大类");
            tvXl.setText("小类");
            tvZy.setText("专业");
            tvDj.setText("等级");
            tvQy.setText("地区范围");
            tvGdQy.setText("地区范围");


            if (AppSharePreferenceMgr.contains(getContext(), EventMessage.LOCA_AREA_CODE)) {
                provinceCode = (String) AppSharePreferenceMgr.get(getContext(), EventMessage.LOCA_AREA_CODE, "");
            } else {
                provinceCode = "510000";
            }

            one = null;
            two = null;
            three = null;
            four = null;
            five = null;
            six = null;

            if ("440000".equals(provinceCode)) {
                tvZzlx.setVisibility(View.VISIBLE);
                tvDl.setVisibility(View.VISIBLE);
                tvXl.setVisibility(View.VISIBLE);
                tvZy.setVisibility(View.VISIBLE);
                tvDj.setVisibility(View.VISIBLE);
                tvQy.setVisibility(View.GONE);
                tvGdQy.setVisibility(View.VISIBLE);
                GdQylist.add("粤内");
                GdQylist.add("粤内和入粤");
                GdQylist.add("全国");
            } else {
                tvZzlx.setVisibility(View.VISIBLE);
                tvDl.setVisibility(View.VISIBLE);
                tvXl.setVisibility(View.VISIBLE);
                tvZy.setVisibility(View.VISIBLE);
                tvDj.setVisibility(View.VISIBLE);
                tvQy.setVisibility(View.VISIBLE);
                tvGdQy.setVisibility(View.GONE);
            }


        }


    }

    @Override
    public void initData() {
        mainBarName.setVisibility(View.VISIBLE);
        mainBarName.setText("资质查询");

        initRecyclerView();
        initAdapter();

        if (AppSharePreferenceMgr.contains(getContext(), EventMessage.LOCA_AREA_CODE)) {
            provinceCode = (String) AppSharePreferenceMgr.get(getContext(), EventMessage.LOCA_AREA_CODE, "");
        } else {
            provinceCode = "510000";
        }
    }

    @Override
    public void initEvent() {

        //创建对象
        promptDialog = new PromptDialog(getActivity());
        //设置自定义属性
        promptDialog.getDefaultBuilder().touchAble(true).round(3).loadingDuration(3000);

        //跑马灯效果
        getScrollViewData();

        if ("440000".equals(provinceCode)) {
            tvZzlx.setVisibility(View.VISIBLE);
            tvDl.setVisibility(View.VISIBLE);
            tvXl.setVisibility(View.VISIBLE);
            tvZy.setVisibility(View.VISIBLE);
            tvDj.setVisibility(View.VISIBLE);
            tvQy.setVisibility(View.GONE);
            tvGdQy.setVisibility(View.VISIBLE);
            GdQylist.add("粤内");
            GdQylist.add("粤内和入粤");
            GdQylist.add("全国");
            loadGdQy();
        } else {
            tvZzlx.setVisibility(View.VISIBLE);
            tvDl.setVisibility(View.VISIBLE);
            tvXl.setVisibility(View.VISIBLE);
            tvZy.setVisibility(View.VISIBLE);
            tvDj.setVisibility(View.VISIBLE);
            tvQy.setVisibility(View.VISIBLE);
            tvGdQy.setVisibility(View.GONE);
        }

        loadZZLX();


        List<UserProfile> users = DatabaseManager.getInstance().getDao().loadAll();
        for (int i = 0; i < users.size(); i++) {
            id = users.get(0).getId();
            mobile = users.get(0).getMobile();
        }

        if (TextUtils.isEmpty(etQuery.getText().toString().trim())) {
            llSearch.setVisibility(View.GONE);
        } else {
            llSearch.setVisibility(View.VISIBLE);
        }

        etQuery.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                if (s.length() != 0) {
                    llSearch.setVisibility(View.VISIBLE);
                } else {
                    llSearch.setVisibility(View.GONE);
                }
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() != 0) {
                    llSearch.setVisibility(View.VISIBLE);
                } else {
                    llSearch.setVisibility(View.GONE);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() != 0) {
                    llSearch.setVisibility(View.VISIBLE);
                } else {
                    llSearch.setVisibility(View.GONE);
                }
            }
        });
    }

    private void initRecyclerView() {
        rlv_query.setLayoutManager(new LinearLayoutManager(getContext()));

        rlv_query.setSwipeMenuCreator(new SwipeMenuCreator() {
            @Override
            public void onCreateMenu(SwipeMenu swipeLeftMenu, SwipeMenu swipeRightMenu, int viewType) {
                int width = getResources().getDimensionPixelSize(R.dimen.d45);

                // 1. MATCH_PARENT 自适应高度，保持和Item一样高;
                // 2. 指定具体的高，比如80;
                // 3. WRAP_CONTENT，自身高度，不推荐;
                int height = ViewGroup.LayoutParams.MATCH_PARENT;

                SwipeMenuItem deleteItem = new SwipeMenuItem(getActivity())
                        .setBackground(R.drawable.selector_red)
                        .setImage(R.mipmap.ic_action_delete)
                        .setWidth(width)
                        .setHeight(height);
                swipeRightMenu.addMenuItem(deleteItem);

            }
        });

        rlv_query.setSwipeMenuItemClickListener(new SwipeMenuItemClickListener() {
            @Override
            public void onItemClick(SwipeMenuBridge menuBridge) {
                menuBridge.closeMenu();
                // RecyclerView的Item的position。
                int adapterPosition = menuBridge.getAdapterPosition();

                switch (adapterPosition) {
                    case 0:
                        if (ids_1.size() > 0) {
                            ids_1.clear();
                        } else {
                            if (ids_2.size() > 0) {
                                ids_2.clear();
                            } else {
                                ids_3.clear();
                            }
                        }
                        mDataList.remove(adapterPosition);
                        mAdapter.notifyDataSetChanged();
                        break;
                    case 1:
                        if (ids_2.size() > 0) {
                            ids_2.clear();
                        } else {
                            ids_3.clear();
                        }
                        mDataList.remove(adapterPosition);
                        mAdapter.notifyDataSetChanged();
                        break;
                    case 2:
                        ids_3.clear();
                        mDataList.remove(adapterPosition);
                        mAdapter.notifyDataSetChanged();
                        break;
                    default:
                        break;
                }

            }
        });


    }

    private void initAdapter() {

        mAdapter = new QueryAdapter(R.layout.query_item, mDataList);

        rlv_query.setAdapter(mAdapter);
    }


    @Override
    public void onSupportInvisible() {
        super.onSupportInvisible();

        if (promptDialog != null) {
            promptDialog.dismissImmediately();
        }

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

        //取消注册EventBus
        EventBus.getDefault().unregister(this);

        if (mDataList.size() > 0) {
            mDataList.clear();
        }

        if (allids.size() > 0) {
            allids.clear();
        }
        if (ids_1.size() > 0) {
            ids_1.clear();
        }
        if (ids_2.size() > 0) {
            ids_2.clear();
        }
        if (ids_3.size() > 0) {
            ids_3.clear();
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.vip_detail:
                String VipUrl = "http://m.lubanjianye.com/home/index/vipservice.html";
                ARouter.getInstance().build("/com/BrowserSuitActivity").withString("mUrl",VipUrl).withString("mTitle","").navigation();
                break;
            case R.id.btn_add:
                if (TextUtils.isEmpty(one)) {
                    ToastUtil.shortToast(getContext(), "请选择资质类型");
                } else if ("工程造价咨询".equals(one) || "设计施工一体化".equals(one)) {
                    if (mDataList.size() <= 2) {
                        QueryBean bean = new QueryBean();
                        bean.setZzlx(one);
                        bean.setDl(two);
                        bean.setXl(three);
                        bean.setZy(four);
                        bean.setDj(five);
                        bean.setDq(six);
                        mDataList.add(bean);

                        mAdapter.notifyDataSetChanged();

                        //得到符合条件的id
                        getSuitCompany();

                    } else {
                        ToastUtil.shortToast(getContext(), "最多叠加三个条件!");
                    }
                } else {
                    if (TextUtils.isEmpty(two)) {
                        ToastUtil.shortToast(getContext(), "请选择大类");
                    } else {
                        if (mDataList.size() <= 2) {
                            QueryBean bean = new QueryBean();
                            bean.setZzlx(one);
                            bean.setDl(two);
                            bean.setXl(three);
                            bean.setZy(four);
                            bean.setDj(five);
                            bean.setDq(six);
                            mDataList.add(bean);

                            mAdapter.notifyDataSetChanged();

                            //得到符合条件的id
                            getSuitCompany();

                        } else {
                            ToastUtil.shortToast(getContext(), "最多叠加三个条件!");
                        }
                    }
                }

                break;
            case R.id.btn_start_search:

                if (mDataList.size() > 0) {
                    if (ids_1.size() > 0 && ids_2.size() == 0 && ids_3.size() == 0) {
                        if (ids.size() > 0) {
                            ids.clear();
                        }
                        ids.addAll(ids_1);
                        allids = ids;
                        i = allids.size();
                        qyIds = allids.toString();
                    } else if (ids_1.size() > 0 && ids_2.size() > 0 && ids_3.size() == 0) {
                        if (ids.size() > 0) {
                            ids.clear();
                        }
                        ids.addAll(ids_1);
                        ids.retainAll(ids_2);
                        allids = ids;
                        i = allids.size();
                        qyIds = allids.toString();
                    } else if (ids_1.size() > 0 && ids_2.size() == 0 && ids_3.size() > 0) {
                        if (ids.size() > 0) {
                            ids.clear();
                        }
                        ids.addAll(ids_1);
                        ids.retainAll(ids_3);
                        allids = ids;
                        i = allids.size();
                        qyIds = allids.toString();
                    } else if (ids_1.size() == 0 && ids_2.size() > 0 && ids_3.size() == 0) {
                        if (ids.size() > 0) {
                            ids.clear();
                        }
                        ids.addAll(ids_2);
                        allids = ids;
                        i = allids.size();
                        qyIds = allids.toString();
                    } else if (ids_1.size() == 0 && ids_2.size() > 0 && ids_3.size() > 0) {
                        if (ids.size() > 0) {
                            ids.clear();
                        }
                        ids.addAll(ids_2);
                        ids.retainAll(ids_3);
                        allids = ids;
                        i = allids.size();
                        qyIds = allids.toString();
                    } else if (ids_1.size() == 0 && ids_2.size() == 0 && ids_3.size() > 0) {
                        if (ids.size() > 0) {
                            ids.clear();
                        }
                        ids.addAll(ids_3);
                        allids = ids;
                        i = allids.size();
                        qyIds = allids.toString();
                    } else if (ids_1.size() > 0 && ids_2.size() > 0 && ids_3.size() > 0) {
                        if (ids.size() > 0) {
                            ids.clear();
                        }
                        ids.addAll(ids_3);
                        ids.retainAll(ids_1);
                        ids.retainAll(ids_2);
                        allids = ids;
                        i = allids.size();
                        qyIds = allids.toString();
                    }


                    if (i != 0) {
                        final PromptButton cancel = new PromptButton("取      消", new PromptButtonListener() {
                            @Override
                            public void onClick(PromptButton button) {
                            }
                        });
                        cancel.setTextColor(Color.parseColor("#cccc33"));
                        cancel.setTextSize(15);

                        final PromptButton toLogin = new PromptButton("查看详情", new PromptButtonListener() {
                            @Override
                            public void onClick(PromptButton button) {
                                if (AppSharePreferenceMgr.contains(getContext(), EventMessage.LOGIN_SUCCSS)) {
                                    List<UserProfile> users = DatabaseManager.getInstance().getDao().loadAll();
                                    for (int i = 0; i < users.size(); i++) {
                                        id = users.get(0).getId();
                                        mobile = users.get(0).getMobile();
                                    }

                                    if (!TextUtils.isEmpty(mobile)) {
                                        //根据返回的id去查询公司名称
                                        Intent intent = new Intent(getActivity(), CompanySearchResultActivity.class);
                                        intent.putExtra("provinceCode", provinceCode);
                                        intent.putExtra("qyIds", qyIds);
                                        intent.putExtra("showSign", "1");
                                        startActivity(intent);
                                    } else {
                                        ToastUtil.shortToast(getContext(), "请先绑定手机号");
                                        Intent intent = new Intent(getActivity(), AvaterActivity.class);
                                        startActivity(intent);
                                    }

                                } else {
                                    //未登录去登陆
                                    startActivity(new Intent(getActivity(), SignInActivity.class));
                                }

                            }
                        });
                        toLogin.setTextColor(Color.parseColor("#00bfdc"));
                        toLogin.setTextSize(15);
                        promptDialog.getAlertDefaultBuilder().withAnim(false).cancleAble(false).touchAble(false);

                        promptDialog.showWarnAlert("共为你查询到" + i + "家企业!", cancel, toLogin);
                    } else {
                        final PromptButton cancel = new PromptButton("重新筛选", new PromptButtonListener() {
                            @Override
                            public void onClick(PromptButton button) {
                            }
                        });
                        cancel.setTextColor(Color.parseColor("#21a9ff"));
                        cancel.setTextSize(15);

                        promptDialog.getAlertDefaultBuilder().withAnim(false).cancleAble(false).touchAble(false);

                        promptDialog.showWarnAlert("共为你查询到" + i + "家企业!", cancel, false);
                    }
                } else {
                    ToastUtil.shortToast(getContext(), "请添加条件");
                }

                break;
            case R.id.tv_query:
                if (TextUtils.isEmpty(etQuery.getText().toString().trim())) {
                    ToastUtil.shortToast(getContext(), "请输入关键字！");
                } else {

                    if (AppSharePreferenceMgr.contains(getContext(), EventMessage.LOGIN_SUCCSS)) {
                        List<UserProfile> users = DatabaseManager.getInstance().getDao().loadAll();
                        for (int i = 0; i < users.size(); i++) {
                            id = users.get(0).getId();
                            mobile = users.get(0).getMobile();
                        }

                        if (!TextUtils.isEmpty(mobile)) {
                            promptDialog.showLoading("正在查询...");

                            final String name = etQuery.getText().toString().trim();

                            OkGo.<String>post(BiaoXunTongApi.URL_GETSUITCOMPANY)
                                    .params("name", name)
                                    .params("userid", id)
                                    .execute(new StringCallback() {
                                        @Override
                                        public void onSuccess(Response<String> response) {
                                            final JSONArray data = JSON.parseObject(response.body()).getJSONArray("data");
                                            if (data.size() > 0) {
                                                //根据返回的id去查询公司名称
                                                promptDialog.dismissImmediately();
                                                Intent intent = new Intent(getActivity(), CompanySearchResultActivity.class);
                                                intent.putExtra("provinceCode", provinceCode);
                                                intent.putExtra("qyIds", data.toString());
                                                intent.putExtra("showSign", "0");
                                                startActivity(intent);
                                            } else {
                                                promptDialog.dismissImmediately();
                                                ToastUtil.shortToast(getContext(), "查询结果为0");
                                            }
                                        }
                                    });

                        } else {
                            ToastUtil.shortToast(getContext(), "请先绑定手机号");
                            startActivity(new Intent(getActivity(), AvaterActivity.class));
                        }
                    } else {
                        //未登录去登陆
                        startActivity(new Intent(getActivity(), SignInActivity.class));
                    }
                }
                break;
            default:
                break;
        }
    }

    public void getSuitCompany() {
        JsonString jsonString = new JsonString();
        jsonString.setLxId(lxId);
        jsonString.setDlId(dlId);
        jsonString.setXlId(xlId);
        jsonString.setZyId(zyId);
        jsonString.setDjId(djId);
        jsonString.setFs(fs);
        jsonString.setZcd(zcd);
        jsonString.setProvinceCode(provinceCode);
        jsonString.setEntrySign(entrySign);
        String userJson = JSON.toJSONString(jsonString);

        Log.d("JABSJHDBASDAS", userJson);

        OkGo.<String>post(BiaoXunTongApi.URL_GETSUITIDS)
                .params("JSONString", userJson)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        final JSONObject jsonObject = JSONObject.parseObject(response.body());
                        String status = jsonObject.getString("status");

                        if ("200".equals(status)) {

                            final JSONArray dataArray = jsonObject.getJSONArray("data");

                            if (ids_1.size() > 0) {
                                if (ids_2.size() > 0) {
                                    ids_3 = dataArray;
                                } else {
                                    ids_2 = dataArray;
                                }
                            } else {
                                ids_1 = dataArray;
                            }

                        } else {
                            ToastUtil.shortToast(getContext(), "服务器错误");
                        }
                    }
                });

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
            case R.id.tv_zzlx:
                tvZzlx.setCompoundDrawables(null, null, drawable, null);
                break;
            case R.id.tv_dl:
                tvDl.setCompoundDrawables(null, null, drawable, null);
                break;
            case R.id.tv_xl:
                tvXl.setCompoundDrawables(null, null, drawable, null);
                break;
            case R.id.tv_zy:
                tvZy.setCompoundDrawables(null, null, drawable, null);
                break;
            case R.id.tv_dj:
                tvDj.setCompoundDrawables(null, null, drawable, null);
                break;
            case R.id.tv_qy:
                tvQy.setCompoundDrawables(null, null, drawable, null);
                break;
            case R.id.tv_gd_qy:
                tvGdQy.setCompoundDrawables(null, null, drawable, null);
                break;
            default:
                break;
        }

    }

    /**
     * 加载广东地区
     */

    public void loadGdQy() {
        mSpinerGdQy = new SpinerPopWindow<String>(getActivity(), GdQylist, new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                mSpinerGdQy.dismiss();
                six = GdQylist.get(position);


                if ("粤内".equals(six)) {
                    entrySign = "0";
                } else if ("粤内和入粤".equals(six)) {
                    entrySign = "2";
                } else if ("全国".equals(six)) {
                    entrySign = "-1";
                }

                one = null;
                two = null;
                three = null;
                four = null;
                five = null;

                tvGdQy.setText(GdQylist.get(position));
                tvZzlx.setVisibility(View.VISIBLE);
                tvDl.setVisibility(View.VISIBLE);
                tvXl.setVisibility(View.VISIBLE);
                tvZy.setVisibility(View.VISIBLE);
                tvDj.setVisibility(View.VISIBLE);

                //重置后面选项名称
                tvZzlx.setText("资质类型");
                tvDl.setText("大类");
                tvXl.setText("小类");
                tvZy.setText("专业");
                tvDj.setText("等级");


            }
        });

        tvGdQy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mSpinerGdQy == null) {
                    return;
                }
                mSpinerGdQy.setWidth(ll.getWidth());
                mSpinerGdQy.setHeight(ll.getHeight()/2);
                mSpinerGdQy.showAsDropDown(view);
                setTextImage(R.id.tv_gd_qy, R.mipmap.up);
            }
        });

        mSpinerGdQy.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                //TODO
                setTextImage(R.id.tv_gd_qy, R.mipmap.down);
            }
        });
    }

    /**
     * 加载资质类型列表
     */

    public void loadZZLX() {

        OkGo.<String>post(BiaoXunTongApi.URL_GETZZLXLIST)
                .params("provinceCode", provinceCode)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {

                        final JSONArray array = JSON.parseArray(response.body());

                        final JSONObject object = array.getJSONObject(0);
                        final String nodeName = object.getString("nodeName");
                        final JSONArray options = object.getJSONArray("options");

                        for (int i = 0; i < 7; i++) {
                            final JSONObject objectOptions = options.getJSONObject(i);

                            final String text = objectOptions.getString("text");

                            Zzlxlist.add(text);
                        }

                        mSpinerZzlx = new SpinerPopWindow<String>(getActivity(), Zzlxlist, new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                mSpinerZzlx.dismiss();
                                one = Zzlxlist.get(position);
                                lxId = options.getJSONObject(position).getString("value");
                                two = null;
                                three = null;
                                four = null;
                                five = null;
                                six = null;

                                tvZzlx.setText(Zzlxlist.get(position));
                                tvDl.setVisibility(View.VISIBLE);
                                tvXl.setVisibility(View.VISIBLE);
                                tvZy.setVisibility(View.VISIBLE);

                                //重置后面选项名称
                                tvDl.setText("大类");
                                tvXl.setText("小类");
                                tvZy.setText("专业");
                                tvDj.setText("等级");
                                tvQy.setText("地区范围");


                                String value = options.getJSONObject(position).getString("value");
                                String text = options.getJSONObject(position).getString("text");

                                if (Dllist.size() > 0) {
                                    Dllist.clear();
                                }

                                loadDL(provinceCode, nodeName, value, text);

                            }
                        });

                        tvZzlx.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                if (mSpinerZzlx == null) {
                                    return;
                                }
                                mSpinerZzlx.setWidth(ll.getWidth());
                                mSpinerZzlx.setHeight(ll.getHeight()/2);
                                mSpinerZzlx.showAsDropDown(view);
                                setTextImage(R.id.tv_zzlx, R.mipmap.up);
                            }
                        });

                        mSpinerZzlx.setOnDismissListener(new PopupWindow.OnDismissListener() {
                            @Override
                            public void onDismiss() {
                                //TODO
                                setTextImage(R.id.tv_zzlx, R.mipmap.down);
                            }
                        });

                    }
                });
    }

    /**
     * 加载大类列表
     */
    public void loadDL(final String provinceCode, String nodeName, String value, String text) {


        OkGo.<String>post(BiaoXunTongApi.URL_GETQYZZLIST)
                .params("provinceCode", provinceCode)
                .params("nodeName", nodeName)
                .params("value", value)
                .params("text", text)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {

                        final JSONArray array = JSON.parseArray(response.body());

                        Log.d("AUBSDASDASD", response.body());

                        for (int i = 0; i < array.size(); i++) {
                            final JSONObject object = array.getJSONObject(i);
                            final String nodeName = object.getString("nodeName");
                            if ("QY_DL".equals(nodeName)) {
                                final boolean visible = object.getBoolean("visible");
                                final JSONArray options = object.getJSONArray("options");
                                if (!visible) {
                                    tvDl.setVisibility(View.GONE);
                                    final JSONObject objectOptions = options.getJSONObject(0);
                                    final String value = objectOptions.getString("value");
                                    dlId = value;
                                } else {
                                    for (int j = 0; j < options.size(); j++) {
                                        final JSONObject objectOptions = options.getJSONObject(j);

                                        final String text = objectOptions.getString("text");

                                        Dllist.add(text);
                                    }

                                    mSpinerDl = new SpinerPopWindow<String>(getActivity(), Dllist, new AdapterView.OnItemClickListener() {
                                        @Override
                                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                            mSpinerDl.dismiss();
                                            two = Dllist.get(position);
                                            dlId = options.getJSONObject(position).getString("value");
                                            three = null;
                                            four = null;
                                            five = null;
                                            six = null;


                                            tvDl.setText(Dllist.get(position));
                                            tvXl.setVisibility(View.VISIBLE);
                                            tvZy.setVisibility(View.VISIBLE);

                                            //重置后面选项名称
                                            tvXl.setText("小类");
                                            tvZy.setText("专业");
                                            tvDj.setText("等级");
                                            tvQy.setText("地区范围");


                                            String value = options.getJSONObject(position).getString("value");
                                            String text = options.getJSONObject(position).getString("text");

                                            if (Xllist.size() > 0) {
                                                Xllist.clear();
                                            }

                                            loadXL(provinceCode, nodeName, value, text);

                                        }
                                    });

                                    tvDl.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            if (mSpinerDl == null) {
                                                return;
                                            }
                                            mSpinerDl.setWidth(ll.getWidth());
                                            mSpinerDl.setHeight(ll.getHeight()/2);
                                            mSpinerDl.showAsDropDown(view);
                                            setTextImage(R.id.tv_dl, R.mipmap.up);
                                        }
                                    });

                                    mSpinerDl.setOnDismissListener(new PopupWindow.OnDismissListener() {
                                        @Override
                                        public void onDismiss() {
                                            //TODO
                                            setTextImage(R.id.tv_dl, R.mipmap.down);
                                        }
                                    });
                                }

                            } else if ("QY_XL".equals(nodeName)) {
                                final boolean visible = object.getBoolean("visible");
                                final JSONArray options = object.getJSONArray("options");

                                if (!visible) {
                                    tvXl.setVisibility(View.GONE);
                                    final JSONObject objectOptions = options.getJSONObject(0);
                                    final String value = objectOptions.getString("value");
                                    xlId = value;
                                } else {
                                    if (Xllist.size() > 0) {
                                        Xllist.clear();
                                    }
                                    for (int j = 0; j < options.size(); j++) {
                                        final JSONObject objectOptions = options.getJSONObject(j);
                                        final String text = objectOptions.getString("text");
                                        Xllist.add(text);
                                    }
                                    mSpinerXl = new SpinerPopWindow<String>(getActivity(), Xllist, new AdapterView.OnItemClickListener() {
                                        @Override
                                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                            mSpinerXl.dismiss();
                                            three = Xllist.get(position);
                                            xlId = options.getJSONObject(position).getString("value");
                                            four = null;
                                            five = null;
                                            six = null;

                                            tvXl.setText(Xllist.get(position));

                                            //重置后面选项名称
                                            tvZy.setText("专业");
                                            tvDj.setText("等级");
                                            tvQy.setText("地区范围");


                                            String value = options.getJSONObject(position).getString("value");
                                            String text = options.getJSONObject(position).getString("text");

                                            loadZY(provinceCode, nodeName, value, text);

                                        }
                                    });

                                    tvXl.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            if (mSpinerXl == null) {
                                                return;
                                            }
                                            mSpinerXl.setWidth(ll.getWidth());
                                            mSpinerXl.setHeight(ll.getHeight()/2);
                                            mSpinerXl.showAsDropDown(view);
                                            setTextImage(R.id.tv_xl, R.mipmap.up);
                                        }
                                    });

                                    mSpinerXl.setOnDismissListener(new PopupWindow.OnDismissListener() {
                                        @Override
                                        public void onDismiss() {
                                            //TODO
                                            setTextImage(R.id.tv_xl, R.mipmap.down);
                                        }
                                    });

                                }
                            } else if ("QY_ZY".equals(nodeName)) {
                                final boolean visible = object.getBoolean("visible");
                                final JSONArray options = object.getJSONArray("options");
                                if (!visible) {
                                    tvZy.setVisibility(View.GONE);
                                    final JSONObject objectOptions = options.getJSONObject(0);
                                    final String value = objectOptions.getString("value");
                                    zyId = value;
                                } else {
                                    if (Zylist.size() > 0) {
                                        Zylist.clear();
                                    }

                                    for (int j = 0; j < options.size(); j++) {
                                        final JSONObject objectOptions = options.getJSONObject(j);

                                        final String text = objectOptions.getString("text");

                                        Zylist.add(text);
                                    }

                                    mSpinerZy = new SpinerPopWindow<String>(getActivity(), Zylist, new AdapterView.OnItemClickListener() {
                                        @Override
                                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                            mSpinerZy.dismiss();
                                            four = Zylist.get(position);
                                            zyId = options.getJSONObject(position).getString("value");
                                            five = null;
                                            six = null;

                                            tvZy.setText(Zylist.get(position));


                                            //重置后面选项名称
                                            tvDj.setText("等级");
                                            tvQy.setText("地区范围");


                                        }
                                    });

                                    tvZy.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            if (mSpinerZy == null) {
                                                return;
                                            }
                                            mSpinerZy.setWidth(ll.getWidth());
                                            mSpinerZy.setHeight(ll.getHeight()/2);
                                            mSpinerZy.showAsDropDown(view);
                                            setTextImage(R.id.tv_zy, R.mipmap.up);
                                        }
                                    });

                                    mSpinerZy.setOnDismissListener(new PopupWindow.OnDismissListener() {
                                        @Override
                                        public void onDismiss() {
                                            //TODO
                                            setTextImage(R.id.tv_zy, R.mipmap.down);
                                        }
                                    });


                                }
                            } else if ("QY_DJ".equals(nodeName)) {
                                final boolean visible = object.getBoolean("visible");
                                final JSONArray options = object.getJSONArray("options");
                                if (!visible) {
                                    tvDj.setVisibility(View.GONE);
                                    final JSONObject objectOptions = options.getJSONObject(0);
                                    final String value = objectOptions.getString("value");
                                    djId = value;
                                } else {
                                    if (Djlist.size() > 0) {
                                        Djlist.clear();
                                    }
                                    for (int j = 0; j < options.size(); j++) {
                                        final JSONObject objectOptions = options.getJSONObject(j);

                                        final String text = objectOptions.getString("text");

                                        Djlist.add(text);
                                    }

                                    mSpinerDj = new SpinerPopWindow<String>(getActivity(), Djlist, new AdapterView.OnItemClickListener() {
                                        @Override
                                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                            mSpinerDj.dismiss();
                                            five = Djlist.get(position);
                                            djId = options.getJSONObject(position).getString("value");
                                            six = null;
                                            tvDj.setText(Djlist.get(position));
                                            //重置后面选项名称
                                            tvQy.setText("地区范围");
                                        }
                                    });

                                    tvDj.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            if (mSpinerDj == null) {
                                                return;
                                            }
                                            mSpinerDj.setWidth(ll.getWidth());
                                            mSpinerDj.setHeight(ll.getHeight()/2);
                                            mSpinerDj.showAsDropDown(view);
                                            setTextImage(R.id.tv_dj, R.mipmap.up);
                                        }
                                    });

                                    mSpinerDj.setOnDismissListener(new PopupWindow.OnDismissListener() {
                                        @Override
                                        public void onDismiss() {
                                            //TODO
                                            setTextImage(R.id.tv_dj, R.mipmap.down);
                                        }
                                    });

                                }
                            } else if ("QY_DQ".equals(nodeName)) {
                                final boolean visible = object.getBoolean("visible");
                                final JSONArray options = object.getJSONArray("options");
                                if (!visible) {
                                    tvQy.setVisibility(View.GONE);
                                    if (options.size() > 0) {
                                        final JSONObject objectOptions = options.getJSONObject(0);
                                        final String value = objectOptions.getString("value");
                                        entrySign = value;
                                    }
                                } else {
                                    if (Qylist.size() > 0) {
                                        Qylist.clear();
                                    }
                                    for (int j = 0; j < options.size(); j++) {
                                        final JSONObject objectOptions = options.getJSONObject(j);

                                        final String text = objectOptions.getString("text");
                                        Log.d("DBAHSBDHASDSA", "area==" + text);
                                        Qylist.add(text);
                                    }

                                    mSpinerQy = new SpinerPopWindow<String>(getActivity(), Qylist, new AdapterView.OnItemClickListener() {
                                        @Override
                                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                            mSpinerQy.dismiss();
                                            six = Qylist.get(position);
                                            entrySign = options.getJSONObject(position).getString("value");
                                            tvQy.setText(Qylist.get(position));

                                        }
                                    });

                                    tvQy.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            if (mSpinerQy == null) {
                                                return;
                                            }
                                            mSpinerQy.setWidth(ll.getWidth());
                                            mSpinerQy.setHeight(ll.getHeight()/2);
                                            mSpinerQy.showAsDropDown(view);
                                            setTextImage(R.id.tv_qy, R.mipmap.up);
                                        }
                                    });

                                    mSpinerQy.setOnDismissListener(new PopupWindow.OnDismissListener() {
                                        @Override
                                        public void onDismiss() {
                                            setTextImage(R.id.tv_qy, R.mipmap.down);
                                        }
                                    });
                                }
                            }
                        }

                    }

                });

    }

    /**
     * 加载小类列表
     */
    public void loadXL(final String provinceCode, String nodeName, String value, String text) {

        OkGo.<String>post(BiaoXunTongApi.URL_GETQYZZLIST)
                .params("provinceCode", provinceCode)
                .params("nodeName", nodeName)
                .params("value", value)
                .params("text", text)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {

                        final JSONArray array = JSON.parseArray(response.body());

                        for (int i = 0; i < array.size(); i++) {
                            final JSONObject object = array.getJSONObject(i);
                            final String nodeName = object.getString("nodeName");

                            if ("QY_XL".equals(nodeName)) {
                                final boolean visible = object.getBoolean("visible");
                                final JSONArray options = object.getJSONArray("options");

                                if (!visible) {
                                    tvXl.setVisibility(View.GONE);
                                    final JSONObject objectOptions = options.getJSONObject(0);
                                    final String value = objectOptions.getString("value");
                                    xlId = value;
                                } else {
                                    for (int j = 0; j < options.size(); j++) {
                                        final JSONObject objectOptions = options.getJSONObject(j);

                                        final String text = objectOptions.getString("text");

                                        Xllist.add(text);
                                    }

                                    mSpinerXl = new SpinerPopWindow<String>(getActivity(), Xllist, new AdapterView.OnItemClickListener() {
                                        @Override
                                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                            mSpinerXl.dismiss();
                                            three = Xllist.get(position);
                                            xlId = options.getJSONObject(position).getString("value");
                                            four = null;
                                            five = null;
                                            six = null;


                                            tvXl.setText(Xllist.get(position));
                                            tvZy.setVisibility(View.VISIBLE);

                                            //重置后面选项名称
                                            tvZy.setText("专业");
                                            tvDj.setText("等级");
                                            tvQy.setText("地区范围");


                                            String value = options.getJSONObject(position).getString("value");
                                            String text = options.getJSONObject(position).getString("text");

                                            loadZY(provinceCode, nodeName, value, text);

                                        }
                                    });

                                    tvXl.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            if (mSpinerXl == null) {
                                                return;
                                            }
                                            mSpinerXl.setWidth(ll.getWidth());
                                            mSpinerXl.setHeight(ll.getHeight()/2);
                                            mSpinerXl.showAsDropDown(view);
                                            setTextImage(R.id.tv_xl, R.mipmap.up);
                                        }
                                    });

                                    mSpinerXl.setOnDismissListener(new PopupWindow.OnDismissListener() {
                                        @Override
                                        public void onDismiss() {
                                            //TODO
                                            setTextImage(R.id.tv_xl, R.mipmap.down);
                                        }
                                    });
                                }
                            } else if ("QY_ZY".equals(nodeName)) {
                                final boolean visible = object.getBoolean("visible");
                                final JSONArray options = object.getJSONArray("options");
                                if (!visible) {
                                    tvZy.setVisibility(View.GONE);
                                    final JSONObject objectOptions = options.getJSONObject(0);
                                    final String value = objectOptions.getString("value");
                                    zyId = value;
                                } else {
                                    if (Zylist.size() > 0) {
                                        Zylist.clear();
                                    }

                                    for (int j = 0; j < options.size(); j++) {
                                        final JSONObject objectOptions = options.getJSONObject(j);

                                        final String text = objectOptions.getString("text");

                                        Zylist.add(text);
                                    }

                                    mSpinerZy = new SpinerPopWindow<String>(getActivity(), Zylist, new AdapterView.OnItemClickListener() {
                                        @Override
                                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                            mSpinerZy.dismiss();
                                            four = Zylist.get(position);
                                            zyId = options.getJSONObject(position).getString("value");
                                            five = null;
                                            six = null;

                                            tvZy.setText(Zylist.get(position));
                                            //重置后面选项名称
                                            tvDj.setText("等级");
                                            tvQy.setText("地区范围");


                                        }
                                    });

                                    tvZy.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            if (mSpinerZy == null) {
                                                return;
                                            }
                                            mSpinerZy.setWidth(ll.getWidth());
                                            mSpinerZy.setHeight(ll.getHeight()/2);
                                            mSpinerZy.showAsDropDown(view);
                                            setTextImage(R.id.tv_zy, R.mipmap.up);
                                        }
                                    });

                                    mSpinerZy.setOnDismissListener(new PopupWindow.OnDismissListener() {
                                        @Override
                                        public void onDismiss() {
                                            //TODO
                                            setTextImage(R.id.tv_zy, R.mipmap.down);
                                        }
                                    });


                                }
                            } else if ("QY_DJ".equals(nodeName)) {
                                final boolean visible = object.getBoolean("visible");
                                final JSONArray options = object.getJSONArray("options");
                                if (!visible) {
                                    tvDj.setVisibility(View.GONE);
                                    final JSONObject objectOptions = options.getJSONObject(0);
                                    final String value = objectOptions.getString("value");
                                    djId = value;
                                } else {
                                    if (Djlist.size() > 0) {
                                        Djlist.clear();
                                    }
                                    for (int j = 0; j < options.size(); j++) {
                                        final JSONObject objectOptions = options.getJSONObject(j);

                                        final String text = objectOptions.getString("text");

                                        Djlist.add(text);
                                    }

                                    mSpinerDj = new SpinerPopWindow<String>(getActivity(), Djlist, new AdapterView.OnItemClickListener() {
                                        @Override
                                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                            mSpinerDj.dismiss();
                                            five = Djlist.get(position);
                                            djId = options.getJSONObject(position).getString("value");
                                            six = null;
                                            tvDj.setText(Djlist.get(position));

                                            //重置后面选项名称
                                            tvQy.setText("地区范围");
                                        }
                                    });

                                    tvDj.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            if (mSpinerDj == null) {
                                                return;
                                            }
                                            mSpinerDj.setWidth(ll.getWidth());
                                            mSpinerDj.setHeight(ll.getHeight()/2);
                                            mSpinerDj.showAsDropDown(view);
                                            setTextImage(R.id.tv_dj, R.mipmap.up);
                                        }
                                    });

                                    mSpinerDj.setOnDismissListener(new PopupWindow.OnDismissListener() {
                                        @Override
                                        public void onDismiss() {
                                            //TODO
                                            setTextImage(R.id.tv_dj, R.mipmap.down);
                                        }
                                    });

                                }
                            } else if ("QY_DQ".equals(nodeName)) {
                                final boolean visible = object.getBoolean("visible");
                                final JSONArray options = object.getJSONArray("options");
                                if (!visible) {
                                    tvQy.setVisibility(View.GONE);
                                    if (options.size() > 0) {
                                        final JSONObject objectOptions = options.getJSONObject(0);
                                        final String value = objectOptions.getString("value");
                                        entrySign = value;
                                    }
                                } else {
                                    if (Qylist.size() > 0) {
                                        Qylist.clear();
                                    }
                                    for (int j = 0; j < options.size(); j++) {
                                        final JSONObject objectOptions = options.getJSONObject(j);

                                        final String text = objectOptions.getString("text");

                                        Qylist.add(text);
                                    }

                                    mSpinerQy = new SpinerPopWindow<String>(getActivity(), Qylist, new AdapterView.OnItemClickListener() {
                                        @Override
                                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                            mSpinerQy.dismiss();
                                            six = Qylist.get(position);
                                            entrySign = options.getJSONObject(position).getString("value");
                                            tvQy.setText(Qylist.get(position));

                                        }
                                    });

                                    tvQy.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            if (mSpinerQy == null) {
                                                return;
                                            }
                                            mSpinerQy.setWidth(ll.getWidth());
                                            mSpinerQy.setHeight(ll.getHeight()/2);
                                            mSpinerQy.showAsDropDown(view);
                                            setTextImage(R.id.tv_qy, R.mipmap.up);
                                        }
                                    });

                                    mSpinerQy.setOnDismissListener(new PopupWindow.OnDismissListener() {
                                        @Override
                                        public void onDismiss() {
                                            setTextImage(R.id.tv_qy, R.mipmap.down);
                                        }
                                    });
                                }
                            }
                        }


                    }
                });

    }

    /**
     * 加载专业列表
     */
    public void loadZY(String provinceCode, String nodeName, String value, String text) {

        OkGo.<String>post(BiaoXunTongApi.URL_GETQYZZLIST)
                .params("provinceCode", provinceCode)
                .params("nodeName", nodeName)
                .params("value", value)
                .params("text", text)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {

                        final JSONArray array = JSON.parseArray(response.body());

                        for (int i = 0; i < array.size(); i++) {
                            final JSONObject object = array.getJSONObject(i);
                            final String nodeName = object.getString("nodeName");

                            if ("QY_ZY".equals(nodeName)) {
                                final boolean visible = object.getBoolean("visible");
                                final JSONArray options = object.getJSONArray("options");
                                if (!visible) {
                                    tvZy.setVisibility(View.GONE);
                                    final JSONObject objectOptions = options.getJSONObject(0);
                                    final String value = objectOptions.getString("value");
                                    zyId = value;
                                } else {

                                    for (int j = 0; j < options.size(); j++) {
                                        final JSONObject objectOptions = options.getJSONObject(j);

                                        final String text = objectOptions.getString("text");

                                        Zylist.add(text);
                                    }


                                    mSpinerZy = new SpinerPopWindow<String>(getActivity(), Zylist, new AdapterView.OnItemClickListener() {
                                        @Override
                                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                            mSpinerZy.dismiss();
                                            four = Zylist.get(position);
                                            zyId = options.getJSONObject(position).getString("value");
                                            five = null;
                                            six = null;

                                            tvZy.setText(Zylist.get(position));

                                            //重置后面选项名称
                                            tvDj.setText("等级");
                                            tvQy.setText("地区范围");


                                        }
                                    });

                                    tvZy.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            if (mSpinerZy == null) {
                                                return;
                                            }
                                            mSpinerZy.setWidth(ll.getWidth());
                                            mSpinerZy.setHeight(ll.getHeight()/2);
                                            mSpinerZy.showAsDropDown(view);
                                            setTextImage(R.id.tv_zy, R.mipmap.up);
                                        }
                                    });

                                    mSpinerZy.setOnDismissListener(new PopupWindow.OnDismissListener() {
                                        @Override
                                        public void onDismiss() {
                                            //TODO
                                            setTextImage(R.id.tv_zy, R.mipmap.down);
                                        }
                                    });

                                }
                            } else if ("QY_DJ".equals(nodeName)) {
                                final boolean visible = object.getBoolean("visible");
                                final JSONArray options = object.getJSONArray("options");
                                if (!visible) {
                                    tvDj.setVisibility(View.GONE);
                                    final JSONObject objectOptions = options.getJSONObject(0);
                                    final String value = objectOptions.getString("value");
                                    djId = value;
                                } else {
                                    if (Djlist.size() > 0) {
                                        Djlist.clear();
                                    }
                                    for (int j = 0; j < options.size(); j++) {
                                        final JSONObject objectOptions = options.getJSONObject(j);

                                        final String text = objectOptions.getString("text");

                                        Djlist.add(text);
                                    }

                                    mSpinerDj = new SpinerPopWindow<String>(getActivity(), Djlist, new AdapterView.OnItemClickListener() {
                                        @Override
                                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                            mSpinerDj.dismiss();
                                            five = Djlist.get(position);
                                            djId = options.getJSONObject(position).getString("value");
                                            six = null;

                                            tvDj.setText(Djlist.get(position));
                                            //重置后面选项名称
                                            tvQy.setText("地区范围");
                                        }
                                    });

                                    tvDj.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            if (mSpinerDj == null) {
                                                return;
                                            }
                                            mSpinerDj.setWidth(ll.getWidth());
                                            mSpinerDj.setHeight(ll.getHeight()/2);
                                            mSpinerDj.showAsDropDown(view);
                                            setTextImage(R.id.tv_dj, R.mipmap.up);
                                        }
                                    });

                                    mSpinerDj.setOnDismissListener(new PopupWindow.OnDismissListener() {
                                        @Override
                                        public void onDismiss() {
                                            //TODO
                                            setTextImage(R.id.tv_dj, R.mipmap.down);
                                        }
                                    });

                                }
                            } else if ("QY_DQ".equals(nodeName)) {
                                final boolean visible = object.getBoolean("visible");
                                final JSONArray options = object.getJSONArray("options");
                                if (!visible) {
                                    tvQy.setVisibility(View.GONE);
                                    if (options.size() > 0) {
                                        final JSONObject objectOptions = options.getJSONObject(0);
                                        final String value = objectOptions.getString("value");
                                        entrySign = value;
                                    }
                                } else {
                                    if (Qylist.size() > 0) {
                                        Qylist.clear();
                                    }
                                    for (int j = 0; j < options.size(); j++) {
                                        final JSONObject objectOptions = options.getJSONObject(j);

                                        final String text = objectOptions.getString("text");

                                        Qylist.add(text);
                                    }

                                    mSpinerQy = new SpinerPopWindow<String>(getActivity(), Qylist, new AdapterView.OnItemClickListener() {
                                        @Override
                                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                            mSpinerQy.dismiss();

                                            six = Qylist.get(position);
                                            entrySign = options.getJSONObject(position).getString("value");
                                            tvQy.setText(Qylist.get(position));


                                        }
                                    });

                                    tvQy.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            if (mSpinerQy == null) {
                                                return;
                                            }
                                            mSpinerQy.setWidth(ll.getWidth());
                                            mSpinerQy.setHeight(ll.getHeight()/2);
                                            mSpinerQy.showAsDropDown(view);
                                            setTextImage(R.id.tv_qy, R.mipmap.up);
                                        }
                                    });

                                    mSpinerQy.setOnDismissListener(new PopupWindow.OnDismissListener() {
                                        @Override
                                        public void onDismiss() {
                                            setTextImage(R.id.tv_qy, R.mipmap.down);
                                        }
                                    });
                                }
                            }

                        }

                    }
                });

    }


}
