package com.marksoft.logger.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.inject.Inject;

import org.joda.time.LocalDate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import com.marksoft.logger.domain.DhcpClient;
import com.marksoft.logger.domain.InternetAddress;
import com.marksoft.logger.domain.Record;
import com.marksoft.logger.domain.SiteRecord;
import com.marksoft.logger.repository.DhcpClientRepository;
import com.marksoft.logger.repository.InternetAddressRepository;
import com.marksoft.logger.repository.SiteRecordRepository;
	
@Service	
public class SiteMinderService {
	private final Logger log = LoggerFactory.getLogger(SiteMinderService.class);

	@Inject
	//TODO just testing to see if this is needed .. . Resource(name = "DDWrtLogFile")	
	RouterLog routerLog;
	
	@Inject
	SiteRecordRepository siteRecordRepository;
	
	@Inject	
	InetAddressService internetAddressService;
	
	@Inject
	InternetAddressRepository internetAddressRepository;
	
	@Inject
	DhcpClientRepository dhcpClientRepository;
	
	@Inject
	Environment env;
	
	private int MAX_RECORDS = 20; 
	
	@PostConstruct
	public void init() {
		String adadfd = env.getProperty("app.maxRecordsToRead");
		
		MAX_RECORDS = Integer.valueOf(env.getProperty("app.maxRecordsToRead"));
	}
	
	public void loadLogs() {   
		log.info("Starting the process to load the logs at: " + new Date());
		
		List<SiteRecord> siteRecords = determineHostnames(routerLog.readFiles());
		
		siteRecordRepository.save(siteRecords);
	}
	
	private List<SiteRecord> determineHostnames(Set<Record> records) {
		List<SiteRecord> siteRecords = new ArrayList<>();
		int maxRecordsToRead = 0;  
		
		for (Record record : records) {
			
			SiteRecord siteRecord = new SiteRecord();
			
			siteRecord.setDate(new LocalDate(record.getDate()));
			siteRecord.setDestinationSite(getHostname(record.getDestination()));
			siteRecord.setDevice(getDhcpDeviceName(record.getMacAddress(), record));
			
			log.info(siteRecord.toString());
			
			siteRecords.add(siteRecord);
			
			maxRecordsToRead++;
			if (maxRecordsToRead > MAX_RECORDS) break;
		}
		return siteRecords;
	}
	
	private String getDhcpDeviceName(String macAddress, Record record) {
		
		List<DhcpClient> addressesToDevice = dhcpClientRepository.findByMacAddress(macAddress);
		 
		if (!addressesToDevice.isEmpty()) {
			return addressesToDevice.iterator().next().getHostname();
		} 
		return getHostname(record.getSource());
	}

	private String getHostname(String ipAddress) {  
		String hostname = ""; 
		
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


	