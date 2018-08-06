package com.lubanjianye.biaoxuntong.ui.main.index;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.AppCompatTextView;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.flyco.tablayout.SlidingTabLayout;
import com.igexin.sdk.PushManager;
import com.lubanjianye.biaoxuntong.R;
import com.lubanjianye.biaoxuntong.app.BiaoXunTong;
import com.lubanjianye.biaoxuntong.base.BaseFragment1;
import com.lubanjianye.biaoxuntong.database.DatabaseManager;
import com.lubanjianye.biaoxuntong.database.UserProfile;
import com.lubanjianye.biaoxuntong.eventbus.EventMessage;
import com.lubanjianye.biaoxuntong.app.BiaoXunTongApi;
import com.lubanjianye.biaoxuntong.ui.citypicker.model.LocateState;
import com.lubanjianye.biaoxuntong.ui.search.activity.SearchActivity;
import com.lubanjianye.biaoxuntong.ui.sign.SignInActivity;
import com.lubanjianye.biaoxuntong.ui.citypicker.CityPicker;
import com.lubanjianye.biaoxuntong.ui.citypicker.adapter.OnPickListener;
import com.lubanjianye.biaoxuntong.ui.citypicker.model.City;
import com.lubanjianye.biaoxuntong.ui.citypicker.model.HotCity;
import com.lubanjianye.biaoxuntong.ui.citypicker.model.LocatedCity;
import com.lubanjianye.biaoxuntong.ui.main.index.sortcolumn.SortColumnActivity;
import com.lubanjianye.biaoxuntong.ui.update.UpdateAppBean;
import com.lubanjianye.biaoxuntong.ui.update.UpdateAppManager;
import com.lubanjianye.biaoxuntong.ui.update.UpdateCallback;
import com.lubanjianye.biaoxuntong.ui.update.utils.OkGoUpdateHttpUtil;
import com.lubanjianye.biaoxuntong.util.appinfo.AppApplicationMgr;
import com.lubanjianye.biaoxuntong.util.dialog.DialogHelper;
import com.lubanjianye.biaoxuntong.util.dialog.PromptButton;
import com.lubanjianye.biaoxuntong.util.dialog.PromptButtonListener;
import com.lubanjianye.biaoxuntong.util.dialog.PromptDialog;
import com.lubanjianye.biaoxuntong.util.netStatus.NetUtil;
import com.lubanjianye.biaoxuntong.util.sp.AppSharePreferenceMgr;
import com.lubanjianye.biaoxuntong.util.toast.ToastUtil;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.cache.CacheMode;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.EasyPermissions;

import static com.lubanjianye.biaoxuntong.app.BiaoXunTong.getApplicationContext;


public class IndexTabFragment extends BaseFragment1 implements View.OnClickListener, BDLocationListener, EasyPermissions.PermissionCallbacks {

    private SlidingTabLayout indexStlTab = null;
    private ViewPager indexVp = null;
    private LinearLayout llSearch = null;
    private ImageView ivAdd = null;

    private List<HotCity> hotCities;

    public LocationClient mLocationClient = null;

    private String clientID = PushManager.getInstance().getClientid(getApplicationContext());

    private IndexFragmentAdapter mAdapter = null;

    private final List<String> mList = new ArrayList<String>();


    private long userId = 0;
    private PromptDialog promptDialog;

    private boolean isInitCache = false;

    private LinearLayout ll_location = null;
    private AppCompatTextView tv_location = null;


    private String locationArea = "";
    private String locationCode = "";

    private String provinceCode = "";

    @Override
    public Object setLayout() {
        return R.layout.fragment_main_index;
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();

        //取消注册EventBus
        EventBus.getDefault().unregister(this);
    }

