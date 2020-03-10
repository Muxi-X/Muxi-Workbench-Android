package com.muxi.workbench.ui.mainControl;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.muxi.workbench.R;
import com.muxi.workbench.services.DownloadService;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {


    public static final String TAG = "MainActivity";
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
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            createNotificationChannel();
        }

        addMainFragment();
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
        if (manager != null) {
            manager.createNotificationChannel(channel);
        }
    }

    private  long lastBackTime=0;
    @Override
    public void onBackPressed() {
        if (System.currentTimeMillis()-lastBackTime<=2000){
            super.onBackPressed();
        }else {
            Toast.makeText(this,"再按一次退出",Toast.LENGTH_LONG).show();
            lastBackTime=System.currentTimeMillis();

        }
    }

    @Override
    protected void onDestroy() {
        stopService(new Intent(this, DownloadService.class));
        super.onDestroy();

    }
}
