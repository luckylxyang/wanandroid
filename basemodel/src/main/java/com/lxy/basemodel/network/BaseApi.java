package com.lxy.basemodel.network;

import com.lxy.basemodel.network.model.ArticleModel;
import com.lxy.basemodel.network.model.PageModel;
import com.lxy.basemodel.network.model.ResponseModel;

import io.reactivex.Observable;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;
import retrofit2.http.Path;

/**
 * Created by Administrator on 2020/1/7.
 */

public interface BaseApi {

    /**
     * 收藏站内文章
     * http://www.wanandroid.com/lg/collect/1165/json
     * @param id
     * @return
     */
    @POST("lg/collect/{id}/json")
    Observable<BaseResponse<PageModel<ArticleModel>>> collectArticleInSite(@Path("id") int id);

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
    Observable<BaseResponse<PageModel<ArticleModel>>> collectArticleOutSite(@Field("title") int title,
                                                    @Field("author") String author,
                                                    @Field("link") String link);

    /**
     * 在文章列表取消收藏
     * http://www.wanandroid.com/lg/uncollect_originId/2333/json
     * @return
     */
    @POST("lg/uncollect_originId/{id}/json")
    Observable<BaseResponse<PageModel<ArticleModel>>> unCollectArticle(@Path("id") int id);

}
