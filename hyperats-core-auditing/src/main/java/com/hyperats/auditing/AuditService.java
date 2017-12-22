package com.hyperats.auditing;

import java.lang.reflect.Method;
import java.util.Map;

import com.hyperats.auditing.beans.LogEntry;
import com.hyperats.paging.Pagination;

public interface AuditService {

	public LogEntry parseLogEntry(Method method);
	
	public abstract Pagination<LogEntry> pageQueryLogEntry(Pagination<LogEntry> page, Map<String,Object> condition);

	public void log(LogEntry logEntry,Method m, Object args, Object result, Throwable raised);

}
