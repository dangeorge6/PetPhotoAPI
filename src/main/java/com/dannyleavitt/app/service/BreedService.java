package com.dannyleavitt.app.service;

import com.dannyleavitt.app.domain.Breed;
import com.dannyleavitt.app.repository.BreedRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.util.List;

/**
 * Service Implementation for managing Breed.
 */
@Service
@Transactional
public class BreedService {

    private final Logger log = LoggerFactory.getLogger(BreedService.class);
    
    @Inject
    private BreedRepository breedRepository;
    
    /**
     * Save a breed.
     * 
     * @param breed the entity to save
     * @return the persisted entity
     */
    public Breed save(Breed breed) {
        log.debug("Request to save Breed : {}", breed);
        Breed result = breedRepository.save(breed);
        return result;
    }

    /**
     *  Get all the breeds.
     *  
     *  @return the list of entities
     */
    @Transactional(readOnly = true) 
    public List<Breed> findAll() {
        log.debug("Request to get all Breeds");
        List<Breed> result = breedRepository.findAll();
        return result;
    }

    /**
     *  Get one breed by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Transactional(readOnly = true) 
    public Breed findOne(Long id) {
        log.debug("Request to get Breed : {}", id);
        Breed breed = breedRepository.findOne(id);
        return breed;
    }

    /**
     *  Delete the  breed by id.
     *  
     *  @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete Breed : {}", id);
        breedRepository.delete(id);
    }
}
