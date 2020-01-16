package com.muxi.workbench.ui.notifications;

public interface NotificationContact {
    interface View {
        void setPresenter(Presenter presenter);

        void initAdapter(NotificationsResponse notificationsResponse);

        void showAllData(NotificationsResponse notificationsResponse);

        void setEmpty();

        void allRead();

        void showMore();

        void refresh();

        void showError();

        void goToDetail(int sourceId);
    }

    interface Presenter {
        void start();

        void loadAllData(boolean isRefresh);

        void refresh();

        void loadMore();

        void clearRedNode();

        void read(int sourceId);
    }


}
