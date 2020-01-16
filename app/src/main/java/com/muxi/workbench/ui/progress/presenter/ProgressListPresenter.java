package com.muxi.workbench.ui.progress.presenter;

import android.util.Log;

import androidx.annotation.NonNull;

import com.muxi.workbench.ui.progress.contract.ProgressContract;
import com.muxi.workbench.ui.progress.ProgressFilterType;
import com.muxi.workbench.ui.progress.model.Progress;
import com.muxi.workbench.ui.progress.model.progressList.ProgressListDataSource;
import com.muxi.workbench.ui.progress.model.progressList.ProgressListRepository;

import java.util.ArrayList;
import java.util.List;

public class ProgressListPresenter implements ProgressContract.Presenter {

    private final ProgressListRepository mProgressListRepository;

    private final ProgressContract.View mProgressView;

    private ProgressFilterType mCurrentFiltering = ProgressFilterType.ALL_PROGRESS;

    private List<Progress> ProgressListToShow = new ArrayList<>();

    private static int page = 1;

    public ProgressListPresenter(@NonNull ProgressListRepository progressListRepository, @NonNull ProgressContract.View progressView) {
        mProgressListRepository = progressListRepository;
        mProgressView = progressView;
        mProgressView.setPresenter(this);
    }

    @Override
    public void start(ProgressFilterType lasProgressFilterType, int lastPage) {
        mCurrentFiltering = lasProgressFilterType;
        page = lastPage;
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

        mProgressListRepository.getAllStickyProgress(new ProgressListDataSource.LoadStickyProgressCallback() {
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

        mProgressListRepository.getProgressList(page, new ProgressListDataSource.LoadProgressListCallback() {

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

                if ( ifForceUpdate || page == 1 ) {
                    Log.e("progressfragment","replace");
                    mProgressView.showProgressList(ProgressListToShow);
                } else {
                    Log.e("progressfragment","addMore");
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
        mProgressListRepository.ifLikeProgress(sid, true, new ProgressListDataSource.SetLikeProgressCallback() {
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
        mProgressListRepository.ifLikeProgress(sid, false, new ProgressListDataSource.SetLikeProgressCallback() {
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
    public int getFiltering() {
        switch (mCurrentFiltering) {
            case ALL_PROGRESS:
                return 0;
            case DESIGN_PROGRESS:
                return 5;
            case ANDROID_PROGRESS:
                return 3;
            case BACKEND_PROGRESS:
                return 4;
            case PRODUCT_PROGRESS:
                return 1;
            case FRONTEND_PROGRESS:
                return 2;
        }
        return 0;
    }

    @Override
    public int getPage() {
        return page;
    }

    @Override
    public void setProgressFilterType(ProgressFilterType requestType) {
        mCurrentFiltering = requestType;
       // showFilterLabel();
    }

    private void getGroupUserList(List<Integer> userList, int gid) {

        mProgressListRepository.getGroupUserList(gid, new ProgressListDataSource.GetGroupUserListCallback() {
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

  /*  private void showFilterLabel() {
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
    }*/

    @Override
    public void openUserInfo(@NonNull int uid) {
        mProgressView.showUserInfo(uid);
    }

    @Override
    public void deleteProgress(int position, int sid) {
        mProgressListRepository.deleteProgress(sid, new ProgressListDataSource.DeleteProgressCallback() {
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
        mProgressListRepository.setStickyProgress(progress);
        mProgressView.moveNewStickyProgress(position);
    }

    @Override
    public void cancelStickyProgress(int position, Progress progress) {
        mProgressListRepository.deleteStickyProgress(progress.getSid());
        mProgressView.moveDeleteStickyProgress(position);
    }
}
