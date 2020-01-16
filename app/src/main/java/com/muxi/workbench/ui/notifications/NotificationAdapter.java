package com.muxi.workbench.ui.notifications;

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
    private List<NotificationsBean.ListBean> listBeans = new ArrayList<>();
    private List<Boolean> readedList = new ArrayList<>();

    @NonNull
    @Override
    public NotificationVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new NotificationVH(
                LayoutInflater.from(parent.getContext()).
                        inflate(R.layout.item_notification_rv, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull NotificationVH holder, int position) {
        NotificationsBean.ListBean bean = listBeans.get(position);

        readedList.set(position, bean.isReaded());

        if (readedList.get(position)) {
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
        return listBeans.size();
    }

    public void setUnread(int position) {
        readedList.set(position, false);
        notifyItemChanged(position);
    }

    public void setListBeans(NotificationsBean notificationsBean) {
        listBeans = notificationsBean.getList();


    }

    class NotificationVH extends RecyclerView.ViewHolder {
        TextView mTitle, mContent, mReaded, mNode;
        View mItemView;

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
