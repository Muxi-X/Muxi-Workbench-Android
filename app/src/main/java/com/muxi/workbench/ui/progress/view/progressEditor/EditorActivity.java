package com.muxi.workbench.ui.progress.view.progressEditor;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ClipData;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.webkit.JavascriptInterface;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.muxi.workbench.R;
import com.muxi.workbench.ui.login.model.UserWrapper;
import com.muxi.workbench.ui.progress.contract.ProgressEditorContract;
import com.muxi.workbench.ui.progress.model.progressEditor.ProgressEditorRemoteDataSource;
import com.muxi.workbench.ui.progress.presenter.ProgressEditorPresenter;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Objects;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.BiFunction;

public class EditorActivity extends AppCompatActivity implements ProgressEditorContract.View {

    private Toolbar mToolbar;
    private WebView mEditWv;
    private ProgressEditorContract.Presenter mPresenter;
    private boolean ifNew = true;
    private String title = "";
    private String content = "";
    private int mSid = 0;

    /**
     * 用于新建的intent创建方法,通过这个方法创建后,mSid=0, title="",content=""
     * @param context
     * @param ifNew      是否是新建(true-新建; false-修改)
     * @return
     */
    public static Intent newIntent(Context context, boolean ifNew) {
        Intent intent = new Intent(context, EditorActivity.class);
        intent.putExtra("ifNew", ifNew);
        return intent;
    }

