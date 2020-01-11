package com.muxi.workbench.ui.home;

import com.muxi.workbench.ui.home.model.FeedBean;

public class FeedRepository {
    RemoteDataSource mRemoteDataSource = new RemoteDataSource();

    void getAllData(int page,LoadStatusBeanCallback loadStatusBeanCallback) {
        mRemoteDataSource.getAllFeedFromRemote(loadStatusBeanCallback,page);
    }

    interface LoadStatusBeanCallback {

        void onDataLoaded(FeedBean mBean);

        void onDataNotAvailable();

        void onComplete();
    }
}
