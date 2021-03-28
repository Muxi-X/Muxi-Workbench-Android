package com.muxi.workbench.ui.login;

import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.widget.ListAdapter;
import android.widget.Toast;

import com.muxi.workbench.commonUtils.net.NetUtil;
import com.muxi.workbench.ui.login.model.netcall.OauthResponse;
import com.muxi.workbench.ui.login.model.netcall.LoginResponse;
import com.muxi.workbench.ui.login.model.User;
import com.muxi.workbench.ui.login.model.netcall.OauthUserBean;
import com.muxi.workbench.ui.login.model.netcall.LoginUserBean;
import com.muxi.workbench.ui.login.model.UserWrapper;

import org.reactivestreams.Subscription;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Function;
import io.reactivex.internal.disposables.ListCompositeDisposable;
import io.reactivex.schedulers.Schedulers;
import okhttp3.Response;

public class LoginPresenter implements LoginContract.Presenter {

    private LoginContract.View view;
    private ListCompositeDisposable disposableContainer;
    private static final String RESPONSE_TYPE = "code";
    private static final String CLIENT_ID = "51f03389-2a18-4941-ba73-c85d08201d42";

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

        NetUtil.getInstance().getApi().loginOauth(RESPONSE_TYPE, CLIENT_ID,new OauthUserBean(account,encode))
                .subscribeOn(Schedulers.io())
                .doOnSubscribe(v->{
                    view.showLoading();
                })
                .flatMap(new Function<OauthResponse, ObservableSource<LoginResponse>>() {
                    @Override
                    public ObservableSource<LoginResponse> apply(OauthResponse oauthResponse) throws Exception {
                        if (oauthResponse.getCode()!=0){
                            return Observable.error(new Exception(oauthResponse.getMessage()));
                        }

                        LoginUserBean loginUserBean =new LoginUserBean();
                        loginUserBean.setOauth_code(oauthResponse.getData().getCode());
                        return NetUtil.getInstance().getApi().loginWorkbench(loginUserBean);

                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<LoginResponse>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        disposableContainer.add(d);
                    }

                    @Override
                    public void onNext(LoginResponse loginBean) {

                        User user=new User(account,password,loginBean.getData().getToken());
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