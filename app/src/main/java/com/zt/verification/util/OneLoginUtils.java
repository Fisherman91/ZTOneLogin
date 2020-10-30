package com.zt.verification.util;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.RelativeLayout;


import com.geetest.onelogin.OneLoginHelper;
import com.geetest.onelogin.config.AuthRegisterViewConfig;
import com.geetest.onelogin.config.OneLoginThemeConfig;
import com.geetest.onelogin.listener.AbstractOneLoginListener;
import com.geetest.onelogin.listener.CustomInterface;
import com.zt.verification.R;
import com.zt.verification.activity.SuccessActivity;
import com.zt.verification.consts.Constants;
import com.zt.verification.holder.Holder;

import org.json.JSONException;
import org.json.JSONObject;

import static com.zt.verification.consts.Constants.ONELOGIN_DIALOG_MODE;
import static com.zt.verification.consts.Constants.ONELOGIN_FLOAT_MODE;
import static com.zt.verification.consts.Constants.ONELOGIN_FULLSCREEN_MODE;
import static com.zt.verification.consts.Constants.ONELOGIN_LANDSCAPE_MODE;
import static com.zt.verification.consts.Constants.TAG;


/**
 * OneLogin 工具类
 * 管理一键登录 SDK 的初始化、预取号/取号，以及通过获取的 token 请求服务端换取手机号
 */
public class OneLoginUtils {

    private Handler backHandler;
    private Handler mainHandler = new Handler(Looper.getMainLooper());
    private Activity context;
    private Activity ongloginActivity;
    private OneLoginResult oneLoginResult;
    private ProgressDialog progressDialog;

    /**
     * 一键登录取号时是否使用自定义 loading 对话框
     */
    private boolean useCustomLoading = true;

    public OneLoginUtils(Activity context, OneLoginResult oneLoginResult) {
        this.context = context;
        this.oneLoginResult = oneLoginResult;
        HandlerThread handlerThread = new HandlerThread("oneLogin-demo");
        handlerThread.start();
        backHandler = new Handler(handlerThread.getLooper());
    }

    /**
     * 初始化注册接口
     */
    public static void init(Context context) {
        OneLoginHelper.with()
                //开启日志打印功能
                .setLogEnable(true)
                /**
                 * OneLogin 与 OnePass 属于不同的产品，注意产品 APPID 不可混用，请在后台分别申请对用的 APPID
                 */
                .init(context, Constants.APP_ID_OL)
                //新的 init 接口第二个参数支持配置 APP_ID，register 接口无需重复配置，可传空字符串
                .register(null, 8000);
    }

