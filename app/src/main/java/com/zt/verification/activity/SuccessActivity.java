package com.zt.verification.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.zt.verification.R;
import com.zt.verification.holder.Holder;
import com.zt.verification.view.SuccessView;


public class SuccessActivity extends BaseTitleActivity {
    private SuccessView successView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_success);
        getWindow().setBackgroundDrawable(null);
        findViewById(R.id.btn_back_success).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                finish();
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });
        successView = findViewById(R.id.gtm_view);
        if (Holder.with().getText().equals("本机认证")) {
            setTitle("认证成功");
            successView.setText("认证成功");
        } else {
            setTitle("登录成功");
            successView.setText("登录成功");
        }
        successView.start();
    }
}
