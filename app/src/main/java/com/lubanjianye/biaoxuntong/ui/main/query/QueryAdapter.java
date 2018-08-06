package com.lubanjianye.biaoxuntong.ui.main.query;

import android.support.annotation.Nullable;
import android.text.TextUtils;

import com.chad.library.adapter.base.BaseItemDraggableAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.lubanjianye.biaoxuntong.R;
import com.lubanjianye.biaoxuntong.bean.QueryBean;

import java.util.List;


public class QueryAdapter extends BaseItemDraggableAdapter<QueryBean, BaseViewHolder> {

    public QueryAdapter(int layoutResId, @Nullable List<QueryBean> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, QueryBean item) {
        String zzlx = item.getZzlx();
        String dl = item.getDl();
        String xl = item.getXl();
        String zy = item.getZy();
        String dj = item.getDj();
        String dq = item.getDq();
        if (TextUtils.isEmpty(zzlx)) {
            helper.setVisible(R.id.tv_query_item, false);
        } else {
            helper.setText(R.id.tv_query_item, zzlx + (dl == null ? "" : "__" + dl) + (xl == null ? "" : "__" + xl) + (zy == null ? "" : "__" + zy)
                    + (dj == null ? "" : "__" + dj) + (dq == null ? "" : "__" + dq));
        }

    }
}
