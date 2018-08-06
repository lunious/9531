package com.lubanjianye.biaoxuntong.ui.citypicker.adapter;


import com.lubanjianye.biaoxuntong.ui.citypicker.model.City;

public interface OnPickListener {
    void onPick(int position, City data);
    void onLocate();
}
