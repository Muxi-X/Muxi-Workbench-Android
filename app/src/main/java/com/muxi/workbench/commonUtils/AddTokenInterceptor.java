package com.muxi.workbench.commonUtils;

import com.muxi.workbench.ui.login.model.UserWrapper;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

public class AddTokenInterceptor implements Interceptor {


    private UserWrapper userWrapper;
    private String host;
    public AddTokenInterceptor(String host){
        userWrapper=UserWrapper.getInstance();
        this.host=host;
    }
    @Override
    public Response intercept(Chain chain) throws IOException {

        Request.Builder builder=chain.request().newBuilder();

        if (!userWrapper.getToken().isEmpty()){
            if (chain.request().url().host().equals(host))
            builder.addHeader("token",userWrapper.getToken());
        }
        return chain.proceed(builder.build());
    }
}
