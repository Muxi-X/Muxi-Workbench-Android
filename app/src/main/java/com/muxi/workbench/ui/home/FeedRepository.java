package com.muxi.workbench.ui.home;

import com.muxi.workbench.ui.home.model.FeedBean;

public class FeedRepository {
    RemoteDataSource mRemoteDataSource = new RemoteDataSource();

    void getAllData(LoadStatusBeanCallback loadStatusBeanCallback) {
        mRemoteDataSource.getAllFeedFromRemote(loadStatusBeanCallback);
    }

    interface LoadStatusBeanCallback {

        void onDataLoaded(FeedBean mBean);

        void onDataNotAvailable();
    }
}
