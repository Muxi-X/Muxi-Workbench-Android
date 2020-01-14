package com.muxi.workbench.ui.progress.view;

import android.content.Context;
import android.util.Log;
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
import com.muxi.workbench.ui.progress.ProgressContract;
import com.muxi.workbench.ui.progress.model.Progress;

import org.jsoup.Jsoup;

import java.util.List;

public class ProgressListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private int uid = UserWrapper.getInstance().getUser().getUid();
    private List<Progress> ProgressList;
    private Context mContext;
    private ProgressItemListener mItemListener;
    private ProgressContract.Presenter mPresenter;

    public ProgressListAdapter (ProgressContract.Presenter mPresenter, Context mContext, List<Progress> progressList, ProgressItemListener itemListener) {
        this.mContext = mContext;
        this.ProgressList = progressList;
        this.mItemListener = itemListener;
        this.mPresenter = mPresenter;
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

            String Acontent = Jsoup.parse(progress.getContent()).body().wholeText();

            while ( Acontent.length() > 0 && Acontent.charAt(0) == '\n' )
                Acontent = Acontent.substring(1);
            while ( Acontent.length() > 0 && Acontent.charAt(Acontent.length() -1) == '\n' )
                Acontent = Acontent.substring(0,Acontent.length()-1);

            if (progress.getContent().contains("img"))
                Acontent = Acontent.concat("\n[图片]");


            if (Acontent.length() > 150 ) {
                Acontent = Acontent.substring(0,150);
                mholder.moreContentTv.setVisibility(View.VISIBLE);
            } else {
                int count = 0;
                for ( int i = 0 ; i < Acontent.length() ; i++ ) {
                    if ( Acontent.charAt(i) == '\n' )
                        count++;
                    if ( count > 8 ) {
                        Acontent = Acontent.substring(0,i);
                        mholder.moreContentTv.setVisibility(View.VISIBLE);
                        break;
                    }
                }
                if ( count < 8 )
                    mholder.moreContentTv.setVisibility(View.GONE);
            }
            mholder.usernameTv.setText(progress.getUsername());
            mholder.timeTv.setText(progress.getTime());
            mholder.contentTv.setText(Acontent);
            mholder.avatarSdv.setImageURI(progress.getAvatar());

            if ( progress.isIfLike() == 1 )
                mholder.likeIv.setImageResource(R.drawable.like_red);
            else mholder.likeIv.setImageResource(R.drawable.like_none);

            if ( progress.getLikeCount() == 0 )
                mholder.likeTv.setText("赞");
            else mholder.likeTv.setText(progress.getLikeCount()+"");

            if ( progress.getCommentCount() == 0 )
                mholder.commentTv.setText("评论");
            else mholder.commentTv.setText(progress.getCommentCount()+"");

            if ( progress.isSticky() )
                mholder.stickyTv.setVisibility(View.VISIBLE);
            else mholder.stickyTv.setVisibility(View.GONE);

            if ( progress.getUid() != uid ) {
                mholder.editTv.setClickable(false);
                mholder.editIv.setClickable(false);
                mholder.editIv.setVisibility(View.INVISIBLE);
                mholder.editTv.setVisibility(View.INVISIBLE);
            } else {
                mholder.editIv.setVisibility(View.VISIBLE);
                mholder.editTv.setVisibility(View.VISIBLE);

                mholder.editIv.setOnClickListener(v -> mItemListener.onEditClick(ProgressList.get(position)));
                mholder.editTv.setOnClickListener(v -> mItemListener.onEditClick(ProgressList.get(position)));
            }

            mholder.itemView.setOnClickListener(v -> mItemListener.onItemClick(ProgressList.get(position)));
            mholder.avatarSdv.setOnClickListener(v -> mItemListener.onUserClick(ProgressList.get(position).getUid()));
            mholder.usernameTv.setOnClickListener(v -> mItemListener.onUserClick(ProgressList.get(position).getUid()));
            mholder.likeIv.setOnClickListener(v -> mItemListener.onLikeClick(ProgressList.get(position), position));
            mholder.likeTv.setOnClickListener(v -> mItemListener.onLikeClick(ProgressList.get(position), position));
            mholder.commentIv.setOnClickListener(v -> mItemListener.onCommentClick(ProgressList.get(position)));
            mholder.commentTv.setOnClickListener(v -> mItemListener.onCommentClick(ProgressList.get(position)));
            mholder.expandIv.setOnClickListener(v -> showPopupMenu(mholder.expandIv, mContext, uid == progress.getUid(), position, progress) );

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
        TextView moreContentTv;

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
            moreContentTv = view.findViewById(R.id.tv_more_content_progress_item);
        }
    }


    public void removeData (int position) {
        ProgressList.remove(position);
        notifyItemRemoved(position);
    }

    public void addMoreProgress (List<Progress> progresses) {
        Log.e("addMore",progresses.size()+"");
        int last = ProgressList.size();
        ProgressList.addAll(progresses);
        notifyItemRangeInserted(last, progresses.size());
    }

    public void notifyProgress(int position, int iflike) {
        ProgressList.get(position).setIfLike(iflike);
        ProgressList.get(position).setLikeCount(ProgressList.get(position).getLikeCount()+(iflike==1?1:-1));
        notifyItemChanged(position);
    }

    public void replaceData(List<Progress> progresslist) {
        ProgressList.clear();
        ProgressList.addAll(progresslist);
        notifyDataSetChanged();
    }

    public void moveProgress(int position, int operator) {
        if ( operator == 0 ) {
            Progress temp = ProgressList.get(position);
            ProgressList.remove(position);
            temp.setSticky(true);
            ProgressList.add(0, temp);
            notifyItemRangeChanged(0, position + 1);
        } else {
            Progress temp = ProgressList.get(position);
            ProgressList.remove(position);
            temp.setSticky(false);
            for ( int i = position ; i < ProgressList.size() ; i++ ) {
                if ( !ProgressList.get(i).isSticky() ) {
                    if ( ProgressList.get(i).getSid() < temp.getSid() ) {
                        ProgressList.add(i,temp);
                        break;
                    }
                }
            }
            if ( ProgressList.get(ProgressList.size()-1).getSid()-1 == temp.getSid() )
                ProgressList.add(temp);
            notifyItemRangeChanged(position,ProgressList.size()-position);
        }
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
        void onLikeClick(Progress likeProgress, int position);
        void onCommentClick(Progress commentProgress);
        void onEditClick(Progress editProgress);
    }

    public void showPopupMenu(View view, Context context, boolean isUser, int position, Progress progress) {
        PopupMenu popupMenu = new PopupMenu(context, view);
        if ( isUser ) {
            if ( !progress.isSticky() ) {
                popupMenu.getMenuInflater().inflate(R.menu.progress_expand_menu_user, popupMenu.getMenu());
                popupMenu.show();
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.progress_sticky_user:
                                mPresenter.setProgressSticky(position, progress);
                                return true;
                            case R.id.progress_delete_user:
                                mPresenter.deleteProgress(position, progress.getSid());
                                return true;
                        }
                        return true;
                    }
                });
            } else {
                popupMenu.getMenuInflater().inflate(R.menu.progress_expand_menu_sticky_user, popupMenu.getMenu());
                popupMenu.show();
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.progress_cancel_sticky_user:
                                mPresenter.cancelStickyProgress(position, progress);
                                return true;
                            case R.id.progress_sticky_delete_user:
                                mPresenter.deleteProgress(position, progress.getSid());
                                return true;
                        }
                        return true;
                    }
                });
            }
        } else {
            if ( !progress.isSticky() ) {
                popupMenu.getMenuInflater().inflate(R.menu.progress_expand_menu, popupMenu.getMenu());
                popupMenu.show();
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.progress_sticky:
                                mPresenter.setProgressSticky(position, progress);
                                return true;
                        }
                        return true;
                    }
                });
            } else {
                popupMenu.getMenuInflater().inflate(R.menu.progress_expand_menu_sticky, popupMenu.getMenu());
                popupMenu.show();
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.progress_cancel_sticky:
                                mPresenter.cancelStickyProgress(position, progress);
                                return true;
                        }
                        return true;
                    }
                });
            }
        }
        popupMenu.setOnDismissListener(new PopupMenu.OnDismissListener() {
            @Override
            public void onDismiss(PopupMenu menu) {
            }
        });
    }

}