package com.muxi.workbench.ui.login.model;

public class User {

    private String account;
    private String password;
    private String token;
    private int uid;
    private int urole;


    public User(){}
    public User(String account, String password, String token) {
        this.account = account;
        this.password = password;
        this.token = token;

    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public int getUid() {
        return uid;
    }

    public void setUid(int uid) {
        this.uid = uid;
    }

    public int getUrole() {
        return urole;
    }

    public void setUrole(int urole) {
        this.urole = urole;
    }
}
