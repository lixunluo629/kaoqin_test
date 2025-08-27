package org.springframework.boot.autoconfigure.jooq;

import org.jooq.SQLDialect;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "spring.jooq")
/* loaded from: spring-boot-autoconfigure-1.5.22.RELEASE.jar:org/springframework/boot/autoconfigure/jooq/JooqProperties.class */
public class JooqProperties {
    private SQLDialect sqlDialect;

    public SQLDialect getSqlDialect() {
        return this.sqlDialect;
    }

    public void setSqlDialect(SQLDialect sqlDialect) {
        this.sqlDialect = sqlDialect;
    }
}
