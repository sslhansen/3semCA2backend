package rest;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import dto.AddressDTO;
import dto.HobbyDTO;
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
import javax.ws.rs.POST;
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
    
    @GET
    @Produces({MediaType.APPLICATION_JSON})
    public String demo() {
        return "{\"msg\":\"Hello World\"}";
    }

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
    public String getPersonByTel(@PathParam("number") int number) throws NotFoundException, MissingInputException {
        PersonsDTO p = FACADE.getPersonByTel(number);
        return GSON.toJson(p);
    }

    @Path("/zip/{number}")
    @GET
    @Produces({MediaType.APPLICATION_JSON})
    public String getPersonsInZip(@PathParam("number") int number) throws NotFoundException, MissingInputException {
        PersonsDTO p = FACADE.getAllPersonsInZip(number);
        return GSON.toJson(p);
    }

    @Path("/street/{street}")
    @GET
    @Produces({MediaType.APPLICATION_JSON})
    public String getPeopleOnStreet(@PathParam("street") String street) throws NotFoundException, MissingInputException {
        PersonsDTO p = FACADE.getPersonByAdd(street);
        return GSON.toJson(p);
    }

    @Path("{id}")
    @PUT
    @Produces({MediaType.APPLICATION_JSON})
    @Consumes({MediaType.APPLICATION_JSON})
    public String editPerson(@PathParam("id") long id, String pers) throws NotFoundException, MissingInputException {
        Person person = GSON.fromJson(pers, Person.class);
        person.setId(id);
        PersonDTO pEdit = FACADE.editPerson(person);
        return GSON.toJson(pEdit);
    }

    @Path("/addperson")
    @POST
    @Produces({MediaType.APPLICATION_JSON})
    @Consumes({MediaType.APPLICATION_JSON})
    public String addPerson(String pers) throws NotFoundException, MissingInputException {
        PersonDTO person = GSON.fromJson(pers, PersonDTO.class);
        PersonDTO pDTO = FACADE.addPerson(person);
        return GSON.toJson(pDTO);
    }

    @Path("/address/{email}")
    @GET
    @Produces({MediaType.APPLICATION_JSON})
    public String getAddressByEmail(@PathParam("email") String email) throws NotFoundException, MissingInputException {
        List<AddressDTO> a = FACADE.getAddressByEmail(email);
        return GSON.toJson(a);
    }

    @Path("/hobby/{hobbyName}")
    @GET
    @Produces({MediaType.APPLICATION_JSON})
    public String getPeopleByHobby(@PathParam("hobbyName") String hobbyName) throws NotFoundException, MissingInputException {
        PersonsDTO p = FACADE.getPersonswithHobby(hobbyName);
        return GSON.toJson(p);
    }

    @Path("/count/{hobbyName}")
    @GET
    @Produces({MediaType.APPLICATION_JSON})
    public String countPeopleWithHobby(@PathParam("hobbyName") String hobbyName) throws NotFoundException, MissingInputException {
        long count = FACADE.countPersonswithHobby(hobbyName);
        return "{\"count\":" + count + "}"; 
    }

    @Path("/hobby/all")
    @GET
    @Produces({MediaType.APPLICATION_JSON})
    public String getAllHobbies() {
        List<HobbyDTO> hDTO = FACADE.getAllHobbies();
        String json = GSON.toJson(hDTO);
        return json;
    }

    @Path("/address/bystreet/{street}")
    @GET
    @Produces({MediaType.APPLICATION_JSON})
    public String getPersonsByStreet(@PathParam("street") String street) throws NotFoundException, MissingInputException {
        PersonsDTO persons = FACADE.getPersonsByStreet(street);
        return GSON.toJson(persons);
    }
    
    @Path("/singlehobby/{hobbyName}")
    @GET
    @Produces({MediaType.APPLICATION_JSON})
    public String getSingleHobby(@PathParam("hobbyName") String hobbyName) throws NotFoundException, MissingInputException {
        HobbyDTO hobby = FACADE.getSpecificHobbyByName(hobbyName);
        return GSON.toJson(hobby);
    }
    
}
