package com.muxi.workbench.ui.progress.view;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.PopupMenu;
import androidx.recyclerview.widget.RecyclerView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.muxi.workbench.R;
import com.muxi.workbench.ui.login.model.UserWrapper;
import com.muxi.workbench.ui.progress.model.Progress;

import java.util.ArrayList;
import java.util.List;

public class ProgressListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private int uid = UserWrapper.getInstance().getUser().getUid();
    private List<Progress> ProgressList;
    private Context mContext;
    private ProgressItemListener mItemListener;

    public ProgressListAdapter (Context mContext, List<Progress> progressList, ProgressItemListener itemListener) {
        this.mContext = mContext;
        this.ProgressList = progressList;
        this.mItemListener = itemListener;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == 0)
            return new MyViewHolder(LayoutInflater.from(mContext).inflate(R.layout.item_progress_list, parent, false));
        else
            return new MoreViewHolder(LayoutInflater.from(mContext).inflate(R.layout.item_more_progress_list, parent, false));
    }

    @Override
    public int getItemViewType(int position) {
        if ( position == ProgressList.size() )
            return 1;
        else return 0;
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {
        if (holder instanceof MyViewHolder) {

            MyViewHolder mholder = (MyViewHolder)holder;

            Progress progress = ProgressList.get(position);

            if ( progress.isIfLike() )
                mholder.likeIv.setImageResource(R.drawable.like_red);
            else mholder.likeIv.setImageResource(R.drawable.like_none);

            if ( progress.getLikeCount() == 0 )
                mholder.likeTv.setText("赞");
            else mholder.likeTv.setText(progress.getLikeCount());

            if ( progress.getCommentCount() == 0 )
                mholder.commentTv.setText("评论");
            else mholder.commentTv.setText(progress.getCommentCount());

            if ( progress.isSticky() )
                mholder.stickyTv.setVisibility(View.VISIBLE);
            else mholder.stickyTv.setVisibility(View.GONE);

            if ( progress.getUid() != uid ) {
                mholder.editIv.setVisibility(View.GONE);
                mholder.editTv.setVisibility(View.GONE);
            } else {
                mholder.editIv.setVisibility(View.VISIBLE);
                mholder.editTv.setVisibility(View.VISIBLE);

                mholder.editIv.setOnClickListener(v -> mItemListener.onEditClick(ProgressList.get(position)));
                mholder.editTv.setOnClickListener(v -> mItemListener.onEditClick(ProgressList.get(position)));
            }

            mholder.itemView.setOnClickListener(v -> mItemListener.onItemClick(ProgressList.get(position)));
            mholder.avatarSdv.setOnClickListener(v -> mItemListener.onUserClick(ProgressList.get(position).getUid()));
            mholder.usernameTv.setOnClickListener(v -> mItemListener.onUserClick(ProgressList.get(position).getUid()));
            mholder.likeIv.setOnClickListener(v -> mItemListener.onLikeClick(ProgressList.get(position)));
            mholder.likeTv.setOnClickListener(v -> mItemListener.onLikeClick(ProgressList.get(position)));
            mholder.commentIv.setOnClickListener(v -> mItemListener.onCommentClick(ProgressList.get(position)));
            mholder.commentTv.setOnClickListener(v -> mItemListener.onCommentClick(ProgressList.get(position)));
            mholder.expandIv.setOnClickListener(v -> showPopupMenu(mholder.expandIv, mContext, uid == progress.getUid()) );

        } else if (holder instanceof MoreViewHolder) {
            MoreViewHolder moreViewHolder = (MoreViewHolder)holder;
            moreViewHolder.moreTv.setOnClickListener(v -> mItemListener.onMoreClick());
        }
    }

    @Override
    public int getItemCount() {
        return ProgressList.size()+1;
    }

    class MyViewHolder extends RecyclerView.ViewHolder {

        SimpleDraweeView avatarSdv;
        TextView usernameTv;
        TextView timeTv;
        TextView contentTv;
        ImageView expandIv;
        ImageView likeIv;
        TextView likeTv;
        ImageView commentIv;
        TextView commentTv;
        ImageView editIv;
        TextView editTv;
        TextView stickyTv;

        public MyViewHolder(View view) {
            super(view);
            avatarSdv = view.findViewById(R.id.sdv_avatar_progress_item);
            usernameTv = view.findViewById(R.id.tv_username_progress_item);
            timeTv = view.findViewById(R.id.tv_time_progress_item);
            contentTv = view.findViewById(R.id.tv_content_progress_item);
            expandIv = view.findViewById(R.id.iv_expand_progress_item);
            likeIv = view.findViewById(R.id.iv_like_progress_item);
            likeTv = view.findViewById(R.id.tv_like_progress_item);
            commentIv = view.findViewById(R.id.iv_comment_progress_item);
            commentTv = view.findViewById(R.id.tv_comment_progress_item);
            editIv = view.findViewById(R.id.iv_edit_progress_item);
            editTv = view.findViewById(R.id.tv_edit_progress_item);
            stickyTv = view.findViewById(R.id.tv_sticky_tag_progress_item);
        }
    }


    public void removeData (int position) {
        ProgressList.remove(position);
        notifyItemRemoved(position);
    }

    public void addMoreProgress (int position, int count, List<Progress> progresses) {
        ProgressList.addAll(progresses);
        notifyItemRangeInserted(position, count);
    }

    public void notifyProgress(int position) {
        notifyItemChanged(position);
    }

    public void replaceData(List<Progress>progresslist) {
        ProgressList = progresslist;
        notifyDataSetChanged();
    }

    class MoreViewHolder extends RecyclerView.ViewHolder {
        TextView moreTv;
        public MoreViewHolder(View view) {
            super(view);
            moreTv = view.findViewById(R.id.tv_more_progress_item);
        }
    }

    public interface ProgressItemListener {
        void onItemClick(Progress clickedProgress);
        void onMoreClick();
        void onUserClick(int uid);
        void onLikeClick(Progress likeProgress);
        void onCommentClick(Progress commentProgress);
        void onEditClick(Progress editProgress);
    }

    public void showPopupMenu(View view, Context context, boolean isUser) {
        // 这里的view代表popupMenu需要依附的view
        PopupMenu popupMenu = new PopupMenu(context, view);
        // 获取布局文件
        if ( isUser ) {
            popupMenu.getMenuInflater().inflate(R.menu.progress_expand_menu_user, popupMenu.getMenu());
            popupMenu.show();
            // 通过上面这几行代码，就可以把控件显示出来了
            popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem item) {
                    switch (item.getItemId()) {
                        case R.id.progress_sticky_user :
                            return true;
                        case R.id.progress_delete_user :
                            return true;
                    }
                    return true;
                }
            });
        } else {
            popupMenu.getMenuInflater().inflate(R.menu.progress_expand_menu, popupMenu.getMenu());
            popupMenu.show();
            // 通过上面这几行代码，就可以把控件显示出来了
            popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem item) {
                    switch (item.getItemId()) {
                        case R.id.progress_sticky :
                            return true;
                    }
                    return true;
                }
            });
        }
        popupMenu.setOnDismissListener(new PopupMenu.OnDismissListener() {
            @Override
            public void onDismiss(PopupMenu menu) {
                // 控件消失时的事件
            }
        });
    }

}