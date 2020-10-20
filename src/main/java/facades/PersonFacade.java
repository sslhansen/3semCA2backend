package facades;

import dto.PersonDTO;
import entities.Person;
import exceptions.MissingInputException;
import exceptions.NotFoundException;
import java.util.List;
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

    public List<PersonDTO> getAllPersons() {
        EntityManager em = getEntityManager();
        try {
            em.getTransaction().begin();
            em.flush();
            em.getTransaction().commit();
            TypedQuery<PersonDTO> query = em.createNamedQuery("Person.getAllPersons", PersonDTO.class);
            return query.getResultList();
        } finally {
            em.close();
        }
    }

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

    public PersonDTO deletePerson(Long id) throws MissingInputException {
        EntityManager em = getEntityManager();
        try {
            Person person = em.find(Person.class, id);
            em.getTransaction().begin();
            em.remove(person);
            em.getTransaction().commit();
            return new PersonDTO(person);
        } finally {
            em.close();
        }
    }
}
