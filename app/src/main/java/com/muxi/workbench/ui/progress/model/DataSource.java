package com.muxi.workbench.ui.progress.model;

import androidx.annotation.NonNull;

import java.util.List;

public interface DataSource {

    interface LoadProgressListCallback {

        void onProgressListLoaded(List<Progress> progressList);

        void onDataNotAvailable();
    }

    void getProgressList(int page, @NonNull LoadProgressListCallback callback);

    void commentProgress(int sid, String comment);

    void ifLikeProgress(int sid, boolean iflike);

    void refreshProgressList();

    void deleteProgress(int sid);

    void setStickyProgress(@NonNull int sid);

    boolean isStickyProgress(int sid);

}
