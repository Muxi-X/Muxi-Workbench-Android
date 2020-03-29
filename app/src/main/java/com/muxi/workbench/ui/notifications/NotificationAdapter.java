package com.muxi.workbench.ui.notifications;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.muxi.workbench.R;
import com.muxi.workbench.ui.notifications.model.NotificationsResponse;

import java.util.ArrayList;
import java.util.List;

public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.NotificationVH> {

    private NotificationContact.Presenter mPresenter;
    private List<NotificationsResponse.ListBean> listBeans = new ArrayList<>();
    private List<Boolean> readedList = new ArrayList<>();
    private OnItemClickListener mListener;

    public NotificationAdapter(NotificationContact.Presenter presenter,
                               NotificationsResponse response,
                               OnItemClickListener listener) {
        mPresenter = presenter;
        listBeans = response.getList();
        setReadedList(listBeans);
        mListener = listener;
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
                if (!readedList.get(position)) {
                    mPresenter.read(bean.getSourceID());
                    readedList.set(position, true);
                }
                mListener.onClick(bean.getSourceID(), "");

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
        listBeans.clear();
        listBeans.addAll(notificationsResponse.getList());
        setReadedList(listBeans);
        notifyDataSetChanged();
    }

    public void clearAll() {
        listBeans.clear();
        notifyDataSetChanged();
    }

    private void setReadedList(List<NotificationsResponse.ListBean> listBeans) {
        if (readedList.size() > 0) readedList.clear();

        for (NotificationsResponse.ListBean bean : listBeans) {
            readedList.add(bean.isReaded());
        }
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

    interface OnItemClickListener {
        void onClick(int objectId, String docName);
    }
}
