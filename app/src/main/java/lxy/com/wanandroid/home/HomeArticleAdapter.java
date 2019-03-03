package lxy.com.wanandroid.home;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;

import java.util.List;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import lxy.com.wanandroid.R;
import lxy.com.wanandroid.baseadapter.BaseAdapter;
import lxy.com.wanandroid.base.ResponseModel;
import lxy.com.wanandroid.base.ToastUtils;
import lxy.com.wanandroid.baseadapter.ViewHolder;
import lxy.com.wanandroid.home.model.ArticleModel;
import lxy.com.wanandroid.login.LoginActivity;
import lxy.com.wanandroid.network.NetworkManager;

/**
 * date: 2019/1/15
 *
 * @author lxy
 */

public class HomeArticleAdapter extends BaseAdapter<ArticleModel> {

    private String TAG = "HomeArticleAdapter";

    public HomeArticleAdapter(Context context, List<ArticleModel> list, int layoutId) {
        super(context, list, layoutId);
    }

    @SuppressLint("ResourceType")
    @Override
    protected void convert(ViewHolder holder, ArticleModel articleModel, int position) {

        holder.setText(R.id.item_home_article_title, articleModel.getTitle())
                .setText(R.id.item_home_article_author, articleModel.getAuthor())
                .setText(R.id.item_home_article_time, articleModel.getNiceDate());

        StringBuffer tag = new StringBuffer();
        for (int i = 0; i < articleModel.getTags().size(); i++) {
            ArticleModel.TagsBean tagsBean = articleModel.getTags().get(i);
            tag.append(tagsBean.getName() + "&");
        }
        if (!TextUtils.isEmpty(tag)) {
            holder.setText(R.id.item_home_article_tag, tag.substring(0, tag.length() - 1));
        }
        if (articleModel.isCollect()){
            holder.setImageResource(R.id.item_home_article_like,R.drawable.ic_article_like);
        }
        holder.setOnClickListener(R.id.item_home_article_like, v -> {
            if (articleModel.isCollect()){
                unCollectArticle(holder,articleModel);
            }else {
                collectArticle(holder,articleModel);
            }
        });
    }

    private void collectArticle(ViewHolder holder, ArticleModel articleModel){
        NetworkManager.getManager().getServer().collectArticleInSite(articleModel.getId())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<ResponseModel>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @SuppressLint("ResourceType")
                    @Override
                    public void onNext(ResponseModel model) {
                        try {
                            if (model.getErrorCode() != 0) {
                                ToastUtils.show(context, R.string.login_yet);
                                Intent intent = new Intent(context, LoginActivity.class);
                                context.startActivity(intent);
                            } else {
                                ToastUtils.show(context, R.string.collect_success);
                                holder.setImageResource(R.id.item_home_article_like, R.drawable.ic_article_like);
                                articleModel.setCollect(true);
                                HomeArticleAdapter.this.notifyDataSetChanged();
                            }
                        } catch (Exception e) {
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

    private void unCollectArticle(ViewHolder holder, ArticleModel articleModel){
        NetworkManager.getManager().getServer().unCollectArticle(articleModel.getId())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<ResponseModel>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @SuppressLint("ResourceType")
                    @Override
                    public void onNext(ResponseModel model) {
                        try {
                            if (model.getErrorCode() != 0) {
                                ToastUtils.show(context, R.string.login_yet);
                                Intent intent = new Intent(context, LoginActivity.class);
                                context.startActivity(intent);
                            } else {
                                ToastUtils.show(context, R.string.uncollect_success);
                                holder.setImageResource(R.id.item_home_article_like, R.drawable.ic_article_unlike);
                                articleModel.setCollect(false);
                                HomeArticleAdapter.this.notifyDataSetChanged();
                            }
                        } catch (Exception e) {
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
}
