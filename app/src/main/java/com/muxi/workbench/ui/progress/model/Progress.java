package com.muxi.workbench.ui.progress.model;

import android.os.Build;
import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.RequiresApi;

public class Progress implements Parcelable {

    private int sid;
    private String avatar;
    private String username;
    private String time;
    private String title;
    private String content;
    private boolean ifLike;
    private int comment_count;
    private int like_count;
    private boolean isSticky;


    public Progress() {
    }

    public Progress(int sid, String avatar, String username, String time, String title, String content, boolean ifLike, int comment_count, int like_count) {
        this.sid = sid;
        this.avatar = avatar;
        this.username = username;
        this.time = time;
        this.title = title;
        this.content = content;
        this.ifLike = ifLike;
        this.comment_count = comment_count;
        this.like_count = like_count;
        this.isSticky = false;
    }

    public Progress(StickyProgress stickyProgress) {
        this.sid = stickyProgress.getSid();
        this.avatar = stickyProgress.getAvatar();
        this.username = stickyProgress.getUsername();
        this.time = stickyProgress.getTime();
        this.title = stickyProgress.getTitle();
        this.content = stickyProgress.getContent();
        this.ifLike = stickyProgress.getIfLike() == 1;
        this.comment_count = stickyProgress.getCommentCount();
        this.like_count = stickyProgress.getLikeCount();
        this.isSticky = true;
    }

    @RequiresApi(api = Build.VERSION_CODES.Q)
    public Progress(Parcel parcel) {
        this.sid = parcel.readInt();
        this.avatar = parcel.readString();
        this.username = parcel.readString();
        this.time = parcel.readString();
        this.title = parcel.readString();
        this.content = parcel.readString();
        this.ifLike = parcel.readBoolean();
        this.comment_count = parcel.readInt();
        this.like_count = parcel.readInt();
        this.isSticky =  parcel.readByte() != 0;
    }

    public int getSid() {
        return sid;
    }

    public void setSid(int sid) {
        this.sid = sid;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getIfLike() {
        if(ifLike)
            return 1;
        else return 0;

    }

    public void setIfLike(boolean ifLike) {
        this.ifLike = ifLike;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getCommentCount() {
        return comment_count;
    }

    public void setCommentCount(int commentCount) {
        this.comment_count = commentCount;
    }

    public int getLikeCount() {
        return like_count;
    }

    public void setLikeCount(int likeCount) {
        this.like_count = likeCount;
    }

    public boolean isSticky() {
        return isSticky;
    }

    public void setSticky(boolean sticky) {
        isSticky = sticky;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @RequiresApi(api = Build.VERSION_CODES.Q)
    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(sid);
        dest.writeString(avatar);
        dest.writeString(username);
        dest.writeString(time);
        dest.writeString(title);
        dest.writeString(content);
        dest.writeBoolean(ifLike);
        dest.writeInt(comment_count);
        dest.writeInt(like_count);
        dest.writeByte((byte) (isSticky ? 1 : 0));
    }

    public static final Creator<Progress> CREATOR = new Creator<Progress>() {

        /**
         * 供外部类反序列化本类数组使用
         */
        @Override
        public Progress[] newArray(int size) {
            return new Progress[size];
        }

        /**
         * 从Parcel中读取数据
         */
        @RequiresApi(api = Build.VERSION_CODES.Q)
        @Override
        public Progress createFromParcel(Parcel source) {
            return new Progress(source);
        }
    };
}
