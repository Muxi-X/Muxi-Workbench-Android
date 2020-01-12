package com.muxi.workbench.ui.progress.model;

import android.util.Log;

import androidx.annotation.NonNull;


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
    public void commentProgress(int sid, String comment, CommentProgressCallback callback) {
        mProgressDataSource.commentProgress(sid, comment, callback);
    }

    @Override
    public void ifLikeProgress(int sid, boolean iflike, SetLikeProgressCallback callback) {
        mProgressDataSource.ifLikeProgress(sid, iflike, callback);
    }

    @Override
    public void refreshProgressList() {

    }

    @Override
    public void deleteProgress(@NonNull int sid, DeleteProgressCallback callback) {
        mProgressDataSource.deleteProgress(sid, callback);
    }

    @Override
    public void setStickyProgress(@NonNull int sid) {
        mProgressDataSource.setStickyProgress(sid);
    }

    @Override
    public void getAllStickyProgress(@NonNull LoadStickyProgressCallback callback) {
        mProgressDataSource.getAllStickyProgress(callback);
    }

    @Override
    public void deleteStickyProgress(@NonNull int sid) {
        mProgressDataSource.deleteStickyProgress(sid);
    }
}
