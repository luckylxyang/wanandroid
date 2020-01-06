package lxy.com.wanandroid.collect;

import android.content.Context;
import android.view.View;

import java.util.List;

import lxy.com.wanandroid.R;
import lxy.com.wanandroid.baseadapter.BaseAdapter;
import lxy.com.wanandroid.baseadapter.ViewHolder;

/**
 * Creator : lxy
 * date: 2019/3/3
 */

public class CollectAdapter extends BaseAdapter<CollectModel.DataBean.DatasBean> {

    public CollectAdapter(Context context, List<CollectModel.DataBean.DatasBean> list, int layoutId) {
        super(context, list, layoutId);
    }

    @Override
    protected void convert(ViewHolder holder, CollectModel.DataBean.DatasBean articleModel, int position) {
        holder.setText(R.id.item_home_article_title, articleModel.getTitle())
                .setText(R.id.item_home_article_author, articleModel.getAuthor())
                .setText(R.id.item_home_article_time, articleModel.getNiceDate());
        holder.getView(R.id.item_home_article_like).setVisibility(View.INVISIBLE);

    }
}
