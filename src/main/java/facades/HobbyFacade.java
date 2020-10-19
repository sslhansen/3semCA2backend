package facades;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;


public class HobbyFacade {
    private static HobbyFacade instance;
    private static EntityManagerFactory emf;

    //Private Constructor to ensure Singleton
    private HobbyFacade() {
    }

    /**
     *
     * @param _emf
     * @return an instance of this facade class.
     */
    public static HobbyFacade getFacadeExample(EntityManagerFactory _emf) {
        if (instance == null) {
            emf = _emf;
            instance = new HobbyFacade();
        }
        return instance;
    }

    private EntityManager getEntityManager() {
        return emf.createEntityManager();
    }
}
