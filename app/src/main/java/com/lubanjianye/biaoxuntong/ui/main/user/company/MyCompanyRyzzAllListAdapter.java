package com.lubanjianye.biaoxuntong.ui.main.user.company;

import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.lubanjianye.biaoxuntong.R;
import com.lubanjianye.biaoxuntong.bean.MyCompanyRyzzAllListBean;

import java.util.List;

/**
 * 项目名:   LBBXT
 * 包名:     com.lubanjianye.biaoxuntong.ui.main.fragment.user.company
 * 文件名:   MyCompanyRyzzAllListAdapter
 * 创建者:   lunious
 * 创建时间: 2017/12/15  14:31
 * 描述:     TODO
 */

public class MyCompanyRyzzAllListAdapter extends BaseQuickAdapter<MyCompanyRyzzAllListBean, BaseViewHolder> {
    public MyCompanyRyzzAllListAdapter(int layoutResId, @Nullable List<MyCompanyRyzzAllListBean> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, MyCompanyRyzzAllListBean item) {
        String ry_name = item.getRyname();
        String zgmc = item.getZg_mcdj();
        String zgzy = item.getZgzy();
        helper.setText(R.id.tv_ryzz_name, ry_name);
        helper.setText(R.id.tv_lx, zgmc);
        helper.setText(R.id.tv_ryzy, ("0".equals(zgzy) ? "--" : zgzy));
    }
}

