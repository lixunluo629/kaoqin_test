package org.springframework.jca.cci;

import javax.resource.ResourceException;
import org.springframework.dao.DataAccessResourceFailureException;

/* loaded from: spring-tx-4.3.25.RELEASE.jar:org/springframework/jca/cci/CannotGetCciConnectionException.class */
public class CannotGetCciConnectionException extends DataAccessResourceFailureException {
    public CannotGetCciConnectionException(String msg, ResourceException ex) {
        super(msg, ex);
    }
}
