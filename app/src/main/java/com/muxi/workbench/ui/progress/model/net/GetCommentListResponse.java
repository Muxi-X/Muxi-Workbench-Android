package com.muxi.workbench.ui.progress.model.net;

import java.util.List;

public class GetCommentListResponse {
    /**
     * code : 0
     * message : OK
     * data : {"count":3,"commentlist":[{"cid":1163,"uid":115,"username":"苏亚鹏","avatar":"https://static.muxixyz.com/workbench/avatar/4.png","time":"2021-03-29 11:28:23","content":"<div>111<\/div>"},{"cid":1162,"uid":115,"username":"苏亚鹏","avatar":"https://static.muxixyz.com/workbench/avatar/4.png","time":"2021-03-29 11:21:23","content":"yy"},{"cid":1161,"uid":115,"username":"苏亚鹏","avatar":"https://static.muxixyz.com/workbench/avatar/4.png","time":"2021-03-29 11:21:22","content":"yy"}]}
     */

    private int code;
    private String message;
    private DataBean data;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * count : 3
         * commentlist : [{"cid":1163,"uid":115,"username":"苏亚鹏","avatar":"https://static.muxixyz.com/workbench/avatar/4.png","time":"2021-03-29 11:28:23","content":"<div>111<\/div>"},{"cid":1162,"uid":115,"username":"苏亚鹏","avatar":"https://static.muxixyz.com/workbench/avatar/4.png","time":"2021-03-29 11:21:23","content":"yy"},{"cid":1161,"uid":115,"username":"苏亚鹏","avatar":"https://static.muxixyz.com/workbench/avatar/4.png","time":"2021-03-29 11:21:22","content":"yy"}]
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
             * cid : 1163
             * uid : 115
             * username : 苏亚鹏
             * avatar : https://static.muxixyz.com/workbench/avatar/4.png
             * time : 2021-03-29 11:28:23
             * content : <div>111</div>
             */

            private int cid;
            private int uid;
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
}
