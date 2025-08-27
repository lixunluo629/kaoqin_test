package org.springframework.jdbc.core;

/* loaded from: spring-jdbc-4.3.25.RELEASE.jar:org/springframework/jdbc/core/DisposableSqlTypeValue.class */
public interface DisposableSqlTypeValue extends SqlTypeValue {
    void cleanup();
}
