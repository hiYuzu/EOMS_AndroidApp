package com.a712.emos_androidapp.receiveModel;

/**
 * Created by Lenovo on 2017/7/3.
 */

public class DeviceCheckedModel extends BaseModel {

    private String deviceId;
    private String deviceName;
    private String deviceModel;
    private boolean checked;
    private String replaceTime;

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public String getDeviceName() {
        return deviceName;
    }

    public void setDeviceName(String deviceName) {
        this.deviceName = deviceName;
    }

    public String getDeviceModel() {
        return deviceModel;
    }

    public void setDeviceModel(String deviceModel) {
        this.deviceModel = deviceModel;
    }

    public boolean getChecked() {
        return checked;
    }

    public void setChecked(boolean checked) {
        this.checked = checked;
    }

    public String getReplaceTime() {
        return replaceTime;
    }

    public void setReplaceTime(String replaceTime) {
        this.replaceTime = replaceTime;
    }
}
