package com.marksoft.logger.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.marksoft.logger.domain.InternetAddress;
import com.marksoft.logger.repository.InternetAddressRepository;
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
 * REST controller for managing InternetAddress.
 */
@RestController
@RequestMapping("/api")
public class InternetAddressResource {

    private final Logger log = LoggerFactory.getLogger(InternetAddressResource.class);

    @Inject
    private InternetAddressRepository internetAddressRepository;

    /**
     * POST  /internetAddresss -> Create a new internetAddress.
     */
    @RequestMapping(value = "/internetAddresss",
            method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<InternetAddress> createInternetAddress(@RequestBody InternetAddress internetAddress) throws URISyntaxException {
        log.debug("REST request to save InternetAddress : {}", internetAddress);
        if (internetAddress.getId() != null) {
            return ResponseEntity.badRequest().header("Failure", "A new internetAddress cannot already have an ID").body(null);
        }
        InternetAddress result = internetAddressRepository.save(internetAddress);
        return ResponseEntity.created(new URI("/api/internetAddresss/" + result.getId()))
                .headers(HeaderUtil.createEntityCreationAlert("internetAddress", result.getId().toString()))
                .body(result);
    }

    /**
     * PUT  /internetAddresss -> Updates an existing internetAddress.
     */
    @RequestMapping(value = "/internetAddresss",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<InternetAddress> updateInternetAddress(@RequestBody InternetAddress internetAddress) throws URISyntaxException {
        log.debug("REST request to update InternetAddress : {}", internetAddress);
        if (internetAddress.getId() == null) {
            return createInternetAddress(internetAddress);
        }
        InternetAddress result = internetAddressRepository.save(internetAddress);
        return ResponseEntity.ok()
                .headers(HeaderUtil.createEntityUpdateAlert("internetAddress", internetAddress.getId().toString()))
                .body(result);
    }

    /**
     * GET  /internetAddresss -> get all the internetAddresss.
     */
    @RequestMapping(value = "/internetAddresss",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<InternetAddress> getAllInternetAddresss() {
        log.debug("REST request to get all InternetAddresss");
        return internetAddressRepository.findAll();
    }

    /**
     * GET  /internetAddresss/:id -> get the "id" internetAddress.
     */
    @RequestMapping(value = "/internetAddresss/{id}",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<InternetAddress> getInternetAddress(@PathVariable Long id) {
        log.debug("REST request to get InternetAddress : {}", id);
        return Optional.ofNullable(internetAddressRepository.findOne(id))
            .map(internetAddress -> new ResponseEntity<>(
                internetAddress,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /internetAddresss/:id -> delete the "id" internetAddress.
     */
    @RequestMapping(value = "/internetAddresss/{id}",
            method = RequestMethod.DELETE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteInternetAddress(@PathVariable Long id) {
        log.debug("REST request to delete InternetAddress : {}", id);
        internetAddressRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("internetAddress", id.toString())).build();
    }
}
