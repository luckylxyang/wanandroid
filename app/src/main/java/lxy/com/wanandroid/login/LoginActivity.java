package lxy.com.wanandroid.login;

import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.gson.Gson;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import lxy.com.wanandroid.R;
import lxy.com.wanandroid.base.BaseActivity;
import lxy.com.wanandroid.base.Constants;
import lxy.com.wanandroid.base.ToastUtils;
import lxy.com.wanandroid.network.NetworkManager;

/**
 * @author : lxy
 *         date: 2019/1/29
 */

public class LoginActivity extends BaseActivity {

    private static String TAG = "LoginActivity";
    private LoginFragment loginFragment;
    private RegisterFragment registerFragment;

    @Override
    protected void initOptions() {
        setToolbarTitle(getString(R.string.login));
        showLogin();
    }

    @Override
    public int setContextView() {
        return R.layout.activity_login;
    }

    public void showLogin() {
        if (loginFragment == null) {
            loginFragment = new LoginFragment();
        }
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.add(R.id.login_frag, loginFragment);
        if (registerFragment != null) {
            transaction.hide(registerFragment);
        }
        transaction.commit();
    }

    public void showRegister() {
        if (registerFragment == null) {
            registerFragment = new RegisterFragment();
        }
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.hide(loginFragment);
        transaction.add(R.id.login_frag, registerFragment);
//        transaction.show(registerFragment);
        transaction.commit();
    }

}
