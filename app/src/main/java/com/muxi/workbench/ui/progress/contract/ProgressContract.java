package com.muxi.workbench.ui.progress.contract;

import androidx.annotation.NonNull;

import com.muxi.workbench.ui.progress.ProgressFilterType;
import com.muxi.workbench.ui.progress.model.Progress;

import java.util.List;

public interface ProgressContract {

    interface View {

        void setPresenter (ProgressContract.Presenter presenter);

        void showProgressList(List<Progress> progressList);

        void showAddNewProgress();

        void showUserInfo(int uid);

        void refreshLikeProgress(int position, int iflike);

        void refreshProgress(int position, Progress progress);

        void showDeleteProgress(int position);

        void showMoreProgress(List<Progress> progresses);

        void showCommentView();

        void showError();

        void moveNewStickyProgress(int position);

        void moveDeleteStickyProgress(int position);

    }

    interface Presenter {

        void start();

        void loadProgressList(boolean ifForceUpdate);

        void addNewProgress();

        void openUserInfo(@NonNull int uid);

        void likeProgress(int sid, int position);

        void cancelLikeProgress(int sid, int position);

        void setProgressFilterType(ProgressFilterType requestType);

        int getFiltering();

        int getPage();

        void setProgressSticky(int position, Progress progress);

        void deleteProgress(int position, int sid);

        void cancelStickyProgress(int position, Progress progress);

        void loadProgress(int position, int sid, String avatar, String username, int uid);

    }

}
