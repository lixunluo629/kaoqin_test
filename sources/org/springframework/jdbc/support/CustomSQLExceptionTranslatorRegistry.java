package org.springframework.jdbc.support;

import java.util.HashMap;
import java.util.Map;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/* loaded from: spring-jdbc-4.3.25.RELEASE.jar:org/springframework/jdbc/support/CustomSQLExceptionTranslatorRegistry.class */
public class CustomSQLExceptionTranslatorRegistry {
    private static final Log logger = LogFactory.getLog(CustomSQLExceptionTranslatorRegistry.class);
    private static final CustomSQLExceptionTranslatorRegistry instance = new CustomSQLExceptionTranslatorRegistry();
    private final Map<String, SQLExceptionTranslator> translatorMap = new HashMap();

    public static CustomSQLExceptionTranslatorRegistry getInstance() {
        return instance;
    }

    private CustomSQLExceptionTranslatorRegistry() {
    }

    public void registerTranslator(String dbName, SQLExceptionTranslator translator) {
        SQLExceptionTranslator replaced = this.translatorMap.put(dbName, translator);
        if (logger.isInfoEnabled()) {
            if (replaced != null) {
                logger.info("Replacing custom translator [" + replaced + "] for database '" + dbName + "' with [" + translator + "]");
            } else {
                logger.info("Adding custom translator of type [" + translator.getClass().getName() + "] for database '" + dbName + "'");
            }
        }
    }

    public SQLExceptionTranslator findTranslatorForDatabase(String dbName) {
        return this.translatorMap.get(dbName);
    }
}
