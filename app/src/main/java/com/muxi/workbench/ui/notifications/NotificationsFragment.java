package com.muxi.workbench.ui.notifications;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.muxi.workbench.R;
import com.muxi.workbench.ui.notifications.model.NotificationsRepository;
import com.muxi.workbench.ui.notifications.model.NotificationsResponse;
import com.muxi.workbench.ui.project.view.projectFolder.ProjectDocWebView;

public class NotificationsFragment extends Fragment implements NotificationContact.View {

    private NotificationContact.Presenter mPresenter;
    private TextView tvReadAll;
    private RecyclerView recyclerView;
    private NotificationsRepository repository;
    private ImageView emptyIv;
    private NotificationAdapter mAdapter;
    private NotificationAdapter.OnItemClickListener mListener;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        repository = new NotificationsRepository();
        mPresenter = new NotificationsPresenter(repository, this);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.frgment_notification, container, false);
        tvReadAll = root.findViewById(R.id.item_notification_read_all);
        recyclerView = root.findViewById(R.id.notification_rv);
        emptyIv = root.findViewById(R.id.notification_empty);

        tvReadAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getContext(), "全部已读", Toast.LENGTH_SHORT).show();
                allRead();
            }
        });
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this.getContext());
        recyclerView.setLayoutManager(linearLayoutManager);
        mListener = (objectId, docName) -> {
            Intent intent = ProjectDocWebView.newIntent(getContext(), objectId, docName);
            startActivity(intent);
        };
        return root;
    }

    @Override
    public void onStart() {
        super.onStart();
        mPresenter.start();
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void setPresenter(NotificationContact.Presenter presenter) {
        mPresenter = presenter;
    }

    @Override
    public void initAdapter(NotificationsResponse response) {
        mAdapter = new NotificationAdapter(mPresenter, response, mListener);
        recyclerView.setAdapter(mAdapter);
    }

    @Override
    public void showAllData(NotificationsResponse notificationsResponse) {
        mAdapter.setListBeans(notificationsResponse);
    }

    @Override
    public void setEmpty() {
        recyclerView.setVisibility(View.GONE);
        emptyIv.setVisibility(View.VISIBLE);
    }

    @Override
    public void allRead() {
        Toast.makeText(getContext(), "一扫而空", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showMore() {
        mPresenter.loadMore();
    }

    @Override
    public void refresh() {
        mPresenter.refresh();
    }

    @Override
    public void showError() {
        Toast.makeText(getContext(), "发生错误了...", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void goToDetail(int sourceId) {

    }
}