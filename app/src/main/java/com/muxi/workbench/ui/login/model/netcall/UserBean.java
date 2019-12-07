package com.muxi.workbench.ui.login.model.netcall;

public class UserBean {

    /**
     * username : 904315105@qq.com
     * password : *****
     */

    private String username;
    private String password;

    public UserBean(){}
    public UserBean(String name,String password){
        this.username=name;
        this.password=password;
    }
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
