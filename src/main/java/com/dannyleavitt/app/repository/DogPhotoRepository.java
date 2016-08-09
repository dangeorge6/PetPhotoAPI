package com.dannyleavitt.app.repository;

import com.dannyleavitt.app.domain.DogPhoto;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the DogPhoto entity.
 */
@SuppressWarnings("unused")
public interface DogPhotoRepository extends JpaRepository<DogPhoto,Long> {

}
