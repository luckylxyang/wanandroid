package lxy.com.wanandroid.network;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Creator : lxy
 * date: 2019/1/15
 */

public class NetworkManager {

    private static NetworkManager manager;
    private OkHttpClient client;
    private String baseUrl = "http://www.wanandroid.com/";

    private NetworkManager(){
        init();
    }

    public static NetworkManager getManager(){
        if (manager == null){
            manager = new NetworkManager();
        }
        return manager;
    }

    private void init(){
        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        client = new OkHttpClient.Builder()
                .connectTimeout(12,TimeUnit.SECONDS)
                .addInterceptor(new SaveCookieInterceptor())
//                .addNetworkInterceptor(loggingInterceptor)
                .addInterceptor(new AddCookieInterceptor())
                .build();
    }

    public NetworkAPI getServer(){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(baseUrl)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();

        return retrofit.create(NetworkAPI.class);
    }

}
