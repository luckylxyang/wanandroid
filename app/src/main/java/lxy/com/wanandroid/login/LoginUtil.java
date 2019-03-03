package lxy.com.wanandroid.login;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;

import com.google.gson.Gson;

import org.greenrobot.eventbus.EventBus;

import lxy.com.wanandroid.base.WanApplication;

/**
 * date: 2019/1/30
 * @author lxy
 */

public class LoginUtil {

    private static LoginUtil instance;
    private static String SP_LOGIN = "sp_login";


    private LoginUtil(){

    }

    public static LoginUtil getInstance(){
        if (instance == null){
            synchronized (LoginUtil.class){
                if (instance == null){
                    instance = new LoginUtil();
                }
            }
        }
        return instance;
    }

    /**
     * 检查是否登录
     * @return
     */
    public boolean checkLogin(){
        SharedPreferences sp = WanApplication.getContext().getSharedPreferences(SP_LOGIN, Context.MODE_PRIVATE);
        return sp.getBoolean("isLogin",false);
    }

    public void setLoginInfo(String loginInfo){
        SharedPreferences sp = WanApplication.getContext().getSharedPreferences(SP_LOGIN, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putBoolean("isLogin",true);
        editor.putString("loginInfo",loginInfo);
        editor.apply();
        EventBus.getDefault().post(new LoginEvent(true));
    }

    public LoginModel getLoginModel(){
        SharedPreferences sp = WanApplication.getContext().getSharedPreferences(SP_LOGIN, Context.MODE_PRIVATE);
        String userInfo = sp.getString("loginInfo","");
        if (TextUtils.isEmpty(userInfo)){
            return new LoginModel();
        }else {
            return new Gson().fromJson(userInfo,LoginModel.class);
        }

    }

    public void clearLoginInfo() {
        SharedPreferences sp = WanApplication.getContext().getSharedPreferences(SP_LOGIN, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putBoolean("isLogin",false);
        editor.putString("loginInfo","");
        editor.apply();
    }
}
