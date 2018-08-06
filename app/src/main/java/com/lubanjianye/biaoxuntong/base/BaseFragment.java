package com.lubanjianye.biaoxuntong.base;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import me.yokeyword.fragmentation_swipeback.SwipeBackFragment;

/**
 * Author: lunious
 * Date: 2018/7/16 13:48
 * Description:
 */
public abstract class BaseFragment extends SwipeBackFragment {
    private Unbinder unbinder;
    private View rootView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(getLayoutId(), container, false);
        unbinder = ButterKnife.bind(this, rootView);
        initData(savedInstanceState);
        initEnvent(savedInstanceState);
        return rootView;

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    protected abstract int getLayoutId();

    protected abstract void initData(Bundle savedInstanceState);

    protected abstract void initEnvent(Bundle savedInstanceState);
}
