package com.muxi.workbench.ui.progress.presenter;

import com.muxi.workbench.ui.progress.contract.ProgressDetailContract;
import com.muxi.workbench.ui.progress.model.Comment;
import com.muxi.workbench.ui.progress.model.Progress;
import com.muxi.workbench.ui.progress.model.net.GetAStatusResponse;
import com.muxi.workbench.ui.progress.model.net.GetCommentListResponse;
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

    private int mLikeCount;

    private boolean mIfLike;

    public ProgressDetailPresenter(ProgressDetailContract.View mProgressDetailView, ProgressDetailRepository mProgressDetailRepository) {
        this.mProgressDetailView = mProgressDetailView;
        this.mProgressDetailRepository = mProgressDetailRepository;
        mProgressDetailView.setPresenter(this);
    }

    @Override
    public void start(int sid, String avatar, String username,int likeCount,boolean ifLike) {
        mSid = sid;
        mAvatar = avatar;
        mUsername = username;
        mLikeCount=likeCount;
        mIfLike=ifLike;
    }

    @Override
    public void setLikeProgress(boolean iflike) {
        mProgressDetailRepository.setLikeProgress(mSid, iflike, new ProgressDetailDataSource.SetLikeProgressCallback() {
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
        List<Comment> commentList = new ArrayList<>();
        mProgressDetailRepository.getCommentList(mSid, new ProgressDetailDataSource.LoadCommentListCallback() {
                    @Override
                    public void onSuccessGetCommentList(GetCommentListResponse getCommentListResponse) {

                        for (int i = 0; i < getCommentListResponse.getCommentlist().size(); i++) {
                            GetCommentListResponse.CommentlistBean temp = getCommentListResponse.getCommentlist().get(i);
                            commentList.add(new Comment(temp.getCid(), temp.getUsername(), temp.getAvatar(), temp.getTime(), temp.getContent()));
                        }
                    }

                    @Override
                    public void onFail() {

                    }
        });
        String content=commentList.get(position).getContent();
        mProgressDetailRepository.deleteProgressComment(sid, cid,content ,new ProgressDetailDataSource.DeleteCommentCallback() {
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
        List<Comment> commentList = new ArrayList<>();
        mProgressDetailRepository.getCommentList(mSid, new ProgressDetailDataSource.LoadCommentListCallback() {
            @Override
            public void onSuccessGetCommentList(GetCommentListResponse getCommentListResponse) {
                //
                if(getCommentListResponse.getCommentlist()!=null)
                for (int i = 0; i < getCommentListResponse.getCommentlist().size(); i++) {
                    GetCommentListResponse.CommentlistBean temp = getCommentListResponse.getCommentlist().get(i);
                    commentList.add(new Comment(temp.getCid(), temp.getUsername(), temp.getAvatar(), temp.getTime(), temp.getContent()));
                }

            }

            @Override
            public void onFail() {
                mProgressDetailView.showError();
            }
            });


        mProgressDetailRepository.getProgressDetail(mSid, new ProgressDetailDataSource.LoadProgressCallback() {
            @Override
            public void onSuccessGet(GetAStatusResponse getAStatusResponse) {
                Progress progress = new Progress(mSid, mAvatar, mUsername,
                        getAStatusResponse.getData().getTime(), getAStatusResponse.getData().getTitle(), getAStatusResponse.getData().getContent(),
                        mIfLike,commentList.size() ,mLikeCount);

                mProgressDetailView.showProgressDetail(progress, commentList, getAStatusResponse.getData().getUserid()+""/*getUser_name()*/);
            }

            @Override
            public void onFail() {
                mProgressDetailView.showError();
            }
        });
    }
}
