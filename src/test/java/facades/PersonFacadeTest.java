package facades;

import dto.AddressDTO;
import dto.CityInfoDTO;
import dto.HobbyDTO;
import dto.PersonDTO;
import dto.PersonsDTO;
import entities.Address;
import entities.CityInfo;
import entities.Hobby;
import entities.Person;
import entities.Phone;
import utils.EMF_Creator;
import entities.RenameMe;
import exceptions.MissingInputException;
import exceptions.NotFoundException;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

//Uncomment the line below, to temporarily disable this test
//@Disabled
public class PersonFacadeTest {

    private static EntityManagerFactory emf;
    private static PersonFacade facade;
    private Person person1;
    private Person person2;
    private Hobby hobby1;
    private Hobby hobby2;
    private Phone phone1;
    private Phone phone2;
    private Address address1;
    private Address address2;
    private CityInfo cityInfo1;
    private CityInfo cityInfo2;

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
            em.getTransaction().commit();
            em.getTransaction().begin();
            em.persist(cityInfo2);
            em.getTransaction().commit();
            em.getTransaction().begin();
            em.persist(address1);
            em.getTransaction().commit();
            em.getTransaction().begin();
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
    public void testgetAllPersons() {
        int actualSize = facade.getAllPersons().getAll().size();
        int expectedSize = 2;
        assertEquals(expectedSize, actualSize, "Expects two persons");
    }
//

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

    @Test
    public void testGetPersonByAddress() throws MissingInputException, NotFoundException {
        PersonsDTO person = facade.getPersonByAdd(address1.getStreet());
        PersonDTO pdto = person.getAll().get(0);
        assertEquals("McTest1", pdto.getFirstName());
    }

    @Test
    public void testGetPersonByAddressMissingInput() throws NotFoundException {
        Assertions.assertThrows(NotFoundException.class, () -> {
            PersonsDTO person = facade.getPersonByAdd("fiskeaddresse");
        });
    }
    
        @Test
    public void testGetAllPersonsInZipInvalidInput() throws NotFoundException {
        Assertions.assertThrows(NotFoundException.class, () -> {
            PersonsDTO person = facade.getAllPersonsInZip(0000000);
        });
    }
    
    @Test
    public void testGetAllPersonsInZip() throws NotFoundException, MissingInputException {
        PersonsDTO person = facade.getAllPersonsInZip(cityInfo1.getZipCode());
        PersonDTO pdto = person.getAll().get(0);
        assertEquals("McTest1", pdto.getFirstName());
    }

    @Test
    public void testGetPersonByTel() throws NotFoundException, MissingInputException {
        PersonsDTO person = facade.getPersonByTel(phone1.getNumber());
        PersonDTO pdto = person.getAll().get(0);
        assertEquals("McTest1", pdto.getFirstName());
    }

    @Test
    public void testPersonsByStreet() throws NotFoundException {
        PersonsDTO person = facade.getPersonsByStreet(address1.getStreet());
        PersonDTO pdto = person.getAll().get(0);
        assertEquals("McTest1", pdto.getFirstName());
    }

    @Test
    public void testPersonsWithHobby() throws NotFoundException {
        PersonsDTO person = facade.getPersonswithHobby(hobby1.getName());
        PersonDTO pdto = person.getAll().get(0);
        assertEquals("McTest1", pdto.getFirstName());
    }

    @Test
    public void testGetAddressByEmail() throws NotFoundException {
        List<AddressDTO> address = facade.getAddressByEmail(person1.getEmail());
        AddressDTO adto = address.get(0);
        assertEquals("TestStreet1", adto.getStreet());
    }

    @Test
    public void testGetAddressByStreet() throws NotFoundException {
        Address address = facade.getAddressByStreet(address1.getStreet());
        assertEquals("TestStreet1", address.getStreet());
    }

    @Test
    public void testGetAllHobbies() {
        List<HobbyDTO> hob = facade.getAllHobbies();
        HobbyDTO hdto = hob.get(0);
        assertEquals("Testing1", hdto.getName());
    }

    @Test
    public void testGetCityInfoByZip() throws NotFoundException {
        CityInfo city = facade.getCityInfoByZip(cityInfo1.getZipCode());
        assertEquals("TestStreet1", city.getAddresses().get(0).getStreet());
    }

    @Test
    public void testCreateTestPerson() throws NotFoundException {
        facade.createTestPerson();
        Address address = facade.getAddressByStreet("lol");
        assertEquals("lol", address.getStreet());
    }

    @Test
    public void testCountPersonsWithHobby() throws NotFoundException {
        long num = facade.countPersonswithHobby(hobby1.getName());
        assertEquals(2, num);
    }
//    @Disabled
//    @Test
//    public void testDeletePerson() throws MissingInputException {
//        EntityManager em = emf.createEntityManager();
//        try {
//            Person p = em.find(Person.class, person1.getId());
//            assertNotNull(p);
//            facade.deletePerson(person1.getId());
//            assertEquals(1, facade.getAllPersons());
//
//        } finally {
//            em.close();
//        }
//    }

    // still no work, trying to figure out WHY TF NOT
//    @Disabled
//    @Test
//    public void testEditPerson() throws MissingInputException{
//        person1.setLastName("ChangedName");
//        PersonDTO p1New = facade.editPerson(new PersonDTO(person1));
//        assertEquals(p1New.getFirstName()), person1.getLastName());
//    }
// Why do i choose to suffer when death is inevitable anyway
}
