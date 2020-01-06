package com.lxy.basemodel.provider;

import com.alibaba.android.arouter.facade.template.IProvider;
import com.lxy.basemodel.network.model.LoginModel;

/**
 * Created by Administrator on 2019/12/27.
 */

public interface LoginProvider extends IProvider {

    LoginModel getLoginInfo();

}
