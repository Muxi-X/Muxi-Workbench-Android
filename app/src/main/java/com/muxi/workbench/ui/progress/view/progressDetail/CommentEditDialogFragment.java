package com.muxi.workbench.ui.progress.view.progressDetail;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;

import androidx.fragment.app.DialogFragment;

import com.muxi.workbench.R;

import java.util.Timer;
import java.util.TimerTask;

public class CommentEditDialogFragment extends DialogFragment {

    private String comment;
    private int sid;
    private EditText mCommentEt;
    private TextView mSendTv;
    private DialogFragmentDataCallback dialogFragmentDataCallback;

    public static CommentEditDialogFragment newInstance(String comment, int sid) {
        Bundle args = new Bundle();
        args.putString("comment", comment);
        args.putInt("sid", sid);
        CommentEditDialogFragment fragment = new CommentEditDialogFragment();
        fragment.setArguments(args);
        return fragment;
    }


    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.view_comment_edit_progressdetail, container, false);
        initView(view);

        dialogFragmentDataCallback = (DialogFragmentDataCallback) getActivity();
        // 回调获取评论条文本内容并填充至输入框中
        mCommentEt.setText(dialogFragmentDataCallback.getCommentText());
        comment = getArguments().getString("comment");
        sid = getArguments().getInt("sid");

        mCommentEt.setSelection(dialogFragmentDataCallback.getCommentText().length());//将光标移至文字末尾

        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                InputMethodManager manager =(InputMethodManager)mCommentEt.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                manager.showSoftInput(mCommentEt,0);
            }
        },500);

        return view;
    }

    public void onStart() {
        super.onStart();
        Window win = getDialog().getWindow();
        // 一定要设置Background，如果不设置，window属性设置无效
        win.setBackgroundDrawable( new ColorDrawable(getResources().getColor(R.color.colorAccent)));

        DisplayMetrics dm = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(dm);

        WindowManager.LayoutParams params = win.getAttributes();
        params.gravity = Gravity.BOTTOM;
        // 使用ViewGroup.LayoutParams，以便Dialog 宽度充满整个屏幕
        params.width =  ViewGroup.LayoutParams.MATCH_PARENT;
        params.height = ViewGroup.LayoutParams.WRAP_CONTENT;
        win.setAttributes(params);
    }

    private void initView(View view) {
        mCommentEt = view.findViewById(R.id.et_comment_edit);
        mSendTv = view.findViewById(R.id.tv_comment_edit);

        mSendTv.setClickable(true);
        mSendTv.setOnClickListener(v -> {
            dialogFragmentDataCallback.submitComment(mCommentEt.getText().toString());
            dismiss();
        });

        //获取焦点
        mCommentEt.setFocusable(true);
        mCommentEt.setFocusableInTouchMode(true);
        mCommentEt.requestFocus();


    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        // 重写onDismiss，将评论框文本填充回评论条
        dialogFragmentDataCallback.setCommentText(mCommentEt.getText().toString());
        super.onDismiss(dialog);
    }

    @Override
    public void onCancel(DialogInterface dialog) {
        // 重写onCancel，将评论框文本填充回评论条，一般是点击外部及返回键所触发
        dialogFragmentDataCallback.setCommentText(mCommentEt.getText().toString());
        super.onCancel(dialog);
    }
}
