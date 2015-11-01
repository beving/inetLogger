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

import com.marksoft.logger.domain.InternetAddress;
import com.marksoft.logger.domain.Record;
import com.marksoft.logger.domain.SiteRecord;
import com.marksoft.logger.repository.InternetAddressRepository;
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
	InetAddressService internetAddressService;
	
	@Inject
	InternetAddressRepository internetAddressRepository;
	
	public void loadLogs() {   
		log.info("Starting the process to load the logs at: " + new Date());
		
		List<SiteRecord> siteRecords = determineHostnames(routerLog.readFiles());
		
		List<SiteRecord> recs = siteRecordRepository.save(siteRecords);
	}
	
	private List<SiteRecord> determineHostnames(Set<Record> records) {
		List<SiteRecord> siteRecords = new ArrayList<>();

		int i = 0;//TODO rm
		
		for (Record record : records) {
		
			i++;
			if (i > 500) break; //TODO rm
			
			SiteRecord siteRecord = new SiteRecord();
			
			siteRecord.setDate(new LocalDate(record.getDate()));
			siteRecord.setDestinationSite(getHostname(record.getDestination()));
			siteRecord.setDevice(getHostname(record.getSource()));
			
			log.info(siteRecord.toString());
			
			siteRecords.add(siteRecord);
		}
		return siteRecords;
	}
	
	private String getHostname(String ipAddress) {  
		String hostname; 
		
		List<InternetAddress> internetAddresses = internetAddressRepository.findByIpAddress(ipAddress);
		
		if (internetAddresses.iterator().hasNext()) {
			hostname = internetAddresses.iterator().next().getHostname();
			log.debug("Used internet address from DB: " + ipAddress + "  Hostname: " + hostname);
		} else {
			hostname = internetAddressService.determineHostname(ipAddress).getHostName();
			
			internetAddressRepository.save(new InternetAddress(ipAddress, hostname));
			log.debug("Saving new internet address: " + ipAddress + "  Hostname: " + hostname);
		}
		return hostname;
	}
}


	