package com.a712.emos_androidapp.receiveModel;

/**
 *
 * <p>[功能描述]：页面传递Fault</p>
 * <p>Copyright (c) 1997-2017 TCB Corporation</p>
 *
 * @author	王垒
 * @version	1.0, 2017年1月25日上午10:45:13
 * @since	eoms 1.0.0
 *
 */
public class FaultModel extends BaseModel {

	private String faultId;// 递增字段
	private String companyCode;//排污企业编码
	private String companyName;//排污企业名称
	private String outletId;//排口ID
	private String outletCode;//排口编码
	private String outletName;//排口名称
	private int    outletDeviceId;//排口设备ID
	private String qrCode;//排口设备序列号
	private String deviceId;//设备id
	private String deviceName;//设备名称
	private String deviceModel;//规格型号
	private String devtypeCode;// 设备类型编码
	private String devtypeName;// 设备类型名称
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

	/**
	 * @return the faultId
	 */
	public String getFaultId() {
		return faultId;
	}
	/**
	 * @param faultId the faultId to set
	 */
	public void setFaultId(String faultId) {
		this.faultId = faultId;
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
	 * @return the deviceModel
	 */
	public String getDeviceModel() {
		return deviceModel;
	}
	/**
	 * @param deviceModel the deviceModel to set
	 */
	public void setDeviceModel(String deviceModel) {
		this.deviceModel = deviceModel;
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
	 * @return the devtypeCode
	 */
	public String getDevtypeCode() {
		return devtypeCode;
	}
	/**
	 * @param devtypeCode the devtypeCode to set
	 */
	public void setDevtypeCode(String devtypeCode) {
		this.devtypeCode = devtypeCode;
	}
	/**
	 * @return the devtypeName
	 */
	public String getDevtypeName() {
		return devtypeName;
	}
	/**
	 * @param devtypeName the devtypeName to set
	 */
	public void setDevtypeName(String devtypeName) {
		this.devtypeName = devtypeName;
	}
	/**
	 * @return the faultType
	 */
	public String getFaultType() {
		return faultType;
	}
	/**
	 * @param faultType the faultType to set
	 */
	public void setFaultType(String faultType) {
		this.faultType = faultType;
	}
	/**
	 * @return the faultTypeName
	 */
	public String getFaultTypeName() {
		return faultTypeName;
	}
	/**
	 * @param faultTypeName the faultTypeName to set
	 */
	public void setFaultTypeName(String faultTypeName) {
		this.faultTypeName = faultTypeName;
	}
	/**
	 * @return the faultDescription
	 */
	public String getFaultDescription() {
		return faultDescription;
	}
	/**
	 * @param faultDescription the faultDescription to set
	 */
	public void setFaultDescription(String faultDescription) {
		this.faultDescription = faultDescription;
	}
	/**
	 * @return the faultReason
	 */
	public String getFaultReason() {
		return faultReason;
	}
	/**
	 * @param faultReason the faultReason to set
	 */
	public void setFaultReason(String faultReason) {
		this.faultReason = faultReason;
	}
	/**
	 * @return the faultDeal
	 */
	public String getFaultDeal() {
		return faultDeal;
	}
	/**
	 * @param faultDeal the faultDeal to set
	 */
	public void setFaultDeal(String faultDeal) {
		this.faultDeal = faultDeal;
	}
	/**
	 * @return the faultBeginTime
	 */
	public String getFaultBeginTime() {
		return faultBeginTime;
	}
	/**
	 * @param faultBeginTime the faultBeginTime to set
	 */
	public void setFaultBeginTime(String faultBeginTime) {
		this.faultBeginTime = faultBeginTime;
	}
	/**
	 * @return the faultEndTime
	 */
	public String getFaultEndTime() {
		return faultEndTime;
	}
	/**
	 * @param faultEndTime the faultEndTime to set
	 */
	public void setFaultEndTime(String faultEndTime) {
		this.faultEndTime = faultEndTime;
	}
	/**
	 * @return the faultHandleTime
	 */
	public String getFaultHandleTime() {
		return faultHandleTime;
	}
	/**
	 * @param faultHandleTime the faultHandleTime to set
	 */
	public void setFaultHandleTime(String faultHandleTime) {
		this.faultHandleTime = faultHandleTime;
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
	 * @return the faultRemark
	 */
	public String getFaultRemark() {
		return faultRemark;
	}
	/**
	 * @param faultRemark the faultRemark to set
	 */
	public void setFaultRemark(String faultRemark) {
		this.faultRemark = faultRemark;
	}
	/**
	 * @return the recordFlag
	 */
	public String getRecordFlag() {
		return recordFlag;
	}
	/**
	 * @param recordFlag the recordFlag to set
	 */
	public void setRecordFlag(String recordFlag) {
		this.recordFlag = recordFlag;
	}
	/**
	 * @return the recordFlagName
	 */
	public String getRecordFlagName() {
		return recordFlagName;
	}
	/**
	 * @param recordFlagName the recordFlagName to set
	 */
	public void setRecordFlagName(String recordFlagName) {
		this.recordFlagName = recordFlagName;
	}
	/**
	 * @return the recordTime
	 */
	public String getRecordTime() {
		return recordTime;
	}
	/**
	 * @param recordTime the recordTime to set
	 */
	public void setRecordTime(String recordTime) {
		this.recordTime = recordTime;
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

	public String getPhoneUserCode() {
		return phoneUserCode;
	}

	public void setPhoneUserCode(String phoneUserCode) {
		this.phoneUserCode = phoneUserCode;
	}

}
