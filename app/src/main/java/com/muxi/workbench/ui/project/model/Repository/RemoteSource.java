package com.muxi.workbench.ui.project.model.Repository;

import android.util.Log;

import com.muxi.workbench.commonUtils.NetUtil;
import com.muxi.workbench.commonUtils.RetrofitApi;
import com.muxi.workbench.ui.login.model.UserWrapper;
import com.muxi.workbench.ui.project.model.Project;

import java.util.ArrayList;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.Single;
import io.reactivex.functions.BiFunction;
import io.reactivex.functions.Function;
import io.reactivex.functions.Function3;
import io.reactivex.functions.Predicate;
import io.reactivex.schedulers.Schedulers;

public class RemoteSource {


    private UserWrapper mUser;
    private RetrofitApi mRetrofitApi;
    public RemoteSource(){
        mUser=UserWrapper.getInstance();
        mRetrofitApi=NetUtil.getInstance().getApi();
    }


    /**
     * 一个分页12个
     * 但是一般项目需要全部显示,而且数量不会太多
     * @param page=1时加载全部(最多到5，也是为了一定的限制)，否则加载制定的页面
     * @return
     */
    public Single<Project> getProject(int page){
        return Observable.range(page,3)
                .concatMap(new Function<Integer, ObservableSource<Project>>() {
                    @Override
                    public ObservableSource<Project> apply(Integer i) throws Exception {
                        Log.i("test", "apply: "+i);
                        return mRetrofitApi.getProject(mUser.getToken(),mUser.getUid(),i);
                    }
                }).takeUntil(new Predicate<Project>() {
                    @Override
                    public boolean test(Project project) throws Exception {
                        if (page!=1){
                            return true;
                        }
                        else
                        {
                            return !project.isHasNext();
                        }
                    }
                }).scan(new BiFunction<Project, Project, Project>() {
                    @Override
                    public Project apply(Project project, Project project2) throws Exception {
                        project.getList().addAll(project2.getList());
                        project2.setList(project.getList());
                        return project2;
                    }
                }).last(new Project())
                .subscribeOn(Schedulers.io());


    }


}
