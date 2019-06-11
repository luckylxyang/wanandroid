package lxy.com.sdk;

import android.app.Application;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;

/**
 * Creator : lxy
 * date: 2019/6/10
 */
public class SensorsDataAPI {
    public static final String SDK_VERSION = "1.0.0";
    private final String TAG = this.getClass().getSimpleName();
    private static SensorsDataAPI instance;
    private static final Object mLock = new Object();
    private static Map<String,Object> mDeviceInfo;
    private static String mDeviceId;

    private SensorsDataAPI(Application application){
        mDeviceInfo = SensorsDataPrivate.getDeviceInfo(application.getApplicationContext());
        mDeviceId = SensorsDataPrivate.getAndroidId(application.getApplicationContext());
        SensorsDataPrivate.registerActivityLifecycleCallbacks(application);
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

    public void track(String eventName, JSONObject properties) {
        try {
            JSONObject json = new JSONObject();
            json.put("event", eventName);
            json.put("device_id",mDeviceId);
            JSONObject sendProperty = new JSONObject(mDeviceInfo);
            if (properties != null){
                SensorsDataPrivate.mergeJSONObject(properties, sendProperty);
            }
            json.put("properties", sendProperty);
            json.put("time",System.currentTimeMillis());
            Log.i(TAG, json.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
