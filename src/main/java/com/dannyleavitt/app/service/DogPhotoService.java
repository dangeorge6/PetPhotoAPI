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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
    public List findAllOfBreed(String breedName) {
        List l = dogPhotoRepository.findAllOfBreed(breedName);
//    	log.debug("Request to FindAllOfBreed Service");
//        List<DogPhoto> l = null;
//        Breed b = bRepo.findByName(breedName);
//        for(Dog d : b.getDogs()){
//        	for(DogPhoto dp : d.getDogPhotos()){
//        		l.add(dp);
//        	}  	
//        }
//        
//        //would have sorted by vote count if had time
//        //l.sort();
        return l;
    }
    
    
    /**
     *  Get all the dogPhotos by Breed.
     *  
     *  @return the list of entities
     */
    @Transactional(readOnly = true) 
    public Map<String,List<Map<String,String>>> findAllGroupedByBreed() {
    	Map<String,List<Map<String,String>>> dogObject = new HashMap();
    	List<Map<String,String>> l = dogPhotoRepository.findAllOrderedByBreed();
        if(l.size()>0){
        	//if rows returned
        	//initialize breedname
        	String breedName = l.get(0).get("breed_name");
        	dogObject.put(breedName, new ArrayList());
        	
        	for(Map<String,String> item : l){
        		//each row
        		if(!breedName.equals(item.get("breed_name"))){
        			//new breed, add it to the map, change the breed
        			breedName = item.get("breed_name");
        			dogObject.put(breedName, new ArrayList());	
        		}
        		//delete breedname from photo row since reduntant
        		item.remove("breed_name");
        		//add photo row to that breed
        		dogObject.get(breedName).add(item);			
        	}
        }
        return dogObject;
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
