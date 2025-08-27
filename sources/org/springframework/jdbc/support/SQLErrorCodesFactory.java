package org.springframework.jdbc.support;

import java.util.Collections;
import java.util.Iterator;
import java.util.Map;
import javax.sql.DataSource;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.beans.factory.xml.XmlBeanDefinitionReader;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.util.Assert;
import org.springframework.util.ConcurrentReferenceHashMap;
import org.springframework.util.PatternMatchUtils;

/* loaded from: spring-jdbc-4.3.25.RELEASE.jar:org/springframework/jdbc/support/SQLErrorCodesFactory.class */
public class SQLErrorCodesFactory {
    public static final String SQL_ERROR_CODE_OVERRIDE_PATH = "sql-error-codes.xml";
    public static final String SQL_ERROR_CODE_DEFAULT_PATH = "org/springframework/jdbc/support/sql-error-codes.xml";
    private static final Log logger = LogFactory.getLog(SQLErrorCodesFactory.class);
    private static final SQLErrorCodesFactory instance = new SQLErrorCodesFactory();
    private final Map<String, SQLErrorCodes> errorCodesMap;
    private final Map<DataSource, SQLErrorCodes> dataSourceCache = new ConcurrentReferenceHashMap(16);

    public static SQLErrorCodesFactory getInstance() {
        return instance;
    }

    protected SQLErrorCodesFactory() {
        Map<String, SQLErrorCodes> errorCodes;
        try {
            DefaultListableBeanFactory lbf = new DefaultListableBeanFactory();
            lbf.setBeanClassLoader(getClass().getClassLoader());
            XmlBeanDefinitionReader bdr = new XmlBeanDefinitionReader(lbf);
            Resource resource = loadResource(SQL_ERROR_CODE_DEFAULT_PATH);
            if (resource != null && resource.exists()) {
                bdr.loadBeanDefinitions(resource);
            } else {
                logger.warn("Default sql-error-codes.xml not found (should be included in spring.jar)");
            }
            Resource resource2 = loadResource(SQL_ERROR_CODE_OVERRIDE_PATH);
            if (resource2 != null && resource2.exists()) {
                bdr.loadBeanDefinitions(resource2);
                logger.info("Found custom sql-error-codes.xml file at the root of the classpath");
            }
            errorCodes = lbf.getBeansOfType(SQLErrorCodes.class, true, false);
            if (logger.isDebugEnabled()) {
                logger.debug("SQLErrorCodes loaded: " + errorCodes.keySet());
            }
        } catch (BeansException ex) {
            logger.warn("Error loading SQL error codes from config file", ex);
            errorCodes = Collections.emptyMap();
        }
        this.errorCodesMap = errorCodes;
    }

    protected Resource loadResource(String path) {
        return new ClassPathResource(path, getClass().getClassLoader());
    }

    public SQLErrorCodes getErrorCodes(String databaseName) {
        Assert.notNull(databaseName, "Database product name must not be null");
        SQLErrorCodes sec = this.errorCodesMap.get(databaseName);
        if (sec == null) {
            Iterator<SQLErrorCodes> it = this.errorCodesMap.values().iterator();
            while (true) {
                if (!it.hasNext()) {
                    break;
                }
                SQLErrorCodes candidate = it.next();
                if (PatternMatchUtils.simpleMatch(candidate.getDatabaseProductNames(), databaseName)) {
                    sec = candidate;
                    break;
                }
            }
        }
        if (sec != null) {
            checkCustomTranslatorRegistry(databaseName, sec);
            if (logger.isDebugEnabled()) {
                logger.debug("SQL error codes for '" + databaseName + "' found");
            }
            return sec;
        }
        if (logger.isDebugEnabled()) {
            logger.debug("SQL error codes for '" + databaseName + "' not found");
        }
        return new SQLErrorCodes();
    }

    public SQLErrorCodes getErrorCodes(DataSource dataSource) {
        String name;
        Assert.notNull(dataSource, "DataSource must not be null");
        if (logger.isDebugEnabled()) {
            logger.debug("Looking up default SQLErrorCodes for DataSource [" + identify(dataSource) + "]");
        }
        SQLErrorCodes sec = this.dataSourceCache.get(dataSource);
        if (sec == null) {
            synchronized (this.dataSourceCache) {
                sec = this.dataSourceCache.get(dataSource);
                if (sec == null) {
                    try {
                        name = (String) JdbcUtils.extractDatabaseMetaData(dataSource, "getDatabaseProductName");
                    } catch (MetaDataAccessException ex) {
                        logger.warn("Error while extracting database name - falling back to empty error codes", ex);
                    }
                    if (name != null) {
                        return registerDatabase(dataSource, name);
                    }
                    return new SQLErrorCodes();
                }
            }
        }
        if (logger.isDebugEnabled()) {
            logger.debug("SQLErrorCodes found in cache for DataSource [" + identify(dataSource) + "]");
        }
        return sec;
    }

    public SQLErrorCodes registerDatabase(DataSource dataSource, String databaseName) {
        SQLErrorCodes sec = getErrorCodes(databaseName);
        if (logger.isDebugEnabled()) {
            logger.debug("Caching SQL error codes for DataSource [" + identify(dataSource) + "]: database product name is '" + databaseName + "'");
        }
        this.dataSourceCache.put(dataSource, sec);
        return sec;
    }

    public SQLErrorCodes unregisterDatabase(DataSource dataSource) {
        return this.dataSourceCache.remove(dataSource);
    }

    private String identify(DataSource dataSource) {
        return dataSource.getClass().getName() + '@' + Integer.toHexString(dataSource.hashCode());
    }

    private void checkCustomTranslatorRegistry(String databaseName, SQLErrorCodes errorCodes) {
        SQLExceptionTranslator customTranslator = CustomSQLExceptionTranslatorRegistry.getInstance().findTranslatorForDatabase(databaseName);
        if (customTranslator != null) {
            if (errorCodes.getCustomSqlExceptionTranslator() != null && logger.isWarnEnabled()) {
                logger.warn("Overriding already defined custom translator '" + errorCodes.getCustomSqlExceptionTranslator().getClass().getSimpleName() + " with '" + customTranslator.getClass().getSimpleName() + "' found in the CustomSQLExceptionTranslatorRegistry for database '" + databaseName + "'");
            } else if (logger.isInfoEnabled()) {
                logger.info("Using custom translator '" + customTranslator.getClass().getSimpleName() + "' found in the CustomSQLExceptionTranslatorRegistry for database '" + databaseName + "'");
            }
            errorCodes.setCustomSqlExceptionTranslator(customTranslator);
        }
    }
}
