package com.lubanjianye.biaoxuntong.ui.main.index.sortcolumn;

import android.content.Intent;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.callback.ItemDragAndSwipeCallback;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.chad.library.adapter.base.listener.OnItemDragListener;
import com.lubanjianye.biaoxuntong.R;
import com.lubanjianye.biaoxuntong.app.BiaoXunTong;
import com.lubanjianye.biaoxuntong.base.BaseFragment1;
import com.lubanjianye.biaoxuntong.bean.SortColumnBean;
import com.lubanjianye.biaoxuntong.database.DatabaseManager;
import com.lubanjianye.biaoxuntong.database.UserProfile;
import com.lubanjianye.biaoxuntong.eventbus.EventMessage;
import com.lubanjianye.biaoxuntong.app.BiaoXunTongApi;
import com.lubanjianye.biaoxuntong.ui.sign.SignInActivity;
import com.lubanjianye.biaoxuntong.util.netStatus.NetUtil;
import com.lubanjianye.biaoxuntong.util.sp.AppSharePreferenceMgr;
import com.lubanjianye.biaoxuntong.util.toast.ToastUtil;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;


public class SortColumnFragment extends BaseFragment1 implements View.OnClickListener {

    private ImageView ivClose = null;
    private RecyclerView oldColumnRv = null;
    private RecyclerView newColumnRv = null;
    private RecyclerView areaColumnRv = null;

    private SortColumnAdapter mAdapter;
    private SortColumnAdapter mAdapter1;
    private SortColumnAdapter mAdapter2;
    private ArrayList<SortColumnBean> mData = new ArrayList<>();
    private ArrayList<SortColumnBean> mData1 = new ArrayList<>();
    private ArrayList<SortColumnBean> mData2 = new ArrayList<>();


    private ArrayList<Integer> mId = new ArrayList<>();


    private ItemTouchHelper mItemTouchHelper;
    private ItemDragAndSwipeCallback mItemDragAndSwipeCallback;

    private int stu = 0;


    private String mDiqu = "";

