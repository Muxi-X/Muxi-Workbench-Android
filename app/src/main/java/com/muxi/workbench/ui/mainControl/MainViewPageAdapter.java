package com.muxi.workbench.ui.mainControl;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import java.util.List;

public class MainViewPageAdapter extends FragmentPagerAdapter {



    private List<Fragment>list;
    public MainViewPageAdapter(FragmentManager fm, List<Fragment>list) {
        super(fm);
        this.list=list;
    }

    @Override
    public Fragment getItem(int position) {


        return list.get(position);
    }

    @Override
    public int getCount() {
        return list.size();
    }
}
