package com.lubanjianye.biaoxuntong.ui.main.index;

import android.support.annotation.Nullable;
import android.text.TextUtils;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.lubanjianye.biaoxuntong.R;
import com.lubanjianye.biaoxuntong.bean.IndexListBean;

import java.util.List;


public class IndexListAdapter extends BaseQuickAdapter<IndexListBean, BaseViewHolder> {

    public IndexListAdapter(int layoutResId, @Nullable List<IndexListBean> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, IndexListBean item) {

        //是否有结果更正
        String isResult = item.getIsResult();
        String isCorrections = item.getIsCorrections();

        if (!"0".equals(isCorrections) && !"0".equals(isResult)) {
            helper.setVisible(R.id.iv_status,true);
            helper.setImageResource(R.id.iv_status, R.mipmap.ddddd);
        } else if (!"0".equals(isResult) && "0".equals(isCorrections)) {
            helper.setVisible(R.id.iv_status,true);
            helper.setImageResource(R.id.iv_status, R.mipmap.aaaaa);
        } else if (!"0".equals(isCorrections) && "0".equals(isResult)) {
            helper.setVisible(R.id.iv_status,true);
            helper.setImageResource(R.id.iv_status, R.mipmap.bbbbb);
        }else {
            helper.setGone(R.id.iv_status,false);
        }

        helper.setText(R.id.tv_index_title, item.getEntryName());
        helper.setText(R.id.tv_index_address, item.getAddress());
        String type = item.getType();
        if (!TextUtils.isEmpty(type)) {
            helper.setText(R.id.tv_index_type, item.getType());
        }
        String signstauts = item.getSignstauts();

        if (signstauts != null) {
            helper.setText(R.id.tv_index_status, signstauts);
            if ("正在报名".equals(signstauts)) {
                helper.setTextColor(R.id.tv_index_status, android.graphics.Color.parseColor("#21a9ff"));
            } else if ("报名结束".equals(signstauts)) {
                helper.setTextColor(R.id.tv_index_status, android.graphics.Color.parseColor("#ff6666"));
            } else if ("待报名".equals(signstauts)) {
                helper.setTextColor(R.id.tv_index_status, android.graphics.Color.parseColor("#00bfdc"));
            }

        }
        String deadTime = item.getDeadTime();
        String sysTime = item.getSysTime();

        if (!TextUtils.isEmpty(deadTime)) {
            helper.setText(R.id.tv_index_time, deadTime);
            helper.setText(R.id.tv_index_time_type, "截止");
            helper.setTextColor(R.id.tv_index_time_type, android.graphics.Color.parseColor("#ff6666"));
        } else {
            helper.setVisible(R.id.tv_index_time, false);
            helper.setVisible(R.id.tv_index_time_type, false);
        }
        if (!TextUtils.isEmpty(sysTime)) {
            helper.setText(R.id.tv_index_pub_time, sysTime.substring(0, 10));
            helper.setText(R.id.tv_index_pub_time_type, "发布");
            helper.setTextColor(R.id.tv_index_pub_time_type, android.graphics.Color.parseColor("#9c9c9c"));
        } else {
            helper.setVisible(R.id.tv_index_pub_time, false);
            helper.setVisible(R.id.tv_index_pub_time_type, false);
        }
    }
}

