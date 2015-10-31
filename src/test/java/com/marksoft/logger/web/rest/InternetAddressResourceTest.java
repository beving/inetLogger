package com.marksoft.logger.web.rest;

import com.marksoft.logger.Application;
import com.marksoft.logger.domain.InternetAddress;
import com.marksoft.logger.repository.InternetAddressRepository;

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
 * Test class for the InternetAddressResource REST controller.
 *
 * @see InternetAddressResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest
public class InternetAddressResourceTest {

    private static final String DEFAULT_IP_ADDRESS = "AAAAA";
    private static final String UPDATED_IP_ADDRESS = "BBBBB";
    private static final String DEFAULT_HOSTNAME = "AAAAA";
    private static final String UPDATED_HOSTNAME = "BBBBB";

    @Inject
    private InternetAddressRepository internetAddressRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restInternetAddressMockMvc;

    private InternetAddress internetAddress;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        InternetAddressResource internetAddressResource = new InternetAddressResource();
        ReflectionTestUtils.setField(internetAddressResource, "internetAddressRepository", internetAddressRepository);
        this.restInternetAddressMockMvc = MockMvcBuilders.standaloneSetup(internetAddressResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        internetAddress = new InternetAddress();
        internetAddress.setIpAddress(DEFAULT_IP_ADDRESS);
        internetAddress.setHostname(DEFAULT_HOSTNAME);
    }

    @Test
    @Transactional
    public void createInternetAddress() throws Exception {
        int databaseSizeBeforeCreate = internetAddressRepository.findAll().size();

        // Create the InternetAddress

        restInternetAddressMockMvc.perform(post("/api/internetAddresss")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(internetAddress)))
                .andExpect(status().isCreated());

        // Validate the InternetAddress in the database
        List<InternetAddress> internetAddresss = internetAddressRepository.findAll();
        assertThat(internetAddresss).hasSize(databaseSizeBeforeCreate + 1);
        InternetAddress testInternetAddress = internetAddresss.get(internetAddresss.size() - 1);
        assertThat(testInternetAddress.getIpAddress()).isEqualTo(DEFAULT_IP_ADDRESS);
        assertThat(testInternetAddress.getHostname()).isEqualTo(DEFAULT_HOSTNAME);
    }

    @Test
    @Transactional
    public void getAllInternetAddresss() throws Exception {
        // Initialize the database
        internetAddressRepository.saveAndFlush(internetAddress);

        // Get all the internetAddresss
        restInternetAddressMockMvc.perform(get("/api/internetAddresss"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(internetAddress.getId().intValue())))
                .andExpect(jsonPath("$.[*].ipAddress").value(hasItem(DEFAULT_IP_ADDRESS.toString())))
                .andExpect(jsonPath("$.[*].hostname").value(hasItem(DEFAULT_HOSTNAME.toString())));
    }

    @Test
    @Transactional
    public void getInternetAddress() throws Exception {
        // Initialize the database
        internetAddressRepository.saveAndFlush(internetAddress);

        // Get the internetAddress
        restInternetAddressMockMvc.perform(get("/api/internetAddresss/{id}", internetAddress.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(internetAddress.getId().intValue()))
            .andExpect(jsonPath("$.ipAddress").value(DEFAULT_IP_ADDRESS.toString()))
            .andExpect(jsonPath("$.hostname").value(DEFAULT_HOSTNAME.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingInternetAddress() throws Exception {
        // Get the internetAddress
        restInternetAddressMockMvc.perform(get("/api/internetAddresss/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateInternetAddress() throws Exception {
        // Initialize the database
        internetAddressRepository.saveAndFlush(internetAddress);

		int databaseSizeBeforeUpdate = internetAddressRepository.findAll().size();

        // Update the internetAddress
        internetAddress.setIpAddress(UPDATED_IP_ADDRESS);
        internetAddress.setHostname(UPDATED_HOSTNAME);

        restInternetAddressMockMvc.perform(put("/api/internetAddresss")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(internetAddress)))
                .andExpect(status().isOk());

        // Validate the InternetAddress in the database
        List<InternetAddress> internetAddresss = internetAddressRepository.findAll();
        assertThat(internetAddresss).hasSize(databaseSizeBeforeUpdate);
        InternetAddress testInternetAddress = internetAddresss.get(internetAddresss.size() - 1);
        assertThat(testInternetAddress.getIpAddress()).isEqualTo(UPDATED_IP_ADDRESS);
        assertThat(testInternetAddress.getHostname()).isEqualTo(UPDATED_HOSTNAME);
    }

    @Test
    @Transactional
    public void deleteInternetAddress() throws Exception {
        // Initialize the database
        internetAddressRepository.saveAndFlush(internetAddress);

		int databaseSizeBeforeDelete = internetAddressRepository.findAll().size();

        // Get the internetAddress
        restInternetAddressMockMvc.perform(delete("/api/internetAddresss/{id}", internetAddress.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<InternetAddress> internetAddresss = internetAddressRepository.findAll();
        assertThat(internetAddresss).hasSize(databaseSizeBeforeDelete - 1);
    }
}
