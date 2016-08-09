package com.dannyleavitt.app.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.dannyleavitt.app.domain.Dog;
import com.dannyleavitt.app.service.DogService;
import com.dannyleavitt.app.web.rest.util.HeaderUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;
import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing Dog.
 */
@RestController
@RequestMapping("/api")
public class DogResource {

    private final Logger log = LoggerFactory.getLogger(DogResource.class);
        
    @Inject
    private DogService dogService;
    
    /**
     * POST  /dogs : Create a new dog.
     *
     * @param dog the dog to create
     * @return the ResponseEntity with status 201 (Created) and with body the new dog, or with status 400 (Bad Request) if the dog has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/dogs",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Dog> createDog(@Valid @RequestBody Dog dog) throws URISyntaxException {
        log.debug("REST request to save Dog : {}", dog);
        if (dog.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("dog", "idexists", "A new dog cannot already have an ID")).body(null);
        }
        Dog result = dogService.save(dog);
        return ResponseEntity.created(new URI("/api/dogs/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("dog", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /dogs : Updates an existing dog.
     *
     * @param dog the dog to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated dog,
     * or with status 400 (Bad Request) if the dog is not valid,
     * or with status 500 (Internal Server Error) if the dog couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/dogs",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Dog> updateDog(@Valid @RequestBody Dog dog) throws URISyntaxException {
        log.debug("REST request to update Dog : {}", dog);
        if (dog.getId() == null) {
            return createDog(dog);
        }
        Dog result = dogService.save(dog);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("dog", dog.getId().toString()))
            .body(result);
    }

    /**
     * GET  /dogs : get all the dogs.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of dogs in body
     */
    @RequestMapping(value = "/dogs",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<Dog> getAllDogs() {
        log.debug("REST request to get all Dogs");
        return dogService.findAll();
    }

    /**
     * GET  /dogs/:id : get the "id" dog.
     *
     * @param id the id of the dog to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the dog, or with status 404 (Not Found)
     */
    @RequestMapping(value = "/dogs/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Dog> getDog(@PathVariable Long id) {
        log.debug("REST request to get Dog : {}", id);
        Dog dog = dogService.findOne(id);
        return Optional.ofNullable(dog)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /dogs/:id : delete the "id" dog.
     *
     * @param id the id of the dog to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @RequestMapping(value = "/dogs/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteDog(@PathVariable Long id) {
        log.debug("REST request to delete Dog : {}", id);
        dogService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("dog", id.toString())).build();
    }

}
