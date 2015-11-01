package com.marksoft.logger.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.marksoft.logger.domain.InternetAddress;

/**
 * Spring Data JPA repository for the InternetAddress entity.
 */
public interface InternetAddressRepository extends JpaRepository<InternetAddress,Long> {
	
		public List<InternetAddress> findByIpAddress(String ipAddress);
}
