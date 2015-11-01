package com.marksoft.logger.service;

import java.io.IOException;
import java.net.SocketException;
import org.apache.commons.net.whois.WhoisClient;

public class WhoisTest {

	public static void main(String[] args) {

		WhoisTest obj = new WhoisTest();
		System.out.println(obj.getWhois("mkyong.com"));
		System.out.println("Done");

	}

	public String getWhois(String domainName) {

		StringBuilder result = new StringBuilder("");

		WhoisClient whois = new WhoisClient();
		try {

			//default is internic.net
			whois.connect("bom04s02-in-f5.1e100.net");//WhoisClient.DEFAULT_HOST);
			String whoisData1 = whois.query("=" + domainName);
			result.append(whoisData1);
			whois.disconnect();

		} catch (SocketException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return result.toString();

	}

}
