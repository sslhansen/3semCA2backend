package facades;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;


public class CityInfoFacade {
    private static CityInfoFacade instance;
    private static EntityManagerFactory emf;

    //Private Constructor to ensure Singleton
    private CityInfoFacade() {
    }

    /**
     *
     * @param _emf
     * @return an instance of this facade class.
     */
    public static CityInfoFacade getFacadeExample(EntityManagerFactory _emf) {
        if (instance == null) {
            emf = _emf;
            instance = new CityInfoFacade();
        }
        return instance;
    }

    private EntityManager getEntityManager() {
        return emf.createEntityManager();
    }
}
