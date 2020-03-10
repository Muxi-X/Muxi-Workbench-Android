package com.muxi.workbench.ui.project.view.projectFolder;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.muxi.workbench.R;

public class ProjectDetailActivity extends AppCompatActivity {


    private static final String PID = "pid";
    private int pid;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        pid = getIntent().getIntExtra(PID, 0);
        setContentView(R.layout.activity_project_folder);

        initView();


    }

    public int getPid() {
        return pid;
    }

    private void initView() {

    }


    public static void startActivity(Context context, int pid) {
        Intent intent = new Intent(context, ProjectDetailActivity.class);
        intent.putExtra(PID, pid);
        context.startActivity(intent);
    }
}
