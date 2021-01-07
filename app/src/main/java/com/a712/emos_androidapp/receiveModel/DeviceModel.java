package com.a712.emos_androidapp.receiveModel;

/**
 * Created by Administrator on 2017/4/11.
 */

public class DeviceModel extends BaseModel{
    private int outletDeviceId;// 递增编号
    private String deviceName;//设备名称
    private String companyCode;//公司编号
    private String outletCode;//排口编号
    private String companyName;//公司名称
    private String outletName;//排口名称
    private String qrCode;// 序列号
    private String deviceMn;//数采仪MN号
    private String inspectFlag;//监控状态
    private String deviceModel;// 设备型号
    private String deviceTypeCode;// 设备类型编号
    private String deviceTypeName;// 设备类型名称
    private String mfrCode;// 生产厂家编码
    private String mfrName;// 生产厂家名称
    private String installTime;// 安装时间

    public int getOutletDeviceId() {
        return outletDeviceId;
    }

    public void setOutletDeviceId(int outletDeviceId) {
        this.outletDeviceId = outletDeviceId;
    }

    public String getDeviceName() {
        return deviceName;
    }

    public void setDeviceName(String deviceName) {
        this.deviceName = deviceName;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getOutletName() {
        return outletName;
    }

    public void setOutletName(String outletName) {
        this.outletName = outletName;
    }

    public String getQrCode() {
        return qrCode;
    }

    public void setQrCode(String qrCode) {
        this.qrCode = qrCode;
    }

    public String getDeviceMn() {
        return deviceMn;
    }

    public void setDeviceMn(String deviceMn) {
        this.deviceMn = deviceMn;
    }

    public String getInspectFlag() {
        return inspectFlag;
    }

    public void setInspectFlag(String inspectFlag) {
        this.inspectFlag = inspectFlag;
    }

    public String getCompanyCode() {
        return companyCode;
    }

    public void setCompanyCode(String companyCode) {
        this.companyCode = companyCode;
    }

    public String getOutletCode() {
        return outletCode;
    }

    public void setOutletCode(String outletCode) {
        this.outletCode = outletCode;
    }

    public String getDeviceModel() {
        return deviceModel;
    }

    public void setDeviceModel(String deviceModel) {
        this.deviceModel = deviceModel;
    }

    public String getDeviceTypeCode() {
        return deviceTypeCode;
    }

    public void setDeviceTypeCode(String deviceTypeCode) {
        this.deviceTypeCode = deviceTypeCode;
    }

    public String getDeviceTypeName() {
        return deviceTypeName;
    }

    public void setDeviceTypeName(String deviceTypeName) {
        this.deviceTypeName = deviceTypeName;
    }

    public String getMfrCode() {
        return mfrCode;
    }

    public void setMfrCode(String mfrCode) {
        this.mfrCode = mfrCode;
    }

    public String getMfrName() {
        return mfrName;
    }

    public void setMfrName(String mfrName) {
        this.mfrName = mfrName;
    }

    public String getInstallTime() {
        return installTime;
    }

    public void setInstallTime(String installTime) {
        this.installTime = installTime;
    }
}
