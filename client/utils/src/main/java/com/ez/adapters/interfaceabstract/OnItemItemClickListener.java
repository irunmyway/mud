package com.ez.adapters.interfaceabstract;

import androidx.viewpager.widget.ViewPager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;

import com.ez.adapters.R;
import com.zhy.view.flowlayout.FlowLayout;

/**
 * 高级功能:adapter套adapter的点击事件,具体用法见实现类
 * //bind直接使用adapter的mListener即可
 * adapter.setOnItemClickListener(mListener);
 * <p>
 * //creat里面写法
 * OrderSunListAdapter adapter = (OrderSunListAdapter) holder.mRv.getAdapter();
 * adapter.setListAndNotifyDataSetChanged(orderBean.getListOrderDetail());
 * holder.mRv.setTag(R.id.tag_view_click,position);
 */
public abstract class OnItemItemClickListener extends OnItemClickListener {
    @Override
    protected final void onItemClick(View view, int listPosition) {
        Integer parentOrNull = getParentTag(view);
        if (parentOrNull == null) {
            onParentItemClick(view, listPosition);
        } else {
            onChildItemClick(view, parentOrNull, listPosition);
        }
    }

    @Override
    protected final boolean onItemLongClick(View view, int listPosition) {
        Integer parentOrNull = getParentTag(view);
        if (parentOrNull == null) {
            return onParentItemLongClick(view, listPosition);
        } else {
            return onChildItemLongClick(view, parentOrNull, listPosition);
        }
    }

    @Override
    protected final void onHeaderClick(View view) {
        Integer parentOrNull = getParentTag(view);
        if (parentOrNull == null) {
            onParentHeaderClick(view);
        } else {
            onChildHeaderClick(view, parentOrNull);
        }
    }

    @Override
    protected final boolean onHeaderLongClick(View view) {
        Integer parentOrNull = getParentTag(view);
        if (parentOrNull == null) {
            return onParentHeaderLongClick(view);
        } else {
            return onChildHeaderLongClick(view, parentOrNull);
        }
    }

    @Override
    protected final void onFooterClick(View view) {
        Integer parentOrNull = getParentTag(view);
        if (parentOrNull == null) {
            onParentFooterClick(view);
        } else {
            onChildFooterClick(view, parentOrNull);
        }
    }

    @Override
    protected final boolean onFooterLongClick(View view) {
        Integer parentOrNull = getParentTag(view);
        if (parentOrNull == null) {
            return onParentFooterLongClick(view);
        } else {
            return onChildFooterLongClick(view, parentOrNull);
        }
    }

    /**
     * 外层的position需要遍历
     */
    protected final Integer getParentTag(View v) {
        ViewParent parent = v.getParent();
        while (parent != null) {
            //第二层不建议使用listview或gridview(肯定没有复用性,并且效率很差,可以尝试使用recyclerview然后wrap)
//            if (parent instanceof RecyclerView || parent instanceof ViewPager || parent instanceof FlowLayout || parent instanceof AdapterView) {
            if (parent instanceof RecyclerView || parent instanceof ViewPager || parent instanceof FlowLayout) {
                return (Integer) ((ViewGroup) parent).getTag(R.id.tag_view_click);
            }
            parent = parent.getParent();
        }
        //没取到返回null
        return null;
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // 以下是parent的回调
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    /**
     * 当外层被点击时
     *
     * @param parentPosition 外层adapter的position
     */
    protected abstract void onParentItemClick(View view, int parentPosition);

    /**
     * 当外层被长按时
     *
     * @param parentPosition 外层adapter的position
     */
    protected boolean onParentItemLongClick(View view, int parentPosition) {
        return false;
    }

    protected void onParentHeaderClick(View view) {
    }

    protected boolean onParentHeaderLongClick(View view) {
        return false;
    }

    protected void onParentFooterClick(View view) {
    }

    protected boolean onParentFooterLongClick(View view) {
        return false;
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // 以下是child的回调
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    /**
     * 当内层被点击时
     *
     * @param parentPosition 外层adapter对应的position
     * @param childPosition  内层adapter对应的position
     */
    protected abstract void onChildItemClick(View view, int parentPosition, int childPosition);

    /**
     * 当内层被长按时
     *
     * @param parentPosition 外层adapter对应的position
     * @param childPosition  内层adapter对应的position
     */
    protected boolean onChildItemLongClick(View view, int parentPosition, int childPosition) {
        return false;
    }

    protected void onChildHeaderClick(View view, int parentPosition) {
    }

    protected boolean onChildHeaderLongClick(View view, int parentPosition) {
        return false;
    }

    protected void onChildFooterClick(View view, int parentPosition) {
    }

    protected boolean onChildFooterLongClick(View view, int parentPosition) {
        return false;
    }
}