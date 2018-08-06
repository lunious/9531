package com.lubanjianye.biaoxuntong.util.dimen;

import android.content.res.Resources;
import android.util.DisplayMetrics;

import com.lubanjianye.biaoxuntong.app.BiaoXunTong;


/**
 * 项目名:   LuBanBiaoXunTong
 * 包名:     com.lubanjianye.biaoxuntong.core.util
 * 文件名:   DimenUtil
 * 创建者:   lunious
 * 创建时间: 2017/10/13  13:49
 * 描述:     获取宽高工具类
 */

public final class DimenUtil {
    public static int getScreenWidth() {
        final Resources resources = BiaoXunTong.getApplicationContext().getResources();
        final DisplayMetrics dm = resources.getDisplayMetrics();
        return dm.widthPixels;
    }

    public static int getScreenHeight() {
        final Resources resources = BiaoXunTong.getApplicationContext().getResources();
        final DisplayMetrics dm = resources.getDisplayMetrics();
        return dm.heightPixels;
    }
}
