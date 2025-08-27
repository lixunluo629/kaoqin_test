package org.springframework.jdbc.datasource.embedded;

import org.springframework.util.Assert;

/* loaded from: spring-jdbc-4.3.25.RELEASE.jar:org/springframework/jdbc/datasource/embedded/EmbeddedDatabaseConfigurerFactory.class */
final class EmbeddedDatabaseConfigurerFactory {
    EmbeddedDatabaseConfigurerFactory() {
    }

    public static EmbeddedDatabaseConfigurer getConfigurer(EmbeddedDatabaseType type) throws IllegalStateException {
        Assert.notNull(type, "EmbeddedDatabaseType is required");
        try {
            switch (type) {
                case HSQL:
                    return HsqlEmbeddedDatabaseConfigurer.getInstance();
                case H2:
                    return H2EmbeddedDatabaseConfigurer.getInstance();
                case DERBY:
                    return DerbyEmbeddedDatabaseConfigurer.getInstance();
                default:
                    throw new UnsupportedOperationException("Embedded database type [" + type + "] is not supported");
            }
        } catch (ClassNotFoundException ex) {
            throw new IllegalStateException("Driver for test database type [" + type + "] is not available", ex);
        } catch (NoClassDefFoundError err) {
            throw new IllegalStateException("Driver for test database type [" + type + "] is not available", err);
        }
    }
}
