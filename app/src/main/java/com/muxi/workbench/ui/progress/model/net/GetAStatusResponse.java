package com.muxi.workbench.ui.progress.model.net;

public class GetAStatusResponse {

    /**
     * code : 0
     * message : OK
     * data : {"sid":4841,"title":"test","content":"test","userid":73,"time":"2020-10-01 22:12:50"}
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
         * sid : 4841
         * title : test
         * content : test
         * userid : 73
         * time : 2020-10-01 22:12:50
         */

        private int sid;
        private String title;
        private String content;
        private int userid;
        private String time;

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

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public int getUserid() {
            return userid;
        }

        public void setUserid(int userid) {
            this.userid = userid;
        }

        public String getTime() {
            return time;
        }

        public void setTime(String time) {
            this.time = time;
        }
    }
}
