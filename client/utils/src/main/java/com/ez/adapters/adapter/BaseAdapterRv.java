package com.ez.adapters.adapter;

import android.app.Activity;

import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ez.adapters.R;
import com.ez.adapters.base.BaseViewHolder;
import com.ez.adapters.interfaceabstract.IAdapter;
import com.ez.adapters.interfaceabstract.IAdapterList;
import com.ez.adapters.interfaceabstract.IItemClick;
import com.ez.adapters.interfaceabstract.OnItemClickListener;

import java.util.List;

/**
 * 适用于rv、我自定义的{@link BaseSuperAdapter}
 * 增加点击事件
 */
public abstract class BaseAdapterRv<VH extends BaseViewHolder> extends RecyclerView.Adapter<VH> implements BaseSuperAdapter.ISuperAdapter<VH>, IAdapter {
    public final String TAG = getClass().getSimpleName();

    protected final Activity mActivity;
    /**
     * 如果{@link #mActivity}是null则也是null
     */
    protected final LayoutInflater mInflater;
    protected IItemClick mListener;

    /**
     * @param activity 是不是null用的时候自己知道，如果是null则{@link #mInflater}也为null
     */
    public BaseAdapterRv(Activity activity) {
        mActivity = activity;
        mInflater = mActivity == null ? null : LayoutInflater.from(mActivity);
    }

    @Override
    public final VH onCreateViewHolder(ViewGroup parent, int viewType) {
        VH holder = onCreateViewHolder(parent, viewType, mInflater == null ? LayoutInflater.from(parent.getContext()) : mInflater);
        //保存holder，如果position无法解决问题，可以使用这个
        holder.itemView.setTag(R.id.tag_view_holder, holder);
        return holder;
    }

    @Override
    public final void onBindViewHolder(VH holder, int position) {
        //保存position
        holder.itemView.setTag(R.id.tag_view_click, position);
        //创建点击事件
        holder.itemView.setOnClickListener(mListener);
        holder.itemView.setOnLongClickListener(mListener);
        onBindVH(holder, position);
    }

    ///////////////////////////////////////////////////////////////////////////
    // 以下是可能用到的父类方法
    ///////////////////////////////////////////////////////////////////////////

    /**
     * 自定义的{@link BaseSuperAdapter}的多条目用到,获取当前item所占条目数
     */
    @Override
    public int getSpanSize(int position) {
        return 1;
    }

//    @Override
//    public int getItemViewType(int position) {
//        return 0;
//    }

    ///////////////////////////////////////////////////////////////////////////
    // 以下是增加的方法
    ///////////////////////////////////////////////////////////////////////////

    /**
     * 给view设置点击事件到{@link #mListener}中
     */
    protected final void setViewClick(View view, int position) {
        view.setTag(R.id.tag_view_click, position);
        if (!(view instanceof RecyclerView)) view.setOnClickListener(mListener);
    }

    /**
     * 给rv设置tag和数据
     * rvAdapter的点击事件请在bind里面设置,这样效率高
     * adapter.setOnItemClickListener(mListener);
     */
    protected final void setViewClick(RecyclerView rv, int position, List<?> adapterList) {
        rv.setTag(R.id.tag_view_click, position);
        //noinspection unchecked 忽略未检查错误,如果出异常说明你传的list和你的adapter对不上
        ((IAdapterList) rv.getAdapter()).setListAndNotifyDataSetChanged(adapterList);
    }

    protected abstract void onBindVH(VH holder, int position);

    /**
     * 注意!涉及到notifyItemInserted刷新时立即获取position可能会不正确
     * 里面也有LongClick
     * 监听事件一般使用实现类{@link OnItemClickListener}
     */
    public void setOnItemClickListener(IItemClick listener) {
        mListener = listener;
        notifyDataSetChanged();
    }
}