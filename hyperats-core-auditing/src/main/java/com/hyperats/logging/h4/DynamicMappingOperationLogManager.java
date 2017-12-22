package com.hyperats.logging.h4;

import com.hyperats.data.h4.DynamicMappingSessionFactory;
import com.hyperats.data.h4.SessionFactoryHelper;
import com.hyperats.logging.OperationLog;
import com.hyperats.logging.OperationLogManager;
import com.hyperats.paging.Pagination;

import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.InitializingBean;

import java.util.Date;
import java.util.HashMap;

/**
 * Created by weiht on 17-1-23.
 */
public class DynamicMappingOperationLogManager implements OperationLogManager, InitializingBean {
    private DynamicMappingSessionFactory sessionFactory;
    private String entityName;
    private String mappingXml;
    private SessionFactoryHelper helper;

    private Criteria criteria() {
        return helper.createEntityCriteria(entityName);
    }

    private Criteria codeCriteria(Criteria c, String code) {
        return code == null ? c : c.add(Restrictions.eq("code", code));
    }

    private Criteria periodCriteria(Criteria c, Date beginTime, Date endTime) {
        if (beginTime != null) {
            c.add(Restrictions.ge("endTime", beginTime));
        }
        if (endTime != null) {
            c.add(Restrictions.ge("beginTime", endTime));
        }
        return c;
    }

    private Criteria exampleCriteria(OperationLog example) {
        if (example == null) return criteria();
        Criteria c = codeCriteria(
                periodCriteria(
                        criteria(),
                        example.getBeginTime(),
                        example.getEndTime()),
                example.getCode());
        if (example.getUserId() != null) {
            c.add(Restrictions.eq("userId", example.getUserId()));
        }
        if (example.getEntityId() != null) {
            c.add(Restrictions.eq("entityId", example.getEntityId()));
        }
        if (example.getEntityClass() != null) {
            c.add(Restrictions.eq("entityClass", example.getEntityClass()));
        }
        return c;
    }

    @Override
    public void clearAll() {
        helper.getCurrentSession().createQuery("delete " + entityName).executeUpdate();
    }

    @Override
    public void clearPeriod(Date beginTime, Date endTime) {
        helper.getCurrentSession()
                .createQuery("delete " + entityName + " e where e.endTime >= ? and e.beginTime <= ?")
                .setDate(0, beginTime).setDate(1, endTime)
                .executeUpdate();
    }

    @Override
    public int countAll() {
        return (int)helper.count(criteria());
    }

    @Override
    public Pagination<OperationLog> pageAll(Pagination<OperationLog> page) {
        if (page == null) {
            page = new Pagination<OperationLog>();
        }
        if (page.getRecordCount() == 0) {
            page.setRecordCount(countAll());
            page.calcPages();
        }
        helper.page(criteria(), page);
        return page;
    }

    @Override
    public int countOperation(String code) {
        return (int)helper.count(codeCriteria(criteria(), code));
    }

    @Override
    public Pagination<OperationLog> pageOperation(String code,
                                                  Pagination<OperationLog> page) {
        if (page == null) {
            page = new Pagination<OperationLog>();
        }
        if (page.getRecordCount() == 0) {
            page.setRecordCount(countOperation(code));
            page.calcPages();
        }
        helper.page(codeCriteria(criteria(), code), page);
        return page;
    }

    @Override
    public int countPeriod(Date beginTime, Date endTime) {
        return (int)helper.count(periodCriteria(criteria(), beginTime, endTime));
    }

    @Override
    public Pagination<OperationLog> pagePeriod(
            Date beginTime, Date endTime,
            Pagination<OperationLog> page) {
        if (page == null) {
            page = new Pagination<OperationLog>();
        }
        if (page.getRecordCount() == 0) {
            page.setRecordCount(countPeriod(beginTime, endTime));
            page.calcPages();
        }
        helper.page(periodCriteria(criteria(), beginTime, endTime), page);
        return page;
    }

    @Override
    public int countPeriodOperation(Date beginTime, Date endTime, String code) {
        return (int)helper.count(periodCriteria(codeCriteria(criteria(), code), beginTime, endTime));
    }

    @Override
    public Pagination<OperationLog> pagePeriodOperaiton(
            Date beginTime, Date endTime, String code,
            Pagination<OperationLog> page) {
        if (page == null) {
            page = new Pagination<OperationLog>();
        }
        if (page.getRecordCount() == 0) {
            page.setRecordCount(countPeriodOperation(beginTime, endTime, code));
            page.calcPages();
        }
        helper.page(periodCriteria(codeCriteria(criteria(), code), beginTime, endTime), page);
        return page;
    }

    @Override
    public int countExample(OperationLog example) {
        return (int)helper.count(exampleCriteria(example));
    }

    @Override
    public Pagination<OperationLog> pageExample(OperationLog example, Pagination<OperationLog> page) {
        if (page == null) {
            page = new Pagination<OperationLog>();
        }
        if (page.getRecordCount() == 0) {
            page.setRecordCount(countExample(example));
            page.calcPages();
        }
        helper.page(exampleCriteria(example), page);
        return page;
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

    @Override
    public void afterPropertiesSet() throws Exception {
        sessionFactory.addMapping(OperationLog.class, entityName, mappingXml, new HashMap());
        helper = new SessionFactoryHelper();
        helper.setSessionFactory(sessionFactory);
    }
}
