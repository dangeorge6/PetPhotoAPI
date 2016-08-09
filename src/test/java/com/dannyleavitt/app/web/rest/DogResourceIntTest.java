package com.dannyleavitt.app.web.rest;

import com.dannyleavitt.app.ApiChallengeApp;
import com.dannyleavitt.app.domain.Dog;
import com.dannyleavitt.app.repository.DogRepository;
import com.dannyleavitt.app.service.DogService;

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
 * Test class for the DogResource REST controller.
 *
 * @see DogResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = ApiChallengeApp.class)
@WebAppConfiguration
@IntegrationTest
public class DogResourceIntTest {

    private static final String DEFAULT_NAME = "AAAAA";
    private static final String UPDATED_NAME = "BBBBB";

    private static final Integer DEFAULT_YEARS_OLD = 1;
    private static final Integer UPDATED_YEARS_OLD = 2;

    @Inject
    private DogRepository dogRepository;

    @Inject
    private DogService dogService;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restDogMockMvc;

    private Dog dog;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        DogResource dogResource = new DogResource();
        ReflectionTestUtils.setField(dogResource, "dogService", dogService);
        this.restDogMockMvc = MockMvcBuilders.standaloneSetup(dogResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        dog = new Dog();
        dog.setName(DEFAULT_NAME);
        dog.setYears_old(DEFAULT_YEARS_OLD);
    }

    @Test
    @Transactional
    public void createDog() throws Exception {
        int databaseSizeBeforeCreate = dogRepository.findAll().size();

        // Create the Dog

        restDogMockMvc.perform(post("/api/dogs")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(dog)))
                .andExpect(status().isCreated());

        // Validate the Dog in the database
        List<Dog> dogs = dogRepository.findAll();
        assertThat(dogs).hasSize(databaseSizeBeforeCreate + 1);
        Dog testDog = dogs.get(dogs.size() - 1);
        assertThat(testDog.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testDog.getYears_old()).isEqualTo(DEFAULT_YEARS_OLD);
    }

    @Test
    @Transactional
    public void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = dogRepository.findAll().size();
        // set the field null
        dog.setName(null);

        // Create the Dog, which fails.

        restDogMockMvc.perform(post("/api/dogs")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(dog)))
                .andExpect(status().isBadRequest());

        List<Dog> dogs = dogRepository.findAll();
        assertThat(dogs).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllDogs() throws Exception {
        // Initialize the database
        dogRepository.saveAndFlush(dog);

        // Get all the dogs
        restDogMockMvc.perform(get("/api/dogs?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(dog.getId().intValue())))
                .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
                .andExpect(jsonPath("$.[*].years_old").value(hasItem(DEFAULT_YEARS_OLD)));
    }

    @Test
    @Transactional
    public void getDog() throws Exception {
        // Initialize the database
        dogRepository.saveAndFlush(dog);

        // Get the dog
        restDogMockMvc.perform(get("/api/dogs/{id}", dog.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(dog.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()))
            .andExpect(jsonPath("$.years_old").value(DEFAULT_YEARS_OLD));
    }

    @Test
    @Transactional
    public void getNonExistingDog() throws Exception {
        // Get the dog
        restDogMockMvc.perform(get("/api/dogs/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateDog() throws Exception {
        // Initialize the database
        dogService.save(dog);

        int databaseSizeBeforeUpdate = dogRepository.findAll().size();

        // Update the dog
        Dog updatedDog = new Dog();
        updatedDog.setId(dog.getId());
        updatedDog.setName(UPDATED_NAME);
        updatedDog.setYears_old(UPDATED_YEARS_OLD);

        restDogMockMvc.perform(put("/api/dogs")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(updatedDog)))
                .andExpect(status().isOk());

        // Validate the Dog in the database
        List<Dog> dogs = dogRepository.findAll();
        assertThat(dogs).hasSize(databaseSizeBeforeUpdate);
        Dog testDog = dogs.get(dogs.size() - 1);
        assertThat(testDog.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testDog.getYears_old()).isEqualTo(UPDATED_YEARS_OLD);
    }

    @Test
    @Transactional
    public void deleteDog() throws Exception {
        // Initialize the database
        dogService.save(dog);

        int databaseSizeBeforeDelete = dogRepository.findAll().size();

        // Get the dog
        restDogMockMvc.perform(delete("/api/dogs/{id}", dog.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<Dog> dogs = dogRepository.findAll();
        assertThat(dogs).hasSize(databaseSizeBeforeDelete - 1);
    }
}
