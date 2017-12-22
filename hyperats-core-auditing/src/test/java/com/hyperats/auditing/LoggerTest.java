package com.hyperats.auditing;

import java.util.List;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import com.hyperats.auditing.beans.LogEntry;
import com.hyperats.data.h4.SessionFactoryHelper;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations="classpath*:META-INF/*-beans.xml")
public class LoggerTest {
	private static final Logger logger = LogManager.getLogger(LoggerTest.class);
	
	@Autowired
	private SessionFactoryHelper helper;
	@Autowired
	private TestAspectService testAspectService;


	public void setTestAspectService(TestAspectService testAspectService) {
		this.testAspectService = testAspectService;
	}

	public void setHelper(SessionFactoryHelper helper) {
		this.helper = helper;
	}
	@Transactional
	@Test
	public void test() {
		testAspectService.log();
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		Criteria c = helper.getCurrentSession().createCriteria(LogEntry.class);
		@SuppressWarnings("unchecked")
		List<LogEntry> lst = c.list();
		assert(lst.size() > 0);
	}
}
