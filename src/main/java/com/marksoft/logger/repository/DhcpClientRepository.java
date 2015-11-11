package com.marksoft.logger.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.marksoft.logger.domain.DhcpClient;

/**
 * Spring Data JPA repository for the DhcpClient entity.
 */
public interface DhcpClientRepository extends JpaRepository<DhcpClient,Long> {
	List<DhcpClient> findByMacAddress(String macAddress);
}
