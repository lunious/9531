package com.lubanjianye.biaoxuntong.ui.search.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.github.ybq.android.spinkit.SpinKitView;
import com.github.ybq.android.spinkit.style.Wave;
import com.lubanjianye.biaoxuntong.R;
import com.lubanjianye.biaoxuntong.ui.search.util.StringUtils;

public class MSGView extends FrameLayout {

    private View mRootView;
    private LinearLayout mLoadingLayout;
    private LinearLayout mErrorLayout;
    private LinearLayout mEmptyLayout;
    private SpinKitView mSpinKitView;
    private TextView mEmptyTipTv;
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return true;
    }

    public MSGView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context);
    }

    public MSGView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public MSGView(Context context) {
        super(context);
        init(context);
    }

    private void init(Context context) {
        mRootView = LayoutInflater.from(this.getContext()).inflate(R.layout.app_view_msg_layout, this);
        mEmptyTipTv = mRootView.findViewById(R.id.tv_empty_tip);
        mLoadingLayout = mRootView.findViewById(R.id.ll_loading);
        mEmptyLayout = mRootView.findViewById(R.id.ll_empty);
        mErrorLayout = mRootView.findViewById(R.id.ll_error);
        mSpinKitView = mRootView.findViewById(R.id.spin_loading);
        if (null != mSpinKitView) {
            mSpinKitView.setColor(this.getContext().getResources().getColor(R.color.main_theme_color));
            mSpinKitView.setIndeterminateDrawable(new Wave());
        }
    }


    public void dismiss(){
        this.setVisibility(View.INVISIBLE);
    }
    public void showLoading() {
        this.setVisibility(View.VISIBLE);
        mLoadingLayout.setVisibility(View.VISIBLE);
        mEmptyLayout.setVisibility(GONE);
        mErrorLayout.setVisibility(View.GONE);
    }
    public void showError() {
        this.setVisibility(View.VISIBLE);
        mLoadingLayout.setVisibility(View.GONE);
        mEmptyLayout.setVisibility(GONE);
        mErrorLayout.setVisibility(View.VISIBLE);
    }

    public void showEmpty() {
        this.setVisibility(View.VISIBLE);
        mLoadingLayout.setVisibility(View.GONE);
        mErrorLayout.setVisibility(View.GONE);
        mEmptyLayout.setVisibility(VISIBLE);
    }

    public void showEmpty(String tip) {
        if (!StringUtils.isNullOrEmpty(tip)) {
            mEmptyTipTv.setText(tip);
        }
        showEmpty();
    }

    public void showSearchEmpty() {
        showEmpty(getResources().getString(R.string.no_content));

    }

    public void setErrorClick(OnClickListener listener){
        if(listener!=null){
            mErrorLayout.setOnClickListener(listener);
        }
    }
}

