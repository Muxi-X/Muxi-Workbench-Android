package com.muxi.workbench;

import android.app.Application;
import android.content.Context;

import com.facebook.drawee.backends.pipeline.Fresco;

public class MyApp extends Application {

    private static Context applicationContext;
    @Override
    public void onCreate() {
        super.onCreate();
        applicationContext=this.getApplicationContext();
        Fresco.initialize(this);
    }


    //一般不要随便使用,只在某些需要用到的全局方法里使用
    public static Context getAppContext(){
        return applicationContext;

    }

}
