package com.muxi.workbench.ui.progress.model;

import android.util.Log;

import androidx.annotation.NonNull;

import com.muxi.workbench.commonUtils.AppExecutors;
import com.muxi.workbench.commonUtils.NetUtil;
import com.muxi.workbench.ui.login.model.UserWrapper;
import com.muxi.workbench.ui.progress.model.net.CommentStautsBean;
import com.muxi.workbench.ui.progress.model.net.GetAStatusResponse;
import com.muxi.workbench.ui.progress.model.net.GetStatusListResponse;
import com.muxi.workbench.ui.progress.model.net.IfLikeStatusBean;

import java.io.SyncFailedException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class ProgressDataSource implements DataSource {

    private static ProgressDataSource INSTANCE;

    private StickyProgressDao mStickyProgressDao;

    private AppExecutors mAppExecutors;

    private final String token = UserWrapper.getInstance().getToken();

    private final int uid = UserWrapper.getInstance().getUser().getUid();

    public static ProgressDataSource getInstance(StickyProgressDao stickyProgressDao, AppExecutors mAppExecutors) {
        if (INSTANCE == null) {
            synchronized (ProgressDataSource.class) {
                if ( INSTANCE == null ) {
                    INSTANCE = new ProgressDataSource(stickyProgressDao, mAppExecutors);
                }
            }
        }
        return INSTANCE;
    }

    // Prevent direct instantiation.
    private ProgressDataSource(StickyProgressDao stickyProgressDao, AppExecutors appExecutors) {
        mStickyProgressDao = stickyProgressDao;
        mAppExecutors = appExecutors;
    }

    @Override
    public void getProgressList(int page, @NonNull LoadProgressListCallback callback) {

        List<Progress> progressList = new ArrayList<>();

        NetUtil.getInstance().getApi().getStatusList(token, page)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe( new Observer<GetStatusListResponse>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(GetStatusListResponse getStatusListResponse) {
                        for (GetStatusListResponse.StatuListBean statuListBean : getStatusListResponse.getStatuList() ) {
                            progressList.add(new Progress(statuListBean.getSid(),
                                    statuListBean.getUid(), statuListBean.getAvatar(),
                                    statuListBean.getUsername(), statuListBean.getTime(),
                                    statuListBean.getContent(), statuListBean.isIflike(),
                                    statuListBean.getCommentCount(), statuListBean.getLikeCount()));
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
    public void commentProgress(int sid, String comment, CommentProgressCallback callback) {

        NetUtil.getInstance().getApi().commentStatus(token, sid, new CommentStautsBean())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(Object o) {

                    }

                    @Override
                    public void onError(Throwable e) {
                        callback.onFail();
                    }

                    @Override
                    public void onComplete() {
                        callback.onSuccessfulComment();
                    }
                });
    }

    @Override
    public void ifLikeProgress(int sid, boolean iflike, SetLikeProgressCallback callback) {

        NetUtil.getInstance().getApi().ifLikeStatus(token, sid, new IfLikeStatusBean(iflike))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(Object o) {

                    }

                    @Override
                    public void onError(Throwable e) {
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
///todo
    }

    @Override
    public void deleteProgress(@NonNull int sid, DeleteProgressCallback callback) {
        NetUtil.getInstance().getApi().deleteStatus(token, sid)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(Object o) {

                    }

                    @Override
                    public void onError(Throwable e) {
                        callback.onFail();
                    }

                    @Override
                    public void onComplete() {
                        callback.onSuccessfulDelete();
                    }
                });
    }

    @Override
    public void setStickyProgress(@NonNull int sid) {
        Runnable r = new Runnable() {
            @Override
            public void run() {
                mStickyProgressDao.addStickyProgress(new StickyProgress(sid));
            }
        };
        mAppExecutors.diskIO().execute(r);

    }

    @Override
    public void getAllStickyProgress(@NonNull LoadStickyProgressCallback callback) {
        Log.e("ddd","");///todo   not active.
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                final List<Integer> list = mStickyProgressDao.getStickyProgressList();
                mAppExecutors.mainThread().execute(new Runnable() {
                    @Override
                    public void run() {
                        if ( !list.isEmpty() ) {
                            callback.onStickyProgressLoaded(list);
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
}
