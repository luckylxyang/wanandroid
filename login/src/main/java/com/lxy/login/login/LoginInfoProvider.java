package com.lxy.login.login;

import android.content.Context;

import com.lxy.basemodel.network.model.LoginModel;
import com.lxy.basemodel.provider.LoginProvider;

/**
 * Created by Administrator on 2019/12/27.
 */

public class LoginInfoProvider implements LoginProvider {

    private Context mContext;

    @Override
    public void init(Context context) {
        mContext = context;
    }

    public LoginModel getLoginInfo(){
        LoginModel model = LoginUtil.getInstance().getLoginModel(mContext);
        return model;
    }
}
