package com.lxy.basemodel.provider;

import android.content.Context;

import com.alibaba.android.arouter.facade.template.IProvider;
import com.lxy.basemodel.model.LoginModel;

/**
 * Created by Administrator on 2019/12/27.
 */

public interface LoginProvider extends IProvider {

    LoginModel getLoginInfo();

}
