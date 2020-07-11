package com.ez.adapters.base;

import androidx.annotation.DrawableRes;
import androidx.annotation.IdRes;
import androidx.annotation.IntDef;
import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * 所有ViewHolder的基类
 * 和经典的viewholder类似,更方便
 */
public class BaseViewHolder extends RecyclerView.ViewHolder {
    @Nullable
    protected SparseArray<View> mViews;

    /**
     * 工具生成的一般都是这个
     */
    public BaseViewHolder(@NonNull View view) {
        super(view);
    }

    public BaseViewHolder(@NonNull ViewGroup parent, @LayoutRes int layoutId) {
        this(parent, layoutId, LayoutInflater.from(parent.getContext()));
    }

    public BaseViewHolder(ViewGroup parent, @LayoutRes int layoutId, LayoutInflater inflater) {
        this(inflater.inflate(layoutId, parent, false));
    }

    public <T extends View> T getView(@IdRes int resId) {
        if (mViews == null) {
            mViews = new SparseArray<>();
        }
        View view = mViews.get(resId);
        if (view == null) {
            view = itemView.findViewById(resId);
            mViews.put(resId, view);
        }
        //noinspection unchecked
        return (T) view;
    }

    public BaseViewHolder setText(@IdRes int resId, CharSequence st) {
        ((TextView) getView(resId)).setText(st);
        return this;
    }

    public BaseViewHolder setImage(@IdRes int resId, @DrawableRes int drawableId) {
        ((ImageView) this.getView(resId)).setImageResource(drawableId);
        return this;
    }

//    /**
//     * 加载普通/网络图片
//     */
//    public BaseViewHolder setImage(@IdRes int resId, String url) {
//        GlideUtil.load((Activity) itemView.getContext(), url, (ImageView) this.getView(resId));
//        return this;
//    }

    public BaseViewHolder setBackground(@IdRes int resId, @DrawableRes int drawableId) {
        getView(resId).setBackgroundResource(drawableId);
        return this;
    }

    //控件是否显示
    public BaseViewHolder setViewVisible(@IdRes int resId, @ViewVisibility int visible) {
        getView(resId).setVisibility(visible);
        return this;
    }

    /**
     * view的被hide了
     */
    @IntDef({View.VISIBLE, View.INVISIBLE, View.GONE})
    @Retention(RetentionPolicy.SOURCE)
    public @interface ViewVisibility {
    }
}