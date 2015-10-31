package com.marksoft.logger.repository;

import com.marksoft.logger.domain.DhcpAddress;
import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the DhcpAddress entity.
 */
public interface DhcpAddressRepository extends JpaRepository<DhcpAddress,Long> {

}
