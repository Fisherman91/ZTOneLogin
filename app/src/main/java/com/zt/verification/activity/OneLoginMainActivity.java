package com.zt.verification.activity;

import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.widget.ImageView;

import com.zt.verification.R;
import com.zt.verification.consts.Constants;
import com.zt.verification.holder.Holder;
import com.zt.verification.util.GlideUtils;
import com.zt.verification.util.OneLoginResult;
import com.zt.verification.util.OneLoginUtils;
import com.zt.verification.util.WindowUtils;


public class OneLoginMainActivity extends BaseTitleActivity {
    private ImageView phoneImage;
    private boolean isPortrait = false;
    private OneLoginUtils oneLoginUtils;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_onelogin_main);

        phoneImage = findViewById(R.id.phone_iv);
        getWindow().setWindowAnimations(R.style.AnimBottom);
        oneLoginUtils = new OneLoginUtils(this, oneLoginResult);

        if (Holder.with().getOneLoginStyle() == Constants.ONELOGIN_FULLSCREEN_MODE) {
            isPortrait = true;
        } else {
            isPortrait = false;
        }

        // 设置当前页面的方向，实际使用建议在 AndroidManifest 中配置
        setRequestedOrientation(isPortrait ? ActivityInfo.SCREEN_ORIENTATION_PORTRAIT : ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

        WindowUtils.setStatusBar(this);
        GlideUtils.loadGifResource(this, R.drawable.getphone, phoneImage);
        oneLoginUtils.requestToken();
    }

    private OneLoginResult oneLoginResult = new OneLoginResult() {
        /**
         * 授权页面返回结果后回调处理，关闭当前页面
         */
        @Override
        public void onResult() {
            finish();
        }

        @Override
        public void onResult(int flag) {
            super.onResult(flag);
            onResult();
        }
    };
}

