package com.marksoft.logger.repository;

import com.marksoft.logger.domain.InternetAddress;
import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the InternetAddress entity.
 */
public interface InternetAddressRepository extends JpaRepository<InternetAddress,Long> {

}
