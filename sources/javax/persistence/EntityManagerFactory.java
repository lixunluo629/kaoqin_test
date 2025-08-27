package javax.persistence;

import java.util.Map;

/* loaded from: persistence-api-1.0.jar:javax/persistence/EntityManagerFactory.class */
public interface EntityManagerFactory {
    EntityManager createEntityManager();

    EntityManager createEntityManager(Map map);

    void close();

    boolean isOpen();
}
