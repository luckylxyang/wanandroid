package lxy.com.wanandroid.baseadapter;

import android.content.Context;
import android.view.ViewGroup;

import java.util.List;

/**
 * Creator : lxy
 * date: 2019/2/8
 */

public abstract class MultiTypeBaseAdapter<T> extends BaseAdapter<T> {

    private MultiTypeSupport multiTypeSupport;

    public MultiTypeBaseAdapter(Context context, List<T> list,MultiTypeSupport<T> multiItemTypeSupport) {
        super(context, list, -1);
        this.multiTypeSupport = multiItemTypeSupport;
    }

    @Override
    public int getItemViewType(int position) {
        return multiTypeSupport.getItemViewType(position,list.get(position));
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        int layoutId = multiTypeSupport.getLayoutId(viewType);
        ViewHolder holder = ViewHolder.get(context,layoutId, parent);
        return holder;
    }

}
