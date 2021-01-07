package com.a712.emos_androidapp.receiveModel;

/**
 * 
 * <p>[功能描述]：页面传递Task</p>
 * <p>Copyright (c) 1997-2017 TCB Corporation</p>
 * 
 * @author	王垒
 * @version	1.0, 2017年1月25日上午10:45:13
 * @since	eoms 1.0.0
 *
 */
public class TaskModel extends BaseModel {

	private String taskId;// 递增字段
	private String companyId;//排污企业ID
	private String companyCode;//排污企业编码
	private String companyName;//排污企业名称
	private String outletId;//排口ID
	private String outletCode;//排口编码
	private String outletName;//排口名称
	private int outletDeviceId;//排口设备ID
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
	
	/**
	 * @return the taskId
	 */
	public String getTaskId() {
		return taskId;
	}
	/**
	 * @param taskId the taskId to set
	 */
	public void setTaskId(String taskId) {
		this.taskId = taskId;
	}
	/**
	 * @return the companyId
	 */
	public String getCompanyId() {
		return companyId;
	}
	/**
	 * @param companyId the companyId to set
	 */
	public void setCompanyId(String companyId) {
		this.companyId = companyId;
	}
	/**
	 * @return the companyCode
	 */
	public String getCompanyCode() {
		return companyCode;
	}
	/**
	 * @param companyCode the companyCode to set
	 */
	public void setCompanyCode(String companyCode) {
		this.companyCode = companyCode;
	}
	/**
	 * @return the companyName
	 */
	public String getCompanyName() {
		return companyName;
	}
	/**
	 * @param companyName the companyName to set
	 */
	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}
	/**
	 * @return the outletId
	 */
	public String getOutletId() {
		return outletId;
	}
	/**
	 * @param outletId the outletId to set
	 */
	public void setOutletId(String outletId) {
		this.outletId = outletId;
	}
	/**
	 * @return the outletCode
	 */
	public String getOutletCode() {
		return outletCode;
	}
	/**
	 * @param outletCode the outletCode to set
	 */
	public void setOutletCode(String outletCode) {
		this.outletCode = outletCode;
	}
	/**
	 * @return the outletName
	 */
	public String getOutletName() {
		return outletName;
	}
	/**
	 * @param outletName the outletName to set
	 */
	public void setOutletName(String outletName) {
		this.outletName = outletName;
	}
	/**
	 * @return the outletDeviceId
	 */
	public int getOutletDeviceId() {
		return outletDeviceId;
	}

	public void setOutletDeviceId(int outletDeviceId) {
		this.outletDeviceId = outletDeviceId;
	}

	/**
	 * @return the deviceId
	 */
	public String getDeviceId() {
		return deviceId;
	}
	/**
	 * @param deviceId the deviceId to set
	 */
	public void setDeviceId(String deviceId) {
		this.deviceId = deviceId;
	}
	/**
	 * @return the deviceName
	 */
	public String getDeviceName() {
		return deviceName;
	}
	/**
	 * @param deviceName the deviceName to set
	 */
	public void setDeviceName(String deviceName) {
		this.deviceName = deviceName;
	}
	
	/**
	 * @return the qrCode
	 */
	public String getQrCode() {
		return qrCode;
	}
	/**
	 * @param qrCode the qrCode to set
	 */
	public void setQrCode(String qrCode) {
		this.qrCode = qrCode;
	}
	/**
	 * @return the taskType
	 */
	public String getTaskType() {
		return taskType;
	}
	/**
	 * @param taskType the taskType to set
	 */
	public void setTaskType(String taskType) {
		this.taskType = taskType;
	}
	/**
	 * @return the taskTypeName
	 */
	public String getTaskTypeName() {
		return taskTypeName;
	}
	/**
	 * @param taskTypeName the taskTypeName to set
	 */
	public void setTaskTypeName(String taskTypeName) {
		this.taskTypeName = taskTypeName;
	}
	/**
	 * @return the taskDescription
	 */
	public String getTaskDescription() {
		return taskDescription;
	}
	/**
	 * @param taskDescription the taskDescription to set
	 */
	public void setTaskDescription(String taskDescription) {
		this.taskDescription = taskDescription;
	}
	/**
	 * @return the taskBeginTime
	 */
	public String getTaskBeginTime() {
		return taskBeginTime;
	}
	/**
	 * @param taskBeginTime the taskBeginTime to set
	 */
	public void setTaskBeginTime(String taskBeginTime) {
		this.taskBeginTime = taskBeginTime;
	}
	/**
	 * @return the taskEndTime
	 */
	public String getTaskEndTime() {
		return taskEndTime;
	}
	/**
	 * @param taskEndTime the taskEndTime to set
	 */
	public void setTaskEndTime(String taskEndTime) {
		this.taskEndTime = taskEndTime;
	}
	/**
	 * @return the taskHandleTime
	 */
	public String getTaskHandleTime() {
		return taskHandleTime;
	}
	/**
	 * @param taskHandleTime the taskHandleTime to set
	 */
	public void setTaskHandleTime(String taskHandleTime) {
		this.taskHandleTime = taskHandleTime;
	}
	/**
	 * @return the positionX
	 */
	public String getPositionX() {
		return positionX;
	}
	/**
	 * @param positionX the positionX to set
	 */
	public void setPositionX(String positionX) {
		this.positionX = positionX;
	}
	/**
	 * @return the positionY
	 */
	public String getPositionY() {
		return positionY;
	}
	/**
	 * @param positionY the positionY to set
	 */
	public void setPositionY(String positionY) {
		this.positionY = positionY;
	}
	/**
	 * @return the positionDeviate
	 */
	public String getPositionDeviate() {
		return positionDeviate;
	}
	/**
	 * @param positionDeviate the positionDeviate to set
	 */
	public void setPositionDeviate(String positionDeviate) {
		this.positionDeviate = positionDeviate;
	}
	/**
	 * @return the isSolve
	 */
	public String getIsSolve() {
		return isSolve;
	}
	/**
	 * @param isSolve the isSolve to set
	 */
	public void setIsSolve(String isSolve) {
		this.isSolve = isSolve;
	}
	/**
	 * @return the isSolveName
	 */
	public String getIsSolveName() {
		return isSolveName;
	}
	/**
	 * @param isSolveName the isSolveName to set
	 */
	public void setIsSolveName(String isSolveName) {
		this.isSolveName = isSolveName;
	}
	/**
	 * @return the manageCode
	 */
	public String getManageCode() {
		return manageCode;
	}
	/**
	 * @param manageCode the manageCode to set
	 */
	public void setManageCode(String manageCode) {
		this.manageCode = manageCode;
	}
	/**
	 * @return the manageName
	 */
	public String getManageName() {
		return manageName;
	}
	/**
	 * @param manageName the manageName to set
	 */
	public void setManageName(String manageName) {
		this.manageName = manageName;
	}
	/**
	 * @return the workCode
	 */
	public String getWorkCode() {
		return workCode;
	}
	/**
	 * @param workCode the workCode to set
	 */
	public void setWorkCode(String workCode) {
		this.workCode = workCode;
	}
	/**
	 * @return the workName
	 */
	public String getWorkName() {
		return workName;
	}
	/**
	 * @param workName the workName to set
	 */
	public void setWorkName(String workName) {
		this.workName = workName;
	}
	/**
	 * @return the taskRemark
	 */
	public String getTaskRemark() {
		return taskRemark;
	}
	/**
	 * @param taskRemark the taskRemark to set
	 */
	public void setTaskRemark(String taskRemark) {
		this.taskRemark = taskRemark;
	}
	/**
	 * @return the orgId
	 */
	public String getOrgId() {
		return orgId;
	}
	/**
	 * @param orgId the orgId to set
	 */
	public void setOrgId(String orgId) {
		this.orgId = orgId;
	}
	
	/**
	 * @return the notifyTime
	 */
	public String getNotifyTime() {
		return notifyTime;
	}
	/**
	 * @param notifyTime the notifyTime to set
	 */
	public void setNotifyTime(String notifyTime) {
		this.notifyTime = notifyTime;
	}
	/**
	 * @return the notifyFlag
	 */
	public String getNotifyFlag() {
		return notifyFlag;
	}
	/**
	 * @param notifyFlag the notifyFlag to set
	 */
	public void setNotifyFlag(String notifyFlag) {
		this.notifyFlag = notifyFlag;
	}
	/**
	 * @return the notifyFlagName
	 */
	public String getNotifyFlagName() {
		return notifyFlagName;
	}
	/**
	 * @param notifyFlagName the notifyFlagName to set
	 */
	public void setNotifyFlagName(String notifyFlagName) {
		this.notifyFlagName = notifyFlagName;
	}
	
}
