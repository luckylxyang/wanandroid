package lxy.com.wanandroid.network;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;
import android.util.Log;

import java.io.IOException;
import java.lang.ref.SoftReference;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import lxy.com.wanandroid.base.WanApplication;
import okhttp3.Interceptor;
import okhttp3.Response;

/**
 * Creator : lxy
 * date: 2019/1/30
 */

public class SaveCookieInterceptor implements Interceptor {

    private static String TAG = "SaveCookieInterceptor";


    @Override
    public Response intercept(Chain chain) throws IOException {
        Response response = chain.proceed(chain.request());
//        Log.i(TAG,response.body().string());
        if (!response.headers("set-Cookie").isEmpty()){
            List<String> cookies = response.headers("set-cookie");
            String cookie = encodeCookie(cookies);
            saveCookie(chain.request().url().toString(), chain.request().url().host(), cookie);
        }
        return response;
    }

    private String encodeCookie(List<String> cookies) {
        StringBuilder sb = new StringBuilder();
        Set<String> set = new HashSet<>();
        for (String cookie : cookies) {
            String[] arr = cookie.split(";");
            for (String s : arr) {
                if (set.contains(s)) {
                    continue;
                }
                set.add(s);
            }
        }

        for (String cookie : set) {
            sb.append(cookie).append(";");
        }

        int last = sb.lastIndexOf(";");
        if (last != -1 && sb.length() - 1 == last) {
            sb.deleteCharAt(last);
        }

        return sb.toString();
    }

    /**
     * 保存cookie到本地，这里我们分别为该url和host设置相同的cookie，其中host可选
     * 这样能使得该cookie的应用范围更广
     */
    private void saveCookie(String url, String domain, String cookies) {
        SharedPreferences sp = WanApplication.getContext().getSharedPreferences("cookies_prefs", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();

        if (TextUtils.isEmpty(url)) {
            throw new NullPointerException("url is null.");
        } else {
            editor.putString(url, cookies);
        }

        if (!TextUtils.isEmpty(domain)) {
            editor.putString(domain, cookies);
        }
        editor.apply();
    }
}
