package rest;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import dto.PersonDTO;
import dto.PersonsDTO;
import entities.Address;
import entities.Person;
import entities.Phone;
import exceptions.MissingInputException;
import exceptions.NotFoundException;
import facades.PersonFacade;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManagerFactory;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
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
        String json = GSON.toJson(pDTO);
        return json;
        //return GSON.toJson(pDTO);
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
    
    @Path("/zip/{number}")
    @GET
    @Produces({MediaType.APPLICATION_JSON})
    public String lol3(@PathParam("number") int number) throws NotFoundException, MissingInputException {
        PersonsDTO p = FACADE.getAllPersonsInZip(number);
        return GSON.toJson(p);
    }
    
    @Path("/address/{street}")
    @GET
    @Produces({MediaType.APPLICATION_JSON})
    public String lol4(@PathParam("street") String street) throws NotFoundException, MissingInputException {
        PersonsDTO p = FACADE.getPersonByAdd(street);
        return GSON.toJson(p);
    }
    
    @Path("{id}")
    @PUT
    @Produces({MediaType.APPLICATION_JSON})
    @Consumes({MediaType.APPLICATION_JSON})
    public String editPerson(@PathParam("id")long id, String pers) throws NotFoundException, MissingInputException {
        Person person = GSON.fromJson(pers, Person.class);
        person.setId(id);
        PersonDTO pEdit = FACADE.editPerson(person);
        return GSON.toJson(pEdit);
    }
}
