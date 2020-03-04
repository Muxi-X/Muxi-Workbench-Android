package com.muxi.workbench.ui.project;

import com.muxi.workbench.ui.project.model.bean.Project;

public interface ProjectMainContract {


    interface Presenter{
        void onCreate();
        void update();
        void destory();

    }

    interface View{
        void setPresenter(Presenter presenter);
        void showLoading();
        void showProject(Project project);
        void showError();
        void showEmpty();
        void addProject(Project project);
    }
}
