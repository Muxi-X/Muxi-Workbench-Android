package com.muxi.workbench.ui.project;

import com.muxi.workbench.ui.project.model.Repository.DetailDataSource.FolderType;
import com.muxi.workbench.ui.project.model.bean.FolderTree;

import java.util.List;

public interface FolderContract {

    interface  View{
        void showEmpty();
        void showError();
        void showLoading();
        void showFolder(List<FolderTree.ChildBean>list);

    }

    interface Presenter{

        void getAllFolder( );
        void getNextFolder( int position);
        void getPreviousFolder( );
        void update();
        void destory();
    }
}

