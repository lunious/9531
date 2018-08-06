package com.lubanjianye.biaoxuntong.ui.main.index.sortcolumn;

import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * 项目名:   LBBXT
 * 包名:     com.lubanjianye.biaoxuntong.ui.main.index.sortcolumn
 * 文件名:   SpaceItemDecoration
 * 创建者:   lunious
 * 创建时间: 2017/12/16  18:44
 * 描述:     TODO
 */

public class SpaceItemDecoration extends RecyclerView.ItemDecoration{

    private int mSpace;

    public SpaceItemDecoration(int space)
    {
        mSpace = space;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state)
    {
        super.getItemOffsets(outRect, view, parent, state);
        outRect.left = mSpace;
        outRect.top = 0;
        outRect.right = mSpace;
        outRect.bottom = mSpace;
    }
}
