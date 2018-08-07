package com.lubanjianye.biaoxuntong.bean;

/**
 * Author: lunious
 * Date: 2018/8/7 13:31
 * Description:
 */
import android.support.annotation.DrawableRes;

public interface CustomTabEntity {
    String getTabTitle();

    @DrawableRes
    int getTabSelectedIcon();

    @DrawableRes
    int getTabUnselectedIcon();
}