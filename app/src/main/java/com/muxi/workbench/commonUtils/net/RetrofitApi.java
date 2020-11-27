package com.muxi.workbench.commonUtils.net;

import com.muxi.workbench.ui.home.model.FeedBean;
import com.muxi.workbench.ui.login.model.netcall.OauthResponse;
import com.muxi.workbench.ui.login.model.netcall.LoginResponse;
import com.muxi.workbench.ui.login.model.netcall.OauthUserBean;
import com.muxi.workbench.ui.login.model.netcall.LoginUserBean;
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

    String BASE_URL = "http://work.test.muxi-tech.xyz/api/v1";
    String OAUTH_URL = "http://pass.muxi-tech.xyz/auth/api/oauth";
    String LOGIN_URL = "/auth/login";


    @POST(OAUTH_URL)
    Observable<OauthResponse> loginOauth(@Query("response_type") String responseType, @Query("client_id") String clientId, @Body OauthUserBean oauthUserBean);

    @POST(BASE_URL+LOGIN_URL)
    Observable<LoginResponse> loginWorkbench(@Body LoginUserBean loginUserBean);

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
