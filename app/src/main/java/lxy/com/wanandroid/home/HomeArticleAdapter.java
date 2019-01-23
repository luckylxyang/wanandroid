package lxy.com.wanandroid.home;

import android.content.Context;
import android.text.TextUtils;

import java.util.List;

import lxy.com.wanandroid.R;
import lxy.com.wanandroid.base.BaseAdapter;
import lxy.com.wanandroid.base.ViewHolder;
import lxy.com.wanandroid.home.model.ArticleModel;

/**
 * date: 2019/1/15
 * @author lxy
 */

public class HomeArticleAdapter extends BaseAdapter<ArticleModel> {

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
    }
}
