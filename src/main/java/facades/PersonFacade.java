package facades;

import dto.AddressDTO;
import dto.HobbyDTO;
import dto.PersonDTO;
import dto.PersonsDTO;
import dto.PhoneDTO;
import entities.Address;
import entities.CityInfo;
import entities.Hobby;
import entities.Person;
import entities.Phone;
import exceptions.MissingInputException;
import exceptions.NotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;
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
    public PersonDTO addPerson(PersonDTO pDTO) throws MissingInputException, NotFoundException {
        EntityManager em = getEntityManager();

        em.getTransaction().begin();
        System.out.println(pDTO.getPhones().size());

        Person person = new Person(pDTO.getEmail(), pDTO.getFirstName(), pDTO.getLastName());

        isPersonNameCorrect(person);

        for (PhoneDTO pa : pDTO.getPhones()) {
            if (isPhonenumberCorrect(pa)) {
                Phone p = new Phone(pa.getNumber(), pa.getDescription());
                person.addPhone(p);
            }
        }

        CityInfo ci;
        if (!cityinfoExists(pDTO.getAddress().getCityinfo().getZipCode())) {
            throw new NotFoundException("Cityinfo does not exist");
        } else {
            ci = getCityInfoByZip(pDTO.getAddress().getCityinfo().getZipCode());
        }
        Address address;
        if (addressExists(pDTO.getAddress().getStreet())) {
            address = getAddressByStreet(pDTO.getAddress().getStreet());
        } else {
            address = new Address(pDTO.getAddress().getStreet(), pDTO.getAddress().getAdditionalInfo());
            em.persist(address);
        }

        for (HobbyDTO hb : pDTO.getHobbies()) {
            if (hobbyExists(hb.getName())) {
                Hobby h = getHobbyByName(hb.getName());
                h.addPersons(person);
            }
        }
        person.setAddress(address);
        ci.addAddress(address);
        address.addPersons(person);
        em.persist(person);
        em.getTransaction().commit();
        return new PersonDTO(person);
    }

    private boolean addressExists(String streetName) throws NotFoundException {
        EntityManager em = getEntityManager();
        try {
            TypedQuery<Address> query = em.createQuery("SELECT a FROM Address a WHERE a.street = :street", Address.class);
            query.setParameter("street", streetName);
            List<Address> adr = query.getResultList();
            if (adr.isEmpty()) {
                return false;
            } else {
                return true;
            }
        } finally {
            em.close();
        }
    }

    private boolean cityinfoExists(int zipCode) {
        EntityManager em = getEntityManager();
        try {
            TypedQuery<CityInfo> query = em.createQuery("SELECT a FROM CityInfo a WHERE a.zipCode = :zipCode", CityInfo.class);
            query.setParameter("zipCode", zipCode);
            List<CityInfo> cityInfos = query.getResultList();
            if (cityInfos.isEmpty()) {
                return false;
            } else {
                return true;
            }
        } finally {
            em.close();
        }
    }

    private boolean hobbyExists(String hobbyName) throws NotFoundException {
        EntityManager em = getEntityManager();
        try {
            TypedQuery<Hobby> query = em.createQuery("SELECT a FROM Hobby a WHERE a.name = :name", Hobby.class);
            query.setParameter("name", hobbyName);
            List<Hobby> adr = query.getResultList();
            if (adr.isEmpty()) {
                return false;
            } else {
                return true;
            }
        } finally {
            em.close();
        }
    }

    private Hobby getHobbyByName(String hobbyName) throws NotFoundException {
        EntityManager em = getEntityManager();
        try {
            TypedQuery<Hobby> query = em.createQuery("SELECT a FROM Hobby a WHERE a.name = :name", Hobby.class);
            query.setParameter("name", hobbyName);
            Hobby hb = query.getSingleResult();
            if (hb == null) {
                throw new NotFoundException("The chosen action is not possible");
            }
            return hb;
        } finally {
            em.close();
        }
    }

    private void isPersonNameCorrect(Person p) throws MissingInputException {
        if (p.getFirstName() == null || p.getLastName() == null) {
            throw new MissingInputException("First Name and/or Last Name is missing");
        }
    }

    private boolean isPhonenumberCorrect(PhoneDTO p) throws MissingInputException {
        if (Integer.valueOf(p.getNumber()) == null || Integer.valueOf(p.getNumber()).toString().length() != 8) {
            throw new MissingInputException("Phonenumber must be 8 characters long");
        } else {
            return true;
        }
    }

    //Get all persons, using PersonsDTO as return
    public PersonsDTO getAllPersons() {
        EntityManager em = getEntityManager();
        try {

            TypedQuery<Person> query = em.createNamedQuery("Person.getAllPersons", Person.class);
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
        hobby.addPersons(person);
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
            em.persist(hobby);
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
                throw new NotFoundException("The chosen action is not possible");
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
                throw new NotFoundException("The chosen action is not possible");
            }
            PersonsDTO result = new PersonsDTO(p);
            return result;
        } finally {
            em.close();
        }
    }

    //Get a persons by address
    public PersonsDTO getPersonByAdd(String streetName) throws NotFoundException {
        EntityManager em = getEntityManager();

        try {
            TypedQuery<Person> query = em.createQuery("SELECT p FROM Person p WHERE p.address.street = :street", Person.class);
            query.setParameter("street", streetName);
            List<Person> p = query.getResultList();
            if (p == null) {
                throw new NotFoundException("The chosen action is not possible");
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
                throw new NotFoundException("The chosen action is not possible");
            }
            person.setAddress(getAddressByStreet(p.getAddress().getStreet()));
            em.getTransaction().begin();
            person.setFirstName(p.getFirstName());
            person.setLastName(p.getLastName());
            person.setEmail(p.getEmail());
            em.getTransaction().commit();
            return new PersonDTO(person);
        } finally {
            em.close();
        }
    }

    public Address getAddressByStreet(String streetName) throws NotFoundException {
        EntityManager em = getEntityManager();
        try {
            TypedQuery<Address> query = em.createQuery("SELECT a FROM Address a WHERE a.street = :street", Address.class);
            query.setParameter("street", streetName);
            Address adr = query.getSingleResult();
            if (adr == null) {
                throw new NotFoundException("The chosen action is not possible");
            }
            return adr;
        } finally {
            em.close();
        }
    }

    public CityInfo getCityInfoByZip(int zip) throws NotFoundException {
        EntityManager em = getEntityManager();
        try {
            TypedQuery<CityInfo> query = em.createQuery("SELECT a FROM CityInfo a WHERE a.zipCode = :zip", CityInfo.class);
            query.setParameter("zip", zip);
            CityInfo ci = query.getSingleResult();
            if (ci == null) {
                throw new NotFoundException("The chosen action is not possible");
            }
            return ci;
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

    //Find adress using email
    public List<AddressDTO> getAddressByEmail(String email) throws NotFoundException {

        EntityManager em = getEntityManager();
        try {
            TypedQuery<Address> query = em.createQuery("SELECT a FROM Address a WHERE a.persons.email = :email", Address.class);
            query.setParameter("email", email);
            List<Address> adr = query.getResultList();
            if (adr.isEmpty()) {
                throw new NotFoundException("No address with given email found");
            }
            
            List<AddressDTO> aDTO = new ArrayList<>();
            for (Address address : adr) {
                AddressDTO dto = new AddressDTO(address);
                aDTO.add(dto);
            }
            
            return aDTO;
        } finally {
            em.close();
        }
    }

    //Get all Hobbie
    public List<HobbyDTO> getAllHobbies() {
        EntityManager em = getEntityManager();
        try {

            TypedQuery<Hobby> query = em.createQuery("SELECT h FROM Hobby h", Hobby.class);
            List<Hobby> hobbies = query.getResultList();

            List<HobbyDTO> hDTO = new ArrayList<>();
            for (Hobby hobby : hobbies) {
                HobbyDTO dto = new HobbyDTO(hobby);
                hDTO.add(dto);
            }

            return hDTO;
        } finally {
            em.close();
        }
    }

    //Find people with same hobby 
    public PersonsDTO getPersonswithHobby(String hobbyName) throws NotFoundException {

        EntityManager em = getEntityManager();
        try {
            TypedQuery<Person> query = em.createQuery("SELECT p FROM Person p WHERE p.hobbies.name = :hobbyName", Person.class);
            query.setParameter("hobbyName", hobbyName);
            List<Person> people = query.getResultList();
            if (people.isEmpty()) {
                throw new NotFoundException("No on has this hobby");
            }
            PersonsDTO pDTO = new PersonsDTO(people);
            return pDTO;
        } finally {
            em.close();
        }
    }

    //Count people with same hobby 
    public int countPersonswithHobby(String hobbyName) throws NotFoundException {

        EntityManager em = getEntityManager();
        try {
            Query query = em.createQuery("SELECT COUNT(p) FROM Person p WHERE p.hobbies.name = :hobbyName", Person.class);
            query.setParameter("hobbyName", hobbyName);
            int result = (int) query.getSingleResult();
            return result;
        } finally {
            em.close();
        }
    }

}
