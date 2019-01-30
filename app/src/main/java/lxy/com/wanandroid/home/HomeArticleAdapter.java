package lxy.com.wanandroid.home;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;

import java.io.IOException;
import java.util.List;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import lxy.com.wanandroid.R;
import lxy.com.wanandroid.base.BaseAdapter;
import lxy.com.wanandroid.base.ToastUtils;
import lxy.com.wanandroid.base.ViewHolder;
import lxy.com.wanandroid.home.model.ArticleModel;
import lxy.com.wanandroid.login.LoginActivity;
import lxy.com.wanandroid.login.LoginUtil;
import lxy.com.wanandroid.network.NetworkManager;
import okhttp3.ResponseBody;

/**
 * date: 2019/1/15
 * @author lxy
 */

public class HomeArticleAdapter extends BaseAdapter<ArticleModel> {

    private String TAG = "HomeArticleAdapter";

    public HomeArticleAdapter(Context context, List<ArticleModel> list, int layoutId) {
        super(context, list, layoutId);
    }

    @Override
    protected void convert(ViewHolder holder, ArticleModel articleModel, int position) {

        holder.setText(R.id.item_home_article_title,articleModel.getTitle())
                .setText(R.id.item_home_article_author,articleModel.getAuthor())
                .setText(R.id.item_home_article_time,articleModel.getNiceDate());

        StringBuffer tag = new StringBuffer();
        for (int i = 0; i < articleModel.getTags().size(); i++) {
            ArticleModel.TagsBean tagsBean = articleModel.getTags().get(i);
            tag.append(tagsBean.getName() + "&");
        }
        if (!TextUtils.isEmpty(tag)){
            holder.setText(R.id.item_home_article_tag,tag.substring(0,tag.length() - 1));
        }
        holder.setOnClickListener(R.id.item_home_article_like, v -> {
            if(!LoginUtil.getInstance().checkLogin()){
                ToastUtils.show(context,R.string.login_out);
                Intent intent = new Intent(context, LoginActivity.class);
                context.startActivity(intent);
            }else {
                NetworkManager.getManager().getServer().collectArticleInSite(articleModel.getId())
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Observer<ResponseBody>() {
                            @Override
                            public void onSubscribe(Disposable d) {

                            }

                            @Override
                            public void onNext(ResponseBody responseBody) {
                                try {
                                    Log.i(TAG,responseBody.string());
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }

                            @Override
                            public void onError(Throwable e) {
                                e.printStackTrace();
                            }

                            @Override
                            public void onComplete() {

                            }
                        });
            }
        });
    }
}
