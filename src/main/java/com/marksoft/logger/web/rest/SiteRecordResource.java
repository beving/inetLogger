package com.marksoft.logger.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.marksoft.logger.domain.SiteRecord;
import com.marksoft.logger.repository.SiteRecordRepository;
import com.marksoft.logger.web.rest.util.HeaderUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing SiteRecord.
 */
@RestController
@RequestMapping("/api")
public class SiteRecordResource {

    private final Logger log = LoggerFactory.getLogger(SiteRecordResource.class);

    @Inject
    private SiteRecordRepository siteRecordRepository;

    /**
     * POST  /siteRecords -> Create a new siteRecord.
     */
    @RequestMapping(value = "/siteRecords",
            method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<SiteRecord> createSiteRecord(@RequestBody SiteRecord siteRecord) throws URISyntaxException {
        log.debug("REST request to save SiteRecord : {}", siteRecord);
        if (siteRecord.getId() != null) {
            return ResponseEntity.badRequest().header("Failure", "A new siteRecord cannot already have an ID").body(null);
        }
        SiteRecord result = siteRecordRepository.save(siteRecord);
        return ResponseEntity.created(new URI("/api/siteRecords/" + result.getId()))
                .headers(HeaderUtil.createEntityCreationAlert("siteRecord", result.getId().toString()))
                .body(result);
    }

    /**
     * PUT  /siteRecords -> Updates an existing siteRecord.
     */
    @RequestMapping(value = "/siteRecords",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<SiteRecord> updateSiteRecord(@RequestBody SiteRecord siteRecord) throws URISyntaxException {
        log.debug("REST request to update SiteRecord : {}", siteRecord);
        if (siteRecord.getId() == null) {
            return createSiteRecord(siteRecord);
        }
        SiteRecord result = siteRecordRepository.save(siteRecord);
        return ResponseEntity.ok()
                .headers(HeaderUtil.createEntityUpdateAlert("siteRecord", siteRecord.getId().toString()))
                .body(result);
    }

    /**
     * GET  /siteRecords -> get all the siteRecords.
     */
    @RequestMapping(value = "/siteRecords",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<SiteRecord> getAllSiteRecords() {
        log.debug("REST request to get all SiteRecords");
        return siteRecordRepository.findAll();
    }

    /**
     * GET  /siteRecords/:id -> get the "id" siteRecord.
     */
    @RequestMapping(value = "/siteRecords/{id}",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<SiteRecord> getSiteRecord(@PathVariable Long id) {
        log.debug("REST request to get SiteRecord : {}", id);
        return Optional.ofNullable(siteRecordRepository.findOne(id))
            .map(siteRecord -> new ResponseEntity<>(
                siteRecord,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /siteRecords/:id -> delete the "id" siteRecord.
     */
    @RequestMapping(value = "/siteRecords/{id}",
            method = RequestMethod.DELETE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteSiteRecord(@PathVariable Long id) {
        log.debug("REST request to delete SiteRecord : {}", id);
        siteRecordRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("siteRecord", id.toString())).build();
    }
}
