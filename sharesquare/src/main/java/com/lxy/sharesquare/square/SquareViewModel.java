package com.lxy.sharesquare.square;

import android.app.Application;
import android.support.annotation.NonNull;

import com.lxy.basemodel.base.BaseViewModel;
import com.lxy.basemodel.network.BaseResponse;
import com.lxy.basemodel.network.NetworkManager;
import com.lxy.basemodel.network.RxHelper;
import com.lxy.basemodel.network.model.ArticleModel;
import com.lxy.basemodel.network.model.PageModel;
import com.lxy.sharesquare.net.SquareApi;

import io.reactivex.Observable;

/**
 * Created by Administrator on 2020/1/3.
 */

public class SquareViewModel extends BaseViewModel {


    public SquareViewModel(@NonNull Application application) {
        super(application);
    }

    public Observable<BaseResponse<PageModel<ArticleModel>>> getSquareArticle(int page){
        return NetworkManager.getManager().getServer(SquareApi.class)
                .getSquareArticles(page)
                .compose(RxHelper.observableIO2Main());
    }
}
