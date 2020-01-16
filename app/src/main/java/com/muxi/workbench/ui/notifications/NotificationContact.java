package com.muxi.workbench.ui.notifications;

public interface NotificationContact {
    interface View {
        void setPresenter(Presenter presenter);

        void initAdapter(NotificationAdapter adapter);

        void showAllData(NotificationsBean notificationsBean);

        void setEmpty();

        void allRead();

        void showMore();

        void refresh();
    }

    interface Presenter {
        void start();

        void loadAllData(boolean isRefresh);

        void refresh();

        void loadMore();

        void clearRedNode();
    }


}
