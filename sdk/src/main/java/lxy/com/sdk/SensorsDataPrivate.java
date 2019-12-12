package lxy.com.sdk;

import android.app.ActionBar;
import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import lxy.com.sdk.click.SensorsDataClickPrivate;

/**
 * Creator : lxy
 * date: 2019/6/10
 */
public class SensorsDataPrivate {

    private static List<Integer> mIgnoredActivities;


    static {
        mIgnoredActivities = new ArrayList<>();
    }

    public static void ignoreAutoTrackActivity(Class<?> activity) {
        if (activity == null) {
            return;
        }
        mIgnoredActivities.add(activity.hashCode());
    }

    public static void registerActivityLifecycleCallbacks(Application application) {
        application.registerActivityLifecycleCallbacks(new Application.ActivityLifecycleCallbacks() {
            private ViewTreeObserver.OnGlobalLayoutListener globalLayoutListener;
            @Override
            public void onActivityCreated(final Activity activity, Bundle savedInstanceState) {
                final View rootView = activity.getWindow().getDecorView();
                globalLayoutListener = new ViewTreeObserver.OnGlobalLayoutListener() {
                    @Override
                    public void onGlobalLayout() {
                        SensorsDataClickPrivate.delegateViewsOnClickListener(activity, rootView);
                    }
                };
            }

            @Override
            public void onActivityStarted(Activity activity) {

            }

            @Override
            public void onActivityResumed(final Activity activity) {
                AppCompatActivity activity1 = (AppCompatActivity) activity;
                activity1.getSupportFragmentManager().registerFragmentLifecycleCallbacks(fragmentLifecycleCallbacks,false);
                // 页面浏览采集
                trackAppViewScreen(activity);
                // 点击事件采集
                final View rootView = activity.getWindow().getDecorView();
                rootView.getViewTreeObserver().addOnGlobalLayoutListener(globalLayoutListener);

            }

            @Override
            public void onActivityPaused(Activity activity) {

            }

            @Override
            public void onActivityStopped(Activity activity) {
                final View rootView = activity.getWindow().getDecorView();
                rootView.getViewTreeObserver().removeOnGlobalLayoutListener(globalLayoutListener);

            }

            @Override
            public void onActivitySaveInstanceState(Activity activity, Bundle outState) {

            }

            @Override
            public void onActivityDestroyed(Activity activity) {

            }
        });

    }

    private static FragmentManager.FragmentLifecycleCallbacks fragmentLifecycleCallbacks = new FragmentManager.FragmentLifecycleCallbacks() {
        @Override
        public void onFragmentAttached(FragmentManager fm, Fragment f, Context context) {
            super.onFragmentAttached(fm, f, context);
        }

        @Override
        public void onFragmentCreated(FragmentManager fm, Fragment f, Bundle savedInstanceState) {
            super.onFragmentCreated(fm, f, savedInstanceState);
        }

        @Override
        public void onFragmentViewCreated(FragmentManager fm, Fragment f, View v, Bundle savedInstanceState) {
            super.onFragmentViewCreated(fm, f, v, savedInstanceState);
        }

        @Override
        public void onFragmentStarted(FragmentManager fm, Fragment f) {
            super.onFragmentStarted(fm, f);
//            Log.i("fragmentLifeCallback",f.getClass().getSimpleName());
            getFragmentMessage(f,"onFragmentStarted");
        }

        @Override
        public void onFragmentResumed(FragmentManager fm, Fragment f) {
            super.onFragmentResumed(fm, f);
        }

        @Override
        public void onFragmentPaused(FragmentManager fm, Fragment f) {
            super.onFragmentPaused(fm, f);
        }

        @Override
        public void onFragmentStopped(FragmentManager fm, Fragment f) {
            super.onFragmentStopped(fm, f);
        }

        @Override
        public void onFragmentSaveInstanceState(FragmentManager fm, Fragment f, Bundle outState) {
            super.onFragmentSaveInstanceState(fm, f, outState);
        }

        @Override
        public void onFragmentDestroyed(FragmentManager fm, Fragment f) {
            super.onFragmentDestroyed(fm, f);
        }

        @Override
        public void onFragmentDetached(FragmentManager fm, Fragment f) {
            super.onFragmentDetached(fm, f);
        }
    };

