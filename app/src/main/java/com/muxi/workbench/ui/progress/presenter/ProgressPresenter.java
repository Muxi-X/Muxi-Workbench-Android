package com.muxi.workbench.ui.progress.presenter;

import android.util.Log;

import androidx.annotation.NonNull;

import com.muxi.workbench.ui.progress.ProgressContract;
import com.muxi.workbench.ui.progress.ProgressFilterType;
import com.muxi.workbench.ui.progress.model.DataSource;
import com.muxi.workbench.ui.progress.model.Progress;
import com.muxi.workbench.ui.progress.model.ProgressRepository;

import java.util.ArrayList;
import java.util.List;

public class ProgressPresenter implements ProgressContract.Presenter {

    private final ProgressRepository mProgressRepository;

    private final ProgressContract.View mProgressView;

    private ProgressFilterType mCurrentFiltering = ProgressFilterType.ALL_PROGRESS;

    private List<Progress> ProgressListToShow = new ArrayList<>();

    public ProgressPresenter(@NonNull ProgressRepository progressRepository, @NonNull ProgressContract.View progressView) {
        mProgressRepository = progressRepository;
        mProgressView = progressView;

       mProgressView.setPresenter(this);
    }

    @Override
    public void start() {
        loadProgressList(true);
    }

    @Override
    public void loadProgressList(boolean ifForceUpdate) {

        if ( ifForceUpdate )
            mProgressRepository.refreshProgressList();

        mProgressRepository.getProgressList(1,new DataSource.LoadProgressListCallback() {

            @Override
            public void onProgressListLoaded(List<Progress> progressList) {
                int i = 0;

                for (int j=0;j<progressList.size();j++) {
                    Progress progress=progressList.get(j);
                    switch (mCurrentFiltering) {
                        case ALL_PROGRESS:
                            ProgressListToShow.add(progress);
                            break;
                        case PRODUCT_PROGRESS:
                            if (ifThisGroup(progress.getUid(),1)) {
                                ProgressListToShow.add(progress);
                            }
                            break;
                        case DESIGN_PROGRESS:
                            if (ifThisGroup(progress.getUid(),2)) {
                                ProgressListToShow.add(progress);
                            }
                            break;
                        case ANDROID_PROGRESS:
                            if (ifThisGroup(progress.getUid(),3)) {
                                ProgressListToShow.add(progress);
                            }
                            break;
                        case BACKEND_PROGRESS:
                            if (ifThisGroup(progress.getUid(),4)) {
                                ProgressListToShow.add(progress);
                            }
                            break;
                        case FRONTEND_PROGRESS:
                            if (ifThisGroup(progress.getUid(),5)) {
                                ProgressListToShow.add(progress);
                            }
                            break;
                    }
                }



                mProgressView.showProgressList(ProgressListToShow);

                showFilterLabel();
            }

            @Override
            public void onDataNotAvailable() {
                if (!mProgressView.isActive())
                    return;
                mProgressView.showError();
            }
        });

    }

    @Override
    public void addNewProgress() {
        mProgressView.showAddNewProgress();
    }

    @Override
    public void openProgressDetails(@NonNull Progress progress) {
        mProgressView.showProgressDetail(progress.getSid());
    }

    @Override
    public void likeProgress(int sid) {
        mProgressRepository.ifLikeProgress(sid, true, new DataSource.SetLikeProgressCallback() {
            @Override
            public void onSuccessfulSet() {
                mProgressView.showLikeProgress();
            }

            @Override
            public void onFail() {
                mProgressView.showError();
            }
        });
    }

    @Override
    public void cancelLikeProgress(int sid) {
        mProgressRepository.ifLikeProgress(sid, false, new DataSource.SetLikeProgressCallback() {
            @Override
            public void onSuccessfulSet() {
                mProgressView.showNotLikedProgress();
            }

            @Override
            public void onFail() {
                mProgressView.showError();
            }
        });
    }

    @Override
    public void commentProgress(@NonNull int sid, String comment) {
        mProgressRepository.commentProgress(sid, comment, new DataSource.CommentProgressCallback() {
            @Override
            public void onSuccessfulComment() {
                mProgressView.renewCommentNum();
            }

            @Override
            public void onFail() {
                mProgressView.showError();
            }
        });
    }

    @Override
    public ProgressFilterType getFiltering() {
        return mCurrentFiltering;
    }

    @Override
    public void setProgressFilterType(ProgressFilterType requestType) {
        mCurrentFiltering = requestType;
    }

    private boolean ifThisGroup(int uid, int gid) {
        ///todo add judgement
        return true;
    }

    private void showFilterLabel() {
        switch (mCurrentFiltering) {
            case ALL_PROGRESS:
                mProgressView.showSelectAllFilter();
                break;
            case ANDROID_PROGRESS:
                mProgressView.showSelectAndroidFilter();
                break;
            case BACKEND_PROGRESS:
                mProgressView.showSelectBackendFilter();
                break;
            case DESIGN_PROGRESS:
                mProgressView.showSelectDesignFilter();
                break;
            case FRONTEND_PROGRESS:
                mProgressView.showSelectFrontendFilter();
                break;
            case PRODUCT_PROGRESS:
                mProgressView.showSelectProductFilter();
                break;
        }
    }

    @Override
    public void setStickyProgress() {
        mProgressRepository.getAllStickyProgress(new DataSource.LoadStickyProgressCallback() {
            @Override
            public void onStickyProgressLoaded(List<Integer> StickyProgressList) {
                for(int i = 0 ; i < ProgressListToShow.size() ; i++) {
                    if ( StickyProgressList.contains(ProgressListToShow.get(i).getSid() ) ) {
                        ProgressListToShow.get(i).setSticky(true);
                        Progress temp = ProgressListToShow.get(i);
                        ProgressListToShow.remove(i);
                        ProgressListToShow.add(0,temp);
                    }
                }
                Log.e(".......","");
                mProgressView.showProgressList(ProgressListToShow);
            }
            @Override
            public void onDataNotAvailable() {

                Log.e("......d.","");
            }
        });
    }

    @Override
    public void openUserInfo(@NonNull int uid) {
        mProgressView.showUserInfo(uid);
    }
}
