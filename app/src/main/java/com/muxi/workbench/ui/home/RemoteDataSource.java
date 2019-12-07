package com.muxi.workbench.ui.home;

import android.util.Log;

import com.muxi.workbench.commonUtils.NetUtil;
import com.muxi.workbench.ui.home.model.FeedBean;
import com.muxi.workbench.ui.login.model.UserWrapper;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class RemoteDataSource {

    public void getAllFeedFromRemote(FeedRepository.LoadStatusBeanCallback callback) {
        String token = UserWrapper.getInstance().getToken();
        Log.e("TAG", "token-->" + token);

        final Disposable[] mDisposable = new Disposable[1];
        NetUtil.getInstance().getApi().getFeed(token, 1).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<FeedBean>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        mDisposable[0] = d;
                    }

                    @Override
                    public void onNext(FeedBean feedBean) {
                        callback.onDataLoaded(feedBean);
                        Log.e("TAG", "RemoteDataSource onNext");

                    }

                    @Override
                    public void onError(Throwable e) {
                        callback.onDataNotAvailable();
                        Log.e("TAG", "RemoteDataSource onError" + e.getMessage());

                    }

                    @Override
                    public void onComplete() {
                        callback.onDataNotAvailable();
                        Log.e("TAG", "RemoteDataSource onComplete");
                    }
                });
    }

}
