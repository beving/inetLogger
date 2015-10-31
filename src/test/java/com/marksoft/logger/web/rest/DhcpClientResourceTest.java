package com.marksoft.logger.web.rest;

import com.marksoft.logger.Application;
import com.marksoft.logger.domain.DhcpClient;
import com.marksoft.logger.repository.DhcpClientRepository;

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
 * Test class for the DhcpClientResource REST controller.
 *
 * @see DhcpClientResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest
public class DhcpClientResourceTest {

    private static final String DEFAULT_MAC_ADDRESS = "AAAAA";
    private static final String UPDATED_MAC_ADDRESS = "BBBBB";
    private static final String DEFAULT_HOSTNAME = "AAAAA";
    private static final String UPDATED_HOSTNAME = "BBBBB";
    private static final String DEFAULT_COMMENT = "AAAAA";
    private static final String UPDATED_COMMENT = "BBBBB";

    @Inject
    private DhcpClientRepository dhcpClientRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restDhcpClientMockMvc;

    private DhcpClient dhcpClient;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        DhcpClientResource dhcpClientResource = new DhcpClientResource();
        ReflectionTestUtils.setField(dhcpClientResource, "dhcpClientRepository", dhcpClientRepository);
        this.restDhcpClientMockMvc = MockMvcBuilders.standaloneSetup(dhcpClientResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        dhcpClient = new DhcpClient();
        dhcpClient.setMacAddress(DEFAULT_MAC_ADDRESS);
        dhcpClient.setHostname(DEFAULT_HOSTNAME);
        dhcpClient.setComment(DEFAULT_COMMENT);
    }

    @Test
    @Transactional
    public void createDhcpClient() throws Exception {
        int databaseSizeBeforeCreate = dhcpClientRepository.findAll().size();

        // Create the DhcpClient

        restDhcpClientMockMvc.perform(post("/api/dhcpClients")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(dhcpClient)))
                .andExpect(status().isCreated());

        // Validate the DhcpClient in the database
        List<DhcpClient> dhcpClients = dhcpClientRepository.findAll();
        assertThat(dhcpClients).hasSize(databaseSizeBeforeCreate + 1);
        DhcpClient testDhcpClient = dhcpClients.get(dhcpClients.size() - 1);
        assertThat(testDhcpClient.getMacAddress()).isEqualTo(DEFAULT_MAC_ADDRESS);
        assertThat(testDhcpClient.getHostname()).isEqualTo(DEFAULT_HOSTNAME);
        assertThat(testDhcpClient.getComment()).isEqualTo(DEFAULT_COMMENT);
    }

    @Test
    @Transactional
    public void getAllDhcpClients() throws Exception {
        // Initialize the database
        dhcpClientRepository.saveAndFlush(dhcpClient);

        // Get all the dhcpClients
        restDhcpClientMockMvc.perform(get("/api/dhcpClients"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(dhcpClient.getId().intValue())))
                .andExpect(jsonPath("$.[*].macAddress").value(hasItem(DEFAULT_MAC_ADDRESS.toString())))
                .andExpect(jsonPath("$.[*].hostname").value(hasItem(DEFAULT_HOSTNAME.toString())))
                .andExpect(jsonPath("$.[*].comment").value(hasItem(DEFAULT_COMMENT.toString())));
    }

    @Test
    @Transactional
    public void getDhcpClient() throws Exception {
        // Initialize the database
        dhcpClientRepository.saveAndFlush(dhcpClient);

        // Get the dhcpClient
        restDhcpClientMockMvc.perform(get("/api/dhcpClients/{id}", dhcpClient.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(dhcpClient.getId().intValue()))
            .andExpect(jsonPath("$.macAddress").value(DEFAULT_MAC_ADDRESS.toString()))
            .andExpect(jsonPath("$.hostname").value(DEFAULT_HOSTNAME.toString()))
            .andExpect(jsonPath("$.comment").value(DEFAULT_COMMENT.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingDhcpClient() throws Exception {
        // Get the dhcpClient
        restDhcpClientMockMvc.perform(get("/api/dhcpClients/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateDhcpClient() throws Exception {
        // Initialize the database
        dhcpClientRepository.saveAndFlush(dhcpClient);

		int databaseSizeBeforeUpdate = dhcpClientRepository.findAll().size();

        // Update the dhcpClient
        dhcpClient.setMacAddress(UPDATED_MAC_ADDRESS);
        dhcpClient.setHostname(UPDATED_HOSTNAME);
        dhcpClient.setComment(UPDATED_COMMENT);

        restDhcpClientMockMvc.perform(put("/api/dhcpClients")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(dhcpClient)))
                .andExpect(status().isOk());

        // Validate the DhcpClient in the database
        List<DhcpClient> dhcpClients = dhcpClientRepository.findAll();
        assertThat(dhcpClients).hasSize(databaseSizeBeforeUpdate);
        DhcpClient testDhcpClient = dhcpClients.get(dhcpClients.size() - 1);
        assertThat(testDhcpClient.getMacAddress()).isEqualTo(UPDATED_MAC_ADDRESS);
        assertThat(testDhcpClient.getHostname()).isEqualTo(UPDATED_HOSTNAME);
        assertThat(testDhcpClient.getComment()).isEqualTo(UPDATED_COMMENT);
    }

    @Test
    @Transactional
    public void deleteDhcpClient() throws Exception {
        // Initialize the database
        dhcpClientRepository.saveAndFlush(dhcpClient);

		int databaseSizeBeforeDelete = dhcpClientRepository.findAll().size();

        // Get the dhcpClient
        restDhcpClientMockMvc.perform(delete("/api/dhcpClients/{id}", dhcpClient.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<DhcpClient> dhcpClients = dhcpClientRepository.findAll();
        assertThat(dhcpClients).hasSize(databaseSizeBeforeDelete - 1);
    }
}
