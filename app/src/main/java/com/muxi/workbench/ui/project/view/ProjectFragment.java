package com.muxi.workbench.ui.project.view;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.muxi.workbench.R;
import com.muxi.workbench.ui.project.ProjectMainContract;
import com.muxi.workbench.ui.project.model.Project;
import com.muxi.workbench.ui.project.presenter.ProjectPresenter;

public class ProjectFragment extends Fragment implements ProjectMainContract.View {



    private ProjectMainContract.Presenter mPresenter;
    private RecyclerView mRecycleview;
    private ProjectListAdapter mAdapter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPresenter=new ProjectPresenter(this);
        mAdapter=new ProjectListAdapter();

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
