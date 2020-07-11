package com.ez.adapters.adapter;

import android.os.Parcelable;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.PagerAdapter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * vp的adapter的fragment的终极封装
 * FragmentStatePagerAdapter>当destroyItem时直接销毁frag
 */
public final class BaseAdapterVpStateFrag extends FragmentStatePagerAdapter {
    public final String TAG = getClass().getSimpleName();

    private final ArrayList<Fragment> mFragments = new ArrayList<>();//添加的Fragment的集合

    private List<? extends CharSequence> mTitles;//tabLayout总是取title

    private boolean mIsSaveState = true;

    public BaseAdapterVpStateFrag(FragmentManager fm, Fragment... frags) {
        super(fm);
        addFragment(frags);
    }

    public BaseAdapterVpStateFrag(FragmentManager fm, List<? extends Fragment> frags) {
        super(fm);
        addFragment(frags);
    }

    public BaseAdapterVpStateFrag addFragment(Fragment... frags) {
        Collections.addAll(mFragments, frags);
        return this;
    }

    public BaseAdapterVpStateFrag addFragment(List<? extends Fragment> frags) {
        mFragments.addAll(frags);
        notifyDataSetChanged();
        return this;
    }

    public BaseAdapterVpStateFrag removeFragnemt(Fragment frag) {
        return removeFragnemt(mFragments.indexOf(frag));
    }

    public BaseAdapterVpStateFrag removeFragnemt(int position) {
        if (position > -1 && position < mFragments.size()) {
            mFragments.remove(position);
            notifyDataSetChanged();
        }
        return this;
    }

    public BaseAdapterVpStateFrag removeAllFragnemt() {
        mFragments.clear();
        notifyDataSetChanged();
        return this;
    }

    @Override
    public Fragment getItem(int position) {
        //得到对应position的Fragment
        return mFragments.get(position);
    }

    @Override
    public int getCount() {
        //返回Fragment的数量
        return mFragments.size();
    }

    /**
     * 注意:修改frag数据需要立即刷新adapter
     */
    public ArrayList<Fragment> getFragments() {
        return mFragments;
    }

    @Override
    public int getItemPosition(@NonNull Object object) {
        return PagerAdapter.POSITION_NONE;
    }

    /**
     * 添加frag的title，类似TabLayout可能会用到
     */
    public BaseAdapterVpStateFrag setTitles(ArrayList<? extends CharSequence> titles) {
        mTitles = titles;
        notifyDataSetChanged();
        return this;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        if (mTitles != null && position >= 0 && position < mTitles.size())
            return mTitles.get(position);

        return null;
    }

    /**
     * 不缓存所有frag
     * 简单的讲就是里面的frag可以使用构造传参，不存在重建构造错误的问题
     * 解决的问题：frag需要bundle不能传的东西（如：activity、frag）
     */
    public BaseAdapterVpStateFrag noSaveState() {
        mIsSaveState = false;
        return this;
    }

    //直接不保存，这样就不会有缓存了
    @Override
    public Parcelable saveState() {
        return mIsSaveState ? super.saveState() : null;
    }
}
