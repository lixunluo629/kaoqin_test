package javax.xml.namespace;

import java.util.Iterator;

/* loaded from: stax-api-1.0.1.jar:javax/xml/namespace/NamespaceContext.class */
public interface NamespaceContext {
    String getNamespaceURI(String str);

    String getPrefix(String str);

    Iterator getPrefixes(String str);
}
