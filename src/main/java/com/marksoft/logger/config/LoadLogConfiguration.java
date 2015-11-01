package com.marksoft.logger.config;

import java.util.Arrays;
import java.util.Collection;
import java.util.Date;

import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import com.marksoft.logger.service.SiteMinderService;
	
@Configuration
@EnableAsync
@EnableScheduling
public class LoadLogConfiguration {
	private final Logger log = LoggerFactory.getLogger(LoadLogConfiguration.class);
	
	@Inject
    private Environment env;
	
	@Inject	
	SiteMinderService siteMinderService;
	
	public LoadLogConfiguration() {
		//Load up when testing
		Collection<String> activeProfiles = Arrays.asList(env.getActiveProfiles());
		if (activeProfiles.contains(Constants.SPRING_PROFILE_DEVELOPMENT)) {
			loadLogsTask();
		}
	}
	
	@Scheduled(cron="0 57 23 ? * *") //Run at a few minutes before midnight.
	public void loadLogsTask() {
		log.info("Starting the process to load the logs at: " + new Date());

		siteMinderService.loadLogs();
	}
}


	