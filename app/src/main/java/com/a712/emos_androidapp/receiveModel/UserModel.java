package com.a712.emos_androidapp.receiveModel;

/**
 * Created by Administrator on 2017/4/14.
 */

public class UserModel extends BaseModel{
    private String userCode;
    private String userName;
    private int userFlag;

    @Override
    public String toString() {
        return userName;
    }

    public int getUserFlag() {
        return userFlag;
    }

    public void setUserFlag(int userFlag) {
        this.userFlag = userFlag;
    }

    public String getUserCode() {
        return userCode;
    }

    public void setUserCode(String userCode) {
        this.userCode = userCode;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }
}
