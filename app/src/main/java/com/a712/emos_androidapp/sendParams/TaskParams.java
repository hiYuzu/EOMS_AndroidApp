package com.a712.emos_androidapp.sendParams;

/**
 * Created by Administrator on 2017/4/17.
 */

public class TaskParams {
    private String taskId;// 递增字段
    private String companyId;//排污企业ID
    private String companyCode;//排污企业编码
    private String companyName;//排污企业名称
    private String outletId;//排口ID
    private String outletCode;//排口编码
    private String outletName;//排口名称
    private String outletDeviceId;//排口设备ID
    private String qrCode;//排口设备序列号
    private String deviceId;//设备id
    private String deviceName;//设备名称
    private String taskType;//任务类型编码
    private String taskTypeName;//任务类型名称
    private String taskDescription;//任务描述
    private String taskBeginTime;//开始时间
    private String taskEndTime;//结束时间
    private String taskHandleTime;//处理时间
    private String positionX;//维修位置X
    private String positionY;//维修位置Y
    private String positionDeviate;//位置判断信息
    private String isSolve;//是否解决编码
    private String isSolveName;//是否解决名称
    private String manageCode;//负责人编码
    private String manageName;//负责人名称
    private String workCode;//负责人编码
    private String workName;//负责人名称
    private String taskRemark;//备注
    private String orgId;//组织id（做为查询条件）
    private String notifyTime;//发送给网关时间戳
    private String notifyFlag;//发送标志（0：发送失败；1：发送成功；2:发送中）
    private String notifyFlagName;//发送标志名称（0：发送失败；1：发送成功；2:发送中）
    private String phoneUserCode;//移动端登录用户

    public String getTaskId() {
        return taskId;
    }

    public void setTaskId(String taskId) {
        this.taskId = taskId;
    }

    public String getCompanyId() {
        return companyId;
    }

    public void setCompanyId(String companyId) {
        this.companyId = companyId;
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

    public String getTaskType() {
        return taskType;
    }

    public void setTaskType(String taskType) {
        this.taskType = taskType;
    }

    public String getTaskTypeName() {
        return taskTypeName;
    }

    public void setTaskTypeName(String taskTypeName) {
        this.taskTypeName = taskTypeName;
    }

    public String getTaskDescription() {
        return taskDescription;
    }

    public void setTaskDescription(String taskDescription) {
        this.taskDescription = taskDescription;
    }

    public String getTaskBeginTime() {
        return taskBeginTime;
    }

    public void setTaskBeginTime(String taskBeginTime) {
        this.taskBeginTime = taskBeginTime;
    }

    public String getTaskEndTime() {
        return taskEndTime;
    }

    public void setTaskEndTime(String taskEndTime) {
        this.taskEndTime = taskEndTime;
    }

    public String getTaskHandleTime() {
        return taskHandleTime;
    }

    public void setTaskHandleTime(String taskHandleTime) {
        this.taskHandleTime = taskHandleTime;
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

    public String getTaskRemark() {
        return taskRemark;
    }

    public void setTaskRemark(String taskRemark) {
        this.taskRemark = taskRemark;
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
