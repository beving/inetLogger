package com.marksoft.logger.config;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

import javax.annotation.Resource;
import javax.inject.Inject;

import org.joda.time.LocalDate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import com.marksoft.logger.domain.Record;
import com.marksoft.logger.domain.SiteRecord;
import com.marksoft.logger.repository.SiteRecordRepository;
import com.marksoft.logger.service.InetAddressService;
import com.marksoft.logger.service.RouterLog;
import com.marksoft.logger.service.SiteMinderService;
	
@Configuration
@EnableAsync
@EnableScheduling
public class LoadLogConfiguration {
	private final Logger log = LoggerFactory.getLogger(LoadLogConfiguration.class);
	
	@Inject	
	SiteMinderService siteMinderService;
	
	//Scheduled(cron="0 26 17 ? * *")
	@Scheduled(fixedRate=5000)
	public void loadLogsTask() {
		log.info("Starting the process to load the logs at: " + new Date());

		siteMinderService.loadLogs();
	}
	 
}


	