    /**
     * 配置页面布局(默认竖屏)
     * 注意事项：
     * 1. 配置项的图片，地址都应为`drawable`目录下资源文件名称，支持配置静态 png/jpg 图片，或者带状态选择的 xml 文件
     * 2. OffsetY 与 OffsetY_B 两者必须有一个值为 0,偏移量以不为 0 的方向作为基准。
     * 3. 颜色为int类型
     *
     * @return config
     */
    private OneLoginThemeConfig initConfig() {
        int screenWidth = context.getResources().getDisplayMetrics().widthPixels;
        int screenHeight = context.getResources().getDisplayMetrics().heightPixels;
        int width = DensityUtils.px2dip(context, screenWidth);
        int popWidth = width * 4 / 5;
        int popHeight = DensityUtils.px2dip(context, screenHeight) * 3 / 5;
        int navigationHeight = DensityUtils.px2dip(context, getNavigationBarHeight(context));

        switch (Holder.with().getOneLoginStyle()) {
            case ONELOGIN_FLOAT_MODE:
                return new OneLoginThemeConfig.Builder()
                        .setDialogTheme(true, width, 500, 0, 0, true, true)
                        .setAuthBGImgPath("gt_one_login_bg")
                        .setStatusBar(0, 0, false)
                        .setAuthNavLayout(0xFF3973FF, 49, true, false)
                        .setAuthNavTextView("一键登录", 0xFFFFFFFF, 17, false, "服务条款", 0xFF000000, 17)
                        .setAuthNavReturnImgView("gt_one_login_ic_chevron_left_black", 40, 40, false, 8)
                        .setLogoImgView("gt_one_login_logo", 71, 71, false, 125 - navigationHeight, 0, 0)
                        .setSloganView(0xFFA8A8A8, 10, 370 - navigationHeight, 0, 0)
                        .setNumberView(0xFF3D424C, 24, 200 - navigationHeight, 0, 0)
                        .setSwitchView("切换账号", 0xFF3973FF, 14, false, 249 - navigationHeight, 0, 0)
                        .setLogBtnLayout("gt_one_login_btn_normal", 290, 45, 310 - navigationHeight, 0, 0)
                        .setLogBtnTextView("一键登录", 0xFFFFFFFF, 18)
                        .setLogBtnLoadingView(useCustomLoading ? "" : "umcsdk_load_dot_white", 20, 20, 12)
                        .setPrivacyCheckBox("gt_one_login_unchecked", "gt_one_login_checked", true, 9, 9)
                        .setPrivacyLayout(256, 0, 18, 0, true)
                        .setPrivacyClauseView(0xFFA8A8A8, 0xFF3973FF, 10)
                        .setPrivacyTextView("登录即同意", "和", "、", "并使用本机号码登录")
                        .build();
            case ONELOGIN_DIALOG_MODE:
                return new OneLoginThemeConfig.Builder()
                        .setDialogTheme(true, popWidth, popHeight, 0, 0, false, true)
                        .setAuthNavReturnImgView("gt_one_login_ic_chevron_left_black", 40, 40, false, 8)
                        .setLogoImgView("gt_one_login_logo", 71, 71, false, 60, 0, 0)
                        .setSloganView(0xFFA8A8A8, 10, 270, 0, 0)
                        .setNumberView(0xFF3D424C, 24, 125, 0, 0)
                        .setSwitchView("切换账号", 0xFF3973FF, 14, false, 165, 0, 0)
                        .setLogBtnLayout("gt_one_login_btn_normal", 268, 45, 220, 0, 0)
                        .setLogBtnTextView("一键登录", 0xFFFFFFFF, 18)
                        .setLogBtnLoadingView(useCustomLoading ? "" : "umcsdk_load_dot_white", 20, 20, 12)
                        .setPrivacyLayout(256, 0, 1, 0, true)
                        .build();
            case ONELOGIN_FULLSCREEN_MODE:
//                // 如需配置slogan、号码栏、切换账号等控件左右相对对齐，需要预先计算x偏移值
//                Paint paint = new Paint();
//                paint.setTextSize(DensityUtils.dip2px(context, 14));
//                int switchWidth = DensityUtils.px2dip(context, (int)paint.measureText("切换账号"));
//                int buttonWidth = 290;
//                int numberLeft = (width - buttonWidth) / 2;
//                int switchLeft = width - switchWidth - (width - buttonWidth) / 2;

                return new OneLoginThemeConfig.Builder()
                        .setStatusBar(0xffffffff, 0xffffffff, true)
                        .setAuthNavReturnImgView("gt_one_login_ic_chevron_left_black", 40, 40, false, 8)
                        .setLogBtnLayout("gt_one_login_btn_normal", 290, 45, 310, 0, 0)
                        .setLogBtnTextView("一键登录", 0xFFFFFFFF, 18)
                        .setLogBtnLoadingView(useCustomLoading ? "" : "umcsdk_load_dot_white", 20, 20, 12)
//                        .setNumberView(0xFF3D424C, 24, 250, 0, numberLeft)
//                        .setSwitchView("切换账号", 0xFF3973FF, 14, false, 250, 0, switchLeft)
//                        .setSwitchViewLayout("", switchWidth, 25)
                        .setPrivacyLayout(280, 0, 18, 0, true)
                        .setPrivacyCheckBox("gt_one_login_unchecked", "gt_one_login_checked", true, 9, 9, 4)
                        .setPrivacyClauseView(Color.parseColor("#80838A"), Color.parseColor("#FFC800"), 12)
                        .setPrivacyClauseViewTypeface(Typeface.defaultFromStyle(Typeface.BOLD), Typeface.defaultFromStyle(Typeface.NORMAL))
                        .setPrivacyTextView("我已阅读并同意", "", "", " 并使用本机号码登录")
//                        .setPrivacyClauseText("用户协议", "https://docs.geetest.com/onelogin/deploy/android",
//                                "隐私政策", "https://docs.geetest.com/onelogin/deploy/android",
//                                null, null
//                        )
                        // 预留一组条款，SDK 填充运营商隐私条款
//                        .setPrivacyClauseTextStrings("我已阅读并同意", "产品概述", "https://docs.geetest.com/onelogin/overview/prodes/", "",
//                                "和", "Android 开发文档", "https://docs.geetest.com/onelogin/deploy/android", "",
//                                "和", "常见问题", "https://docs.geetest.com/onelogin/help/faq", "",
//                                "和", "", "", "并使用本机号码登录")
                        .setPrivacyAddFrenchQuotes(true)
                        .build();
            case ONELOGIN_LANDSCAPE_MODE:
                return new OneLoginThemeConfig.Builder()
                        .setAuthBGImgPath("gt_one_login_bg")
                        .setDialogTheme(false, 300, 500, 0, 0, false, false)
                        .setStatusBar(0xffffffff, 0xffffffff, true)
                        .setAuthNavLayout(0xFF3973FF, 49, true, false)
                        .setAuthNavTextView("一键登录", 0xFFFFFFFF, 17, false, "服务条款", 0xFF000000, 17)
                        .setAuthNavReturnImgView("gt_one_login_ic_chevron_left_black", 45, 45, false, 8)
                        .setLogoImgView("gt_one_login_logo", 71, 71, false, 55, 0, 0)
                        .setSloganView(0xFFA8A8A8, 10, 226, 0, 0)
                        .setNumberView(0xFF3D424C, 24, 84, 0, 0)
                        .setSwitchView("切换账号", 0xFF3973FF, 14, false, 128, 0, 0)
                        .setLogBtnLayout("gt_one_login_btn_normal", 268, 36, 169, 0, 0)
                        .setLogBtnTextView("一键登录", 0xFFFFFFFF, 15)
                        .setLogBtnLoadingView(useCustomLoading ? "" : "umcsdk_load_dot_white", 20, 20, 12)
                        .setPrivacyCheckBox("gt_one_login_unchecked", "gt_one_login_checked", true, 9, 9)
                        .setPrivacyLayout(268, 0, 1, 0, true)
                        .setPrivacyClauseView(0xFFA8A8A8, 0xFF3973FF, 10)
                        .setPrivacyTextView("登录即同意", "和", "、", "并使用本机号码登录")
                        .build();
        }
        return new OneLoginThemeConfig.Builder()
                .setLogBtnLoadingView("loading_icon", 20, 20, 12)
                .setAuthNavReturnImgView("gt_one_login_ic_chevron_left_black", 40, 40, false, 8)
                .build();
    }