    @Override
    public void initView() {

        //注册EventBus
        EventBus.getDefault().register(this);

        //创建对象
        promptDialog = new PromptDialog(getActivity());

        indexStlTab = getView().findViewById(R.id.index_stl_tab);
        indexVp = getView().findViewById(R.id.index_vp);
        llSearch = getView().findViewById(R.id.ll_search);
        ivAdd = getView().findViewById(R.id.iv_add);

        llSearch.setOnClickListener(this);
        ivAdd.setOnClickListener(this);

        ll_location = getView().findViewById(R.id.ll_location);
        tv_location = getView().findViewById(R.id.tv_location);
        ll_location.setOnClickListener(this);


        //定位相关
        mLocationClient = new LocationClient(getApplicationContext());
        //声明LocationClient类
        mLocationClient.registerLocationListener(this);
        //注册监听函数
        LocationClientOption option = new LocationClientOption();

        option.setIsNeedAddress(true);
        //可选，是否需要地址信息，默认为不需要，即参数为false
        //如果开发者需要获得当前点的地址信息，此处必须为true
        mLocationClient.setLocOption(option);


        if (AppSharePreferenceMgr.contains(getApplicationContext(), EventMessage.LOCA_AREA)) {
            String area = (String) AppSharePreferenceMgr.get(getApplicationContext(), EventMessage.LOCA_AREA, "");
            tv_location.setText(area);

        } else {
            tv_location.setText("四川");
        }

        if (AppSharePreferenceMgr.contains(getApplicationContext(), EventMessage.LOCA_AREA_CODE)) {
            provinceCode = (String) AppSharePreferenceMgr.get(getApplicationContext(), EventMessage.LOCA_AREA_CODE, "");
        } else {
            provinceCode = "510000";
        }

    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void XXXXXX(EventMessage message) {

        if (EventMessage.LOGIN_SUCCSS.equals(message.getMessage()) || EventMessage.LOGIN_OUT.equals(message.getMessage())
                || EventMessage.TAB_CHANGE.equals(message.getMessage())) {
            //更新UI
            if (indexStlTab != null) {
                indexStlTab.setCurrentTab(0);
                indexVp.setCurrentItem(0);
                indexStlTab.setViewPager(indexVp);
                indexStlTab.notifyDataSetChanged();
            }
            requestData(true);
        }
    }

    @Override
    public void initData() {

        requestData(false);

    }

    @Override
    public void initEvent() {
        //热门城市
        hotCities = new ArrayList<>();
        hotCities.add(new HotCity("四川", "四川", "510000"));
        hotCities.add(new HotCity("重庆", "重庆", "500000"));


        requestStorage_location();


        if (AppSharePreferenceMgr.contains(getContext(), EventMessage.TOKEN_FALSE)) {

            //token失效，需要重新登录
            final PromptButton cancel = new PromptButton("取      消", new PromptButtonListener() {
                @Override
                public void onClick(PromptButton button) {
                    AppSharePreferenceMgr.remove(getContext(), EventMessage.TOKEN_FALSE);
                }
            });
            cancel.setTextColor(Color.parseColor("#cccc33"));
            cancel.setTextSize(16);

            final PromptButton toLogin = new PromptButton("重新登陆", new PromptButtonListener() {
                @Override
                public void onClick(PromptButton button) {
                    AppSharePreferenceMgr.remove(getContext(), EventMessage.TOKEN_FALSE);
                    startActivity(new Intent(getActivity(), SignInActivity.class));
                }
            });
            toLogin.setTextColor(Color.parseColor("#00bfdc"));
            toLogin.setTextSize(16);
            promptDialog.getAlertDefaultBuilder().withAnim(true).cancleAble(false).touchAble(false)
                    .round(4).loadingDuration(600);
            promptDialog.showWarnAlert("账号登陆过期、请重新登录!", toLogin, cancel, true);
        }

    }


    public void requestData(final boolean isRefresh) {

        String mDiqu = tv_location.getText().toString();

        //保存地区
        AppSharePreferenceMgr.put(getApplicationContext(), EventMessage.LOCA_AREA, mDiqu);
        //保存地区code
        AppSharePreferenceMgr.put(getApplicationContext(), EventMessage.LOCA_AREA_CODE, provinceCode);


        if (AppSharePreferenceMgr.contains(getContext(), EventMessage.LOGIN_SUCCSS)) {
            //得到用户userId
            List<UserProfile> users = DatabaseManager.getInstance().getDao().loadAll();
            for (int i = 0; i < users.size(); i++) {
                userId = users.get(0).getId();
            }

            if (isRefresh) {
                OkGo.<String>post(BiaoXunTongApi.URL_INDEXTAB)
                        .params("userId", userId)
                        .params("clientId", clientID)
                        .params("diqu", mDiqu)
                        .cacheKey("index_tab_cache_login" + userId + mDiqu)
                        .cacheMode(CacheMode.REQUEST_FAILED_READ_CACHE)
                        .cacheTime(3600 * 48000)
                        .execute(new StringCallback() {
                            @Override
                            public void onSuccess(Response<String> response) {

                                final JSONObject object = JSON.parseObject(response.body());
                                String status = object.getString("status");
                                String message = object.getString("message");

                                if ("200".equals(status)) {
                                    final JSONArray ownerList = object.getJSONArray("data");
                                    if (mList.size() > 0) {
                                        mList.clear();
                                    }

                                    for (int i = 0; i < ownerList.size(); i++) {
                                        final JSONObject list = ownerList.getJSONObject(i);
                                        String name = list.getString("name");
                                        mList.add(name);
                                    }

                                    setUI(mList);

                                } else {
                                    ToastUtil.shortToast(getContext(), message);
                                }
                            }

                            @Override
                            public void onCacheSuccess(Response<String> response) {
                                if (!isInitCache) {
                                    final JSONObject object = JSON.parseObject(response.body());
                                    String status = object.getString("status");
                                    String message = object.getString("message");

                                    if ("200".equals(status)) {
                                        final JSONArray ownerList = object.getJSONArray("data");
                                        if (mList.size() > 0) {
                                            mList.clear();
                                        }

                                        for (int i = 0; i < ownerList.size(); i++) {
                                            final JSONObject list = ownerList.getJSONObject(i);
                                            String name = list.getString("name");
                                            mList.add(name);
                                        }

                                        setUI(mList);

                                    } else {
                                        ToastUtil.shortToast(getContext(), message);
                                    }
                                    isInitCache = true;
                                }
                            }
                        });
            } else {

                OkGo.<String>post(BiaoXunTongApi.URL_INDEXTAB)
                        .params("userId", userId)
                        .params("clientId", clientID)
                        .params("diqu", mDiqu)
                        .cacheKey("index_tab_cache_login" + userId + mDiqu)
                        .cacheMode(CacheMode.IF_NONE_CACHE_REQUEST)
                        .cacheTime(3600 * 48000)
                        .execute(new StringCallback() {
                            @Override
                            public void onSuccess(Response<String> response) {

                                final JSONObject object = JSON.parseObject(response.body());
                                String status = object.getString("status");
                                String message = object.getString("message");

                                if ("200".equals(status)) {
                                    final JSONArray ownerList = object.getJSONArray("data");
                                    if (mList.size() > 0) {
                                        mList.clear();
                                    }

                                    for (int i = 0; i < ownerList.size(); i++) {
                                        final JSONObject list = ownerList.getJSONObject(i);
                                        String name = list.getString("name");
                                        mList.add(name);
                                    }

                                    setUI(mList);

                                } else {
                                    ToastUtil.shortToast(getContext(), message);
                                }
                            }

                            @Override
                            public void onCacheSuccess(Response<String> response) {
                                if (!isInitCache) {
                                    final JSONObject object = JSON.parseObject(response.body());
                                    String status = object.getString("status");
                                    String message = object.getString("message");

                                    if ("200".equals(status)) {
                                        final JSONArray ownerList = object.getJSONArray("data");
                                        if (mList.size() > 0) {
                                            mList.clear();
                                        }

                                        for (int i = 0; i < ownerList.size(); i++) {
                                            final JSONObject list = ownerList.getJSONObject(i);
                                            String name = list.getString("name");
                                            mList.add(name);
                                        }

                                        setUI(mList);

                                    } else {
                                        ToastUtil.shortToast(getContext(), message);
                                    }
                                    isInitCache = true;
                                }
                            }
                        });
            }


        } else {

            if (isRefresh) {
                OkGo.<String>post(BiaoXunTongApi.URL_INDEXTAB)
                        .params("clientId", clientID)
                        .params("diqu", mDiqu)
                        .cacheKey("index_tab_cache_no_login" + userId + mDiqu)
                        .cacheMode(CacheMode.REQUEST_FAILED_READ_CACHE)
                        .cacheTime(3600 * 48000)
                        .execute(new StringCallback() {
                            @Override
                            public void onSuccess(Response<String> response) {
                                final JSONObject object = JSON.parseObject(response.body());
                                String status = object.getString("status");
                                String message = object.getString("message");

                                if ("200".equals(status)) {
                                    final JSONArray ownerList = object.getJSONArray("data");
                                    if (mList.size() > 0) {
                                        mList.clear();
                                    }

                                    for (int i = 0; i < ownerList.size(); i++) {
                                        final JSONObject list = ownerList.getJSONObject(i);
                                        String name = list.getString("name");
                                        mList.add(name);
                                    }

                                    setUI(mList);
                                } else {
                                    ToastUtil.shortToast(getContext(), message);
                                }
                            }

                            @Override
                            public void onCacheSuccess(Response<String> response) {
                                if (!isInitCache) {
                                    final JSONObject object = JSON.parseObject(response.body());
                                    String status = object.getString("status");
                                    String message = object.getString("message");

                                    if ("200".equals(status)) {
                                        final JSONArray ownerList = object.getJSONArray("data");
                                        if (mList.size() > 0) {
                                            mList.clear();
                                        }

                                        for (int i = 0; i < ownerList.size(); i++) {
                                            final JSONObject list = ownerList.getJSONObject(i);
                                            String name = list.getString("name");
                                            mList.add(name);
                                        }

                                        setUI(mList);

                                    } else {
                                        ToastUtil.shortToast(getContext(), message);
                                    }
                                    isInitCache = true;
                                }
                            }
                        });
            } else {
                OkGo.<String>post(BiaoXunTongApi.URL_INDEXTAB)
                        .params("clientId", clientID)
                        .params("diqu", mDiqu)
                        .cacheKey("index_tab_cache_no_login" + userId + mDiqu)
                        .cacheMode(CacheMode.IF_NONE_CACHE_REQUEST)
                        .cacheTime(3600 * 48000)
                        .execute(new StringCallback() {
                            @Override
                            public void onSuccess(Response<String> response) {
                                final JSONObject object = JSON.parseObject(response.body());
                                String status = object.getString("status");
                                String message = object.getString("message");

                                if ("200".equals(status)) {
                                    final JSONArray ownerList = object.getJSONArray("data");
                                    if (mList.size() > 0) {
                                        mList.clear();
                                    }

                                    for (int i = 0; i < ownerList.size(); i++) {
                                        final JSONObject list = ownerList.getJSONObject(i);
                                        String name = list.getString("name");
                                        mList.add(name);
                                    }

                                    setUI(mList);
                                } else {
                                    ToastUtil.shortToast(getContext(), message);
                                }
                            }

                            @Override
                            public void onCacheSuccess(Response<String> response) {
                                if (!isInitCache) {
                                    final JSONObject object = JSON.parseObject(response.body());
                                    String status = object.getString("status");
                                    String message = object.getString("message");

                                    if ("200".equals(status)) {
                                        final JSONArray ownerList = object.getJSONArray("data");
                                        if (mList.size() > 0) {
                                            mList.clear();
                                        }

                                        for (int i = 0; i < ownerList.size(); i++) {
                                            final JSONObject list = ownerList.getJSONObject(i);
                                            String name = list.getString("name");
                                            mList.add(name);
                                        }

                                        setUI(mList);

                                    } else {
                                        ToastUtil.shortToast(getContext(), message);
                                    }
                                    isInitCache = true;
                                }
                            }
                        });
            }


        }

    }


    private void setUI(List<String> mList) {
        mAdapter = new IndexFragmentAdapter(getContext(), getChildFragmentManager(), mList);
        indexVp.setAdapter(mAdapter);
        indexStlTab.setViewPager(indexVp);
        mAdapter.notifyDataSetChanged();

    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ll_search:
                //点击搜素
                Intent intent = new Intent(getActivity(), SearchActivity.class);
                intent.putExtra("searchTye", 1);
                startActivity(intent);
                break;
            case R.id.iv_add:
                startActivity(new Intent(getActivity(), SortColumnActivity.class));
                break;
            case R.id.ll_location:
                if (!TextUtils.isEmpty(locationArea)) {

                    CityPicker.getInstance()
                            .setFragmentManager(getFragmentManager())
                            .enableAnimation(true)
                            .setLocatedCity(new LocatedCity(locationArea, locationArea, locationCode))
                            .setHotCities(hotCities)
                            .setOnPickListener(new OnPickListener() {
                                @Override
                                public void onPick(int position, City data) {

                                    if (data != null) {

                                        if (!data.getName().equals(tv_location.getText().toString())) {
                                            tv_location.setText(String.format("%s", data.getName()));
                                            provinceCode = data.getCode();
                                            if (indexStlTab != null) {
                                                indexStlTab.setCurrentTab(0);
                                                indexStlTab.setViewPager(indexVp);
                                                indexStlTab.notifyDataSetChanged();
                                            }
                                            requestData(true);
                                            EventBus.getDefault().post(new EventMessage(EventMessage.LOCA_AREA_CHANGE));
                                        }


                                    } else {
                                        tv_location.setText(tv_location.getText().toString());
                                    }


                                }

                                @Override
                                public void onLocate() {
                                    AppSharePreferenceMgr.remove(getContext(), EventMessage.IF_ASK_LOCATION);
                                    //开始定位，这里模拟一下定位
                                    setArea = true;
                                    locationTask();

                                }

                            })
                            .show();
                } else {
                    CityPicker.getInstance()
                            .setFragmentManager(getFragmentManager())
                            .enableAnimation(true)
                            .setHotCities(hotCities)
                            .setOnPickListener(new OnPickListener() {
                                @Override
                                public void onPick(int position, City data) {


                                    if (data != null) {
                                        if (!data.getName().equals(tv_location.getText().toString())) {
                                            tv_location.setText(String.format("%s", data.getName()));
                                            provinceCode = data.getCode();
                                            if (indexStlTab != null) {
                                                indexStlTab.setCurrentTab(0);
                                                indexStlTab.setViewPager(indexVp);
                                                indexStlTab.notifyDataSetChanged();
                                            }
                                            requestData(true);
                                            EventBus.getDefault().post(new EventMessage(EventMessage.LOCA_AREA_CHANGE));
                                        }

                                    } else {
                                        tv_location.setText(tv_location.getText().toString());
                                    }


                                }

                                @Override
                                public void onLocate() {
                                    //开始定位，这里模拟一下定位
                                    AppSharePreferenceMgr.remove(getContext(), EventMessage.IF_ASK_LOCATION);
                                    setArea = true;
                                    locationTask();

                                }
                            })
                            .show();
                }
                break;
            default:
                break;
        }
    }

