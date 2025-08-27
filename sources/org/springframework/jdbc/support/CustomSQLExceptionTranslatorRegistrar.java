package org.springframework.jdbc.support;

import java.util.HashMap;
import java.util.Map;
import org.springframework.beans.factory.InitializingBean;

/* loaded from: spring-jdbc-4.3.25.RELEASE.jar:org/springframework/jdbc/support/CustomSQLExceptionTranslatorRegistrar.class */
public class CustomSQLExceptionTranslatorRegistrar implements InitializingBean {
    private final Map<String, SQLExceptionTranslator> translators = new HashMap();

    public void setTranslators(Map<String, SQLExceptionTranslator> translators) {
        this.translators.putAll(translators);
    }

    @Override // org.springframework.beans.factory.InitializingBean
    public void afterPropertiesSet() {
        for (String dbName : this.translators.keySet()) {
            CustomSQLExceptionTranslatorRegistry.getInstance().registerTranslator(dbName, this.translators.get(dbName));
        }
    }
}
