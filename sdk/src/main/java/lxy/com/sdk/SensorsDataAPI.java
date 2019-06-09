package lxy.com.sdk;

import android.app.Application;

/**
 * Creator : lxy
 * date: 2019/6/10
 */
public class SensorsDataAPI {
    private final String TAG = this.getClass().getSimpleName();
    private static SensorsDataAPI instance;
    private static final Object mLock = new Object();

    private SensorsDataAPI(Application application){

    }

    public static SensorsDataAPI init(Application application){
        synchronized (mLock){
            if (instance == null){
                instance = new SensorsDataAPI(application);
            }
            return instance;
        }
    }

    public static SensorsDataAPI getInstance(){
        return instance;
    }
}
