package com.a712.emos_androidapp.sendParams;

/**
 * Created by Administrator on 2017/4/13.
 */

public class FaultParams {
    private String faultId;// 递增字段
    private String companyCode;//排污企业编码
    private String companyName;//排污企业名称
    private String outletId;//排口ID
    private String outletCode;//排口编码
    private String outletName;//排口名称
    private String outletDeviceId;//排口设备ID
    private String qrCode;//排口设备序列号
    private String deviceId;//设备id
    private String deviceName;//设备名称
    private String deviceModel;//规格型号
    private String faultType;//任务类型编码
    private String faultTypeName;//任务类型名称
    private String faultDescription;//任务描述
    private String faultReason;// 故障原因
    private String faultDeal;// 故障处理
    private String faultBeginTime;//开始时间
    private String faultEndTime;//结束时间
    private String faultHandleTime;//处理时间
    private String positionX;//维修位置X
    private String positionY;//维修位置Y
    private String positionDeviate;//位置判断信息
    private String isSolve;//是否解决编码
    private String isSolveName;//是否解决名称
    private String manageCode;//负责人编码
    private String manageName;//负责人名称
    private String workCode;//负责人编码
    private String workName;//负责人名称
    private String faultRemark;//故障备注
    private String recordFlag;//报备状态
    private String recordFlagName;//报备状态名称
    private String recordTime;//报备时间
    private String orgId;//组织id（做为查询条件）
    private String notifyTime;//发送给网关时间戳
    private String notifyFlag;//发送标志（0：发送失败；1：发送成功；2:发送中）
    private String notifyFlagName;//发送标志名称（0：发送失败；1：发送成功；2:发送中）
    private String phoneUserCode;//移动端登录用户

    public String getFaultId() {
        return faultId;
    }

    public void setFaultId(String faultId) {
        this.faultId = faultId;
    }

    public String getCompanyCode() {
        return companyCode;
    }

    public void setCompanyCode(String companyCode) {
        this.companyCode = companyCode;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getOutletId() {
        return outletId;
    }

    public void setOutletId(String outletId) {
        this.outletId = outletId;
    }

    public String getOutletCode() {
        return outletCode;
    }

    public void setOutletCode(String outletCode) {
        this.outletCode = outletCode;
    }

    public String getOutletName() {
        return outletName;
    }

    public void setOutletName(String outletName) {
        this.outletName = outletName;
    }

    public String getOutletDeviceId() {
        return outletDeviceId;
    }

    public void setOutletDeviceId(String outletDeviceId) {
        this.outletDeviceId = outletDeviceId;
    }

    public String getQrCode() {
        return qrCode;
    }

    public void setQrCode(String qrCode) {
        this.qrCode = qrCode;
    }

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


    public String getFaultType() {
        return faultType;
    }

    public void setFaultType(String faultType) {
        this.faultType = faultType;
    }

    public String getFaultTypeName() {
        return faultTypeName;
    }

    public void setFaultTypeName(String faultTypeName) {
        this.faultTypeName = faultTypeName;
    }

    public String getFaultDescription() {
        return faultDescription;
    }

    public void setFaultDescription(String faultDescription) {
        this.faultDescription = faultDescription;
    }

    public String getFaultReason() {
        return faultReason;
    }

    public void setFaultReason(String faultReason) {
        this.faultReason = faultReason;
    }

    public String getFaultDeal() {
        return faultDeal;
    }

    public void setFaultDeal(String faultDeal) {
        this.faultDeal = faultDeal;
    }

    public String getFaultBeginTime() {
        return faultBeginTime;
    }

    public void setFaultBeginTime(String faultBeginTime) {
        this.faultBeginTime = faultBeginTime;
    }

    public String getFaultEndTime() {
        return faultEndTime;
    }

    public void setFaultEndTime(String faultEndTime) {
        this.faultEndTime = faultEndTime;
    }

    public String getFaultHandleTime() {
        return faultHandleTime;
    }

    public void setFaultHandleTime(String faultHandleTime) {
        this.faultHandleTime = faultHandleTime;
    }

    public String getPositionX() {
        return positionX;
    }

    public void setPositionX(String positionX) {
        this.positionX = positionX;
    }

    public String getPositionY() {
        return positionY;
    }

    public void setPositionY(String positionY) {
        this.positionY = positionY;
    }

    public String getPositionDeviate() {
        return positionDeviate;
    }

    public void setPositionDeviate(String positionDeviate) {
        this.positionDeviate = positionDeviate;
    }

    public String getIsSolve() {
        return isSolve;
    }

    public void setIsSolve(String isSolve) {
        this.isSolve = isSolve;
    }

    public String getIsSolveName() {
        return isSolveName;
    }

    public void setIsSolveName(String isSolveName) {
        this.isSolveName = isSolveName;
    }

    public String getManageCode() {
        return manageCode;
    }

    public void setManageCode(String manageCode) {
        this.manageCode = manageCode;
    }

    public String getManageName() {
        return manageName;
    }

    public void setManageName(String manageName) {
        this.manageName = manageName;
    }

    public String getWorkCode() {
        return workCode;
    }

    public void setWorkCode(String workCode) {
        this.workCode = workCode;
    }

    public String getWorkName() {
        return workName;
    }

    public void setWorkName(String workName) {
        this.workName = workName;
    }

    public String getFaultRemark() {
        return faultRemark;
    }

    public void setFaultRemark(String faultRemark) {
        this.faultRemark = faultRemark;
    }

    public String getRecordFlag() {
        return recordFlag;
    }

    public void setRecordFlag(String recordFlag) {
        this.recordFlag = recordFlag;
    }

    public String getRecordFlagName() {
        return recordFlagName;
    }

    public void setRecordFlagName(String recordFlagName) {
        this.recordFlagName = recordFlagName;
    }

    public String getRecordTime() {
        return recordTime;
    }

    public void setRecordTime(String recordTime) {
        this.recordTime = recordTime;
    }

    public String getOrgId() {
        return orgId;
    }

    public void setOrgId(String orgId) {
        this.orgId = orgId;
    }

    public String getNotifyTime() {
        return notifyTime;
    }

    public void setNotifyTime(String notifyTime) {
        this.notifyTime = notifyTime;
    }

    public String getNotifyFlag() {
        return notifyFlag;
    }

    public void setNotifyFlag(String notifyFlag) {
        this.notifyFlag = notifyFlag;
    }

    public String getNotifyFlagName() {
        return notifyFlagName;
    }

    public void setNotifyFlagName(String notifyFlagName) {
        this.notifyFlagName = notifyFlagName;
    }

    public String getPhoneUserCode() {
        return phoneUserCode;
    }

    public void setPhoneUserCode(String phoneUserCode) {
        this.phoneUserCode = phoneUserCode;
    }
}
