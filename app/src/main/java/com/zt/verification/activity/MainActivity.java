package com.zt.verification.activity;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Intent;
import android.os.Bundle;
import android.view.TextureView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;


import com.zt.verification.R;
import com.zt.verification.holder.Holder;
import com.zt.verification.util.WindowUtils;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        WindowUtils.setStatusBarTransparent(this);

        init();
        getWindow().setBackgroundDrawable(null);
        findViewById(R.id.btn_onelogin).setOnClickListener(v -> {
            startOneLogin();
        });
        findViewById(R.id.btn_onepass).setOnClickListener(v -> {
            startOnePass();
        });
    }

    private void init() {

        TextView textView = findViewById(R.id.gtm_iv);
        final LinearLayout linear = findViewById(R.id.gtm_ll);
        ObjectAnimator animator = ObjectAnimator.ofFloat(textView, "translationY", 0.0f, 0.0f, 0.0f, -280.0f);
        ValueAnimator val = ValueAnimator.ofFloat(0.0f, 0.0f, 0.0f, 1.0f);
        val.addUpdateListener(animation -> {
            linear.setAlpha((Float) animation.getAnimatedValue());
        });
        AnimatorSet set = new AnimatorSet();
        set.setDuration(1500);
        set.playTogether(animator, val);
        set.start();
    }

    private void startOneLogin() {
        Holder.with().setText("一键登录");
        startActivity(new Intent(getApplicationContext(), OneLoginStyleSelectActivity.class));
    }

    private void startOnePass() {
        Holder.with().setText("本机认证");
        startActivity(new Intent(getApplicationContext(), OnePassActivity.class));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
