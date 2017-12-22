package com.hyperats.auditing;

import java.lang.reflect.Method;
import java.util.Date;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.aop.aspectj.MethodInvocationProceedingJoinPoint;
import com.hyperats.auditing.beans.LogEntry;

public class AuditAspect {
	private static final Logger logger = LogManager.getLogger(AuditAspect.class);
	
	private AuditService auditService;
	
	public void setAuditService(AuditService auditService) {
		this.auditService = auditService;
	}
	public Object around(final ProceedingJoinPoint proceedingJoinPoint)
			throws Throwable {
		logger.debug("Audit started...");
		final Object[] arguments = proceedingJoinPoint.getArgs();
		MethodInvocationProceedingJoinPoint joinPoint = (MethodInvocationProceedingJoinPoint) proceedingJoinPoint;
		Method m = ((MethodSignature) joinPoint.getSignature()).getMethod();
		LogEntry le = auditService.parseLogEntry(m);
		
		Object result;
		Throwable raised;
		try {
			result = proceedingJoinPoint.proceed();
			raised = null;
		} catch (Throwable t) {
			raised = t;
			result = null;
		}
		le.setEndTime((new Date()).getTime());
		auditService.log(le,m, arguments, result, raised);		
		logger.debug("Audit finished.");
		return result;
	}
}
