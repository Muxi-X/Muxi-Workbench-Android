package com.muxi.workbench.ui.project.view;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewStub;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.muxi.workbench.R;
import com.muxi.workbench.services.DownloadService;
import com.muxi.workbench.ui.project.FolderContract;
import com.muxi.workbench.ui.project.model.Repository.DetailDataSource;
import com.muxi.workbench.ui.project.model.bean.FolderTree;
import com.muxi.workbench.ui.project.presenter.FolderPresenter;

import java.util.ArrayList;
import java.util.List;

public class ProjectFolderFragment extends Fragment implements FolderContract.View {


    private RecyclerView mRecycleView;
    private FolderListAdapter mAdapter;
    private FolderContract.Presenter mPresenter;
    public static final String PROJECTID="project_id";
    public static final String TYPE="Type";
    private ViewStub viewStub;
    private DetailDataSource.FolderType type;
    private boolean isViewStubLoad=false;
    private boolean isViewStubVisible=false;



    //权限检查
    private List<String> showRequests=new ArrayList<>();
    private String[]permissions=new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.READ_EXTERNAL_STORAGE};
    private boolean isPermissionAllowed=false;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle args=getArguments();
        if (args==null)
            return;
        type= DetailDataSource.FolderType.values()[args.getInt(TYPE)];
        mPresenter=new FolderPresenter(args.getInt(PROJECTID),this,type);
        mAdapter=new FolderListAdapter();

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View fragmentView=inflater.inflate(R.layout.folder_fragment,container,false);
        mRecycleView=fragmentView.findViewById(R.id.folder_recycle_view);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        mRecycleView.setLayoutManager(layoutManager);
        mPresenter.getAllFolder();
        mRecycleView.setAdapter(mAdapter);
        viewStub=fragmentView.findViewById(R.id.empty_folder_viewstub);
        viewStub.setOnInflateListener((stub, inflated) -> isViewStubLoad=true);


        mAdapter.setListener((childBean, position) -> {
            if (childBean.isFolder()) {
                goNextFolder(childBean.androidRoute);
            }else {
                if (getActivity()!=null&&childBean.getUrl()!=null){
                    if (isPermissionAllowed||isPermissionAllow()) {
                        Intent intent=new Intent(getActivity(), DownloadService.class);
                        intent.putExtra(DownloadService.URL,childBean.getUrl());
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                            getActivity().startForegroundService(intent);
                        }else {
                            getActivity().startService(intent);
                        }
                    }else {
                        isPermissionAllow();
                        if (getActivity()==null)
                            return;
                        ActivityCompat.requestPermissions(getActivity(),
                                showRequests.toArray(new String[showRequests.size()]),
                                1);
                    }


                }


            }
        });



        return fragmentView;
    }

    public static ProjectFolderFragment newInstance(int pid, int type){

        Bundle args = new Bundle();
        args.putInt(PROJECTID,pid);
        args.putInt(TYPE,type);
        ProjectFolderFragment folderFragment=new ProjectFolderFragment();
        folderFragment.setArguments(args);

        return folderFragment;
    }

    public void backToPreFolder(){
        if (isViewStubVisible){
            viewStub.setVisibility(View.GONE);
            isViewStubVisible=false;
        }
        mPresenter.getPreviousFolder();
    }

    public void goNextFolder(int position){
        mPresenter.getNextFolder(position);
    }

    @Override
    public void showEmpty() {
        mAdapter.setEmpty();
        isViewStubVisible=true;
        if (isViewStubLoad){
            viewStub.setVisibility(View.VISIBLE);
        }else {
            viewStub.inflate();
        }
    }

    public boolean isPermissionAllow(){
        boolean isAllowed=true;
        showRequests.clear();
        for (int i = 0; i <permissions.length ; i++) {
            if (getActivity()==null)
                return false;
            if (ActivityCompat.checkSelfPermission(getActivity(),permissions[i])!= PackageManager.PERMISSION_GRANTED){
                isAllowed=false;
                showRequests.add(permissions[i]);
            }
        }
        return isAllowed;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,  String[] permissions,  int[] grantResults) {
        if (requestCode == 1) {
            for (int i = 0; i <permissions.length ; i++) {
                Log.i("Main", "onRequestPermissionsResult: "+permissions[i]+"   --->"+grantResults[i]);
                if (grantResults[i]==PackageManager.PERMISSION_DENIED){
                    Toast.makeText(getActivity(),"请允许获取权限以确保程序正常进行",Toast.LENGTH_LONG).show();
                    return;
                }
            }
            isPermissionAllowed=true;

        }

    }
    @Override
    public void showError() {

    }

    @Override
    public void showLoading() {
        mAdapter.setEmpty();
    }

    @Override
    public void showFolder(List<FolderTree.ChildBean> list) {
        mAdapter.setmList(list);
    }


}
