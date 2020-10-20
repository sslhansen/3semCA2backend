package rest;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import dto.PersonDTO;
import dto.PersonsDTO;
import entities.Person;
import entities.Phone;
import exceptions.MissingInputException;
import exceptions.NotFoundException;
import facades.PersonFacade;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManagerFactory;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import utils.EMF_Creator;

@Path("person")
public class PersonResource {

    private static final EntityManagerFactory EMF = EMF_Creator.createEntityManagerFactory();
    private static final PersonFacade FACADE = PersonFacade.getFacadeExample(EMF);
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();

    @Path("all")
    @GET
    @Produces({MediaType.APPLICATION_JSON})
    public String getAllPersons() {
        PersonsDTO pDTO = FACADE.getAllPersons();
        return GSON.toJson(pDTO);
    }

    @Path("lol")
    @GET
    @Produces({MediaType.APPLICATION_JSON})
    public String lol() {
        FACADE.createTestPerson();
        return GSON.toJson("FIX ME");
    }

    @Path("{number}")
    @GET
    @Produces({MediaType.APPLICATION_JSON})
    public String lol2(@PathParam("number") int number) throws NotFoundException, MissingInputException {
        PersonDTO p = FACADE.getPersonByTel(number);
        return GSON.toJson(p);
    }

}
