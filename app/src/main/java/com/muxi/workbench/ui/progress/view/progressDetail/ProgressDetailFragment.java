package com.muxi.workbench.ui.progress.view.progressDetail;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.muxi.workbench.R;
import com.muxi.workbench.ui.progress.contract.ProgressDetailContract;
import com.muxi.workbench.ui.progress.model.Comment;
import com.muxi.workbench.ui.progress.model.Progress;
import com.muxi.workbench.ui.progress.model.progressDetail.ProgressDetailRemoteDataSource;
import com.muxi.workbench.ui.progress.model.progressDetail.ProgressDetailRepository;
import com.muxi.workbench.ui.progress.presenter.ProgressDetailPresenter;

import java.util.ArrayList;

public class ProgressDetailFragment extends Fragment implements ProgressDetailContract.View {

    private ProgressDetailContract.Presenter mPresenter;

    private ProgressDetailListAdapter mAdapter;

    private Toolbar mToolbar;

    private RecyclerView mProgressDetailRv;

    ProgressDetailListAdapter.ProgressDetailListener mPRogressDetailListener = new ProgressDetailListAdapter.ProgressDetailListener() {
        @Override
        public void onLikeClick() {

        }

        @Override
        public void onEditClick() {

        }

        @Override
        public void onCommentClick() {

        }

        @Override
        public void onUserClick() {

        }

        @Override
        public void onDeleteCommentClick() {

        }
    };

    @Override
    public void onResume() {
        super.onResume();
        mPresenter.start();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPresenter = new ProgressDetailPresenter(this, ProgressDetailRepository.getInstance(ProgressDetailRemoteDataSource.getInstance()));
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_progressdetail, container, false);

        mAdapter = new ProgressDetailListAdapter(getContext(), new Progress(), new ArrayList<Comment>(), mPRogressDetailListener);

        mProgressDetailRv = root.findViewById(R.id.rv_progressdetail);
        mProgressDetailRv.setLayoutManager(new LinearLayoutManager(getContext()));
        mProgressDetailRv.setAdapter(mAdapter);

        mToolbar = root.findViewById(R.id.tb_progressdetail);
        ((AppCompatActivity) getActivity()).setSupportActionBar(mToolbar);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);//添加默认的返回图标
        ((AppCompatActivity) getActivity()).getSupportActionBar().setHomeButtonEnabled(true); //设置返回键可用

        return root;
    }

    @Override
    public void setPresenter(ProgressDetailContract.Presenter mPresenter) {
        mPresenter = mPresenter;
    }

    @Override
    public void refreshLike() {

    }

    @Override
    public void showEditCommentView() {

    }

    @Override
    public void showNewComment() {

    }

    @Override
    public void showDeleteComment() {

    }

    @Override
    public void showEditProgress() {

    }
}
