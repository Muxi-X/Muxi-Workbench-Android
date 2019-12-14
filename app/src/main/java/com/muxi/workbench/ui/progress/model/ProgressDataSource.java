package com.muxi.workbench.ui.progress.model;

import android.util.Log;

import androidx.annotation.NonNull;

import com.muxi.workbench.commonUtils.NetUtil;
import com.muxi.workbench.ui.login.model.UserWrapper;
import com.muxi.workbench.ui.progress.model.net.CommentStautsBean;
import com.muxi.workbench.ui.progress.model.net.GetAStatusResponse;
import com.muxi.workbench.ui.progress.model.net.GetStatusListResponse;
import com.muxi.workbench.ui.progress.model.net.IfLikeStatusBean;

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

    private final String token = UserWrapper.getInstance().getToken();

    private final int uid = UserWrapper.getInstance().getUser().getUid();

    public static ProgressDataSource getInstance(StickyProgressDao stickyProgressDao) {
        if (INSTANCE == null) {
            synchronized (ProgressDataSource.class) {
                if ( INSTANCE == null ) {
                    INSTANCE = new ProgressDataSource(stickyProgressDao);
                }
            }
        }
        return INSTANCE;
    }

    // Prevent direct instantiation.
    private ProgressDataSource(StickyProgressDao stickyProgressDao) {
        mStickyProgressDao = stickyProgressDao;
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

                        Log.e("are you ok?","what is your problem?"+"  ok");
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

                        Log.e("are you ok?","");
                        e.printStackTrace();
                        callback.onDataNotAvailable();
                    }

                    @Override
                    public void onComplete() {
                        callback.onProgressListLoaded(progressList);
                    }
                });
    }

   /* @Override
    public void getProgress(@NonNull int sid, @NonNull GetProgressCallback callback) {
        NetUtil.getInstance().getApi().getAStatus(token, sid)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<GetAStatusResponse>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(GetAStatusResponse getAStatusResponse) {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }*/

    @Override
    public void commentProgress(int sid, String comment) {

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

                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    @Override
    public void ifLikeProgress(int sid, boolean iflike) {

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

                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    @Override
    public void refreshProgressList() {
///todo
    }

    @Override
    public void deleteProgress(@NonNull int sid) {
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

                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    @Override
    public void setStickyProgress(@NonNull int sid) {
        mStickyProgressDao.addStickyProgress(new StickyProgress(sid));
    }

    @Override
    public boolean isStickyProgress(int sid) {
        if (mStickyProgressDao.isStickyProgress(sid)  == null)
            return false;
        else return true;
    }
}
