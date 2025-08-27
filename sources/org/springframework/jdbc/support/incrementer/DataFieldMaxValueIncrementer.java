package org.springframework.jdbc.support.incrementer;

import org.springframework.dao.DataAccessException;

/* loaded from: spring-jdbc-4.3.25.RELEASE.jar:org/springframework/jdbc/support/incrementer/DataFieldMaxValueIncrementer.class */
public interface DataFieldMaxValueIncrementer {
    int nextIntValue() throws DataAccessException;

    long nextLongValue() throws DataAccessException;

    String nextStringValue() throws DataAccessException;
}
