package com.lxy.sharesquare;

import android.app.Application;
import android.content.Context;

import com.lxy.basemodel.BaseApp;


/**
 * Created by Administrator on 2019/12/27.
 */

public class SquareApp extends Application {

    private static Context context;

    @Override
    public void onCreate() {
        super.onCreate();
        context = getApplicationContext();
        BaseApp.init(this);
    }

    public static Context getContext(){
        return context;
    }
}
