package com.muxi.workbench.ui.login.model.netcall;

public class OauthResponse {


    /**
     * code : 0
     * message : OK
     * data : {"code":"NIRRHYJXNJ2SHL_0JVXJLG","expired":1800}
     */

    private int code;
    private String message;
    /**
     * code : NIRRHYJXNJ2SHL_0JVXJLG
     * expired : 1800
     */

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
        private String code;
        private int expired;

        public String getCode() {
            return code;
        }

        public void setCode(String code) {
            this.code = code;
        }

        public int getExpired() {
            return expired;
        }

        public void setExpired(int expired) {
            this.expired = expired;
        }
    }
}
