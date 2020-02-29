package com.muxi.workbench.ui.progress.presenter;

import com.muxi.workbench.ui.progress.contract.ProgressDetailContract;
import com.muxi.workbench.ui.progress.model.Comment;
import com.muxi.workbench.ui.progress.model.Progress;
import com.muxi.workbench.ui.progress.model.net.GetAStatusResponse;
import com.muxi.workbench.ui.progress.model.progressDetail.ProgressDetailDataSource;
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
    public void setLikeProgress(int iflike) {
        mProgressDetailRepository.setLikeProgress(mSid, iflike == 1, new ProgressDetailDataSource.SetLikeProgressCallback() {
            @Override
            public void onSuccessfulSet() {
                mProgressDetailView.refreshLike(iflike);
            }

            @Override
            public void onFail() {
                mProgressDetailView.showError();
            }
        });
    }

    @Override
    public void submitComment(int sid, String comment) {
        mProgressDetailRepository.commentProgress(sid, comment, new ProgressDetailDataSource.CommentProgressCallback() {
            @Override
            public void onSuccessfulComment() {
                loadProgressAndCommentList();
                mProgressDetailView.clearCommentContent();
            }

            @Override
            public void onFail() {
                mProgressDetailView.showError();
            }
        });

    }

    @Override
    public void deleteComment(int sid, int cid, int position) {
        mProgressDetailRepository.deleteProgressComment(sid, cid, new ProgressDetailDataSource.DeleteCommentCallback() {
            @Override
            public void onSuccessfulDelete() {
                mProgressDetailView.deleteComment(position);
            }

            @Override
            public void onFail() {
                mProgressDetailView.showError();
            }
        });
    }

    @Override
    public void loadProgressAndCommentList() {
        mProgressDetailRepository.getProgressDetail(mSid, new ProgressDetailDataSource.LoadProgressCallback() {
            @Override
            public void onSuccessGet(GetAStatusResponse getAStatusResponse) {
                Progress progress = new Progress(mSid, getAStatusResponse.getAuthor_id(), mAvatar, mUsername,
                        getAStatusResponse.getTime(), getAStatusResponse.getTitle(), getAStatusResponse.getContent(),
                        getAStatusResponse.getIflike(), getAStatusResponse.getCommentList().size(), getAStatusResponse.getLikeCount());
                List<Comment> commentList = new ArrayList<>();
                for ( int i = 0 ; i < getAStatusResponse.getCommentList().size() ; i++ ) {
                    GetAStatusResponse.CommentListBean temp = getAStatusResponse.getCommentList().get(i);
                    commentList.add(new Comment(temp.getCid(), temp.getUsername(), temp.getAvatar(), temp.getTime(), temp.getContent()));
                }
                mProgressDetailView.showProgressDetail(progress, commentList, getAStatusResponse.getUsername());
            }

            @Override
            public void onFail() {
                mProgressDetailView.showError();
            }
        });
    }
}
