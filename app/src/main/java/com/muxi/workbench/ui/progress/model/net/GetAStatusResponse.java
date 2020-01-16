package com.muxi.workbench.ui.progress.model.net;

import java.util.List;

public class GetAStatusResponse {

    /**
     * sid : 0
     * title : string
     * author_id : 0
     * content : string
     * time : string
     * likeCount : 0
     * iflike : true
     * userID : 0
     * username : string
     * commentList : [{"cid":0,"username":"string","avatar":"string","time":"string","content":"string"}]
     */

    private int sid;
    private String title;
    private int author_id;
    private String content;
    private String time;
    private int likeCount;
    private boolean iflike;
    private int userID;
    private String username;
    private List<CommentListBean> commentList;

    public int getSid() {
        return sid;
    }

    public void setSid(int sid) {
        this.sid = sid;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getAuthor_id() {
        return author_id;
    }

    public void setAuthor_id(int author_id) {
        this.author_id = author_id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public int getLikeCount() {
        return likeCount;
    }

    public void setLikeCount(int likeCount) {
        this.likeCount = likeCount;
    }

    public boolean isIflike() {
        return iflike;
    }

    public void setIflike(boolean iflike) {
        this.iflike = iflike;
    }

    public int getUserID() {
        return userID;
    }

    public void setUserID(int userID) {
        this.userID = userID;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public List<CommentListBean> getCommentList() {
        return commentList;
    }

    public void setCommentList(List<CommentListBean> commentList) {
        this.commentList = commentList;
    }

    public static class CommentListBean {
        /**
         * cid : 0
         * username : string
         * avatar : string
         * time : string
         * content : string
         */

        private int cid;
        private String username;
        private String avatar;
        private String time;
        private String content;

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
}
