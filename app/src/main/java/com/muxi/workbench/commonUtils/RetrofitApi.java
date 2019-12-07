package com.muxi.workbench.commonUtils;

import com.muxi.workbench.ui.login.model.netcall.LoginResponse1;
import com.muxi.workbench.ui.login.model.netcall.LoginResponse2;
import com.muxi.workbench.ui.login.model.netcall.UserBean;
import com.muxi.workbench.ui.login.model.netcall.UserBeanTwo;

import io.reactivex.Observable;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface RetrofitApi {

    @POST("http://pass.muxi-tech.xyz/auth/api/signin")
    Observable<LoginResponse1> loginFirst(@Body UserBean userBean);


    @POST("http://work.muxixyz.com/api/v1.0/auth/login/")
    Observable<LoginResponse2> loginWorkbench(@Body UserBeanTwo userBeanTwo);

}
