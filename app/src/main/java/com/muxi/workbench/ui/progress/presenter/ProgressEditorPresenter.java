package com.muxi.workbench.ui.progress.presenter;

import com.muxi.workbench.ui.progress.contract.ProgressEditorContract;
import com.muxi.workbench.ui.progress.model.progressEditor.ProgressEditorDataSource;
import com.muxi.workbench.ui.progress.model.progressEditor.ProgressEditorRemoteDataSource;

public class ProgressEditorPresenter implements ProgressEditorContract.Presenter {

    private ProgressEditorContract.View mProgressEditorView;

    private ProgressEditorRemoteDataSource mProgressEditorDataSource;

    public ProgressEditorPresenter(ProgressEditorContract.View progressEditorView, ProgressEditorRemoteDataSource progressEditorDataSource) {
        this.mProgressEditorView = progressEditorView;
        this.mProgressEditorDataSource = progressEditorDataSource;
        mProgressEditorView.setPresenter(this);
    }

    @Override
    public void start() {

    }

    @Override
    public void newProgress(String title, String content) {
        mProgressEditorDataSource.newProgress(title, content, new ProgressEditorDataSource.NewProgressCallback() {
            @Override
            public void onSuccess() {
                mProgressEditorView.back();
            }

            @Override
            public void onError() {
                mProgressEditorView.showError();
            }
        });
    }

    @Override
    public void changeProgress(int sid, String title, String content) {
        mProgressEditorDataSource.changeProgress(sid, title, content, new ProgressEditorDataSource.ChangeProgressCallback() {
            @Override
            public void onSuccess() {
                mProgressEditorView.back();
            }

            @Override
            public void onError() {
                mProgressEditorView.showError();
            }
        });
    }
}
