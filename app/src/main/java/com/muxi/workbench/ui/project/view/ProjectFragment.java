package com.muxi.workbench.ui.project.view;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.muxi.workbench.R;
import com.muxi.workbench.ui.project.ProjectMainContract;

import com.muxi.workbench.ui.project.model.bean.Project;
import com.muxi.workbench.ui.project.presenter.ProjectPresenter;
import com.muxi.workbench.ui.project.view.projectFolder.ProjectDetailActivity;

import java.util.ArrayList;
import java.util.List;

public class ProjectFragment extends Fragment implements ProjectMainContract.View {



    private ProjectMainContract.Presenter mPresenter;
    private RecyclerView mRecycleview;
    private ProjectListAdapter mAdapter;

    //权限检查
    private List<String> showRequests=new ArrayList<>();
    private String[]permissions=new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.READ_EXTERNAL_STORAGE};
    private boolean isPermissionAllowed=false;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPresenter=new ProjectPresenter(this);
        mAdapter=new ProjectListAdapter();


        mAdapter.setItemClickListener(new ProjectListAdapter.OnItemClickListener<Project.ListBean>() {
            @Override
            public void onclick(Project.ListBean listBean, int position) {
                if (ProjectFragment.this.getActivity()!=null)
                    ProjectDetailActivity.startActivity(ProjectFragment.this.getActivity(),listBean.getProjectID());
            }
        });
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View fragmentView=inflater.inflate(R.layout.fragment_project,container,false);
        mRecycleview=fragmentView.findViewById(R.id.project_recycle);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        mRecycleview.setLayoutManager(layoutManager);
        mRecycleview.setAdapter(mAdapter);

        mPresenter.onCreate();


        return fragmentView;
    }



    public boolean isPermissionAllow(){
        boolean isAllowed=true;
        showRequests.clear();
        for (int i = 0; i <permissions.length ; i++) {
            if (getActivity()==null)
                return false;
            if (ActivityCompat.checkSelfPermission(getActivity(),permissions[i])!= PackageManager.PERMISSION_GRANTED){
                isAllowed=false;
                showRequests.add(permissions[i]);
            }
        }
        return isAllowed;
    }

    @Override
    public void setPresenter(ProjectMainContract.Presenter presenter) {
        mPresenter=presenter;
    }

    @Override
    public void showLoading() {

    }

    @Override
    public void showProject(Project project) {
        mAdapter.setProject(project);
        mAdapter.notifyDataSetChanged();

    }


    @Override
    public void onRequestPermissionsResult(int requestCode,  String[] permissions,  int[] grantResults) {
        if (requestCode == 1) {
            for (int i = 0; i <permissions.length ; i++) {
                Log.i("Main", "onRequestPermissionsResult: "+permissions[i]+"   --->"+grantResults[i]);
                if (grantResults[i]==PackageManager.PERMISSION_DENIED){
                    Toast.makeText(getActivity(),"请允许获取权限以确保程序正常进行",Toast.LENGTH_LONG).show();
                    return;
                }
            }
            isPermissionAllowed=true;

        }

    }
    @Override
    public void showError() {

    }

    @Override
    public void showEmpty() {

    }

    @Override
    public void addProject(Project project) {
        mAdapter.addProject(project);
    }

}
