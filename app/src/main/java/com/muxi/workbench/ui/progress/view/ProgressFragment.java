package com.muxi.workbench.ui.progress.view;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.muxi.workbench.R;
import com.muxi.workbench.ui.login.model.UserWrapper;
import com.muxi.workbench.ui.progress.ProgressContract;
import com.muxi.workbench.ui.progress.ProgressFilterType;
import com.muxi.workbench.ui.progress.model.Progress;
import com.muxi.workbench.ui.progress.model.ProgressDataSource;
import com.muxi.workbench.ui.progress.model.ProgressRepository;
import com.muxi.workbench.ui.progress.model.StickyProgressDatabase;
import com.muxi.workbench.ui.progress.presenter.ProgressPresenter;
import com.muxi.workbench.ui.progress.view.ProgressListAdapter.ProgressItemListener;

import java.util.ArrayList;
import java.util.List;

public class ProgressFragment extends Fragment implements ProgressContract.View {

    private int uid = UserWrapper.getInstance().getUser().getUid();

    private ProgressContract.Presenter mPresenter;

    private ProgressTitleBar mProgressTitleBar;

    private RecyclerView mProgressListRv;

    private ProgressListAdapter mAdapter;


    ProgressItemListener mItemListener = new ProgressItemListener() {
        @Override
        public void onItemClick(Progress clickedProgress) {
            mPresenter.openProgressDetails(clickedProgress);
        }

        @Override
        public void onMoreClick() {
            mPresenter.loadProgressList(false);
        }

        @Override
        public void onUserClick(int uid) {
            mPresenter.openUserInfo(uid);
        }

        @Override
        public void onLikeClick(Progress likeProgress) {
            if ( likeProgress.isIfLike() )
                mPresenter.likeProgress(likeProgress.getSid());
            else
                mPresenter.cancelLikeProgress(likeProgress.getSid());
        }

        @Override
        public void onCommentClick(Progress commentProgress) {
            if ( commentProgress.getCommentCount() == 0 ) {
                String comment ="";
                mPresenter.commentProgress(commentProgress.getSid(),comment);
            } else {
                mPresenter.openProgressDetails(commentProgress);
            }
        }

        @Override
        public void onEditClick(Progress editProgress) {

        }
    };

    public static ProgressFragment newInstance() {
        return new ProgressFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mPresenter = new ProgressPresenter(ProgressRepository.getInstance(
                ProgressDataSource.getInstance(StickyProgressDatabase.getInstance(getContext()).
                        stickyProgressDao())), this);
    }

    @Override
    public void onResume() {
        super.onResume();
        mPresenter.start();
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_progress, container, false);

        mProgressTitleBar = root.findViewById(R.id.ptb_progress);

        mProgressTitleBar.setOptionSp(getContext());
        mProgressTitleBar.setOptionSelectListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                mProgressTitleBar.adapter.setSelectedPosition(position);
                switch (position){
                    case 0:
                        mPresenter.setProgressFilterType(ProgressFilterType.ALL_PROGRESS);
                        break;
                    case 1:
                        mPresenter.setProgressFilterType(ProgressFilterType.PRODUCT_PROGRESS);
                        break;
                    case 2:
                        mPresenter.setProgressFilterType(ProgressFilterType.DESIGN_PROGRESS);
                        break;
                    case 3:
                        mPresenter.setProgressFilterType(ProgressFilterType.FRONTEND_PROGRESS);
                        break;
                    case 4:
                        mPresenter.setProgressFilterType(ProgressFilterType.ANDROID_PROGRESS);
                        break;
                    case 5:
                        mPresenter.setProgressFilterType(ProgressFilterType.BACKEND_PROGRESS);
                        break;
                }
                mPresenter.loadProgressList(true);
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        mProgressTitleBar.setOptionIbListener(v -> mProgressTitleBar.showSpinner());

        mProgressTitleBar.setAddListener(v -> {
             ///TODO  to Progress-editing Fragment
        });


        mAdapter = new ProgressListAdapter(getContext(), new ArrayList<>(), mItemListener );

        mProgressListRv = root.findViewById(R.id.rv_progress);
        mProgressListRv.setAdapter(mAdapter);
        mProgressListRv.setLayoutManager(new LinearLayoutManager(getContext()));


        return root;
    }

    @Override
    public void setPresenter(ProgressContract.Presenter presenter) {
        mPresenter = presenter;
    }

    @Override
    public void showProgressList(List<Progress> progressList) {
        Log.d("!!!!!!!",progressList.size()+"");
        mAdapter.replaceData(progressList);
    }

    @Override
    public void showCommentView() {

    }

    @Override
    public void showProgressDetail(int sid) {

    }

    @Override
    public void showLikeProgress() {

    }

    @Override
    public void showNotLikedProgress() {

    }

    @Override
    public void renewCommentNum() {

    }

    @Override
    public void showSelectAllFilter() {
        mProgressTitleBar.setSpinnerLabel(0);
    }

    @Override
    public void showSelectProductFilter() {
        mProgressTitleBar.setSpinnerLabel(1);
    }

    @Override
    public void showSelectBackendFilter() {
        mProgressTitleBar.setSpinnerLabel(5);

    }

    @Override
    public void showSelectFrontendFilter() {
        mProgressTitleBar.setSpinnerLabel(3);

    }

    @Override
    public void showSelectAndroidFilter() {
        mProgressTitleBar.setSpinnerLabel(4);

    }

    @Override
    public void showSelectDesignFilter() {
        mProgressTitleBar.setSpinnerLabel(2);

    }

    @Override
    public void showMoreProgress(List<Progress> progresses) {
        ///todo showMoreProgress
    }

    @Override
    public void showUserInfo(int uid) {
        ///todo intent to info
    }

    @Override
    public void showLoadingError() {
        Toast.makeText(getContext(), "加载失败", Toast.LENGTH_LONG).show();
    }

    @Override
    public boolean isActive() {
        return false;
    }

    @Override
    public void showAddNewProgress() {
        ///todo intent to add
    }

    @Override
    public void showDeleteProgress() {

    }
}