package lxy.com.wanandroid.base;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

/**
 *
 * @author 刘晓阳
 * @date 2018/1/17
 */

public abstract class BaseAdapter<T> extends RecyclerView.Adapter<ViewHolder> {

    protected Context context;
    protected List<T> list;
    private int layoutId;
    private LayoutInflater inflater;
    protected OnItemClickListener listener;
    protected OnItemLongClickListener longListener;
    protected int pos;

    public BaseAdapter(Context context, List<T> list, int layoutId) {
        this.context = context;
        this.list = list;
        this.layoutId = layoutId;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        ViewHolder holder = ViewHolder.get(context,layoutId,parent);
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        convert(holder, list.get(position),position);

    }

    protected abstract void convert(ViewHolder holder, T t,int position);



    @Override
    public int getItemCount() {
        return list.size();
    }

    public interface OnItemClickListener{
        void onClick(View view, int position);
    }

    public interface OnItemLongClickListener{
        void onClick(View view, int position);
    }

    public void setOnItemListener(OnItemClickListener listener){
        this.listener = listener;
    }

    public void setOnItemLongListener(OnItemLongClickListener listener){
        this.longListener = listener;
    }
}
