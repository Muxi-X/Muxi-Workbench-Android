package com.muxi.workbench.commonUtils;

import com.muxi.workbench.ui.home.model.FeedBean;
import com.muxi.workbench.ui.login.model.netcall.LoginResponse1;
import com.muxi.workbench.ui.login.model.netcall.LoginResponse2;
import com.muxi.workbench.ui.login.model.netcall.UserBean;
import com.muxi.workbench.ui.login.model.netcall.UserBeanTwo;
import com.muxi.workbench.ui.progress.model.net.CommentStautsBean;
import com.muxi.workbench.ui.progress.model.net.GetAStatusResponse;
import com.muxi.workbench.ui.progress.model.net.GetGroupUserListResponse;
import com.muxi.workbench.ui.progress.model.net.GetStatusListResponse;
import com.muxi.workbench.ui.progress.model.net.IfLikeStatusBean;
import com.muxi.workbench.ui.progress.model.net.LikeStatusResponse;

import io.reactivex.Observable;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.DELETE;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface RetrofitApi {

    @POST("http://pass.muxi-tech.xyz/auth/api/signin")
    Observable<LoginResponse1> loginFirst(@Body UserBean userBean);


    @POST("http://work.muxixyz.com/api/v1.0/auth/login/")
    Observable<LoginResponse2> loginWorkbench(@Body UserBeanTwo userBeanTwo);

    @Headers("Content-Type: application/json")
    @GET("feed/list/{page}/")
    Observable<FeedBean> getFeed(@Header("token") String token, @Path("page") int page);

    @GET("http://work.muxi-tech.xyz/api/v1.0/status/{sid}/")
    Observable<GetAStatusResponse> getAStatus(@Header("token") String token, @Path("sid") int sid);

    @DELETE("http://work.muxi-tech.xyz/api/v1.0/status/{sid}/")
    Observable<Response<Void>> deleteStatus(@Header("token") String token, @Path("sid") int sid);

    @GET("http://work.muxi-tech.xyz/api/v1.0/status/list/{page}/")
    Observable<GetStatusListResponse> getStatusList(@Header("token") String token, @Path("page") int page);

    @PUT("http://work.muxi-tech.xyz/api/v1.0/status/{sid}/like/")
    Observable<LikeStatusResponse> ifLikeStatus(@Header("token") String token, @Path("sid") int sid, @Body IfLikeStatusBean ifLikeStatusBean);

    @PUT("http://work.muxi-tech.xyz/api/v1.0/status/{sid}/comments/")
    Observable<Response<Void>> commentStatus(@Header("token") String token, @Path("sid") int sid, @Body CommentStautsBean commentStautsBean);

    @GET("http://work.muxi-tech.xyz/api/v1.0/group/{gid}/userList/")
    Observable<GetGroupUserListResponse> getGroupUserList(@Header("token") String token, @Path("gid") int gid);

}
