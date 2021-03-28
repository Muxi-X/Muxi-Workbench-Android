package com.muxi.workbench.ui.login.model.netcall;

public class LoginResponse {


    /**
     * code : 0
     * message : OK
     * data : {"token":"eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpZCI6MTQyLCJyb2xlIjoxLCJ0ZWFtX2lkIjoxLCJleHBpcmVzX2F0IjoxNjEzODI4OTMyfQ.2pBj2QiJqWJjNDEH7jXitPYjb0utv25rp7azauH92oo","redirect_url":""}
     */

    private int code;
    private String message;
    /**
     * token : eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpZCI6MTQyLCJyb2xlIjoxLCJ0ZWFtX2lkIjoxLCJleHBpcmVzX2F0IjoxNjEzODI4OTMyfQ.2pBj2QiJqWJjNDEH7jXitPYjb0utv25rp7azauH92oo
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
        private String redirect_url;

        public String getToken() {
            return token;
        }

        public void setToken(String token) {
            this.token = token;
        }

        public String getRedirect_url() {
            return redirect_url;
        }

        public void setRedirect_url(String redirectUrl) {
            this.redirect_url = redirectUrl;
        }
    }

}
