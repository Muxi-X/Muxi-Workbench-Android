package com.muxi.workbench.ui.project.view;

import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import java.util.List;

public class FolderViewPagerAdapter  extends FragmentStatePagerAdapter {



    private List<Fragment> mFragmentList;
    private List<String> mTitles;
    private ProjectFolderFragment mCurrentFragment;
    public FolderViewPagerAdapter(FragmentManager fm, List<Fragment> fragments, List<String> titles) {
        super(fm);
        mFragmentList = fragments;
        mTitles = titles;
    }

    @Override
    public void setPrimaryItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        super.setPrimaryItem(container, position, object);
        mCurrentFragment=(ProjectFolderFragment) object;
    }



    @Override
    public CharSequence getPageTitle(int position) {
        return mTitles.get(position);
    }

    @Override
    public Fragment getItem(int position) {
        return mFragmentList.get(position);
    }

    @Override
    public int getCount() {
        return mFragmentList.size();
    }

    public ProjectFolderFragment getmCurrentFragment() {
        return mCurrentFragment;
    }
}
