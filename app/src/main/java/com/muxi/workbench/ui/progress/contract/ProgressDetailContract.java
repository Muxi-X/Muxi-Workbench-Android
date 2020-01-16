package com.muxi.workbench.ui.progress.contract;

import com.muxi.workbench.ui.progress.model.Comment;
import com.muxi.workbench.ui.progress.model.Progress;

import java.util.List;

public interface ProgressDetailContract {

    interface View {

        void setPresenter(Presenter mPresenter);

        void refreshLike();

        void showEditCommentView();

        void showNewComment();

        void showDeleteComment();

        void showEditProgress();

        void showProgressDetail(Progress progress, List<Comment> commentList);

    }

    interface Presenter {

        void start(int sid, String avatar, String username);

        void setLikeProgress();

        void submitComment();

        void deleteComment();

        void loadProgressAndCommentList();


    }

}
