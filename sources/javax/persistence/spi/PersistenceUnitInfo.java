package javax.persistence.spi;

import java.net.URL;
import java.util.List;
import java.util.Properties;
import javax.sql.DataSource;

/* loaded from: persistence-api-1.0.jar:javax/persistence/spi/PersistenceUnitInfo.class */
public interface PersistenceUnitInfo {
    String getPersistenceUnitName();

    String getPersistenceProviderClassName();

    PersistenceUnitTransactionType getTransactionType();

    DataSource getJtaDataSource();

    DataSource getNonJtaDataSource();

    List<String> getMappingFileNames();

    List<URL> getJarFileUrls();

    URL getPersistenceUnitRootUrl();

    List<String> getManagedClassNames();

    boolean excludeUnlistedClasses();

    Properties getProperties();

    ClassLoader getClassLoader();

    void addTransformer(ClassTransformer classTransformer);

    ClassLoader getNewTempClassLoader();
}
