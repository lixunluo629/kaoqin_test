package org.springframework.oxm;

import org.springframework.core.NestedRuntimeException;

/* loaded from: spring-oxm-4.3.25.RELEASE.jar:org/springframework/oxm/XmlMappingException.class */
public abstract class XmlMappingException extends NestedRuntimeException {
    public XmlMappingException(String msg) {
        super(msg);
    }

    public XmlMappingException(String msg, Throwable cause) {
        super(msg, cause);
    }
}
