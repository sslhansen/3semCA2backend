package rest;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import facades.PersonFacade;
import javax.persistence.EntityManagerFactory;
import javax.ws.rs.Path;
import utils.EMF_Creator;

@Path("person")
public class PersonResource {
    private static final EntityManagerFactory EMF = EMF_Creator.createEntityManagerFactory();
    private static final PersonFacade FACADE =  PersonFacade.getFacadeExample(EMF);
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    
}
