package com.zt.verification.ui.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.zt.verification.R;
//import com.zt.verifylibrary.ZVerificationInterface;
//import com.zt.verifylibrary.config.OperatorType;
//import com.zt.verifylibrary.listener.LoginAuthListener;
//import com.zt.verifylibrary.listener.PreLoginListener;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

public class HomeFragment extends Fragment {

    private HomeViewModel homeViewModel;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        homeViewModel =
                ViewModelProviders.of(this).get(HomeViewModel.class);
        View root = inflater.inflate(R.layout.fragment_home, container, false);
        final TextView textView = root.findViewById(R.id.text_home);
        homeViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });

        final TextView tvToken = root.findViewById(R.id.tv_token);
//        ZVerificationInterface.getInstance().preLogin(new PreLoginListener() {
//            @Override
//            public void onResult(int code, String content) {
//                tvToken.setText(content);
//
//            }
//        });



        root.findViewById(R.id.btn_onkey_login).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                ZVerificationInterface.getInstance().loginAuth(new LoginAuthListener() {
//                    @Override
//                    public void onResult(int code, String content, OperatorType... operator) {
//                        if (ZVerificationInterface.getInstance().isAuthSuccessful(code)) {
//                        ZVerificationInterface.getInstance().quitAuthActivity();
                        }
//                        tvToken.setText(content);
//                    }
//                });

//            }
        });

        return root;
    }
}