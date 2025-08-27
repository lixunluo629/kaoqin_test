package org.springframework.jca.cci;

import javax.resource.ResourceException;
import org.springframework.dao.InvalidDataAccessResourceUsageException;

/* loaded from: spring-tx-4.3.25.RELEASE.jar:org/springframework/jca/cci/CciOperationNotSupportedException.class */
public class CciOperationNotSupportedException extends InvalidDataAccessResourceUsageException {
    public CciOperationNotSupportedException(String msg, ResourceException ex) {
        super(msg, ex);
    }
}
