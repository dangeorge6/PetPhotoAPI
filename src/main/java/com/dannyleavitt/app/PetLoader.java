package com.dannyleavitt.app;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

import com.dannyleavitt.app.domain.Breed;
import com.dannyleavitt.app.domain.Client;
import com.dannyleavitt.app.repository.BreedRepository;
import com.dannyleavitt.app.repository.ClientRepository;
import com.dannyleavitt.app.domain.Dog;
import com.dannyleavitt.app.domain.DogPhoto;
import com.dannyleavitt.app.domain.Vote;

import javax.sql.DataSource;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Random;

/**
 * Loads stored objects from the file system and builds up
 * the appropriate objects to add to the data source.
 *
 * Created by fredjean on 9/21/15.
 */
@Component
public class PetLoader implements InitializingBean {
    // Resources to the different files we need to load.
    @Value("classpath:data/labrador.txt")
    private Resource labradors;

    @Value("classpath:data/pug.txt")
    private Resource pugs;

    @Value("classpath:data/retriever.txt")
    private Resource retrievers;

    @Value("classpath:data/yorkie.txt")
    private Resource yorkies;

    @Autowired
    DataSource dataSource;
    
    @Autowired
    BreedRepository bRepo;
    
    @Autowired
    ClientRepository cRepo;

    /**
     * Load the different breeds into the data source after
     * the application is ready.
     *
     * @throws Exception In case something goes wrong while we load the breeds.
     */
    @Override
    public void afterPropertiesSet() throws Exception {
    	 Client c1 = new Client();
         Client c2 = new Client();
         c1.setUsername("Victor");
         c2.setUsername("Danny");
         cRepo.save(c1);
         cRepo.save(c2);
    	
    	loadBreed("Labrador", labradors,c1,c2);
        loadBreed("Pug", pugs,c1,c2);
        loadBreed("Retriever", retrievers,c1,c2);
        loadBreed("Yorkie", yorkies,c1,c2);
    }

    /**
     * Reads the list of dogs in a category and (eventually) add
     * them to the data source.
     * @param breed The breed that we are loading.
     * @param source The file holding the breeds.
     * @throws IOException In case things go horribly, horribly wrong.
     */
    private void loadBreed(String breed, Resource source, Client c1, Client c2) throws IOException {
        try ( BufferedReader br = new BufferedReader(new InputStreamReader(source.getInputStream()))) {
            String line;
           
            
            
            Breed b = new Breed();
            b.setName(breed);
            Dog d1 = new Dog();
            d1.setBreed(b);
            Dog d2 = new Dog();
            d2.setBreed(b);
            String n1 = "";
            String n2 = "";
            switch(breed){
            case "Labrador" :   
            	n1 = "Fido";
            	n2 = "Rufus";
               break; 
            case "Pug" :
            	n1 = "Yippy";
            	n2 = "Yoppy";       
               break;
            case "Retriever" :
            	n1 = "Spot";
            	n2 = "Joe";            
                break; 
            case "Yorkie" :
            	n1 = "Pepper";
            	n2 = "Salt";  
                break; 
            default : 
        }
            d1.setName(n1); d1.setYears_old(12);
            d2.setName(n2); d2.setYears_old(1);
            int i = 0;
            while ((line = br.readLine()) != null) {
            	DogPhoto dp = new DogPhoto();
            	
            	dp.setUrl(line);
            	
            	//get a random int between -1 and 1 for vote
         	   Random random = new Random();
        	   int min = -1;
        	   int max = 1;
        	   int randomNumber = random.nextInt(max - min) + min;
            	
               if(i<3){
            	   //put 3 in dog 1
            	   dp.setDog(d1);
            	   d1.getDogPhotos().add(dp);
            	   
            	   dp.setDescription("sweet photo of " + d1.getName());
            	   //generate a random vote
            
            	   Vote v = new Vote(); Vote v2 = new Vote();
            	   v.setClient(c1); v.setDogPhoto(dp); v.setUpOrDown(1);
            	   v2.setClient(c2); v2.setDogPhoto(dp); v2.setUpOrDown(randomNumber);
            	   dp.getVotes().add(v);
            	   dp.getVotes().add(v2);
               } else {
            	   //put in dog 2
            	   dp.setDog(d2);
            	   d2.getDogPhotos().add(dp);
            	   
            	   dp.setDescription("sweet photo of " + d2.getName());
            	   Vote v3 = new Vote(); Vote v4 = new Vote();
            	   v3.setClient(c1); v3.setDogPhoto(dp); v3.setUpOrDown(1);
            	   v4.setClient(c2); v4.setDogPhoto(dp); v4.setUpOrDown(randomNumber);
            	   dp.getVotes().add(v3);
            	   dp.getVotes().add(v4);
               }
                
                
                
              i++;  	
            }
            
            b.getDogs().add(d1);
            b.getDogs().add(d2);
            bRepo.save(b);
            
        }
    }
}
