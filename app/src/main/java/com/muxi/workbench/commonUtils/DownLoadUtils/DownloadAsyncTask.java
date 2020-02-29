package com.muxi.workbench.commonUtils.DownLoadUtils;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.muxi.workbench.commonUtils.NetUtil;
import com.muxi.workbench.commonUtils.SPUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.ref.WeakReference;
import java.net.HttpURLConnection;

import okhttp3.Call;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.HttpException;

public class DownloadAsyncTask extends AsyncTask<String,Integer,Boolean> {

    private static final String TAG="DownloadAsyncTaskTAG";


    private WeakReference<DownloadCallback> mCallback;
    private WeakReference<Context>mContext;
    private SPUtils spUtils;
    private long begin;
    private long total;
    private String fileName;


    public DownloadAsyncTask(DownloadCallback callback,Context context){
        this.mCallback=new WeakReference<>(callback);
        mContext=new WeakReference<>(context);
        spUtils=SPUtils.getInstance(SPUtils.SP_DOWNLOAD);

    }

    public String getFileNameFromUrl(String url){
        if (url.lastIndexOf('/')+1>=url.length())
            return String.valueOf(System.currentTimeMillis());
        else
            return url.substring(url.lastIndexOf('/')+1);
    }

    public void getFileInfo(String url){
        fileName=getFileNameFromUrl(url);
        String info=spUtils.getString(fileName);
        if (info.length()==0)
            return;

        String[]infos=info.split("-");
        begin=Long.valueOf(infos[0]);
        total=Long.valueOf(infos[1]);

    }

    @Override
    protected Boolean doInBackground(String... strings) {

        String url=strings[0];
        getFileInfo(url);
        File file=new File(mContext.get().getExternalFilesDir(null),fileName);

        begin=file.length();
        long realLength=checkRemoteFile(url);

        //表示文件已经改变,或者文件被删除，都要重新下载
        if (!file.exists()||total!=realLength){
            begin=0;
            total=0;
            if (file.exists())
                file.delete();
        }



        if (begin!=0){
            mCallback.get().onProgressUpdate((int)(((double)begin/total)*100));
        }
        Log.i(TAG, "doInBackground: begin"+begin);
        Request request;
        OkHttpClient client;
        Response response = null;
        Log.i(TAG, "doInBackground: file path"+file.getPath());
        Log.i(TAG, "doInBackground: file.length()"+file.length());
        InputStream in = null;
        FileOutputStream out = null;
        if (total==0){
             request = new Request.Builder()
                    .url(url)
                    .header("RANGE", "bytes=" + begin + "-")
                    .build();
        }else {
            request = new Request.Builder()
                    .url(url)
                    .header("RANGE", "bytes=" + begin + "-"+total)
                    .build();
        }

        client= NetUtil.getInstance().getClient();
        try {
             response=client.newCall(request).execute();
             if (response.code()!= HttpURLConnection.HTTP_PARTIAL){
                 throw new Exception("服务端不支持断点续传");
             }

             total=response.body().contentLength();
             out=new FileOutputStream(file,true);
             in=response.body().byteStream();
             byte[]bytes=new byte[1024*16];
             int n=in.read(bytes);
             while (n!=-1){
                out.write(bytes,0,n);
                n=in.read(bytes);
                begin+=n;
                mCallback.get().onProgressUpdate((int)(((double)begin/total)*100));
                 Log.i(TAG, "doInBackground: "+begin);
                if (this.isCancelled()){
                    spUtils.put(fileName, String.valueOf(begin) + '-' + total);
                    mCallback.get().onCancel(begin==total);
                    break;
                }

            }



        } catch (Exception e) {
            mCallback.get().onError(e);
            e.printStackTrace();
            return false;
        }finally {
            try {
                out.close();
                in.close();
                response.close();
            }catch (IOException e) {
                e.printStackTrace();
            }

        }

        Log.i(TAG, "doInBackground: totlevsbegin"+total+"  "+begin);
        return true;
    }



    private long checkRemoteFile(String url) {
        Request request = new Request.Builder()
                .url(url)
                .build();
        try {
            okhttp3.Response response = NetUtil.getInstance().getClient().newCall(request).execute();
            if (response.isSuccessful()) {
                long contentLength = response.body().contentLength();
                response.close();
                return contentLength == 0 ? -1 : contentLength;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return -1;
    }

    @Override
    protected void onPreExecute() {
    }


    /**
     *
     * doInBackground结束后调用，如果cancel，则不会调用
     * @param aBoolean
     */
    @Override
    protected void onPostExecute(Boolean aBoolean) {
        DownloadCallback callback=mCallback.get();
        spUtils.put(fileName, String.valueOf(total) + '-' + total);
        if (callback!=null){
            callback.onPostExecute(aBoolean);
        }
    }

    @Override
    protected void onProgressUpdate(Integer... values) {
        super.onProgressUpdate(values);
    }

    @Override
    protected void onCancelled(Boolean aBoolean) {
        DownloadCallback callback=mCallback.get();
        if (callback!=null){
            callback.onCancel(aBoolean);
        }
    }

    interface DownloadCallback {

        void preExecute();
        void onPostExecute(Boolean result);
        void onCancel(Boolean result);
        void onProgressUpdate(Integer integer);
        void onError(Exception e);

    }

    public static class DefaultCallback implements DownloadCallback {
        @Override
        public void preExecute() {
            Log.i(TAG, "preExecute: ");
        }

        @Override
        public void onPostExecute(Boolean result) {
            Log.i(TAG, "AfterTaskfinish: result"+result);
        }

        @Override
        public void onCancel(Boolean result) {
            Log.i(TAG, "task Canceled: "+result);
        }

        @Override
        public void onProgressUpdate(Integer integer) {
            Log.i(TAG, "onProgressUpdate: "+integer);
        }

        @Override
        public void onError(Exception e) {
            Log.i(TAG, "onError: ");
        }
    }

}
