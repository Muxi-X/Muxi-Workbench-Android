package com.muxi.workbench.ui.progress.contract;

public interface ProgressEditorContract {
    interface View {
        void setPresenter(Presenter presenter);
        void back();
        void showError();
    }

    interface Presenter {
        void start();
        void newProgress(String title, String content);
        void changeProgress(int sid, String title, String content);
    }
}