    /**
     * 用于修改的intent创建方法
     * @param context                 开启编辑页的context
     * @param ifNew                   是否是新建 (true-新建; false-修改)
     * @param sid                     要编辑的进度id
     * @param originalProgressTitle   要编辑的进度标题
     * @param originalProgressContent 要编辑的进度内容
     * @return
     */
    public static Intent newIntent(Context context, boolean ifNew, int sid, String originalProgressTitle, String originalProgressContent) {
        Intent intent = new Intent(context, EditorActivity.class);
        intent.putExtra("ifNew", ifNew);
        intent.putExtra("sid", sid);
        intent.putExtra("originalProgressContent", originalProgressContent);
        intent.putExtra("originalProgressTitle", originalProgressTitle);
        return intent;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.editor_toolbar_menu, menu);
        return true;
    }

    //发送进度前调用js方法获取标题和内容并进行转换
    private void sendProgress() {
        //调用js方法会在子线程
        Observable<String> observableTitle = Observable.create(new ObservableOnSubscribe<String>() {
            @Override
            public void subscribe(ObservableEmitter<String> observableEmitter) throws Exception {
                mEditWv.evaluateJavascript("javascript:this.getTitle()",value -> {
                    if ( value == null )
                        observableEmitter.onError(new Throwable("title is null"));
                    else {
                        observableEmitter.onNext(value.substring(1,value.length()-1));
                        observableEmitter.onComplete();
                    }
                });
            }
        });
        Observable<String[]> observableContent = Observable.create(new ObservableOnSubscribe<String[]>() {
            @Override
            public void subscribe(ObservableEmitter<String[]> observableEmitter) throws Exception {
                mEditWv.evaluateJavascript("javascript:this.getContent()",value -> {
                    if ( value == null )
                        observableEmitter.onError(new Throwable("content is null"));
                    else {
                        observableEmitter.onNext(new String[]{value.substring(1,value.length()-1)});
                        observableEmitter.onComplete();
                    }
                });
            }
        });
        Observable.zip(observableTitle, observableContent, new BiFunction<String, String[], String[]>() {
            @Override
            public String[] apply(String s, String[] strings) throws Exception {
                return new String[]{s,strings[0]};
            }
        }).subscribe(new Observer<String[]>() {
            @Override
            public void onSubscribe(Disposable disposable) {
            }

            @Override
            public void onNext(String[] strings) {
                //如果未编辑 content会是空字符
                if ( !content.equals("") && strings[1].equals("") )
                    strings[1] = content;
                //unicode转换
                strings[1] = strings[1].replaceAll("\\\\u003C", "<");
                strings[1] = strings[1].replaceAll("\\\\n", "\n");
                //判断标题 内容是否为空
                if ( strings[0].equals("") ) {
                    Toast.makeText(EditorActivity.this, "请输入标题", Toast.LENGTH_SHORT).show();
                } else if ( strings[1].equals("") ) {
                    Toast.makeText(EditorActivity.this, "请输入进度内容", Toast.LENGTH_SHORT).show();
                } else {
                    //请求
                    if (ifNew) {
                        mPresenter.newProgress(strings[0], strings[1]);
                    } else {
                        mPresenter.changeProgress(mSid, strings[0], strings[1]);
                    }
                }
            }

            @Override
            public void onError(Throwable throwable) {
                Log.e("getProgressError", Objects.requireNonNull(throwable.getMessage()));
                showError();
            }

            @Override
            public void onComplete() {

            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.editor_send: { //点击发送按钮
                sendProgress();
            }
            default:
                break;
        }
        return true;
    }

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editor);

        Log.e("editor","oncreate");
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);

        mPresenter = new ProgressEditorPresenter(this, ProgressEditorRemoteDataSource.getInstance());

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) //状态栏设置
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);

        Intent intent = getIntent();
        if ( !intent.getBooleanExtra("ifNew", true) ) {
            title = intent.getStringExtra("originalProgressTitle");
            content = intent.getStringExtra("originalProgressContent");
            ifNew = intent.getBooleanExtra("ifNew", true);
            mSid = intent.getIntExtra("sid", 0);
        } else {
            ifNew = intent.getBooleanExtra("ifNew", true);
            title = "";
            content = "";
            mSid = 0;
        }

        setWebView();

        mToolbar = findViewById(R.id.tb_editor);
        mToolbar.setTitle("");
        setSupportActionBar(mToolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);//添加默认的返回图标
        mToolbar.setNavigationOnClickListener(v -> {
            finish();
        });

        //请求读取存储的权限
        if (ContextCompat.checkSelfPermission(this,Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) //权限没有被允许
                ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},1);

    }

    private void setWebView() {
        mEditWv = findViewById(R.id.wv_editor);
        mEditWv.post(new Runnable() {
            @Override
            public void run() {
                try {//跨域设置
                    Class<?> clazz = mEditWv.getSettings().getClass();
                    Method method = clazz.getMethod(
                            "setAllowUniversalAccessFromFileURLs", boolean.class);//利用反射机制去修改设置对象
                    if (method != null) {
                        method.invoke(mEditWv.getSettings(), true);//修改设置
                    }
                } catch (IllegalArgumentException e) {
                    e.printStackTrace();
                } catch (NoSuchMethodException e) {
                    e.printStackTrace();
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                } catch (InvocationTargetException e) {
                    e.printStackTrace();
                }
                WebSettings webSettings = mEditWv.getSettings();
                webSettings.setDomStorageEnabled(true);
                webSettings.setJavaScriptEnabled(true);
                mEditWv.loadUrl("file:///android_asset/build/index.html");
                mEditWv.addJavascriptInterface(new JavascriptIteration(), "android");//js调用android方法设置
                mEditWv.setWebContentsDebuggingEnabled(true);
                mEditWv.setWebChromeClient(new MyWebChromeClient());//设置打开相册
            }
        });
    }

    @Override
    public void setPresenter(ProgressEditorContract.Presenter presenter) {
        mPresenter = presenter;
        presenter.start();
    }

    @Override
    public void back() {
        setResult(RESULT_OK, new Intent());
        finish();
    }

    @Override
    public void showError() {
        Toast.makeText(this,"出现错误了", Toast.LENGTH_LONG).show();
    }

    //js调用的方法类
    class JavascriptIteration {
        //获取token 用于图片上传的请求
        @JavascriptInterface
        public String getToken() {
            return UserWrapper.getInstance().getToken();
        }
        //获取内容
        @JavascriptInterface
        public String getEdits() {
            return content;
        }
        //获取标题
        @JavascriptInterface
        public String getStatusTitle() {
            return title;
        }
    }

    private ValueCallback<Uri> mUploadMessage;
    private ValueCallback<Uri[]> mUploadCallbackAboveL;
    private final static int FILECHOOSER_RESULTCODE = 101;
    private String mTag = "MyWebChromeClient";

    //用于webview调用相册
    class MyWebChromeClient extends WebChromeClient {

        public void openFileChooser(ValueCallback<Uri> uploadMsg) {
            Log.d(mTag, "openFileChoose(ValueCallback<Uri> uploadMsg)");
            mUploadMessage = uploadMsg;
            Intent i = new Intent(Intent.ACTION_GET_CONTENT);
            i.addCategory(Intent.CATEGORY_OPENABLE);
            i.setType("image/*");
            EditorActivity.this.startActivityForResult(Intent.createChooser(i, "File Chooser"),
                    FILECHOOSER_RESULTCODE);
        }

        public void openFileChooser(ValueCallback uploadMsg, String acceptType) {
            Log.d(mTag, "openFileChoose( ValueCallback uploadMsg, String acceptType )");
            mUploadMessage = uploadMsg;
            Intent i = new Intent(Intent.ACTION_GET_CONTENT);
            i.addCategory(Intent.CATEGORY_OPENABLE);
            i.setType("image/*");
            EditorActivity.this.startActivityForResult(
                    Intent.createChooser(i, "File Browser"),
                    FILECHOOSER_RESULTCODE);
        }
        public void openFileChooser(ValueCallback<Uri> uploadMsg, String acceptType, String capture) {
            Log.d(mTag, "openFileChoose(ValueCallback<Uri> uploadMsg, String acceptType, String capture)");
            mUploadMessage = uploadMsg;
            Intent i = new Intent(Intent.ACTION_GET_CONTENT);
            i.addCategory(Intent.CATEGORY_OPENABLE);
            i.setType("image/*");
            EditorActivity.this.startActivityForResult(Intent.createChooser(i, "File Browser"),
                    FILECHOOSER_RESULTCODE);
        }
        @Override
        public boolean onShowFileChooser(WebView webView, ValueCallback<Uri[]> filePathCallback, FileChooserParams fileChooserParams) {
            mUploadCallbackAboveL = filePathCallback;
            Intent i = new Intent(Intent.ACTION_GET_CONTENT);
            i.addCategory(Intent.CATEGORY_OPENABLE);
            i.setType("image/*");
            EditorActivity.this.startActivityForResult(Intent.createChooser(i, "File Browser"), FILECHOOSER_RESULTCODE);
            return true;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == FILECHOOSER_RESULTCODE) {
            if (null == mUploadMessage && null == mUploadCallbackAboveL) return;
            Uri result = data == null || resultCode != RESULT_OK ? null : data.getData();
            if (mUploadCallbackAboveL != null) {
                onActivityResultAboveL(requestCode, resultCode, data);
            } else if (mUploadMessage != null) {
                mUploadMessage.onReceiveValue(result);
                mUploadMessage = null;
            }
        }
    }

    private void onActivityResultAboveL(int requestCode, int resultCode, Intent data) {
        if (requestCode != FILECHOOSER_RESULTCODE || mUploadCallbackAboveL == null)
            return;
        Uri[] results = null;
        if (resultCode == Activity.RESULT_OK) {
            if (data == null) {
            } else {
                String dataString = data.getDataString();
                ClipData clipData = data.getClipData();
                if (clipData != null) {
                    results = new Uri[clipData.getItemCount()];
                    for (int i = 0; i < clipData.getItemCount(); i++) {
                        ClipData.Item item = clipData.getItemAt(i);
                        results[i] = item.getUri();
                        Log.e(mTag, "onActivityResultAboveL: " + results[i].getPath());
                    }
                }
                if (dataString != null)
                    results = new Uri[]{Uri.parse(dataString)};
                Log.e(mTag, "onActivityResultAboveL: " + results.length);
            }
        }
        mUploadCallbackAboveL.onReceiveValue(results);
        mUploadCallbackAboveL = null;
        return;
    }


}
