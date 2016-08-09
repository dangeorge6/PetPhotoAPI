package com.dannyleavitt.app.service;

import com.dannyleavitt.app.domain.Client;
import com.dannyleavitt.app.repository.ClientRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.util.List;

/**
 * Service Implementation for managing Client.
 */
@Service
@Transactional
public class ClientService {

    private final Logger log = LoggerFactory.getLogger(ClientService.class);
    
    @Inject
    private ClientRepository clientRepository;
    
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
