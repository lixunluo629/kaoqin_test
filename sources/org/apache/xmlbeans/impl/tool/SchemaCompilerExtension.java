package org.apache.xmlbeans.impl.tool;

import java.util.Map;
import org.apache.xmlbeans.SchemaTypeSystem;

/* loaded from: xmlbeans-3.1.0.jar:org/apache/xmlbeans/impl/tool/SchemaCompilerExtension.class */
public interface SchemaCompilerExtension {
    void schemaCompilerExtension(SchemaTypeSystem schemaTypeSystem, Map map);

    String getExtensionName();
}
