package com.muxi.workbench.ui.mainControl;

import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.muxi.workbench.R;
import com.muxi.workbench.ui.home.HomeFragment;
import com.muxi.workbench.ui.mine.MineFragment;
import com.muxi.workbench.ui.notifications.NotificationsFragment;
import com.muxi.workbench.ui.progress.view.progressLIst.ProgressFragment;
import com.muxi.workbench.ui.project.ProjectFragment;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {


    public static final String TAG="MainActivity";
    public ViewPager viewPager;
    public BottomNavigationView navigationView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();


    }

    public void initView(){
        viewPager=findViewById(R.id.viewpage);
        navigationView=findViewById(R.id.nav_view);
        viewPager.setAdapter(new MainViewPageAdapter(getSupportFragmentManager(),initFragment()) );
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
                switch (menuItem.getItemId()){
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
    }


    public List<Fragment> initFragment(){
        List<Fragment>list=new ArrayList<>();
        list.add(new HomeFragment());
        list.add(new ProgressFragment());
        list.add(new ProjectFragment());
        list.add(new NotificationsFragment());
        list.add(new MineFragment());
        return list;
    }





}
