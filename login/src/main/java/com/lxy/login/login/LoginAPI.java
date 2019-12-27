package com.lxy.login.login;

import com.lxy.basemodel.model.LoginModel;
import com.lxy.basemodel.network.BaseResponse;


import io.reactivex.Observable;

import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;

/**
 * @author : lxy
 * date: 2019/1/15
 */

public interface LoginAPI {


    // 登录注册

    /**
     * 登录
     * @param username
     * @param password
     * @return
     */
    @POST("user/login")
    @FormUrlEncoded
    Observable<BaseResponse<LoginModel>> login(@Field("username") String username,
                                               @Field("password") String password);

    /**
     * http://www.wanandroid.com/user/logout/json
     * @return
     */
    @GET("user/logout/json")
    Observable<BaseResponse<String>> logout();

    /**
     * http://www.wanandroid.com/user/register
     * @param username
     * @param password
     * @param repassword
     * @return
     */
    @POST("user/register")
    @FormUrlEncoded
    Observable<BaseResponse<LoginModel>> register(@Field("username") String username,
                                    @Field("password") String password,
                                    @Field("repassword") String repassword);

}
