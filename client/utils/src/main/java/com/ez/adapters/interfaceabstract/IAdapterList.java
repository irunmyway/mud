package com.ez.adapters.interfaceabstract;

import androidx.annotation.NonNull;

import java.util.Collection;
import java.util.List;

/**
 * 所有list的adapter的接口
 */
public interface IAdapterList<BEAN> extends IAdapter {

    void setListAndNotifyDataSetChanged(List<BEAN> list);

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // list相关的方法
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    List<BEAN> getList();

    /**
     * 获取指定bean
     */
    @NonNull
    BEAN get(int listPosition);

    /**
     * 清空list,不刷新adapter
     */
    void clear();

    /**
     * 添加全部条目,不刷新adapter
     */
    void addAll(@NonNull Collection<? extends BEAN> addList);

    int size();

    /**
     * null或空
     */
    boolean isEmptyArray();
}
