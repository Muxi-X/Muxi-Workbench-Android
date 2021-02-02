package com.muxi.workbench.ui.progress.model.net;

import java.util.List;

public class GetCommentListResponse {
    /**
     * commentlist : [{"avatar":"string","cid":0,"content":"string","time":"string","uid":0,"username":"string"}]
     * count : 0
     */

    private int count;
    private List<CommentlistBean> commentlist;

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public List<CommentlistBean> getCommentlist() {
        return commentlist;
    }

    public void setCommentlist(List<CommentlistBean> commentlist) {
        this.commentlist = commentlist;
    }

    public static class CommentlistBean {
        /**
         * avatar : string
         * cid : 0
         * content : string
         * time : string
         * uid : 0
         * username : string
         */

        private String avatar;
        private int cid;
        private String content;
        private String time;
        private int uid;
        private String username;

        public String getAvatar() {
            return avatar;
        }

        public void setAvatar(String avatar) {
            this.avatar = avatar;
        }

        public int getCid() {
            return cid;
        }

        public void setCid(int cid) {
            this.cid = cid;
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

        public int getUid() {
            return uid;
        }

        public void setUid(int uid) {
            this.uid = uid;
        }

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }
    }
}
