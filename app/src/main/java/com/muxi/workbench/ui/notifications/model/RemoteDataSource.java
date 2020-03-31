package com.muxi.workbench.ui.notifications.model;

import com.muxi.workbench.commonUtils.net.NetUtil;
import com.muxi.workbench.ui.login.model.UserWrapper;
import com.muxi.workbench.ui.notifications.model.NotificationsRepository;
import com.muxi.workbench.ui.notifications.model.NotificationsResponse;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class RemoteDataSource {
    public static void getNotificationFromRemote(NotificationsRepository.LoadNotiResponseCallback callback, int page) {
        String token = UserWrapper.getInstance().getToken();
        final Disposable[] mDisposable = new Disposable[1];
        NetUtil.getInstance().getApi().getNotifications( page)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<NotificationsResponse>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        mDisposable[0] = d;
                    }

                    @Override
                    public void onNext(NotificationsResponse feedBean) {
                        callback.onDataLoaded(feedBean);
                    }

                    @Override
                    public void onError(Throwable e) {
                        callback.onDataNotAvailable();
                        e.printStackTrace();
                    }

                    @Override
                    public void onComplete() {
                        callback.onComplete();
                    }
                });

    }

    public static void clearNotifications(NotificationsRepository.LoadNotiResponseCallback callback){

    }
}
