package org.springframework.boot.autoconfigure.jdbc;

import javax.sql.DataSource;
import org.springframework.context.ApplicationEvent;

/* loaded from: spring-boot-autoconfigure-1.5.22.RELEASE.jar:org/springframework/boot/autoconfigure/jdbc/DataSourceInitializedEvent.class */
public class DataSourceInitializedEvent extends ApplicationEvent {
    public DataSourceInitializedEvent(DataSource source) {
        super(source);
    }
}
