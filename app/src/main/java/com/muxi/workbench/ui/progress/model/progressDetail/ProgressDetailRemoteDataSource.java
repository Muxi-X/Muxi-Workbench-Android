package com.muxi.workbench.ui.progress.model.progressDetail;

import com.muxi.workbench.commonUtils.NetUtil;
import com.muxi.workbench.ui.login.model.UserWrapper;
import com.muxi.workbench.ui.progress.model.net.CommentStautsBean;
import com.muxi.workbench.ui.progress.model.net.IfLikeStatusBean;
import com.muxi.workbench.ui.progress.model.net.LikeStatusResponse;
import com.muxi.workbench.ui.progress.model.net.GetAStatusResponse;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Response;

public class ProgressDetailRemoteDataSource implements ProgressDetailDataSource {

    private static ProgressDetailRemoteDataSource INSTANCE;

    private final String token = UserWrapper.getInstance().getToken();

    public static ProgressDetailRemoteDataSource getInstance() {
        if (INSTANCE == null) {
            synchronized (ProgressDetailRemoteDataSource.class) {
                if ( INSTANCE == null ) {
                    INSTANCE = new ProgressDetailRemoteDataSource();
                }
            }
        }
        return INSTANCE;
    }

    private ProgressDetailRemoteDataSource() {
    }

    @Override
    public void getProgressDetail(int sid, loadProgressCallback callback) {


        NetUtil.getInstance().getApi().getAStatus(token, sid)
                .observeOn(Schedulers.io())
                .subscribeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<GetAStatusResponse>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(GetAStatusResponse getAStatusResponse) {
                        callback.onSuccessGet(getAStatusResponse);
                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                        callback.onFail();
                    }

                    @Override
                    public void onComplete() {
                    }
                });
    }

    @Override
    public void setLikeProgress(int sid, boolean iflike, SetLikeProgressCallback callback) {
        NetUtil.getInstance().getApi().ifLikeStatus(token, sid, new IfLikeStatusBean(iflike == true ? 1 : 0) )
                .observeOn(Schedulers.io())
                .subscribeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<LikeStatusResponse>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(LikeStatusResponse likeStatusResponse) {
                        callback.onSuccessfulSet();
                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                        callback.onFail();
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    @Override
    public void commentProgress(int sid, String comment, CommentProgressCallback callback) {
        NetUtil.getInstance().getApi().commentStatus(token,sid, new CommentStautsBean(comment))
                .observeOn(Schedulers.io())
                .subscribeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Response<Void>>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(Response<Void> voidResponse) {
                        callback.onSuccessfulComment();
                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                        callback.onFail();
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    @Override
    public void deleteProgressComment(int sid, int cid, DeleteCommentCallback callback) {
        NetUtil.getInstance().getApi().deleteComment(token, sid, cid)
                .observeOn(Schedulers.io())
                .subscribeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Response<Void>>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(Response<Void> voidResponse) {
                        callback.onSuccessfulDelete();
                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                        callback.onFail();
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }
}