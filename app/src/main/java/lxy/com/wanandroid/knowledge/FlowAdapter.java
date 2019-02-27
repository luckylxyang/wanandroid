package lxy.com.wanandroid.knowledge;

import android.content.Context;

import java.util.List;

import lxy.com.wanandroid.R;
import lxy.com.wanandroid.baseadapter.BaseAdapter;
import lxy.com.wanandroid.baseadapter.ViewHolder;

/**
 * Creator : lxy
 * date: 2019/2/26
 */

public class FlowAdapter extends BaseAdapter<KnowledgeModel.DataBean.ChildrenBean> {

    public FlowAdapter(Context context, List<KnowledgeModel.DataBean.ChildrenBean> list, int layoutId) {
        super(context, list, layoutId);
    }

    @Override
    protected void convert(ViewHolder holder, KnowledgeModel.DataBean.ChildrenBean childrenBean, int position) {
        holder.setText(R.id.item_item_knowledge_title,childrenBean.getName());
    }
}
