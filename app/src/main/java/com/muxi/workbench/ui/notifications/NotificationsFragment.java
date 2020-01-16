package com.muxi.workbench.ui.notifications;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.RecyclerView;

import com.muxi.workbench.R;

public class NotificationsFragment extends Fragment implements NotificationContact.View {

    private NotificationContact.Presenter mPresenter;
    private TextView tvReadAll;
    private RecyclerView recyclerView;
    private NotificationsRepository repository;
    private ImageView emptyIv;

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
    public void initAdapter(NotificationAdapter adapter) {
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void showAllData(NotificationsBean notificationsBean) {
        mPresenter.loadAllData(false);
    }

    @Override
    public void setEmpty() {
        recyclerView.setVisibility(View.GONE);
        emptyIv.setVisibility(View.VISIBLE);
    }

    @Override
    public void allRead() {
        mPresenter.clearRedNode();
    }

    @Override
    public void showMore() {
        mPresenter.loadMore();
    }

    @Override
    public void refresh() {
        mPresenter.refresh();
    }
}