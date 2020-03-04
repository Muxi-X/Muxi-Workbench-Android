package com.muxi.workbench.commonUtils;

import com.muxi.workbench.commonUtils.DownLoadUtils.ProgressResponseBody;

import okhttp3.OkHttpClient;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class NetUtil {

    private OkHttpClient client;
    private RetrofitApi api;

    private NetUtil() {
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor()
                .setLevel(HttpLoggingInterceptor.Level.BODY);
        client= new OkHttpClient.Builder()
                .addInterceptor(interceptor)
                .addInterceptor(new AddTokenInterceptor("work.muxi-tech.xyz"))
                .build();

        api = new Retrofit.Builder()
                .client(client)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl("http://work.muxi-tech.xyz/api/v1.0/")
                .build()
                .create(RetrofitApi.class);
    }

    public static NetUtil getInstance() {
        return NetUtilHolder.INSTANCE;
    }

    private static class NetUtilHolder {
        private static NetUtil INSTANCE = new NetUtil();

    }

    public OkHttpClient getClient() {
        return client;
    }

    public RetrofitApi getApi() {
        return api;
    }
}
