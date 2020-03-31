package com.muxi.workbench.ui.project.view.projectFolder;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.muxi.workbench.R;
import com.muxi.workbench.commonUtils.net.NetUtil;
import com.muxi.workbench.ui.project.model.bean.FileContent;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class ProjectDocWebView extends AppCompatActivity {


    private WebView mWebView;
    private Toolbar mToolbar;
    private int id;
    private String fileName;
    private Disposable disposable;
    public static final String DOCID="file_id";
    public static final String DOCNAME="file_name";
    private static final String FILEHTML="file:///android_asset/ProjectDoc.html";
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_project_doc_webview);
        init();
        mWebView.getSettings().setJavaScriptEnabled(true);
        mWebView.setWebViewClient(new WebViewClient(){

            @Override
            public void onPageFinished(WebView view, String url) {
                if (url.equals(FILEHTML)) {
                    getContent();
                }
            }


            @Override
            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                //调用手机浏览器打开
                Uri uri = null;
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
                    uri = Uri.parse(request.getUrl().toString());
                    Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                    startActivity(intent);
                    return true;
                }
                else
                    return super.shouldOverrideUrlLoading(view, request);
            }
        });
        mWebView.loadUrl(FILEHTML);



    }

    private void init(){
        mWebView=findViewById(R.id.doc_webview);
        mToolbar=findViewById(R.id.project_webview_title_bar);
        id=getIntent().getIntExtra(DOCID,0);
        fileName=getIntent().getStringExtra(DOCNAME);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            mToolbar.setTitle(fileName);
        }

    }

    private void getContent(){
        NetUtil.getInstance().getApi()
                .getFileContent(id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<FileContent>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        disposable=d;
                    }

                    @Override
                    public void onNext(FileContent fileContent) {
                        mWebView.loadUrl("javascript:loadContent('" + fileContent.getContent() + " ');");
                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                        Toast.makeText(ProjectDocWebView.this,"加载失败",Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void onComplete() {

                    }
                });

    }

    public static Intent newIntent(Context context, int id, String name){
        Intent intent=new Intent(context,ProjectDocWebView.class);
        intent.putExtra(DOCID,id);
        intent.putExtra(DOCNAME,name);
        return intent;

    }
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (mWebView.canGoBack()) {
                mWebView.goBack();
                return true;
            }
            onBackPressed();
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onDestroy() {
        if (disposable!=null){
            disposable.dispose();;
        }
        Log.i("webview", "onDestroy: ");
        super.onDestroy();
    }
}
