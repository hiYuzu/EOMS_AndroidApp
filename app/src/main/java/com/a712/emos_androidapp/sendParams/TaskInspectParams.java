package com.a712.emos_androidapp.sendParams;

import com.a712.emos_androidapp.receiveModel.BaseModel;

/**
 * 
 * <p>[功能描述]：任务报表传递项</p>
 * <p>Copyright (c) 1997-2017 TCB Corporation</p>
 * 
 * @author	王垒
 * @version	1.0, 2017年3月16日上午10:52:49
 * @since	eoms 1.0.0
 *
 */
public class TaskInspectParams extends BaseModel {

	private String taskInspectId;
	private String taskId;
	private String inspectId;
	private String itemType;
	private String itemTypeName;
	private String itemName;
	private String itemContent;
	private String itemResult;
	private String itemResultName;
	private String itemRemark;
	private String userCode;
	
	/**
	 * @return the taskInspectId
	 */
	public String getTaskInspectId() {
		return taskInspectId;
	}
	/**
	 * @param taskInspectId the taskInspectId to set
	 */
	public void setTaskInspectId(String taskInspectId) {
		this.taskInspectId = taskInspectId;
	}
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
	 * @return the inspectId
	 */
	public String getInspectId() {
		return inspectId;
	}
	/**
	 * @param inspectId the inspectId to set
	 */
	public void setInspectId(String inspectId) {
		this.inspectId = inspectId;
	}
	
	/**
	 * @return the itemType
	 */
	public String getItemType() {
		return itemType;
	}
	/**
	 * @param itemType the itemType to set
	 */
	public void setItemType(String itemType) {
		this.itemType = itemType;
	}
	/**
	 * @return the itemTypeName
	 */
	public String getItemTypeName() {
		return itemTypeName;
	}
	/**
	 * @param itemTypeName the itemTypeName to set
	 */
	public void setItemTypeName(String itemTypeName) {
		this.itemTypeName = itemTypeName;
	}
	/**
	 * @return the itemName
	 */
	public String getItemName() {
		return itemName;
	}
	/**
	 * @param itemName the itemName to set
	 */
	public void setItemName(String itemName) {
		this.itemName = itemName;
	}
	/**
	 * @return the itemContent
	 */
	public String getItemContent() {
		return itemContent;
	}
	/**
	 * @param itemContent the itemContent to set
	 */
	public void setItemContent(String itemContent) {
		this.itemContent = itemContent;
	}
	/**
	 * @return the itemResult
	 */
	public String getItemResult() {
		return itemResult;
	}
	/**
	 * @param itemResult the itemResult to set
	 */
	public void setItemResult(String itemResult) {
		this.itemResult = itemResult;
	}
	/**
	 * @return the itemResultName
	 */
	public String getItemResultName() {
		return itemResultName;
	}
	/**
	 * @param itemResultName the itemResultName to set
	 */
	public void setItemResultName(String itemResultName) {
		this.itemResultName = itemResultName;
	}
	/**
	 * @return the itemRemark
	 */
	public String getItemRemark() {
		return itemRemark;
	}
	/**
	 * @param itemRemark the itemRemark to set
	 */
	public void setItemRemark(String itemRemark) {
		this.itemRemark = itemRemark;
	}

	public String getUserCode() {
		return userCode;
	}

	public void setUserCode(String userCode) {
		this.userCode = userCode;
	}

}
