package com.muxi.workbench.ui.progress.model.progressDetail;

import androidx.annotation.NonNull;

public class ProgressDetailRepository implements ProgressDetailDataSource {

    private static ProgressDetailRepository INSTANCE = null;

    private final ProgressDetailDataSource mProgressDetailDataSource;

    private ProgressDetailRepository(@NonNull ProgressDetailDataSource progressDetailDataSource) {
        mProgressDetailDataSource = progressDetailDataSource;
    }

    public static ProgressDetailRepository getInstance(ProgressDetailDataSource progressDetailDataSource) {
        if (INSTANCE == null) {
            INSTANCE = new ProgressDetailRepository(progressDetailDataSource);
        }
        return INSTANCE;
    }

    public static void destroyInstance() {
        INSTANCE = null;
    }


    @Override
    public void getProgressDetail(int sid, LoadProgressCallback callback) {
        mProgressDetailDataSource.getProgressDetail(sid, callback);
    }

    @Override
    public void setLikeProgress(int sid, boolean iflike, SetLikeProgressCallback callback) {
        mProgressDetailDataSource.setLikeProgress(sid, iflike, callback);
    }

    @Override
    public void commentProgress(int sid, String comment, CommentProgressCallback callback) {
        mProgressDetailDataSource.commentProgress(sid, comment, callback);
    }

    @Override
    public void deleteProgressComment(int sid, int cid, DeleteCommentCallback callback) {
        mProgressDetailDataSource.deleteProgressComment(sid, cid, callback);
    }
}
