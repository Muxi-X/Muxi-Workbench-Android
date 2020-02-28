package com.muxi.workbench.ui.progress.model.progressDetail;

import com.muxi.workbench.ui.progress.model.net.GetAStatusResponse;

public interface ProgressDetailDataSource {

    interface LoadProgressCallback {

        void onSuccessGet(GetAStatusResponse getAStatusResponse);

        void onFail();

    }

    interface CommentProgressCallback {

        void onSuccessfulComment();

        void onFail();
    }

    interface SetLikeProgressCallback {

        void onSuccessfulSet();

        void onFail();
    }

    interface DeleteCommentCallback {

        void onSuccessfulDelete();

        void onFail();
    }

    void getProgressDetail(int sid, LoadProgressCallback callback);

    void setLikeProgress(int sid, boolean iflike, SetLikeProgressCallback callback);

    void commentProgress(int sid, String comment, CommentProgressCallback callback);

    void deleteProgressComment(int sid, int cid, DeleteCommentCallback callback);

}
