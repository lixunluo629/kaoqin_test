package org.springframework.oxm;

import java.lang.reflect.Type;

/* loaded from: spring-oxm-4.3.25.RELEASE.jar:org/springframework/oxm/GenericMarshaller.class */
public interface GenericMarshaller extends Marshaller {
    boolean supports(Type type);
}
