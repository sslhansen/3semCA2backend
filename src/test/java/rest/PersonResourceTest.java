package rest;

import dto.PersonDTO;
import entities.Address;
import entities.CityInfo;
import entities.Hobby;
import entities.Person;
import entities.Phone;
import io.restassured.RestAssured;
import static io.restassured.RestAssured.given;
import io.restassured.parsing.Parser;
import io.restassured.response.Response;
import java.net.URI;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.ws.rs.core.UriBuilder;
import org.glassfish.grizzly.http.server.HttpServer;
import org.glassfish.grizzly.http.util.HttpStatus;
import org.glassfish.jersey.grizzly2.httpserver.GrizzlyHttpServerFactory;
import org.glassfish.jersey.server.ResourceConfig;
import static org.hamcrest.MatcherAssert.assertThat;
import org.hamcrest.Matchers;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasItems;
import static org.hamcrest.Matchers.notNullValue;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import utils.EMF_Creator;

/**
 *
 * @author groen
 */
public class PersonResourceTest {

    private static final int SERVER_PORT = 7777;
    private static final String SERVER_URL = "http://localhost/api";

    static final URI BASE_URI = UriBuilder.fromUri(SERVER_URL).port(SERVER_PORT).build();
    private static HttpServer httpServer;
    private static EntityManagerFactory emf;
    private CityInfo cityInfo1;
    private CityInfo cityInfo2;
    private Address address1;
    private Address address2;
    private Phone phone1;
    private Phone phone2;
    private Hobby hobby1;
    private Hobby hobby2;
    private Person person1;
    private Person person2;

    static HttpServer startServer() {
        ResourceConfig rc = ResourceConfig.forApplication(new ApplicationConfig());
        return GrizzlyHttpServerFactory.createHttpServer(BASE_URI, rc);
    }

    @BeforeAll
    public static void setUpClass() {
        //This method must be called before you request the EntityManagerFactory
        EMF_Creator.startREST_TestWithDB();
        emf = EMF_Creator.createEntityManagerFactoryForTest();

        httpServer = startServer();
        //Setup RestAssured
        RestAssured.baseURI = SERVER_URL;
        RestAssured.port = SERVER_PORT;
        RestAssured.defaultParser = Parser.JSON;
    }

    @AfterAll
    public static void closeTestServer() {
        //System.in.read();
        //Don't forget this, if you called its counterpart in @BeforeAll
        EMF_Creator.endREST_TestWithDB();
        httpServer.shutdownNow();
    }

    @BeforeEach
    public void setUp() {
        EntityManager em = emf.createEntityManager();

        try {
            em.getTransaction().begin();
            cityInfo1 = new CityInfo(1111, "TestCity1");
            address1 = new Address("TestStreet1", "dont live here1");
            phone1 = new Phone(11111111, "dont call me");
            hobby1 = new Hobby("Testing1", "nej.dk", "testing1", "desc1");
            person1 = new Person("Test1@tester.dk", "McTest1", "Test1");
            cityInfo2 = new CityInfo(2222, "TestCity2");
            address2 = new Address("TestStreet2", "dont live here2");
            phone2 = new Phone(22222222, "dont call me");
            hobby2 = new Hobby("Testing2", "nej.dk", "testing2", "desc2");
            person2 = new Person("Test2@tester.dk", "McTest2", "Test2");
            em.getTransaction().commit();
            em.getTransaction().begin();
            hobby1.addPersons(person1);
            person1.addPhone(phone1);
            cityInfo1.addAddress(address1);
            person1.setAddress(address1);
            em.getTransaction().commit();
            em.getTransaction().begin();
            hobby2.addPersons(person2);
            person2.addPhone(phone2);
            cityInfo2.addAddress(address2);
            person2.setAddress(address2);
            em.getTransaction().commit();
            em.getTransaction().begin();
            em.createNamedQuery("Phone.deleteAllRows").executeUpdate();
            em.getTransaction().commit();
            em.getTransaction().begin();
            em.createNamedQuery("Hobby.deleteAllRows").executeUpdate();
            em.getTransaction().commit();
            em.getTransaction().begin();
            em.createNamedQuery("Person.deleteAllRows").executeUpdate();
            em.getTransaction().commit();
            em.getTransaction().begin();
            em.createNamedQuery("Address.deleteAllRows").executeUpdate();
            em.getTransaction().commit();
            em.getTransaction().begin();
            em.createNamedQuery("CityInfo.deleteAllRows").executeUpdate();
            em.getTransaction().commit();
            em.getTransaction().begin();
            em.persist(cityInfo1);
            em.persist(cityInfo2);
            em.getTransaction().commit();
            em.getTransaction().begin();
            em.persist(address1);
            em.persist(address2);
            em.getTransaction().commit();
            em.getTransaction().begin();
            em.persist(hobby1);
            em.getTransaction().commit();
            em.getTransaction().begin();
            em.persist(hobby2);
            em.getTransaction().commit();
            em.getTransaction().begin();
            em.persist(person1);
            em.getTransaction().commit();
            em.getTransaction().begin();
            em.persist(person2);
            em.getTransaction().commit();
        } finally {
            em.close();
        }
    }

    @AfterEach
    public void tearDown() {
//        Remove any data after each test was run
    }

    @Test
    public void testServerIsUp() {
        System.out.println("Testing is server UP");
        given().when().get("/person").then().statusCode(200);
    }

    @Test
    public void testDummyMsg() throws Exception {
        given()
                .contentType("application/json")
                .get("/person/").then()
                .assertThat()
                .statusCode(HttpStatus.OK_200.getStatusCode())
                .body("msg", equalTo("Hello World"));
    }

    @Test
    public void getAllPersons() {
        Response response = given()
                .when().get("/person/all")
                .then()
                .contentType("application/json")
                .body("all.firstName", hasItems("McTest1", "McTest2"))
                .extract().response();
    }
    
    @Test
    public void testCount() throws Exception {
        given()
                .contentType("application/json")
                .get("/person/count/"+hobby1.getName()).then()
                .assertThat()
                .statusCode(HttpStatus.OK_200.getStatusCode())
                .body("count", equalTo(2));
    }
    @Test
    public void getAllPersonsTest() {
        List<PersonDTO> personsDTO;
        personsDTO = given()
                .contentType("application/json")
                .when()
                .get("/person/all").then()
                .extract().body().jsonPath().getList("all", PersonDTO.class);

        assertEquals(personsDTO.size(), 2);
    }
    @Test
    public void testFindPersonByTel() {
        List<PersonDTO> personsDTO;
        personsDTO = given()
                .contentType("application/json")
                .when()
                .get("/person/"+phone1.getNumber()).then()
                .extract().body().jsonPath().getList("all", PersonDTO.class);

        assertEquals(personsDTO.get(0).getFirstName(), person1.getFirstName());
    }
    
//    @Test
//    public void testAddPerson() {
//        Person p = new Person("Imposter", "FromAddPerson", "0001");
//        given()
//                .contentType("application/json")
//                .body(new PersonDTO(p))
//                .when()
//                .post("person")
//                .then()
//                .body("email", equalTo("Imposter"))
//                .body("firstName", equalTo("FromAddPerson"))
//                .body("lastName", equalTo("0001"))
//                .body("id", notNullValue());
//    }
    
    
}
