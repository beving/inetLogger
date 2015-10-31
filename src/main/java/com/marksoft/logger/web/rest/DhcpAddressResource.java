package com.marksoft.logger.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.marksoft.logger.domain.DhcpAddress;
import com.marksoft.logger.repository.DhcpAddressRepository;
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
 * REST controller for managing DhcpAddress.
 */
@RestController
@RequestMapping("/api")
public class DhcpAddressResource {

    private final Logger log = LoggerFactory.getLogger(DhcpAddressResource.class);

    @Inject
    private DhcpAddressRepository dhcpAddressRepository;

    /**
     * POST  /dhcpAddresss -> Create a new dhcpAddress.
     */
    @RequestMapping(value = "/dhcpAddresss",
            method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<DhcpAddress> createDhcpAddress(@RequestBody DhcpAddress dhcpAddress) throws URISyntaxException {
        log.debug("REST request to save DhcpAddress : {}", dhcpAddress);
        if (dhcpAddress.getId() != null) {
            return ResponseEntity.badRequest().header("Failure", "A new dhcpAddress cannot already have an ID").body(null);
        }
        DhcpAddress result = dhcpAddressRepository.save(dhcpAddress);
        return ResponseEntity.created(new URI("/api/dhcpAddresss/" + result.getId()))
                .headers(HeaderUtil.createEntityCreationAlert("dhcpAddress", result.getId().toString()))
                .body(result);
    }

    /**
     * PUT  /dhcpAddresss -> Updates an existing dhcpAddress.
     */
    @RequestMapping(value = "/dhcpAddresss",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<DhcpAddress> updateDhcpAddress(@RequestBody DhcpAddress dhcpAddress) throws URISyntaxException {
        log.debug("REST request to update DhcpAddress : {}", dhcpAddress);
        if (dhcpAddress.getId() == null) {
            return createDhcpAddress(dhcpAddress);
        }
        DhcpAddress result = dhcpAddressRepository.save(dhcpAddress);
        return ResponseEntity.ok()
                .headers(HeaderUtil.createEntityUpdateAlert("dhcpAddress", dhcpAddress.getId().toString()))
                .body(result);
    }

    /**
     * GET  /dhcpAddresss -> get all the dhcpAddresss.
     */
    @RequestMapping(value = "/dhcpAddresss",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<DhcpAddress> getAllDhcpAddresss() {
        log.debug("REST request to get all DhcpAddresss");
        return dhcpAddressRepository.findAll();
    }

    /**
     * GET  /dhcpAddresss/:id -> get the "id" dhcpAddress.
     */
    @RequestMapping(value = "/dhcpAddresss/{id}",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<DhcpAddress> getDhcpAddress(@PathVariable Long id) {
        log.debug("REST request to get DhcpAddress : {}", id);
        return Optional.ofNullable(dhcpAddressRepository.findOne(id))
            .map(dhcpAddress -> new ResponseEntity<>(
                dhcpAddress,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /dhcpAddresss/:id -> delete the "id" dhcpAddress.
     */
    @RequestMapping(value = "/dhcpAddresss/{id}",
            method = RequestMethod.DELETE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteDhcpAddress(@PathVariable Long id) {
        log.debug("REST request to delete DhcpAddress : {}", id);
        dhcpAddressRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("dhcpAddress", id.toString())).build();
    }
}