    private static final int RC_LOCATION_PERM = 123;

    @AfterPermissionGranted(RC_LOCATION_PERM)
    public void locationTask() {
        if (EasyPermissions.hasPermissions(getApplicationContext(), android.Manifest.permission.ACCESS_FINE_LOCATION)) {
            // Have permission, do the thing!
            //开始定位
            mLocationClient.start();
        } else {
            // 没有权限
            EasyPermissions.requestPermissions(this, getString(R.string.rationale_location),
                    RC_LOCATION_PERM, android.Manifest.permission.ACCESS_FINE_LOCATION);

        }

    }

    //定位和存储权限
    private static final int RC_STORAGE_AND_LOCATION = 0x01;

    @AfterPermissionGranted(RC_STORAGE_AND_LOCATION)
    public void requestStorage_location() {
        String[] perms = {Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.READ_EXTERNAL_STORAGE};
        if (EasyPermissions.hasPermissions(getContext(), perms)) {
            // 检查更新
            if (NetUtil.isNetworkConnected(getActivity())) {
                updateDiy();
            }
            //开始定位
            mLocationClient.start();
        } else {
            // 没有权限
            EasyPermissions.requestPermissions(this, getString(R.string.rationale_storage_location),
                    RC_STORAGE_AND_LOCATION, perms);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);

    }


