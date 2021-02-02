package com.muxi.workbench.ui.progress.model.progressList;

import androidx.annotation.NonNull;

import com.muxi.workbench.ui.progress.model.Progress;

import java.util.List;

public interface ProgressListDataSource {

    interface LoadProgressCallback {

        void onProgressLoaded(Progress progress);

        void onDataNotAvailable();
    }

    interface LoadProgressListCallback {

        void onProgressListLoaded(List<Progress> progressList);

        void onDataNotAvailable();
    }

    interface LoadStickyProgressCallback {

        void onStickyProgressLoaded(List<Progress> StickyProgressList);

        void onDataNotAvailable();

    }

    interface DeleteProgressCallback {

        void onSuccessfulDelete();

        void onFail();
    }


    interface SetLikeProgressCallback {

        void onSuccessfulSet();

        void onFail();
    }

    interface GetGroupUserListCallback {

        void onSuccessfulGet(List<Integer> UserList);

        void onFail();

    }

    void getProgressList( @NonNull LoadProgressListCallback callback);

    void ifLikeProgress(int sid, boolean iflike, SetLikeProgressCallback callback);

    void refreshProgressList();

    void deleteProgress(int sid, String title, @NonNull DeleteProgressCallback callback);

    void setStickyProgress(@NonNull Progress progress);

    void getAllStickyProgress(@NonNull LoadStickyProgressCallback callback);

    void deleteStickyProgress(@NonNull int sid);

    void getGroupUserList(int gid, GetGroupUserListCallback callback);

    void getProgress(int sid, String avatar, String username,LoadProgressCallback callback);
}
