package com.marksoft.logger.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.marksoft.logger.domain.DhcpClient;
import com.marksoft.logger.repository.DhcpClientRepository;
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
 * REST controller for managing DhcpClient.
 */
@RestController
@RequestMapping("/api")
public class DhcpClientResource {

    private final Logger log = LoggerFactory.getLogger(DhcpClientResource.class);

    @Inject
    private DhcpClientRepository dhcpClientRepository;

    /**
     * POST  /dhcpClients -> Create a new dhcpClient.
     */
    @RequestMapping(value = "/dhcpClients",
            method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<DhcpClient> createDhcpClient(@RequestBody DhcpClient dhcpClient) throws URISyntaxException {
        log.debug("REST request to save DhcpClient : {}", dhcpClient);
        if (dhcpClient.getId() != null) {
            return ResponseEntity.badRequest().header("Failure", "A new dhcpClient cannot already have an ID").body(null);
        }
        DhcpClient result = dhcpClientRepository.save(dhcpClient);
        return ResponseEntity.created(new URI("/api/dhcpClients/" + result.getId()))
                .headers(HeaderUtil.createEntityCreationAlert("dhcpClient", result.getId().toString()))
                .body(result);
    }

    /**
     * PUT  /dhcpClients -> Updates an existing dhcpClient.
     */
    @RequestMapping(value = "/dhcpClients",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<DhcpClient> updateDhcpClient(@RequestBody DhcpClient dhcpClient) throws URISyntaxException {
        log.debug("REST request to update DhcpClient : {}", dhcpClient);
        if (dhcpClient.getId() == null) {
            return createDhcpClient(dhcpClient);
        }
        DhcpClient result = dhcpClientRepository.save(dhcpClient);
        return ResponseEntity.ok()
                .headers(HeaderUtil.createEntityUpdateAlert("dhcpClient", dhcpClient.getId().toString()))
                .body(result);
    }

    /**
     * GET  /dhcpClients -> get all the dhcpClients.
     */
    @RequestMapping(value = "/dhcpClients",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<DhcpClient> getAllDhcpClients() {
        log.debug("REST request to get all DhcpClients");
        return dhcpClientRepository.findAll();
    }

    /**
     * GET  /dhcpClients/:id -> get the "id" dhcpClient.
     */
    @RequestMapping(value = "/dhcpClients/{id}",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<DhcpClient> getDhcpClient(@PathVariable Long id) {
        log.debug("REST request to get DhcpClient : {}", id);
        return Optional.ofNullable(dhcpClientRepository.findOne(id))
            .map(dhcpClient -> new ResponseEntity<>(
                dhcpClient,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /dhcpClients/:id -> delete the "id" dhcpClient.
     */
    @RequestMapping(value = "/dhcpClients/{id}",
            method = RequestMethod.DELETE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteDhcpClient(@PathVariable Long id) {
        log.debug("REST request to delete DhcpClient : {}", id);
        dhcpClientRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("dhcpClient", id.toString())).build();
    }
}
