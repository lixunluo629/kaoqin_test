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
import org.apache.xmlbeans.xml.stream.XMLInputStream;
import org.apache.xmlbeans.xml.stream.XMLStreamException;
import org.w3c.dom.Node;

/* loaded from: xmlbeans-3.1.0.jar:org/apache/xmlbeans/impl/xb/xmlconfig/ConfigDocument.class */
public interface ConfigDocument extends XmlObject {
    public static final SchemaType type = (SchemaType) XmlBeans.typeSystemForClassLoader(ConfigDocument.class.getClassLoader(), "schemaorg_apache_xmlbeans.system.sXMLCONFIG").resolveHandle("config4185doctype");

    Config getConfig();

    void setConfig(Config config);

    Config addNewConfig();

    /* loaded from: xmlbeans-3.1.0.jar:org/apache/xmlbeans/impl/xb/xmlconfig/ConfigDocument$Config.class */
    public interface Config extends XmlObject {
        public static final SchemaType type = (SchemaType) XmlBeans.typeSystemForClassLoader(Config.class.getClassLoader(), "schemaorg_apache_xmlbeans.system.sXMLCONFIG").resolveHandle("configf467elemtype");

        Nsconfig[] getNamespaceArray();

        Nsconfig getNamespaceArray(int i);

        int sizeOfNamespaceArray();

        void setNamespaceArray(Nsconfig[] nsconfigArr);

        void setNamespaceArray(int i, Nsconfig nsconfig);

        Nsconfig insertNewNamespace(int i);

        Nsconfig addNewNamespace();

        void removeNamespace(int i);

        Qnameconfig[] getQnameArray();

        Qnameconfig getQnameArray(int i);

        int sizeOfQnameArray();

        void setQnameArray(Qnameconfig[] qnameconfigArr);

        void setQnameArray(int i, Qnameconfig qnameconfig);

        Qnameconfig insertNewQname(int i);

        Qnameconfig addNewQname();

        void removeQname(int i);

        Extensionconfig[] getExtensionArray();

        Extensionconfig getExtensionArray(int i);

        int sizeOfExtensionArray();

        void setExtensionArray(Extensionconfig[] extensionconfigArr);

        void setExtensionArray(int i, Extensionconfig extensionconfig);

        Extensionconfig insertNewExtension(int i);

        Extensionconfig addNewExtension();

        void removeExtension(int i);

        Usertypeconfig[] getUsertypeArray();

        Usertypeconfig getUsertypeArray(int i);

        int sizeOfUsertypeArray();

        void setUsertypeArray(Usertypeconfig[] usertypeconfigArr);

        void setUsertypeArray(int i, Usertypeconfig usertypeconfig);

        Usertypeconfig insertNewUsertype(int i);

        Usertypeconfig addNewUsertype();

        void removeUsertype(int i);

        /* loaded from: xmlbeans-3.1.0.jar:org/apache/xmlbeans/impl/xb/xmlconfig/ConfigDocument$Config$Factory.class */
        public static final class Factory {
            public static Config newInstance() {
                return (Config) XmlBeans.getContextTypeLoader().newInstance(Config.type, null);
            }

            public static Config newInstance(XmlOptions options) {
                return (Config) XmlBeans.getContextTypeLoader().newInstance(Config.type, options);
            }

            private Factory() {
            }
        }
    }

    /* loaded from: xmlbeans-3.1.0.jar:org/apache/xmlbeans/impl/xb/xmlconfig/ConfigDocument$Factory.class */
    public static final class Factory {
        public static ConfigDocument newInstance() {
            return (ConfigDocument) XmlBeans.getContextTypeLoader().newInstance(ConfigDocument.type, null);
        }

        public static ConfigDocument newInstance(XmlOptions options) {
            return (ConfigDocument) XmlBeans.getContextTypeLoader().newInstance(ConfigDocument.type, options);
        }

        public static ConfigDocument parse(String xmlAsString) throws XmlException {
            return (ConfigDocument) XmlBeans.getContextTypeLoader().parse(xmlAsString, ConfigDocument.type, (XmlOptions) null);
        }

