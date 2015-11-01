package com.marksoft.logger.web.rest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.codahale.metrics.annotation.Timed;

/**
 * REST controller for managing DhcpAddress.
 */
@RestController
@RequestMapping("/api")
public class LogReadResource {

    private final Logger log = LoggerFactory.getLogger(LogReadResource.class);

    /**
     * GET  /dhcpAddresss -> get all the dhcpAddresss.
     */
    @RequestMapping(value = "/processLog",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public String processLog() {
        log.debug("REST request to load the log");
        return "Test123";
    }
}
	