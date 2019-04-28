package com.han.mvpdome.base;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.han.mvpdome.R;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public abstract class RVBaseAdapter<T> extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    public BaseActivity activity;
    public ArrayList<T> allList = new ArrayList<T>();
    protected String footerStatus = "nomal";

    public static final int TYPE_EMPTY = -1;  //数据为空


    public RVBaseAdapter(BaseActivity activity) {
        this.activity = activity;
    }


    public void setFooterStatus(String footerStatus, boolean isRefresh) {
        this.footerStatus = footerStatus;
        if (isRefresh) {
            notifyDataSetChanged();
        }
    }

    public void setAllList(List<T> arraylist) {
        allList.clear();
        if (arraylist != null) {
            allList.addAll(arraylist);
        }
        notifyDataSetChanged();
    }

    public ArrayList<T> getAllList() {
        return allList;
    }

    /**
     * position 为-1时，直接add
     *
     * @param position
     * @param arraylist
     */
    public void addArrayList(int position, List<T> arraylist) {
        if (arraylist != null) {
            if (position > -1) {
                allList.addAll(position, arraylist);
            } else {
                allList.addAll(arraylist);
            }
            notifyDataSetChanged();
        }
    }

    public void addEntity(T entity) {
        allList.clear();
        if (entity != null) {
            allList.add(entity);
            notifyDataSetChanged();
        }
    }

    public T getEntity(int position) {
        return allList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public static class EmptyViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.tv_hint)
        public TextView tvHint;

        public EmptyViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }

    public interface OnItemClickListener {
        void onItemClicked(int position);
    }

    private OnItemClickListener mOnItemClickListener;

    public void setOnItemClickListener(OnItemClickListener mOnItemClickListener) {
        this.mOnItemClickListener = mOnItemClickListener;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, final int position) {
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mOnItemClickListener != null) {
                    if (getItemViewType(position) != TYPE_EMPTY) {
                        mOnItemClickListener.onItemClicked(position);
                    }
                }
            }
        });
    }

    public T getItem(int position) {
        if (getItemViewType(position) == TYPE_EMPTY) {
            return null;
        }
        return allList.get(position);
    }

    public void setItem(T t, int position) {
        allList.set(position, t);
    }
}

