package com.muxi.workbench.ui.notifications;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.muxi.workbench.R;

import java.util.ArrayList;
import java.util.List;

public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.NotificationVH> {

    private NotificationContact.Presenter mPresenter;
    private List<NotificationsResponse.ListBean> listBeans = new ArrayList<>();
    private List<Boolean> readedList = new ArrayList<>();

    public NotificationAdapter(NotificationContact.Presenter presenter, NotificationsResponse response) {
        mPresenter = presenter;
        listBeans = response.getList();
    }

    @NonNull
    @Override
    public NotificationVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View itemView = layoutInflater.inflate(R.layout.item_notification_rv, parent, false);
        return new NotificationVH(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull NotificationVH holder, int position) {
        NotificationsResponse.ListBean bean = listBeans.get(position);
        if (readedList.size() > 0)
            readedList.set(position, bean.isReaded());

        if (bean.isReaded()) {
            holder.mNode.setVisibility(View.GONE);
            holder.mReaded.setVisibility(View.VISIBLE);
        } else {
            holder.mNode.setVisibility(View.VISIBLE);
            holder.mReaded.setVisibility(View.GONE);
        }


        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPresenter.read(bean.getSourceID());
                readedList.set(position, true);
            }
        });

        holder.mTitle.setText(bean.getAction());
        holder.mContent.setText("进度");
    }

    @Override
    public int getItemCount() {
        Log.e("Notification adapter", "number=" + listBeans.size());
        return listBeans.size();
    }

    public void setUnread(int position) {
        readedList.set(position, false);
        notifyItemChanged(position);
    }

    public void setListBeans(NotificationsResponse notificationsResponse) {
        listBeans = notificationsResponse.getList();
        notifyDataSetChanged();
    }

    public void clearAll() {
        listBeans.clear();
        notifyDataSetChanged();
    }

    class NotificationVH extends RecyclerView.ViewHolder {
        TextView mTitle, mContent, mReaded;
        View mItemView, mNode;

        NotificationVH(@NonNull View itemView) {
            super(itemView);
            mItemView = itemView;
            mTitle = itemView.findViewById(R.id.item_notification_tittle);
            mContent = itemView.findViewById(R.id.item_notification_content);
            mReaded = itemView.findViewById(R.id.get_it);
            mNode = itemView.findViewById(R.id.item_notification_node);
        }
    }
}
