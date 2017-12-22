package com.hyperats.logging;

import com.hyperats.paging.Pagination;

import java.util.Date;

/**
 * Created by weiht on 17-1-23.
 */
public interface OperationLogManager {
    public abstract void clearAll();
    public abstract void clearPeriod(Date beginTime, Date endTime);
    public abstract int countAll();
    public abstract Pagination<OperationLog> pageAll(Pagination<OperationLog> page);
    public abstract int countOperation(String code);
    public abstract Pagination<OperationLog> pageOperation(String code,
                                                           Pagination<OperationLog> page);
    public abstract int countPeriod(Date beginTime, Date endTime);
    public abstract Pagination<OperationLog> pagePeriod(Date beginTime, Date endTime,
                                                        Pagination<OperationLog> page);
    public abstract int countPeriodOperation(Date beginTime, Date endTime, String code);
    public abstract Pagination<OperationLog> pagePeriodOperaiton(Date beginTime, Date endTime, String code,
                                                                 Pagination<OperationLog> page);
    public abstract int countExample(OperationLog example);
    public abstract Pagination<OperationLog> pageExample(OperationLog example, Pagination<OperationLog> page);
}
