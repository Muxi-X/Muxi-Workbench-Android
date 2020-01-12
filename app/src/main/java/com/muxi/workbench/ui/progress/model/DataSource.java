package com.muxi.workbench.ui.progress.model;

import androidx.annotation.NonNull;

import java.util.List;

public interface DataSource {

    interface LoadProgressListCallback {

        void onProgressListLoaded(List<Progress> progressList);

        void onDataNotAvailable();
    }

    interface LoadStickyProgressCallback {

        void onStickyProgressLoaded(List<Integer> StickyProgressList);

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

    interface CommentProgressCallback {

        void onSuccessfulComment();

        void onFail();
    }

    void getProgressList(int page, @NonNull LoadProgressListCallback callback);

    void commentProgress(int sid, String comment, CommentProgressCallback callback);

    void ifLikeProgress(int sid, boolean iflike, SetLikeProgressCallback callback);

    void refreshProgressList();

    void deleteProgress(int sid, @NonNull DeleteProgressCallback callback);

    void setStickyProgress(@NonNull int sid);

    void getAllStickyProgress(@NonNull LoadStickyProgressCallback callback);

    void deleteStickyProgress(@NonNull int sid);
}
