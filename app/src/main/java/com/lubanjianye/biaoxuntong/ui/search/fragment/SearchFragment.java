package com.lubanjianye.biaoxuntong.ui.search.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.AppCompatTextView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.lubanjianye.biaoxuntong.R;
import com.lubanjianye.biaoxuntong.app.BiaoXunTongApi;
import com.lubanjianye.biaoxuntong.base.BaseFragment1;
import com.lubanjianye.biaoxuntong.ui.search.adapter.BaseFragmentStateAdapter;
import com.lubanjianye.biaoxuntong.ui.search.rx.RxBus;
import com.lubanjianye.biaoxuntong.ui.search.rx.RxManager;
import com.lubanjianye.biaoxuntong.ui.search.util.GlobalConstant;
import com.lubanjianye.biaoxuntong.ui.search.util.StringUtils;
import com.lubanjianye.biaoxuntong.ui.search.view.MSGView;
import com.lubanjianye.biaoxuntong.util.aes.AesUtil;
import com.lubanjianye.biaoxuntong.util.toast.ToastUtil;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import rx.functions.Action1;

public class SearchFragment extends BaseFragment1 implements View.OnClickListener {


    private ViewPager viewPager;
    private AppBarLayout mAppBarLayout;
    private EditText mSerachEt;
    private ImageView mClearIv;
    private LinearLayout mHeadLl;
    private FrameLayout mHistoryLayout;
    private View mViewSpace;
    private View mStopView;
    private AppCompatTextView mSearch;
    private ImageView mBack;
    private MSGView mMsgView;

    public RxManager mRxManager;

    private static final String SEARCH_TYPE = "SEARCH_TYPE";

    private int searchType = -1;

    private BaseFragmentStateAdapter fragmentAdapter;
    List<Fragment> mNewsFragmentList = new ArrayList<>();
    LinkedHashMap<String, String> mSearchResult = new LinkedHashMap<>();
    private SearchHistoryFragment mSearchHistoryFragment;


    @Override
    public Object setLayout() {
        return R.layout.fragment_search_main;
    }


