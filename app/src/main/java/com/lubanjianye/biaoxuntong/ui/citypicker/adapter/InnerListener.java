package com.lubanjianye.biaoxuntong.ui.citypicker.adapter;

import com.lubanjianye.biaoxuntong.ui.citypicker.model.City;

public interface InnerListener {
    void dismiss(int position, City data);
    void locate();
}
