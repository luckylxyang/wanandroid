package com.lxy.basemodel.baseadapter;

import android.content.Context;
import android.support.annotation.LayoutRes;
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
    protected OnItemClickListener listener;
    protected OnItemLongClickListener longListener;


    private int ITEM_HEADER = 0;
    private int ITEM_COMMON = 1;
    private int ITEM_FOOTER = 2;
    protected OnItemChildClickListener childClickListener;

    public BaseAdapter(List<T> list,@LayoutRes int layoutId) {
        this.list = list;
        this.layoutId = layoutId;
    }

    public BaseAdapter(Context context, List<T> list,@LayoutRes int layoutId) {
        this.context = context;
        this.list = list;
        this.layoutId = layoutId;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (context == null){
            context = parent.getContext();
        }
        ViewHolder holder = ViewHolder.get(context,layoutId,parent);
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        holder.setBaseAdapter(this);
        holder.convertView.setOnClickListener(v -> {
            if (listener != null){
                listener.onClick(v,position);
            }
        });
        holder.convertView.setOnLongClickListener(v -> {
            if (longListener != null){
                longListener.onLongClick(v,position);
            }
            return true;
        });
        convert(holder, list.get(position),position);

    }

    protected abstract void convert(ViewHolder holder, T t,int position);


    @Override
    public int getItemCount() {
        return list.size();
    }

    /**
     * 列表的每条 item 的点击事件
     */
    public interface OnItemClickListener{

        void onClick(View view, int position);
    }

    public interface OnItemChildClickListener{

        void onClick(View view, int position);
    }

    public interface OnItemLongClickListener{
        void onLongClick(View view, int position);
    }

    public void setOnItemListener(OnItemClickListener listener){
        this.listener = listener;
    }

    public void setOnItemLongListener(OnItemLongClickListener listener){
        this.longListener = listener;
    }

    public void addOnItemChildClickListener(OnItemChildClickListener listener){
        this.childClickListener = listener;
    }


}
