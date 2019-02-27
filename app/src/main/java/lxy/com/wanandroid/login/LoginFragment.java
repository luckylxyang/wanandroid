package lxy.com.wanandroid.login;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.google.gson.Gson;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import lxy.com.wanandroid.R;
import lxy.com.wanandroid.base.Constants;
import lxy.com.wanandroid.base.ToastUtils;
import lxy.com.wanandroid.network.NetworkManager;

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
                login();
            }
        });

        tvRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((LoginActivity)getActivity()).showRegister();
            }
        });
    }

    private void login(){
        String user = etUsername.getText().toString();
        String pswd = etPassword.getText().toString();
        if (TextUtils.isEmpty(user) || TextUtils.isEmpty(pswd)){
            ToastUtils.show(R.string.login_toast);
            return;
        }

        NetworkManager.getManager().getServer().login(user,pswd)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<LoginModel>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(LoginModel loginModel) {
                        Log.i(TAG,new Gson().toJson(loginModel));
                        if (loginModel.getErrorCode() != Constants.NET_SUCCESS){
                            ToastUtils.show(loginModel.getErrorMsg());
                        }else {
                            ToastUtils.show(R.string.login_success);
                            LoginUtil.getInstance().setLoginInfo(new Gson().toJson(loginModel));
//                            Intent intent = new Intent(LoginActivity.this,MainActivity.class);
//                            startActivity(intent);
                            getActivity().finish();
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }
}
