package com.marksoft.logger.repository;

import com.marksoft.logger.domain.DhcpClient;
import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the DhcpClient entity.
 */
public interface DhcpClientRepository extends JpaRepository<DhcpClient,Long> {

}
