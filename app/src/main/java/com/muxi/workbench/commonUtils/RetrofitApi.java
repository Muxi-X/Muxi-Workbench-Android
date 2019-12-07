package com.muxi.workbench.commonUtils;

import com.muxi.workbench.ui.home.model.FeedBean;
import com.muxi.workbench.ui.login.model.LoginBean;
import com.muxi.workbench.ui.login.model.UserBean;

import io.reactivex.Observable;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface RetrofitApi {

    @POST("signin")
    Observable<LoginBean> login(@Body UserBean userBean);

    @GET("feed/list/{page}/")
    Observable<FeedBean> getFeed(@Header("token") String token, @Path("page") int page);
}
