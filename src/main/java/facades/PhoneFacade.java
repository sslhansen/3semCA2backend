package facades;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

public class PhoneFacade {

    private static PhoneFacade instance;
    private static EntityManagerFactory emf;

    //Private Constructor to ensure Singleton
    private PhoneFacade() {
    }

    /**
     *
     * @param _emf
     * @return an instance of this facade class.
     */
    public static PhoneFacade getFacadeExample(EntityManagerFactory _emf) {
        if (instance == null) {
            emf = _emf;
            instance = new PhoneFacade();
        }
        return instance;
    }

    private EntityManager getEntityManager() {
        return emf.createEntityManager();
    }
}
