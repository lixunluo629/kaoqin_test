package javax.persistence.spi;

import java.util.Map;
import javax.persistence.EntityManagerFactory;

/* loaded from: persistence-api-1.0.jar:javax/persistence/spi/PersistenceProvider.class */
public interface PersistenceProvider {
    EntityManagerFactory createEntityManagerFactory(String str, Map map);

    EntityManagerFactory createContainerEntityManagerFactory(PersistenceUnitInfo persistenceUnitInfo, Map map);
}
