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

    private static int page = 1;

    public ProgressPresenter(@NonNull ProgressRepository progressRepository, @NonNull ProgressContract.View progressView) {
        mProgressRepository = progressRepository;
        mProgressView = progressView;

       mProgressView.setPresenter(this);
    }

    @Override
    public void start() {
        page = 1;
    }

    @Override
    public void loadProgressList(boolean ifForceUpdate) {

        ProgressListToShow = new ArrayList<>();

        if ( ifForceUpdate )
            page = 1;
        else
            page++;
        Log.e("load",ifForceUpdate+"  "+page);

        if ( ifForceUpdate ) {
            mProgressRepository.getAllStickyProgress(new DataSource.LoadStickyProgressCallback() {
                @Override
                public void onStickyProgressLoaded(List<Progress> StickyProgressList) {
                    Log.e("sticky","get");
                    ProgressListToShow.addAll(StickyProgressList);
                }
                @Override
                public void onDataNotAvailable() {
                    Log.e("sticky", "get none");
                }
            });
        }

        mProgressRepository.getProgressList(page, new DataSource.LoadProgressListCallback() {

            @Override
            public void onProgressListLoaded(List<Progress> progressList) {

                showFilterLabel();

                for (int j=0;j<progressList.size();j++) {

                    Progress progress = progressList.get(j);
                    switch (mCurrentFiltering) {
                        case ALL_PROGRESS:
                            ProgressListToShow.add(progress);
                            break;
                        case PRODUCT_PROGRESS:
                            if (ifThisGroup(progress.getUid(), 1)) {
                                ProgressListToShow.add(progress);
                            }
                            break;
                        case DESIGN_PROGRESS:
                            if (ifThisGroup(progress.getUid(), 2)) {
                                ProgressListToShow.add(progress);
                            }
                            break;
                        case ANDROID_PROGRESS:
                            if (ifThisGroup(progress.getUid(), 3)) {
                                ProgressListToShow.add(progress);
                            }
                            break;
                        case BACKEND_PROGRESS:
                            if (ifThisGroup(progress.getUid(), 4)) {
                                ProgressListToShow.add(progress);
                            }
                            break;
                        case FRONTEND_PROGRESS:
                            if (ifThisGroup(progress.getUid(), 5)) {
                                ProgressListToShow.add(progress);
                            }
                            break;
                    }
                }

                if ( ifForceUpdate ) {
                    Log.e("refresh","replaceAll");
                    mProgressView.showProgressList(ProgressListToShow);
                } else {
                    Log.e("refresh","Add");
                    mProgressView.showMoreProgress(ProgressListToShow);
                }
            }

            @Override
            public void onDataNotAvailable() {
                Log.e("getPage1","fail");
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
    public void likeProgress(int sid, int position) {
        mProgressRepository.ifLikeProgress(sid, true, new DataSource.SetLikeProgressCallback() {
            @Override
            public void onSuccessfulSet() {
                mProgressView.refreshLikeProgress(position, 1);
            }

            @Override
            public void onFail() {
                mProgressView.showError();
            }
        });
    }

    @Override
    public void cancelLikeProgress(int sid, int position) {
        mProgressRepository.ifLikeProgress(sid, false, new DataSource.SetLikeProgressCallback() {
            @Override
            public void onSuccessfulSet() {
                mProgressView.refreshLikeProgress(position, 0);
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
    public void openUserInfo(@NonNull int uid) {
        mProgressView.showUserInfo(uid);
    }

    @Override
    public void deleteProgress(int position, int sid) {
        mProgressRepository.deleteProgress(sid, new DataSource.DeleteProgressCallback() {
            @Override
            public void onSuccessfulDelete() {
                mProgressView.showDeleteProgress(position);
            }

            @Override
            public void onFail() {
                mProgressView.showError();
            }
        });
    }

    @Override
    public void setProgressSticky(int position, Progress progress) {
        mProgressRepository.setStickyProgress(progress);
        mProgressView.moveNewStickyProgress(position);
    }

    @Override
    public void cancelStickyProgress(int position, Progress progress) {
        mProgressRepository.deleteStickyProgress(progress.getSid());
        mProgressView.moveDeleteStickyProgress(position);
    }
}
