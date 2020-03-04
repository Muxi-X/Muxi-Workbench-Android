package com.muxi.workbench.ui.project.view;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewStub;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.muxi.workbench.R;
import com.muxi.workbench.ui.project.FolderContract;
import com.muxi.workbench.ui.project.model.Repository.DetailDataSource;
import com.muxi.workbench.ui.project.model.bean.FolderTree;
import com.muxi.workbench.ui.project.presenter.FolderPresenter;

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

    @Override
    public void showError() {

    }

    @Override
    public void showLoading() {

    }

    @Override
    public void showFolder(List<FolderTree.ChildBean> list) {
        mAdapter.setmList(list);
    }

    @Override
    public void finish() {
        getActivity().getSupportFragmentManager()
                .popBackStack();
    }
}
