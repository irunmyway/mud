package com.ez.adapters.adapter;

import androidx.annotation.IntRange;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import android.util.SparseArray;
import android.view.View;
import android.view.ViewGroup;

import com.ez.adapters.utils.Utils;

import java.util.ArrayList;

/**
 * 新增:不仅仅可以回收view,也可以回收其他任何对象
 * 涉及多级嵌套(比如:购物车),不能很好复用,可以简单的使用ViewRecycler来管理(recyclerview高写成wrap也可以嵌套,当嵌套的rv的高超出屏幕时,rv会自己滑动并复用)
 * 在adapter初始化的时候顺便初始化mViewRecycler=new ViewRecycler<View></View>(最大缓存数);
 * 在复用时如下,也可参见{@link BaseAdapterLvs}
 * View view = mViewRecycler.get(0);
 * //回到以前的listview的复用模式了
 * ViewHolder vh = null;
 * if (view == null) {
 * view = LayoutInflater.from(mActivity).inflate(R.layout.pop_list, null, false);
 * vh = new ViewHolder(view);
 * vh.lvListpopLv = (LinearLayout) view.findViewById(0);
 * view.setTag(R.id.tag_view_holder, vh);
 * } else {
 * vh = (ViewHolder) view.getTag(R.id.tag_view_holder);
 * }
 */
public final class ViewRecycler<T> {

    /**
     * 最大缓存数量
     */
    @IntRange(from = 1, to = 1000)
    public int mMaxCacheSize;

    private final SparseArray<ArrayList<T>> mCaches = new SparseArray<>();

    public ViewRecycler() {
        this(10);
    }

    public ViewRecycler(@IntRange(from = 1, to = 1000) int maxCacheSize) {
        mMaxCacheSize = maxCacheSize;
    }

    /**
     * 检查是否超出缓存数量
     */
    private void checkCahe(int viewType) {
        int size = 0, typePosition = 0;
        ArrayList<T> views = mCaches.get(viewType);
        for (int i = 0; i < mCaches.size(); i++) {
            ArrayList<T> vs = mCaches.valueAt(i);
            size += vs.size();
            if (views == vs) typePosition = i;
        }
        if (size > mMaxCacheSize) {
            //太多的话,删除下一个type的view
            ArrayList<T> vs = null;
            while (Utils.isEmptyArray(vs)) {
                vs = mCaches.valueAt((++typePosition) % mCaches.size());
            }
            vs.remove(0);
        }
    }

    ///////////////////////////////////////////////////////////////////////////
    // 以下是公共方法
    ///////////////////////////////////////////////////////////////////////////

    /**
     * 回收view,会remove掉回收的child
     *
     * @param vg        父
     * @param childView 要回收的view
     * @param viewType  相当于itemtype,就一种传0即可
     */
    public void recuclerItem(@Nullable ViewGroup vg, @NonNull T childView, int viewType) {
        recuclerItem(childView, viewType);
        if (vg != null) vg.removeView((View) childView);
    }

    public void recuclerItem(@NonNull ViewGroup vg, int childIndex, int viewType) {
        //noinspection unchecked
        recuclerItem((T) vg.getChildAt(childIndex), viewType);
        vg.removeViewAt(childIndex);
    }

    public void recuclerItem(T item, int viewType) {
        ArrayList<T> views = mCaches.get(viewType);
        if (views == null) {
            views = new ArrayList<>();
            mCaches.append(viewType, views);
        }
        views.add(item);
        checkCahe(viewType);
    }

    public void recuclerAllItem(ViewGroup vg, int viewType) {
        for (int i = vg.getChildCount() - 1; i >= 0; i--) {
            recuclerItem(vg, i, viewType);
        }
    }

    public T get(int viewType) {
        ArrayList<T> views = mCaches.get(viewType);
        return Utils.isEmptyArray(views) ? null : views.remove(0);
    }

    /**
     * 获取当前type所有的view
     */
    public ArrayList<T> getTypeViews(int viewType) {
        return mCaches.get(viewType);
    }

    public void clearCache(int viewType) {
        ArrayList<T> views = mCaches.get(viewType);
        if (!Utils.isEmptyArray(views)) views.clear();
    }

    public void clearAllCache() {
        for (int i = 0; i < mCaches.size(); i++) {
            mCaches.valueAt(i).clear();
        }
    }
}
