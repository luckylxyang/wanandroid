package com.lxy.basemodel;

import android.app.Application;
import android.content.Context;

/**
 * Created by Administrator on 2019/12/26.
 */

public class BaseApp {

    private static Context mContext;

    public static void init(Application application){
        mContext = application;
    }

    public static Context getContext(){
        return mContext;
    }
}
