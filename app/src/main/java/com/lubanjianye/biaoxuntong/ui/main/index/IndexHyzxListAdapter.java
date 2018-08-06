package com.lubanjianye.biaoxuntong.ui.main.index;

import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.lubanjianye.biaoxuntong.R;
import com.lubanjianye.biaoxuntong.bean.IndexHyzxListBean;

import java.util.List;

/**
 * 项目名:   LBBXT
 * 包名:     com.lubanjianye.biaoxuntong.ui.main.index
 * 文件名:   IndexHyzxListAdapter
 * 创建者:   lunious
 * 创建时间: 2017/12/16  10:22
 * 描述:     TODO
 */

public class IndexHyzxListAdapter extends BaseQuickAdapter<IndexHyzxListBean,BaseViewHolder> {

    public IndexHyzxListAdapter(int layoutResId, @Nullable List<IndexHyzxListBean> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, IndexHyzxListBean item) {

        helper.setText(R.id.tv_index_hyzx_title, item.getTitle());
        helper.setText(R.id.tv_index_hyzx_time, item.getCreate_time().substring(0, 10));
        String imgUrl = item.getMobile_img();
        int img = item.getImg();
        if (imgUrl.isEmpty()) {
            helper.setBackgroundRes(R.id.vp_index_hyzx_img, img);
        } else {
            helper.setBackgroundRes(R.id.vp_index_hyzx_img, R.mipmap.hyzx_1);
        }
    }
}
