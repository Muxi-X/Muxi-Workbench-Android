package com.muxi.workbench.ui.project.presenter;

import android.Manifest;
import android.content.pm.PackageManager;

import androidx.core.app.ActivityCompat;

import com.muxi.workbench.ui.project.ProjectMainContract;
import com.muxi.workbench.ui.project.model.Project;
import com.muxi.workbench.ui.project.model.Repository.ProjectDataSource;
import com.muxi.workbench.ui.project.ProjectMainContract.Presenter;
import com.muxi.workbench.ui.project.ProjectMainContract.View;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

import io.reactivex.Observer;
import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.internal.disposables.ListCompositeDisposable;

public class ProjectPresenter implements Presenter {


    private View mView;
    private ProjectDataSource mDataSource;
    private ListCompositeDisposable mDisposableList;
    private AtomicBoolean hasStarted = new AtomicBoolean(false);



    public ProjectPresenter(View view){
        mView=view;
        mDataSource=new ProjectDataSource();
        mDisposableList=new ListCompositeDisposable();
    }


    @Override
    public void onCreate() {
        if (!hasStarted.compareAndSet(false, true)) {
            return;
        }
        getProject();
    }


    public void getProjectByPage(int page){
        mDataSource.getProjectFromRemote(1)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleObserver<Project>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        mDisposableList.add(d);
                        mView.showLoading();
                    }

                    @Override
                    public void onSuccess(Project project) {
                        if (project.getList()==null||project.getList().isEmpty())
                            return;
                        mView.addProject(project);
                    }

                    @Override
                    public  void onError(Throwable e) {
                        e.printStackTrace();
                    }
                });
    }

    public void getProject(){
        mDataSource.getProjectFromRemote(1)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleObserver<Project>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        mDisposableList.add(d);
                        mView.showLoading();
                    }

                    @Override
                    public void onSuccess(Project project) {
                        if (project.getList()==null||project.getList().isEmpty())
                            mView.showEmpty();
                        mView.showProject(project);
                    }

                    @Override
                    public  void onError(Throwable e) {
                        e.printStackTrace();
                        mView.showError();
                    }
                });
    }
    @Override
    public void update() {
        getProject();
    }

    @Override
    public void destory() {
        if (mDisposableList!=null)
            mDisposableList.dispose();
        mView=null;
    }


}
