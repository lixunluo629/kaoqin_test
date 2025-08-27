package org.springframework.jdbc.support.xml;

import java.io.IOException;
import java.io.Writer;

/* loaded from: spring-jdbc-4.3.25.RELEASE.jar:org/springframework/jdbc/support/xml/XmlCharacterStreamProvider.class */
public interface XmlCharacterStreamProvider {
    void provideXml(Writer writer) throws IOException;
}
