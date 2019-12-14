package com.muxi.workbench.ui.progress.model;

import androidx.annotation.NonNull;

import java.util.Map;

public class ProgressRepository implements DataSource {

    private static ProgressRepository INSTANCE = null;

    private final DataSource mProgressDataSource;

    private ProgressRepository(@NonNull DataSource progressDataSource) {
        mProgressDataSource = progressDataSource;
    }

    public static ProgressRepository getInstance(DataSource progressDataSource) {
        if (INSTANCE == null) {
            INSTANCE = new ProgressRepository(progressDataSource);
        }
        return INSTANCE;
    }

    public static void destroyInstance() {
        INSTANCE = null;
    }

    @Override
    public void getProgressList(int page, @NonNull LoadProgressListCallback callback) {
        mProgressDataSource.getProgressList(page,callback);
    }


    @Override
    public void commentProgress(int sid, String comment) {
        mProgressDataSource.commentProgress(sid, comment);
    }

    @Override
    public void ifLikeProgress(int sid, boolean iflike) {
        mProgressDataSource.ifLikeProgress(sid, iflike);
    }

    @Override
    public void refreshProgressList() {

    }

    @Override
    public void deleteProgress(@NonNull int sid) {
        mProgressDataSource.deleteProgress(sid);
    }

    @Override
    public void setStickyProgress(@NonNull int sid) {
        mProgressDataSource.setStickyProgress(sid);
    }

    @Override
    public boolean isStickyProgress(int sid) {
        return mProgressDataSource.isStickyProgress(sid);
    }
}
