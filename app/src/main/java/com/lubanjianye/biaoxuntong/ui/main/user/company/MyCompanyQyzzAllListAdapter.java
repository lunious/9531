package com.lubanjianye.biaoxuntong.ui.main.user.company;

import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.lubanjianye.biaoxuntong.R;
import com.lubanjianye.biaoxuntong.bean.MyCompanyQyzzAllListBean;

import java.util.List;

/**
 * 项目名:   LBBXT
 * 包名:     com.lubanjianye.biaoxuntong.ui.main.fragment.user.company
 * 文件名:   MyCompanyQyzzAllListAdapter
 * 创建者:   lunious
 * 创建时间: 2017/12/15  14:30
 * 描述:     TODO
 */

public class MyCompanyQyzzAllListAdapter extends BaseQuickAdapter<MyCompanyQyzzAllListBean, BaseViewHolder> {
    public MyCompanyQyzzAllListAdapter(int layoutResId, @Nullable List<MyCompanyQyzzAllListBean> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, MyCompanyQyzzAllListBean item) {

        String lx_name = item.getLx_name();
        String dl_name = item.getDl_name();
        String xl_name = item.getXl_name();
        String zy_name = item.getZy_name();
        String dj = item.getDj();
        String dq = item.getDq();
        helper.setText(R.id.tv_company_qyzz, lx_name + "__" + zy_name + "__" + dj);

    }
}
