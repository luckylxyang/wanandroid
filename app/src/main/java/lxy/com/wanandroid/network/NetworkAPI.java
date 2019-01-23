package lxy.com.wanandroid.network;

import io.reactivex.Observable;
import lxy.com.wanandroid.base.ResponseModel;
import lxy.com.wanandroid.home.model.ArticleModel;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

/**
 * @author : lxy
 * date: 2019/1/15
 */

public interface NetworkAPI {

    /**
     * Get article in a website which link is www.wanandroid.com
     * @param page
     * @return
     */
    @GET("article/list/{page}/json")
    Observable<ResponseModel> getArticleList(@Path("page") int page);

    @GET("article/list/{page}/json")
    Call<ResponseBody> getArticle(@Path("page") int page);

}
