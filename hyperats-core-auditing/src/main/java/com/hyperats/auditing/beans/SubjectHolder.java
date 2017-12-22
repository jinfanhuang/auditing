package com.hyperats.auditing.beans;

public class SubjectHolder {

	private static ThreadLocal<AuditHost> host = new ThreadLocal<AuditHost>();
	private static ThreadLocal<String> userId = new ThreadLocal<String>();
	
	public static void setHost(AuditHost auditHost) {
		host.set(auditHost);
	}
	
	public static AuditHost getHost() {
		return host.get();
	}
	
	public static void removeHost() {
		host.remove();
	}
	
	public static void setUserId(String uId) {
		userId.set(uId);
	}
	
	public static String getUserId() {
		return userId.get();
	}
	
	public static void removeUsrId() {
		userId.remove();
	}
}
