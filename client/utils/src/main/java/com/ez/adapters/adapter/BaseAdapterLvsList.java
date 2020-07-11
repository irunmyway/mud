package com.ez.adapters.adapter;

import android.app.Activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ez.adapters.R;
import com.ez.adapters.base.BaseViewHolder;
import com.ez.adapters.interfaceabstract.IAdapterList;
import com.ez.adapters.interfaceabstract.IItemClick;
import com.ez.adapters.interfaceabstract.OnItemClickListener;
import com.ez.adapters.utils.Utils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * 和{@link BaseAdapterRvList}基本一致，适用于listview、gridview、viewpager
 */
public abstract class BaseAdapterLvsList<VH extends BaseViewHolder, BEAN> extends BaseAdapterLvs<BaseViewHolder> implements IAdapterList<BEAN> {

    protected List<BEAN> mList;

    public View mHeaderView, mFooterView;

    public BaseAdapterLvsList(Activity activity) {
        this(activity, null, null, null);
    }

    public BaseAdapterLvsList(Activity activity, @Nullable List<BEAN> list) {
        this(activity, list, null, null);
    }

    /**
     * @param activity 是不是null用的时候自己知道，如果是null则{@link #mInflater}也为null
     * @param list     是不是null用的时候自己知道
     */
    public BaseAdapterLvsList(Activity activity, List<BEAN> list, @Nullable View headerView, @Nullable View footerView) {
        super(activity);
        mList = list;
        mHeaderView = headerView;
        mFooterView = footerView;
    }

    @Override
    public final int getItemCount() {
        int count = 0;
        if (mHeaderView != null) {
            count++;
        }
        if (mFooterView != null) {
            count++;
        }
        if (mList != null) {
            count += mList.size();
        }
        return count;
    }

    @Override
    public final void onBindViewHolder(BaseViewHolder holder, int position) {
        switch (getItemViewType(position)) {
            case BaseAdapterRvList.TYPE_HEADER:
                holder.itemView.setTag(R.id.tag_view_click, BaseAdapterRvList.POSITION_HEADER);
                break;
            case BaseAdapterRvList.TYPE_FOOTER:
                holder.itemView.setTag(R.id.tag_view_click, BaseAdapterRvList.POSITION_FOOTER);
                break;
            case BaseAdapterRvList.TYPE_BODY:
                if (mHeaderView != null) {
                    position--;
                }
                holder.itemView.setTag(R.id.tag_view_click, position);
                onBindVH((VH) holder, position, mList.get(position));
                break;
            default:
                throw new RuntimeException("仅支持header、footer和body,想拓展请使用BaseAdapterLvs");
        }
    }

    @NonNull
    @Override
    public final BaseViewHolder onCreateViewHolder(ViewGroup parent, @BaseAdapterRvList.AdapterListType int viewType,
                                                   LayoutInflater inflater) {
        switch (viewType) {
            case BaseAdapterRvList.TYPE_HEADER:
                return new BaseViewHolder(mHeaderView);
            case BaseAdapterRvList.TYPE_FOOTER:
                return new BaseViewHolder(mFooterView);
            case BaseAdapterRvList.TYPE_BODY:
                return onCreateVH(parent, inflater);
            default:
                throw new RuntimeException("仅支持header、footer和body,想拓展请使用BaseAdapterLvs");
        }
    }

    @BaseAdapterRvList.AdapterListType
    @Override
    public final int getItemViewType(int position) {
        if (mHeaderView != null && position == 0) {
            return BaseAdapterRvList.TYPE_HEADER;
        }
        if (mFooterView != null && getItemCount() == position + 1) {
            return BaseAdapterRvList.TYPE_FOOTER;
        }
        return BaseAdapterRvList.TYPE_BODY;
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // list相关的方法，其他方法请使用getList进行操作
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    /**
     * @return 注意list是否传了null或者根本没传
     */
    @Override
    public List<BEAN> getList() {
        return mList;
    }

    /**
     * 获取指定bean
     */
    @NonNull
    public BEAN get(int listPosition) {
        if (mList != null && listPosition < mList.size()) {
            return mList.get(listPosition);
        }
        throw new RuntimeException("lit为空或指针越界");
    }

    /**
     * 清空list,不刷新adapter
     */
    public void clear() {
        if (mList != null) mList.clear();
    }

    /**
     * 添加全部条目,不刷新adapter
     */
    public void addAll(@NonNull Collection<? extends BEAN> addList) {
        if (mList == null) {
            mList = new ArrayList<>();
        }
        mList.addAll(addList);
    }

    @Override
    public int size() {
        return mList == null ? 0 : mList.size();
    }

    @Override
    public boolean isEmptyArray() {
        return Utils.isEmptyArray(mList);
    }

    ///////////////////////////////////////////////////////////////////////////
    // 以下是增加的方法
    ///////////////////////////////////////////////////////////////////////////

    /**
     * @param listPosition 已经做过处理,就是list的position
     */
    protected abstract void onBindVH(VH holder, int listPosition, BEAN bean);

    @NonNull
    protected abstract VH onCreateVH(ViewGroup parent, LayoutInflater inflater);

    public void setListAndNotifyDataSetChanged(List<BEAN> list) {
        mList = list;
        notifyDataSetChanged();
    }

    /**
     * add null表示删除
     */
    public void addHeaderView(View view) {
        mHeaderView = view;
        notifyDataSetChanged();
    }

    /**
     * add null表示删除
     */
    public void addFooterView(View view) {
        mFooterView = view;
        notifyDataSetChanged();
    }

    /**
     * 新的监听
     */
    public void setOnItemClickListener(OnItemClickListener listener) {
        super.setOnItemClickListener(listener);
    }

    /**
     * 不太建议使用这个，自定义的时候才会用到
     */
    @Deprecated
    @Override
    public void setOnItemClickListener(IItemClick listener) {
        super.setOnItemClickListener(listener);
    }
}
