package com.muxi.workbench.ui.login;

import android.text.TextUtils;
import android.util.Base64;
import android.widget.Toast;

import com.muxi.workbench.commonUtils.NetUtil;
import com.muxi.workbench.ui.login.model.LoginBean;
import com.muxi.workbench.ui.login.model.User;
import com.muxi.workbench.ui.login.model.UserWrapper;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.internal.disposables.DisposableContainer;
import io.reactivex.internal.disposables.ListCompositeDisposable;
import io.reactivex.schedulers.Schedulers;

public class LoginPresenter implements LoginContract.Presenter {

    private LoginContract.View view;
    private ListCompositeDisposable disposableContainer;
    public LoginPresenter(LoginContract.View view){
        this.view=view;
        disposableContainer=new ListCompositeDisposable();
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
        String json="{\n" +
                "  \"username\": \""+account+"\",\n" +
                "  \"password\": \""+encode+"\"\n" +
                "}";
        NetUtil.getInstance().getApi().login(json)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(v->{
                    view.showLoading();
                })
                .subscribe(new Observer<LoginBean>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        disposableContainer.add(d);
                    }

                    @Override
                    public void onNext(LoginBean loginBean) {
                        if (loginBean.getCode()!=0){
                            view.loginFail(loginBean.getMessage());
                            return;
                        }
                        User user=new User(account,password,loginBean.getData().getToken(),
                                loginBean.getData().getUser_id());
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
        view=null;
    }
}