    public static SearchFragment create(@NonNull int searchTye) {
        final Bundle args = new Bundle();
        args.putInt(SEARCH_TYPE, searchTye);
        final SearchFragment fragment = new SearchFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final Bundle args = getArguments();
        if (args != null) {
            searchType = args.getInt(SEARCH_TYPE);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

        if (mRxManager != null) {
            mRxManager.clear();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back:
                getActivity().onBackPressed();
                break;
            case R.id.atv_search:
                if (StringUtils.isNullOrEmpty(mSerachEt.getText().toString())) {
                    ToastUtil.shortToast(getContext(), "请输入关键字搜索");
                } else {
                    search(mSerachEt.getText().toString());
                    if (mSearchHistoryFragment != null) {
                        mSearchHistoryFragment.addHistory(mSerachEt.getText().toString());
                    }
                }
                break;
            case R.id.iv_search_clear:
                mSerachEt.setText("");
                break;
            default:
                break;
        }
    }

    @Override
    public void initView() {
        mRxManager = new RxManager();
        viewPager = getView().findViewById(R.id.view_pager);
        mAppBarLayout = getView().findViewById(R.id.abl_layout);
        mSerachEt = getView().findViewById(R.id.et_search_content);
        mClearIv = getView().findViewById(R.id.iv_search_clear);
        mHeadLl = getView().findViewById(R.id.ll_head);
        mHistoryLayout = getView().findViewById(R.id.fl_history);
        mViewSpace = getView().findViewById(R.id.view_sapce);
        mStopView = getView().findViewById(R.id.view_stop);
        mBack = getView().findViewById(R.id.iv_back);
        mSearch = getView().findViewById(R.id.atv_search);
        mMsgView = getView().findViewById(R.id.msg_view);
        mBack.setOnClickListener(this);
        mSearch.setOnClickListener(this);
        mClearIv.setOnClickListener(this);

    }

    @Override
    public void initData() {
        mRxManager.on(GlobalConstant.RxBus.SERACH_HISTORY_CLICK, new Action1<String>() {
            @Override
            public void call(String s) {
                if (StringUtils.isNullOrEmpty(s)) {
                    return;
                }
                mSerachEt.setText(s);
                mSerachEt.setSelection(s.length());
                search(s);
            }
        });

        mRxManager.on(GlobalConstant.RxBus.SEARCH_TOP_SHOW_HIDE, new Action1<Boolean>() {

            @Override
            public void call(Boolean hideOrShow) {
                if (hideOrShow) {
                    RxBus.getInstance().post(GlobalConstant.RxBus.MENU_SHOW_HIDE, true);
                }
            }
        });

    }

    @Override
    public void initEvent() {
        mMsgView.setVisibility(View.GONE);
        mStopView.setVisibility(View.GONE);
        mMsgView.setErrorClick(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!StringUtils.isNullOrEmpty(mSerachEt.getText().toString())) {
                    search(mSerachEt.getText().toString());
                } else {
                    ToastUtil.shortToast(getContext(), "请输入关键字搜索");
                }

            }
        });

        mSerachEt.setImeOptions(EditorInfo.IME_ACTION_SEARCH);
        mSerachEt.addTextChangedListener(new SearchResultWatcher());
        mSerachEt.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                if (i == EditorInfo.IME_ACTION_SEARCH || (keyEvent != null && keyEvent.getKeyCode() == KeyEvent.KEYCODE_ENTER)) {
                    if (StringUtils.isNullOrEmpty(mSerachEt.getText().toString())) {
                        ToastUtil.shortToast(getContext(), "请输入关键字搜索");
                        return true;
                    }
                    search(mSerachEt.getText().toString());
                    if (mSearchHistoryFragment != null) {
                        mSearchHistoryFragment.addHistory(mSerachEt.getText().toString());
                    }
                    return true;
                }
                return false;
            }
        });

        mSearchHistoryFragment = SearchHistoryFragment.newInstance();
        getChildFragmentManager().beginTransaction().add(R.id.fl_history, mSearchHistoryFragment).commit();
        showHistory(true);

    }

    private void showHistory(boolean isShow) {
        mViewSpace.setVisibility(isShow ? View.VISIBLE : View.VISIBLE);
        mHistoryLayout.setVisibility(isShow ? View.VISIBLE : View.GONE);
        if (isShow) {
            mMsgView.dismiss();
        }
    }

    private void initResultFragment(String content) {
        showHistory(false);
        mNewsFragmentList.clear();
        viewPager.setAdapter(null);
        List<String> values = new ArrayList<>();

        mNewsFragmentList.add(SearchResultListFragment.newInstance(content));
        fragmentAdapter = new BaseFragmentStateAdapter(getChildFragmentManager(), mNewsFragmentList, values);
        viewPager.setAdapter(fragmentAdapter);
        viewPager.setVisibility(View.VISIBLE);
    }

    private void initIndexFragment(String content) {
        showHistory(false);
        mNewsFragmentList.clear();
        viewPager.setAdapter(null);
        List<String> values = new ArrayList<>();

        mNewsFragmentList.add(SearchIndexListFragment.newInstance(content));
        fragmentAdapter = new BaseFragmentStateAdapter(getChildFragmentManager(), mNewsFragmentList, values);
        viewPager.setAdapter(fragmentAdapter);
        viewPager.setVisibility(View.VISIBLE);
    }

    private void setSearchRsult(String content, int searchType) {
        mSearchResult.clear();
        if (content != null) {
            if (searchType == 1) {
                initIndexFragment(content);
            } else if (searchType == 2) {
                initResultFragment(content);
            }
        }

    }


    private void search(final String content) {
        mStopView.setVisibility(View.VISIBLE);
        hideSoftInput();
        mAppBarLayout.setExpanded(true);
        mMsgView.showLoading();


        if (searchType == 1) {
            OkGo.<String>post(BiaoXunTongApi.URL_GETINDEXLIST)
                    .params("keyWord", content)
                    .execute(new StringCallback() {
                        @Override
                        public void onSuccess(Response<String> response) {
                            mMsgView.dismiss();
                            mStopView.setVisibility(View.GONE);

                            String jiemi = AesUtil.aesDecrypt(response.body(), BiaoXunTongApi.PAS_KEY);


                            final JSONObject object = JSON.parseObject(jiemi);
                            final String status = object.getString("status");
                            final String message = object.getString("message");

                            if ("200".equals(status)) {
                                final JSONObject data = object.getJSONObject("data");
                                final JSONArray array = data.getJSONArray("list");
                                if (array.size() > 0) {
                                    setSearchRsult(content, searchType);
                                } else {
                                    mMsgView.showSearchEmpty();
                                }
                            } else {
                                ToastUtil.shortToast(getContext(), message);
                            }


                        }

                        @Override
                        public void onError(Response<String> response) {
                            mMsgView.showError();
                            mStopView.setVisibility(View.GONE);
                        }

                        @Override
                        public void onFinish() {
                            mStopView.setVisibility(View.GONE);
                        }
                    });
        } else if (searchType == 2) {
            OkGo.<String>post(BiaoXunTongApi.URL_GETRESULTLIST)
                    .params("keyWord", content)
                    .execute(new StringCallback() {
                        @Override
                        public void onSuccess(Response<String> response) {
                            mMsgView.dismiss();
                            mStopView.setVisibility(View.GONE);

                            String jiemi = AesUtil.aesDecrypt(response.body(), BiaoXunTongApi.PAS_KEY);

                            final JSONObject object = JSON.parseObject(jiemi);
                            final String status = object.getString("status");
                            final String message = object.getString("message");

                            if ("200".equals(status)) {
                                final JSONObject data = object.getJSONObject("data");
                                final JSONArray array = data.getJSONArray("list");
                                if (array.size() > 0) {
                                    setSearchRsult(content, searchType);
                                } else {
                                    mMsgView.showSearchEmpty();
                                }
                            } else {
                                ToastUtil.shortToast(getContext(), message);
                            }
                        }

                        @Override
                        public void onError(Response<String> response) {
                            mMsgView.showError();
                            mStopView.setVisibility(View.GONE);
                        }

                        @Override
                        public void onFinish() {
                            mStopView.setVisibility(View.GONE);
                        }
                    });
        }

    }

    private class SearchResultWatcher implements TextWatcher {

        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {

        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {

        }

        @Override
        public void afterTextChanged(Editable editable) {
            if (editable.length() > 0) {
                mClearIv.setVisibility(View.VISIBLE);
            } else {
                mClearIv.setVisibility(View.GONE);
                showHistory(true);
            }
        }
    }

}