    private boolean setArea = false;

    @Override
    public void onReceiveLocation(BDLocation bdLocation) {
        String province = bdLocation.getProvince();
        locationArea = province.substring(0, province.length() - 1);

        if ("北京".equals(locationArea)) {
            locationCode = "110000";
        } else if ("天津".equals(locationArea)) {
            locationCode = "120000";
        } else if ("河北".equals(locationArea)) {
            locationCode = "130000";
        } else if ("山西".equals(locationArea)) {
            locationCode = "140000";
        } else if ("内蒙古".equals(locationArea)) {
            locationCode = "150000";
        } else if ("辽宁".equals(locationArea)) {
            locationCode = "210000";
        } else if ("吉林".equals(locationArea)) {
            locationCode = "220000";
        } else if ("黑龙江".equals(locationArea)) {
            locationCode = "230000";
        } else if ("上海".equals(locationArea)) {
            locationCode = "310000";
        } else if ("江苏".equals(locationArea)) {
            locationCode = "320000";
        } else if ("浙江".equals(locationArea)) {
            locationCode = "330000";
        } else if ("安徽".equals(locationArea)) {
            locationCode = "340000";
        } else if ("福建".equals(locationArea)) {
            locationCode = "350000";
        } else if ("江西".equals(locationArea)) {
            locationCode = "360000";
        } else if ("山东".equals(locationArea)) {
            locationCode = "370000";
        } else if ("广东".equals(locationArea)) {
            locationCode = "440000";
        } else if ("广西".equals(locationArea)) {
            locationCode = "450000";
        } else if ("海南".equals(locationArea)) {
            locationCode = "460000";
        } else if ("河南".equals(locationArea)) {
            locationCode = "410000";
        } else if ("湖北".equals(locationArea)) {
            locationCode = "420000";
        } else if ("湖南".equals(locationArea)) {
            locationCode = "430000";
        } else if ("重庆".equals(locationArea)) {
            locationCode = "500000";
        } else if ("四川".equals(locationArea)) {
            locationCode = "510000";
        } else if ("贵州".equals(locationArea)) {
            locationCode = "520000";
        } else if ("云南".equals(locationArea)) {
            locationCode = "530000";
        } else if ("西藏".equals(locationArea)) {
            locationCode = "540000";
        } else if ("陕西".equals(locationArea)) {
            locationCode = "610000";
        } else if ("甘肃".equals(locationArea)) {
            locationCode = "620000";
        } else if ("青海".equals(locationArea)) {
            locationCode = "630000";
        } else if ("宁夏".equals(locationArea)) {
            locationCode = "640000";
        } else if ("新疆".equals(locationArea)) {
            locationCode = "650000";
        } else if ("兵团".equals(locationArea)) {
            locationCode = "650000";
        }


        if (!AppSharePreferenceMgr.contains(getApplicationContext(), EventMessage.NO_CHANGE_AREA)) {
            String area = tv_location.getText().toString();
            if (!area.equals(locationArea)) {
                //是否切换地区
                final PromptButton cancel = new PromptButton("取      消", new PromptButtonListener() {
                    @Override
                    public void onClick(PromptButton button) {
                        AppSharePreferenceMgr.put(getApplicationContext(), EventMessage.NO_CHANGE_AREA, "no_change_area");
                    }
                });
                cancel.setTextColor(getResources().getColor(R.color.status_text_color));
                cancel.setTextSize(16);

                final PromptButton sure = new PromptButton("切      换", new PromptButtonListener() {
                    @Override
                    public void onClick(PromptButton button) {
                        //确认切换地区，刷新数据
                        tv_location.setText(locationArea);

                        //更新UI
                        if (indexStlTab != null) {
                            indexStlTab.setCurrentTab(0);
                            indexStlTab.setViewPager(indexVp);
                            indexStlTab.notifyDataSetChanged();
                        }

                        provinceCode = locationCode;

                        requestData(true);

                        EventBus.getDefault().post(new EventMessage(EventMessage.LOCA_AREA_CHANGE));
                    }
                });
                sure.setTextColor(getResources().getColor(R.color.main_status_blue));
                sure.setTextSize(16);
                promptDialog.getAlertDefaultBuilder().withAnim(true).cancleAble(false).touchAble(false)
                        .round(4).loadingDuration(600);
                promptDialog.showWarnAlert("当前定位为" + locationArea + "," + "是否切换到" + locationArea + "?", cancel, sure, true);
            }
        }

        if (setArea) {
            CityPicker.getInstance().locateComplete(new LocatedCity(locationArea, locationArea, locationCode), locationArea == null ? LocateState.FAILURE : LocateState.SUCCESS);
        }

    }

