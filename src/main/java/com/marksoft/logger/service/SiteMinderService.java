package com.marksoft.logger.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

import javax.annotation.Resource;
import javax.inject.Inject;

import org.joda.time.LocalDate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.marksoft.logger.domain.Record;
import com.marksoft.logger.domain.SiteRecord;
import com.marksoft.logger.repository.SiteRecordRepository;
	
@Service	
public class SiteMinderService {
	private final Logger log = LoggerFactory.getLogger(SiteMinderService.class);

	@Inject
	@Resource(name = "DDWrtLogFile")	
	RouterLog routerLog;
	
	@Inject
	SiteRecordRepository siteRecordRepository;
	
	@Inject	
	InetAddressService iNetAddressService;
	
	public void loadLogs() {
		log.info("Starting the process to load the logs at: " + new Date());
		
		List<SiteRecord> siteRecords = determineHostnames(routerLog.readFiles());
		
		siteRecordRepository.save(siteRecords);
	}
	
	private List<SiteRecord> determineHostnames(Set<Record> records) {
		List<SiteRecord> siteRecords = new ArrayList<>();
		InetAddressService internetAddressService = new InetAddressService();

		int i = 0;//TODO rm
		
		for (Record record : records) {
		
			i++;
			if (i > 10) break; //TODO rm
			
			SiteRecord siteRecord = new SiteRecord();
			
			siteRecord.setDate(new LocalDate(record.getDate()));
			siteRecord.setDestinationSite(internetAddressService.determineHostname(record.getDestination()).getHostName());
			siteRecord.setDevice(internetAddressService.determineHostname(record.getSource()).getHostName());
			
			log.info("siteRecord++ " + siteRecord);
			
			siteRecords.add(siteRecord);
		}
		return siteRecords;
	}
}


	