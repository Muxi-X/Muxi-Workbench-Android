package com.muxi.workbench.ui.home;

import com.muxi.workbench.ui.home.model.FeedBean;

public interface HomeContract {
    interface View {
        void setPresenter(Presenter presenter);

        void initAdapter(FeedBean feedBean);

        void addItem(FeedBean nextPage);

        void showAllData(FeedBean feedBean);

        void setEmpty();

        void setLoadingIndicator(boolean loadingIndicator, boolean isSucceed);

        void refreshData();

        void showLoadMoreSign(boolean isSuccess);
    }

    interface Presenter {
        void start();

        void loadAllData(boolean isRefresh);

        void addItem(FeedBean itemData);

        void refresh();

        void loadMore();
    }
}
