package com.marksoft.logger.service;

import java.util.Set;

import com.marksoft.logger.domain.Record;

public interface RouterLog {

	public Set<Record> readFiles();
	
}