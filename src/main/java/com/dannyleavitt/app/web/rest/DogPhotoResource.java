package com.dannyleavitt.app.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.dannyleavitt.app.domain.DogPhoto;
import com.dannyleavitt.app.service.DogPhotoService;
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
 * REST controller for managing DogPhoto.
 */
@RestController
@RequestMapping("/api")
public class DogPhotoResource {

    private final Logger log = LoggerFactory.getLogger(DogPhotoResource.class);
        
    @Inject
    private DogPhotoService dogPhotoService;
    
    /**
     * POST  /dog-photos : Create a new dogPhoto.
     *
     * @param dogPhoto the dogPhoto to create
     * @return the ResponseEntity with status 201 (Created) and with body the new dogPhoto, or with status 400 (Bad Request) if the dogPhoto has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/dog-photos",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<DogPhoto> createDogPhoto(@Valid @RequestBody DogPhoto dogPhoto) throws URISyntaxException {
        log.debug("REST request to save DogPhoto : {}", dogPhoto);
        if (dogPhoto.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("dogPhoto", "idexists", "A new dogPhoto cannot already have an ID")).body(null);
        }
        DogPhoto result = dogPhotoService.save(dogPhoto);
        return ResponseEntity.created(new URI("/api/dog-photos/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("dogPhoto", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /dog-photos : Updates an existing dogPhoto.
     *
     * @param dogPhoto the dogPhoto to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated dogPhoto,
     * or with status 400 (Bad Request) if the dogPhoto is not valid,
     * or with status 500 (Internal Server Error) if the dogPhoto couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/dog-photos",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<DogPhoto> updateDogPhoto(@Valid @RequestBody DogPhoto dogPhoto) throws URISyntaxException {
        log.debug("REST request to update DogPhoto : {}", dogPhoto);
        if (dogPhoto.getId() == null) {
            return createDogPhoto(dogPhoto);
        }
        DogPhoto result = dogPhotoService.save(dogPhoto);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("dogPhoto", dogPhoto.getId().toString()))
            .body(result);
    }

    /**
     * GET  /dog-photos : get all the dogPhotos.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of dogPhotos in body
     */
    @RequestMapping(value = "/dog-photos",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<DogPhoto> getAllDogPhotos() {
        log.debug("REST request to get all DogPhotos");
        return dogPhotoService.findAll();
    }

    /**
     * GET  /dog-photos/:id : get the "id" dogPhoto.
     *
     * @param id the id of the dogPhoto to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the dogPhoto, or with status 404 (Not Found)
     */
    @RequestMapping(value = "/dog-photos/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<DogPhoto> getDogPhoto(@PathVariable Long id) {
        log.debug("REST request to get DogPhoto : {}", id);
        DogPhoto dogPhoto = dogPhotoService.findOne(id);
        return Optional.ofNullable(dogPhoto)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /dog-photos/:id : delete the "id" dogPhoto.
     *
     * @param id the id of the dogPhoto to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @RequestMapping(value = "/dog-photos/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteDogPhoto(@PathVariable Long id) {
        log.debug("REST request to delete DogPhoto : {}", id);
        dogPhotoService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("dogPhoto", id.toString())).build();
    }

}