    /**
     * 自定义控件
     * 注意：横屏的自定义控件的边距需要自己再重新按照设计图设计
     * Demo中自定义控件只支持竖屏，横屏的自定义控件需要自己实现
     */
    private void initView() {
        Button mTitleBtn = new Button(context);
        mTitleBtn.setText("其他");
        mTitleBtn.setTextColor(0xff000000);
        mTitleBtn.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
        mTitleBtn.setBackgroundColor(Color.TRANSPARENT);
        RelativeLayout.LayoutParams mLayoutParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        mLayoutParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, RelativeLayout.TRUE);
        mTitleBtn.setLayoutParams(mLayoutParams);


        Button mBtn = new Button(context);
        mBtn.setText("其他方式登录");
        mBtn.setTextColor(0xff3a404c);
        mBtn.setBackgroundColor(Color.TRANSPARENT);
        mBtn.setTextSize(TypedValue.COMPLEX_UNIT_SP, 13);
        RelativeLayout.LayoutParams mLayoutParams1 = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        mLayoutParams1.addRule(RelativeLayout.CENTER_HORIZONTAL, RelativeLayout.TRUE);
        mLayoutParams1.setMargins(0, DensityUtils.dip2px(context, 400), 0, 0);
        mBtn.setLayoutParams(mLayoutParams1);


//        OneLoginHelper.with().addOneLoginRegisterViewConfig("title_button", new AuthRegisterViewConfig.Builder()
//                .setView(mTitleBtn)
//                .setRootViewId(AuthRegisterViewConfig.RootViewId.ROOT_VIEW_ID_TITLE_BAR)
//                .setCustomInterface(context -> {
//                    Utils.toastMessage(context, "动态注册的其他按钮");
//                })
//                .build()
//        ).addOneLoginRegisterViewConfig("text_button", new AuthRegisterViewConfig.Builder()
//                .setView(mBtn)
//                .setRootViewId(AuthRegisterViewConfig.RootViewId.ROOT_VIEW_ID_BODY)
//                .setCustomInterface(context -> {
//                    Utils.toastMessage(context, "动态注册的其他登录按钮");
//                })
//                .build()
//        );
    }

    /**
     * 定义的第三方登录设置
     */
    private void initLogin() {
        LayoutInflater inflater1 = LayoutInflater.from(context);
        RelativeLayout relativeLayout = (RelativeLayout) inflater1.inflate(R.layout.layout_other_login, null);
        RelativeLayout.LayoutParams layoutParamsOther = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        layoutParamsOther.setMargins(0, DensityUtils.dip2px(context, 430), 0, 0);
        layoutParamsOther.addRule(RelativeLayout.CENTER_HORIZONTAL);
        relativeLayout.setLayoutParams(layoutParamsOther);
        relativeLayout.findViewById(R.id.weixin).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Utils.toastMessage(context, "微信登录");
            }
        });
        relativeLayout.findViewById(R.id.qq).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Utils.toastMessage(context, "qq登录");
            }
        });
        relativeLayout.findViewById(R.id.weibo).setOnClickListener(new View.OnClickListener() {
                                                                       @Override
                                                                       public void onClick(View v) {
                                                                           Utils.toastMessage(context, "微博登录");

                                                                       }
                                                                   }
        );
        OneLoginHelper.with().addOneLoginRegisterViewConfig("title_button", new AuthRegisterViewConfig.Builder()
                .setView(relativeLayout)
                .setRootViewId(AuthRegisterViewConfig.RootViewId.ROOT_VIEW_ID_BODY)
                .setCustomInterface(new CustomInterface() {
                    @Override
                    public void onClick(Context context) {
                        Utils.toastMessage(context, "动态注册的其他按钮");
                    }
                })
                .build()
        );
    }

    /**
     * 点击直接进行取号
     */
    public void requestToken() {
        oneLoginRequestToken();
    }

    /**
     * 取号接口
     * 在这个方法里也可以配置自定义的布局页面
     * 比如    initView() initLogin()
     * 注意:0.8.0之后的版本loading由用户自己控制消失时间
     */
    private void oneLoginRequestToken() {
        if (Holder.with().getOneLoginStyle() == ONELOGIN_FULLSCREEN_MODE) {
            //三方登录自定义 UI 配置，可选配
            initLogin();
        }
        OneLoginHelper.with().requestToken(initConfig(), oneLoginListener);
    }

    private AbstractOneLoginListener oneLoginListener = new AbstractOneLoginListener() {
        /**
         * 拉起授权页、点一键登录取号以及授权页其他用户操作的回调
         * @param jsonObject
         */
        @Override
        public void onResult(final JSONObject jsonObject) {
            Log.i(TAG, "取号结果为：" + jsonObject.toString());
            try {
                int status = jsonObject.getInt("status");
                //status=200 表示取号成功，可携带 process_id、token、authcode 去服务端换取手机号
                if (status == Constants.SUCCESS_CODE) {
                    final String process_id = jsonObject.getString("process_id");
                    final String token = jsonObject.getString("token");
                    /**
                     * authcode值在电信卡通过 token 换取手机号时必要参数，为了避免服务端校验出错，尽量三网都传
                     */
                    final String authCode = jsonObject.optString("authcode");
                    backHandler.post(() -> verify(process_id, token, authCode));
                } else {
                    //在页面返回失败的情况下 如果不注重实用返回的监听事件，可以对于返回的事件不进行处理
                    String errorCode = jsonObject.getString("errorCode");
                    if (errorCode.equals("-20301") || errorCode.equals("-20302")) {
                        onVerifyEnd();
                        Log.d(TAG, "用户点击返回键关闭了授权页面");
                        return;
                    }
                    // 用户选择切换账号登录，收到此回调后可关闭授权页，跳转或降级走其他方式登录，或者待用户选择
                    // 重新拉起授权页进行一键登录
                    if (errorCode.equals("-20303")) {
                        Log.d(TAG, "用户点击切换账号");
                        Utils.toastMessage(context, "根据实际需求进行定制开发，具体参考文档");

                        return;
                    }
                    onVerifyEnd();
                    OneLoginHelper.with().dismissAuthActivity();
                    //其他错误码产生原因与处理方式，请参考部署文档中的错误码说明
                    Utils.toastMessage(context, "取号失败:" + jsonObject.toString());
                }
            } catch (JSONException e) {
                onVerifyEnd();
                OneLoginHelper.with().dismissAuthActivity();
                Utils.toastMessage(context, "取号失败:" + jsonObject.toString());
            }
        }

        /**
         * 授权页面点击隐私协议条款的回调
         * 如有需要自定义隐私条款页，可在此回调中跳转到自定义隐私条款页展示隐私条款
         * 自定义隐私条款页参考 setPrivacyLayout 接口说明
         * @param s  隐私条款名字
         * @param s1 隐私条款URL
         */
        @Override
        public void onPrivacyClick(String s, String s1) {
            Log.d(TAG, "当前点击了隐私条款名为：" + s + "---地址为:" + s1);
        }

        /**
         * 授权页点击一键登录按钮的回调
         */
        @Override
        public void onLoginButtonClick() {
            Log.d(TAG, "当前点击了登录按钮");
        }

        /**
         * 授权页点击切换账号按钮的回调
         * 2.1.4 版本新增，同时 onResult 回调中仍然会返回 -20303 返回码的回调结果
         */
        @Override
        public void onSwitchButtonClick() {
            super.onSwitchButtonClick();
        }

        /**
         * 授权页点击标题栏返回按钮或者手机返回键的回调
         * 2.1.4 版本新增，同时 onResult 回调中仍然会返回 -20301 和 -20302 返回码的回调结果
         */
        @Override
        public void onBackButtonClick() {
            super.onBackButtonClick();
        }

        /**
         * 拉起授权页时返回脱敏手机号的回调
         * @param s 脱敏手机号
         */
        @Override
        public void onRequestTokenSecurityPhone(String s) {
            super.onRequestTokenSecurityPhone(s);
        }

        /**
         * 授权页点击一键登录按钮后开始启动 loading 的回调
         * 当用户未集成第三方验证时，onLoginButtonClick 与 onLoginLoading 先后几乎同时发回
         * 当用户集成了第三方验证时，onLoginButtonClick 在点击一键登录时立即发回调，
         *  onLoginLoading 在验证结束后开始取号时发回调
         */
        @Override
        public void onLoginLoading() {
            Log.d(TAG, "开始加载 loading");
            /**
             * 如需自定义对话框式 loading，可在此处开始显示自定义 loading 对话框
             */
            if (useCustomLoading && ongloginActivity != null) {
                progressDialog = ProgressDialog.show(ongloginActivity, null, "一键登录取号中", true, true);
            }
        }

        /**
         * 拉起授权页成功时授权页 Activity 创建时的回调
         * @param activity 授权页 Activity 实例
         */
        @Override
        public void onAuthActivityCreate(Activity activity) {
            Log.d(TAG, "当前弹起授权页面:" + activity.getClass().getSimpleName());
            //如需设置弹框模式背景蒙层透明度，可在此处设置
            //activity.getWindow().setDimAmount(0.8f);
        }

        /**
         * 点一键登录之前用户可增加一些其他额外的校验功能，防止被异常攻击
         * @return true 接入其他验证, 验证成功结束后调用 requestTokenDelay 启动正常取号
         *         false 默认验证
         */
        @Override
        public boolean onRequestOtherVerify() {
            return super.onRequestOtherVerify();
        }

        /**
         * 用户点击授权页隐私条款项跳转到隐私页显示隐私条款内容，隐私页 Activity 创建时的回调
         * 用户若自定义了隐私页，就不会受到该回调
         * @param activity 隐私条款页 Activity 实例
         */
        @Override
        public void onAuthWebActivityCreate(Activity activity) {
            Log.d(TAG, "当前弹起授权Web页面:" + activity.getClass().getSimpleName());
        }

        /**
         * 授权页隐私栏选择框点击事件回调
         * @param b 选择框当前的选择状态
         */
        @Override
        public void onPrivacyCheckBoxClick(boolean b) {
            Log.d(TAG, "当前点击了CheckBox---" + b);
        }
    };

    /**
     * 手机号校验接口 需要网站主配置相关接口进行获取手机号等操作
     * 在这个阶段，也需要调用OneLoginHelper.with().dismissAuthActivity()来进行授权页的销毁
     * 注意:0.8.0之后的版本loading由用户自己控制消失时间
     */
    private void verify(String id, String token, String authCode) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("process_id", id);
            jsonObject.put("token", token);
            jsonObject.put("authcode", authCode);
            /**
             * 注意，此处的id_2_sign参数是<url>http://z.zthysms.com:23004/app/oneLogin</url>服务端demo接口所需要的参数
             * 具体的参数与网站主服务端有关
             */
            jsonObject.put("id_2_sign", Constants.APP_ID_OL);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        final String result = HttpUtils.requestNetwork(Constants.CHECK_PHONE_URL, jsonObject);
        Log.i(TAG, "校验结果为:" + result);
        mainHandler.post(() -> {
            /**
             * 关闭loading动画
             */
            OneLoginHelper.with().stopLoading();

            /**
             * 关闭授权页面
             * sdk内部除了返回或初始化异常以外是不关闭授权页面的，需要开发者自行关闭
             */
            OneLoginHelper.with().dismissAuthActivity();
            onVerifyEnd();
            try {
                JSONObject jsonObject1 = new JSONObject(result);
                int status = jsonObject1.getInt("status");
                if (status == Constants.SUCCESS_CODE) {
                    context.startActivity(new Intent(context, SuccessActivity.class));
                } else {
                    Utils.toastMessage(context, "校验失败, status = " + status);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        });
    }

    /**
     * 一键登录结束
     */
    private void onVerifyEnd() {
        oneLoginResult.onResult();
        if (useCustomLoading) {
            if (progressDialog != null) {
                progressDialog.dismiss();
                progressDialog = null;
            }
            ongloginActivity = null;
        }
    }

    /**
     * 判断是否有NavigationBar
     *
     * @param activity
     * @return
     */
    public static boolean checkHasNavigationBar(Activity activity) {
        WindowManager windowManager = activity.getWindowManager();
        Display d = windowManager.getDefaultDisplay();

        DisplayMetrics realDisplayMetrics = new DisplayMetrics();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            d.getRealMetrics(realDisplayMetrics);
        }

        int realHeight = realDisplayMetrics.heightPixels;
        int realWidth = realDisplayMetrics.widthPixels;

        DisplayMetrics displayMetrics = new DisplayMetrics();
        d.getMetrics(displayMetrics);

        int displayHeight = displayMetrics.heightPixels;
        int displayWidth = displayMetrics.widthPixels;

        return (realWidth - displayWidth) > 0 || (realHeight - displayHeight) > 0;
    }

    /**
     * 获得NavigationBar的高度
     */
    public static int getNavigationBarHeight(Activity activity) {
        int result = 0;
        Resources resources = activity.getResources();
        int resourceId = resources.getIdentifier("navigation_bar_height", "dimen", "android");
        if (resourceId > 0 && checkHasNavigationBar(activity)) {
            result = resources.getDimensionPixelSize(resourceId);
        }
        return result;
    }
}
