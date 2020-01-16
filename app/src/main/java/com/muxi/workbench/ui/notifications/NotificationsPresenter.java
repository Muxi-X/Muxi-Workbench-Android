package com.muxi.workbench.ui.notifications;

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
    }

    @Override
    public void refresh() {

    }

    @Override
    public void loadMore() {

    }

    @Override
    public void clearRedNode() {

    }

    @Override
    public void read(int sourceId) {
        mView.goToDetail(sourceId);
    }
}
