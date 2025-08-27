package org.springframework.oxm.mime;

import java.io.IOException;
import javax.xml.transform.Source;
import org.springframework.oxm.Unmarshaller;
import org.springframework.oxm.XmlMappingException;

/* loaded from: spring-oxm-4.3.25.RELEASE.jar:org/springframework/oxm/mime/MimeUnmarshaller.class */
public interface MimeUnmarshaller extends Unmarshaller {
    Object unmarshal(Source source, MimeContainer mimeContainer) throws XmlMappingException, IOException;
}
