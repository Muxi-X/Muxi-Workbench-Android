package com.muxi.workbench.ui.notifications;

import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.NotificationVH> {


    @NonNull
    @Override
    public NotificationVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull NotificationVH holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }

    public class NotificationVH extends RecyclerView.ViewHolder {
        public NotificationVH(@NonNull View itemView) {
            super(itemView);
        }
    }
}
