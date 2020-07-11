package com.ez.adapters.interfaceabstract;

/**
 * 所有adapter的接口
 */
public interface IAdapter {

    int getItemCount();

    int getItemViewType(int position);

    void setOnItemClickListener(IItemClick listener);
}
