package org.springframework.oxm;

import java.lang.reflect.Type;

/* loaded from: spring-oxm-4.3.25.RELEASE.jar:org/springframework/oxm/GenericUnmarshaller.class */
public interface GenericUnmarshaller extends Unmarshaller {
    boolean supports(Type type);
}
