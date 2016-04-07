package com.htlc.cykf.model;

/**
 * Created by sks on 2016/2/19.
 */
public class UserBean {
    public String userid;
    public String token;
    public String username;
    public String password;
    public String name;
    public String photo;
    public String flag;//用户信息完善进度

    @Override
    public String toString() {
        return "UserBean{" +
                "userid='" + userid + '\'' +
                ", token='" + token + '\'' +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", name='" + name + '\'' +
                ", photo='" + photo + '\'' +
                ", flag='" + flag + '\'' +
                '}';
    }
}
