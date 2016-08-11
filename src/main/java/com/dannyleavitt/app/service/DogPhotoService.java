package com.dannyleavitt.app.service;

import com.dannyleavitt.app.domain.Breed;
import com.dannyleavitt.app.domain.Dog;
import com.dannyleavitt.app.domain.DogPhoto;

import com.dannyleavitt.app.repository.DogPhotoRepository;
import com.dannyleavitt.app.repository.BreedRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.util.List;

/**
 * Service Implementation for managing DogPhoto.
 */
@Service
@Transactional
public class DogPhotoService {

    private final Logger log = LoggerFactory.getLogger(DogPhotoService.class);
    
    @Inject
    private DogPhotoRepository dogPhotoRepository;
    
    @Inject
    private BreedRepository bRepo;
    
    /**
     *  Get all the dogPhotos by Breed.
     *  
     *  @return the list of entities
     */
    @Transactional(readOnly = true) 
    public List<DogPhoto> findAllOfBreed(String breedName) {
        log.debug("Request to get all DogPhotos");
        List<DogPhoto> l = null;
        Breed b = bRepo.findByName(breedName);
        for(Dog d : b.getDogs()){
        	for(DogPhoto dp : d.getDogPhotos()){
        		l.add(dp);
        	}  	
        }
        
        //would have sorted by vote count if had time
        //l.sort();
        return l;
    }
    
    
    
    /**
     * Save a dogPhoto.
     * 
     * @param dogPhoto the entity to save
     * @return the persisted entity
     */
    public DogPhoto save(DogPhoto dogPhoto) {
        log.debug("Request to save DogPhoto : {}", dogPhoto);
        DogPhoto result = dogPhotoRepository.save(dogPhoto);
        return result;
    }

    /**
     *  Get all the dogPhotos.
     *  
     *  @return the list of entities
     */
    @Transactional(readOnly = true) 
    public List<DogPhoto> findAll() {
        log.debug("Request to get all DogPhotos");
        List<DogPhoto> result = dogPhotoRepository.findAll();
        return result;
    }

    /**
     *  Get one dogPhoto by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Transactional(readOnly = true) 
    public DogPhoto findOne(Long id) {
        log.debug("Request to get DogPhoto : {}", id);
        DogPhoto dogPhoto = dogPhotoRepository.findOne(id);
        return dogPhoto;
    }

    /**
     *  Delete the  dogPhoto by id.
     *  
     *  @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete DogPhoto : {}", id);
        dogPhotoRepository.delete(id);
    }
}
