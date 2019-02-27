package lxy.com.wanandroid.home;

import android.content.Context;

import java.util.List;

import lxy.com.wanandroid.R;
import lxy.com.wanandroid.baseadapter.MultiTypeBaseAdapter;
import lxy.com.wanandroid.baseadapter.MultiTypeSupport;
import lxy.com.wanandroid.baseadapter.ViewHolder;
import lxy.com.wanandroid.home.model.ArticleModel;

/**
 * Creator : lxy
 * date: 2019/2/26
 */

public class HomeMultiAdapter extends MultiTypeBaseAdapter<ArticleModel> {

    public HomeMultiAdapter(Context context, List<ArticleModel> list, MultiTypeSupport<ArticleModel> multiItemTypeSupport) {
        super(context, list, multiItemTypeSupport);
    }

    @Override
    protected void convert(ViewHolder holder, ArticleModel articleModel, int position) {
        if (getItemViewType(position) == 0){
            return;
        }else {
            holder.setText(R.id.item_home_article_title, articleModel.getTitle())
                    .setText(R.id.item_home_article_author, articleModel.getAuthor())
                    .setText(R.id.item_home_article_time, articleModel.getNiceDate());
        }
    }
}
