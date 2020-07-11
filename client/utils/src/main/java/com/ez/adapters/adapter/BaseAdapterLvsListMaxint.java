package com.ez.adapters.adapter;

import android.app.Activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.ez.adapters.base.BaseViewHolder;
import com.ez.adapters.interfaceabstract.IAdapterList;
import com.ez.adapters.utils.Utils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * 无限滑动的adapter（主要适用于vp）
 */
public abstract class BaseAdapterLvsListMaxint<VH extends BaseViewHolder, BEAN> extends BaseAdapterLvs<VH> implements IAdapterList<BEAN> {

    public List<BEAN> mList;

    public BaseAdapterLvsListMaxint(@NonNull Activity activity, @Nullable List<BEAN> list) {
        super(activity);
        mList = list;
    }

    @Override
    public final int getItemCount() {
        return (Utils.isEmptyArray(mList)) ? 0 : Integer.MAX_VALUE;
    }

    @Override
    public final void onBindViewHolder(VH holder, int position) {
        //对position进行了%处理
        position = position % mList.size();
        onBindVH(holder, position, mList.get(position));
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
        if (mList != null) {
            return mList.get(listPosition % mList.size());
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

    protected abstract void onBindVH(VH holder, int position, BEAN bean);

    public void setListAndNotifyDataSetChanged(List<BEAN> list) {
        mList = list;
        notifyDataSetChanged();
    }
}
