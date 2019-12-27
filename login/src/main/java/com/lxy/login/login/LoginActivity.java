package com.lxy.login.login;

import android.support.v4.app.FragmentTransaction;
import android.util.Log;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.lxy.basemodel.base.BaseActivity;
import com.lxy.basemodel.base.Constants;
import com.lxy.login.R;


/**
 * @author : lxy
 *         date: 2019/1/29
 */
@Route(path = Constants.URL_LOGIN_ACTIVITY)
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

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        if (loginFragment == null) {
            loginFragment = new LoginFragment();
            transaction.add(R.id.login_frag, loginFragment);
        } else {
            transaction.show(loginFragment);
        }
        if (registerFragment != null) {
            transaction.hide(registerFragment);
        }
        transaction.setCustomAnimations(FragmentTransaction.TRANSIT_ENTER_MASK, FragmentTransaction.TRANSIT_EXIT_MASK);
        transaction.commit();
        setToolbarTitle(getString(R.string.login));
        Log.i(TAG, getSupportFragmentManager().getFragments().size() + "");
    }

    public void showRegister() {

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.hide(loginFragment);
        if (registerFragment == null) {
            registerFragment = new RegisterFragment();
            transaction.add(R.id.login_frag, registerFragment);
        } else {
            transaction.show(registerFragment);
        }
        transaction.commit();
        setToolbarTitle(getString(R.string.register));
        Log.i(TAG, getSupportFragmentManager().getFragments().size() + "");
    }

}
