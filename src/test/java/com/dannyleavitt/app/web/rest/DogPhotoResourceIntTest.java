package com.dannyleavitt.app.web.rest;

import com.dannyleavitt.app.ApiChallengeApp;
import com.dannyleavitt.app.domain.DogPhoto;
import com.dannyleavitt.app.repository.DogPhotoRepository;
import com.dannyleavitt.app.service.DogPhotoService;

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
 * Test class for the DogPhotoResource REST controller.
 *
 * @see DogPhotoResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = ApiChallengeApp.class)
@WebAppConfiguration
@IntegrationTest
public class DogPhotoResourceIntTest {

    private static final String DEFAULT_URL = "AAAAA";
    private static final String UPDATED_URL = "BBBBB";
    private static final String DEFAULT_DESCRIPTION = "AAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBB";

    @Inject
    private DogPhotoRepository dogPhotoRepository;

    @Inject
    private DogPhotoService dogPhotoService;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restDogPhotoMockMvc;

    private DogPhoto dogPhoto;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        DogPhotoResource dogPhotoResource = new DogPhotoResource();
        ReflectionTestUtils.setField(dogPhotoResource, "dogPhotoService", dogPhotoService);
        this.restDogPhotoMockMvc = MockMvcBuilders.standaloneSetup(dogPhotoResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        dogPhoto = new DogPhoto();
        dogPhoto.setUrl(DEFAULT_URL);
        dogPhoto.setDescription(DEFAULT_DESCRIPTION);
    }

    @Test
    @Transactional
    public void createDogPhoto() throws Exception {
        int databaseSizeBeforeCreate = dogPhotoRepository.findAll().size();

        // Create the DogPhoto

        restDogPhotoMockMvc.perform(post("/api/dog-photos")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(dogPhoto)))
                .andExpect(status().isCreated());

        // Validate the DogPhoto in the database
        List<DogPhoto> dogPhotos = dogPhotoRepository.findAll();
        assertThat(dogPhotos).hasSize(databaseSizeBeforeCreate + 1);
        DogPhoto testDogPhoto = dogPhotos.get(dogPhotos.size() - 1);
        assertThat(testDogPhoto.getUrl()).isEqualTo(DEFAULT_URL);
        assertThat(testDogPhoto.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
    }

    @Test
    @Transactional
    public void checkUrlIsRequired() throws Exception {
        int databaseSizeBeforeTest = dogPhotoRepository.findAll().size();
        // set the field null
        dogPhoto.setUrl(null);

        // Create the DogPhoto, which fails.

        restDogPhotoMockMvc.perform(post("/api/dog-photos")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(dogPhoto)))
                .andExpect(status().isBadRequest());

        List<DogPhoto> dogPhotos = dogPhotoRepository.findAll();
        assertThat(dogPhotos).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllDogPhotos() throws Exception {
        // Initialize the database
        dogPhotoRepository.saveAndFlush(dogPhoto);

        // Get all the dogPhotos
        restDogPhotoMockMvc.perform(get("/api/dog-photos?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(dogPhoto.getId().intValue())))
                .andExpect(jsonPath("$.[*].url").value(hasItem(DEFAULT_URL.toString())))
                .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())));
    }

    @Test
    @Transactional
    public void getDogPhoto() throws Exception {
        // Initialize the database
        dogPhotoRepository.saveAndFlush(dogPhoto);

        // Get the dogPhoto
        restDogPhotoMockMvc.perform(get("/api/dog-photos/{id}", dogPhoto.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(dogPhoto.getId().intValue()))
            .andExpect(jsonPath("$.url").value(DEFAULT_URL.toString()))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingDogPhoto() throws Exception {
        // Get the dogPhoto
        restDogPhotoMockMvc.perform(get("/api/dog-photos/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateDogPhoto() throws Exception {
        // Initialize the database
        dogPhotoService.save(dogPhoto);

        int databaseSizeBeforeUpdate = dogPhotoRepository.findAll().size();

        // Update the dogPhoto
        DogPhoto updatedDogPhoto = new DogPhoto();
        updatedDogPhoto.setId(dogPhoto.getId());
        updatedDogPhoto.setUrl(UPDATED_URL);
        updatedDogPhoto.setDescription(UPDATED_DESCRIPTION);

        restDogPhotoMockMvc.perform(put("/api/dog-photos")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(updatedDogPhoto)))
                .andExpect(status().isOk());

        // Validate the DogPhoto in the database
        List<DogPhoto> dogPhotos = dogPhotoRepository.findAll();
        assertThat(dogPhotos).hasSize(databaseSizeBeforeUpdate);
        DogPhoto testDogPhoto = dogPhotos.get(dogPhotos.size() - 1);
        assertThat(testDogPhoto.getUrl()).isEqualTo(UPDATED_URL);
        assertThat(testDogPhoto.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    public void deleteDogPhoto() throws Exception {
        // Initialize the database
        dogPhotoService.save(dogPhoto);

        int databaseSizeBeforeDelete = dogPhotoRepository.findAll().size();

        // Get the dogPhoto
        restDogPhotoMockMvc.perform(delete("/api/dog-photos/{id}", dogPhoto.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<DogPhoto> dogPhotos = dogPhotoRepository.findAll();
        assertThat(dogPhotos).hasSize(databaseSizeBeforeDelete - 1);
    }
}
