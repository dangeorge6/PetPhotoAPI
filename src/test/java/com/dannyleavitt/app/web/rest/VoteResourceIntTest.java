package com.dannyleavitt.app.web.rest;

import com.dannyleavitt.app.ApiChallengeApp;
import com.dannyleavitt.app.domain.Vote;
import com.dannyleavitt.app.repository.VoteRepository;
import com.dannyleavitt.app.service.VoteService;

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
 * Test class for the VoteResource REST controller.
 *
 * @see VoteResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = ApiChallengeApp.class)
@WebAppConfiguration
@IntegrationTest
public class VoteResourceIntTest {


    private static final Integer DEFAULT_UP_OR_DOWN = 1;
    private static final Integer UPDATED_UP_OR_DOWN = 2;

    @Inject
    private VoteRepository voteRepository;

    @Inject
    private VoteService voteService;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restVoteMockMvc;

    private Vote vote;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        VoteResource voteResource = new VoteResource();
        ReflectionTestUtils.setField(voteResource, "voteService", voteService);
        this.restVoteMockMvc = MockMvcBuilders.standaloneSetup(voteResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        vote = new Vote();
        vote.setUpOrDown(DEFAULT_UP_OR_DOWN);
    }

    @Test
    @Transactional
    public void createVote() throws Exception {
        int databaseSizeBeforeCreate = voteRepository.findAll().size();

        // Create the Vote

        restVoteMockMvc.perform(post("/api/votes")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(vote)))
                .andExpect(status().isCreated());

        // Validate the Vote in the database
        List<Vote> votes = voteRepository.findAll();
        assertThat(votes).hasSize(databaseSizeBeforeCreate + 1);
        Vote testVote = votes.get(votes.size() - 1);
        assertThat(testVote.getUpOrDown()).isEqualTo(DEFAULT_UP_OR_DOWN);
    }

    @Test
    @Transactional
    public void getAllVotes() throws Exception {
        // Initialize the database
        voteRepository.saveAndFlush(vote);

        // Get all the votes
        restVoteMockMvc.perform(get("/api/votes?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(vote.getId().intValue())))
                .andExpect(jsonPath("$.[*].upOrDown").value(hasItem(DEFAULT_UP_OR_DOWN)));
    }

    @Test
    @Transactional
    public void getVote() throws Exception {
        // Initialize the database
        voteRepository.saveAndFlush(vote);

        // Get the vote
        restVoteMockMvc.perform(get("/api/votes/{id}", vote.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(vote.getId().intValue()))
            .andExpect(jsonPath("$.upOrDown").value(DEFAULT_UP_OR_DOWN));
    }

    @Test
    @Transactional
    public void getNonExistingVote() throws Exception {
        // Get the vote
        restVoteMockMvc.perform(get("/api/votes/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateVote() throws Exception {
        // Initialize the database
        voteService.save(vote);

        int databaseSizeBeforeUpdate = voteRepository.findAll().size();

        // Update the vote
        Vote updatedVote = new Vote();
        updatedVote.setId(vote.getId());
        updatedVote.setUpOrDown(UPDATED_UP_OR_DOWN);

        restVoteMockMvc.perform(put("/api/votes")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(updatedVote)))
                .andExpect(status().isOk());

        // Validate the Vote in the database
        List<Vote> votes = voteRepository.findAll();
        assertThat(votes).hasSize(databaseSizeBeforeUpdate);
        Vote testVote = votes.get(votes.size() - 1);
        assertThat(testVote.getUpOrDown()).isEqualTo(UPDATED_UP_OR_DOWN);
    }

    @Test
    @Transactional
    public void deleteVote() throws Exception {
        // Initialize the database
        voteService.save(vote);

        int databaseSizeBeforeDelete = voteRepository.findAll().size();

        // Get the vote
        restVoteMockMvc.perform(delete("/api/votes/{id}", vote.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<Vote> votes = voteRepository.findAll();
        assertThat(votes).hasSize(databaseSizeBeforeDelete - 1);
    }
}
