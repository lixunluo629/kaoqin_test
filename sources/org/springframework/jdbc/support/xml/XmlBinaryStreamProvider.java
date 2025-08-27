package org.springframework.jdbc.support.xml;

import java.io.IOException;
import java.io.OutputStream;

/* loaded from: spring-jdbc-4.3.25.RELEASE.jar:org/springframework/jdbc/support/xml/XmlBinaryStreamProvider.class */
public interface XmlBinaryStreamProvider {
    void provideXml(OutputStream outputStream) throws IOException;
}
