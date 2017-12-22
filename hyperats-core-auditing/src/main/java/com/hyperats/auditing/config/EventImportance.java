package com.hyperats.auditing.config;

public enum EventImportance {
	CRITICAL(100), MAJOR(80), MINOR(60), LESSER(40);

	private int importance;

	EventImportance(int imp) {
		this.importance = imp;
	}

	public int getImportance() {
		return importance;
	}
}
