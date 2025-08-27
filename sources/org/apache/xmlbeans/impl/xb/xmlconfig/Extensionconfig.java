package org.apache.xmlbeans.impl.xb.xmlconfig;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.net.URL;
import javax.xml.stream.XMLStreamReader;
import org.apache.xmlbeans.SchemaType;
import org.apache.xmlbeans.XmlBeans;
import org.apache.xmlbeans.XmlException;
import org.apache.xmlbeans.XmlObject;
import org.apache.xmlbeans.XmlOptions;
import org.apache.xmlbeans.XmlString;
import org.apache.xmlbeans.xml.stream.XMLInputStream;
import org.apache.xmlbeans.xml.stream.XMLStreamException;
import org.w3c.dom.Node;

/* loaded from: xmlbeans-3.1.0.jar:org/apache/xmlbeans/impl/xb/xmlconfig/Extensionconfig.class */
public interface Extensionconfig extends XmlObject {
    public static final SchemaType type = (SchemaType) XmlBeans.typeSystemForClassLoader(Extensionconfig.class.getClassLoader(), "schemaorg_apache_xmlbeans.system.sXMLCONFIG").resolveHandle("extensionconfig2ac2type");

    Interface[] getInterfaceArray();

    Interface getInterfaceArray(int i);

    int sizeOfInterfaceArray();

    void setInterfaceArray(Interface[] interfaceArr);

    void setInterfaceArray(int i, Interface r2);

    Interface insertNewInterface(int i);

    Interface addNewInterface();

    void removeInterface(int i);

    PrePostSet getPrePostSet();

    boolean isSetPrePostSet();

    void setPrePostSet(PrePostSet prePostSet);

    PrePostSet addNewPrePostSet();

    void unsetPrePostSet();

    Object getFor();

    JavaNameList xgetFor();

    boolean isSetFor();

    void setFor(Object obj);

    void xsetFor(JavaNameList javaNameList);

    void unsetFor();

    /* loaded from: xmlbeans-3.1.0.jar:org/apache/xmlbeans/impl/xb/xmlconfig/Extensionconfig$Interface.class */
    public interface Interface extends XmlObject {
        public static final SchemaType type = (SchemaType) XmlBeans.typeSystemForClassLoader(Interface.class.getClassLoader(), "schemaorg_apache_xmlbeans.system.sXMLCONFIG").resolveHandle("interface02a7elemtype");

        String getStaticHandler();

        XmlString xgetStaticHandler();

        void setStaticHandler(String str);

        void xsetStaticHandler(XmlString xmlString);

        String getName();

        XmlString xgetName();

        boolean isSetName();

        void setName(String str);

        void xsetName(XmlString xmlString);

        void unsetName();

        /* loaded from: xmlbeans-3.1.0.jar:org/apache/xmlbeans/impl/xb/xmlconfig/Extensionconfig$Interface$Factory.class */
        public static final class Factory {
            public static Interface newInstance() {
                return (Interface) XmlBeans.getContextTypeLoader().newInstance(Interface.type, null);
            }

            public static Interface newInstance(XmlOptions options) {
                return (Interface) XmlBeans.getContextTypeLoader().newInstance(Interface.type, options);
            }

            private Factory() {
            }
        }
    }

    /* loaded from: xmlbeans-3.1.0.jar:org/apache/xmlbeans/impl/xb/xmlconfig/Extensionconfig$PrePostSet.class */
    public interface PrePostSet extends XmlObject {
        public static final SchemaType type = (SchemaType) XmlBeans.typeSystemForClassLoader(PrePostSet.class.getClassLoader(), "schemaorg_apache_xmlbeans.system.sXMLCONFIG").resolveHandle("prepostset5c9delemtype");

        String getStaticHandler();

        XmlString xgetStaticHandler();

        void setStaticHandler(String str);

        void xsetStaticHandler(XmlString xmlString);

        /* loaded from: xmlbeans-3.1.0.jar:org/apache/xmlbeans/impl/xb/xmlconfig/Extensionconfig$PrePostSet$Factory.class */
        public static final class Factory {
            public static PrePostSet newInstance() {
                return (PrePostSet) XmlBeans.getContextTypeLoader().newInstance(PrePostSet.type, null);
            }

            public static PrePostSet newInstance(XmlOptions options) {
                return (PrePostSet) XmlBeans.getContextTypeLoader().newInstance(PrePostSet.type, options);
            }

            private Factory() {
            }
        }
    }

