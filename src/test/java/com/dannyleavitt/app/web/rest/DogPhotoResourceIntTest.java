package com.dannyleavitt.app.web.rest;

import com.dannyleavitt.app.ApiChallengeApp;
import com.dannyleavitt.app.domain.Breed;
import com.dannyleavitt.app.domain.Client;
import com.dannyleavitt.app.domain.Dog;
import com.dannyleavitt.app.domain.DogPhoto;
import com.dannyleavitt.app.domain.Vote;
import com.dannyleavitt.app.repository.BreedRepository;
import com.dannyleavitt.app.repository.ClientRepository;
import com.dannyleavitt.app.repository.DogPhotoRepository;
import com.dannyleavitt.app.service.DogPhotoService;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.hasSize;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.IntegrationTest;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.core.io.Resource;
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

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
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
    private BreedRepository bRepo;
    
    @Inject
    private ClientRepository cRepo;

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

    
    private void initRelationshipData() {   
            
    		//some test data for testing APIs that retrieve by breed or groups of breeds
	    	Client c1 = new Client();
	        Client c2 = new Client();
	        c1.setUsername("client1");
	        c2.setUsername("client2");
	        cRepo.save(c1);
	        cRepo.save(c2);
    	
            Breed b = new Breed();
            b.setName("breed1");
            Breed b2 = new Breed();
            b2.setName("breed2");
            
            
            Dog d1 = new Dog();
            d1.setBreed(b);
            Dog d2 = new Dog();
            d2.setBreed(b2);
           
            d1.setName("name1");
            d2.setName("name2");
            
            DogPhoto dp = new DogPhoto();
            dp.setUrl("dp1");
            dp.setDog(d1);
            d1.getDogPhotos().add(dp);
            Vote v1 = new Vote(); Vote v2 = new Vote();
     	    v1.setClient(c1); v1.setDogPhoto(dp); v1.setUpOrDown(1);
     	    v2.setClient(c2); v2.setDogPhoto(dp); v2.setUpOrDown(1);
     	    dp.getVotes().add(v1);
     	    dp.getVotes().add(v2);
     	   
            
            DogPhoto dp2 = new DogPhoto();
            dp2.setUrl("dp2");
            dp2.setDog(d1);
            d1.getDogPhotos().add(dp2);
            Vote v3 = new Vote(); Vote v4 = new Vote();
     	    v3.setClient(c1); v3.setDogPhoto(dp); v3.setUpOrDown(1);
     	    v4.setClient(c2); v4.setDogPhoto(dp); v4.setUpOrDown(1);
     	    dp2.getVotes().add(v3);
     	    dp2.getVotes().add(v4);
     	   
            
            DogPhoto dp3 = new DogPhoto();
            dp3.setUrl("dp3");
            dp3.setDog(d2);
            d2.getDogPhotos().add(dp3);
            Vote v5 = new Vote(); Vote v6 = new Vote();
     	    v5.setClient(c1); v5.setDogPhoto(dp); v5.setUpOrDown(-1);
     	    v6.setClient(c2); v6.setDogPhoto(dp); v6.setUpOrDown(-1);
     	    dp3.getVotes().add(v5);
     	    dp3.getVotes().add(v6);
            
            DogPhoto dp4 = new DogPhoto();
            dp4.setUrl("dp4");
            dp4.setDog(d2);
            d2.getDogPhotos().add(dp4);
            Vote v7 = new Vote(); Vote v8 = new Vote();
     	    v7.setClient(c1); v7.setDogPhoto(dp); v7.setUpOrDown(-1);
     	    v8.setClient(c2); v8.setDogPhoto(dp); v8.setUpOrDown(-1);
     	    dp4.getVotes().add(v3);
     	    dp4.getVotes().add(v4);
            
     	    b.getDogs().add(d1);
            b.getDogs().add(d2);
            bRepo.save(b);
            bRepo.save(b2);  
    }

    
    @Test
    @Transactional
    public void getAllDogPhotosOfBreedShouldDisplayDogsInBreed() throws Exception {
        // Initialize the database with data
    	initRelationshipData();
        
        // Get all the dogPhotos
        restDogPhotoMockMvc.perform(get("/api/dog-photos-of-breed/breed1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].breed_name").value(hasItem("breed1")))
                .andExpect(jsonPath("$.*", hasSize(2)));
    }
    
    
    @Test
    @Transactional
    public void getDogPhotosGroupedByBreedShouldDisplayBreedsMappedToListsOfPhotos() throws Exception {
        // Initialize the database with data
    	initRelationshipData();
        
        // Make sure both breeds made it in and correct number of photos returned for each breed
        restDogPhotoMockMvc.perform(get("/api/dog-photos-grouped-by-breed"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.breed1[0].url").value("dp1"))
                .andExpect(jsonPath("$.breed2[0].url").value("dp3"))
                .andExpect(jsonPath("$.breed1.[*]", hasSize(2)))
        		.andExpect(jsonPath("$.breed2.[*]", hasSize(2)));
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
