package com.muxi.workbench.ui.progress.contract;

import com.muxi.workbench.ui.progress.model.Comment;
import com.muxi.workbench.ui.progress.model.Progress;

import java.util.List;

public interface ProgressDetailContract {

    interface View {

        void setPresenter(Presenter mPresenter);

        void refreshLike(int iflike);

        void deleteComment(int position);

        void showEditCommentView();

        void clearCommentContent();

        void showProgressDetail(Progress progress, List<Comment> commentList, String username);

        void showError();

    }

    interface Presenter {

        void start(int sid, String avatar, String username);

        void setLikeProgress(int iflike);

        void submitComment(int sid, String comment);

        void deleteComment(int sid, int cid, int position);

        void loadProgressAndCommentList();


    }

}
