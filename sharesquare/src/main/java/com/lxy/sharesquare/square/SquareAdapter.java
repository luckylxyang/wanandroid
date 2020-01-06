package com.lxy.sharesquare.square;

import android.content.Context;
import android.text.TextUtils;

import com.lxy.basemodel.baseadapter.BaseAdapter;
import com.lxy.basemodel.baseadapter.ViewHolder;
import com.lxy.basemodel.network.model.ArticleModel;
import com.lxy.sharesquare.R;

import java.util.List;

/**
 * Created by Administrator on 2020/1/6.
 */

public class SquareAdapter extends BaseAdapter<ArticleModel>{

    public SquareAdapter(List<ArticleModel> list, int layoutId) {
        super( list, layoutId);
    }

    @Override
    protected void convert(ViewHolder helper, ArticleModel articleModel, int position) {
        helper.setText(R.id.item_home_article_title, articleModel.getTitle())
                .setText(R.id.item_home_article_author, !TextUtils.isEmpty(articleModel.getAuthor()) ? articleModel.getAuthor() : articleModel.getShareUser())
                .setText(R.id.item_home_article_time, articleModel.getNiceDate());

    }
}
