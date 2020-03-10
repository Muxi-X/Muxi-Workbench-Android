package com.muxi.workbench.ui.notifications.model;

public class NotificationsRepository {


    public void getNotifications(NotificationsRepository.LoadNotiResponseCallback callback, int page) {
        RemoteDataSource.getNotificationFromRemote(callback, page);
    }

    public void clearAllNotifications(NotificationsRepository.LoadNotiResponseCallback callback) {
        RemoteDataSource.clearNotifications(callback);
    }

    public interface LoadNotiResponseCallback {

        void onDataLoaded(NotificationsResponse mBean);

        void onDataNotAvailable();

        void onComplete();
    }
}
