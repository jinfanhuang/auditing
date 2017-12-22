package com.hyperats.logging.h4;

import static org.junit.Assert.*;

import java.util.Date;

import javax.annotation.Resource;

import com.hyperats.logging.OperationLogManager;
import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import com.hyperats.data.h4.DynamicMappingSessionFactory;
import com.hyperats.logging.OperationLogger;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath*:/META-INF/*-beans.xml")
@Ignore
public class DynamicMappingOperationLoggerTest {
	@Resource(name="someOperationLogger")
	OperationLogger someLogger;
	@Resource(name="someOperationLogManager")
	OperationLogManager someManager;
	@Resource(name="dynamicMappingSessionFactory")
	DynamicMappingSessionFactory sessionFactory;
	
	@Before
	public void setUp() {
	}
	
	@After
	public void tearDown() {
	}
	
	@Test
	@Transactional
	public void test() {
		assertEquals(0, someManager.countAll());
		someLogger.log("abc", "abcd",
				"admin", "nimda", DynamicMappingOperationLogger.STATUS_SUCCESS,
				new Date(), new Date(),
				null);
		assertEquals(1, someManager.countAll());
		someLogger.log("abc", "abcd",
				"admin", "nimda", DynamicMappingOperationLogger.STATUS_SUCCESS,
				new Date(), new Date(),
				null);
		assertEquals(2, someManager.countOperation("abc"));
	}
}
