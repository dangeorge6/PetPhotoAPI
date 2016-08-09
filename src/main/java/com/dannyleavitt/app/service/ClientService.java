package com.dannyleavitt.app.service;

import com.dannyleavitt.app.domain.Client;
import com.dannyleavitt.app.domain.DogPhoto;
import com.dannyleavitt.app.domain.Vote;
import com.dannyleavitt.app.repository.ClientRepository;
import com.dannyleavitt.app.repository.DogPhotoRepository;
import com.dannyleavitt.app.repository.VoteRepository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.util.List;
import java.util.Set;

/**
 * Service Implementation for managing Client.
 */
@Service
@Transactional
public class ClientService {

    private final Logger log = LoggerFactory.getLogger(ClientService.class);
    
    @Inject
    private ClientRepository clientRepository;
    @Inject
    private DogPhotoRepository dpRepo;
    @Inject
    private VoteRepository vRepo;
    
    public Client upVotePhoto(Long clientId, Long dogPhotoId) {
    	Vote v = null;
		Client client = clientRepository.findOne(clientId);
		if(client != null){
			DogPhoto dp = dpRepo.findOne(dogPhotoId);
			if(dp != null){
				List<Vote> clientsVote = client.getVotes();
				
				if(clientsVote.size()>0){
					//there is already a vote 
					v = clientsVote.get(0);
					v.setUpOrDown(1);
				} else {
					v.setUpOrDown(1);
					v.setClient(client);
					v.setDogPhoto(dp);
				}
				vRepo.save(v);
			}
		}
		return client;
	}
    
    public Client downVotePhoto(Long clientId, Long dogPhotoId) {
    	Vote v = null;
		Client client = clientRepository.findOne(clientId);
		if(client != null){
			DogPhoto dp = dpRepo.findOne(dogPhotoId);
			if(dp != null){
				List<Vote> clientsVote = client.getVotes();
				
				if(clientsVote.size()>0){
					//there is already a vote 
					v = clientsVote.get(0);
					v.setUpOrDown(-1);
				} else {
					v.setUpOrDown(-1);
					v.setClient(client);
					v.setDogPhoto(dp);
				}
				vRepo.save(v);
			}
		}
		return client;
	}
    
      
    
    
    
    /**
     * Save a client.
     * 
     * @param client the entity to save
     * @return the persisted entity
     */
    public Client save(Client client) {
        log.debug("Request to save Client : {}", client);
        Client result = clientRepository.save(client);
        return result;
    }

    /**
     *  Get all the clients.
     *  
     *  @return the list of entities
     */
    @Transactional(readOnly = true) 
    public List<Client> findAll() {
        log.debug("Request to get all Clients");
        List<Client> result = clientRepository.findAll();
        return result;
    }

    /**
     *  Get one client by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Transactional(readOnly = true) 
    public Client findOne(Long id) {
        log.debug("Request to get Client : {}", id);
        Client client = clientRepository.findOne(id);
        return client;
    }

    /**
     *  Delete the  client by id.
     *  
     *  @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete Client : {}", id);
        clientRepository.delete(id);
    }

	
}
