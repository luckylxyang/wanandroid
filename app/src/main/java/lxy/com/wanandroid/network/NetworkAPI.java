package lxy.com.wanandroid.network;

import io.reactivex.Observable;
import lxy.com.wanandroid.base.ResponseModel;
import lxy.com.wanandroid.home.model.ArticleModel;
import lxy.com.wanandroid.login.LoginModel;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.QueryMap;

/**
 * @author : lxy
 * date: 2019/1/15
 */

public interface NetworkAPI {


    // 登录注册

    /**
     * 登录
     * @param username
     * @param password
     * @return
     */
    @POST("user/login")
    @FormUrlEncoded
    Observable<LoginModel> login(@Field("username") String username,
                                 @Field("password") String password);

    /**
     * Get article in a website which link is www.wanandroid.com
     * @param page
     * @return
     */
    @GET("article/list/{page}/json")
    Observable<ResponseModel> getArticleList(@Path("page") int page);

    /**
     * http://wanandroid.com/article/listproject/0/json
     * @return
     */
    @GET("article/listproject/{page}/json")
    Observable<ResponseModel> getNewProject(@Path("page") int page);

}
