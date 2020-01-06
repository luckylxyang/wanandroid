package com.lxy.sharesquare.net;

import com.lxy.basemodel.network.model.ArticleModel;
import com.lxy.basemodel.network.model.PageModel;
import com.lxy.basemodel.network.model.ResponseModel;
import com.lxy.basemodel.network.BaseResponse;
import com.lxy.basemodel.network.model.ShareModel;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

/**
 * Created by Administrator on 2020/1/2.
 */

public interface SquareApi {


    /**
     * 获取广场列表的数据
     * @param page
     * @return
     */
    @GET("user_article/list/{page}/json")
    Observable<BaseResponse<PageModel<ArticleModel>>> getSquareArticles(@Path("page")int page);


    /**
     * 获取某个分享人的分享文章
     * @param user
     * @param page
     * @return
     */
    @GET("user/{user}/share_articles/{page}/json")
    Observable<BaseResponse<ShareModel<ArticleModel>>> getUserShareArticles(@Path("user")int user, @Path("page")int page);

    /**
     * 获取自己分享的文章
     * @param page
     * @return
     */
    @GET("user/lg/private_articles/{page}/json")
    Observable<BaseResponse<ShareModel<ArticleModel>>> getOwnShareArticles(@Path("page")int page);

    /**
     * 删除自己分享的文章
     * @param id
     * @return
     */
    @POST("lg/user_article/delete/{id}/json")
    Observable<BaseResponse> deleteOwnShareArticleById(@Path("id")int id);
}
