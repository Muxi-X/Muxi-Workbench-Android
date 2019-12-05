package com.muxi.workbench.commonUtils;

import com.muxi.workbench.ui.login.model.LoginBean;
import com.muxi.workbench.ui.login.model.UserBean;

import io.reactivex.Observable;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface RetrofitApi {

    @POST("signin")
    Observable<LoginBean>login(@Body UserBean userBean);
}
