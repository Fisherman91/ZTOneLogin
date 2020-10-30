package com.zt.verification.activity;

import android.app.ProgressDialog;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.geetest.onepassv2.OnePassHelper;
import com.geetest.onepassv2.listener.OnePassListener;
import com.zt.verification.R;
import com.zt.verification.consts.Constants;
import com.zt.verification.holder.Holder;
import com.zt.verification.onepass.CheckGatewayTask;
import com.zt.verification.util.Utils;

import org.json.JSONException;
import org.json.JSONObject;

import androidx.appcompat.app.AlertDialog;

public class OnePassActivity extends BaseTitleActivity {
    /**
     * 控件
     */
    private EditText editText;
    private Button button;
    private TextView textView;
    /**
     * OnePass的监听类
     */
    private OnePassListener onePassListener;
    /**
     * 校验网关的异步任务
     */
    private CheckGatewayTask checkGatewayTask;
    /**
     * 进度条
     */
    private ProgressDialog progressDialog;

    private String phoneNumber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_onepass);
        getWindow().setBackgroundDrawable(null);
        setTitle(Holder.with().getText());
        init();
        initGop();
    }

    /**
     * 初始化控件
     */
    private void init() {
        editText = findViewById(R.id.et_phone_number);
        button = findViewById(R.id.btn_next);
        textView = findViewById(R.id.gtm_check_num_tv);
        button.setOnClickListener(v -> {
            startGetToken();
        });
    }
    /**
     * 本机认证取号
     */
    private void startGetToken() {
        phoneNumber = editText.getText().toString().trim();
        //检测手机号格式
        if (Utils.checkPhoneNum(phoneNumber)) {
            textView.setVisibility(View.INVISIBLE);
            editText.getBackground().clearColorFilter();
            if (!Utils.haveIntent(OnePassActivity.this)) {
                AlertDialog.Builder builder = new AlertDialog.Builder(OnePassActivity.this);
                builder.setMessage("检测到未开启移动数据，建议手动开启进行操作");
                builder.setTitle("提示");
                builder.setPositiveButton("好的，我这就去开启", (dialog, which) -> {
                    dialog.dismiss();
                });
                builder.setNegativeButton("不好，我就不开", (dialog, which) -> {
                    dialog.dismiss();
                    progressDialog = ProgressDialog.show(OnePassActivity.this, null, "验证加载中", true, true);

                    /**
                     * OneLogin 与 OnePass 属于不同的产品，注意产品 APPID 不可混用，请在后台分别申请对用的 APPID
                     */
                    OnePassHelper.with().getToken(phoneNumber, onePassListener);
                });
                builder.create().show();
            } else {
                progressDialog = ProgressDialog.show(OnePassActivity.this, null, "验证加载中", true, true);
                OnePassHelper.with().getToken(phoneNumber, onePassListener);
            }
        } else {
            textView.setVisibility(View.VISIBLE);
            editText.getBackground().setColorFilter(Color.RED, PorterDuff.Mode.SRC_IN);
        }
    }

    private void initGop() {
        OnePassHelper.with().init(OnePassActivity.this, Constants.APP_ID_OP, 8000);
        onePassListener = new OnePassListener() {

            @Override
            public void onTokenFail(JSONObject jsonObject) {
                Log.i(Constants.TAG, "onTokenFail:" + jsonObject.toString());
                Utils.toastMessage(OnePassActivity.this, "本机认证失败");
                progressDialog.dismiss();
            }

            @Override
            public void onTokenSuccess(final JSONObject jsonObject) {
                Log.i(Constants.TAG, "onTokenSuccess:" + jsonObject.toString());
                try {
                    jsonObject.put("id_2_sign", Constants.APP_ID_OP);
                    jsonObject.put("phone", phoneNumber);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                Log.i(Constants.TAG, "开始请求 checkgateway:" + jsonObject.toString());
                checkGatewayTask = new CheckGatewayTask(OnePassActivity.this, jsonObject, progressDialog);
                checkGatewayTask.execute(Constants.CHECK_GATE_WAY);
            }
        };
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (progressDialog != null) {
            progressDialog.dismiss();
        }
        //TODO 必须调用，释放资源，销毁的时候执行
        OnePassHelper.with().cancel();
    }
}
