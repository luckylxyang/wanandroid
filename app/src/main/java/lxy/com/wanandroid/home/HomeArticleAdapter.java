package lxy.com.wanandroid.home;

import android.content.Context;

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

        holder.setText(R.id.item_home_article_title,articleModel.getTitle());
    }
}
