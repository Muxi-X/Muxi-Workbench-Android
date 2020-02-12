package com.muxi.workbench.ui.home.model;

public class FeedRepository {

    public void getAllData(int page, LoadStatusBeanCallback loadStatusBeanCallback) {
        RemoteDataSource.getAllFeedFromRemote(loadStatusBeanCallback, page);
    }

    public interface LoadStatusBeanCallback {

        void onDataLoaded(FeedBean mBean);

        void onDataNotAvailable();

        void onComplete();
    }
}
