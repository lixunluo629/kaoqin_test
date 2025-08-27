package org.springframework.oxm;

import java.io.IOException;
import javax.xml.transform.Result;

/* loaded from: spring-oxm-4.3.25.RELEASE.jar:org/springframework/oxm/Marshaller.class */
public interface Marshaller {
    boolean supports(Class<?> cls);

    void marshal(Object obj, Result result) throws XmlMappingException, IOException;
}
