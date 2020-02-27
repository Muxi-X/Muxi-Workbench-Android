package com.muxi.workbench.ui.progress.view.progressDetail;


public interface DialogFragmentDataCallback {

    String getCommentText();

    void setCommentText(String commentTextTemp);

    void submitComment(String comment);
}
