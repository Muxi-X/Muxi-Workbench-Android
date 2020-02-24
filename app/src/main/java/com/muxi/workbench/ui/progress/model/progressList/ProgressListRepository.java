package com.muxi.workbench.ui.progress.model.progressList;

import androidx.annotation.NonNull;

import com.muxi.workbench.ui.progress.model.Progress;


public class ProgressListRepository implements ProgressListDataSource {

    private static ProgressListRepository INSTANCE = null;

    private final ProgressListDataSource mProgressListDataSource;

    private ProgressListRepository(@NonNull ProgressListDataSource progressDataSource) {
        mProgressListDataSource = progressDataSource;
    }

    public static ProgressListRepository getInstance(ProgressListDataSource progressDataSource) {
        if (INSTANCE == null) {
            INSTANCE = new ProgressListRepository(progressDataSource);
        }
        return INSTANCE;
    }

    @Override
    public void getProgressList(int page, @NonNull LoadProgressListCallback callback) {
        mProgressListDataSource.getProgressList(page,callback);
    }


    @Override
    public void ifLikeProgress(int sid, boolean iflike, SetLikeProgressCallback callback) {
        mProgressListDataSource.ifLikeProgress(sid, iflike, callback);
    }

    @Override
    public void refreshProgressList() {
    }

    @Override
    public void deleteProgress(@NonNull int sid, DeleteProgressCallback callback) {
        mProgressListDataSource.deleteProgress(sid, callback);
    }

    @Override
    public void setStickyProgress(@NonNull Progress progress) {
        mProgressListDataSource.setStickyProgress(progress);
    }

    @Override
    public void getAllStickyProgress(@NonNull LoadStickyProgressCallback callback) {
        mProgressListDataSource.getAllStickyProgress(callback);
    }

    @Override
    public void deleteStickyProgress(@NonNull int sid) {
        mProgressListDataSource.deleteStickyProgress(sid);
    }
    @Override
    public void getGroupUserList(int gid, GetGroupUserListCallback callback) {
        mProgressListDataSource.getGroupUserList(gid, callback);
    }

    @Override
    public void getProgress(int sid, String avatar, String username, int uid, LoadProgressCallback callback) {
        mProgressListDataSource.getProgress(sid, avatar, username, uid, callback);
    }
}
