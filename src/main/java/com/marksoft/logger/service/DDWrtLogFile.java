package com.marksoft.logger.service;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Set;

import javax.inject.Inject;

import org.joda.time.LocalDate;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import com.google.common.collect.HashMultiset;
import com.google.common.collect.Multiset;
import com.marksoft.logger.domain.Record;
import com.marksoft.logger.domain.SiteRecord;
/**
 * Read Log file and parse into usable variables.
 * 
 * Example of data from Log File:
 * Oct 25 13:27:13 DD-WRT kern.warn kernel: ACCEPT IN=br0 OUT= MAC=dc:fb:02:82:9a:a0:00:1a:ef:17:bc:ca:08:00 SRC=192.168.11.104 DST=192.168.11.1 LEN=61 TOS=0x00 PREC=0x00 TTL=128 ID=28000 PROTO=UDP SPT=64275 DPT=53 LEN=41
 * 
 * @author mark
 */
@Service
public class DDWrtLogFile implements RouterLog {
	
	@Inject
	private Environment environment;
	
	public Set<Record> readFiles() {
		Multiset<Record> records=HashMultiset.create(1); 
		
		try {
			FileInputStream fstream = new FileInputStream(environment.getProperty("router.logFileName"));
			
			DataInputStream in = new DataInputStream(fstream);
			BufferedReader br = new BufferedReader(new InputStreamReader(in));
			String strLine;
			
			while ((strLine = br.readLine()) != null) {

				if (strLine.length() > 100) {

					String[] tokens = strLine.split(" ");  
					String month = tokens[0];
					String day = tokens[1];
					String macAddress = tokens[9];
					String src = tokens[10].replace("SRC=", "");
					String dest = tokens[11].replace("DST=", "");
					
					//Ignore anything on the ignore all list
					List<String> ignoreAllList =  Arrays.asList(environment.getProperty("router.ignoreAllList").split(" "));
					if (ignoreAllList.contains(src) || ignoreAllList.contains(dest)) {
						continue;
					}

					// Ignore lines that are internal to our network only
					String ignoreInternalNetwork = environment.getProperty("router.ignoreInternalNetwork");
					if (src.contains(ignoreInternalNetwork) && dest.contains(ignoreInternalNetwork)) {
						continue;
					}

					String year = String.valueOf(Calendar.getInstance().get(Calendar.YEAR));
					SimpleDateFormat format1 = new SimpleDateFormat("MMMddyyyy");
					Date date = format1.parse(month + day + year);
					
					Record record = new Record(date, macAddress, src, dest);
					records.add(record);
				}
			}
			in.close();
		} catch (ParseException | IOException e) {
			throw new RuntimeException(e);	
		}
		
		//Set the count of each record
		for (Record record : records) {
			record.setCount(records.count(record));
		}
		 
		return records.elementSet();
	}
	
	/*public List<SiteRecord> determineHostnames(Set<Record> records) {
		List<SiteRecord> siteRecords = new ArrayList<>();
		InetAddressService inetAddressTest = new InetAddressService();

		int i = 0;//TODO rm
		
		for (Record record : records) {
		
			i++;
			if (i > 10) break; //TODO rm
			
			SiteRecord siteRecord = new SiteRecord();
			
			siteRecord.setDate(new LocalDate(record.getDate())); //TODO map  Date to LocalDate
			siteRecord.setDestinationSite(inetAddressTest.determineHostname(record.getDestination()).getHostName());
			siteRecord.setDevice(inetAddressTest.determineHostname(record.getSource()).getHostName());
			
			System.out.println("+++" + siteRecord);
			
			siteRecords.add(siteRecord);
		}
		return siteRecords;
	} */
}