    @Override
    public void onPermissionsGranted(int requestCode, @NonNull List<String> perms) {
        mLocationClient.start();
    }

    @Override
    public void onPermissionsDenied(int requestCode, @NonNull List<String> perms) {

        if (!AppSharePreferenceMgr.contains(getContext(), EventMessage.IF_ASK_LOCATION)) {
            if (EasyPermissions.somePermissionPermanentlyDenied(this, perms)) {
                DialogHelper.getConfirmDialog(getContext(), "温馨提示", "你已多次拒绝定位权限，为了得到更好的服务，请到设置中开启定位权限", "去设置", "取消", false, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        startActivity(new Intent(Settings.ACTION_APPLICATION_SETTINGS));
                    }
                }, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        AppSharePreferenceMgr.put(getContext(), EventMessage.IF_ASK_LOCATION, "no_location");
                    }
                }).show();
            }
        }


    }

    /**
     * 自定义接口协议
     */
    public void updateDiy() {

        int versionCode = AppApplicationMgr.getVersionCode(BiaoXunTong.getApplicationContext());

        Map<String, String> params = new HashMap<String, String>();

        params.put("versionCode", String.valueOf(versionCode));


        new UpdateAppManager
                .Builder()
                //必须设置，当前Activity
                .setActivity(getActivity())
                //必须设置，实现httpManager接口的对象
                .setHttpManager(new OkGoUpdateHttpUtil())
                //必须设置，更新地址
                .setUpdateUrl(BiaoXunTongApi.URL_UPDATE)

                //以下设置，都是可选
                //设置请求方式，默认get
                .setPost(true)
                //不显示通知栏进度条
//                .dismissNotificationProgress()
                //是否忽略版本
//                .showIgnoreVersion()
                //添加自定义参数，默认version=1.0.0（app的versionName）；apkKey=唯一表示（在AndroidManifest.xml配置）
                .setParams(params)
                //设置点击升级后，消失对话框，默认点击升级后，对话框显示下载进度
                .hideDialogOnDownloading(false)
                //设置头部，不设置显示默认的图片，设置图片后自动识别主色调，然后为按钮，进度条设置颜色
//                .setTopPic(R.mipmap.top_8)
                //为按钮，进度条设置颜色。
                .setThemeColor(0xffffac5d)
                //设置apk下砸路径，默认是在下载到sd卡下/Download/1.0.0/test.apk
//                .setTargetPath(path)
                //设置appKey，默认从AndroidManifest.xml获取，如果，使用自定义参数，则此项无效
//                .setAppKey("ab55ce55Ac4bcP408cPb8c1Aaeac179c5f6f")

                .build()
                //检测是否有新版本
                .checkNewApp(new UpdateCallback() {
                    /**
                     * 解析json,自定义协议
                     *
                     * @param json 服务器返回的json
                     * @return UpdateAppBean
                     */
                    @Override
                    protected UpdateAppBean parseJson(String json) {
                        final JSONObject object = JSON.parseObject(json);
                        String status = object.getString("status");
                        UpdateAppBean updateAppBean = new UpdateAppBean();

                        if ("200".equals(status)) {
                            final JSONObject data = object.getJSONObject("data");
                            String name = data.getString("name");
                            String content = data.getString("content");
                            String url = data.getString("downloadUrl");


                            String doUrl = "http://openbox.mobilem.360.cn/index/d/sid/3958155";

                            updateAppBean
                                    //（必须）是否更新Yes,No
                                    .setUpdate("Yes")
                                    //（必须）新版本号，
                                    .setNewVersion(name)
                                    //（必须）下载地址
                                    .setApkFileUrl(doUrl)
                                    //（必须）更新内容
                                    .setUpdateLog(content)
                                    //是否强制更新，可以不设置
                                    .setConstraint(false);

                        }
                        return updateAppBean;
                    }

                    @Override
                    protected void hasNewApp(UpdateAppBean updateApp, UpdateAppManager updateAppManager) {
                        updateAppManager.showDialogFragment();
                    }

                    /**
                     * 网络请求之前
                     */
                    @Override
                    public void onBefore() {
                    }

                    /**
                     * 网路请求之后
                     */
                    @Override
                    public void onAfter() {
                    }

                    /**
                     * 没有新版本
                     */
                    @Override
                    public void noNewApp() {
                    }
                });

    }
}
