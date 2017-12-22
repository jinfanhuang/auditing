package com.hyperats.auditing.beans;

public class LogEntry {

	private Long id;
	/**
	 * 系统级配置
	 */
	private String applicationId;
	/**
	 * 从Shiro中获取身份，记录在此
	 */
	private String invokerId;
	/**
	 * 来自AuditedService的category
	 */
	private String category;
	/**
	 * 来自AuditedService的name
	 */
	private String serviceName;
    /**
     * 来自AuditedEvent的name
     */
	private String eventName;
	
	private Long beginTime;
	
	private Long endTime;
	
	private String contentTemplate;
	
	private String clienAddres;
	
	private String clientHost;
	
	/**
	 * 如果抛出了异常，记录异常的class全名
	 */
	private String exceptionType;
	/**
	 * 如果抛出了异常，记录异常消息
	 */
	private String exceptionMessage;
	/**
	 * 日志的类型
	 * 1.业务相关的日志，该字段为空
	 * 2.安全相关的日志，该字段的值为secureManagement
	 */
	private String securityTag;
	/**
	 * 日志的重要性，该字段的值为枚举类的常量
	 */
	private Integer importance;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getApplicationId() {
		return applicationId;
	}

	public void setApplicationId(String applicationId) {
		this.applicationId = applicationId;
	}

	public String getInvokerId() {
		return invokerId;
	}

	public void setInvokerId(String invokerId) {
		this.invokerId = invokerId;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public String getServiceName() {
		return serviceName;
	}

	public void setServiceName(String serviceName) {
		this.serviceName = serviceName;
	}

	public String getEventName() {
		return eventName;
	}

	public void setEventName(String eventName) {
		this.eventName = eventName;
	}

	public Long getBeginTime() {
		return beginTime;
	}

	public void setBeginTime(Long beginTime) {
		this.beginTime = beginTime;
	}

	public Long getEndTime() {
		return endTime;
	}

	public void setEndTime(Long endTime) {
		this.endTime = endTime;
	}

	public String getContentTemplate() {
		return contentTemplate;
	}

	public void setContentTemplate(String contentTemplate) {
		this.contentTemplate = contentTemplate;
	}

	public String getClienAddres() {
		return clienAddres;
	}

	public void setClienAddres(String clienAddres) {
		this.clienAddres = clienAddres;
	}

	public String getClientHost() {
		return clientHost;
	}

	public void setClientHost(String clientHost) {
		this.clientHost = clientHost;
	}

	public String getExceptionType() {
		return exceptionType;
	}

	public void setExceptionType(String exceptionType) {
		this.exceptionType = exceptionType;
	}

	public String getExceptionMessage() {
		return exceptionMessage;
	}

	public void setExceptionMessage(String exceptionMessage) {
		this.exceptionMessage = exceptionMessage;
	}

	public String getSecurityTag() {
		return securityTag;
	}

	public void setSecurityTag(String securityTag) {
		this.securityTag = securityTag;
	}

	public Integer getImportance() {
		return importance;
	}

	public void setImportance(Integer importance) {
		this.importance = importance;
	}
	

}
