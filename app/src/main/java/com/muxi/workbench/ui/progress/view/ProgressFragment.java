package com.muxi.workbench.ui.progress.view;

import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.muxi.workbench.R;
import com.muxi.workbench.commonUtils.AppExecutors;
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

    private ProgressContract.Presenter mPresenter;

    private ProgressTitleBar mProgressTitleBar;

    private RecyclerView mProgressListRv;

    private SwipeRefreshLayout mProgressSrl;

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
        public void onLikeClick(Progress likeProgress, int position) {
            if ( likeProgress.isIfLike() == 1 )
                mPresenter.cancelLikeProgress(likeProgress.getSid(), position);
            else
                mPresenter.likeProgress(likeProgress.getSid(), position);
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
            Toast.makeText(getContext(), "去编辑进度",Toast.LENGTH_LONG).show();
        }
    };

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPresenter = new ProgressPresenter(
                ProgressRepository.getInstance(
                    ProgressDataSource.getInstance(
                            StickyProgressDatabase.getInstance(getContext()).ProgressDao(),
                        new AppExecutors()
                    )
                ), this);
    }

    @Override
    public void onResume() {
        super.onResume();
        mPresenter.start();
    }


    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_progress, container, false);

        mProgressSrl = root.findViewById(R.id.srl_progress);
        mProgressSrl.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mPresenter.loadProgressList(true);
            }
        });
        mProgressSrl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mProgressSrl.isRefreshing()) {
                    mProgressSrl.setRefreshing(false);
                    Toast.makeText(getContext(), "已停止更新", Toast.LENGTH_SHORT).show();
                }
            }
        });

        mAdapter = new ProgressListAdapter(mPresenter, getContext(), new ArrayList<>(), mItemListener);
        mProgressListRv = root.findViewById(R.id.rv_progress);
        mProgressListRv.setLayoutManager(new LinearLayoutManager(getContext()));
        mProgressListRv.setAdapter(mAdapter);

        mProgressTitleBar = root.findViewById(R.id.ptb_progress);
        mProgressTitleBar.setOptionSp(getContext());
        mProgressTitleBar.setBackgroundColor(Color.parseColor("#ffffff"));
        mProgressTitleBar.setElevation(2);
        mProgressTitleBar.setOptionSelectListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                mProgressTitleBar.adapter.setSelectedPosition(position);
                switch (position) {
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
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        mProgressTitleBar.setOptionIbListener(v -> mProgressTitleBar.showSpinner());
        mProgressTitleBar.setAddListener(v -> {
             ///TODO  to Progress-editing Fragment
        });

//        mPresenter.loadProgressList(true);

        return root;
    }

    @Override
    public void setPresenter(ProgressContract.Presenter presenter) {
        mPresenter = presenter;
    }

    @Override
    public void showProgressList(List<Progress> progressList) {
        mAdapter.replaceData(progressList);
        mProgressSrl.setRefreshing(false);
    }

    @Override
    public void showCommentView() {
        Toast.makeText(getContext(),"去评论",Toast.LENGTH_LONG).show();
    }

    @Override
    public void showProgressDetail(int sid) {
        Toast.makeText(getContext(),"去详情页",Toast.LENGTH_LONG).show();
    }

    @Override
    public void refreshLikeProgress(int position, int iflike) {
        mAdapter.notifyProgress(position, iflike);
    }

    @Override
    public void showSelectAllFilter() {
        mProgressTitleBar.setSpinnerLabel(0);
        mPresenter.loadProgressList(true);
    }

    @Override
    public void showSelectProductFilter() {
        mProgressTitleBar.setSpinnerLabel(1);
        mPresenter.loadProgressList(true);
    }

    @Override
    public void showSelectBackendFilter() {
        mProgressTitleBar.setSpinnerLabel(5);
        mPresenter.loadProgressList(true);

    }

    @Override
    public void showSelectFrontendFilter() {
        mProgressTitleBar.setSpinnerLabel(3);
        mPresenter.loadProgressList(true);

    }

    @Override
    public void showSelectAndroidFilter() {
        mProgressTitleBar.setSpinnerLabel(4);
        mPresenter.loadProgressList(true);

    }

    @Override
    public void showSelectDesignFilter() {
        mProgressTitleBar.setSpinnerLabel(2);
        mPresenter.loadProgressList(true);

    }

    @Override
    public void showMoreProgress(List<Progress> progresses) {
        mAdapter.addMoreProgress(progresses);
    }

    @Override
    public void showUserInfo(int uid) {
        Toast.makeText(getContext(),"去往个人主页",Toast.LENGTH_LONG).show();
        ///todo intent to info
    }

    @Override
    public void showError() {
        Toast.makeText(getContext(), "失败辽", Toast.LENGTH_LONG).show();
    }

    @Override
    public void showAddNewProgress() {
        Toast.makeText(getContext(),"去往新进度编辑页",Toast.LENGTH_LONG).show();
        ///todo intent to empty edit-fragment
    }

    @Override
    public void showDeleteProgress(int position) {
        mAdapter.removeData(position);
    }

    @Override
    public void moveNewStickyProgress(int position) {
        mAdapter.moveProgress(position, 0);
    }

    @Override
    public void moveDeleteStickyProgress(int position) {
        mAdapter.moveProgress(position, 1);
    }
}