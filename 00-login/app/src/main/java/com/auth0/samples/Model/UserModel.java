package com.auth0.samples.Model;

/**
 * Created by ayush on 19/3/18.
 */

public class UserModel {
    private int userid;

    private long userCode;

    private String userName;

    public int getUserid() {
        return userid;
    }

    public void setUserid(int userid) {
        this.userid = userid;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public long getUserCode() {
        return userCode;
    }

    public void setUserCode(long userCode) {
        this.userCode = userCode;
    }


}
