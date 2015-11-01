package com.marksoft.logger.service;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

import org.springframework.stereotype.Service;

import com.marksoft.logger.domain.Record;
import com.marksoft.logger.domain.SiteRecord;

@Service	
public class InetAddressService {
	
	public InetAddress determineHostname(String internetAddress) {
		try {
			return InetAddress.getByName(internetAddress);
			
		} catch (UnknownHostException e) {
			throw new RuntimeException(e);
		}
	}
	
	public static void main(String[] args) {

		InetAddress addr;
		try {
			
			String ids[] = {"192.168.11.147", "192.168.11.108", "192.168.11.104", "192.168.11.148", "31.13.66.5",
			"8.8.8.8", "24.32.41.171"		
			};
			List<String> idList = Arrays.asList(ids);
			
			for (String id : idList) {  
				addr = InetAddress.getByName(id);
				System.out.println("Canonical " + addr.getCanonicalHostName());
				System.out.println("Host " +addr.getHostName());
				System.out.println("ToString " +addr +"\n");
				
				//System.out.println("LoopBacK: " + InetAddress.getByName(addr.getHostAddress()).getLoopbackAddress());
			}
				
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
	}

}
