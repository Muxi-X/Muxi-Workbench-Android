package com.muxi.workbench.ui.login.model;

import com.alibaba.fastjson.JSONObject;
import com.muxi.workbench.commonUtils.SPUtils;

public class UserWrapper {



    private User user;
    private SPUtils spUtils;
    //这里我选了饿汉式单例,关于饿汉式和懒汉式的选择可以查看知乎上这一回答https://www.zhihu.com/question/60307849
    private static UserWrapper instance=new UserWrapper();

    private UserWrapper(){
        spUtils=SPUtils.getInstance(SPUtils.SP_CONFIG);
        String userJson= spUtils.getString("user","");
        if (userJson.length()==0){
            user=null;
        }
        else {
            user = JSONObject.parseObject(userJson, User.class);
        }

    }

    public static UserWrapper getInstance(){
        return instance;
    }

    /**
     *
     *
     * @return 存储的user，如果没有则return null,
     * 表示还没有登录
     *
     */
    public User getUser(){
        return user;

    }
    public void setUser(User user){
        this.user=user;
        String json=JSONObject.toJSONString(user);
        spUtils.put("user",json,false);
    }

    public String getToken(){
        if (user==null)
            throw new NullPointerException("user=null");
        return user.getToken();
    }

}
