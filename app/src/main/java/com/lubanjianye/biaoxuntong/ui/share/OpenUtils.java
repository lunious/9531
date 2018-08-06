package com.lubanjianye.biaoxuntong.ui.share;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.IdRes;


public class OpenUtils {

    public static Bitmap getShareBitmap(Context context, @IdRes int resId) {
        return BitmapFactory.decodeResource(context.getResources(), resId);
    }

    public static String buildTransaction(final String type) {
        return (type == null) ? String.valueOf(System.currentTimeMillis()) : type + System
                .currentTimeMillis();
    }
}

