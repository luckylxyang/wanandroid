package lxy.com.wanandroid.base;

import android.app.Application;
import android.content.Context;

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
    }

    public static Context getContext(){
        return context;
    }
}