    /* loaded from: xmlbeans-3.1.0.jar:org/apache/xmlbeans/impl/xb/xmlconfig/Extensionconfig$Factory.class */
    public static final class Factory {
        public static Extensionconfig newInstance() {
            return (Extensionconfig) XmlBeans.getContextTypeLoader().newInstance(Extensionconfig.type, null);
        }

        public static Extensionconfig newInstance(XmlOptions options) {
            return (Extensionconfig) XmlBeans.getContextTypeLoader().newInstance(Extensionconfig.type, options);
        }

        public static Extensionconfig parse(String xmlAsString) throws XmlException {
            return (Extensionconfig) XmlBeans.getContextTypeLoader().parse(xmlAsString, Extensionconfig.type, (XmlOptions) null);
        }

        public static Extensionconfig parse(String xmlAsString, XmlOptions options) throws XmlException {
            return (Extensionconfig) XmlBeans.getContextTypeLoader().parse(xmlAsString, Extensionconfig.type, options);
        }

        public static Extensionconfig parse(File file) throws XmlException, IOException {
            return (Extensionconfig) XmlBeans.getContextTypeLoader().parse(file, Extensionconfig.type, (XmlOptions) null);
        }

        public static Extensionconfig parse(File file, XmlOptions options) throws XmlException, IOException {
            return (Extensionconfig) XmlBeans.getContextTypeLoader().parse(file, Extensionconfig.type, options);
        }

        public static Extensionconfig parse(URL u) throws XmlException, IOException {
            return (Extensionconfig) XmlBeans.getContextTypeLoader().parse(u, Extensionconfig.type, (XmlOptions) null);
        }

        public static Extensionconfig parse(URL u, XmlOptions options) throws XmlException, IOException {
            return (Extensionconfig) XmlBeans.getContextTypeLoader().parse(u, Extensionconfig.type, options);
        }

        public static Extensionconfig parse(InputStream is) throws XmlException, IOException {
            return (Extensionconfig) XmlBeans.getContextTypeLoader().parse(is, Extensionconfig.type, (XmlOptions) null);
        }

        public static Extensionconfig parse(InputStream is, XmlOptions options) throws XmlException, IOException {
            return (Extensionconfig) XmlBeans.getContextTypeLoader().parse(is, Extensionconfig.type, options);
        }

        public static Extensionconfig parse(Reader r) throws XmlException, IOException {
            return (Extensionconfig) XmlBeans.getContextTypeLoader().parse(r, Extensionconfig.type, (XmlOptions) null);
        }

        public static Extensionconfig parse(Reader r, XmlOptions options) throws XmlException, IOException {
            return (Extensionconfig) XmlBeans.getContextTypeLoader().parse(r, Extensionconfig.type, options);
        }

        public static Extensionconfig parse(XMLStreamReader sr) throws XmlException {
            return (Extensionconfig) XmlBeans.getContextTypeLoader().parse(sr, Extensionconfig.type, (XmlOptions) null);
        }

        public static Extensionconfig parse(XMLStreamReader sr, XmlOptions options) throws XmlException {
            return (Extensionconfig) XmlBeans.getContextTypeLoader().parse(sr, Extensionconfig.type, options);
        }

        public static Extensionconfig parse(Node node) throws XmlException {
            return (Extensionconfig) XmlBeans.getContextTypeLoader().parse(node, Extensionconfig.type, (XmlOptions) null);
        }

        public static Extensionconfig parse(Node node, XmlOptions options) throws XmlException {
            return (Extensionconfig) XmlBeans.getContextTypeLoader().parse(node, Extensionconfig.type, options);
        }

        public static Extensionconfig parse(XMLInputStream xis) throws XMLStreamException, XmlException {
            return (Extensionconfig) XmlBeans.getContextTypeLoader().parse(xis, Extensionconfig.type, (XmlOptions) null);
        }

        public static Extensionconfig parse(XMLInputStream xis, XmlOptions options) throws XMLStreamException, XmlException {
            return (Extensionconfig) XmlBeans.getContextTypeLoader().parse(xis, Extensionconfig.type, options);
        }

        public static XMLInputStream newValidatingXMLInputStream(XMLInputStream xis) throws XMLStreamException, XmlException {
            return XmlBeans.getContextTypeLoader().newValidatingXMLInputStream(xis, Extensionconfig.type, null);
        }

        public static XMLInputStream newValidatingXMLInputStream(XMLInputStream xis, XmlOptions options) throws XMLStreamException, XmlException {
            return XmlBeans.getContextTypeLoader().newValidatingXMLInputStream(xis, Extensionconfig.type, options);
        }

        private Factory() {
        }
    }
}
