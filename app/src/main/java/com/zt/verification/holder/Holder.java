package com.zt.verification.holder;


import com.zt.verification.consts.Constants;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import androidx.annotation.IntDef;

public class Holder {
    /**
     * 当前对象
     */
    private volatile static Holder holder;

    /**
     * 窗体模式 默认竖屏全屏沉浸式
     * 0:浮窗式
     * 1:弹窗式
     * 2:沉浸式
     * 3:横屏
     */
    private @OneLoginStyle int olStyle = Constants.ONELOGIN_FULLSCREEN_MODE;

    /**
     * 标题文字
     */
    private String text = "注册";

    /**
     * 构造方法
     */
    private Holder() {
    }

    /**
     * 初始化
     *
     * @return <>当前的对象</>
     */
    public static Holder with() {
        if (holder == null) {
            synchronized (Holder.class) {
                if (holder == null) {
                    holder = new Holder();
                }
            }
        }
        return holder;
    }

    @IntDef({Constants.ONELOGIN_FLOAT_MODE, Constants.ONELOGIN_DIALOG_MODE, Constants.ONELOGIN_FULLSCREEN_MODE, Constants.ONELOGIN_LANDSCAPE_MODE})
    @Retention(RetentionPolicy.SOURCE)
    public @interface OneLoginStyle {}

    public @OneLoginStyle int getOneLoginStyle() {
        return olStyle;
    }

    public void setOneLoginStyle(@OneLoginStyle int olStyle) {
        this.olStyle = olStyle;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    private boolean enableSenseBot = false;

    public boolean isEnableSenseBot() {
        return enableSenseBot;
    }

    public void setEnableSenseBot(boolean enableSenseBot) {
        this.enableSenseBot = enableSenseBot;
    }
}
