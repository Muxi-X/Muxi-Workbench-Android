package com.muxi.workbench.ui.progress.presenter;

import android.util.Log;

import androidx.annotation.NonNull;

import com.muxi.workbench.ui.progress.ProgressContract;
import com.muxi.workbench.ui.progress.ProgressFilterType;
import com.muxi.workbench.ui.progress.model.DataSource;
import com.muxi.workbench.ui.progress.model.Progress;
import com.muxi.workbench.ui.progress.model.ProgressRepository;

import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;

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
        mCurrentFiltering = ProgressFilterType.ALL_PROGRESS;
        page = 1;
    }

    @Override
    public void loadProgressList(boolean ifForceUpdate) {

        ProgressListToShow = new ArrayList<>();

        if ( ifForceUpdate )
            page = 1;
        else
            page++;

        List<Integer> StickyProgressSidList = new ArrayList<>();
        List<Integer> UserListToShow = new ArrayList<>();

        mProgressRepository.getAllStickyProgress(new DataSource.LoadStickyProgressCallback() {
            @Override
            public void onStickyProgressLoaded(List<Progress> StickyProgressList) {
                if (ifForceUpdate)
                    ProgressListToShow.addAll(StickyProgressList);
                for (int i = 0; i < StickyProgressList.size(); i++)
                    StickyProgressSidList.add(StickyProgressList.get(i).getSid());
            }

            @Override
            public void onDataNotAvailable() {
            }
        });

        switch (mCurrentFiltering) {
            case ALL_PROGRESS:
                UserListToShow.add(0);
                break;
            case PRODUCT_PROGRESS:
                getGroupUserList(UserListToShow,1);
                break;
            case DESIGN_PROGRESS:
                getGroupUserList(UserListToShow,5);
                break;
            case ANDROID_PROGRESS:
                getGroupUserList(UserListToShow,4);
                break;
            case BACKEND_PROGRESS:
                getGroupUserList(UserListToShow,3);
                break;
            case FRONTEND_PROGRESS:
                getGroupUserList(UserListToShow,2);
                break;
        }

        mProgressRepository.getProgressList(page, new DataSource.LoadProgressListCallback() {

            @Override
            public void onProgressListLoaded(List<Progress> progressList) {

                for (int j=0 ; j < progressList.size() ; j++ ) {
                    Progress progress = progressList.get(j);
                    if ( StickyProgressSidList.contains(progress.getSid()))
                        continue;
                    if ( UserListToShow.size() == 1 )
                        ProgressListToShow.add(progress);
                    else if ( UserListToShow.contains(progress.getUid()) )
                        ProgressListToShow.add(progress);
                }

                if ( ifForceUpdate ) {
                    mProgressView.showProgressList(ProgressListToShow);
                } else {
                    mProgressView.showMoreProgress(ProgressListToShow);
                }
            }

            @Override
            public void onDataNotAvailable() {
                Log.e("getPage","fail");
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
        showFilterLabel();
    }

    private void getGroupUserList(List<Integer> userList, int gid) {

        mProgressRepository.getGroupUserList(gid, new DataSource.GetGroupUserListCallback() {
            @Override
            public void onSuccessfulGet(List<Integer> UserList) {
                userList.addAll(UserList);
            }
            @Override
            public void onFail() {
                mProgressView.showError();
            }
        });
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
