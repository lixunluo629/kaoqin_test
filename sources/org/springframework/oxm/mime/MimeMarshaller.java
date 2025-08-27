package org.springframework.oxm.mime;

import java.io.IOException;
import javax.xml.transform.Result;
import org.springframework.oxm.Marshaller;
import org.springframework.oxm.XmlMappingException;

/* loaded from: spring-oxm-4.3.25.RELEASE.jar:org/springframework/oxm/mime/MimeMarshaller.class */
public interface MimeMarshaller extends Marshaller {
    void marshal(Object obj, Result result, MimeContainer mimeContainer) throws XmlMappingException, IOException;
}
