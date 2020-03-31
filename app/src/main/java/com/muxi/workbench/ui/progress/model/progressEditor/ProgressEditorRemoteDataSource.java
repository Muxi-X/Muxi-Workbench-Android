package com.muxi.workbench.ui.progress.model.progressEditor;

import com.muxi.workbench.commonUtils.net.NetUtil;
import com.muxi.workbench.ui.login.model.UserWrapper;
import com.muxi.workbench.ui.progress.model.net.PostNewStatusResponse;
import com.muxi.workbench.ui.progress.model.net.StatusBean;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Response;

public class ProgressEditorRemoteDataSource implements ProgressEditorDataSource{

    private static ProgressEditorRemoteDataSource INSTANCE;

    private final String token = UserWrapper.getInstance().getToken();

    public static ProgressEditorRemoteDataSource getInstance() {
        if (INSTANCE == null) {
            synchronized (ProgressEditorRemoteDataSource.class) {
                if ( INSTANCE == null ) {
                    INSTANCE = new ProgressEditorRemoteDataSource();
                }
            }
        }
        return INSTANCE;
    }

    private ProgressEditorRemoteDataSource(){}

    @Override
    public void newProgress(String title, String content, NewProgressCallback callback) {
        NetUtil.getInstance().getApi()
                .newStatus(token,new StatusBean(title, content))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<PostNewStatusResponse>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(PostNewStatusResponse postNewStatusResponse) {

                    }

                    @Override
                    public void onError(Throwable e) {
                        callback.onError();
                    }

                    @Override
                    public void onComplete() {
                        callback.onSuccess();
                    }
                });

    }

    @Override
    public void changeProgress(int sid, String title, String content, ChangeProgressCallback callback) {
        NetUtil.getInstance().getApi()
                .editStatus(token,sid,new StatusBean(title, content))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Response<Void>>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(Response<Void> voidResponse) {

                    }

                    @Override
                    public void onError(Throwable e) {
                        callback.onError();
                    }

                    @Override
                    public void onComplete() {
                        callback.onSuccess();
                    }
                });
    }
}
