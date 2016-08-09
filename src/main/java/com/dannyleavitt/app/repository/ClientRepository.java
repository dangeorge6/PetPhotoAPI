package com.dannyleavitt.app.repository;

import com.dannyleavitt.app.domain.Client;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Client entity.
 */
@SuppressWarnings("unused")
public interface ClientRepository extends JpaRepository<Client,Long> {

}
