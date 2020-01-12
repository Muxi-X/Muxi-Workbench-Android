package com.muxi.workbench.ui.progress;

import androidx.annotation.NonNull;

import com.muxi.workbench.ui.progress.model.Progress;

import java.util.List;

public interface ProgressContract {

    interface View {

        void setPresenter (ProgressContract.Presenter presenter);

        void showProgressList(List<Progress> progressList);

        void showCommentView();

        void showProgressDetail(int sid);

        void showAddNewProgress();

        void showUserInfo(int uid);

        void showLikeProgress();

        void showNotLikedProgress();

        void showDeleteProgress();

        void renewCommentNum();

        void showSelectAllFilter();

        void showSelectProductFilter();

        void showSelectBackendFilter();

        void showSelectFrontendFilter();

        void showSelectAndroidFilter();

        void showSelectDesignFilter();

        void showMoreProgress(List<Progress> progresses);

        void showError();

        boolean isActive();

    }

    interface Presenter {

        void start();

        void loadProgressList(boolean ifForceUpdate);

        void addNewProgress();

        void openProgressDetails(@NonNull Progress progress);

        void openUserInfo(@NonNull int uid);

        void likeProgress(int sid);

        void cancelLikeProgress(int sid);

        void commentProgress(int sid, String comment);

        void setProgressFilterType(ProgressFilterType requestType);

        ProgressFilterType getFiltering();

        void setStickyProgress();

    }

}
