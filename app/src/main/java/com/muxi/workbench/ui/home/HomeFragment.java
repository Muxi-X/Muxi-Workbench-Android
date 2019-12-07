package com.muxi.workbench.ui.home;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewStub;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.muxi.workbench.R;
import com.muxi.workbench.ui.home.model.FeedBean;

public class HomeFragment extends Fragment implements HomeContract.View {
    private Toolbar toolbar;
    private RecyclerView recyclerView;
    private FeedRepository mFeedRepository;
    private HomeContract.Presenter mPresenter;
    private FeedAdapter mAdapter;
    private FeedBean mBean = new FeedBean();
    private ViewStub viewStub;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private LinearLayout mLinearLayout;
    private Button mRetry;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mFeedRepository = new FeedRepository();
        mPresenter = new HomePresenter(mFeedRepository, this);
        mAdapter = new FeedAdapter(mBean, mPresenter);

    }

    @Override
    public void onResume() {
        super.onResume();
        mPresenter.start();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_home, container, false);
        toolbar = root.findViewById(R.id.home_toolbar);
        recyclerView = root.findViewById(R.id.home_rcv);
        viewStub = root.findViewById(R.id.home_view_stub);
        mSwipeRefreshLayout = root.findViewById(R.id.swipe_refresh);
        mSwipeRefreshLayout.setOnRefreshListener(
                new SwipeRefreshLayout.OnRefreshListener() {
                    @Override
                    public void onRefresh() {
                        refreshData();
                    }
                }
        );
        mLinearLayout = root.findViewById(R.id.home_content);
        mLinearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mSwipeRefreshLayout.isRefreshing()) {
                    mSwipeRefreshLayout.setRefreshing(false);
                    Toast.makeText(getContext(), "手动停止", Toast.LENGTH_SHORT).show();
                }
            }
        });
        initToolbar();
        initRv();
        return root;
    }

    private void initRv() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this.getContext());
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(mAdapter);

    }

    private void initToolbar() {
        toolbar.inflateMenu(R.menu.home_toolbar_item);
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.home_scan_code:
                        //todo: scan code
                        break;
                    case R.id.home_add:
                        //todo: add progress
                        break;
                    default:
                        break;
                }
                return true;
            }
        });
    }

    FeedAdapter.ItemListener listener = new FeedAdapter.ItemListener() {
        @Override
        public void onNameClick() {
            //跳转到个人界面
            Toast.makeText(getContext(), "应该跳转到个人界面", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onFileClick() {
            //跳转到相应文档
            Toast.makeText(getContext(), "应该跳转到相应文档", Toast.LENGTH_SHORT).show();
        }
    };

    @Override
    public void setPresenter(HomeContract.Presenter presenter) {
        mPresenter = presenter;
    }

    @Override
    public void addItem(FeedBean.DataListBean itemData) {

    }

    @Override
    public void showAllData(FeedBean feedBean) {
        mAdapter.replaceData(feedBean);
    }

    @Override
    public void setEmpty() {
        Log.e("TAG", "HomeFragment setEmpty");
        View view;
        try {
            view = viewStub.inflate();
            mRetry = view.findViewById(R.id.item_false_retry);
        } catch (Exception e) {

            viewStub.setVisibility(View.VISIBLE);
        } finally {
            if (mRetry != null) {
                mRetry.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        mPresenter.loadAllData();
                        Toast.makeText(getContext(), "i'm trying!", Toast.LENGTH_SHORT).show();
                    }
                });

            }
        }

    }


    @Override
    public void setLoadingIndicator(boolean loadingIndicator, boolean isSucceed) {

        mSwipeRefreshLayout.setRefreshing(loadingIndicator);

        if (isSucceed) {
            Toast.makeText(getContext(), "刷新成功", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(getContext(), "刷新失败", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void showPerson() {

    }

    @Override
    public void showFile() {

    }

    @Override
    public void refreshData() {
        viewStub.setVisibility(View.GONE);
        mPresenter.loadAllData();
    }


}
