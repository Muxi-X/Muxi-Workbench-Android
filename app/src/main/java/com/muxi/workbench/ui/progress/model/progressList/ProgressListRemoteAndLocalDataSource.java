package com.muxi.workbench.ui.progress.model.progressList;

import android.util.Log;

import androidx.annotation.NonNull;

import com.muxi.workbench.commonUtils.AppExecutors;
import com.muxi.workbench.commonUtils.net.NetUtil;
import com.muxi.workbench.ui.login.model.UserWrapper;
import com.muxi.workbench.ui.progress.model.Progress;
import com.muxi.workbench.ui.progress.model.StickyProgress;
import com.muxi.workbench.ui.progress.model.StickyProgressDao;
import com.muxi.workbench.ui.progress.model.net.GetAStatusResponse;
import com.muxi.workbench.ui.progress.model.net.GetGroupUserListResponse;
import com.muxi.workbench.ui.progress.model.net.GetStatusListResponse;
import com.muxi.workbench.ui.progress.model.net.IfLikeStatusBean;
import com.muxi.workbench.ui.progress.model.net.LikeStatusResponse;
import com.muxi.workbench.ui.progress.model.net.StatusTitleBean;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Response;

public class ProgressListRemoteAndLocalDataSource implements ProgressListDataSource {

    private static ProgressListRemoteAndLocalDataSource INSTANCE;

    private StickyProgressDao mStickyProgressDao;

    private AppExecutors mAppExecutors;

   private final String token = UserWrapper.getInstance().getToken();

    public static ProgressListRemoteAndLocalDataSource getInstance(StickyProgressDao stickyProgressDao, AppExecutors mAppExecutors) {
        if (INSTANCE == null) {
            synchronized (ProgressListRemoteAndLocalDataSource.class) {
                if ( INSTANCE == null ) {
                    INSTANCE = new ProgressListRemoteAndLocalDataSource(stickyProgressDao, mAppExecutors);
                }
            }
        }
        return INSTANCE;
    }

    private ProgressListRemoteAndLocalDataSource(StickyProgressDao stickyProgressDao, AppExecutors appExecutors) {
        mStickyProgressDao = stickyProgressDao;
        mAppExecutors = appExecutors;
    }

    @Override
    public void getProgressList(@NonNull LoadProgressListCallback callback) {

        List<Progress> progressList = new ArrayList<>();
        NetUtil.getInstance().getApi()
                .getStatusList(token)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe( new Observer<GetStatusListResponse>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(GetStatusListResponse getStatusListResponse) {
                        for (GetStatusListResponse.DataBean.StautsBean statusBean : getStatusListResponse.getData().getStauts()) {
                            progressList.add(new Progress(statusBean.getId(), statusBean.getAvatar(),
                                    statusBean.getUsername(), statusBean.getTime(), statusBean.getTitle(),
                                    statusBean.getContent(), statusBean.isLiked(),
                                    statusBean.getComment_count(), statusBean.getLike_count()));
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                        callback.onDataNotAvailable();
                    }

                    @Override
                    public void onComplete() {
                        callback.onProgressListLoaded(progressList);
                    }
                });
    }

        @Override
        public void ifLikeProgress(int sid, boolean iflike, SetLikeProgressCallback callback) {

            NetUtil.getInstance().getApi().ifLikeStatus(token,sid, new IfLikeStatusBean(iflike?false:true))
                    .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<LikeStatusResponse>() {
                    @Override
                    public void onSubscribe(Disposable disposable) {

                    }

                    @Override
                    public void onNext(LikeStatusResponse likeStatusResponse) {

                    }

                    @Override
                    public void onError(Throwable throwable) {
                        throwable.printStackTrace();
                        callback.onFail();
                    }

                    @Override
                    public void onComplete() {
                        callback.onSuccessfulSet();
                    }
                });
    }

    @Override
    public void refreshProgressList() {

    }

    @Override
    public void deleteProgress(@NonNull int sid,String title, DeleteProgressCallback callback) {
        NetUtil.getInstance().getApi().deleteStatus(sid,token,new StatusTitleBean(title))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Response<Void>>() {
                    @Override
                    public void onSubscribe(Disposable disposable) {

                    }

                    @Override
                    public void onNext(Response<Void> voidResponse) {

                    }

                    @Override
                    public void onError(Throwable throwable) {
                        throwable.printStackTrace();
                        callback.onFail();
                    }

                    @Override
                    public void onComplete() {
                        callback.onSuccessfulDelete();
                    }
                });
    }

    @Override
    public void setStickyProgress(@NonNull Progress progress) {
        Runnable r = new Runnable() {
            @Override
            public void run() {
                mStickyProgressDao.addStickyProgress(new StickyProgress(progress));
            }
        };
        mAppExecutors.diskIO().execute(r);

    }

    @Override
    public void getAllStickyProgress(@NonNull LoadStickyProgressCallback callback) {
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                List<StickyProgress> list = mStickyProgressDao.getStickyProgressList();
                List<Progress> progressList = new ArrayList<>();
                for ( int i = 0 ; i < list.size() ; i++ )
                    progressList.add(new Progress(list.get(i)));
                mAppExecutors.mainThread().execute(new Runnable() {
                    @Override
                    public void run() {
                        if ( !list.isEmpty() ) {
                            callback.onStickyProgressLoaded(progressList);
                        } else {
                            callback.onDataNotAvailable();
                        }
                    }
                });
            }
        };
        mAppExecutors.diskIO().execute(runnable);
    }

    @Override
    public void deleteStickyProgress(int sid) {
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                mStickyProgressDao.deleteStickyProgress(sid);

            }
        };
        mAppExecutors.diskIO().execute(runnable);
    }

    @Override
    public void getGroupUserList(int gid, GetGroupUserListCallback callback) {
        List<Integer> UserList = new ArrayList<>();
        NetUtil.getInstance().getApi().getGroupUserList( gid)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<GetGroupUserListResponse>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(GetGroupUserListResponse getGroupUserListResponse) {
                        for ( int i = 0 ; i < getGroupUserListResponse.getList().size() ; i++ )
                            UserList.add(getGroupUserListResponse.getList().get(i).getUserID());
                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                        callback.onFail();
                    }

                    @Override
                    public void onComplete() {
                        callback.onSuccessfulGet(UserList);
                    }
                });
    }

    @Override
    public void getProgress(int sid, String avatar, String username, LoadProgressCallback callback) {
        Progress progress = new Progress();
        NetUtil.getInstance().getApi().getAStatus(token,sid)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<GetAStatusResponse>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                    }

                    @Override
                    public void onNext(GetAStatusResponse getAStatusResponse) {
                      progress.setTime(getAStatusResponse.getData().getTime());
                        progress.setSid(getAStatusResponse.getData().getSid());
                        progress.setContent(getAStatusResponse.getData().getContent());
                        progress.setTitle(getAStatusResponse.getData().getTitle());
                        progress.setAvatar(avatar);
                        progress.setUsername(username);
                    }

                    @Override
                    public void onError(Throwable e) {
                        callback.onDataNotAvailable();
                    }

                    @Override
                    public void onComplete() {
                        callback.onProgressLoaded(progress);
                    }
                });
    }
}
