package org.apache.xmlbeans;

import java.io.IOException;
import java.io.Writer;

/* loaded from: xmlbeans-3.1.0.jar:org/apache/xmlbeans/SchemaCodePrinter.class */
public interface SchemaCodePrinter {
    void printTypeImpl(Writer writer, SchemaType schemaType) throws IOException;

    void printType(Writer writer, SchemaType schemaType) throws IOException;

    void printLoader(Writer writer, SchemaTypeSystem schemaTypeSystem) throws IOException;
}
