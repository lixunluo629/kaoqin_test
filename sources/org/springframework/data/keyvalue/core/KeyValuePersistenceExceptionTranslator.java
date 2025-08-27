package org.springframework.data.keyvalue.core;

import java.util.NoSuchElementException;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DataRetrievalFailureException;
import org.springframework.dao.support.PersistenceExceptionTranslator;

/* loaded from: spring-data-keyvalue-1.2.23.RELEASE.jar:org/springframework/data/keyvalue/core/KeyValuePersistenceExceptionTranslator.class */
public class KeyValuePersistenceExceptionTranslator implements PersistenceExceptionTranslator {
    @Override // org.springframework.dao.support.PersistenceExceptionTranslator
    public DataAccessException translateExceptionIfPossible(RuntimeException e) {
        if (e == null || (e instanceof DataAccessException)) {
            return (DataAccessException) e;
        }
        if ((e instanceof NoSuchElementException) || (e instanceof IndexOutOfBoundsException) || (e instanceof IllegalStateException)) {
            return new DataRetrievalFailureException(e.getMessage(), e);
        }
        if (e.getClass().getName().startsWith("java")) {
            return new UncategorizedKeyValueException(e.getMessage(), e);
        }
        return null;
    }
}
