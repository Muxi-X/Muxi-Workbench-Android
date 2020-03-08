package com.muxi.workbench.ui.project.view;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;
import com.muxi.workbench.R;

import java.util.ArrayList;
import java.util.List;

public class ProjectDetailActivity extends AppCompatActivity {


    private static final String PID="pid";
    private int pid;
    private ProjectDetailFragment mFragment;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_project_folder);
        pid=getIntent().getIntExtra(PID,0);

        initView();



    }

    public int getPid(){
        return pid;
    }
    private void initView(){
        mFragment=new ProjectDetailFragment();
       getSupportFragmentManager().beginTransaction()
               .add(R.id.project_detail_container,mFragment)
               .commit();
    }


    public static void  startActivity(Context context,int pid){
        Intent intent=new Intent(context,ProjectDetailActivity.class);
        intent.putExtra(PID,pid);
        context.startActivity(intent);


    }




}
