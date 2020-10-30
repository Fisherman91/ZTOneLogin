package com.zt.verification.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;

import com.geetest.onelogin.OneLoginHelper;
import com.zt.verification.R;
import com.zt.verification.consts.Constants;
import com.zt.verification.holder.Holder;
import com.zt.verification.util.GlideUtils;
import com.zt.verification.util.OneLoginResult;
import com.zt.verification.util.OneLoginUtils;
import com.zt.verification.util.Utils;


public class OneLoginDialogStyleActivity extends Activity {
    private ImageView phoneImage;
    private boolean isDialog = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Holder.with().getOneLoginStyle() == Constants.ONELOGIN_DIALOG_MODE) {
            isDialog = true;
        }
        OneLoginUtils oneLoginUtils = new OneLoginUtils(this, oneLoginResult);

        if (OneLoginHelper.with().isPreGetTokenResultValidate()) {
            oneLoginUtils.requestToken();
            return;
        } else {
            oneLoginUtils.requestToken();
            setContentView(R.layout.activity_onelogin_dialog_style);
        }

        phoneImage = findViewById(R.id.phone_iv);
        getWindow().setWindowAnimations(R.style.AnimBottom);

        if (isDialog) {
            getWindow().setLayout(Utils.getMinEdge(getApplicationContext())*4/5, ViewGroup.LayoutParams.WRAP_CONTENT);
        } else {
            getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            getWindow().getAttributes().gravity = Gravity.BOTTOM;
        }

        GlideUtils.loadGifResource(this, R.drawable.getphone, phoneImage);
    }

    private OneLoginResult oneLoginResult = new OneLoginResult() {
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