    private static void getFragmentMessage(Fragment fragment, String lifeMethod){
        try {
            if (fragment == null) {
                return;
            }

            JSONObject object = new JSONObject();
            object.put("fragment_activity", fragment.getActivity().getClass().getCanonicalName());
            object.put("fragment", fragment.getClass().getCanonicalName());
            object.put("lifeMethod", lifeMethod);
            SensorsDataAPI.getInstance().track("AppViewScreen", object);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * activity 页面浏览采集
     *
     * @param activity
     */
    private static void trackAppViewScreen(Activity activity) {
        try {
            if (activity == null) {
                return;
            }
            // 如果该页面已经记录了
            if (mIgnoredActivities.contains(activity.getClass().hashCode())) {
                return;
            }
            JSONObject object = new JSONObject();
            object.put("activity", activity.getClass().getCanonicalName());
            object.put("title", getActivityTitle(activity));
            SensorsDataAPI.getInstance().track("AppViewScreen", object);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取 activity 标题
     * @param activity
     * @return
     */
    private static String getActivityTitle(Activity activity) {
        String title = null;
        if (activity == null) {
            return null;
        }
        try {
            title = activity.getTitle().toString();
            if (Build.VERSION.SDK_INT >= 11) {
                String toolbarTitle = getToolbarTitle(activity);
                if (!TextUtils.isEmpty(toolbarTitle)) {
                    title = toolbarTitle;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return title;
    }

    /**
     * 获取 activity 的标题，需要设置 actionbar or supportActionBar
     *
     * @param activity
     * @return
     */
    private static String getToolbarTitle(Activity activity) {
        try {
            ActionBar actionBar = activity.getActionBar();
            if (actionBar != null) {
                if (!TextUtils.isEmpty(actionBar.getTitle())) {
                    return actionBar.getTitle().toString();
                }
            } else {
                if (activity instanceof AppCompatActivity) {
                    AppCompatActivity appCompatActivity = (AppCompatActivity) activity;
                    android.support.v7.app.ActionBar supportActionBar = appCompatActivity.getSupportActionBar();
                    if (supportActionBar != null) {
                        if (!TextUtils.isEmpty(supportActionBar.getTitle())) {
                            return supportActionBar.getTitle().toString();
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    /**
     * 获取设备信息
     * @param context
     * @return
     */
    public static Map<String, Object> getDeviceInfo(Context context) {
        HashMap<String, Object> deviceInfo = new HashMap<>();
        deviceInfo.put("lib", "Android");
        deviceInfo.put("lib_version", SensorsDataAPI.SDK_VERSION);
        deviceInfo.put("os", "Android");
        deviceInfo.put("os_version", Build.VERSION.RELEASE == null ? "UNKNOWN" : Build.VERSION.RELEASE);
        // 生产厂家
        deviceInfo.put("manfacturer", Build.MANUFACTURER == null ? "UNKNOWN" : Build.MANUFACTURER);
        if (TextUtils.isEmpty(Build.MODEL)) {
            deviceInfo.put("model", "UNKNOWN");
        } else {
            deviceInfo.put("model", Build.MODEL.trim());
        }
        try {
            PackageManager packageManager = context.getPackageManager();
            PackageInfo packageInfo = packageManager.getPackageInfo(context.getPackageName(), 0);
            deviceInfo.put("app_version", packageInfo.versionName);
            int labelRes = packageInfo.applicationInfo.labelRes;
            deviceInfo.put("app_name", context.getResources().getString(labelRes));
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        deviceInfo.put("screen_height", displayMetrics.heightPixels);
        deviceInfo.put("screen_width", displayMetrics.widthPixels);
        return Collections.unmodifiableMap(deviceInfo);
    }

    public static String getAndroidId(Context context) {
        String androidId = "";
        try {
            androidId = Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return androidId;
    }

    public static void mergeJSONObject(JSONObject source, JSONObject dest) throws JSONException {
        Iterator<String> keys = source.keys();
        while (keys.hasNext()) {
            String key = keys.next();
            dest.put(key, source.get(key));
        }
    }


}
