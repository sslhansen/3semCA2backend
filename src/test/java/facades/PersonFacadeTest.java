package facades;

import dto.PersonDTO;
import entities.Address;
import entities.CityInfo;
import entities.Hobby;
import entities.Person;
import entities.Phone;
import utils.EMF_Creator;
import entities.RenameMe;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

//Uncomment the line below, to temporarily disable this test
@Disabled
public class PersonFacadeTest {

    private static EntityManagerFactory emf;
    private static PersonFacade facade;

    public PersonFacadeTest() {
    }

    @BeforeAll
    public static void setUpClass() {
        emf = EMF_Creator.createEntityManagerFactoryForTest();
        facade = PersonFacade.getFacadeExample(emf);
    }

    @AfterAll
    public static void tearDownClass() {
//        Clean up database after test is done or use a persistence unit with drop-and-create to start up clean on every test
    }

    // Setup the DataBase in a known state BEFORE EACH TEST
    //TODO -- Make sure to change the code below to use YOUR OWN entity class
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
        //person1.addHobby(hobby1);
        person1.addPhone(phone1);
        cityInfo1.addAddress(address1);
        person1.setAddress(address1);
        CityInfo cityInfo2 = new CityInfo(2222, "TestCity2");
        Address address2 = new Address("TestStreet2", "dont live here2");
        Phone phone2 = new Phone(22222222, "dont call me");
        Hobby hobby2 = new Hobby("Testing2", "nej.dk", "testing2", "desc2");
        Person person2 = new Person("Test2@tester.dk", "McTest2", "Test2");
        //person2.addHobby(hobby2);
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
    public void testgetAllPersons() {
        int actualSize = facade.getAllPersons().getAll().size();
        int expectedSize = 2;
        assertEquals(expectedSize, actualSize, "Expects two persons");
    }

    @Test
    public void testAddPerson() {
        EntityManager em = emf.createEntityManager();
        CityInfo cityInfo2 = new CityInfo(2222, "TestCity2");
        Address address2 = new Address("TestStreet2", "dont live here2");
        Phone phone2 = new Phone(22222222, "dont call me");
        Hobby hobby2 = new Hobby("Testing2", "nej.dk", "testing2", "desc2");
        Person person2 = new Person("Test2@tester.dk", "McTest2", "Test2");
        //person2.addHobby(hobby2);
        person2.addPhone(phone2);
        cityInfo2.addAddress(address2);
        person2.setAddress(address2);
        try {
            em.getTransaction().begin();
            em.persist(cityInfo2);
            em.getTransaction().commit();
            em.getTransaction().begin();
            em.persist(address2);
            em.getTransaction().commit();
            em.getTransaction().begin();
            em.persist(person2);
            em.getTransaction().commit();
        } finally {
            em.close();
        }
        int expectedPersons = 3;
        assertEquals(expectedPersons, facade.getAllPersons().getAll().size(), "Expects two persons");
    }

//    @Test
//    public void testDeletePerson(){
//        EntityManager em = emf.createEntityManager();
//        try {
//            Person person1 = em.find(Person.class, person1.getId());
//            assertNotNull(person1);
//            facade.deletePerson(person1.getId());
//            assertEquals(1, facade.getAllPersons());
//
//        } finally {
//            em.close();
//        }
//    }
//    @Test
//    public void testGetPersonByAddress() {
//        Person person1 = facade.
//        person1.getAddress();
//        Movie mov = facade.getPersonByAdd(person1.getAddress());
//        assertEquals(rm1.getTitle(), mov.getTitle());
//    }
//    @Test
//    public void testGetPersonByAddress() {
//        
//     PersonDTO person1 = facade.getPersonByAdd(streetName);
//     int expectedPerson = person1.getAddress()
//        assertEquals("lol", person1.getAddress());
//    }
//     @Test
//    public void testGetPersonByAddress() {
//        int actualSize = facade.;
//        int expectedSize = "Hejda";
//        assertEquals(expectedSize, actualSize, "Expects Hejsa");
//    }
// Why do i choose to suffer when death is inevitable anyway
}
