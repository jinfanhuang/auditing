package com.hyperats.logging;

import java.util.Date;

import com.hyperats.paging.Pagination;

public interface OperationLogger {
	public abstract void log(String code, String description,
			String uid, String uname, int status,
			Date beginTime, Date endTime,
			Object detailObject);
	public abstract void logTask(Runnable task,
			String code, String description,
			String uid, String uname,
			Object detailObject) throws Exception;
}
