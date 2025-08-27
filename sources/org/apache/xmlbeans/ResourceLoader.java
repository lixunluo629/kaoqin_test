package org.apache.xmlbeans;

import java.io.InputStream;

/* loaded from: xmlbeans-3.1.0.jar:org/apache/xmlbeans/ResourceLoader.class */
public interface ResourceLoader {
    InputStream getResourceAsStream(String str);

    void close();
}
