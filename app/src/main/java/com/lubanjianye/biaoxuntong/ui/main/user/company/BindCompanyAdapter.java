package com.lubanjianye.biaoxuntong.ui.main.user.company;

import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.lubanjianye.biaoxuntong.R;
import com.lubanjianye.biaoxuntong.bean.BindCompanyBean;

import java.util.List;

/**
 * 项目名:   9527
 * 包名:     com.lubanjianye.biaoxuntong.ui.main.user.company
 * 文件名:   BindCompanyAdapter
 * 创建者:   lunious
 * 创建时间: 2017/12/21  13:35
 * 描述:     TODO
 */

public class BindCompanyAdapter extends BaseQuickAdapter<BindCompanyBean, BaseViewHolder> {
    public BindCompanyAdapter(int layoutResId, @Nullable List<BindCompanyBean> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, BindCompanyBean item) {

        helper.setText(R.id.tv_company_name, item.getQy());
        String lxr = item.getLxr();
        if (lxr != null) {
            helper.setText(R.id.tv_lxr, lxr);
        } else {
            helper.setText(R.id.tv_lxr, "/");
        }
        String areaType = item.getEntrySign();
        if ("1".equals(areaType)) {
            helper.setText(R.id.tv_area_type, "川内");
        } else if ("0".equals(areaType)) {
            helper.setText(R.id.tv_area_type, "入川");
        } else {
            helper.setText(R.id.tv_area_type, "");
        }

    }
}

