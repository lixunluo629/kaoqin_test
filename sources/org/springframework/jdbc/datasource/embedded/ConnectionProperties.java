package org.springframework.jdbc.datasource.embedded;

import java.sql.Driver;

/* loaded from: spring-jdbc-4.3.25.RELEASE.jar:org/springframework/jdbc/datasource/embedded/ConnectionProperties.class */
public interface ConnectionProperties {
    void setDriverClass(Class<? extends Driver> cls);

    void setUrl(String str);

    void setUsername(String str);

    void setPassword(String str);
}
