package com.zt.verification.onepass;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.text.TextUtils;
import android.util.Log;


import com.zt.verification.activity.SuccessActivity;
import com.zt.verification.consts.Constants;
import com.zt.verification.util.HttpUtils;
import com.zt.verification.util.Utils;

import org.json.JSONObject;

public class CheckGatewayTask extends AsyncTask<String, Void, String> {
    /**
     * 构造网关校验参数
     */
    private JSONObject jsonObject;
    /**
     * 页面的activity
     */
    private Activity context;
    /**
     * 加载对话框
     */
    private ProgressDialog progressDialog;

    public CheckGatewayTask(Activity context, JSONObject jsonObject, ProgressDialog progressDialog) {
        this.context = context;
        this.jsonObject = jsonObject;
        this.progressDialog = progressDialog;
    }

    @Override
    protected String doInBackground(String... params) {
        if (isCancelled()) {
            return null;
        }
        return HttpUtils.requestNetwork(params[0], jsonObject, null);
    }

    @Override
    protected void onPostExecute(String s) {
        Log.i(Constants.TAG, "CheckGateway 请求结束");
        if (isCancelled()) {
            return;
        }
        if (TextUtils.isEmpty(s)) {
            Log.e(Constants.TAG, "CheckGateway 校验异常");
            handleFailed(context, progressDialog);
            return;
        }
        Log.i(Constants.TAG, "CheckGateway 请求成功:" + s);
        try {
            JSONObject jsonObject = new JSONObject(s);
            int status = jsonObject.getInt("status");
            if (status == Constants.SUCCESS_CODE && "0".equals(jsonObject.getString("result"))) {
                if (progressDialog != null) {
                    progressDialog.dismiss();
                }
                context.startActivity(new Intent(context.getApplicationContext(), SuccessActivity.class));
            } else {
                Log.e(Constants.TAG, "CheckGateway 校验失败:" + s);
                handleFailed(context, progressDialog);
            }
        } catch (Exception e) {
            Log.e(Constants.TAG, "CheckGateway 校验失败:" + s);
            handleFailed(context, progressDialog);
        }
    }

    private void handleFailed(Context context, ProgressDialog dialog){
        Utils.toastMessage(context, "本机认证校验失败");
        dialog.dismiss();
    }
}
