package com.dannyleavitt.app.repository;

import com.dannyleavitt.app.domain.Breed;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Breed entity.
 */
@SuppressWarnings("unused")
public interface BreedRepository extends JpaRepository<Breed,Long> {
	Breed findByName(String breedName);
}
