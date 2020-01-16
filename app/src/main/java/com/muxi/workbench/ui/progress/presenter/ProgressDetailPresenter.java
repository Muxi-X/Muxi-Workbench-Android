package com.muxi.workbench.ui.progress.presenter;

import com.muxi.workbench.ui.progress.contract.ProgressDetailContract;
import com.muxi.workbench.ui.progress.model.progressDetail.ProgressDetailRepository;

public class ProgressDetailPresenter implements ProgressDetailContract.Presenter {

    private ProgressDetailContract.View mProgressDetailView;

    private ProgressDetailRepository mProgressDetailRepository;

    public ProgressDetailPresenter(ProgressDetailContract.View mProgressDetailView, ProgressDetailRepository mProgressDetailRepository) {
        this.mProgressDetailView = mProgressDetailView;
        this.mProgressDetailRepository = mProgressDetailRepository;
        mProgressDetailView.setPresenter(this);
    }

    @Override
    public void start() {
    }

    @Override
    public void setLikeProgress() {
        //mProgressDetailRepository.setLikeProgress();
        //mProgressDetailView.refreshLike();
    }

    @Override
    public void submitComment() {

    }

    @Override
    public void deleteComment() {

    }

}
