package com.lubanjianye.biaoxuntong.ui.main.query.detail;

import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.lubanjianye.biaoxuntong.R;
import com.lubanjianye.biaoxuntong.bean.CompanySgyjListBean;

import java.util.List;

/**
 * 项目名:   Lunious
 * 包名:     com.lubanjianye.biaoxuntong.ui.main.query.detail
 * 文件名:   CompanySgyjListAdapter
 * 创建者:   lunious
 * 创建时间: 2017/11/30  13:41
 * 描述:     TODO
 */

public class CompanySgyjListAdapter extends BaseQuickAdapter<CompanySgyjListBean,BaseViewHolder> {
    public CompanySgyjListAdapter(int layoutResId, @Nullable List<CompanySgyjListBean> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, CompanySgyjListBean item) {
        helper.setText(R.id.tv_xmmc, item.getXmmc());
        helper.setText(R.id.tv_zbje, item.getZbje());
        helper.setText(R.id.tv_zbsj, item.getZbsj());
        helper.setText(R.id.tv_fzr, item.getXmfzr());
    }
}
