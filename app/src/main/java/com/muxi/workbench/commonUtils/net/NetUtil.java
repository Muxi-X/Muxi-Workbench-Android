package com.muxi.workbench.commonUtils.net;

import com.google.gson.Gson;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class NetUtil {

    private OkHttpClient client;
    private RetrofitApi api;
    private Gson gson;

    private NetUtil() {
        gson=new Gson();
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor()
                .setLevel(HttpLoggingInterceptor.Level.BODY);
        client= new OkHttpClient.Builder()
                .addInterceptor(interceptor)
                .addInterceptor(new AddTokenInterceptor("work.muxi-tech.xyz"))
                .build();

        api = new Retrofit.Builder()
                .client(client)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create(gson))
                .baseUrl("http://work.test.muxi-tech.xyz/api/v1/")
                .build()
                .create(RetrofitApi.class);
    }

    public Gson getGson(){
        return gson;
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