        public static ConfigDocument parse(String xmlAsString, XmlOptions options) throws XmlException {
            return (ConfigDocument) XmlBeans.getContextTypeLoader().parse(xmlAsString, ConfigDocument.type, options);
        }

        public static ConfigDocument parse(File file) throws XmlException, IOException {
            return (ConfigDocument) XmlBeans.getContextTypeLoader().parse(file, ConfigDocument.type, (XmlOptions) null);
        }

        public static ConfigDocument parse(File file, XmlOptions options) throws XmlException, IOException {
            return (ConfigDocument) XmlBeans.getContextTypeLoader().parse(file, ConfigDocument.type, options);
        }

        public static ConfigDocument parse(URL u) throws XmlException, IOException {
            return (ConfigDocument) XmlBeans.getContextTypeLoader().parse(u, ConfigDocument.type, (XmlOptions) null);
        }

        public static ConfigDocument parse(URL u, XmlOptions options) throws XmlException, IOException {
            return (ConfigDocument) XmlBeans.getContextTypeLoader().parse(u, ConfigDocument.type, options);
        }

        public static ConfigDocument parse(InputStream is) throws XmlException, IOException {
            return (ConfigDocument) XmlBeans.getContextTypeLoader().parse(is, ConfigDocument.type, (XmlOptions) null);
        }

        public static ConfigDocument parse(InputStream is, XmlOptions options) throws XmlException, IOException {
            return (ConfigDocument) XmlBeans.getContextTypeLoader().parse(is, ConfigDocument.type, options);
        }

        public static ConfigDocument parse(Reader r) throws XmlException, IOException {
            return (ConfigDocument) XmlBeans.getContextTypeLoader().parse(r, ConfigDocument.type, (XmlOptions) null);
        }

        public static ConfigDocument parse(Reader r, XmlOptions options) throws XmlException, IOException {
            return (ConfigDocument) XmlBeans.getContextTypeLoader().parse(r, ConfigDocument.type, options);
        }

        public static ConfigDocument parse(XMLStreamReader sr) throws XmlException {
            return (ConfigDocument) XmlBeans.getContextTypeLoader().parse(sr, ConfigDocument.type, (XmlOptions) null);
        }

        public static ConfigDocument parse(XMLStreamReader sr, XmlOptions options) throws XmlException {
            return (ConfigDocument) XmlBeans.getContextTypeLoader().parse(sr, ConfigDocument.type, options);
        }

        public static ConfigDocument parse(Node node) throws XmlException {
            return (ConfigDocument) XmlBeans.getContextTypeLoader().parse(node, ConfigDocument.type, (XmlOptions) null);
        }

        public static ConfigDocument parse(Node node, XmlOptions options) throws XmlException {
            return (ConfigDocument) XmlBeans.getContextTypeLoader().parse(node, ConfigDocument.type, options);
        }

        public static ConfigDocument parse(XMLInputStream xis) throws XMLStreamException, XmlException {
            return (ConfigDocument) XmlBeans.getContextTypeLoader().parse(xis, ConfigDocument.type, (XmlOptions) null);
        }

        public static ConfigDocument parse(XMLInputStream xis, XmlOptions options) throws XMLStreamException, XmlException {
            return (ConfigDocument) XmlBeans.getContextTypeLoader().parse(xis, ConfigDocument.type, options);
        }

        public static XMLInputStream newValidatingXMLInputStream(XMLInputStream xis) throws XMLStreamException, XmlException {
            return XmlBeans.getContextTypeLoader().newValidatingXMLInputStream(xis, ConfigDocument.type, null);
        }

        public static XMLInputStream newValidatingXMLInputStream(XMLInputStream xis, XmlOptions options) throws XMLStreamException, XmlException {
            return XmlBeans.getContextTypeLoader().newValidatingXMLInputStream(xis, ConfigDocument.type, options);
        }

        private Factory() {
        }
    }
}