    @Override
    public Object setLayout() {
        return R.layout.fragment_column;
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

        ivClose = getView().findViewById(R.id.iv_close);
        oldColumnRv = getView().findViewById(R.id.old_column_rv);
        newColumnRv = getView().findViewById(R.id.new_column_rv);
        areaColumnRv = getView().findViewById(R.id.area_column_rv);
        ivClose.setOnClickListener(this);

    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void XXXXXX(EventMessage message) {

        if (EventMessage.LOGIN_SUCCSS.equals(message.getMessage()) || EventMessage.LOGIN_OUT.equals(message.getMessage())) {
            //更新UI
            requestData();

        } else {
            //TODO
        }
    }

    @Override
    public void initData() {
        initRecyclerView();
        initAdapter();

        BiaoXunTong.getHandler().postDelayed(new Runnable() {
            @Override
            public void run() {
                requestData();
            }
        }, 0);
    }


    @Override
    public void initEvent() {

    }


    private void initRecyclerView() {
        oldColumnRv.setLayoutManager(new GridLayoutManager(getContext(), 4));
        newColumnRv.setLayoutManager(new GridLayoutManager(getContext(), 4));
        areaColumnRv.setLayoutManager(new GridLayoutManager(getContext(), 4));

        oldColumnRv.addOnItemTouchListener(new OnItemClickListener() {
            @Override
            public void onSimpleItemClick(BaseQuickAdapter adapter, View view, int position) {

                final SortColumnBean data = (SortColumnBean) adapter.getData().get(position);
                final int id = data.getId();

                if (AppSharePreferenceMgr.contains(getContext(), EventMessage.LOGIN_SUCCSS)) {
                    int min = 7;
                    if (id > min) {
                        //删除栏目
                        OkGo.<String>post(BiaoXunTongApi.URL_DELEITEMLABEL)
                                .params("userId", userId)
                                .params("labelId", id)
                                .execute(new StringCallback() {
                                    @Override
                                    public void onSuccess(Response<String> response) {
                                        final JSONObject object = JSON.parseObject(response.body());
                                        String status = object.getString("status");
                                        if ("200".equals(status)) {

                                            requestData();
                                            stu = 1;
                                        } else {
                                            ToastUtil.shortToast(getContext(), "删除失败！");
                                        }
                                    }
                                });

                    } else {
                        ToastUtil.shortToast(getContext(), "官方推荐栏目，请保留!");
                    }
                } else {
                    startActivity(new Intent(getActivity(), SignInActivity.class));
                }


            }
        });

        newColumnRv.addOnItemTouchListener(new OnItemClickListener() {
            @Override
            public void onSimpleItemClick(BaseQuickAdapter adapter, View view, int position) {

                final SortColumnBean data = (SortColumnBean) adapter.getData().get(position);
                final int id = data.getId();

                if (AppSharePreferenceMgr.contains(getContext(), EventMessage.LOGIN_SUCCSS)) {
                    //添加栏目
                    OkGo.<String>post(BiaoXunTongApi.URL_ADDITEMLABEL)
                            .params("userId", userId)
                            .params("labelId", id)
                            .execute(new StringCallback() {
                                @Override
                                public void onSuccess(Response<String> response) {
                                    final JSONObject object = JSON.parseObject(response.body());
                                    String status = object.getString("status");
                                    if ("200".equals(status)) {

                                        requestData();
                                        stu = 1;
                                    } else {
                                        ToastUtil.shortToast(getContext(), "添加失败！");
                                    }
                                }
                            });

                } else {
                    startActivity(new Intent(getActivity(), SignInActivity.class));
                }


            }
        });

        areaColumnRv.addOnItemTouchListener(new OnItemClickListener() {
            @Override
            public void onSimpleItemClick(BaseQuickAdapter adapter, View view, int position) {

                final SortColumnBean data = (SortColumnBean) adapter.getData().get(position);
                final int id = data.getId();

                final String name = data.getName();

                if (AppSharePreferenceMgr.contains(getContext(), EventMessage.LOGIN_SUCCSS)) {
                    //添加栏目

                    OkGo.<String>post(BiaoXunTongApi.URL_ADDITEMLABEL)
                            .params("userId", userId)
                            .params("labelId", id)
                            .execute(new StringCallback() {
                                @Override
                                public void onSuccess(Response<String> response) {
                                    final JSONObject object = JSON.parseObject(response.body());
                                    String status = object.getString("status");
                                    if ("200".equals(status)) {

                                        requestData();
                                        stu = 1;
                                    } else {
                                        ToastUtil.shortToast(getContext(), "添加失败！");
                                    }
                                }
                            });


                } else {
                    startActivity(new Intent(getActivity(), SignInActivity.class));
                }


            }
        });
    }

    private void initAdapter() {

        OnItemDragListener listener = new OnItemDragListener() {

            int oldPosition = 0;
            int newPosition = 0;

            @Override
            public void onItemDragStart(RecyclerView.ViewHolder viewHolder, int pos) {

                oldPosition = mId.get(pos);

                Log.d("HABVSDSADASDA", "oldPosition===" + oldPosition);
            }


            @Override
            public void onItemDragMoving(final RecyclerView.ViewHolder source, int from, final RecyclerView.ViewHolder target, int to) {

            }

            @Override
            public void onItemDragEnd(RecyclerView.ViewHolder viewHolder, int pos) {

                newPosition = mId.get(pos);

                Log.d("HABVSDSADASDA", "newPosition===" + newPosition);


                if (AppSharePreferenceMgr.contains(getContext(), EventMessage.LOGIN_SUCCSS)) {

                    //得到用个户userId
                    List<UserProfile> users = DatabaseManager.getInstance().getDao().loadAll();
                    for (int i = 0; i < users.size(); i++) {
                        userId = users.get(0).getId();
                    }


                    OkGo.<String>post(BiaoXunTongApi.URL_TABLINE)
                            .params("userId", userId)
                            .params("id", oldPosition)
                            .params("previousId", newPosition)
                            .execute(new StringCallback() {
                                @Override
                                public void onSuccess(Response<String> response) {
                                    final JSONObject object = JSON.parseObject(response.body());
                                    String status = object.getString("status");
                                    String message = object.getString("message");
                                    if ("200".equals(status)) {
                                        requestData();
                                        stu = 1;
                                    } else {
                                        ToastUtil.shortToast(getContext(), message);
                                    }
                                }
                            });

                } else {
                    ToastUtil.shortToast(getContext(), "未登录!");
                }

            }


        };

        mAdapter = new SortColumnAdapter(R.layout.item_column_layout, mData);
        mAdapter1 = new SortColumnAdapter(R.layout.item_column_layout, mData1);
        mAdapter2 = new SortColumnAdapter(R.layout.item_column_layout, mData2);
        oldColumnRv.addItemDecoration(new SpaceItemDecoration(8));
        newColumnRv.addItemDecoration(new SpaceItemDecoration(8));
        areaColumnRv.addItemDecoration(new SpaceItemDecoration(8));
        oldColumnRv.setAdapter(mAdapter);
        newColumnRv.setAdapter(mAdapter1);
        areaColumnRv.setAdapter(mAdapter2);


        mItemDragAndSwipeCallback = new ItemDragAndSwipeCallback(mAdapter);
        mItemTouchHelper = new ItemTouchHelper(mItemDragAndSwipeCallback);
        mItemTouchHelper.attachToRecyclerView(oldColumnRv);
        mItemDragAndSwipeCallback.setSwipeMoveFlags(ItemTouchHelper.START | ItemTouchHelper.END);
        mAdapter.enableDragItem(mItemTouchHelper);
        mAdapter.setOnItemDragListener(listener);


    }

    private long userId = 0;

    public void requestData() {

        if (AppSharePreferenceMgr.contains(getContext(), EventMessage.LOCA_AREA)) {
            mDiqu = (String) AppSharePreferenceMgr.get(getContext(), EventMessage.LOCA_AREA, "");
        }

        if (!NetUtil.isNetworkConnected(getContext())) {
            ToastUtil.shortToast(getContext(), "网络出错，请检查网络设置！");
        } else {
            if (AppSharePreferenceMgr.contains(getContext(), EventMessage.LOGIN_SUCCSS)) {
                //得到用个户userId
                List<UserProfile> users = DatabaseManager.getInstance().getDao().loadAll();
                for (int i = 0; i < users.size(); i++) {
                    userId = users.get(0).getId();
                }

                OkGo.<String>post(BiaoXunTongApi.URL_GETALLTAB)
                        .params("userId", userId)
                        .params("diqu", mDiqu)
                        .execute(new StringCallback() {
                            @Override
                            public void onSuccess(Response<String> response) {
                                final JSONObject object = JSON.parseObject(response.body());
                                final JSONObject data = object.getJSONObject("data");
                                String status = object.getString("status");
                                String message = object.getString("message");

                                if ("200".equals(status)) {
                                    final JSONArray ownerList = data.getJSONArray("ownerList");
                                    final JSONArray typeList = data.getJSONArray("typeList");
                                    final JSONArray areaList = data.getJSONArray("areaList");

                                    if (ownerList.size() > 0) {
                                        setData(ownerList);
                                    } else {
                                        if (mData != null) {
                                            mData.clear();
                                            mAdapter.notifyDataSetChanged();
                                        }
                                    }

                                    if (typeList != null) {
                                        setData1(typeList);
                                    } else {
                                        if (mData1 != null) {
                                            mData1.clear();
                                            mAdapter1.notifyDataSetChanged();
                                        }
                                    }

                                    if (areaList != null) {
                                        setData2(areaList);
                                    } else {
                                        if (mData2 != null) {
                                            mData2.clear();
                                            mAdapter2.notifyDataSetChanged();
                                        }
                                    }
                                } else {
                                    ToastUtil.shortToast(getContext(), message);
                                }
                            }
                        });

            } else {

                OkGo.<String>post(BiaoXunTongApi.URL_GETALLTAB)
                        .params("diqu", mDiqu)
                        .execute(new StringCallback() {
                            @Override
                            public void onSuccess(Response<String> response) {
                                final JSONObject object = JSON.parseObject(response.body());
                                final JSONObject data = object.getJSONObject("data");
                                String status = object.getString("status");
                                String message = object.getString("message");

                                if ("200".equals(status)) {
                                    final JSONArray ownerList = data.getJSONArray("ownerList");
                                    final JSONArray typeList = data.getJSONArray("typeList");
                                    final JSONArray areaList = data.getJSONArray("areaList");

                                    if (ownerList.size() > 0) {
                                        setData(ownerList);
                                    } else {
                                        if (mData != null) {
                                            mData.clear();
                                            mAdapter.notifyDataSetChanged();
                                        }
                                    }

                                    if (typeList != null) {
                                        setData1(typeList);
                                    } else {
                                        if (mData1 != null) {
                                            mData1.clear();
                                            mAdapter1.notifyDataSetChanged();
                                        }
                                    }

                                    if (areaList != null) {
                                        setData2(areaList);
                                    } else {
                                        if (mData2 != null) {
                                            mData2.clear();
                                            mAdapter2.notifyDataSetChanged();
                                        }
                                    }
                                } else {
                                    ToastUtil.shortToast(getContext(), message);
                                }
                            }
                        });

            }

        }

    }


    private void setData(JSONArray data) {

        mData.clear();
        mId.clear();
        for (int i = 0; i < data.size(); i++) {
            SortColumnBean bean = new SortColumnBean();
            JSONObject list = data.getJSONObject(i);
            int id = list.getInteger("id");
            mId.add(id);
            bean.setId(id);
            String name = list.getString("name");
            int isShow = list.getInteger("isShow");
            bean.setIsShow(isShow);
            bean.setName(name);
            if ("最新标讯".equals(name) || "施工".equals(name) || "监理".equals(name) || "勘察".equals(name)
                    || "设计".equals(name) || "政府采购".equals(name) || "行业资讯".equals(name)) {
                bean.setShowDele(false);
                bean.setChangeColo(true);
            } else {
                bean.setShowDele(true);
                bean.setChangeColo(false);
            }

            mData.add(bean);
        }

        Log.d("DAVSDSADSADAS", mId.toString());

        mAdapter.notifyDataSetChanged();

    }

    private void setData1(JSONArray data) {
        mData1.clear();
        for (int i = 0; i < data.size(); i++) {
            SortColumnBean bean = new SortColumnBean();
            JSONObject list = data.getJSONObject(i);
            bean.setId(list.getInteger("id"));
            bean.setName("+ " + list.getString("name"));
            bean.setShowDele(false);
            bean.setChangeColo(false);
            int isShow = list.getInteger("isShow");
            bean.setIsShow(isShow);
            mData1.add(bean);
        }
        mAdapter1.notifyDataSetChanged();

    }

    private void setData2(JSONArray data) {
        mData2.clear();
        for (int i = 0; i < data.size(); i++) {
            SortColumnBean bean = new SortColumnBean();
            JSONObject list = data.getJSONObject(i);
            bean.setId(list.getInteger("id"));
            bean.setName("+ " + list.getString("name"));
            bean.setShowDele(false);
            bean.setChangeColo(false);
            int isShow = list.getInteger("isShow");
            bean.setIsShow(isShow);
            mData2.add(bean);
        }
        mAdapter2.notifyDataSetChanged();

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_close:
                if (stu == 1) {
                    EventBus.getDefault().post(new EventMessage(EventMessage.TAB_CHANGE));
                }
                getActivity().onBackPressed();
                break;
            default:
                break;
        }
    }

    @Override
    public boolean onBackPressedSupport() {
        if (stu == 1) {
            EventBus.getDefault().post(new EventMessage(EventMessage.TAB_CHANGE));
        }
        return super.onBackPressedSupport();
    }

}
