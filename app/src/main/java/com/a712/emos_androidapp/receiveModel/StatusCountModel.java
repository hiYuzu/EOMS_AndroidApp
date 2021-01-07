package com.a712.emos_androidapp.receiveModel;

/**
 * Created by Administrator on 2017/3/30.
 */

public class StatusCountModel extends BaseModel{
    private String statusCode;//状态编码
    private String statusName;//状态名称
    private String statusCount;//状态个数

    /**
     * @return the statusCode
     */
    public String getStatusCode() {
        return statusCode;
    }
    /**
     * @param statusCode the statusCode to set
     */
    public void setStatusCode(String statusCode) {
        this.statusCode = statusCode;
    }
    /**
     * @return the statusName
     */
    public String getStatusName() {
        return statusName;
    }
    /**
     * @param statusName the statusName to set
     */
    public void setStatusName(String statusName) {
        this.statusName = statusName;
    }
    /**
     * @return the statusCount
     */
    public String getStatusCount() {
        return statusCount;
    }
    /**
     * @param statusCount the statusCount to set
     */
    public void setStatusCount(String statusCount) {
        this.statusCount = statusCount;
    }

}
