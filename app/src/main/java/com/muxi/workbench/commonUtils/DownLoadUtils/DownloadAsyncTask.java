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
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

import javax.net.ssl.HttpsURLConnection;

import okhttp3.Call;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.HttpException;

public class DownloadAsyncTask extends AsyncTask<String,Integer,Integer> {

    private static final String TAG="DownloadAsyncTaskTAG";
    private static final Integer SUCCESS=1;
    private static final Integer PAUSE=0;
    private static final Integer ERROR=-1;
    private static final String DOWNEXT="WBdata";
    private String fileName;
    private WeakReference<DownloadCallback> mCallback;
    private WeakReference<Context>mContext;
    private SPUtils spUtils;
    private String url;



    public DownloadAsyncTask(DownloadCallback callback,Context context){
        this.mCallback=new WeakReference<>(callback);
        mContext=new WeakReference<>(context);
        spUtils=SPUtils.getInstance(SPUtils.SP_DOWNLOAD);

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
    protected Integer doInBackground(String... strings) {

        url=strings[0];
        fileName=changeFileExt(getFileNameFromUrl(url),DOWNEXT);
        File file=new File(mContext.get().getExternalFilesDir(null),fileName);
        long begin=0;


        URL Url;
        HttpURLConnection connection = null;
        //主要用来验证是否过期，与服务器的文件大小作比较
        long length = spUtils.getLong(fileName,-1L);

        InputStream in = null;
        FileOutputStream out = null;
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

            Log.i(TAG, "doInBackground:  begin length"+begin);

            Url=new URL(url);

            connection=(HttpURLConnection)Url.openConnection();
            connection.setRequestMethod("GET");
            connection.setRequestProperty("Range", "bytes=" + begin + "-"+length );
            connection.setRequestProperty("Connection","close");

            out=new FileOutputStream(file,true);
            in=Url.openStream();
            Log.i(TAG, "doInBackground: network     "+connection.getResponseCode());
            Log.i(TAG, "doInBackground: network   "+connection.getHeaderField("content-length"));
            byte[]bytes=new byte[1024*8];
            int n=in.read(bytes);
            while (n!=-1){
                out.write(bytes,0,n);
                begin+=n;
                n=in.read(bytes);
                mCallback.get().onProgressUpdate((int)(((double)begin/length)*100));
                Log.i(TAG, "doInBackground: now  "+begin);
                if (this.isCancelled()){
                    spUtils.put(fileName,length);
                    break;
                }

            }

        } catch (IOException e) {
            spUtils.put(fileName,length);
            mCallback.get().onError(e);
            e.printStackTrace();

            return ERROR;
        }finally {

            try {
                if (out!=null)
                out.close();
                if (in!=null)
                in.close();
                if (connection!=null){
                    connection.disconnect();
                }
            }catch (IOException e) {
                e.printStackTrace();
            }

        }


        if (this.isCancelled())
            return PAUSE;
        else
            return SUCCESS;
    }




    @Override
    protected void onPreExecute() {

    }
    private long getRemoteFileLength2(String url) throws IOException {

        URL Url;
        HttpURLConnection connection = null;
        Url=new URL(url);
        InputStream in = null;

        connection=(HttpURLConnection)Url.openConnection();
        connection.setRequestMethod("GET");
        connection.setRequestProperty("Range", "bytes=" + 100000 + "-"+200000 );
        connection.setRequestProperty("Connection","close");
        Log.i(TAG, "doInBackground: network     "+connection.getResponseCode());
        Log.i(TAG, "doInBackground: network   "+connection.getHeaderField("content-length"));
        in=Url.openStream();
        byte[]bytes=new byte[1024];
        int n=in.read(bytes);
        int begin=0;
        while (n!=-1){
            begin+=n;
            n=in.read(bytes);

        }
        Log.i(TAG, "getRemoteFileLength: "+begin);

        in.close();
        connection.disconnect();
        return 0;
    }

    private long getRemoteFileLength(String url) throws IOException {
        Request request=new Request.Builder()
                .addHeader("Connection","close")
                .addHeader("Range", "bytes=" +100000  + "-"+200000)
                .url(url)
                .build();

        Response response=NetUtil.getInstance().getClient()
                .newCall(request).execute();
        String length=response.header("content-length");

        InputStream in=response.body().byteStream();
        byte[]bytes=new byte[1024];
        int n=in.read(bytes);
        int begin=0;
        while (n!=-1){
            begin+=n;
            n=in.read(bytes);

        }
        Log.i(TAG, "getRemoteFileLength: "+begin);
        Log.i(TAG, "getRemoteFileLength: len  "+length);
        response.close();
        return Long.valueOf(length==null?"-1":length);


    }

    /**
     *
     * doInBackground结束后调用，如果cancel，则不会调用
     * @param aBoolean
     */
    @Override
    protected void onPostExecute(Integer aBoolean) {
        if (aBoolean.equals(SUCCESS)){
            String name=getFileNameFromUrl(url);
        }
        DownloadCallback callback=mCallback.get();
        if (callback!=null){
            callback.onPostExecute(aBoolean);
        }
    }

    @Override
    protected void onProgressUpdate(Integer... values) {
        super.onProgressUpdate(values);
    }

    @Override
    protected void onCancelled(Integer aBoolean) {
        //记录暂停信息

        DownloadCallback callback=mCallback.get();
        if (callback!=null){
            callback.onCancel(aBoolean);
        }
    }

    interface DownloadCallback {

        void preExecute();
        void onPostExecute(Integer result);
        void onCancel(Integer result);
        void onProgressUpdate(Integer integer);
        void onError(Exception e);

    }

    public static class DefaultCallback implements DownloadCallback {


        @Override
        public void preExecute() {
            Log.i(TAG, "preExecute: ");
        }

        @Override
        public void onPostExecute(Integer result) {
            Log.i(TAG, "AfterTaskfinish: result"+result);
        }

        @Override
        public void onCancel(Integer result) {
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
