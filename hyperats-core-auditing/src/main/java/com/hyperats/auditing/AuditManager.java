package com.hyperats.auditing;

import java.util.List;
import java.util.Map;

public interface AuditManager {

	public Map<String,List<String>> getCategory(String securityTag);
}
