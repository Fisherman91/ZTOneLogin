package com.zt.verification.util;

import android.content.Context;
import android.net.ConnectivityManager;
import android.view.Gravity;
import android.widget.Toast;

import java.lang.reflect.Method;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Utils {
    /**
     * 判断数据网络是否开启
     *
     * @param context
     * @return
     */
    public static boolean haveIntent(Context context) {
        boolean mobileDataEnabled = false; // Assume disabled
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        try {
            Class cmClass = Class.forName(cm.getClass().getName());
            Method method = cmClass.getDeclaredMethod("getMobileDataEnabled");
            method.setAccessible(true); // Make the method callable
            // get the setting for "mobile data"
            mobileDataEnabled = (Boolean) method.invoke(cm);
        } catch (Exception e) {
            // Some problem accessible private API
            // TODO do whatever error handling you want here
        }
        return mobileDataEnabled;
    }

    /**
     * 判断手机号合法性
     *
     * @param phoneNumber 手机号
     * @return
     */
    public static boolean checkPhoneNum(String phoneNumber) {
        String regExp = "^((13[0-9])|(15[^4])|(18[0-9])|(17[0-8])|(14[5-9])|(166)|(19[8,9])|)\\d{8}$";
        Pattern p = Pattern.compile(regExp);
        Matcher m = p.matcher(phoneNumber);
        return m.matches();
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

    private static Toast toast;

    /**
     * Toast 短通知
     * @param context
     * @param param
     */
    public static void toastMessage(Context context, String param) {
        try {
            checkToastIsNull();

            toast = Toast.makeText(context.getApplicationContext(), param, Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.CENTER, 0, 0);
            toast.show();
        } catch (Exception e) {
            cancelToast();
        }
    }

    private static void checkToastIsNull() {
        if (toast != null) {
            toast.cancel();
            toast = null;
        }
    }

    public static void cancelToast() {
        if (toast != null) {
            toast.cancel();
            toast = null;
        }
    }
}
