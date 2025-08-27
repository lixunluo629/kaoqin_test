package org.springframework.boot.autoconfigure.session;

import javax.sql.DataSource;
import org.springframework.boot.autoconfigure.AbstractDatabaseInitializer;
import org.springframework.boot.autoconfigure.session.SessionProperties;
import org.springframework.core.io.ResourceLoader;
import org.springframework.util.Assert;

/* loaded from: spring-boot-autoconfigure-1.5.22.RELEASE.jar:org/springframework/boot/autoconfigure/session/JdbcSessionDatabaseInitializer.class */
public class JdbcSessionDatabaseInitializer extends AbstractDatabaseInitializer {
    private final SessionProperties.Jdbc properties;

    public JdbcSessionDatabaseInitializer(DataSource dataSource, ResourceLoader resourceLoader, SessionProperties properties) {
        super(dataSource, resourceLoader);
        Assert.notNull(properties, "SessionProperties must not be null");
        this.properties = properties.getJdbc();
    }

    @Override // org.springframework.boot.autoconfigure.AbstractDatabaseInitializer
    protected boolean isEnabled() {
        return this.properties.getInitializer().isEnabled();
    }

    @Override // org.springframework.boot.autoconfigure.AbstractDatabaseInitializer
    protected String getSchemaLocation() {
        return this.properties.getSchema();
    }
}
