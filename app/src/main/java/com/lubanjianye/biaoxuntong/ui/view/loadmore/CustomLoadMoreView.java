package com.lubanjianye.biaoxuntong.ui.view.loadmore;

import com.chad.library.adapter.base.loadmore.LoadMoreView;
import com.lubanjianye.biaoxuntong.R;

/**
 * 项目名:   Lunioussky
 * 包名:     com.lubanjianye.biaoxuntong.ui.loadmore
 * 文件名:   CustomLoadMoreView
 * 创建者:   lunious
 * 创建时间: 2017/11/29  16:07
 * 描述:     TODO
 */

public final class CustomLoadMoreView extends LoadMoreView {

    @Override
    public int getLayoutId() {
        return R.layout.quick_view_load_more;
    }

    @Override
    protected int getLoadingViewId() {
        return R.id.load_more_loading_view;
    }

    @Override
    protected int getLoadFailViewId() {
        return R.id.load_more_load_fail_view;
    }

    @Override
    protected int getLoadEndViewId() {
        return R.id.load_more_load_end_view;
    }
}
