package org.springframework.jdbc.support.xml;

import org.springframework.dao.InvalidDataAccessApiUsageException;

/* loaded from: spring-jdbc-4.3.25.RELEASE.jar:org/springframework/jdbc/support/xml/SqlXmlFeatureNotImplementedException.class */
public class SqlXmlFeatureNotImplementedException extends InvalidDataAccessApiUsageException {
    public SqlXmlFeatureNotImplementedException(String msg) {
        super(msg);
    }

    public SqlXmlFeatureNotImplementedException(String msg, Throwable cause) {
        super(msg, cause);
    }
}
