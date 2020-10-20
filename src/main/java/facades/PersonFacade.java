package facades;

import dto.PersonDTO;
import dto.PersonsDTO;
import entities.Address;
import entities.CityInfo;
import entities.Hobby;
import entities.Person;
import entities.Phone;
import exceptions.MissingInputException;
import exceptions.NotFoundException;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.TypedQuery;

public class PersonFacade {

    private static PersonFacade instance;
    private static EntityManagerFactory emf;

    //Private Constructor to ensure Singleton
    private PersonFacade() {
    }

    /**
     *
     * @param _emf
     * @return an instance of this facade class.
     */
    public static PersonFacade getFacadeExample(EntityManagerFactory _emf) {
        if (instance == null) {
            emf = _emf;
            instance = new PersonFacade();
        }
        return instance;
    }

    private EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    //Add person
    public PersonDTO addPerson(Person person, Phone phone, Address address) throws MissingInputException {
        EntityManager em = getEntityManager();

        try {
            isPersonNameCorrect(person);
            isPhonenumberCorrect(phone);
            em.getTransaction().begin();
            em.persist(address);
            em.getTransaction().commit();
            em.getTransaction().begin();
            em.persist(person);
            em.getTransaction().commit();
            em.getTransaction().begin();
            em.persist(phone);
            em.getTransaction().commit();
        } finally {
            em.close();
        }
        return new PersonDTO(person);
    }

    private void isPersonNameCorrect(Person p) throws MissingInputException {
        if (p.getFirstName() == null || p.getLastName() == null) {
            throw new MissingInputException("First Name and/or Last Name is missing");
        }
    }

    private void isPhonenumberCorrect(Phone p) throws MissingInputException {
        if (Integer.valueOf(p.getNumber()) == null || Integer.valueOf(p.getNumber()).toString().length() != 8) {
            throw new MissingInputException("Phonenumber must be 8 characters long");
        }
    }

    //Get all persons, using PersonsDTO as return
    public PersonsDTO getAllPersons() {
        EntityManager em = getEntityManager();
        try {

            TypedQuery<Person> query = em.createNamedQuery("Person.getAllPersons", Person.class);
           // TypedQuery<Person> query = em.createQuery("SELECT p FROM Person p", Person.class);
            List<Person> persons = query.getResultList();

            PersonsDTO pDTO = new PersonsDTO(persons);
            
            return pDTO;
        } finally {
            em.close();
        }
    }

    public void createTestPerson() {
        CityInfo cityInfo = new CityInfo(123, "lolcity");
        Address address = new Address("lol", "dont live here");
        Phone phone = new Phone(12345678, "dont call me");
        Hobby hobby = new Hobby("loasld", "nej", "ok", "asd");
        Person person = new Person("stgerve", "mecgee", "lolasdas");
        person.addHobby(hobby);
        person.addPhone(phone);
        cityInfo.addAddress(address);
        person.setAddress(address);
        EntityManager em = getEntityManager();
        try {
            em.getTransaction().begin();
            em.persist(cityInfo);
            em.getTransaction().commit();
            em.getTransaction().begin();
            em.persist(address);
            em.getTransaction().commit();

            em.getTransaction().begin();
            em.persist(person);
            em.getTransaction().commit();

        } finally {
            em.close();
        }

    }

    //Get a person by a phone number, that must be 8 characters long
    public PersonDTO getPersonByTel(int phonenumber) throws NotFoundException, MissingInputException {
        EntityManager em = getEntityManager();
        if (Integer.valueOf(phonenumber) == null || Integer.valueOf(phonenumber).toString().length() != 8) {
            throw new MissingInputException("The chosen input is not possible");
        }
        try {
            TypedQuery<Person> query = em.createQuery("SELECT p FROM Person p, Phone a WHERE p.id = a.id AND a.number = :number", Person.class);
            query.setParameter("number", phonenumber);
            Person p = query.getSingleResult();
            if (p == null) {
                throw new NotFoundException("The chosen action is not possible, lol");
            }
            PersonDTO result = new PersonDTO(p);
            return result;
        } finally {
            em.close();
        }
    }

    //Get all persons, given a zip
    public PersonsDTO getAllPersonsInZip(int zip) throws NotFoundException, MissingInputException {
        EntityManager em = getEntityManager();
        if (Integer.valueOf(zip) == null) {
            throw new MissingInputException("The chosen input is not possible");
        }
        try {
            TypedQuery<Person> query = em.createQuery("SELECT p FROM Person p WHERE p.address.cityinfo.zipCode = :zip ", Person.class);
            query.setParameter("zip", zip);
            List<Person> p = query.getResultList();
            if (p == null) {
                throw new NotFoundException("The chosen action is not possible, lol");
            }
            PersonsDTO result = new PersonsDTO(p);
            return result;
        } finally {
            em.close();
        }
    }

    //Get a persons by address
    public PersonsDTO getPersonByTel(Address address) throws NotFoundException {
        EntityManager em = getEntityManager();

        try {
            TypedQuery<Person> query = em.createQuery("SELECT p FROM Person p WHERE p.address = : address", Person.class);
            query.setParameter("address", address);
            List<Person> p = query.getResultList();
            if (p == null) {
                throw new NotFoundException("The chosen action is not possible, lol");
            }
            PersonsDTO result = new PersonsDTO(p);
            return result;
        } finally {
            em.close();
        }
    }

    public PersonDTO editPerson(Person p) throws MissingInputException, NotFoundException {
        EntityManager em = getEntityManager();
        isPersonNameCorrect(p);
        try {
            Person person = em.find(Person.class, p.getId());
            if (p == null) {
                throw new NotFoundException("The chosen action is not possible, lol");
            }
            em.getTransaction().begin();
            person.setFirstName(p.getFirstName());
            person.setLastName(p.getLastName());
            person.setEmail(p.getEmail());
            person.setAddress(p.getAddress());
            em.getTransaction().commit();
            return new PersonDTO(person);
        } finally {
            em.close();
        }

    }

    //Delete person by id
    public PersonDTO deletePerson(Long id) throws MissingInputException {
        EntityManager em = getEntityManager();
        try {
            Person person = em.find(Person.class, id);
            if (person == null) {
                throw new MissingInputException("No valid input given");
            }
            em.getTransaction().begin();
            em.remove(person);
            em.getTransaction().commit();
            return new PersonDTO(person);
        } finally {
            em.close();
        }
    }
}
