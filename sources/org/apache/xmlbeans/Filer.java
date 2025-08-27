package org.apache.xmlbeans;

import java.io.IOException;
import java.io.OutputStream;
import java.io.Writer;

/* loaded from: xmlbeans-3.1.0.jar:org/apache/xmlbeans/Filer.class */
public interface Filer {
    OutputStream createBinaryFile(String str) throws IOException;

    Writer createSourceFile(String str) throws IOException;
}
