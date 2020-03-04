package com.muxi.workbench.ui.project.model.Repository;

import com.muxi.workbench.ui.project.model.bean.FolderTree;

import java.util.ArrayList;
import java.util.List;

import com.muxi.workbench.ui.project.model.bean.FolderTree.ChildBean;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.functions.Function;

public class DetailDataSource {


    public enum FolderType {
        DOCTREE,FILETREE
    }
    private DetailRemoteSource remote;
    private FolderTree fileTree;
    private FolderTree docTree;
    private int productId;

    public DetailDataSource(int productId){
        remote=new DetailRemoteSource();
        this.productId=productId;
    }


    public Observable<List<ChildBean>>getAllFolder(boolean isUpdate,FolderType type){
        switch (type){
            case DOCTREE:
                return getAllDoc(isUpdate);
            case FILETREE:
                return getAllFile(isUpdate);
            default:
                return null;
        }
    }

    public Observable<List<ChildBean>>getWithRouter(boolean isUpdate, FolderType folderType, List<Integer>router){
        List<ChildBean> tempTree;
        Observable<List<ChildBean>>observableTemp=null;
        if (folderType == FolderType.FILETREE){
            observableTemp=getFileFromRemote();
            tempTree=fileTree.getChild();
        }else {
            observableTemp=getDocFromRemote();
            tempTree=docTree.getChild();
        }


        if (isUpdate||tempTree==null){
            return observableTemp
                    .flatMap(list -> {
                        List<ChildBean> temp=list;
                        for (int i = 0; i < router.size(); i++) {
                            temp=temp.get(router.get(i)).getChild();
                            if (temp==null)
                                temp=new ArrayList<>();
                        }
                        return Observable.just(temp);
                    });
        }else {
            return Observable.fromCallable(()->{
                List<ChildBean> temp=tempTree;
                for (int i = 0; i < router.size(); i++) {
                    temp=temp.get(router.get(i)).getChild();
                    if (temp==null)
                        temp=new ArrayList<>();
                }
                return temp;
            });

        }
    }



    private Observable<List<ChildBean>>getAllDoc(boolean isUpdate){
        if (isUpdate){
            return getDocFromRemote();
        }
        else if (docTree!=null){
            return Observable.just(docTree.getChild());
        }
        else
            return getDocFromRemote();

    }

    private Observable<List<ChildBean>>getDocFromRemote(){
        return remote.getDocFolder(productId)
                .flatMap(folderTree -> {
                    docTree=folderTree;
                    return Observable.just(folderTree.getChild());

                });
    }




    private Observable<List<ChildBean>> getAllFile(boolean isUpdate){
        if (isUpdate){
            return getFileFromRemote();
        }
        else if (fileTree!=null){
            return Observable.just(fileTree.getChild());
        }
        else
            return getFileFromRemote();
    }

    private Observable<List<ChildBean>>getFileFromRemote(){
        return remote.getFileFolder(productId)
                .flatMap(new Function<FolderTree, ObservableSource<List<ChildBean>>>() {
                    @Override
                    public ObservableSource<List<ChildBean>> apply(FolderTree folderTree) throws Exception {
                        fileTree=folderTree;
                        return Observable.just(folderTree.getChild());
                    }
                });
    }


}
