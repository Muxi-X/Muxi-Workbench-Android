package com.muxi.workbench.ui.progress.model.net;

import java.util.List;

public class GetStatusListResponse {

    /**
     * statuList : [
     * {"sid":0,
     * "username":"string",
     * "uid":0,
     * "time":"string",
     * "avatar":"string",
     * "title":"string",
     * "content":"string",
     * "likeCount":0,
     * "iflike":true,
     * "commentCount":0}]
     * page : 0
     * count : 0
     */

    private int page;
    private int count;
    private List<StatuListBean> statuList;

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public List<StatuListBean> getStatuList() {
        return statuList;
    }

    public void setStatuList(List<StatuListBean> statuList) {
        this.statuList = statuList;
    }

    public static class StatuListBean {
        /**
         * sid : 0
         * username : string
         * uid : 0
         * time : string
         * avatar : string
         * title : string
         * content : string
         * likeCount : 0
         * iflike : true
         * commentCount : 0
         */

        private int sid;
        private String username;
        private int uid;
        private String time;
        private String avatar;
        private String title;
        private String content;
        private int likeCount;
        private int iflike;
        private int commentCount;

        public int getSid() {
            return sid;
        }

        public void setSid(int sid) {
            this.sid = sid;
        }

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public int getUid() {
            return uid;
        }

        public void setUid(int uid) {
            this.uid = uid;
        }

        public String getTime() {
            return time;
        }

        public void setTime(String time) {
            this.time = time;
        }

        public String getAvatar() {
            return avatar;
        }

        public void setAvatar(String avatar) {
            this.avatar = avatar;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public int getLikeCount() {
            return likeCount;
        }

        public void setLikeCount(int likeCount) {
            this.likeCount = likeCount;
        }

        public int isIflike() {
            return iflike;
        }

        public void setIflike(int iflike) {
            this.iflike = iflike;
        }

        public int getCommentCount() {
            return commentCount;
        }

        public void setCommentCount(int commentCount) {
            this.commentCount = commentCount;
        }
    }
}
