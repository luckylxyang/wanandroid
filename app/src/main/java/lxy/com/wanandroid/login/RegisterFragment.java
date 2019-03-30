package lxy.com.wanandroid.login;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.google.gson.Gson;

import java.io.IOException;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import lxy.com.wanandroid.R;
import lxy.com.wanandroid.base.ToastUtils;
import lxy.com.wanandroid.network.NetworkManager;
import okhttp3.ResponseBody;

/**
 * Creator : lxy
 * date: 2019/2/27
 */

public class RegisterFragment extends Fragment {

    private static String TAG = LoginFragment.class.getSimpleName();

    private TextInputEditText etUsername;
    private TextInputEditText etPassword;
    private TextInputEditText etREPassword;
    private Button btnLogin;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_register,container,false);
        initView(view);
        initListener();
        return view;
    }

    private void initView(View view) {
        etUsername = view.findViewById(R.id.register_et_username);
        etPassword = view.findViewById(R.id.register_et_password);
        etREPassword = view.findViewById(R.id.register_et_repassword);
        btnLogin = view.findViewById(R.id.register_btn);
    }

    private void initListener(){
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                register();
            }
        });
    }

    private void register() {
        String name = etUsername.getText().toString().trim();
        String pswd = etPassword.getText().toString().trim();
        String repswd = etREPassword.getText().toString().trim();
        if (TextUtils.isEmpty(name) || TextUtils.isEmpty(pswd) || TextUtils.isEmpty(repswd)){
            ToastUtils.show(R.string.login_toast);
            return;
        }
        NetworkManager.getManager().getServer().register(name,pswd,repswd)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<LoginModel>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(LoginModel model) {
                        if (model.getErrorCode() == 0){
                            ToastUtils.show(R.string.login_success);
                            model.getData().setPassword(pswd);
                            LoginUtil.getInstance().setLoginInfo(new Gson().toJson(model));
                        }else {
                            ToastUtils.show(model.getErrorMsg());
                        }

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }
}
