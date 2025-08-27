package org.springframework.jdbc.datasource.embedded;

import javax.sql.DataSource;

/* loaded from: spring-jdbc-4.3.25.RELEASE.jar:org/springframework/jdbc/datasource/embedded/EmbeddedDatabase.class */
public interface EmbeddedDatabase extends DataSource {
    void shutdown();
}
