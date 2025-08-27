package org.springframework.data.keyvalue.core;

import org.springframework.dao.UncategorizedDataAccessException;

/* loaded from: spring-data-keyvalue-1.2.23.RELEASE.jar:org/springframework/data/keyvalue/core/UncategorizedKeyValueException.class */
public class UncategorizedKeyValueException extends UncategorizedDataAccessException {
    private static final long serialVersionUID = -8087116071859122297L;

    public UncategorizedKeyValueException(String msg, Throwable cause) {
        super(msg, cause);
    }
}
