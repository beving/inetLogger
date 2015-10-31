package com.marksoft.logger.web.rest;

import com.marksoft.logger.Application;
import com.marksoft.logger.domain.SiteRecord;
import com.marksoft.logger.repository.SiteRecordRepository;

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
import org.joda.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


/**
 * Test class for the SiteRecordResource REST controller.
 *
 * @see SiteRecordResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest
public class SiteRecordResourceTest {

    private static final String DEFAULT_DEVICE = "AAAAA";
    private static final String UPDATED_DEVICE = "BBBBB";
    private static final String DEFAULT_DESTINATION_SITE = "AAAAA";
    private static final String UPDATED_DESTINATION_SITE = "BBBBB";

    private static final LocalDate DEFAULT_DATE = new LocalDate(0L);
    private static final LocalDate UPDATED_DATE = new LocalDate();

    @Inject
    private SiteRecordRepository siteRecordRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restSiteRecordMockMvc;

    private SiteRecord siteRecord;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        SiteRecordResource siteRecordResource = new SiteRecordResource();
        ReflectionTestUtils.setField(siteRecordResource, "siteRecordRepository", siteRecordRepository);
        this.restSiteRecordMockMvc = MockMvcBuilders.standaloneSetup(siteRecordResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        siteRecord = new SiteRecord();
        siteRecord.setDevice(DEFAULT_DEVICE);
        siteRecord.setDestinationSite(DEFAULT_DESTINATION_SITE);
        siteRecord.setDate(DEFAULT_DATE);
    }

    @Test
    @Transactional
    public void createSiteRecord() throws Exception {
        int databaseSizeBeforeCreate = siteRecordRepository.findAll().size();

        // Create the SiteRecord

        restSiteRecordMockMvc.perform(post("/api/siteRecords")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(siteRecord)))
                .andExpect(status().isCreated());

        // Validate the SiteRecord in the database
        List<SiteRecord> siteRecords = siteRecordRepository.findAll();
        assertThat(siteRecords).hasSize(databaseSizeBeforeCreate + 1);
        SiteRecord testSiteRecord = siteRecords.get(siteRecords.size() - 1);
        assertThat(testSiteRecord.getDevice()).isEqualTo(DEFAULT_DEVICE);
        assertThat(testSiteRecord.getDestinationSite()).isEqualTo(DEFAULT_DESTINATION_SITE);
        assertThat(testSiteRecord.getDate()).isEqualTo(DEFAULT_DATE);
    }

    @Test
    @Transactional
    public void getAllSiteRecords() throws Exception {
        // Initialize the database
        siteRecordRepository.saveAndFlush(siteRecord);

        // Get all the siteRecords
        restSiteRecordMockMvc.perform(get("/api/siteRecords"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(siteRecord.getId().intValue())))
                .andExpect(jsonPath("$.[*].device").value(hasItem(DEFAULT_DEVICE.toString())))
                .andExpect(jsonPath("$.[*].destinationSite").value(hasItem(DEFAULT_DESTINATION_SITE.toString())))
                .andExpect(jsonPath("$.[*].date").value(hasItem(DEFAULT_DATE.toString())));
    }

    @Test
    @Transactional
    public void getSiteRecord() throws Exception {
        // Initialize the database
        siteRecordRepository.saveAndFlush(siteRecord);

        // Get the siteRecord
        restSiteRecordMockMvc.perform(get("/api/siteRecords/{id}", siteRecord.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(siteRecord.getId().intValue()))
            .andExpect(jsonPath("$.device").value(DEFAULT_DEVICE.toString()))
            .andExpect(jsonPath("$.destinationSite").value(DEFAULT_DESTINATION_SITE.toString()))
            .andExpect(jsonPath("$.date").value(DEFAULT_DATE.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingSiteRecord() throws Exception {
        // Get the siteRecord
        restSiteRecordMockMvc.perform(get("/api/siteRecords/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateSiteRecord() throws Exception {
        // Initialize the database
        siteRecordRepository.saveAndFlush(siteRecord);

		int databaseSizeBeforeUpdate = siteRecordRepository.findAll().size();

        // Update the siteRecord
        siteRecord.setDevice(UPDATED_DEVICE);
        siteRecord.setDestinationSite(UPDATED_DESTINATION_SITE);
        siteRecord.setDate(UPDATED_DATE);

        restSiteRecordMockMvc.perform(put("/api/siteRecords")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(siteRecord)))
                .andExpect(status().isOk());

        // Validate the SiteRecord in the database
        List<SiteRecord> siteRecords = siteRecordRepository.findAll();
        assertThat(siteRecords).hasSize(databaseSizeBeforeUpdate);
        SiteRecord testSiteRecord = siteRecords.get(siteRecords.size() - 1);
        assertThat(testSiteRecord.getDevice()).isEqualTo(UPDATED_DEVICE);
        assertThat(testSiteRecord.getDestinationSite()).isEqualTo(UPDATED_DESTINATION_SITE);
        assertThat(testSiteRecord.getDate()).isEqualTo(UPDATED_DATE);
    }

    @Test
    @Transactional
    public void deleteSiteRecord() throws Exception {
        // Initialize the database
        siteRecordRepository.saveAndFlush(siteRecord);

		int databaseSizeBeforeDelete = siteRecordRepository.findAll().size();

        // Get the siteRecord
        restSiteRecordMockMvc.perform(delete("/api/siteRecords/{id}", siteRecord.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<SiteRecord> siteRecords = siteRecordRepository.findAll();
        assertThat(siteRecords).hasSize(databaseSizeBeforeDelete - 1);
    }
}
