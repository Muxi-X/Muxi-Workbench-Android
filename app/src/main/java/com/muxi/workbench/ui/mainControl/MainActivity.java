package com.muxi.workbench.ui.mainControl;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.muxi.workbench.R;
import com.muxi.workbench.ui.home.view.HomeFragment;
import com.muxi.workbench.ui.mine.MineFragment;
import com.muxi.workbench.ui.notifications.NotificationsFragment;
import com.muxi.workbench.ui.progress.view.progressList.ProgressFragment;
import com.muxi.workbench.ui.project.view.ProjectFragment;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {


    public static final String TAG = "MainActivity";
    public ViewPager viewPager;
    public BottomNavigationView navigationView;
    public static final String CHANNEL_ID="WORKBENCH";
    public static final String CHANNEL_NAME="DownloadService";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            getWindow().getDecorView().
                    setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN |
                            View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        }
        addMainFragment();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            createNotificationChannel();
        }

    }

    public void addMainFragment(){
        getSupportFragmentManager().beginTransaction()
                .add(R.id.main_container,new MainFragment(),"mainFragment")
                .commit();
    }


    @RequiresApi(api = Build.VERSION_CODES.O)
    private void createNotificationChannel(){
        NotificationChannel channel=new NotificationChannel(CHANNEL_ID,CHANNEL_NAME, NotificationManager.IMPORTANCE_LOW);
        NotificationManager manager=(NotificationManager)getSystemService((Context.NOTIFICATION_SERVICE));
        manager.createNotificationChannel(channel);
    }

}
