package lxy.com.wanandroid.base;

import android.app.Application;
import android.content.Context;

import lxy.com.sdk.SensorsDataAPI;
import lxy.com.wanandroid.db.DBHelper;

/**
 * Creator : lxy
 * date: 2019/1/30
 */

public class WanApplication extends Application {

    private static Context context;

    @Override
    public void onCreate() {
        super.onCreate();
        context = getApplicationContext();
        SensorsDataAPI.init(this);
    }

    public static Context getContext(){
        return context;
    }
}
