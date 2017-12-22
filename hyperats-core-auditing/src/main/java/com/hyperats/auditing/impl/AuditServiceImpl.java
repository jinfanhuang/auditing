package com.hyperats.auditing.impl;

import java.lang.reflect.Method;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.apache.commons.lang.StringUtils;
import org.codehaus.jackson.map.ObjectMapper;
import org.hibernate.Criteria;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

import com.hyperats.auditing.AuditService;
import com.hyperats.auditing.beans.AuditHost;
import com.hyperats.auditing.beans.LogEntry;
import com.hyperats.auditing.beans.SubjectHolder;
import com.hyperats.auditing.config.AuditedEvent;
import com.hyperats.auditing.config.AuditedService;
import com.hyperats.data.h4.HibernateUtil;
import com.hyperats.data.h4.SessionFactoryHelper;
import com.hyperats.paging.Pagination;

public class AuditServiceImpl implements AuditService{

	private String applicationId;
	
	private SessionFactoryHelper helper;
	
	public void setHelper(SessionFactoryHelper helper) {
		this.helper = helper;
	}

	public void setApplicationId(String applicationId) {
		this.applicationId = applicationId;
	}

	@Override
	public LogEntry parseLogEntry(Method method) {
		AuditedEvent ae = method.getAnnotation(AuditedEvent.class);
		AuditedService as = method.getDeclaringClass().getAnnotation(AuditedService.class);		
		LogEntry e = new LogEntry();
		e.setApplicationId(applicationId);
		e.setBeginTime((new Date()).getTime());
		e.setCategory(as.category());
		e.setClienAddres(getAddress());
		e.setClientHost(getHost());
		e.setEventName(ae.name());
		e.setInvokerId(getInvokerId());
		e.setServiceName(as.name());
		return e;
	}

	private String getHost() {
		AuditHost host = SubjectHolder.getHost();
		if(host != null) {
			return host.clientHost;
		}
		return null;
	}

	private String getAddress() {
		AuditHost host = SubjectHolder.getHost();
		if(host != null) {
			return host.clientAddress;
		}
		return null;
	}

	private String getInvokerId() {
		return SubjectHolder.getUserId();
	}
	@Override
	public void log(final LogEntry logEntry,final Method method, final Object args, final Object result, final Throwable raised) {
		ExecutorService pool = Executors.newCachedThreadPool();
		pool.submit(new Runnable() {			
			@Override
			public void run() {
				try {
					doLog(logEntry, method, args, result, raised);
				} catch (Throwable t) {
					t.printStackTrace();
				}
			}

			private void doLog(final LogEntry logEntry, final Method method, final Object args, final Object result,
					final Throwable raised) {
				AuditedEvent ae = method.getAnnotation(AuditedEvent.class);
				if(StringUtils.isEmpty(ae.contentTemplate())) {				
					Map<String,Object> map = new HashMap<String,Object>();
					map.put("args", args);
					map.put("result", result);
					if(raised != null) {
						map.put("exceptionMessage", raised.getMessage());
					}
					ObjectMapper om = new ObjectMapper();
					try {
						logEntry.setContentTemplate(om.writeValueAsString(map));
					} catch (Exception e) {
						e.printStackTrace();
					} 		
				}else {
					//TODO 处理content是模板或系统参数的情况。
				}
				if(raised != null) {
					logEntry.setExceptionType(raised.getClass().getTypeName());
					logEntry.setExceptionMessage(raised.getMessage());			
				}
				SessionFactory sessionFactory = helper.getSessionFactory();
				boolean participate = HibernateUtil.bindCurrentSession(helper.getSessionFactory());
				try{
					Transaction tx = helper.getCurrentSession().beginTransaction();
					sessionFactory.getCurrentSession().save(logEntry);						
					sessionFactory.getCurrentSession().flush();
					tx.commit();
				}finally{
					HibernateUtil.unbindCurrentSession(participate, sessionFactory);
				}
			}	
		});
	}

	@SuppressWarnings("unchecked")
	@Override
	public Pagination<LogEntry> pageQueryLogEntry(Pagination<LogEntry> page, Map<String, Object> condition) {
		if(page == null) {
			page = new Pagination<LogEntry>();
		}
		page.setRecordCount((int) helper.count(makeCri(condition)));
		return helper.page(makeCri(condition), page);
	}
	
	private Criteria makeCri(Map<String, Object> condition) {
		Criteria cri = helper.createCriteria(LogEntry.class);
		String securityTag = (String) condition.get("securityTag");
		String category = (String) condition.get("category");
		long beginTime = 0, endTime = 0;
		
		if(StringUtils.isNotEmpty((String) condition.get("beginTime"))) {
			beginTime = Long.parseLong((String) condition.get("beginTime"));
		}
		if(StringUtils.isNotEmpty((String) condition.get("endTime"))) {
			endTime = Long.parseLong((String) condition.get("endTime"));
		}
		
		if(StringUtils.isEmpty(securityTag)) {
			cri.add(Restrictions.isNull("securityTag"));
		}else {
			cri.add(Restrictions.eq("securityTag", securityTag));
		}
		
		if(StringUtils.isNotEmpty(category)) {
			cri.add(Restrictions.eq("category", category));
		}
		
		if(beginTime > 0 && endTime > 0 && beginTime <= endTime) {
			cri.add(Restrictions.between("beginTime", beginTime, endTime));
		}
		cri.addOrder(Order.desc("id"));
		return cri;
	}

}