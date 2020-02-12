package com.muxi.workbench.ui.progress.presenter;

import com.muxi.workbench.ui.progress.contract.ProgressDetailContract;
import com.muxi.workbench.ui.progress.model.Comment;
import com.muxi.workbench.ui.progress.model.Progress;
import com.muxi.workbench.ui.progress.model.net.GetAStatusResponse;
import com.muxi.workbench.ui.progress.model.progressDetail.ProgressDetailDataSource;
import com.muxi.workbench.ui.progress.model.progressDetail.ProgressDetailRemoteDataSource;
import com.muxi.workbench.ui.progress.model.progressDetail.ProgressDetailRepository;

import java.util.ArrayList;
import java.util.List;

public class ProgressDetailPresenter implements ProgressDetailContract.Presenter {

    private ProgressDetailContract.View mProgressDetailView;

    private ProgressDetailRepository mProgressDetailRepository;

    private int mSid;

    private String mAvatar;

    private String mUsername;

    public ProgressDetailPresenter(ProgressDetailContract.View mProgressDetailView, ProgressDetailRepository mProgressDetailRepository) {
        this.mProgressDetailView = mProgressDetailView;
        this.mProgressDetailRepository = mProgressDetailRepository;
        mProgressDetailView.setPresenter(this);
    }

    @Override
    public void start(int sid, String avatar, String username) {
        mSid = sid;
        mAvatar = avatar;
        mUsername = username;
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

    @Override
    public void loadProgressAndCommentList() {
        mProgressDetailRepository.getProgressDetail(mSid, new ProgressDetailDataSource.loadProgressCallback() {
            @Override
            public void onSuccessGet(GetAStatusResponse getAStatusResponse) {
                Progress progress = new Progress(mSid, getAStatusResponse.getAuthor_id(), mAvatar,
                        mUsername, getAStatusResponse.getTime(), getAStatusResponse.getTitle(), getAStatusResponse.getContent(),
                        (getAStatusResponse.isIflike() == true )? 1: 0 , getAStatusResponse.getCommentList().size(), getAStatusResponse.getLikeCount());
                List<Comment> commentList = new ArrayList<>();
                for ( int i = 0 ; i < getAStatusResponse.getCommentList().size() ; i++ ) {
              //      commentList.add(new Comment(getAStatusResponse.getCommentList().get(i)));
                }
                //int uid, int cid, String username, String avatar, String time, String content
             //   mProgressDetailView.showProgressDetail(progress,);
            }

            @Override
            public void onFail() {

            }
        });
    }
}
