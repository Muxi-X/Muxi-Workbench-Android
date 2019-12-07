package com.muxi.workbench.ui.login;

import android.text.TextUtils;
import android.util.Base64;
import android.widget.Toast;

import com.muxi.workbench.commonUtils.NetUtil;
import com.muxi.workbench.ui.login.model.netcall.LoginResponse1;
import com.muxi.workbench.ui.login.model.netcall.LoginResponse2;
import com.muxi.workbench.ui.login.model.User;
import com.muxi.workbench.ui.login.model.netcall.UserBean;
import com.muxi.workbench.ui.login.model.netcall.UserBeanTwo;
import com.muxi.workbench.ui.login.model.UserWrapper;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Function;
import io.reactivex.internal.disposables.ListCompositeDisposable;
import io.reactivex.schedulers.Schedulers;

public class LoginPresenter implements LoginContract.Presenter {

    private LoginContract.View view;
    private ListCompositeDisposable disposableContainer;

    public LoginPresenter(LoginContract.View view) {
        this.view = view;
        disposableContainer = new ListCompositeDisposable();
    }

    @Override
    public void login() {
        String account=view.getAccount();
        String password=view.getPassword();
        if (TextUtils.isEmpty(account)){
            Toast.makeText(view.getContext(),"请输入账号",Toast.LENGTH_SHORT).show();
            return;
        }
        if (TextUtils.isEmpty(password)){
            Toast.makeText(view.getContext(),"请输入密码",Toast.LENGTH_SHORT).show();
            return;
        }
        String encode= Base64.encodeToString(password.getBytes(),Base64.DEFAULT);

        NetUtil.getInstance().getApi().loginFirst(new UserBean(account,encode))
                .subscribeOn(Schedulers.io())
                .doOnSubscribe(v->{
                    view.showLoading();
                })
                .flatMap(new Function<LoginResponse1, ObservableSource<LoginResponse2>>() {
                    @Override
                    public ObservableSource<LoginResponse2> apply(LoginResponse1 loginResponse1) throws Exception {
                        if (loginResponse1.getCode()!=0){
                            return Observable.error(new Exception(loginResponse1.getMessage()));
                        }

                        UserBeanTwo userBeanTwo=new UserBeanTwo();
                        userBeanTwo.setEmail(account);
                        userBeanTwo.setToken(loginResponse1.getData().getToken());
                        return NetUtil.getInstance().getApi().loginWorkbench(userBeanTwo);

                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<LoginResponse2>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        disposableContainer.add(d);
                    }

                    @Override
                    public void onNext(LoginResponse2 loginBean) {

                        User user=new User(account,password,loginBean.getToken(),loginBean.getUid(),loginBean.getUrole() );
                        UserWrapper.getInstance().setUser(user);

                    }

                    @Override
                    public void onError(Throwable e) {
                        view.loginFail(e.getMessage());
                        e.printStackTrace();

                    }

                    @Override
                    public void onComplete() {
                        view.loginSuccess();
                    }
                });

    }

    @Override
    public void onDestroy() {
        disposableContainer.dispose();
        view = null;
    }
}
