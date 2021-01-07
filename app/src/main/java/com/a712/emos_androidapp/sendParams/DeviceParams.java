package com.a712.emos_androidapp.sendParams;

/**
 * Created by Administrator on 2017/3/14.
 */

public class DeviceParams {
    private int outletDeviceId;
    private String deviceId;// 递增字段
    private String userCode;
    private String qrCode;// 设备二维码编号
    private int rowStart;
    private int rowCount;

    public int getOutletDeviceId() {
        return outletDeviceId;
    }

    public void setOutletDeviceId(int outletDeviceId) {
        this.outletDeviceId = outletDeviceId;
    }

    public String getUserCode() {
        return userCode;
    }

    public void setUserCode(String userCode) {
        this.userCode = userCode;
    }

    public int getRowStart() {
        return rowStart;
    }

    public void setRowStart(int rowStart) {
        this.rowStart = rowStart;
    }

    public int getRowCount() {
        return rowCount;
    }

    public void setRowCount(int rowCount) {
        this.rowCount = rowCount;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }


    public String getQrCode() {
        return qrCode;
    }

    public void setQrCode(String qrCode) {
        this.qrCode = qrCode;
    }
}
