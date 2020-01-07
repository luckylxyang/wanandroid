package com.lxy.basemodel.baseadapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.annotation.DrawableRes;
import android.support.annotation.IdRes;
import android.support.v7.widget.RecyclerView;
import android.text.Spannable;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by 刘晓阳 on 2018/2/1.
 */

public class ViewHolder extends RecyclerView.ViewHolder {

    private SparseArray<View> views;
    private Context context;
    public View convertView;
    private List<Integer> mList;
    private BaseAdapter baseAdapter;

    public ViewHolder(Context context, View itemView, ViewGroup parentView) {
        super(itemView);
        this.context = context;
        this.convertView = itemView;
        views = new SparseArray<>();
        mList = new ArrayList<>();
    }

    public static ViewHolder get(Context context, int layoutId, ViewGroup parentView) {
        View view = LayoutInflater.from(context).inflate(layoutId, parentView, false);

        ViewHolder holder = new ViewHolder(context, view, parentView);
        return holder;
    }

    public ViewHolder setBaseAdapter(BaseAdapter adapter) {
        baseAdapter = adapter;
        return this;
    }

    public <T extends View> T getView(int viewId) {
        View view = views.get(viewId);
        if (view == null) {
            view = convertView.findViewById(viewId);
            views.append(viewId, view);
        }

        return (T) view;
    }

    public ViewHolder setOnClickListener(int viewId, View.OnClickListener listener) {
        View view = getView(viewId);
        view.setOnClickListener(listener);
        return this;
    }

    public ViewHolder setOnLongClickListener(int viewId, View.OnLongClickListener listener) {
        View view = getView(viewId);
        view.setOnLongClickListener(listener);
        return this;
    }

    public ViewHolder setText(int viewId, String message) {
        TextView tv = getView(viewId);
        tv.setText(message);
        return this;
    }

    public ViewHolder setText(int viewId, Spannable message) {
        TextView tv = getView(viewId);
        tv.setText(message);
        return this;
    }

    public ViewHolder setText(int viewId, CharSequence message) {
        TextView tv = getView(viewId);
        tv.setText(message);
        return this;
    }

    @SuppressLint("ResourceType")
    public ViewHolder setImageResource(int viewId, @DrawableRes int imageId) {
        ImageView iv = getView(viewId);
        iv.setImageResource(imageId);
        return this;
    }


    public ViewHolder addItemChildListener(int viewId) {
        mList.add(viewId);
        View view = getView(viewId);
        if (view != null) {
            if (!view.isClickable()) {
                view.setClickable(true);
            }
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (baseAdapter != null) {
                        baseAdapter.childClickListener.onClick(v, getLayoutPosition());
                    }
                }
            });
        }
        return this;
    }


}
