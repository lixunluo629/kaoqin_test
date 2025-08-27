package org.springframework.boot.autoconfigure.flyway;

import org.flywaydb.core.Flyway;

/* loaded from: spring-boot-autoconfigure-1.5.22.RELEASE.jar:org/springframework/boot/autoconfigure/flyway/FlywayMigrationStrategy.class */
public interface FlywayMigrationStrategy {
    void migrate(Flyway flyway);
}
