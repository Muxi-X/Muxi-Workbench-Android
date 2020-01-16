package com.muxi.workbench.ui.progress.model;

public class Progress {

    private int sid;
    private int uid;
    private String avatar;
    private String username;
    private String time;
    private String title;
    private String content;
    private int ifLike;
    private int commentCount;
    private int likeCount;
    private boolean isSticky;

    public Progress() {

    }

    public Progress(int sid, int uid, String avatar, String username, String time, String title, String content, int ifLike, int commentCount, int likeCount) {
        this.sid = sid;
        this.uid = uid;
        this.avatar = avatar;
        this.username = username;
        this.time = time;
        this.title = title;
        this.content = content;
        this.ifLike = ifLike;
        this.commentCount = commentCount;
        this.likeCount = likeCount;
        this.isSticky = false;
    }

    public Progress(StickyProgress stickyProgress) {
        this.sid = stickyProgress.getSid();
        this.uid = stickyProgress.getUid();
        this.avatar = stickyProgress.getAvatar();
        this.username = stickyProgress.getUsername();
        this.time = stickyProgress.getTime();
        this.title = stickyProgress.getTitle();
        this.content = stickyProgress.getContent();
        this.ifLike = stickyProgress.getIfLike();
        this.commentCount = stickyProgress.getCommentCount();
        this.likeCount = stickyProgress.getLikeCount();
        this.isSticky = true;
    }

    public int getSid() {
        return sid;
    }

    public void setSid(int sid) {
        this.sid = sid;
    }

    public int getUid() {
        return uid;
    }

    public void setUid(int uid) {
        this.uid = uid;
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
        return ifLike;
    }

    public void setIfLike(int ifLike) {
        this.ifLike = ifLike;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getCommentCount() {
        return commentCount;
    }

    public void setCommentCount(int commentCount) {
        this.commentCount = commentCount;
    }

    public int getLikeCount() {
        return likeCount;
    }

    public void setLikeCount(int likeCount) {
        this.likeCount = likeCount;
    }

    public boolean isSticky() {
        return isSticky;
    }

    public void setSticky(boolean sticky) {
        isSticky = sticky;
    }
}
