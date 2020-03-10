package com.muxi.workbench.ui.notifications;

import com.muxi.workbench.ui.notifications.model.NotificationsRepository;
import com.muxi.workbench.ui.notifications.model.NotificationsResponse;

public class NotificationsPresenter implements NotificationContact.Presenter {

    private NotificationAdapter mAdapter;
    private NotificationContact.View mView;
    private NotificationsRepository mRepository;

    public NotificationsPresenter(NotificationsRepository repository, NotificationContact.View view) {
        mView = view;
        mRepository = repository;
    }

    @Override
    public void start() {
        loadAllData(false);
    }

    @Override
    public void loadAllData(boolean isRefresh) {

        mRepository.getNotifications(new NotificationsRepository.LoadNotiResponseCallback() {
            @Override
            public void onDataLoaded(NotificationsResponse mBean) {
                if (isRefresh) mView.showAllData(mBean);
                else mView.initAdapter(mBean);
            }

            @Override
            public void onDataNotAvailable() {
                mView.showError();
            }

            @Override
            public void onComplete() {

            }
        }, 1);

    }

    @Override
    public void refresh() {

    }

    @Override
    public void loadMore() {

    }

    @Override
    public void clearRedNode() {
        mAdapter.clearAll();
        mRepository.clearAllNotifications(new NotificationsRepository.LoadNotiResponseCallback() {
            @Override
            public void onDataLoaded(NotificationsResponse mBean) {

            }

            @Override
            public void onDataNotAvailable() {
                mView.showError();
            }

            @Override
            public void onComplete() {
                mView.allRead();
            }
        });
    }

    @Override
    public void read(int sourceId) {
        mView.goToDetail(sourceId);
    }
}
