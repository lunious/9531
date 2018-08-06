package com.lubanjianye.biaoxuntong.ui.search.fragment;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.gson.reflect.TypeToken;
import com.lubanjianye.biaoxuntong.R;
import com.lubanjianye.biaoxuntong.base.BaseFragment1;
import com.lubanjianye.biaoxuntong.ui.search.adapter.SearchHistoryAdapter;
import com.lubanjianye.biaoxuntong.ui.search.rx.RxBus;
import com.lubanjianye.biaoxuntong.ui.search.util.AccountPreference;
import com.lubanjianye.biaoxuntong.ui.search.util.GlobalConstant;
import com.lubanjianye.biaoxuntong.ui.search.util.JsonUtils;
import com.lubanjianye.biaoxuntong.ui.search.util.StringUtils;
import com.lubanjianye.biaoxuntong.util.dimen.DensityUtil;

import java.util.ArrayList;
import java.util.List;

import cn.lankton.flowlayout.FlowLayout;

public class SearchHistoryFragment extends BaseFragment1 {

    private static final int MAX_HISTORY_AMOUNT = 19;

    private RecyclerView mRecyclerView;
    private SearchHistoryAdapter mAdapter;
    private List<String> mHistoryData;
    private TextView mNoHistoryTv;
    private View mBottomView;


    @Override
    public Object setLayout() {
        return R.layout.fragment_search_history;
    }

    @Override
    public void initView() {
        mRecyclerView = getView().findViewById(R.id.swipe_target);
    }

    @Override
    public void initData() {
        String historyData = AccountPreference.getInstance().getHistoryData();
        if (!StringUtils.isNullOrEmpty(historyData)) {
            mHistoryData = JsonUtils.decode(historyData, new TypeToken<List<String>>() {
            }.getType());
        }
        mAdapter = new SearchHistoryAdapter();
        mBottomView = getBottomView();
        mAdapter.addFooterView(mBottomView);


        mAdapter.setOnItemClickListener(new SearchHistoryAdapter.OnItemClickListener() {
            @Override
            public void onItemClickListener(int position, String info) {
                RxBus.getInstance().post(GlobalConstant.RxBus.SERACH_HISTORY_CLICK, info);
                addHistory(info);
            }

            @Override
            public void onDeleteListener(int position) {
                if (null != mHistoryData && !mHistoryData.isEmpty()) {
                    mHistoryData.remove(position - mAdapter.getHeaderLayoutCount());
                    saveHistory();
                }
                if (mHistoryData == null || mHistoryData.isEmpty()) {
                    isShowBottomClearView(false);
                } else {
                    isShowBottomClearView(true);
                }
                mAdapter.setNewData(mHistoryData);
            }
        });
    }

    @Override
    public void initEvent() {
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRecyclerView.setAdapter(mAdapter);
        View headView = getHeadView();
        mNoHistoryTv = headView.findViewById(R.id.tv_no_history);
        mAdapter.addHeaderView(headView);
        if (mHistoryData == null || mHistoryData.isEmpty()) {
            isShowBottomClearView(false);
        } else {
            isShowBottomClearView(true);
            mAdapter.addData(mHistoryData);
        }
    }

    private View getBottomView() {
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.search_clear_history_bottom_adapter, null, false);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mHistoryData.clear();
                saveHistory();
                mAdapter.setNewData(mHistoryData);
                isShowBottomClearView(false);
            }
        });
        return view;
    }

    private void saveHistory() {
        if (mHistoryData == null) {
            return;
        }
        if (mHistoryData.isEmpty()) {
            AccountPreference.getInstance().setHistoryData("");
            return;
        }
        AccountPreference.getInstance().setHistoryData(JsonUtils.encode(mHistoryData));
    }

    private View getHeadView() {
        View headView = LayoutInflater.from(getActivity()).inflate(R.layout.search_history_pic_adapter_head, null);
        String[] tabs = new String[]{"天然气", "道路", "基础设施","服务器", "扶贫产业", "搬迁", "污水处理", "隧道","地质灾害隐患", "绿化","整治工程","疫苗"};
        FlowLayout flowLayout = headView.findViewById(R.id.flowlayout);
        for (int i = 0; i < tabs.length; i++) {
            ViewGroup.MarginLayoutParams lp = new ViewGroup.MarginLayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, DensityUtil.dip2px(getContext(), 26));
            lp.setMargins(DensityUtil.dip2px(getContext(), 5), 0, DensityUtil.dip2px(getContext(), 5), 0);
            TextView tv = new TextView(getActivity());
            tv.setPadding(DensityUtil.dip2px(getContext(), 5), 0, DensityUtil.dip2px(getContext(), 5), 0);
            tv.setTextColor(getResources().getColor(R.color.main_theme_color));
            tv.setTextSize(TypedValue.COMPLEX_UNIT_SP, 15);
            tv.setText(tabs[i]);
            tv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    RxBus.getInstance().post(GlobalConstant.RxBus.SERACH_HISTORY_CLICK, ((TextView) view).getText());
                }
            });
            tv.setGravity(Gravity.CENTER_VERTICAL);
            tv.setLines(1);
            tv.setBackgroundResource(R.drawable.bt_shape_main_color);
            flowLayout.addView(tv, lp);
            headView.findViewById(R.id.cv_tab_content).setVisibility(View.VISIBLE);
        }


        return headView;
    }

    private void isShowBottomClearView(boolean yes) {
        mBottomView.setVisibility(yes ? View.VISIBLE : View.GONE);
        mNoHistoryTv.setVisibility(!yes ? View.VISIBLE : View.GONE);
    }

    public void addHistory(String content) {

        if (StringUtils.isNullOrEmpty(content)) {
            return;
        }

        if (mHistoryData == null) {
            mHistoryData = new ArrayList<>();
        }

        isShowBottomClearView(true);

        if (mHistoryData.isEmpty()) {
            mHistoryData.add(content);
            mAdapter.setNewData(mHistoryData);
            saveHistory();
            return;
        }

        //去重
        for (int i = 0; i < mHistoryData.size(); i++) {
            if (content.equals(mHistoryData.get(i))) {
                mHistoryData.remove(i);
                break;
            }
        }

        if (mHistoryData.isEmpty()) {
            mHistoryData.add(content);
            mAdapter.setNewData(mHistoryData);
            saveHistory();
            return;
        }
        mHistoryData.add(0, content);
        if (mHistoryData.size() > MAX_HISTORY_AMOUNT) {
            for (int i = mHistoryData.size() - 1; i > MAX_HISTORY_AMOUNT; i--) {
                mHistoryData.remove(i);
            }
        }
        mAdapter.setNewData(mHistoryData);
        saveHistory();
    }


    public static SearchHistoryFragment newInstance() {
        SearchHistoryFragment fragment = new SearchHistoryFragment();
        return fragment;
    }

}
