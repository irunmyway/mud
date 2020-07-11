package com.ez.adapters.adapter;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * vp的adapter的fragment的终极封装
 * FragmentPagerAdapter>当destroyItem时并不会销毁frag,只是解绑视图了,所以刷新frag集合的方式不能删除frag
 * 如果想删除{@link BaseAdapterVpStateFrag}
 */
public final class BaseAdapterVpFrag extends FragmentPagerAdapter {
    public final String TAG = getClass().getSimpleName();

    private final ArrayList<Fragment> mFragments = new ArrayList<>();//添加的Fragment的集合

    private List<? extends CharSequence> mTitles;//tabLayout总是取title

    public BaseAdapterVpFrag(FragmentManager fm, Fragment... frags) {
        super(fm);
        addFragment(frags);
    }

    public BaseAdapterVpFrag(FragmentManager fm, List<? extends Fragment> frags) {
        super(fm);
        addFragment(frags);
    }

    public BaseAdapterVpFrag addFragment(Fragment... frags) {
        Collections.addAll(mFragments, frags);
        notifyDataSetChanged();
        return this;
    }

    public BaseAdapterVpFrag addFragment(List<? extends Fragment> frags) {
        mFragments.addAll(frags);
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
     * 添加frag的title，类似TabLayout可能会用到
     */
    public BaseAdapterVpFrag setTitles(ArrayList<? extends CharSequence> titles) {
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
}
