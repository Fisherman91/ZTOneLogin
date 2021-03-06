package com.zt.verification.util;

import android.content.Context;

public class DensityUtils {
    /**
     * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
     */
    public static int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    /**
     * 根据手机的分辨率从 px(像素) 的单位 转成为 dp
     */
    public static int px2dip(Context context, float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }

    /**
     * 根据手机的分辨率获取窄边值 单位:px
     */
    public static int getMinEdge(Context context) {
        Context appContext = context.getApplicationContext();
        int w = appContext.getResources().getDisplayMetrics().widthPixels;
        int h = appContext.getResources().getDisplayMetrics().heightPixels;
        return Math.min(w, h);
    }
}
