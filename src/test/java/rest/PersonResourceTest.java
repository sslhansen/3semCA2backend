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
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasItems;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import utils.EMF_Creator;

/**
 *
 * @author groen
 */
@Disabled
public class PersonResourceTest {

    private static final int SERVER_PORT = 7777;
    private static final String SERVER_URL = "http://localhost/api";

    static final URI BASE_URI = UriBuilder.fromUri(SERVER_URL).port(SERVER_PORT).build();
    private static HttpServer httpServer;
    private static EntityManagerFactory emf;

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
//        try {
//            em.getTransaction().begin();
//            em.createNamedQuery("Person.deleteAllRows").executeUpdate();
//            em.persist(new Person("Some txt", "More text"));
//            em.persist(new Person("aaa", "bbb"));
//
//            em.getTransaction().commit();
//        } finally {
//            em.close();
//        }
        CityInfo cityInfo1 = new CityInfo(1111, "TestCity1");
        Address address1 = new Address("TestStreet1", "dont live here1");
        Phone phone1 = new Phone(11111111, "dont call me");
        Hobby hobby1 = new Hobby("Testing1", "nej.dk", "testing1", "desc1");
        Person person1 = new Person("Test1@tester.dk", "McTest1", "Test1");
  //      person1.addHobby(hobby1);
        person1.addPhone(phone1);
        cityInfo1.addAddress(address1);
        person1.setAddress(address1);
        CityInfo cityInfo2 = new CityInfo(2222, "TestCity2");
        Address address2 = new Address("TestStreet2", "dont live here2");
        Phone phone2 = new Phone(22222222, "dont call me");
        Hobby hobby2 = new Hobby("Testing2", "nej.dk", "testing2", "desc2");
        Person person2 = new Person("Test2@tester.dk", "McTest2", "Test2");
//        person2.addHobby(hobby2);
        person2.addPhone(phone2);
        cityInfo2.addAddress(address2);
        person2.setAddress(address2);
        try {
            em.getTransaction().begin();
            em.createNamedQuery("Person.deleteAllRows").executeUpdate();
            em.createNamedQuery("Person.deleteAllRows").executeUpdate();
            em.createNamedQuery("Person.deleteAllRows").executeUpdate();
            em.createNamedQuery("Person.deleteAllRows").executeUpdate();
            em.createNamedQuery("Person.deleteAllRows").executeUpdate();
            em.persist(cityInfo1);
            em.persist(cityInfo2);
            em.getTransaction().commit();
            em.getTransaction().begin();
            em.persist(address1);
            em.persist(address2);
            em.getTransaction().commit();
            em.getTransaction().begin();
            em.persist(person1);
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
        List<PersonDTO> personsDTOs;

        Response response = given()
                .when().get("/person/all")
                .then()
                .contentType("application/json")
                .body("all.firstName", hasItems("Lars", "Henrik", "Pleasevirk"))
                .extract().response();
        System.out.println(response.asString());

    }

}
