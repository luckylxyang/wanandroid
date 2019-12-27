package lxy.com.wanandroid;

import android.app.Application;
import android.content.Context;

import com.alibaba.android.arouter.launcher.ARouter;
import com.lxy.basemodel.BaseApp;

import lxy.com.sdk.SensorsDataAPI;
import lxy.com.wanandroid.db.DBHelper;

/**
 * Creator : lxy
 * date: 2019/1/30
 */

public class WanApplication extends Application {

    private static Context context;
    private boolean debugger = true;

    @Override
    public void onCreate() {
        super.onCreate();
        context = getApplicationContext();
        SensorsDataAPI.init(this);
        BaseApp.init(this);

        if (debugger){
            ARouter.openLog();
            ARouter.openDebug();
        }
        ARouter.init(this);
    }

    public static Context getContext(){
        return context;
    }
}
