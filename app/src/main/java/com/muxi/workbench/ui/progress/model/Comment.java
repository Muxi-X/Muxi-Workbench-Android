package com.muxi.workbench.ui.progress.model;

public class Comment {

    //private int uid;
    private int cid;
    private String username;
    private String avatar;
    private String time;
    private String content;

    public Comment(int cid, String username, String avatar, String time, String content) {
      //  this.uid = uid;
        this.cid = cid;
        this.username = username;
        this.avatar = avatar;
        this.time = time;
        this.content = content;
    }

    //public int getUid() {
    //    return uid;
    //}

    //public void setUid(int uid) {
    //    this.uid = uid;
    //}

    public int getCid() {
        return cid;
    }

    public void setCid(int cid) {
        this.cid = cid;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
