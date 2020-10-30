package com.zt.verification.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.geetest.onelogin.OneLoginHelper;
import com.zt.verification.R;
import com.zt.verification.consts.Constants;
import com.zt.verification.holder.Holder;
import com.zt.verification.util.OneLoginResult;
import com.zt.verification.util.OneLoginUtils;


public class OneLoginStyleSelectActivity extends BaseTitleActivity {
    private TextView bigTv;
    private ImageView floatImage, popImage, fullScreenImage, landscapeImage;
    private OneLoginUtils oneLoginUtils;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_onelogin_style_select);
        setTitle(Holder.with().getText());
        bigTv = findViewById(R.id.onelogin_select_bigtv_tv);
        floatImage = findViewById(R.id.onelogin_select_float_iv);
        popImage = findViewById(R.id.onelogin_select_pop_iv);
        fullScreenImage = findViewById(R.id.onelogin_select_fullscreen_iv);
        landscapeImage = findViewById(R.id.onelogin_select_landscape_iv);

        bigTv.getPaint().setFakeBoldText(true);

        changeAlpha(floatImage);
        changeAlpha(popImage);
        changeAlpha(fullScreenImage);
        changeAlpha(landscapeImage);

        oneLoginUtils = new OneLoginUtils(this, oneLoginResult);

        floatImage.setOnClickListener(v -> {
            Holder.with().setEnableSenseBot(false);
            Holder.with().setOneLoginStyle(Constants.ONELOGIN_FLOAT_MODE);
            Intent intent = new Intent(OneLoginStyleSelectActivity.this, OneLoginDialogStyleActivity.class);
            startActivityForResult(intent,2);
        });

        popImage.setOnClickListener(v -> {
            Holder.with().setEnableSenseBot(false);
            Holder.with().setOneLoginStyle(Constants.ONELOGIN_DIALOG_MODE);
            Intent intent = new Intent(OneLoginStyleSelectActivity.this, OneLoginDialogStyleActivity.class);
            startActivityForResult(intent,2);
        });

        fullScreenImage.setOnClickListener(v -> {
            Holder.with().setEnableSenseBot(false);
            Holder.with().setOneLoginStyle(Constants.ONELOGIN_FULLSCREEN_MODE);
            if (OneLoginHelper.with().isPreGetTokenResultValidate()) {
                //预取号有效时，直接拉起授权页
                oneLoginUtils.requestToken();
                return;
            } else {
//                预取号无效时，可先加载自定义 loading 对话框，然后调用 requestToken，
//                SDK 内部会进行自动预取号，待授权页拉起后(当前 Activity onPause)即可关闭 loading 对话框
//                progressDialog = ProgressDialog.show(OneLoginStyleSelectActivity.this, null, "一键登录预取号中", true, true);
//                oneLoginUtils.requestToken();
            }
            Intent intent = new Intent(OneLoginStyleSelectActivity.this, OneLoginMainActivity.class);
            startActivityForResult(intent,2);
        });

        landscapeImage.setOnClickListener(v -> {
            Holder.with().setEnableSenseBot(false);
            Holder.with().setOneLoginStyle(Constants.ONELOGIN_LANDSCAPE_MODE);
            if (OneLoginHelper.with().isPreGetTokenResultValidate()) {
                if (getRequestedOrientation() != ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE) {
                    setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
                }
                oneLoginUtils.requestToken();
                return;
            }
            Intent intent = new Intent(OneLoginStyleSelectActivity.this, OneLoginMainActivity.class);
            startActivityForResult(intent,2);
        });
    }

    public void changeAlpha(View view){
        view.setOnTouchListener((v, motionEvent) -> {
            switch (motionEvent.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    view.setAlpha(0.7f);
                    break;
                case MotionEvent.ACTION_UP:
                    view.setAlpha(1f);
                    break;
                default:
                    break;
            }
            return false;

        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (getRequestedOrientation() == ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.dismiss();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.dismiss();
        }
    }

    private OneLoginResult oneLoginResult = new OneLoginResult() {
        @Override
        public void onResult() {
        }

        @Override
        public void onResult(int flag) {
            super.onResult(flag);
            if (getRequestedOrientation() == ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE) {
                setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
            }
        }
    };
}
