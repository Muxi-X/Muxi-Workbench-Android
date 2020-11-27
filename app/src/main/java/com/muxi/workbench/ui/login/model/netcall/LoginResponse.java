package com.muxi.workbench.ui.login.model.netcall;

public class LoginResponse {


    /**
     * code : 0
     * message : OK
     * data : {"token":"eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJleHAiOjE2MDQ4ODg2NjksImlkIjoxNDIsInJvbGUiOjEsInRlYW1faWQiOjF9.PNCqJNAR-nDEjL4H6TvoHrFgpV70vMWfSjB_s2SVbkE","redirect_url":""}
     */

    private int code;
    private String message;
    /**
     * token : eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJleHAiOjE2MDQ4ODg2NjksImlkIjoxNDIsInJvbGUiOjEsInRlYW1faWQiOjF9.PNCqJNAR-nDEjL4H6TvoHrFgpV70vMWfSjB_s2SVbkE
     * redirect_url :
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
        private String token;
        private String redirectUrl;

        public String getToken() {
            return token;
        }

        public void setToken(String token) {
            this.token = token;
        }

        public String getRedirectUrl() {
            return redirectUrl;
        }

        public void setRedirectUrl(String redirectUrl) {
            this.redirectUrl = redirectUrl;
        }
    }
}
