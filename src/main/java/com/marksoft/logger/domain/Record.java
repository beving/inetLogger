package com.marksoft.logger.domain;

import java.util.Date;

public class Record {
	Date date;
	String macAddress;
	String source;
	String destination;
	int count;
	
	public Record() {
	}
	
	public Record(Date date, String macAddress, String src, String dest ) {
		this.date = date;
		this.macAddress = macAddress;
		this.source = src;
		this.destination = dest;
		sanctify();
	}

	public void setDate(Date date) {
		this.date = date;
	}	

	public void setMacAddress(String macAddress) {
		this.macAddress = macAddress;
	}

	public void setSource(String source) {
		this.source = source;
	}

	public void setDestination(String destination) {
		this.destination = destination;
	}

	public void setCount(int count) {
		this.count = count;
	}
	
	public Date getDate() {
		return date;
	}

	public String getMacAddress() {
		return macAddress;
	}

	public String getSource() {
		return source;
	}

	public String getDestination() {
		return destination;
	}

	private void sanctify() {
		String networkPrefix = "MAC=dc:fb:02:82:9a:a0:"; //TODO put into yaml properties file
		String netWorkPostFix =  ":08:00";

		macAddress = macAddress.replace(networkPrefix, "");
		macAddress = macAddress.replace(netWorkPostFix, "");
		
		setMacAddress(macAddress);
		System.out.println("After fixing " + macAddress);
		
		setSource(source.replace("SRC=", ""));
		setDestination(destination.replace("DST=", ""));
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((date == null) ? 0 : date.hashCode());
		result = prime * result + ((destination == null) ? 0 : destination.hashCode());
		result = prime * result + ((macAddress == null) ? 0 : macAddress.hashCode());
		result = prime * result + ((source == null) ? 0 : source.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Record other = (Record) obj;
		if (date == null) {
			if (other.date != null)
				return false;
		} else if (!date.equals(other.date))
			return false;
		if (destination == null) {
			if (other.destination != null)
				return false;
		} else if (!destination.equals(other.destination))
			return false;
		if (macAddress == null) {
			if (other.macAddress != null)
				return false;
		} else if (!macAddress.equals(other.macAddress))
			return false;
		if (source == null) {
			if (other.source != null)
				return false;
		} else if (!source.equals(other.source))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Record [date=" + date + ", macAddress=" + macAddress + ", source=" + source + ", destination="
				+ destination + ", count=" + count + "]";
	}

}
