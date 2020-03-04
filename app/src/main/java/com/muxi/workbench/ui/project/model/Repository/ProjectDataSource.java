package com.muxi.workbench.ui.project.model.Repository;

import com.muxi.workbench.ui.project.model.bean.Project;

import io.reactivex.Single;

public class ProjectDataSource {


    private RemoteSource remote;
    public ProjectDataSource(){
        remote=new RemoteSource();
    }

    public Single<Project> getProjectFromRemote(int page){
        return remote.getProject(page);

    }




}
