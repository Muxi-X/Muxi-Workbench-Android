package com.muxi.workbench.ui.progress.view.progressList;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
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
import com.muxi.workbench.commonUtils.customView.MyRefreshLayout;
import com.muxi.workbench.ui.progress.contract.ProgressContract;
import com.muxi.workbench.ui.progress.ProgressFilterType;
import com.muxi.workbench.ui.progress.model.Progress;
import com.muxi.workbench.ui.progress.model.progressList.ProgressListRemoteAndLocalDataSource;
import com.muxi.workbench.ui.progress.model.progressList.ProgressListRepository;
import com.muxi.workbench.ui.progress.model.StickyProgressDatabase;
import com.muxi.workbench.ui.progress.presenter.ProgressListPresenter;
import com.muxi.workbench.ui.progress.view.progressDetail.ProgressDetailActivity;
import com.muxi.workbench.ui.progress.view.progressEditor.EditorActivity;
import com.muxi.workbench.ui.progress.view.progressList.ProgressListAdapter.ProgressItemListener;

import java.util.ArrayList;
import java.util.List;

import static android.app.Activity.RESULT_OK;

public class ProgressFragment extends Fragment implements ProgressContract.View {

    private ProgressContract.Presenter mPresenter;

    private ProgressTitleBar mProgressTitleBar;

    private RecyclerView mProgressListRv;

    private MyRefreshLayout mProgressSrl;

    private ProgressListAdapter mAdapter;

    ProgressItemListener mItemListener = new ProgressItemListener() {
        @Override
        public void onItemClick(Progress clickedProgress, int position) {
            Intent toDetailIntent = ProgressDetailActivity
                    .newIntent(getContext(), clickedProgress.getSid(), clickedProgress.getUsername(),
                            clickedProgress.getAvatar(), false, clickedProgress.getTitle(), position,clickedProgress.getLikeCount(),clickedProgress.getIfLike()==1?true:false);
            startActivityForResult(toDetailIntent, 1);
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
            if (likeProgress.getIfLike() == 1)
                mPresenter.cancelLikeProgress(likeProgress.getSid(), position);
            else
                mPresenter.likeProgress(likeProgress.getSid(), position);
        }

        @Override
        public void onCommentClick(Progress commentProgress, int position) {
            Intent toDetailIntent = null;
            if (commentProgress.getCommentCount() == 0)
                toDetailIntent = ProgressDetailActivity
                        .newIntent(getContext(), commentProgress.getSid(), commentProgress.getUsername(),
                        commentProgress.getAvatar(), true, commentProgress.getTitle(), position);
            else
                toDetailIntent = ProgressDetailActivity
                        .newIntent(getContext(), commentProgress.getSid(), commentProgress.getUsername(),
                        commentProgress.getAvatar(), false, commentProgress.getTitle(), position);
            startActivityForResult(toDetailIntent, 1);
        }

        @Override
        public void onEditClick(Progress editProgress) {
            startActivityForResult(EditorActivity.newIntent(getContext(),false, editProgress.getSid(), editProgress.getTitle(), editProgress.getContent()), 2);
        }
    };

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPresenter = new ProgressListPresenter(
                ProgressListRepository.getInstance(
                        ProgressListRemoteAndLocalDataSource.getInstance(
                                StickyProgressDatabase.getInstance(getContext()).ProgressDao(),
                                new AppExecutors()
                        )
                ), this);
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
        /*
         * @author ljl
         */
        mProgressSrl.setOnLoadMoreListener(new MyRefreshLayout.OnLoadMoreListener() {
            @Override
            public void onLoadMore() {
                mPresenter.loadProgressList(false);
                mProgressSrl.setLoading(false);
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
                        mPresenter.loadProgressList(true);
                        break;
                    case 1:
                        mPresenter.setProgressFilterType(ProgressFilterType.PRODUCT_PROGRESS);
                        mPresenter.loadProgressList(true);
                        break;
                    case 2:
                        mPresenter.setProgressFilterType(ProgressFilterType.DESIGN_PROGRESS);
                        mPresenter.loadProgressList(true);
                        break;
                    case 3:
                        mPresenter.setProgressFilterType(ProgressFilterType.FRONTEND_PROGRESS);
                        mPresenter.loadProgressList(true);
                        break;
                    case 4:
                        mPresenter.setProgressFilterType(ProgressFilterType.ANDROID_PROGRESS);
                        mPresenter.loadProgressList(true);
                        break;
                    case 5:
                        mPresenter.setProgressFilterType(ProgressFilterType.BACKEND_PROGRESS);
                        mPresenter.loadProgressList(true);
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        mProgressTitleBar.setAddListener(v -> {
            Intent intent = EditorActivity.newIntent(getContext(), true);
            startActivityForResult(intent, 2);
        });

        return root;
    }

    /**
     *
     * @param requestCode       1-进入详情页 2-进入编辑页
     * @param resultCode
     * @param data
     */
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case 1: {
                if ( resultCode == RESULT_OK ) {
                    mPresenter.loadProgress(data.getIntExtra("position", -1),
                            data.getIntExtra("sid", -1),
                            data.getStringExtra("avatar"),
                            data.getStringExtra("username"),
                            data.getIntExtra("uid", -1));
                }
                break;
            }
            case 2: {
                if (resultCode == RESULT_OK) {
                    mPresenter.loadProgressList(true);
                }
                break;
            }
        }
    }

    @Override
    public void refreshProgress(int position, Progress progress) {
        mAdapter.notifyProgress(position, progress);
    }

    @Override
    public void setPresenter(ProgressContract.Presenter presenter) {
        mPresenter = presenter;
    }

    @Override
    public void showProgressList(List<Progress> progressList) {
        mAdapter.replaceData(progressList);
        mProgressListRv.scrollToPosition(0);
        LinearLayoutManager mLayoutManager = (LinearLayoutManager) mProgressListRv.getLayoutManager();
        mLayoutManager.scrollToPositionWithOffset(0, 0);
        mProgressSrl.setRefreshing(false);
    }

    @Override
    public void refreshLikeProgress(int position, boolean iflike) {
        mAdapter.notifyProgressLike(position, iflike);
    }

    @Override
    public void showMoreProgress(List<Progress> progresses) {
        mAdapter.addMoreProgress(progresses);
    }

    @Override
    public void showUserInfo(int uid) {
        Toast.makeText(getContext(), "去往个人主页", Toast.LENGTH_LONG).show();
        ///todo intent to info
    }

    @Override
    public void showError() {
        Toast.makeText(getContext(), "失败辽", Toast.LENGTH_LONG).show();
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