package lxy.com.wanandroid.home;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.text.TextUtils;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import lxy.com.wanandroid.R;
import lxy.com.wanandroid.base.ResponseModel;
import lxy.com.wanandroid.base.ToastUtils;
import lxy.com.wanandroid.home.model.ArticleModel;
import lxy.com.wanandroid.login.LoginActivity;
import lxy.com.wanandroid.network.NetworkManager;

/**
 * Creator : lxy
 * date: 2019/2/8
 */

public class HomeAdapter extends BaseQuickAdapter<ArticleModel,BaseViewHolder> {

    public HomeAdapter(int layoutResId, @Nullable List<ArticleModel> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, ArticleModel articleModel) {
        helper.setText(R.id.item_home_article_title, articleModel.getTitle())
                .setText(R.id.item_home_article_author, articleModel.getAuthor())
                .setText(R.id.item_home_article_time, articleModel.getNiceDate());

        StringBuffer tag = new StringBuffer();
        for (int i = 0; i < articleModel.getTags().size(); i++) {
            ArticleModel.TagsBean tagsBean = articleModel.getTags().get(i);
            tag.append(tagsBean.getName() + "&");
        }
        if (!TextUtils.isEmpty(tag)) {
            helper.setText(R.id.item_home_article_tag, tag.substring(0, tag.length() - 1));
        }
        if (articleModel.isCollect()){
            helper.setImageResource(R.id.item_home_article_like,R.drawable.ic_article_like);
        }
        helper.setOnClickListener(R.id.item_home_article_like, v -> {
            if (articleModel.isCollect()){
                unCollectArticle(helper,articleModel);
            }else {
                collectArticle(helper,articleModel);
            }
        });
    }


    private void collectArticle(BaseViewHolder holder, ArticleModel articleModel){
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
                                ToastUtils.show(R.string.login_yet);
                                Intent intent = new Intent(mContext, LoginActivity.class);
                                mContext.startActivity(intent);
                            } else {
                                ToastUtils.show( R.string.collect_success);
                                holder.setImageResource(R.id.item_home_article_like, R.drawable.ic_article_like);
                                articleModel.setCollect(true);
                                HomeAdapter.this.notifyDataSetChanged();
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

    private void unCollectArticle(BaseViewHolder holder, ArticleModel articleModel){
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
                                ToastUtils.show(R.string.login_yet);
                                Intent intent = new Intent(mContext, LoginActivity.class);
                                mContext.startActivity(intent);
                            } else {
                                ToastUtils.show( R.string.uncollect_success);
                                holder.setImageResource(R.id.item_home_article_like, R.drawable.ic_article_unlike);
                                articleModel.setCollect(false);
                                HomeAdapter.this.notifyDataSetChanged();
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
