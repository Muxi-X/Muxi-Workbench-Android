package com.muxi.workbench.ui.home;


import android.util.Log;

import com.muxi.workbench.ui.home.model.FeedBean;

public class HomePresenter implements HomeContract.Presenter {

    private FeedRepository mFeedRepository;
    private HomeContract.View mHomeView;

    public HomePresenter(FeedRepository feedRepository, HomeContract.View homeView) {
        mFeedRepository = feedRepository;
        mHomeView = homeView;

        mHomeView.setPresenter(this);

    }

    @Override
    public void start() {
        loadAllData();
    }

    @Override
    public void loadAllData() {
        mFeedRepository.getAllData(new FeedRepository.LoadStatusBeanCallback() {
            @Override
            public void onDataLoaded(FeedBean mBean) {
                mHomeView.showAllData(mBean);
            }

            @Override
            public void onDataNotAvailable() {
                Log.e("TAG","HomePresenter onDataNotAvailable");
                mHomeView.setEmpty();
            }
        });
    }

    @Override
    public void addItem(FeedBean.DataListBean itemData) {

    }

    @Override
    public void refresh() {
        mFeedRepository.getAllData(new FeedRepository.LoadStatusBeanCallback() {
            @Override
            public void onDataLoaded(FeedBean mBean) {

                mHomeView.setLoadingIndicator(false, true);
            }

            @Override
            public void onDataNotAvailable() {

                mHomeView.setLoadingIndicator(false, true);

            }
        });
    }
}
