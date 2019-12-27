package com.lxy.login.login;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.google.gson.Gson;
import com.lxy.basemodel.model.LoginModel;
import com.lxy.basemodel.network.BaseObserver;
import com.lxy.basemodel.network.NetworkManager;
import com.lxy.basemodel.network.RxHelper;
import com.lxy.basemodel.utils.ToastUtils;
import com.lxy.login.R;


/**
 * Creator : lxy
 * date: 2019/2/27
 */

public class LoginFragment extends Fragment {
    private static String TAG = LoginFragment.class.getSimpleName();

    private TextInputEditText etUsername;
    private TextInputEditText etPassword;
    private Button btnLogin;
    private TextView tvRegister;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_login,container,false);
        initView(view);
        initListener();
        return view;
    }

    private void initView(View view) {
        etUsername = view.findViewById(R.id.login_et_username);
        etPassword = view.findViewById(R.id.login_et_password);
        btnLogin = view.findViewById(R.id.login_btn);
        tvRegister = view.findViewById(R.id.login_register);
    }

    private void initListener(){
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LoginFragment.this.login();
            }
        });

        tvRegister.setOnClickListener(v -> ((LoginActivity)getActivity()).showRegister());
    }

    private void login(){
        String user = etUsername.getText().toString();
        String pswd = etPassword.getText().toString();
        if (TextUtils.isEmpty(user) || TextUtils.isEmpty(pswd)){
            ToastUtils.show(R.string.login_toast);
            return;
        }

        NetworkManager.getManager().getServer(LoginAPI.class).login(user,pswd)
                .compose(RxHelper.observableIO2Main())
                .subscribe(new BaseObserver<LoginModel>(){
                    @Override
                    public void onSuccess(LoginModel loginModel) {
                        ToastUtils.show(R.string.login_success);
                        loginModel.setPassword(pswd);
                        LoginUtil.getInstance().setLoginInfo(getContext(),new Gson().toJson(loginModel));
                        getActivity().finish();
                    }

                    @Override
                    public void onFailure(String message) {
                        ToastUtils.show(message);
                    }
                });
    }
}
