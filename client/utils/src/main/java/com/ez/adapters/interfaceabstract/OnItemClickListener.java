package com.ez.adapters.interfaceabstract;

import android.view.View;

import com.ez.adapters.R;
import com.ez.adapters.adapter.BaseAdapterRv;
import com.ez.adapters.adapter.BaseAdapterRvList;
import com.ez.adapters.base.BaseViewHolder;

/**
 * 点击,长按,header,footer的回调
 * 完美解决类似recyclerviewAdapter的setOnclicklistener重复new 对象的问题
 * 可以看看怎么写的{@link BaseAdapterRv}
 * holder.mRLLiaoTian.setTag(R.id.tag_view_click,position);
 * holder.mRLLiaoTian.setOnClickListener(mLiaoTianListener(全局常量,不用每个都new一下));
 */
public abstract class OnItemClickListener implements IItemClick {
    @Override
    public final void onClick(View view) {
        int position = getViewPosition(view);
        switch (position) {
            case BaseAdapterRvList.POSITION_HEADER:
                onHeaderClick(view);
                break;
            case BaseAdapterRvList.POSITION_FOOTER:
                onFooterClick(view);
                break;
            default:
                onItemClick(view, position);
                break;
        }
    }

    @Override
    public final boolean onLongClick(View view) {
        int position = getViewPosition(view);
        switch (position) {
            case BaseAdapterRvList.POSITION_HEADER:
                return onHeaderLongClick(view);
            case BaseAdapterRvList.POSITION_FOOTER:
                return onFooterLongClick(view);
            default:
                return onItemLongClick(view, position);
        }
    }

    /**
     * 获取当前view所保存的position
     */
    protected final int getViewPosition(View view) {
        return (int) view.getTag(R.id.tag_view_click);
    }

    /**
     * 获取当前view所在的ViewHolder
     */
    protected final BaseViewHolder getViewHolder(View view) {
        return (BaseViewHolder) view.getTag(R.id.tag_view_holder);
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // 以下是item,header,footer的点击和长按回调
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    /**
     * item被点击时
     *
     * @param listPosition list集合所对应的position,不需要-1
     */
    protected abstract void onItemClick(View view, int listPosition);

    /**
     * item被长按时
     *
     * @param listPosition list集合所对应的position,不需要-1
     */
    protected boolean onItemLongClick(View view, int listPosition) {
        return false;
    }

    /**
     * 添加的header被点击时,没有可以忽略
     */
    protected void onHeaderClick(View view) {
    }

    /**
     * 添加的header被长按时,没有可以忽略
     */
    protected boolean onFooterLongClick(View view) {
        return false;
    }

    /**
     * 添加的footer被点击时,没有可以忽略
     */
    protected void onFooterClick(View view) {
    }

    /**
     * 添加的footer被长按时,没有可以忽略
     */
    protected boolean onHeaderLongClick(View view) {
        return false;
    }
}