package com.dannyleavitt.app.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.dannyleavitt.app.domain.Breed;
import com.dannyleavitt.app.service.BreedService;
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
 * REST controller for managing Breed.
 */
@RestController
@RequestMapping("/api")
public class BreedResource {

    private final Logger log = LoggerFactory.getLogger(BreedResource.class);
        
    @Inject
    private BreedService breedService;
    
    /**
     * POST  /breeds : Create a new breed.
     *
     * @param breed the breed to create
     * @return the ResponseEntity with status 201 (Created) and with body the new breed, or with status 400 (Bad Request) if the breed has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/breeds",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Breed> createBreed(@Valid @RequestBody Breed breed) throws URISyntaxException {
        log.debug("REST request to save Breed : {}", breed);
        if (breed.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("breed", "idexists", "A new breed cannot already have an ID")).body(null);
        }
        Breed result = breedService.save(breed);
        return ResponseEntity.created(new URI("/api/breeds/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("breed", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /breeds : Updates an existing breed.
     *
     * @param breed the breed to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated breed,
     * or with status 400 (Bad Request) if the breed is not valid,
     * or with status 500 (Internal Server Error) if the breed couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/breeds",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Breed> updateBreed(@Valid @RequestBody Breed breed) throws URISyntaxException {
        log.debug("REST request to update Breed : {}", breed);
        if (breed.getId() == null) {
            return createBreed(breed);
        }
        Breed result = breedService.save(breed);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("breed", breed.getId().toString()))
            .body(result);
    }

    /**
     * GET  /breeds : get all the breeds.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of breeds in body
     */
    @RequestMapping(value = "/breeds",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<Breed> getAllBreeds() {
        log.debug("REST request to get all Breeds");
        return breedService.findAll();
    }

    /**
     * GET  /breeds/:id : get the "id" breed.
     *
     * @param id the id of the breed to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the breed, or with status 404 (Not Found)
     */
    @RequestMapping(value = "/breeds/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Breed> getBreed(@PathVariable Long id) {
        log.debug("REST request to get Breed : {}", id);
        Breed breed = breedService.findOne(id);
        return Optional.ofNullable(breed)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /breeds/:id : delete the "id" breed.
     *
     * @param id the id of the breed to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @RequestMapping(value = "/breeds/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteBreed(@PathVariable Long id) {
        log.debug("REST request to delete Breed : {}", id);
        breedService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("breed", id.toString())).build();
    }

}
