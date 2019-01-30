package lxy.com.wanandroid.login;

import android.content.Intent;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.gson.Gson;

import java.io.IOException;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import lxy.com.wanandroid.MainActivity;
import lxy.com.wanandroid.R;
import lxy.com.wanandroid.base.BaseActivity;
import lxy.com.wanandroid.base.ResponseModel;
import lxy.com.wanandroid.base.ToastUtils;
import lxy.com.wanandroid.network.NetworkAPI;
import lxy.com.wanandroid.network.NetworkManager;
import okhttp3.ResponseBody;

/**
 * @author : lxy
 * date: 2019/1/29
 */

public class LoginActivity extends BaseActivity {

    private static String TAG = "LoginActivity";

    private EditText etUsername;
    private EditText etPassword;
    private Button btnLogin;

    @Override
    protected void initOptions() {
        showToolbarBack(true);
        setToolbarTitle(getString(R.string.login));
        initView();
        initListener();
    }

    @Override
    public int setContextView() {
        return R.layout.activity_login;
    }

    private void initView(){
        etUsername = findViewById(R.id.login_et_username);
        etPassword = findViewById(R.id.login_et_password);
        btnLogin = findViewById(R.id.login_btn);

    }

    private void initListener(){
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                login();
            }
        });
    }

    private void login(){
        String user = etUsername.getText().toString();
        String pswd = etPassword.getText().toString();
        if (TextUtils.isEmpty(user) || TextUtils.isEmpty(pswd)){
            ToastUtils.show(LoginActivity.this,R.string.login_toast);
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
                        if (loginModel.getErrorCode() != 0){
                            ToastUtils.show(LoginActivity.this,loginModel.getErrorMsg());
                        }else {
                            ToastUtils.show(LoginActivity.this,R.string.login_success);

                            Intent intent = new Intent(LoginActivity.this,MainActivity.class);
                            startActivity(intent);
                            finish();
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
