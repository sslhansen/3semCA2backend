package facades;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

public class AddressFacade {

    private static AddressFacade instance;
    private static EntityManagerFactory emf;

    //Private Constructor to ensure Singleton
    private AddressFacade() {
    }

    /**
     *
     * @param _emf
     * @return an instance of this facade class.
     */
    public static AddressFacade getFacadeExample(EntityManagerFactory _emf) {
        if (instance == null) {
            emf = _emf;
            instance = new AddressFacade();
        }
        return instance;
    }

    private EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

}
