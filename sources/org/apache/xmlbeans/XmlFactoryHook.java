package org.apache.xmlbeans;

import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.lang.ref.SoftReference;
import javax.xml.stream.XMLStreamReader;
import org.apache.xmlbeans.xml.stream.XMLInputStream;
import org.apache.xmlbeans.xml.stream.XMLStreamException;
import org.w3c.dom.DOMImplementation;
import org.w3c.dom.Node;

/* loaded from: xmlbeans-3.1.0.jar:org/apache/xmlbeans/XmlFactoryHook.class */
public interface XmlFactoryHook {
    XmlObject newInstance(SchemaTypeLoader schemaTypeLoader, SchemaType schemaType, XmlOptions xmlOptions);

    XmlObject parse(SchemaTypeLoader schemaTypeLoader, String str, SchemaType schemaType, XmlOptions xmlOptions) throws XmlException;

    XmlObject parse(SchemaTypeLoader schemaTypeLoader, InputStream inputStream, SchemaType schemaType, XmlOptions xmlOptions) throws XmlException, IOException;

    XmlObject parse(SchemaTypeLoader schemaTypeLoader, XMLStreamReader xMLStreamReader, SchemaType schemaType, XmlOptions xmlOptions) throws XmlException;

    XmlObject parse(SchemaTypeLoader schemaTypeLoader, Reader reader, SchemaType schemaType, XmlOptions xmlOptions) throws XmlException, IOException;

    XmlObject parse(SchemaTypeLoader schemaTypeLoader, Node node, SchemaType schemaType, XmlOptions xmlOptions) throws XmlException;

    XmlObject parse(SchemaTypeLoader schemaTypeLoader, XMLInputStream xMLInputStream, SchemaType schemaType, XmlOptions xmlOptions) throws XMLStreamException, XmlException;

    XmlSaxHandler newXmlSaxHandler(SchemaTypeLoader schemaTypeLoader, SchemaType schemaType, XmlOptions xmlOptions);

    DOMImplementation newDomImplementation(SchemaTypeLoader schemaTypeLoader, XmlOptions xmlOptions);

    /* loaded from: xmlbeans-3.1.0.jar:org/apache/xmlbeans/XmlFactoryHook$ThreadContext.class */
    public static final class ThreadContext {
        private static ThreadLocal threadHook = new ThreadLocal();

        public static void clearThreadLocals() {
            threadHook.remove();
        }

        public static XmlFactoryHook getHook() {
            SoftReference softRef = (SoftReference) threadHook.get();
            if (softRef == null) {
                return null;
            }
            return (XmlFactoryHook) softRef.get();
        }

        public static void setHook(XmlFactoryHook hook) {
            threadHook.set(new SoftReference(hook));
        }

        private ThreadContext() {
        }
    }
}
