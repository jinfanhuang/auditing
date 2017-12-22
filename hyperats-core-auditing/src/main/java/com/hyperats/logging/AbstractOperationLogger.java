package com.hyperats.logging;

import java.io.StringWriter;
import java.io.Writer;
import java.lang.reflect.InvocationTargetException;
import java.util.Date;
import java.util.Map.Entry;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;

public abstract class AbstractOperationLogger
implements OperationLogger {
	public static final int STATUS_SUCCESS = 0;
	public static final int STATUS_GENERAL_FAILURE = -1;
	
	private String detailsTemplate;
	
	protected abstract void doLog(OperationLog l);
	
	private VelocityEngine engine;
	
	private synchronized VelocityEngine ensureEngine() {
		if (detailsTemplate == null) return null;
		if (engine == null) {
			engine = new VelocityEngine();
		}
		return engine;
	}
	
	private String getId(Object obj) {
		try {
			return BeanUtils.getProperty(obj, "id");
		} catch (IllegalAccessException e) {
			return null;
		} catch (InvocationTargetException e) {
			return null;
		} catch (NoSuchMethodException e) {
			return null;
		}
	}
	
	private String guessId(Object obj) {
		String id = getId(obj);
		if (id != null) return id;
		try {
			int l = 100;
			for (Entry<String, String> e: BeanUtils.describe(obj).entrySet()) {
				String n = e.getKey();
				if ((n.endsWith("Id") || n.endsWith("ID"))
					&& n.length() < l) {
					id = e.getValue();
					l = n.length();
				}
			}
			return id;
		} catch (IllegalAccessException e) {
			return null;
		} catch (InvocationTargetException e) {
			return null;
		} catch (NoSuchMethodException e) {
			return null;
		}
	}

	protected void makeDetails(OperationLog l,
			Object detailObject, Throwable err) {
		if (detailObject != null) {
			if (detailObject instanceof String) {
				l.setEntityClass((String)detailObject);
				l.setEntityId(guessId((String)detailObject));
			} else {
				l.setEntityClass(detailObject.getClass().getName());
				l.setEntityId(guessId(detailObject));
			}
		}
		VelocityEngine ve = ensureEngine();
		if (ve == null) return;
		VelocityContext context = new VelocityContext();
		context.put("log", l);
		context.put("detailObject", detailObject);
		context.put("err", err);
		context.put("thread", Thread.currentThread());
		Writer w = new StringWriter();
		engine.evaluate(context, w, l.getCode(), detailsTemplate);
		l.setDetails(w.toString());
	}

	private OperationLog createLogEntry(String code, String description,
			String uid, String uname, int status,
			Date beginTime, Date endTime) {
		OperationLog l = new OperationLog();
		l.setCode(code);
		l.setDescription(description);
		l.setUserId(uid);
		l.setUserName(uname);
		l.setResultStatus(status);
		l.setBeginTime(beginTime);
		if (endTime == null) endTime = new Date();
		l.setEndTime(endTime);
		return l;
	}

	@Override
	public void log(String code, String description,
			String uid, String uname, int status,
			Date beginTime, Date endTime,
			Object detailObject) {
		OperationLog l = createLogEntry(code, description,
				uid, uname, status,
				beginTime, endTime);
		makeDetails(l, detailObject, null);
		try {
			doLog(l);
		} catch (Exception e) {
			// Does nothing.
		}
	}

	@Override
	public void logTask(Runnable task,
			String code, String description,
			String uid, String uname,
			Object detailObject) throws Exception {
		Date beginTime = new Date();
		try {
			task.run();
			OperationLog l = createLogEntry(code, description,
					uid, uname, STATUS_SUCCESS,
					beginTime, new Date());
			makeDetails(l, detailObject, null);
			try {
				doLog(l);
			} catch (Exception ie) {
				// Does nothing.
			}
		} catch (Exception e) {
			OperationLog l = createLogEntry(code, description,
					uid, uname, STATUS_GENERAL_FAILURE,
					beginTime, new Date());
			makeDetails(l, detailObject, e);
			try {
				doLog(l);
			} catch (Exception ie) {
				// Does nothing.
			}
			throw e;
		}
	}

	public void setDetailsTemplate(String detailsTemplate) {
		this.detailsTemplate = detailsTemplate;
	}
}
