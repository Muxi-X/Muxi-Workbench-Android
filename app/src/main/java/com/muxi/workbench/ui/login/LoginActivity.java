package com.muxi.workbench.ui.login;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.muxi.workbench.R;
import com.muxi.workbench.ui.login.model.User;
import com.muxi.workbench.ui.login.model.UserWrapper;
import com.muxi.workbench.ui.mainControl.MainActivity;

public class LoginActivity extends AppCompatActivity implements LoginContract.View{

    private LoginContract.Presenter presenter;
    private EditText mEtAccount;
    private EditText mEtPassword;
    private Button mBtLogin;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        if (UserWrapper.getInstance().getUser()!=null){
            startActivity(new Intent(this, MainActivity.class));
            this.finish();
            return;
        }
        initView();
        presenter=new LoginPresenter(this);



    }

    public void  initView(){
        mEtAccount=findViewById(R.id.et_account);
        mEtPassword=findViewById(R.id.et_password);
        mBtLogin=findViewById(R.id.bt_login);

        mBtLogin.setOnClickListener(v -> {
            if (presenter!=null)
                presenter.login();

        });

    }
    @Override
    public String getAccount() {
        return mEtAccount.getText().toString();
    }

    @Override
    public String getPassword() {
        return mEtPassword.getText().toString();
    }

    @Override
    public void showLoading() {
        mBtLogin.setText("登录中...");
        mBtLogin.setClickable(false);
    }

    @Override
    public void loginFail(String meg) {
        mBtLogin.setClickable(true);
        mBtLogin.setText("登录");
        Toast.makeText(this,meg,Toast.LENGTH_LONG).show();
    }

    @Override
    public void loginSuccess() {
        mBtLogin.setClickable(true);
        Toast.makeText(getApplicationContext(),"登录成功！",Toast.LENGTH_SHORT).show();
        startActivity(new Intent(this, MainActivity.class));
        this.finish();
    }

    @Override
    public Context getContext() {
        return this;
    }

    @Override
    protected void onDestroy() {
        if(presenter!=null)
            presenter.onDestroy();
        super.onDestroy();
    }
}
