package com.marksoft.logger.repository;

import com.marksoft.logger.domain.SiteRecord;
import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the SiteRecord entity.
 */
public interface SiteRecordRepository extends JpaRepository<SiteRecord,Long> {

}
