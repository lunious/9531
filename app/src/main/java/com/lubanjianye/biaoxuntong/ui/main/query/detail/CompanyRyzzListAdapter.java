package com.lubanjianye.biaoxuntong.ui.main.query.detail;

import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.lubanjianye.biaoxuntong.R;
import com.lubanjianye.biaoxuntong.bean.CompanyRyzzListBean;

import java.util.List;

/**
 * 项目名:   Lunious
 * 包名:     com.lubanjianye.biaoxuntong.ui.main.query
 * 文件名:   CompanyRyzzListAdapter
 * 创建者:   lunious
 * 创建时间: 2017/11/30  12:46
 * 描述:     TODO
 */

public class CompanyRyzzListAdapter extends BaseQuickAdapter<CompanyRyzzListBean, BaseViewHolder> {
    public CompanyRyzzListAdapter(int layoutResId, @Nullable List<CompanyRyzzListBean> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, CompanyRyzzListBean item) {

        String ry_name = item.getRy();
        String zgmc = item.getZgMcdj();
        String zgzy = item.getZgZy();
        helper.setText(R.id.tv_ryzz_name, ry_name);
        helper.setText(R.id.tv_lx, zgmc);
        helper.setText(R.id.tv_ryzy, ("0".equals(zgzy) ? "--" : zgzy));

    }
}
