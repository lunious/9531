package com.lubanjianye.biaoxuntong.ui.main.collection;

import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.lubanjianye.biaoxuntong.R;
import com.lubanjianye.biaoxuntong.bean.CollectionListBean;

import java.util.List;


public class CollectionListAdapter extends BaseQuickAdapter<CollectionListBean, BaseViewHolder> {


    public CollectionListAdapter(int layoutResId, @Nullable List<CollectionListBean> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, CollectionListBean item) {

        //是否有结果更正
        String isResult = item.getIsResult();
        String isCorrections = item.getIsCorrections();

        if (!"0".equals(isCorrections) && !"0".equals(isResult)) {
            helper.setVisible(R.id.iv_status, true);
            helper.setImageResource(R.id.iv_status, R.mipmap.ddddd);
        } else if (!"0".equals(isResult) && "0".equals(isCorrections)) {
            helper.setVisible(R.id.iv_status, true);
            helper.setImageResource(R.id.iv_status, R.mipmap.aaaaa);
        } else if (!"0".equals(isCorrections) && "0".equals(isResult)) {
            helper.setVisible(R.id.iv_status, true);
            helper.setImageResource(R.id.iv_status, R.mipmap.bbbbb);
        } else {
            helper.setGone(R.id.iv_status, false);
        }


        helper.setText(R.id.tv_item_title, item.getEntryName());
        helper.setText(R.id.tv_item_area, item.getAddress());
        helper.setText(R.id.tv_item_type, item.getType());
        helper.setText(R.id.tv_item_time, item.getSysTime().substring(0, 10));

    }
}
