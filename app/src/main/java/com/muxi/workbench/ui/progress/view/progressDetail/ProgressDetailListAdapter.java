package com.muxi.workbench.ui.progress.view.progressDetail;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.muxi.workbench.R;
import com.muxi.workbench.ui.login.model.UserWrapper;
import com.muxi.workbench.ui.progress.contract.ProgressDetailContract;
import com.muxi.workbench.ui.progress.model.Comment;
import com.muxi.workbench.ui.progress.model.Progress;

import java.util.List;

public class ProgressDetailListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context mContext;

    private Progress mProgress;

    private List<Comment> mCommentList;

    private int uid = UserWrapper.getInstance().getUser().getUid();

    private ProgressDetailListener mProgressDetailListener;

    public interface ProgressDetailListener {

        void onLikeClick(); //进度详情下方的喜欢图标和文字的点击监听

        void onEditClick(); //个人进度详情下方的编辑图标和文字的点击监听

        void onCommentClick(); //进度详情下的评论图标和文字的点击监听

        void onUserClick(); //进度和评论的用户头像和用户名的点击监听

        void onDeleteCommentClick(); //个人评论的删除的点击监听

    }

    public ProgressDetailListAdapter(Context context, Progress progress, List<Comment> commentList, ProgressDetailListener progressDetailListener) {
        this.mContext = context;
        this.mProgress = progress;
        this.mCommentList = commentList;
        this.mProgressDetailListener = progressDetailListener;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if ( viewType == 0 ) {
            return new ContentViewHolder(LayoutInflater.from(mContext).inflate(R.layout.item_progresscontent_progressdetail_list, parent, false));
        } else {
            return new CommentViewHolder(LayoutInflater.from(mContext).inflate(R.layout.item_progresscomment_progressdetail_list, parent, false));
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        if ( holder instanceof ContentViewHolder ) {

            ContentViewHolder mholder = (ContentViewHolder)holder;

            if ( uid == mProgress.getUid() ) {
                mholder.mEditTv.setClickable(true);
                mholder.mEditIv.setClickable(true);
                mholder.mEditIv.setImageResource(R.drawable.editing_icon);
                mholder.mEditTv.setText("评论");
                mholder.mEditTv.setVisibility(View.VISIBLE);
                mholder.mEditIv.setVisibility(View.VISIBLE);
                mholder.mEditIv.setOnClickListener(v -> mProgressDetailListener.onEditClick());
                mholder.mEditTv.setOnClickListener(v -> mProgressDetailListener.onEditClick());
            } else {
                mholder.mEditTv.setClickable(false);
                mholder.mEditIv.setClickable(false);
                mholder.mEditTv.setVisibility(View.INVISIBLE);
                mholder.mEditIv.setVisibility(View.INVISIBLE);
            }

            if ( mProgress.getIfLike() == 1 ) {
                mholder.mLikeIv.setImageResource(R.drawable.like_red);
            } else {
                mholder.mLikeIv.setImageResource(R.drawable.like_none);
            }

            if ( mProgress.getLikeCount() == 0 ) {
                mholder.mLikeTv.setText("赞");
            } else {
                mholder.mLikeTv.setText(mProgress.getLikeCount());
            }

            if ( mProgress.getCommentCount() == 0 ) {
                mholder.mCommentTv.setText("评论");
            } else {
                mholder.mCommentTv.setText(mProgress.getCommentCount());
            }

            mholder.mTitleTv.setText(mProgress.getTitle());
            mholder.mAvatarSdv.setImageURI(mProgress.getAvatar());
            mholder.mUsernameTv.setText(mProgress.getUsername());
            mholder.mTimeTv.setText(mProgress.getTime());
            mholder.mContentWv.loadData(mProgress.getContent(),"text/html","UTF-8");
            mholder.mCommentIv.setImageResource(R.drawable.comment_icon);

            mholder.mLikeTv.setOnClickListener(v -> mProgressDetailListener.onLikeClick());
            mholder.mLikeIv.setOnClickListener(v -> mProgressDetailListener.onLikeClick());
            mholder.mAvatarSdv.setOnClickListener(v -> mProgressDetailListener.onUserClick());
            mholder.mUsernameTv.setOnClickListener(v -> mProgressDetailListener.onUserClick());
            mholder.mCommentIv.setOnClickListener(v -> mProgressDetailListener.onCommentClick());
            mholder.mCommentTv.setOnClickListener(v -> mProgressDetailListener.onCommentClick());

        } else if ( holder instanceof CommentViewHolder) {

            CommentViewHolder mholder = (CommentViewHolder)holder;
            Comment comment = mCommentList.get(position-1);

            if ( uid == comment.getUid()) {
                mholder.mDeleteTv.setVisibility(View.VISIBLE);
                mholder.mDeleteTv.setText("删除");
                mholder.mDeleteTv.setClickable(true);
                mholder.mDeleteTv.setOnClickListener(v -> mProgressDetailListener.onDeleteCommentClick());
            } else {
                mholder.mDeleteTv.setVisibility(View.INVISIBLE);
                mholder.mDeleteTv.setClickable(true);
            }

            mholder.mAvatarSdv.setImageURI(comment.getAvatar());
            mholder.mContentTv.setText(comment.getContent());
            mholder.mTimeTv.setText(comment.getTime());
            mholder.mUsernameTv.setText(comment.getUsername());

            mholder.mAvatarSdv.setOnClickListener(v -> mProgressDetailListener.onUserClick());
            mholder.mUsernameTv.setOnClickListener(v -> mProgressDetailListener.onUserClick());

        }
    }

    @Override
    public int getItemCount() {
        return mCommentList.size()+1;
    }

    @Override
    public int getItemViewType(int position) {
        if ( position == 0 )
            return 0;
        else return 1;
    }

    public void refresh (Progress progress, List<Comment> commentList) {
        mProgress = progress;
        mCommentList = commentList;
        notifyDataSetChanged();
    }

    class ContentViewHolder extends RecyclerView.ViewHolder {

        SimpleDraweeView mAvatarSdv;
        TextView mUsernameTv;
        TextView mTimeTv;
        WebView mContentWv;
        ImageView mLikeIv;
        TextView mLikeTv;
        ImageView mCommentIv;
        TextView mCommentTv;
        ImageView mEditIv;
        TextView mEditTv;
        TextView mTitleTv;

        public ContentViewHolder(@NonNull View itemView) {
            super(itemView);
            mAvatarSdv = itemView.findViewById(R.id.sdv_avatar_progressdetail_content_item);
            mUsernameTv = itemView.findViewById(R.id.tv_username_progressdetail_content_item);
            mTimeTv = itemView.findViewById(R.id.tv_time_progressdetail_content_item);
            mContentWv = itemView.findViewById(R.id.wv_content_progressdetail_content_item);
            mLikeIv = itemView.findViewById(R.id.iv_like_progressdetail_content_item);
            mLikeTv = itemView.findViewById(R.id.tv_like_progressdetail_content_item);
            mCommentIv = itemView.findViewById(R.id.iv_comment_progressdetail_content_item);
            mCommentTv = itemView.findViewById(R.id.tv_comment_progressdetail_content_item);
            mEditIv = itemView.findViewById(R.id.iv_edit_progressdetail_content_item);
            mEditTv = itemView.findViewById(R.id.tv_edit_progressdetail_content_item);
            mTitleTv = itemView.findViewById(R.id.tv_title_progressdetail_content_item);
        }
    }

    class CommentViewHolder extends RecyclerView.ViewHolder {

        SimpleDraweeView mAvatarSdv;
        TextView mUsernameTv;
        TextView mTimeTv;
        TextView mContentTv;
        TextView mDeleteTv;

        public CommentViewHolder(@NonNull View itemView) {
            super(itemView);
            mAvatarSdv = itemView.findViewById(R.id.sdv_avatar_progressdetail_comment_item);
            mUsernameTv = itemView.findViewById(R.id.tv_username_progressdetail_comment_item);
            mTimeTv = itemView.findViewById(R.id.tv_time_progressdetail_comment_item);
            mDeleteTv = itemView.findViewById(R.id.tv_delete_progressdetail_comment_item);
            mContentTv = itemView.findViewById(R.id.tv_content_progressdetail_comment_item);
        }
    }

}
