package com.muxi.workbench.ui.progress.view.progressDetail;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.muxi.workbench.R;
import com.muxi.workbench.ui.progress.contract.ProgressDetailContract;
import com.muxi.workbench.ui.progress.model.Comment;
import com.muxi.workbench.ui.progress.model.Progress;
import com.muxi.workbench.ui.progress.model.progressDetail.ProgressDetailRemoteDataSource;
import com.muxi.workbench.ui.progress.model.progressDetail.ProgressDetailRepository;
import com.muxi.workbench.ui.progress.presenter.ProgressDetailPresenter;
import com.muxi.workbench.ui.progress.view.progressEditor.EditorActivity;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ProgressDetailActivity extends AppCompatActivity implements ProgressDetailContract.View, DialogFragmentDataCallback  {

    private ProgressDetailContract.Presenter mPresenter;
    private ProgressDetailListAdapter mAdapter;
    private Toolbar mToolbar;
    private RecyclerView mProgressDetailRv;
    private CommentSendView mCommentSendView;
    private String mAvatar = " ", mUsername = " ", mTitle = " ";
    private boolean mIfComment = false;
    private int mLikeCount=0;
    private boolean mIfLike=false;
    private Progress mProgress = new Progress(); // 当前进度
    private int mPosition = -1; //当前进度在ProgressList页的位置
    private int mSid = -1;

    ProgressDetailListAdapter.ProgressDetailListener mProgressDetailListener = new ProgressDetailListAdapter.ProgressDetailListener() {
        @Override
        public void onLikeClick() {
            if ( mProgress.getIfLike()==1 )
                mPresenter.setLikeProgress(false);
            else
                mPresenter.setLikeProgress(true);
        }

        @Override
        public void onEditClick() {
            Intent intent = EditorActivity.newIntent(ProgressDetailActivity.this,false,mProgress.getSid(), mProgress.getTitle() ,mProgress.getContent());
            startActivityForResult(intent, 2);
        }

        @Override
        public void onCommentClick() {
            CommentEditDialogFragment commentEditDialogFragment = CommentEditDialogFragment.newInstance(mCommentSendView.getContent(), mSid);
            commentEditDialogFragment.show(getSupportFragmentManager(), "CommentEditDialogFragment");
        }

        @Override
        public void onUserClick() {
            Toast.makeText(ProgressDetailActivity.this, "去个人主页", Toast.LENGTH_LONG).show();
            ///todo  去个人主页
        }

        @Override
        public void onDeleteCommentClick(int cid, int position) {
           mPresenter.deleteComment(mSid, cid, position);
        }
    };

    /**
     * 跳转到详情页intent的构建
     * @param packageContext   跳转来的页面
     * @param sid              当前进度的sid
     * @param username         当前进度所有者的昵称
     * @param avatar           当前进度所有者的头像
     * @param ifComment        是否获取评论框焦点 true-获取  false-不获取
     * @param position         点击位置，等待传回ProgressList做数据更新
     * @return
     */
    public static Intent newIntent(Context packageContext, int sid, String username, String avatar, boolean ifComment, String title, int position) {
        Intent intent = new Intent(packageContext, ProgressDetailActivity.class);
        intent.putExtra("sid", sid);
        intent.putExtra("avatar", avatar);
        intent.putExtra("username", username);
        intent.putExtra("ifComment", ifComment);
        intent.putExtra("position", position);
        intent.putExtra("title", title);
        return intent;
    }


    public static Intent newIntent(Context packageContext, int sid, String username, String avatar, boolean ifComment, String title, int position,int likeCount,boolean ifLike) {
        Intent intent = new Intent(packageContext, ProgressDetailActivity.class);
        intent.putExtra("sid", sid);
        intent.putExtra("avatar", avatar);
        intent.putExtra("username", username);
        intent.putExtra("ifComment", ifComment);
        intent.putExtra("position", position);
        intent.putExtra("title", title);
        intent.putExtra("likeCount",likeCount);
        intent.putExtra("ifLike",ifLike);
        return intent;
    }

    /**
     *
     * @param packageContext   跳转来的页面
     * @param progress         当前要显示的进度
     * @param ifComment        是否要评论
     * @param position         进度在列表页的位置，等待传回做数据更新
     * @return
     */
    public static Intent newIntent(Context packageContext,Progress progress, boolean ifComment, int position) {
        Intent intent = new Intent(packageContext, ProgressDetailActivity.class);
        intent.putExtra("progress", progress);
        intent.putExtra("ifComment", ifComment);
        intent.putExtra("position", position);
        return intent;
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_progress_detail);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);

        mPresenter = new ProgressDetailPresenter(this, ProgressDetailRepository.getInstance(ProgressDetailRemoteDataSource.getInstance()));

        Intent intent = this.getIntent();
        mIfComment = intent.getBooleanExtra("ifComment", false);
        mPosition = intent.getIntExtra("position", -1);
        mSid = intent.getIntExtra("sid", -1);
        mUsername = intent.getStringExtra("username");
        mAvatar = intent.getStringExtra("avatar");
        mTitle = intent.getStringExtra("title");
        mLikeCount=intent.getIntExtra("likeCount",0);
        mIfLike=intent.getBooleanExtra("ifLike",false);



        mAdapter = new ProgressDetailListAdapter(this, new Progress(), new ArrayList<Comment>(), mProgressDetailListener, mUsername);

        mProgressDetailRv = findViewById(R.id.rv_progressdetail);
        mProgressDetailRv.setLayoutManager(new LinearLayoutManager(this));
        mProgressDetailRv.setAdapter(mAdapter);

        mToolbar = findViewById(R.id.tb_progressdetail);
        mToolbar.setTitle(mTitle);
        setSupportActionBar(mToolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);//添加默认的返回图标
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    Intent backIntent = new Intent();
                backIntent.putExtra("position", mPosition);
                backIntent.putExtra("sid", mSid);
                backIntent.putExtra("avatar", mAvatar);
                backIntent.putExtra("username", mUsername);
              //  backIntent.putExtra("uid",mProgress.getUid());
                setResult(RESULT_OK, backIntent);
                finish();
            }
        });

        mCommentSendView = findViewById(R.id.csv_progressdetail);
        mCommentSendView.setBackgroundColor(Color.parseColor("#ffffff"));
        mCommentSendView.setElevation(35);
        mCommentSendView.setOnEditClickListener(v -> mProgressDetailListener.onCommentClick());
        mCommentSendView.setOnSendClickListener(v -> {
            if ( mCommentSendView.getContent() == null || mCommentSendView.getContent().length() == 0 ) {
                Toast.makeText(this, "请输入评论", Toast.LENGTH_LONG).show();
            } else {
                mPresenter.submitComment(mSid, mCommentSendView.getContent());
            }
        });

        mPresenter.start(mSid, mAvatar, mUsername,mLikeCount,mIfLike,mTitle);
        mPresenter.loadProgressAndCommentList();

        if ( mIfComment )
            showEditCommentView();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case 2:
                if (resultCode == RESULT_OK) {
                    mPresenter.loadProgressAndCommentList();
                }
                break;
        }
    }

    @Override
    public void setPresenter(ProgressDetailContract.Presenter presenter) {
        mPresenter = presenter;
        mPresenter.start(mSid, mAvatar, mUsername,mLikeCount,mIfLike,mTitle);
    }

    @Override
    public void refreshLike(boolean iflike) {
        mAdapter.refreshProgressLike(iflike);
    }

    @Override
    public void showEditCommentView() {
        CommentEditDialogFragment commentEditDialogFragment = CommentEditDialogFragment.newInstance(mCommentSendView.getContent(), mSid);
        commentEditDialogFragment.show(getSupportFragmentManager(), "CommentEditDialogFragment");
    }

    @Override
    public void clearCommentContent() {
        mCommentSendView.setContent("");
    }

    @Override
    public void showProgressDetail(Progress progress, List<Comment> commentList, int uid) {
        mProgress = progress;
        mAdapter.refresh(progress, commentList, uid);
    }

    @Override
    public void showError() {
        Toast.makeText(this, "失败了", Toast.LENGTH_LONG).show();
    }

    @Override
    public String getCommentText() {
        return mCommentSendView.getContent();
    }

    @Override
    public void setCommentText(String commentTextTemp) {
        mCommentSendView.setContent(commentTextTemp);
    }

    @Override
    public void submitComment(String comment) {
        mPresenter.submitComment(mSid, comment);
    }

    @Override
    public void deleteComment(int position) {
        mAdapter.deleteComment(position);
    }
}
