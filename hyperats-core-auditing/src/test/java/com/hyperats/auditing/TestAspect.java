package com.hyperats.auditing;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import com.hyperats.auditing.config.AuditedEvent;
import com.hyperats.auditing.config.AuditedService;
import com.hyperats.auditing.config.EventImportance;

@AuditedService(category="com.hyperats.auditing",name="TestAspect",securityTag="secureManagement")
public class TestAspect implements TestAspectService {

	private static final Logger logger = LogManager.getLogger(TestAspect.class);
	@AuditedEvent(contentTemplate="",importance=EventImportance.MAJOR,name="test")
	public void log() {
		logger.info("Audited by Aspect");
	}
}
