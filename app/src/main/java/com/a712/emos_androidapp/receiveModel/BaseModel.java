package com.a712.emos_androidapp.receiveModel;

/**
 * Created by Administrator on 2017/3/30.
 */

public class BaseModel {

    /**
     * 是否成功
     */
    private boolean flag = false;
    /**
     * 详细信息
     */
    private String info;

    /**
     * @return the flag
     */
    public boolean getFlag() {
        return flag;
    }
    /**
     * @param flag the flag to set
     */
    public void setFlag(boolean flag) {
        this.flag = flag;
    }
    /**
     * @return the info
     */
    public String getInfo() {
        return info;
    }
    /**
     * @param info the info to set
     */
    public void setInfo(String info) {
        this.info = info;
    }

}
