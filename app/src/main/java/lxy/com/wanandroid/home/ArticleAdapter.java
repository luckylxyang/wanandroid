package lxy.com.wanandroid.home;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.support.annotation.Nullable;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.widget.ImageView;

import com.alibaba.android.arouter.launcher.ARouter;
import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.lxy.basemodel.base.Constants;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import lxy.com.wanandroid.R;
import lxy.com.wanandroid.base.ToastUtils;
import lxy.com.wanandroid.home.model.ArticleModel;
import lxy.com.wanandroid.network.NetworkManager;

/**
 * Creator : lxy
 * date: 2019/2/8
 */

public class ArticleAdapter extends BaseQuickAdapter<ArticleModel,BaseViewHolder> {


    public ArticleAdapter(int layoutResId, @Nullable List<ArticleModel> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, ArticleModel articleModel) {
        helper.setText(R.id.item_home_article_title, articleModel.getTitle())
                .setText(R.id.item_home_article_author, articleModel.getAuthor())
                .setText(R.id.item_home_article_time, articleModel.getNiceDate());

        StringBuffer tag = new StringBuffer();
        for (int i = 0; i < articleModel.getTags().size(); i++) {
            if (i > 0){
                tag.append(" & ");
            }
            ArticleModel.TagsBean tagsBean = articleModel.getTags().get(i);
            tag.append(tagsBean.getName());
        }
        helper.setText(R.id.item_home_article_tag, tag.toString());
        hasTitleHighLight(helper,articleModel.getTitle());
        if (articleModel.isCollect()){
            helper.setImageResource(R.id.item_home_article_like,R.drawable.ic_article_like);
        }else {
            helper.setImageResource(R.id.item_home_article_like,R.drawable.ic_article_unlike);
        }
        helper.setOnClickListener(R.id.item_home_article_like, v -> {
            if (articleModel.isCollect()){
                unCollectArticle(helper,articleModel);
            }else {
                collectArticle(helper,articleModel);
            }
        });
        if (!TextUtils.isEmpty(articleModel.getEnvelopePic())) {
            ImageView iv = helper.getView(R.id.item_home_article_image);
            iv.setVisibility(View.VISIBLE);
            Glide.with(mContext)
                    .load(articleModel.getEnvelopePic())
                    .into(iv);
        }
    }

    private void hasTitleHighLight(BaseViewHolder helper, String title) {
        if (!title.contains("<em class=")) {
            helper.setText(R.id.item_home_article_title,title);
            return;
        }
        String pattern1 = "<";
        Pattern compile = Pattern.compile(pattern1);
        StringBuffer buffer = new StringBuffer(title);
        Matcher matcher = compile.matcher(buffer);

        List<Integer> indexs = new ArrayList<>();
        while (buffer.indexOf("<") > -1) {
            int start = buffer.indexOf("<");
            int end = buffer.indexOf(">") + 1;
            buffer.replace(start,end,"");
            end = buffer.indexOf("<");
            indexs.add(start);
            indexs.add(end);
            buffer.replace(end,end + 5,"");
        }
        SpannableStringBuilder builder = new SpannableStringBuilder(buffer);
        for (int i = 0; i < indexs.size(); i = i + 2) {
            builder.setSpan(new ForegroundColorSpan(mContext.getResources().getColor(R.color.colorPrimary)),
                    indexs.get(i), indexs.get(i + 1), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        }
        helper.setText(R.id.item_home_article_title,builder);
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
                                ARouter.getInstance().build(Constants.URL_LOGIN_ACTIVITY).navigation();
                            } else {
                                ToastUtils.show( R.string.collect_success);
                                collectAnimator(holder,articleModel);

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

    private void collectAnimator(BaseViewHolder holder, ArticleModel articleModel) {
        View view = holder.getView(R.id.item_home_article_like);
        ObjectAnimator animator = ObjectAnimator.ofFloat(view,"scaleX",1f,0f);
        ObjectAnimator animator2 = ObjectAnimator.ofFloat(view,"scaleX",0f,1f);
        animator.setDuration(300).start();
        animator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                articleModel.setCollect(true);
                ArticleAdapter.this.notifyDataSetChanged();
                animator2.setDuration(300).start();
            }
        });
    }

    private void unCollectAnimator(BaseViewHolder holder, ArticleModel articleModel) {
        View view = holder.getView(R.id.item_home_article_like);
        ObjectAnimator animator = ObjectAnimator.ofFloat(view,"scaleX",1f,0f);
        ObjectAnimator animator2 = ObjectAnimator.ofFloat(view,"scaleX",0f,1f);
        animator.setDuration(300).start();
        animator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                articleModel.setCollect(false);
                ArticleAdapter.this.notifyDataSetChanged();
                animator2.setDuration(300).start();
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
                                ARouter.getInstance().build(Constants.URL_LOGIN_ACTIVITY).navigation();
                            } else {
                                ToastUtils.show( R.string.uncollect_success);
                                unCollectAnimator(holder, articleModel);
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
