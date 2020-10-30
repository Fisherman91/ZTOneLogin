package com.zt.verification.consts;

/**
 * 公共常量配置
 */
public class Constants {
    public static final String TAG = "Zhutong_OneLogin";

    /**
     * 服务器配置的verifyUrl接口地址<>需要用到服务SDK</>
     * 当前验证地址仅供演示Demo使用，实际使用请配置为自定义校验的服务端地址
     */
    public static final String BASE_URL = "http://z.zthysms.com:23004/";


    /**
     * *******************************************************************************
     * OneLogin 与 OnePass 属于不同的产品，注意产品 APPID 不可混用，请在后台分别创建对应的应用
     * *******************************************************************************
     */
    /**
     * 后台申请的 OneLogin APP_ID
     * 当前APP_ID仅供演示Demo使用，如果修改了应用包名或者签名信息，请使用申请的APP_ID
     * 谨记：APP_ID需绑定相关的包名和包签名(提供这两项信息从后台申请APP_ID)
     */
    public static final String APP_ID_OL = "01187fad7e71fa90845b446819b968a4";
//    public static final String APP_ID_OL = "e4fcb3086ca25bbe2da08a09d75c70e8";
    /**
     * 后台申请的 OnePass APP_ID
     * 当前APP_ID仅供演示Demo使用，如果修改了应用包名或者签名信息，请使用申请的APP_ID
     * 谨记：APP_ID需绑定相关的包名和包签名(提供这两项信息从后台申请APP_ID)
     */
    public static final String APP_ID_OP = "98b9a4e3ece8ba3c99092344f303c9b5";
//    public static final String APP_ID_OP = "e18f5f45da5614928f4af0410e886e0a";


    /**
     * 后台配置的服务校验接口，该地址仅限Demo使用，如果修改了Demo包名与APP_ID，请勿使用该地址
     */
    public static final String CHECK_PHONE_URL = BASE_URL + "app/oneLogin";

    /**
     * CheckGateWay接口
     * 网关登录校验
     */
    public static final String CHECK_GATE_WAY = BASE_URL + "app/onePass";

    /**
     * 请求成功的响应值
     */
    public static final int SUCCESS_CODE = 200;

    /**
     * 一键登录 浮窗式
     */
    public static final int ONELOGIN_FLOAT_MODE = 0;
    /**
     * 一键登录 弹窗式
     */
    public static final int ONELOGIN_DIALOG_MODE = 1;
    /**
     * 一键登录 沉浸式
     */
    public static final int ONELOGIN_FULLSCREEN_MODE = 2;
    /**
     * 一键登录 横屏
     */
    public static final int ONELOGIN_LANDSCAPE_MODE = 3;
}
