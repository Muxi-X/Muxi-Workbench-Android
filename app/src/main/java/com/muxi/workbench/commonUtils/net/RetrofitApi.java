package com.muxi.workbench.commonUtils.net;

import com.muxi.workbench.ui.home.model.FeedBean;
import com.muxi.workbench.ui.login.model.netcall.LoginResponse1;
import com.muxi.workbench.ui.login.model.netcall.LoginResponse2;
import com.muxi.workbench.ui.login.model.netcall.UserBean;
import com.muxi.workbench.ui.login.model.netcall.UserBeanTwo;
import com.muxi.workbench.ui.notifications.model.NotificationsResponse;
import com.muxi.workbench.ui.progress.model.net.CommentStautsBean;
import com.muxi.workbench.ui.progress.model.net.GetAStatusResponse;
import com.muxi.workbench.ui.progress.model.net.GetGroupUserListResponse;
import com.muxi.workbench.ui.progress.model.net.GetStatusListResponse;
import com.muxi.workbench.ui.progress.model.net.IfLikeStatusBean;
import com.muxi.workbench.ui.progress.model.net.LikeStatusResponse;
import com.muxi.workbench.ui.progress.model.net.PostNewStatusResponse;
import com.muxi.workbench.ui.progress.model.net.StatusBean;
import com.muxi.workbench.ui.project.model.bean.FileContent;
import com.muxi.workbench.ui.project.model.bean.FilesId;
import com.muxi.workbench.ui.project.model.bean.FilesResponse;
import com.muxi.workbench.ui.project.model.bean.Folder;
import com.muxi.workbench.ui.project.model.bean.Project;

import io.reactivex.Observable;
import retrofit2.Response;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.DELETE;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface RetrofitApi {

//    @POST("http://pass.muxi-tech.xyz/auth/api/signin")
//    Observable<LoginResponse1> loginFirst(@Body UserBean userBean);

    @POST("http://pass.muxi-tech.xyz/auth/api/oauth")
    Observable<LoginResponse1>  loginFirst(@Query("response_type") String response_type, @Query("client_id") String client_id, @Body UserBean userBean);

//    @POST("http://work.muxixyz.com/api/v1.0/auth/login/")
//    Observable<LoginResponse2> loginWorkbench(@Body UserBeanTwo userBeanTwo);

    @POST("http://work.test.muxi-tech.xyz/api/v1/auth/login")
    Observable<LoginResponse2> loginWorkbench(@Body UserBeanTwo userBeanTwo);

    @GET("feed/list/{page}/")
    Observable<FeedBean> getFeed(@Path("page") int page);

    @GET("http://work.muxi-tech.xyz/api/v1.0/status/{sid}/")
    Observable<GetAStatusResponse> getAStatus( @Path("sid") int sid);

    @PUT("http://work.muxi-tech.xyz/api/v1.0/status/{sid}/")
    Observable<Response<Void>> editStatus(@Header ("token") String token, @Path("sid") int sid, @Body StatusBean changeStatusBean);

    @POST("http://work.muxi-tech.xyz/api/v1.0/status/new/")
    Observable<PostNewStatusResponse> newStatus(@Header ("token") String token, @Body StatusBean changeStatusBean);

    @DELETE("http://work.muxi-tech.xyz/api/v1.0/status/{sid}/")
    Observable<Response<Void>> deleteStatus(@Path("sid") int sid);

    @GET("http://work.muxi-tech.xyz/api/v1.0/status/list/{page}/")
    Observable<GetStatusListResponse> getStatusList( @Path("page") int page);

    @PUT("http://work.muxi-tech.xyz/api/v1.0/status/{sid}/like/")
    Observable<LikeStatusResponse> ifLikeStatus(@Path("sid") int sid, @Body IfLikeStatusBean ifLikeStatusBean);

    @POST("http://work.muxi-tech.xyz/api/v1.0/status/{sid}/comments/")
    Observable<Response<Void>> commentStatus( @Path("sid") int sid, @Body CommentStautsBean commentStautsBean);

    @GET("http://work.muxi-tech.xyz/api/v1.0/group/{gid}/userList/")
    Observable<GetGroupUserListResponse> getGroupUserList( @Path("gid") int gid);

    @DELETE("http://work.muxi-tech.xyz/api/v1.0/status/{sid}/comment/{cid}/")
    Observable<Response<Void>> deleteComment( @Path("sid") int sid, @Path("cid") int cid);

    @GET("http://work.muxi-tech.xyz/api/v1.0/message/list/")
    Observable<NotificationsResponse> getNotifications( @Query("page") int page);


    @GET("user/{uid}/project/list/")
    Observable<Project>getProject(@Path("uid")int uid,@Query("page")int page);

    @GET("folder/doctree/{id}/")
    Observable<Folder>getDoctree(@Path("id")int id);

    @GET("folder/filetree/{id}/")
    Observable<Folder>getFiletree(@Path("id")int id);

    @POST("folder/file/children/")
    Observable<FilesResponse>getFileDetail(@Body FilesId filesId);

    @POST("folder/doc/children/")
    Observable<FilesResponse>getDocDetail(@Body FilesId filesId);


    @GET("file/doc/{id}")
    Observable<FileContent>getFileContent(@Path("id")int id);
}
