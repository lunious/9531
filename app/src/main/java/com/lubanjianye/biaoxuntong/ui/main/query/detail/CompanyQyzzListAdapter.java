package com.lubanjianye.biaoxuntong.ui.main.query.detail;

import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.lubanjianye.biaoxuntong.R;
import com.lubanjianye.biaoxuntong.bean.CompanyQyzzListBean;

import java.util.List;

/**
 * 项目名:   LBBXT
 * 包名:     com.lubanjianye.biaoxuntong.ui.main.fragment.query.detail
 * 文件名:   CompanyQyzzListAdapter
 * 创建者:   lunious
 * 创建时间: 2017/12/16  0:02
 * 描述:     TODO
 */

public class CompanyQyzzListAdapter extends BaseQuickAdapter<CompanyQyzzListBean, BaseViewHolder> {

    public CompanyQyzzListAdapter(int layoutResId, @Nullable List<CompanyQyzzListBean> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, CompanyQyzzListBean item) {
        helper.setText(R.id.tv_company_qyzz, item.getLx() + "_" + item.getZzmc());
    }
}
