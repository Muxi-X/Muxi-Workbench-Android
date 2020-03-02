package com.muxi.workbench.commonUtils.DownLoadUtils;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.muxi.workbench.commonUtils.SPUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.ref.WeakReference;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;

import com.muxi.workbench.commonUtils.DownLoadUtils.DownloadAsyncTask.Status;
public class DownloadAsyncTask extends AsyncTask<String, Integer,Status> {


    public enum Status{
        SUCCESS,PAUSE,ERROR
    }

    private static final String TAG="DownloadAsyncTaskTAG";
    private static final String DOWNEXT="WBdata";
    private WeakReference<DownloadCallback> mCallback;
    private WeakReference<Context>mContext;
    private SPUtils spUtils;
    private String url;
    private File file;
    private static OkHttpClient client= new OkHttpClient.Builder()
            .connectTimeout(Integer.MAX_VALUE, TimeUnit.MILLISECONDS)
            .readTimeout(Integer.MAX_VALUE,TimeUnit.MILLISECONDS)
            .writeTimeout(Integer.MAX_VALUE,TimeUnit.MILLISECONDS)
            .addInterceptor(new HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.HEADERS))
            .build();


    public DownloadAsyncTask(String url,DownloadCallback callback,Context context){
        this.mCallback=new WeakReference<>(callback);
        mContext=new WeakReference<>(context);
        spUtils=SPUtils.getInstance(SPUtils.SP_DOWNLOAD);
        this.url=url;
    }

    public String getUrl(){
        return url;
    }
    public String getFileNameFromUrl(String url) {
        if (url.lastIndexOf('/') + 1 >= url.length())
            return String.valueOf(System.currentTimeMillis()) ;
        else {
            return url.substring(url.lastIndexOf('/') + 1);

        }
    }
    public String changeFileExt(String name,String ext){

        int dot=name.lastIndexOf('.');
        if (dot==-1)
            return name+'.'+ext;
        else
            return name.substring(0,dot+1)+ext;
    }


    @Override
    protected Status doInBackground(String... strings) {



        String fileName = changeFileExt(getFileNameFromUrl(url), DOWNEXT);
        file=new File(mContext.get().getExternalFilesDir(null), fileName);
        long begin=0;



        //主要用来验证是否过期，与服务器的文件大小作比较
        long length = spUtils.getLong(fileName,-1L);

        InputStream in = null;
        FileOutputStream out = null;
        Response response=null;
        try {

            long serversLength=getRemoteFileLength(url);

            Log.i(TAG, "doInBackground: server  last"+serversLength+" "+length);
            if (file.exists()){
                begin=file.length();
                Log.i(TAG, "doInBackground: origin"+begin);

            }
            if (serversLength!=length){
                begin=0;
                length=serversLength;
                if (file.exists())
                    file.delete();
            }

            if (begin!=0){
                mCallback.get().onProgressUpdate((int)(((double)begin/length)*100));
            }
            //其实已经下载完成
            if (begin==length){
                return Status.SUCCESS;
            }

            Log.i(TAG, "doInBackground:  begin length"+begin);
            Request request=new Request.Builder()
                    .addHeader("Connection","close")
                    .addHeader("Range", "bytes=" +begin  + "-"+length)
                    .url(url)
                    .build();

            response=client.newCall(request).execute();


            out=new FileOutputStream(file,true);
            in=response.body().byteStream();

            byte[]bytes=new byte[1024*8];
            int n=in.read(bytes);
            long lastTime=System.currentTimeMillis();
            while (n!=-1){
                out.write(bytes,0,n);
                begin+=n;
                n=in.read(bytes);
                long now=System.currentTimeMillis();
                if (now-lastTime>=500) {
                    mCallback.get().onProgressUpdate((int) (((double) begin / length) * 100));
                    lastTime=now;
                }

                if (this.isCancelled()){
                    spUtils.put(fileName,length);
                    break;
                }

            }
            if (!this.isCancelled()){
                mCallback.get().onProgressUpdate(100);
            }

        } catch (IOException e) {
            spUtils.put(fileName,length);
            mCallback.get().onError(e);
            e.printStackTrace();

            return Status.ERROR;
        }finally {

            try {
                if (out!=null)
                out.close();
                if (in!=null)
                in.close();
                response.close();

            }catch (IOException e) {
                e.printStackTrace();
            }

        }


        return Status.SUCCESS;
    }




    @Override
    protected void onPreExecute() {
        mCallback.get().preExecute();
    }

    private long getRemoteFileLength(String url) throws IOException {


        Request request=new Request.Builder()
                .addHeader("Connection","close")
                .head()
                .url(url)
                .build();

        Log.i(TAG, "getRemoteFileLength:  time"+System.currentTimeMillis());

        Response response=client.newCall(request).execute();
        String length=response.header("content-length");

        InputStream in=response.body().byteStream();
        byte[]bytes=new byte[1024];
        int n=in.read(bytes);
        int begin=0;
        Log.i(TAG, "getRemoteFileLength: "+System.currentTimeMillis());
        while (n!=-1){
            begin+=n;
            n=in.read(bytes);

        }
        Log.i(TAG, "getRemoteFileLength: "+begin);
        Log.i(TAG, "getRemoteFileLength: len  "+length);
        response.close();
        return Long.parseLong(length==null?"-1":length);


    }

    /**
     *
     * doInBackground结束后调用，如果cancel，则不会调用
     * @param
     */
    @Override
    protected void onPostExecute(Status status) {
        if (status==Status.SUCCESS){
            String name=getFileNameFromUrl(url);
            file.renameTo(new File(mContext.get().getExternalFilesDir(null),name));
        }
        DownloadCallback callback=mCallback.get();
        if (callback!=null){
            callback.onPostExecute(status);
        }
    }

    @Override
    protected void onProgressUpdate(Integer... values) {
        super.onProgressUpdate(values);
    }

    @Override
    protected void onCancelled(Status status) {
        //记录暂停信息

        DownloadCallback callback=mCallback.get();
        if (callback!=null){
            callback.onCancel(status);
        }
    }

    public interface DownloadCallback {

        void preExecute();
        void onPostExecute(Status result);
        void onProgressUpdate(Integer integer);
        void onError(Exception e);
        void onCancel(Status status);
    }

    public static class DefaultCallback implements DownloadCallback {


        @Override
        public void preExecute() {
            Log.i(TAG, "preExecute: ");
        }

        @Override
        public void onPostExecute(Status result) {
            Log.i(TAG, "AfterTaskfinish: result"+result);
        }


        @Override
        public void onProgressUpdate(Integer integer) {
            Log.i(TAG, "onProgressUpdate: "+integer);
        }

        @Override
        public void onError(Exception e) {
            Log.i(TAG, "onError: ");
        }

        @Override
        public void onCancel(Status status) {

        }
    }

}
