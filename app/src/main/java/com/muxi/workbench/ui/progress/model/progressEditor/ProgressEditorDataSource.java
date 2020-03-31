package com.muxi.workbench.ui.progress.model.progressEditor;

public interface ProgressEditorDataSource {

    interface ChangeProgressCallback {
        void onSuccess();
        void onError();
    }

    interface NewProgressCallback {
        void onSuccess();
        void onError();
    }

    void changeProgress(int sid, String title, String content, ChangeProgressCallback callback);

    void newProgress(String title, String content, NewProgressCallback callback);

}
