package com.muxi.workbench.ui.progress.contract;

public interface ProgressDetailContract {

    interface View {

        void setPresenter(Presenter mPresenter);

        void refreshLike();

        void showEditCommentView();

        void showNewComment();

        void showDeleteComment();

        void showEditProgress();

    }

    interface Presenter {

        void start();

        void setLikeProgress();

        void submitComment();

        void deleteComment();


    }

}
