package com.muxi.workbench.commonUtils.net;

import com.muxi.workbench.ui.home.model.FeedBean;
import com.muxi.workbench.ui.login.model.netcall.OauthResponse;
import com.muxi.workbench.ui.login.model.netcall.LoginResponse;
import com.muxi.workbench.ui.login.model.netcall.OauthUserBean;
import com.muxi.workbench.ui.login.model.netcall.LoginUserBean;
import com.muxi.workbench.ui.notifications.model.NotificationsResponse;
import com.muxi.workbench.ui.progress.model.net.CommentStautsBean;
import com.muxi.workbench.ui.progress.model.net.DeleteCommentBean;
import com.muxi.workbench.ui.progress.model.net.GetAStatusResponse;
import com.muxi.workbench.ui.progress.model.net.GetCommentListResponse;
import com.muxi.workbench.ui.progress.model.net.GetGroupUserListResponse;
import com.muxi.workbench.ui.progress.model.net.GetStatusListResponse;
import com.muxi.workbench.ui.progress.model.net.IfLikeStatusBean;
import com.muxi.workbench.ui.progress.model.net.LikeStatusResponse;
import com.muxi.workbench.ui.progress.model.net.PostNewStatusResponse;
import com.muxi.workbench.ui.progress.model.net.StatusBean;
import com.muxi.workbench.ui.progress.model.net.StatusTitleBean;
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
    String DETAIL_URL = "/status/detail/{id}";
    String COMMENTS_URL = "/status/detail/{id}/comments";
    String STATUS_URL = "/status";
    String STATUS_LIKE_URL = "/status/like/{id}";
    String STATUS_COMMENT_URL = "/status/comment/{id}";


    @POST(OAUTH_URL)
    Observable<OauthResponse> loginOauth(@Query("response_type") String responseType, @Query("client_id") String clientId, @Body OauthUserBean oauthUserBean);

    @POST(BASE_URL+LOGIN_URL)
    Observable<LoginResponse> loginWorkbench(@Body LoginUserBean loginUserBean);

    @GET("feed/list/{page}/")
    Observable<FeedBean> getFeed(@Path("page") int page);

    @GET(BASE_URL+DETAIL_URL)
    Observable<GetAStatusResponse> getAStatus(@Header ("Authorization") String token,@Path("id") int id);

    @GET(BASE_URL+COMMENTS_URL)
    Observable<GetCommentListResponse> getCommentList(@Path("id") int id,@Header ("Authorization") String token);

    @PUT(BASE_URL+DETAIL_URL)
    Observable<Response<Void>> editStatus(@Header ("Authorization") String token, @Path("id") int id, @Body StatusBean changeStatusBean);

    @POST(BASE_URL+STATUS_URL)
    Observable<PostNewStatusResponse> newStatus( @Body StatusBean statusBean,@Header ("Authorization") String token);

    @DELETE(BASE_URL+DETAIL_URL)
    Observable<Response<Void>> deleteStatus(@Path("id") int id, @Header ("Authorization") String token,@Body StatusTitleBean statusTitleBean);

    @GET(BASE_URL+STATUS_URL)
    Observable<GetStatusListResponse> getStatusList( @Header ("Authorization") String token);

    @PUT(BASE_URL+STATUS_LIKE_URL)
    Observable<LikeStatusResponse> ifLikeStatus(@Header ("Authorization") String token,@Path("id") int id, @Body IfLikeStatusBean ifLikeStatusBean);

    @POST(BASE_URL+STATUS_COMMENT_URL)
    Observable<Response<Void>> commentStatus( @Path("id") int id, @Body CommentStautsBean commentStautsBean,@Header ("Authorization") String token);

    @GET("http://work.muxi-tech.xyz/api/v1.0/group/{gid}/userList/")
    Observable<GetGroupUserListResponse> getGroupUserList( @Path("gid") int gid);

    @DELETE(BASE_URL+STATUS_COMMENT_URL)
    Observable<Response<Void>> deleteComment(@Path("id") int id, @Header ("Authorization") String token, @Body DeleteCommentBean deleteCommentBean);

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
