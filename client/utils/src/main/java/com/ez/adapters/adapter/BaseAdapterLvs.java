package com.ez.adapters.adapter;

import android.app.Activity;

import androidx.annotation.IntRange;
import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.viewpager.widget.PagerAdapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;
import android.widget.SpinnerAdapter;

import com.ez.adapters.R;
import com.ez.adapters.base.BaseViewHolder;
import com.ez.adapters.interfaceabstract.IAdapter;
import com.ez.adapters.interfaceabstract.IAdapterList;
import com.ez.adapters.interfaceabstract.IItemClick;
import com.ez.adapters.interfaceabstract.OnItemClickListener;

/**
 * 超级adapter，适用于listview、gridview、viewpager
 * rv涉及到动画等,单独写{@link BaseAdapterRv}
 */
public abstract class BaseAdapterLvs<VH extends BaseViewHolder> extends PagerAdapter implements ListAdapter, SpinnerAdapter, IAdapter {
    public final String TAG = getClass().getSimpleName();

    protected final Activity mActivity;
    /**
     * 如果{@link #mActivity}是null则也是null
     */
    protected final LayoutInflater mInflater;
    protected final ViewRecycler<View> mRecycler;

    protected IItemClick mListener;

    /**
     * @param activity 是不是null用的时候自己知道，如果是null则{@link #mInflater}也为null
     */
    public BaseAdapterLvs(Activity activity) {
        mActivity = activity;
        mInflater = mActivity == null ? null : LayoutInflater.from(mActivity);
        mRecycler = new ViewRecycler<>();
    }

    ///////////////////////////////////////////////////////////////////////////
    // lv相关
    ///////////////////////////////////////////////////////////////////////////
    public final long getItemId(int position) {
        return position;
    }

    @Override
    public final View getView(int position, View convertView, ViewGroup parent) {
        VH holder;
        if (convertView == null || convertView.getTag(R.id.tag_view_holder) == null) {
            //模仿recyclerview,除了bind是position外,其他都是viewType
            holder = onCreateViewHolder(parent, getItemViewType(position), mInflater == null ? LayoutInflater.from(parent.getContext()) : mInflater);
            holder.itemView.setTag(R.id.tag_view_holder, holder);
        } else {
            //noinspection unchecked
            holder = (VH) convertView.getTag(R.id.tag_view_holder);
        }
        bindViewHolder(holder, position);
        return holder.itemView;
    }

    @Override
    public final boolean areAllItemsEnabled() {
        return true;
    }

    @Override
    public final boolean isEnabled(int position) {
        return true;
    }

    @Override
    public final View getDropDownView(int position, View convertView, ViewGroup parent) {
        return getView(position, convertView, parent);
    }

    /**
     * 此处是lv用到，list的空判断见{@link IAdapterList#isEmptyArray}
     */
    @RequiresApi(999)
    @Override
    public final boolean isEmpty() {
        return getItemCount() == 0;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    /**
     * lv为了方便,可以在lv直接获取你想要的数据,但是理论上没啥用
     * list的使用见{@link IAdapterList}的get、clear、addAll
     */
    @Override
    public final Object getItem(int position) {
        return null;
    }

    ///////////////////////////////////////////////////////////////////////////
    // vp相关
    ///////////////////////////////////////////////////////////////////////////
    @NonNull
    @Override
    public final Object instantiateItem(@NonNull ViewGroup container, int position) {
        //取缓存
        View convertView = mRecycler.get(getItemViewType(position));
        //和lv一样,直接复用代码
        View view = getView(position, convertView, container);
        container.addView(view);
        return view;
    }

    @Override
    public final void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        mRecycler.recuclerItem(container, (View) object, getItemViewType(position));
    }

    @Override
    public final boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }

    /**
     * POSITION_UNCHANGED:永远都在原来的position(notify时不会{@link #destroyItem},也不会{@link #instantiateItem})
     * POSITION_NONE:没有位置(notify时会重新调两个方法来添加新视图)
     */
    @Override
    public int getItemPosition(@NonNull Object object) {
        return POSITION_NONE;
    }

    /**
     * 不可继承,请使用{@link #getItemCount}
     */
    @Override
    public final int getCount() {
        return getItemCount();
    }

    /**
     * 不可继承,请使用{@link #onBindViewHolder}
     */
    public final void bindViewHolder(VH holder, int position) {
        //设置点击事件,不判断会顶掉lv的itemclick事件
        if (mListener != null) {
            holder.itemView.setTag(R.id.tag_view_click, position);
            holder.itemView.setOnClickListener(mListener);
            holder.itemView.setOnLongClickListener(mListener);
        }
        onBindViewHolder(holder, position);
    }

    ///////////////////////////////////////////////////////////////////////////
    // 以下是可能用到的父类方法
    ///////////////////////////////////////////////////////////////////////////

    /**
     * lv用到,getItemViewType的个数(巨坑,没啥用,必须大于0大于getItemViewType的最大值,并且不能太大,见{@link android.widget.AbsListView}的RecycleBin)
     */
    @IntRange(from = 1, to = 50)
    @Override
    public int getViewTypeCount() {
        return 1;
    }

    @Override
    public int getItemViewType(int position) {
        return 0;
    }

    ///////////////////////////////////////////////////////////////////////////
    // 以下是增加的方法
    ///////////////////////////////////////////////////////////////////////////

    protected abstract void onBindViewHolder(VH holder, int position);

    protected abstract VH onCreateViewHolder(ViewGroup parent, int itemViewType, @NonNull LayoutInflater inflater);

    /**
     * 这里的点击事件不会因有checkbox而被抢焦点
     * 里面也有LongClick
     * 监听事件一般使用实现类{@link OnItemClickListener}
     */
    public void setOnItemClickListener(IItemClick listener) {
        mListener = listener;
        notifyDataSetChanged();
    }
}