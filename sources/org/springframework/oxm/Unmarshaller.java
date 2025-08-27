package org.springframework.oxm;

import java.io.IOException;
import javax.xml.transform.Source;

/* loaded from: spring-oxm-4.3.25.RELEASE.jar:org/springframework/oxm/Unmarshaller.class */
public interface Unmarshaller {
    boolean supports(Class<?> cls);

    Object unmarshal(Source source) throws XmlMappingException, IOException;
}
