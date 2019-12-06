package lxy.com.wanandroid.network;

import java.util.List;

import io.reactivex.Observable;
import lxy.com.wanandroid.base.ResponseModel;
import lxy.com.wanandroid.collect.CollectModel;
import lxy.com.wanandroid.home.model.ArticleModel;
import lxy.com.wanandroid.home.model.BannerModel;
import lxy.com.wanandroid.knowledge.KnowledgeModel;
import lxy.com.wanandroid.login.LoginModel;
import lxy.com.wanandroid.officeAccount.OfficeAccountArticleModel;
import lxy.com.wanandroid.officeAccount.OfficeAccountModel;
import lxy.com.wanandroid.search.HotModel;
import lxy.com.wanandroid.search.WebsiteModel;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;
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
    Observable<BaseResponse<LoginModel>> login(@Field("username") String username,
                                 @Field("password") String password);

    /**
     * http://www.wanandroid.com/user/logout/json
     * @return
     */
    @GET("user/logout/json")
    Observable<LoginModel> logout();

    /**
     * http://www.wanandroid.com/user/register
     * @param username
     * @param password
     * @param repassword
     * @return
     */
    @POST("user/register")
    @FormUrlEncoded
    Observable<LoginModel> register(@Field("username") String username,
                                    @Field("password") String password,
                                    @Field("repassword") String repassword);

    /**
     * http://www.wanandroid.com/banner/json
     * 获取首页banner
     * @return
     */
    @GET("banner/json")
    Observable<BaseResponse<List<BannerModel>>> getBannerList();

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

    /**
     * http://www.wanandroid.com/article/list/0/json?cid=60
     * @param cid
     * @return
     */
    @GET("article/list/{page}/json")
    Observable<ResponseModel> getKnowledgeList(@Path("page") int page,
                                               @Query("cid") int cid);

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

    /**
     * http://www.wanandroid.com/lg/collect/list/0/json
     * @param page
     * @return
     */
    @GET("/lg/collect/list/{page}/json")
    Observable<CollectModel> getMyCollectList(@Path("page") int page);

    /**
     * http://www.wanandroid.com/hotkey/json
     * @return
     */
    @GET("hotkey/json")
    Observable<HotModel> getHotTag();

    /**
     * https://www.wanandroid.com/article/query/0/json
     * @param page
     * @param key
     * @return
     */
    @POST("/article/query/{page}/json")
    @FormUrlEncoded
    Observable<ResponseModel> queryByKey(@Path("page") int page,
                                         @Field("k") String key);

    /**
     * https://www.wanandroid.com/friend/json
     * 常用网址
     * @return
     */
    @GET("friend/json")
    Observable<WebsiteModel> getFriendWeb();

    /**
     * https://wanandroid.com/wxarticle/chapters/json
     * @return
     */
    @GET("wxarticle/chapters/json")
    Observable<BaseResponse<List<OfficeAccountModel>>> getOfficeAccountList();

    /**
     * https://wanandroid.com/wxarticle/list/408/1/json
     * @return
     */
    @GET("wxarticle/list/{id}/{page}/json")
    Observable<BaseResponse<OfficeAccountArticleModel>> getOfficeAccountArticle(@Path("id") int id,
                                                                                @Path("page") int page);

    /**
     * https://www.wanandroid.com/project/list/1/json?cid=294
     * @return
     */
    @GET("project/list/{page}/json")
    Observable<ResponseModel> getProjectList(@Path("page") int page,
                                             @Query("cid") int cid);

    /**
     * https://www.wanandroid.com/project/tree/json
     * @return
     */
    @GET("project/tree/json")
    Observable<KnowledgeModel> getProjectTree();
}
