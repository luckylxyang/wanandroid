package lxy.com.wanandroid.knowledge;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

import lxy.com.wanandroid.R;
import lxy.com.wanandroid.baseadapter.BaseAdapter;
import lxy.com.wanandroid.baseadapter.ViewHolder;

/**
 * Creator : lxy
 * date: 2019/2/26
 */

public class KnowledgeAdapter extends BaseQuickAdapter<KnowledgeModel.DataBean,BaseViewHolder> {


    public KnowledgeAdapter(int layoutResId, @Nullable List<KnowledgeModel.DataBean> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder holder, KnowledgeModel.DataBean model) {
        holder.setText(R.id.item_knowledge_title,model.getName());
        FlowLayout flowLayout = holder.getView(R.id.item_knowledge_recycle);
        LayoutInflater inflater = LayoutInflater.from(mContext);
        flowLayout.removeAllViews();
        for (KnowledgeModel.DataBean.ChildrenBean bean : model.getChildren()) {
            TextView tv = (TextView) inflater.inflate(R.layout.item_item_knowledge,flowLayout,false);
            tv.setText(bean.getName());
            flowLayout.addView(tv);
        }
    }
}
