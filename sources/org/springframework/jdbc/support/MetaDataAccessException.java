package org.springframework.jdbc.support;

import org.springframework.core.NestedCheckedException;

/* loaded from: spring-jdbc-4.3.25.RELEASE.jar:org/springframework/jdbc/support/MetaDataAccessException.class */
public class MetaDataAccessException extends NestedCheckedException {
    public MetaDataAccessException(String msg) {
        super(msg);
    }

    public MetaDataAccessException(String msg, Throwable cause) {
        super(msg, cause);
    }
}
