package com.muxi.workbench.ui.mainControl;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.muxi.workbench.R;
import com.muxi.workbench.ui.home.view.HomeFragment;
import com.muxi.workbench.ui.mine.MineFragment;
import com.muxi.workbench.ui.notifications.NotificationsFragment;
import com.muxi.workbench.ui.progress.view.progressList.ProgressFragment;
import com.muxi.workbench.ui.project.view.ProjectFragment;

import java.util.ArrayList;
import java.util.List;

public class MainFragment extends Fragment {

    public ViewPager viewPager;
    public BottomNavigationView navigationView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View mainView=inflater.inflate(R.layout.fragment_main,container,false);
        viewPager = mainView.findViewById(R.id.viewpage);
        navigationView = mainView.findViewById(R.id.nav_view);
        viewPager.setAdapter(new MainViewPageAdapter(getChildFragmentManager(), initFragment()));
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                navigationView.getMenu().getItem(position).setChecked(true);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        navigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    case R.id.navigation_home:
                        viewPager.setCurrentItem(0);
                        break;
                    case R.id.navigation_progress:
                        viewPager.setCurrentItem(1);
                        break;
                    case R.id.navigation_project:
                        viewPager.setCurrentItem(2);
                        break;
                    case R.id.navigation_notifications:
                        viewPager.setCurrentItem(3);
                        break;
                    case R.id.navigation_mine:
                        viewPager.setCurrentItem(4);
                        break;
                }

                return false;
            }
        });

        return mainView;
    }


    public List<Fragment> initFragment() {
        List<Fragment> list = new ArrayList<>();
        list.add(new HomeFragment());
        list.add(new ProgressFragment());
        list.add(new ProjectFragment());
        list.add(new NotificationsFragment());
        list.add(new MineFragment());
        return list;
    }

}
