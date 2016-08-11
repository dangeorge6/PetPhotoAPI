package com.dannyleavitt.app.repository;

import com.dannyleavitt.app.domain.DogPhoto;

import org.springframework.data.jpa.repository.*;

import java.util.List;
import java.util.Map;

/**
 * Spring Data JPA repository for the DogPhoto entity.
 */
@SuppressWarnings("unused")
public interface DogPhotoRepository extends JpaRepository<DogPhoto,Long> {

//	Map<String,List<DogPhoto>>	
	//find all for a particular breed
		@Query("select new map(s.id as id,s.url as url,s.description as description,s.dog.name as dog_name,s.dog.years_old as years_old,s.dog.breed.name as breed_name, SUM(v.upOrDown) as voteTotal) "
				+" from DogPhoto s "
				+" LEFT JOIN s.votes v"
				+" Group by s.id"
				+" ORDER BY s.dog.breed.name,voteTotal DESC"
				)
		List<Map<String,String>> findAllOrderedByBreed();
	
	//find all for a particular breed
	@Query("select new map(s.id as id,s.url as url,s.description as description,s.dog.name as dog_name,s.dog.years_old as years_old,s.dog.breed.name as breed_name, SUM(v.upOrDown) as voteTotal) "
			+" from DogPhoto s "
			+" LEFT JOIN s.votes v"
			+" where LOWER(s.dog.breed.name) = LOWER(?1)"
			+" Group by s.id"
			+" ORDER BY voteTotal DESC"
			)
	List<Map<String,String>> findAllOfBreed(String breedName);
}





