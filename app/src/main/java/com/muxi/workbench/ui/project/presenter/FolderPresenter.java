package com.muxi.workbench.ui.project.presenter;

import com.muxi.workbench.ui.project.FolderContract;
import com.muxi.workbench.ui.project.model.Repository.DetailDataSource;
import com.muxi.workbench.ui.project.model.bean.FolderTree;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.internal.disposables.ListCompositeDisposable;

public class FolderPresenter implements FolderContract.Presenter {



    private List<Integer>router;
    private DetailDataSource dataSource;
    private FolderContract.View view;
    private ListCompositeDisposable mDisposableList;
    private DetailDataSource.FolderType type;

    private int pid;
    public FolderPresenter(int pid, FolderContract.View view, DetailDataSource.FolderType type){
        router=new ArrayList<>();
        dataSource=new DetailDataSource(pid);
        mDisposableList=new ListCompositeDisposable();
        this.type=type;
        this.view=view;
    }

    @Override
    public void getAllFolder() {
        dataSource.getAllFolder(true,type)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new FolderObserver());
    }

    @Override
    public void getNextFolder(int position) {
        router.add(position);
        dataSource.getWithRouter(false,type,router)
                .subscribe(new FolderObserver());
    }

    @Override
    public void getPreviousFolder() {
        if (router.size()==0){
            view.finish();
        }else {
            router.remove(router.size()-1);
            dataSource.getWithRouter(false,type,router)
                    .subscribe(new FolderObserver());
        }


    }

    @Override
    public void update() {

    }

    @Override
    public void destory() {
        if (mDisposableList!=null){
            mDisposableList.dispose();
        }
        view=null;
    }

    private class FolderObserver implements Observer<List<FolderTree.ChildBean>> {

        @Override
        public void onSubscribe(Disposable d) {
            mDisposableList.add(d);
            view.showLoading();
        }

        @Override
        public void onNext(List<FolderTree.ChildBean> childBeans) {
            if (childBeans.isEmpty()){
                view.showEmpty();
                return;
            }
            view.showFolder(childBeans);
        }

        @Override
        public void onError(Throwable e) {
            view.showError();
            e.printStackTrace();
        }

        @Override
        public void onComplete() {

        }
    }
}
