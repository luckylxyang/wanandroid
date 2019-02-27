package lxy.com.wanandroid.network;

import io.reactivex.Observable;
import lxy.com.wanandroid.base.ResponseModel;
import lxy.com.wanandroid.home.model.ArticleModel;
import lxy.com.wanandroid.home.model.BannerModel;
import lxy.com.wanandroid.knowledge.KnowledgeModel;
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
     * http://www.wanandroid.com/banner/json
     * 获取首页banner
     * @return
     */
    @GET("banner/json")
    Observable<BannerModel> getBannerList();

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

    /**
     * http://www.wanandroid.com/tree/json
     * @return
     */
    @GET("tree/json")
    Observable<KnowledgeModel> getTree();
    // 收藏

    /**
     * 收藏站内文章
     * http://www.wanandroid.com/lg/collect/1165/json
     * @param id
     * @return
     */
    @POST("lg/collect/{id}/json")
    Observable<ResponseModel> collectArticleInSite(@Path("id") int id);

    /**
     * 收藏站外文章
     * http://www.wanandroid.com/lg/collect/add/json
     * @param title
     * @param author
     * @param link
     * @return
     */
    @POST("lg/collect/add/json")
    @FormUrlEncoded
    Observable<ResponseModel> collectArticleOutSite(@Field("title") int title,
                                                    @Field("author") String author,
                                                    @Field("link") String link);

    /**
     * 在文章列表取消收藏
     * http://www.wanandroid.com/lg/uncollect_originId/2333/json
     * @return
     */
    @POST("lg/uncollect_originId/{id}/json")
    Observable<ResponseModel> unCollectArticle(@Path("id") int id);


}
