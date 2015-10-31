package com.marksoft.logger.web.rest;

import com.marksoft.logger.Application;
import com.marksoft.logger.domain.DhcpAddress;
import com.marksoft.logger.repository.DhcpAddressRepository;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import static org.hamcrest.Matchers.hasItem;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.IntegrationTest;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


/**
 * Test class for the DhcpAddressResource REST controller.
 *
 * @see DhcpAddressResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest
public class DhcpAddressResourceTest {

    private static final String DEFAULT_MAC_ADDRESS = "AAAAA";
    private static final String UPDATED_MAC_ADDRESS = "BBBBB";
    private static final String DEFAULT_IP_ADDRESS = "AAAAA";
    private static final String UPDATED_IP_ADDRESS = "BBBBB";

    @Inject
    private DhcpAddressRepository dhcpAddressRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restDhcpAddressMockMvc;

    private DhcpAddress dhcpAddress;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        DhcpAddressResource dhcpAddressResource = new DhcpAddressResource();
        ReflectionTestUtils.setField(dhcpAddressResource, "dhcpAddressRepository", dhcpAddressRepository);
        this.restDhcpAddressMockMvc = MockMvcBuilders.standaloneSetup(dhcpAddressResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        dhcpAddress = new DhcpAddress();
        dhcpAddress.setMacAddress(DEFAULT_MAC_ADDRESS);
        dhcpAddress.setIpAddress(DEFAULT_IP_ADDRESS);
    }

    @Test
    @Transactional
    public void createDhcpAddress() throws Exception {
        int databaseSizeBeforeCreate = dhcpAddressRepository.findAll().size();

        // Create the DhcpAddress

        restDhcpAddressMockMvc.perform(post("/api/dhcpAddresss")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(dhcpAddress)))
                .andExpect(status().isCreated());

        // Validate the DhcpAddress in the database
        List<DhcpAddress> dhcpAddresss = dhcpAddressRepository.findAll();
        assertThat(dhcpAddresss).hasSize(databaseSizeBeforeCreate + 1);
        DhcpAddress testDhcpAddress = dhcpAddresss.get(dhcpAddresss.size() - 1);
        assertThat(testDhcpAddress.getMacAddress()).isEqualTo(DEFAULT_MAC_ADDRESS);
        assertThat(testDhcpAddress.getIpAddress()).isEqualTo(DEFAULT_IP_ADDRESS);
    }

    @Test
    @Transactional
    public void getAllDhcpAddresss() throws Exception {
        // Initialize the database
        dhcpAddressRepository.saveAndFlush(dhcpAddress);

        // Get all the dhcpAddresss
        restDhcpAddressMockMvc.perform(get("/api/dhcpAddresss"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(dhcpAddress.getId().intValue())))
                .andExpect(jsonPath("$.[*].macAddress").value(hasItem(DEFAULT_MAC_ADDRESS.toString())))
                .andExpect(jsonPath("$.[*].ipAddress").value(hasItem(DEFAULT_IP_ADDRESS.toString())));
    }

    @Test
    @Transactional
    public void getDhcpAddress() throws Exception {
        // Initialize the database
        dhcpAddressRepository.saveAndFlush(dhcpAddress);

        // Get the dhcpAddress
        restDhcpAddressMockMvc.perform(get("/api/dhcpAddresss/{id}", dhcpAddress.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(dhcpAddress.getId().intValue()))
            .andExpect(jsonPath("$.macAddress").value(DEFAULT_MAC_ADDRESS.toString()))
            .andExpect(jsonPath("$.ipAddress").value(DEFAULT_IP_ADDRESS.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingDhcpAddress() throws Exception {
        // Get the dhcpAddress
        restDhcpAddressMockMvc.perform(get("/api/dhcpAddresss/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateDhcpAddress() throws Exception {
        // Initialize the database
        dhcpAddressRepository.saveAndFlush(dhcpAddress);

		int databaseSizeBeforeUpdate = dhcpAddressRepository.findAll().size();

        // Update the dhcpAddress
        dhcpAddress.setMacAddress(UPDATED_MAC_ADDRESS);
        dhcpAddress.setIpAddress(UPDATED_IP_ADDRESS);

        restDhcpAddressMockMvc.perform(put("/api/dhcpAddresss")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(dhcpAddress)))
                .andExpect(status().isOk());

        // Validate the DhcpAddress in the database
        List<DhcpAddress> dhcpAddresss = dhcpAddressRepository.findAll();
        assertThat(dhcpAddresss).hasSize(databaseSizeBeforeUpdate);
        DhcpAddress testDhcpAddress = dhcpAddresss.get(dhcpAddresss.size() - 1);
        assertThat(testDhcpAddress.getMacAddress()).isEqualTo(UPDATED_MAC_ADDRESS);
        assertThat(testDhcpAddress.getIpAddress()).isEqualTo(UPDATED_IP_ADDRESS);
    }

    @Test
    @Transactional
    public void deleteDhcpAddress() throws Exception {
        // Initialize the database
        dhcpAddressRepository.saveAndFlush(dhcpAddress);

		int databaseSizeBeforeDelete = dhcpAddressRepository.findAll().size();

        // Get the dhcpAddress
        restDhcpAddressMockMvc.perform(delete("/api/dhcpAddresss/{id}", dhcpAddress.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<DhcpAddress> dhcpAddresss = dhcpAddressRepository.findAll();
        assertThat(dhcpAddresss).hasSize(databaseSizeBeforeDelete - 1);
    }
}
