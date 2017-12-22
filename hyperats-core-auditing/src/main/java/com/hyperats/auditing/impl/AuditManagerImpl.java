package com.hyperats.auditing.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import com.hyperats.auditing.AuditManager;
import com.hyperats.auditing.config.AuditedService;

public class AuditManagerImpl implements AuditManager,ApplicationContextAware {

	private ApplicationContext applicationContext;
	
	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		this.applicationContext = applicationContext;
	}
	
	@Override
	public Map<String,List<String>> getCategory(String securityTag) {
		Map<String, List<String>> map = new HashMap<String,List<String>>();
		String[] beanNames = applicationContext.getBeanDefinitionNames();
		if(beanNames.length > 0) {
			for(int i=0;i<beanNames.length;i++) {
				Class<?> c = null;
				try {
					c = applicationContext.getType(beanNames[i]);
				} catch (Exception e) {
					e.printStackTrace();
				}
				if(c != null) {
					AuditedService as = c.getAnnotation(AuditedService.class);
					if(as != null) {
						String st = as.securityTag();
						if(securityTag == st) {
							String category = as.category();
							if(!map.containsKey(category)) {
								List<String> beanlst = new ArrayList<String>();
								beanlst.add(beanNames[i]);
								map.put(category, beanlst);						
							}else {
								List<String> lst = map.get(category);
								lst.add(beanNames[i]);
								map.put(category, lst);
							}													
						}
					}
				}
			}			
		}
		return map;
	}

}
