package com.dannyleavitt.app.service;

import com.dannyleavitt.app.domain.Dog;
import com.dannyleavitt.app.repository.DogRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.util.List;

/**
 * Service Implementation for managing Dog.
 */
@Service
@Transactional
public class DogService {

    private final Logger log = LoggerFactory.getLogger(DogService.class);
    
    @Inject
    private DogRepository dogRepository;
    
    /**
     * Save a dog.
     * 
     * @param dog the entity to save
     * @return the persisted entity
     */
    public Dog save(Dog dog) {
        log.debug("Request to save Dog : {}", dog);
        Dog result = dogRepository.save(dog);
        return result;
    }

    /**
     *  Get all the dogs.
     *  
     *  @return the list of entities
     */
    @Transactional(readOnly = true) 
    public List<Dog> findAll() {
        log.debug("Request to get all Dogs");
        List<Dog> result = dogRepository.findAll();
        return result;
    }

    /**
     *  Get one dog by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Transactional(readOnly = true) 
    public Dog findOne(Long id) {
        log.debug("Request to get Dog : {}", id);
        Dog dog = dogRepository.findOne(id);
        return dog;
    }

    /**
     *  Delete the  dog by id.
     *  
     *  @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete Dog : {}", id);
        dogRepository.delete(id);
    }
}
