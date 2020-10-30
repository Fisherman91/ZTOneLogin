package com.zt.verification;

import android.app.Application;

import com.zt.verification.util.OneLoginUtils;
//import com.zt.verifylibrary.ZVerificationInterface;
//import com.zt.verifylibrary.utils.ToastUtils;

/**
 * 作者：created by LiuGang on 2020-01-05 23:30
 */
public class MyApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();

//        ZVerificationInterface.getInstance().init(this, "300011958919", "5F91B90DF2698E3662AB754220E5D82F", BuildConfig.DEBUG
//                , new ZVerificationInterface.InitListener() {
//                    @Override
//                    public void getInitStatus(int code, String result) {
//                        ToastUtils.showShortText(result);
//                    }
//                });、、


        /**
         * OneLogin 初始化
         */
        OneLoginUtils.init(this);


    }
}
