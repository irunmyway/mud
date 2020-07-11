package com.ez.adapters.adapter;

import android.app.Activity;

import androidx.annotation.IntDef;
import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ez.adapters.R;
import com.ez.adapters.base.BaseViewHolder;
import com.ez.adapters.interfaceabstract.IAdapterList;
import com.ez.adapters.interfaceabstract.IItemClick;
import com.ez.adapters.interfaceabstract.OnItemClickListener;
import com.ez.adapters.utils.Utils;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * listAdapter的基类
 * 可以加header、footer,如果是Grid需要自行处理setSpanSizeLookup头尾的跨度
 * {@link #notifyItemChanged}相关方法时注意有header时需要-1
 * bug:{@link #notifyItemChanged}方法不能刷新header、footer（header、footer不需要刷新，仅仅是先记着）
 */
public abstract class BaseAdapterRvList<VH extends BaseViewHolder, BEAN> extends BaseAdapterRv<BaseViewHolder> implements IAdapterList<BEAN> {

    protected List<BEAN> mList;

    public View mHeaderView, mFooterView;

    public static final int POSITION_HEADER = -128, POSITION_FOOTER = -127;//-128~127的integer有优化

    public static final int TYPE_HEADER = 0, TYPE_BODY = 1, TYPE_FOOTER = 2;

    @IntDef({TYPE_HEADER, TYPE_BODY, TYPE_FOOTER})
    @Retention(RetentionPolicy.SOURCE)
    public @interface AdapterListType {
    }//该变量只能传入上面几种,否则会报错

    public BaseAdapterRvList(Activity activity) {
        this(activity, null, null, null);
    }

    public BaseAdapterRvList(Activity activity, @Nullable List<BEAN> list) {
        this(activity, list, null, null);
    }

    /**
     * @param activity 是不是null用的时候自己知道，如果是null则{@link #mInflater}也为null
     * @param list     是不是null用的时候自己知道
     */
    public BaseAdapterRvList(Activity activity, List<BEAN> list, @Nullable View headerView, @Nullable View footerView) {
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
    protected final void onBindVH(BaseViewHolder holder, int position) {
        switch (getItemViewType(position)) {
            case TYPE_HEADER:
                holder.itemView.setTag(R.id.tag_view_click, POSITION_HEADER);
                break;
            case TYPE_FOOTER:
                holder.itemView.setTag(R.id.tag_view_click, POSITION_FOOTER);
                break;
            case TYPE_BODY:
                if (mHeaderView != null) {
                    position--;
                }
                holder.itemView.setTag(R.id.tag_view_click, position);
                onBindVH((VH) holder, position, mList.get(position));
                break;
            default:
                throw new RuntimeException("仅支持header、footer和body,想拓展请使用BaseAdapterRv");
        }
    }

    @NonNull
    @Override
    public final BaseViewHolder onCreateViewHolder(ViewGroup parent, @BaseAdapterRvList.AdapterListType int viewType,
                                                   LayoutInflater inflater) {
        switch (viewType) {
            case TYPE_HEADER:
                return new BaseViewHolder(mHeaderView);
            case TYPE_FOOTER:
                return new BaseViewHolder(mFooterView);
            case TYPE_BODY:
                return onCreateVH(parent, inflater);
            default:
                throw new RuntimeException("仅支持header、footer和body,想拓展请使用BaseAdapterRv");
        }
    }

    @AdapterListType
    @Override
    public final int getItemViewType(int position) {
        if (mHeaderView != null && position == 0) {
            return TYPE_HEADER;
        }
        if (mFooterView != null && getItemCount() == position + 1) {
            return TYPE_FOOTER;
        }
        return TYPE_BODY;
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
        //没有params添加一个默认的
        if (view != null && view.getLayoutParams() == null)
            view.setLayoutParams(new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));

        notifyDataSetChanged();
    }

    /**
     * add null表示删除
     */
    public void addFooterView(View view) {
        mFooterView = view;
        //没有params添加一个默认的
        if (view != null && view.getLayoutParams() == null)
            view.setLayoutParams(new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));

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

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // 追加一个懒汉写法
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    /**
     * 懒得不能再懒了的写法
     * adapter里的Activity为null
     *
     * @param layouId  creat时的资源id
     * @param listener 当bind时回调
     * @param <BEAN>   list数据的bean
     */
    public static <BEAN> BaseAdapterRvList<BaseViewHolder, BEAN> createAdapter
    (@LayoutRes final int layouId, final OnAdapterBindListener<BEAN> listener) {
        return createAdapter(null, null, layouId, listener);
    }

    public static <BEAN> BaseAdapterRvList<BaseViewHolder, BEAN> createAdapter
            (@Nullable Activity activity, @Nullable List<BEAN> list, @LayoutRes final int layouId, final OnAdapterBindListener<BEAN> listener) {
        return new BaseAdapterRvList<BaseViewHolder, BEAN>(activity, list) {
            @Override
            protected void onBindVH(BaseViewHolder holder, int listPosition, BEAN bean) {
                listener.onBindVH(holder, listPosition, bean);
            }

            @NonNull
            @Override
            protected BaseViewHolder onCreateVH(ViewGroup parent, LayoutInflater inflater) {
                return new BaseViewHolder(parent, layouId, inflater);
            }
        };
    }

    public interface OnAdapterBindListener<BEAN> {
        void onBindVH(BaseViewHolder holder, int listPosition, BEAN bean);
    }
}
