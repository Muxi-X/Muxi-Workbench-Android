package com.muxi.workbench.ui.home;

import android.util.Log;

import com.muxi.workbench.commonUtils.NetUtil;
import com.muxi.workbench.ui.home.model.FeedBean;
import com.muxi.workbench.ui.login.model.UserWrapper;

import java.io.IOException;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import okhttp3.Request;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RemoteDataSource {

    public void getAllFeedFromRemote(FeedRepository.LoadStatusBeanCallback callback) {
        String token = UserWrapper.getInstance().getToken();
//        String token = "eyJpYXQiOjE1NzU5Nzc1MzksImV4cCI6MTg5MTMzNzUzOSwiYWxnIjoiSFMyNTYifQ.eyJjb25maXJtIjo3N30.AJO_FunQjD7OAAwD3phC7sqAoYlVd-1fgsXQ1vF4H7o";
        Log.e("TAG", "token-->" + token);

        NetUtil.getInstance().getApi().getFeed(token, 1)
                .enqueue(new Callback<FeedBean>() {
                    @Override
                    public void onResponse(Call<FeedBean> call, Response<FeedBean> response) {
                        callback.onDataLoaded(response.body());
                    }

                    @Override
                    public void onFailure(Call<FeedBean> call, Throwable t) {
                        Log.e("TAG", t.getMessage());
                        callback.onDataNotAvailable();
                        t.getStackTrace();
//                        StackTraceElement elements[] = t.getStackTrace();
//                        for (int i = 0; i < elements.length; i++) {
//                            StackTraceElement stackTraceElement = elements[i];
//                            String className = stackTraceElement.getClassName();
//                            String methodName = stackTraceElement.getMethodName();
//                            String fileName = stackTraceElement.getFileName();
//                            int lineNumber = stackTraceElement.getLineNumber();
//                            Log.e("TAG", "StackTraceElement数组下标 i=" + i + ",fileName="
//                                    + fileName + ",className=" + className + ",methodName=" + methodName + ",lineNumber=" + lineNumber);
//                        }

                    }
                });


//        final Disposable[] mDisposable = new Disposable[1];
//        NetUtil.getInstance().getApi().getFeed(token, 1)
//                .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(new Observer<FeedBean>() {
//                    @Override
//                    public void onSubscribe(Disposable d) {
//                        mDisposable[0] = d;
//                    }
//
//                    @Override
//                    public void onNext(FeedBean feedBean) {
//                        callback.onDataLoaded(feedBean);
//                        Log.e("TAG", "RemoteDataSource onNext");
//
//                    }
//
//                    @Override
//                    public void onError(Throwable e) {
//                        callback.onDataNotAvailable();
//                        Log.e("TAG", "RemoteDataSource onError" + e.getMessage());
//
//                    }
//
//                    @Override
//                    public void onComplete() {
//                        callback.onDataNotAvailable();
//                        Log.e("TAG", "RemoteDataSource onComplete");
//                    }
//                });
    }

}
