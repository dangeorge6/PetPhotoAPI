package com.dannyleavitt.app.repository;

import com.dannyleavitt.app.domain.DogPhoto;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the DogPhoto entity.
 */
@SuppressWarnings("unused")
public interface DogPhotoRepository extends JpaRepository<DogPhoto,Long> {
	//group by breed
//	@Query("select s from DogPhoto s JOIN Dog d ON d.id = s.id JOIN Breed b ON b.id = d.id  GROUP BY b.name")
//    List<DogPhoto> findAllByBreed(String author, String title);
	
	//find all for a particular breed
//	@Query("select s.id, SUM(v.upOrDown) as numVotes "
//			+" from DogPhoto s "
//			+" JOIN s.dogs d"
//			+" JOIN d.breeds b"
//			+" LEFT JOIN s.votes v"
//			+" where b.name = ?1"
//			+" Group by s.id"
//			+" ORDER BY numVotes"
//			)
//    List<DogPhoto> findAllOfBreed(String breedName);
}





