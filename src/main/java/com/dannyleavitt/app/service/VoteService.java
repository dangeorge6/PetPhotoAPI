package com.dannyleavitt.app.service;

import com.dannyleavitt.app.domain.Vote;
import com.dannyleavitt.app.repository.VoteRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.util.List;

/**
 * Service Implementation for managing Vote.
 */
@Service
@Transactional
public class VoteService {

    private final Logger log = LoggerFactory.getLogger(VoteService.class);
    
    @Inject
    private VoteRepository voteRepository;
    
    /**
     * Save a vote.
     * 
     * @param vote the entity to save
     * @return the persisted entity
     */
    public Vote save(Vote vote) {
        log.debug("Request to save Vote : {}", vote);
        Vote result = voteRepository.save(vote);
        return result;
    }

    /**
     *  Get all the votes.
     *  
     *  @return the list of entities
     */
    @Transactional(readOnly = true) 
    public List<Vote> findAll() {
        log.debug("Request to get all Votes");
        List<Vote> result = voteRepository.findAll();
        return result;
    }

    /**
     *  Get one vote by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Transactional(readOnly = true) 
    public Vote findOne(Long id) {
        log.debug("Request to get Vote : {}", id);
        Vote vote = voteRepository.findOne(id);
        return vote;
    }

    /**
     *  Delete the  vote by id.
     *  
     *  @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete Vote : {}", id);
        voteRepository.delete(id);
    }
}
