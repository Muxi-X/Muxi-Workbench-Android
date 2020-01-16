package com.muxi.workbench.ui.home;

import com.muxi.workbench.ui.home.model.FeedBean;

public class FeedRepository {

    void getAllData(int page, LoadStatusBeanCallback loadStatusBeanCallback) {
        RemoteDataSource.getAllFeedFromRemote(loadStatusBeanCallback, page);
    }

    interface LoadStatusBeanCallback {

        void onDataLoaded(FeedBean mBean);

        void onDataNotAvailable();

        void onComplete();
    }
}
