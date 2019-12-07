package com.muxi.workbench.ui.home;

import com.muxi.workbench.ui.home.model.FeedBean;

public interface HomeContract {
    interface View {
        void setPresenter(Presenter presenter);

        void addItem(FeedBean.DataListBean itemData);

        void showAllData(FeedBean feedBean);

        void setEmpty();

        void setLoadingIndicator(boolean loadingIndicator, boolean isSucceed);

        void showPerson();

        void showFile();

        void refreshData();
    }

    interface Presenter {
        void start();

        void loadAllData();

        void addItem(FeedBean.DataListBean itemData);

        void refresh();
    }
}
