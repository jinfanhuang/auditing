package com.hyperats.logging.h4;

import java.util.Date;
import java.util.HashMap;

import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.InitializingBean;

import com.hyperats.data.IdGenerator;
import com.hyperats.data.h4.DynamicMappingSessionFactory;
import com.hyperats.data.h4.SessionFactoryHelper;
import com.hyperats.logging.AbstractOperationLogger;
import com.hyperats.logging.OperationLog;
import com.hyperats.paging.Pagination;

public class DynamicMappingOperationLogger
extends AbstractOperationLogger implements InitializingBean {
	private DynamicMappingSessionFactory sessionFactory;
	private String entityName;
	private String mappingXml;
	private SessionFactoryHelper helper;
	private IdGenerator idGenerator;

	@Override
	protected void doLog(OperationLog l) {
		l.setId(idGenerator.generate());
		helper.getCurrentSession().save(entityName, l);
	}

	public void setSessionFactory(DynamicMappingSessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}

	public void setEntityName(String entityName) {
		this.entityName = entityName;
	}

	public void setMappingXml(String mappingXml) {
		this.mappingXml = mappingXml;
	}

	public void setIdGenerator(IdGenerator idGenerator) {
		this.idGenerator = idGenerator;
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		sessionFactory.addMapping(OperationLog.class, entityName, mappingXml, new HashMap());
		helper = new SessionFactoryHelper();
		helper.setSessionFactory(sessionFactory);
	}
}
