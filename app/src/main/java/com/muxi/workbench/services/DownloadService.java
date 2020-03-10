package com.muxi.workbench.services;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;
import android.util.Log;
import android.widget.RemoteViews;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

import com.muxi.workbench.R;
import com.muxi.workbench.services.DownLoadUtils.DownloadAsyncTask;
import com.muxi.workbench.ui.mainControl.MainActivity;
import com.muxi.workbench.services.DownLoadUtils.DownloadAsyncTask.Status;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;

public class DownloadService extends Service {

    private int downloadNum=0;
    private static final int NOTIFICATION_ID=1;
    private static final int NOTIFICATION_FINISH=2;
    private static final String CLICK_ACTION="Notification_button_click";
    public static final String URL="URL";
    private DownloadAsyncTask.DownloadCallback mCallback;
    private LinkedList<DownloadAsyncTask> mTaskList;
    private RemoteViews mRemoteViews;
    private NotificationManager manager;
    private Notification notification;
    private MyReceiver receiver;
    private List<String>errorFile=new ArrayList<>();
    private final static String TAG="Service";


    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }


    @Override
    public void onCreate() {
        super.onCreate();
        mTaskList =new LinkedList<>();
        mCallback=new Callback();
        manager=(NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        registerBroadCast();
        //开启前台服务
        showOriginNotification();
        Log.i(TAG, "onCreate: ");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.i(TAG, "onStartCommand: ");
        String url=intent.getStringExtra(URL);
        DownloadAsyncTask task=new DownloadAsyncTask(url, mCallback,this);
        if (mTaskList.size()==0){
            if (notification==null){
                showOriginNotification();
            }
            mRemoteViews.setTextViewText(R.id.download_info,task.getFileNameFromUrl(task.getUrl()));
            manager.notify(NOTIFICATION_ID,notification);
            task.execute(url);
            mTaskList.add(task);
        }
        else {
            mTaskList.add(task);
            mRemoteViews.setTextViewText(R.id.task_num,"剩余任务："+(mTaskList.size()-1));
            manager.notify(NOTIFICATION_ID,notification);
        }
        return START_REDELIVER_INTENT;
    }


    private void registerBroadCast(){
        receiver=new MyReceiver();
        IntentFilter filter=new IntentFilter();
        filter.addAction(CLICK_ACTION);
        registerReceiver(receiver,filter);
    }
    private void showOriginNotification( ){
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, MainActivity.CHANNEL_ID);
        builder.setSmallIcon(R.drawable.workbench);
        mRemoteViews=new RemoteViews(this.getPackageName(),R.layout.notification_download);
        builder.setCustomContentView(mRemoteViews);
        builder.setAutoCancel(false);
        builder.setOngoing(true);
        builder.setOngoing(true);
        builder.setShowWhen(false);

        Intent intent=new Intent(CLICK_ACTION);
        PendingIntent pendingIntent=PendingIntent.getBroadcast(this,2,intent,0);
        mRemoteViews.setOnClickPendingIntent(R.id.pause,pendingIntent);

        notification = builder.build();
        startForeground(NOTIFICATION_ID,notification);
    }

    private void showFinishNotification(){
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, MainActivity.CHANNEL_ID);
        builder.setSmallIcon(R.drawable.workbench);
        builder.setAutoCancel(true);
        builder.setOngoing(false);
        builder.setShowWhen(true);
        builder.setContentTitle("完成下载");

        Notification notification=builder.build();
        manager.notify(NOTIFICATION_FINISH,notification);

    }

    private void showErrorNotification(){
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, MainActivity.CHANNEL_ID);
        builder.setSmallIcon(R.drawable.workbench);
        builder.setAutoCancel(true);
        builder.setOngoing(false);
        builder.setShowWhen(true);
        StringBuilder sb=new StringBuilder();
        sb.append(String.format(Locale.CHINESE,"%d个文件下载失败： \n",errorFile.size()));
        for (int i = 0; i < errorFile.size(); i++) {
            sb.append(errorFile.get(i)).append(',');
        }

        builder.setContentTitle(sb.toString());

        Notification notification=builder.build();
        manager.notify(NOTIFICATION_FINISH,notification);
    }

    @Override
    public void onDestroy() {
        Log.i(TAG, "onDestroy: ");
        if (!mTaskList.isEmpty()){
            mTaskList.getFirst().cancel(true);
        }
        if (receiver!=null){
            unregisterReceiver(receiver);
        }
        super.onDestroy();
    }



    private class Callback implements DownloadAsyncTask.DownloadCallback{


        @Override
        public void preExecute() {
            mRemoteViews.setProgressBar(R.id.download_progress,100,0,false);

        }

        @Override
        public void onPostExecute(DownloadAsyncTask.Status result) {
            if (result==Status.SUCCESS||result==Status.PAUSE){
                if (result!=Status.PAUSE)
                    mTaskList.removeFirst();
                if (!mTaskList.isEmpty()) {
                    DownloadAsyncTask task=mTaskList.getFirst();
                    task.execute(task.getUrl());
                    mRemoteViews.setTextViewText(R.id.download_info,task.getFileNameFromUrl(task.getUrl()));
                    mRemoteViews.setTextViewText(R.id.task_num,"剩余任务："+(mTaskList.size()-1));
                    manager.notify(NOTIFICATION_ID,notification);
                }else {
                    //已经全部完成,变后台服务
                    stopForeground(true);
                    notification=null;
                    if (errorFile.isEmpty()) {
                        showFinishNotification();
                    }else {
                        showErrorNotification();
                    }

                }

            }else {
                //error--->发送一个暂停给这个，当做是把这个出错的任务给暂停了，而这个pause只有这里会发出
                DownloadAsyncTask task=mTaskList.getFirst();
                mTaskList.removeFirst();
                errorFile.add(task.getFileNameFromUrl(task.getUrl()));
                this.onPostExecute(Status.PAUSE);


            }
        }

        @Override
        public void onProgressUpdate(Integer integer) {
            mRemoteViews.setProgressBar(R.id.download_progress,100,integer,false);
            manager.notify(NOTIFICATION_ID,notification);
        }

        @Override
        public void onError(Exception e) {

        }

        @Override
        public void onCancel(DownloadAsyncTask.Status status) {

        }
    }



    private class MyReceiver extends BroadcastReceiver{


        @Override
        public void onReceive(Context context, Intent intent) {
            if (mTaskList.isEmpty())return;
            if (mTaskList.getFirst().isCancelled()){
                mRemoteViews.setTextViewText(R.id.pause,"暂停");
                manager.notify(NOTIFICATION_ID,notification);
                String url=mTaskList.getFirst().getUrl();
                mTaskList.removeFirst();
                DownloadAsyncTask task=new DownloadAsyncTask(url,mCallback,DownloadService.this);
                mTaskList.addFirst(task);
                task.execute(url);

            }else {
                mTaskList.getFirst().cancel(true);
                mRemoteViews.setTextViewText(R.id.pause,"继续");
                manager.notify(NOTIFICATION_ID,notification);
            }
        }
    }

}
