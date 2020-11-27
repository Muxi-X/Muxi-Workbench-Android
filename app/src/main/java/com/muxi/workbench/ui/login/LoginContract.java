package com.muxi.workbench.ui.login;

import android.content.Context;

import com.muxi.workbench.ui.login.model.User;

public interface LoginContract {


    interface View{
        String getAccount();
        String getPassword();
        void showLoading();
        void loginFail(String meg);
        void loginSuccess();
        Context getContext();
    }

    interface Presenter{
        void login();
        void onDestroy();
    }

}
