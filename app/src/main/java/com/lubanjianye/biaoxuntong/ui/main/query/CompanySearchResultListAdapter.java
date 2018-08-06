package com.lubanjianye.biaoxuntong.ui.main.query;

import android.support.annotation.Nullable;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.lubanjianye.biaoxuntong.R;
import com.lubanjianye.biaoxuntong.bean.CompanySearchResultListBean;

import java.util.List;


public class CompanySearchResultListAdapter extends BaseQuickAdapter<CompanySearchResultListBean, BaseViewHolder> {
    public CompanySearchResultListAdapter(int layoutResId, @Nullable List<CompanySearchResultListBean> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, CompanySearchResultListBean item) {

        helper.setText(R.id.tv_company_name, item.getQy());

        String lxr = item.getLxr();

        String ss = "";
        if (lxr.contains("，")) {
            String regex = "\\，";
            String[] arr = lxr.split(regex);

            for (int i = 0; i < arr.length; i++) {
                String s1 = arr[0];
                ss = s1;
            }
        } else {
            ss = lxr;
        }


        if (ss != null) {
            helper.setText(R.id.tv_lxr, ss);
        } else {
            helper.setText(R.id.tv_lxr, "暂未添加");
        }

        String provinceCode = item.getProvinceCode();
        String showSign = item.getShowSign();
        String areaType = item.getEntrySign();

        if ("1".equals(showSign)) {
            if ("0".equals(areaType)) {
                if ("110000".equals(provinceCode)) {
                    helper.setText(R.id.tv_area_type, "京内");
                } else if ("120000".equals(provinceCode)) {
                    helper.setText(R.id.tv_area_type, "津内");
                } else if ("130000".equals(provinceCode)) {
                    helper.setText(R.id.tv_area_type, "冀内");
                } else if ("140000".equals(provinceCode)) {
                    helper.setText(R.id.tv_area_type, "晋内");
                } else if ("150000".equals(provinceCode)) {
                    helper.setText(R.id.tv_area_type, "蒙内");
                } else if ("210000".equals(provinceCode)) {
                    helper.setText(R.id.tv_area_type, "辽内");
                } else if ("220000".equals(provinceCode)) {
                    helper.setText(R.id.tv_area_type, "吉内");
                } else if ("230000".equals(provinceCode)) {
                    helper.setText(R.id.tv_area_type, "黑内");
                } else if ("310000".equals(provinceCode)) {
                    helper.setText(R.id.tv_area_type, "沪内");
                } else if ("320000".equals(provinceCode)) {
                    helper.setText(R.id.tv_area_type, "苏内");
                } else if ("330000".equals(provinceCode)) {
                    helper.setText(R.id.tv_area_type, "浙内");
                } else if ("340000".equals(provinceCode)) {
                    helper.setText(R.id.tv_area_type, "晥内");
                } else if ("350000".equals(provinceCode)) {
                    helper.setText(R.id.tv_area_type, "闽内");
                } else if ("360000".equals(provinceCode)) {
                    helper.setText(R.id.tv_area_type, "赣内");
                } else if ("370000".equals(provinceCode)) {
                    helper.setText(R.id.tv_area_type, "鲁内");
                } else if ("440000".equals(provinceCode)) {
                    helper.setText(R.id.tv_area_type, "粤内");
                } else if ("450000".equals(provinceCode)) {
                    helper.setText(R.id.tv_area_type, "桂内");
                } else if ("460000".equals(provinceCode)) {
                    helper.setText(R.id.tv_area_type, "琼内");
                } else if ("410000".equals(provinceCode)) {
                    helper.setText(R.id.tv_area_type, "豫内");
                } else if ("420000".equals(provinceCode)) {
                    helper.setText(R.id.tv_area_type, "鄂内");
                } else if ("430000".equals(provinceCode)) {
                    helper.setText(R.id.tv_area_type, "湘内");
                } else if ("500000".equals(provinceCode)) {
                    helper.setText(R.id.tv_area_type, "渝内");
                } else if ("510000".equals(provinceCode)) {
                    helper.setText(R.id.tv_area_type, "川内");
                } else if ("520000".equals(provinceCode)) {
                    helper.setText(R.id.tv_area_type, "黔内");
                } else if ("530000".equals(provinceCode)) {
                    helper.setText(R.id.tv_area_type, "滇内");
                } else if ("540000".equals(provinceCode)) {
                    helper.setText(R.id.tv_area_type, "藏内");
                } else if ("610000".equals(provinceCode)) {
                    helper.setText(R.id.tv_area_type, "陕内");
                } else if ("620000".equals(provinceCode)) {
                    helper.setText(R.id.tv_area_type, "甘内");
                } else if ("630000".equals(provinceCode)) {
                    helper.setText(R.id.tv_area_type, "青内");
                } else if ("640000".equals(provinceCode)) {
                    helper.setText(R.id.tv_area_type, "宁内");
                } else if ("650000".equals(provinceCode)) {
                    helper.setText(R.id.tv_area_type, "新内");
                } else {
                    helper.setText(R.id.tv_area_type, "");
                    helper.setVisible(R.id.tv_area_type, false);
                }

            } else if ("1".equals(areaType)) {
                if ("110000".equals(provinceCode)) {
                    helper.setText(R.id.tv_area_type, "入京");
                } else if ("120000".equals(provinceCode)) {
                    helper.setText(R.id.tv_area_type, "入津");
                } else if ("130000".equals(provinceCode)) {
                    helper.setText(R.id.tv_area_type, "入冀");
                } else if ("140000".equals(provinceCode)) {
                    helper.setText(R.id.tv_area_type, "入晋");
                } else if ("150000".equals(provinceCode)) {
                    helper.setText(R.id.tv_area_type, "入蒙");
                } else if ("210000".equals(provinceCode)) {
                    helper.setText(R.id.tv_area_type, "入辽");
                } else if ("220000".equals(provinceCode)) {
                    helper.setText(R.id.tv_area_type, "入吉");
                } else if ("230000".equals(provinceCode)) {
                    helper.setText(R.id.tv_area_type, "入黑");
                } else if ("310000".equals(provinceCode)) {
                    helper.setText(R.id.tv_area_type, "入沪");
                } else if ("320000".equals(provinceCode)) {
                    helper.setText(R.id.tv_area_type, "入苏");
                } else if ("330000".equals(provinceCode)) {
                    helper.setText(R.id.tv_area_type, "入浙");
                } else if ("340000".equals(provinceCode)) {
                    helper.setText(R.id.tv_area_type, "入晥");
                } else if ("350000".equals(provinceCode)) {
                    helper.setText(R.id.tv_area_type, "入闽");
                } else if ("360000".equals(provinceCode)) {
                    helper.setText(R.id.tv_area_type, "入赣");
                } else if ("370000".equals(provinceCode)) {
                    helper.setText(R.id.tv_area_type, "入鲁");
                } else if ("440000".equals(provinceCode)) {
                    helper.setText(R.id.tv_area_type, "入粤");
                } else if ("450000".equals(provinceCode)) {
                    helper.setText(R.id.tv_area_type, "入桂");
                } else if ("460000".equals(provinceCode)) {
                    helper.setText(R.id.tv_area_type, "入琼");
                } else if ("410000".equals(provinceCode)) {
                    helper.setText(R.id.tv_area_type, "入豫");
                } else if ("420000".equals(provinceCode)) {
                    helper.setText(R.id.tv_area_type, "入鄂");
                } else if ("430000".equals(provinceCode)) {
                    helper.setText(R.id.tv_area_type, "入湘");
                } else if ("500000".equals(provinceCode)) {
                    helper.setText(R.id.tv_area_type, "入渝");
                } else if ("510000".equals(provinceCode)) {
                    helper.setText(R.id.tv_area_type, "入川");
                } else if ("520000".equals(provinceCode)) {
                    helper.setText(R.id.tv_area_type, "入黔");
                } else if ("530000".equals(provinceCode)) {
                    helper.setText(R.id.tv_area_type, "入滇");
                } else if ("540000".equals(provinceCode)) {
                    helper.setText(R.id.tv_area_type, "入藏");
                } else if ("610000".equals(provinceCode)) {
                    helper.setText(R.id.tv_area_type, "入陕");
                } else if ("620000".equals(provinceCode)) {
                    helper.setText(R.id.tv_area_type, "入甘");
                } else if ("630000".equals(provinceCode)) {
                    helper.setText(R.id.tv_area_type, "入青");
                } else if ("640000".equals(provinceCode)) {
                    helper.setText(R.id.tv_area_type, "入宁");
                } else if ("650000".equals(provinceCode)) {
                    helper.setText(R.id.tv_area_type, "入新");
                } else {
                    helper.setText(R.id.tv_area_type, "");
                    helper.setVisible(R.id.tv_area_type, false);
                }
            }
        } else {
            helper.setText(R.id.tv_area_type, "");
            helper.setVisible(R.id.tv_area_type, false);
        }

    }
}

