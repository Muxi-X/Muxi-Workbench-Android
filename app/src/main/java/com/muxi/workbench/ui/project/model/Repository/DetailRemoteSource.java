package com.muxi.workbench.ui.project.model.Repository;

import com.google.gson.Gson;
import com.muxi.workbench.commonUtils.net.NetUtil;
import com.muxi.workbench.commonUtils.net.RetrofitApi;
import com.muxi.workbench.ui.login.model.UserWrapper;
import com.muxi.workbench.ui.project.model.bean.Folder;
import com.muxi.workbench.ui.project.model.bean.FolderTree;

import java.util.concurrent.Callable;

import io.reactivex.Observable;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

public class DetailRemoteSource {


    private UserWrapper mUser;
    private RetrofitApi mRetrofitApi;

    public DetailRemoteSource() {
        mUser = UserWrapper.getInstance();
        mRetrofitApi = NetUtil.getInstance().getApi();
    }


    public Observable<FolderTree> getFileFolder(int pId) {
        return mRetrofitApi
                .getFiletree(pId)
                .subscribeOn(Schedulers.io())
                .flatMap(new Function<Folder, Observable<FolderTree>>() {
                    @Override
                    public Observable<FolderTree> apply(Folder folder) throws Exception {
                        return Observable.fromCallable(new Callable<FolderTree>() {
                            @Override
                            public FolderTree call() throws Exception {
                                Gson gson = NetUtil.getInstance().getGson();
                                FolderTree folderTree = gson.fromJson(folder.getFiletree(), FolderTree.class);
                                return folderTree;
                            }
                        });
                    }
                });
    }

    public Observable<FolderTree>getDocFolder(int pId){

        return mRetrofitApi
                .getDoctree(pId)
                .subscribeOn(Schedulers.io())
                .flatMap((Function<Folder, Observable<FolderTree>>) folder -> Observable.fromCallable(new Callable<FolderTree>() {
                    @Override
                    public FolderTree call() throws Exception {
                        Gson gson = NetUtil.getInstance().getGson();
                        FolderTree folderTree = gson.fromJson(folder.getDoctree(), FolderTree.class);
                        return folderTree;
                    }
                }));

    }

}
