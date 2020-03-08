package com.muxi.workbench.ui.project.model.Repository;

import android.text.TextUtils;

import com.muxi.workbench.commonUtils.NetUtil;
import com.muxi.workbench.ui.project.model.bean.FilesId;
import com.muxi.workbench.ui.project.model.bean.FilesResponse;
import com.muxi.workbench.ui.project.model.bean.FolderTree;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.muxi.workbench.ui.project.model.bean.FolderTree.ChildBean;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

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
             observableTemp=observableTemp
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
             observableTemp=Observable.fromCallable(()->{
                List<ChildBean> temp=tempTree;
                for (int i = 0; i < router.size(); i++) {
                    temp=temp.get(router.get(i)).getChild();
                    if (temp==null)
                        temp=new ArrayList<>();
                }
                return temp;
            });

        }

        return addMoreInfo(observableTemp,folderType)
                .subscribeOn(Schedulers.io());
    }



    private Observable<List<ChildBean>>getAllDoc(boolean isUpdate){
        Observable<List<ChildBean>> observable;
        if (isUpdate){
            observable= getDocFromRemote();
        }
        else if (docTree!=null){
            observable= Observable.just(docTree.getChild());
        }
        else
            observable= getDocFromRemote();
        return addMoreInfo(observable,FolderType.DOCTREE);

    }

    private Observable<List<ChildBean>>getDocFromRemote(){
        return remote.getDocFolder(productId)
                .flatMap(folderTree -> {
                    docTree=folderTree;
                    return Observable.just(folderTree.getChild());

                });
    }




    private Observable<List<ChildBean>> getAllFile(boolean isUpdate){
        Observable<List<ChildBean>> observable;
        if (isUpdate){
            observable= getFileFromRemote();
        }
        else if (fileTree!=null){
            observable= Observable.just(fileTree.getChild());
        }
        else
            observable= getFileFromRemote();

        return addMoreInfo(observable,FolderType.FILETREE);
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

    private Observable<List<ChildBean>>addMoreInfo(Observable<List<ChildBean>> observable,FolderType folderType){
        List<ChildBean> content = new ArrayList<>();
        HashMap<Integer, ChildBean> mTempFileMap = new HashMap<>();
        return observable.flatMap((Function<List<ChildBean>, ObservableSource<FilesResponse>>) list -> {
            List<Integer> temp = new ArrayList<>();
            content.addAll(list);
            for (int i = 0; i < list.size(); i++) {
                ChildBean file = list.get(i);
                if (!file.isFolder()) {
                    //表示已经添加过信息，直接跳过
                    if (!TextUtils.isEmpty(file.getTime()))
                        return Observable.just(new FilesResponse());
                    temp.add(Integer.valueOf(file.getId()));
                    mTempFileMap.put(Integer.valueOf(file.getId()), file);
                }
            }
            if (!temp.isEmpty()) {
                if (folderType==FolderType.FILETREE) {
                    FilesId filesId = new FilesId();
                    filesId.setFile(temp);
                    return NetUtil.getInstance().getApi().getFileDetail(filesId);
                }
                else {
                    FilesId filesId=new FilesId();
                    filesId.setDoc(temp);
                    return NetUtil.getInstance().getApi().getDocDetail(filesId);

                }
            } else {
                return Observable.just(new FilesResponse());
            }

        }).flatMap((Function<FilesResponse, ObservableSource<List<ChildBean>>>) filesResponse -> {
            if (filesResponse.getFileList() == null&&filesResponse.getDocList()==null) {
                return Observable.just(content);
            } else {
                List<FilesResponse.FileListBean>list;
                if (filesResponse.getFileList()==null){
                    list=filesResponse.getDocList();
                }else {
                    list=filesResponse.getFileList();
                }
                for (FilesResponse.FileListBean bean : list) {
                    ChildBean child = mTempFileMap.get(bean.getId());
                    if (child != null) {
                        child.setTime(bean.getCreate_time());
                        child.setCreator(bean.getCreator());
                        child.setUrl(bean.getUrl());
                    }
                }
                return Observable.just(content);
            }

        });

    }



}
