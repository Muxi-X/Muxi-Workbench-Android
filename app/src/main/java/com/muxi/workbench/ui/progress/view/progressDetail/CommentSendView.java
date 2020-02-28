package com.muxi.workbench.ui.progress.view.progressDetail;

import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.muxi.workbench.R;

public class CommentSendView extends ConstraintLayout {

    private TextView mContentTv, mSendTv;
    private ConstraintLayout mConstraintLayout;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public CommentSendView(Context context) {
        super(context);
        initView(context);
    }

    @Override
    public void setElevation(float elevation) {
        super.setElevation(elevation);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public CommentSendView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public CommentSendView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void initView (Context context) {
        LayoutInflater.from(context).inflate(R.layout.view_comment_send_progressdetail, this, true);
        mSendTv = findViewById(R.id.tv_send_comment_send);
        mContentTv = findViewById(R.id.tv_content_comment_send);
        mConstraintLayout = findViewById(R.id.cl_comment_send);
        mConstraintLayout.setElevation(3);
    }

    public void setOnEditClickListener (OnClickListener onEditClickListener) {
        mContentTv.setOnClickListener(onEditClickListener);
    }

    public void setOnSendClickListener (OnClickListener onSendClickListener) {
        mSendTv.setOnClickListener(onSendClickListener);
    }

    public String getContent() {
        return mContentTv.getText().toString();
    }

    public void setContent(String content) {
        mContentTv.setText(content);
    }
}
