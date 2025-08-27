package org.apache.xmlbeans.impl.xb.substwsdl;

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

/* loaded from: xmlbeans-3.1.0.jar:org/apache/xmlbeans/impl/xb/substwsdl/DefinitionsDocument.class */
public interface DefinitionsDocument extends XmlObject {
    public static final SchemaType type = (SchemaType) XmlBeans.typeSystemForClassLoader(DefinitionsDocument.class.getClassLoader(), "schemaorg_apache_xmlbeans.system.sXMLTOOLS").resolveHandle("definitionsc7f1doctype");

    Definitions getDefinitions();

    void setDefinitions(Definitions definitions);

    Definitions addNewDefinitions();

    /* loaded from: xmlbeans-3.1.0.jar:org/apache/xmlbeans/impl/xb/substwsdl/DefinitionsDocument$Definitions.class */
    public interface Definitions extends XmlObject {
        public static final SchemaType type = (SchemaType) XmlBeans.typeSystemForClassLoader(Definitions.class.getClassLoader(), "schemaorg_apache_xmlbeans.system.sXMLTOOLS").resolveHandle("definitions05ddelemtype");

        TImport[] getImportArray();

        TImport getImportArray(int i);

        int sizeOfImportArray();

        void setImportArray(TImport[] tImportArr);

        void setImportArray(int i, TImport tImport);

        TImport insertNewImport(int i);

        TImport addNewImport();

        void removeImport(int i);

        XmlObject[] getTypesArray();

        XmlObject getTypesArray(int i);

        int sizeOfTypesArray();

        void setTypesArray(XmlObject[] xmlObjectArr);

        void setTypesArray(int i, XmlObject xmlObject);

        XmlObject insertNewTypes(int i);

        XmlObject addNewTypes();

        void removeTypes(int i);

        XmlObject[] getMessageArray();

        XmlObject getMessageArray(int i);

        int sizeOfMessageArray();

        void setMessageArray(XmlObject[] xmlObjectArr);

        void setMessageArray(int i, XmlObject xmlObject);

        XmlObject insertNewMessage(int i);

        XmlObject addNewMessage();

        void removeMessage(int i);

        XmlObject[] getBindingArray();

        XmlObject getBindingArray(int i);

        int sizeOfBindingArray();

        void setBindingArray(XmlObject[] xmlObjectArr);

        void setBindingArray(int i, XmlObject xmlObject);

        XmlObject insertNewBinding(int i);

        XmlObject addNewBinding();

        void removeBinding(int i);

        XmlObject[] getPortTypeArray();

        XmlObject getPortTypeArray(int i);

        int sizeOfPortTypeArray();

        void setPortTypeArray(XmlObject[] xmlObjectArr);

        void setPortTypeArray(int i, XmlObject xmlObject);

        XmlObject insertNewPortType(int i);

        XmlObject addNewPortType();

        void removePortType(int i);

        XmlObject[] getServiceArray();

        XmlObject getServiceArray(int i);

        int sizeOfServiceArray();

        void setServiceArray(XmlObject[] xmlObjectArr);

        void setServiceArray(int i, XmlObject xmlObject);

        XmlObject insertNewService(int i);

        XmlObject addNewService();

        void removeService(int i);

        /* loaded from: xmlbeans-3.1.0.jar:org/apache/xmlbeans/impl/xb/substwsdl/DefinitionsDocument$Definitions$Factory.class */
        public static final class Factory {
            public static Definitions newInstance() {
                return (Definitions) XmlBeans.getContextTypeLoader().newInstance(Definitions.type, null);
            }

            public static Definitions newInstance(XmlOptions options) {
                return (Definitions) XmlBeans.getContextTypeLoader().newInstance(Definitions.type, options);
            }

            private Factory() {
            }
        }
    }

    /* loaded from: xmlbeans-3.1.0.jar:org/apache/xmlbeans/impl/xb/substwsdl/DefinitionsDocument$Factory.class */
    public static final class Factory {
        public static DefinitionsDocument newInstance() {
            return (DefinitionsDocument) XmlBeans.getContextTypeLoader().newInstance(DefinitionsDocument.type, null);
        }

        public static DefinitionsDocument newInstance(XmlOptions options) {
            return (DefinitionsDocument) XmlBeans.getContextTypeLoader().newInstance(DefinitionsDocument.type, options);
        }

        public static DefinitionsDocument parse(String xmlAsString) throws XmlException {
            return (DefinitionsDocument) XmlBeans.getContextTypeLoader().parse(xmlAsString, DefinitionsDocument.type, (XmlOptions) null);
        }

        public static DefinitionsDocument parse(String xmlAsString, XmlOptions options) throws XmlException {
            return (DefinitionsDocument) XmlBeans.getContextTypeLoader().parse(xmlAsString, DefinitionsDocument.type, options);
        }

        public static DefinitionsDocument parse(File file) throws XmlException, IOException {
            return (DefinitionsDocument) XmlBeans.getContextTypeLoader().parse(file, DefinitionsDocument.type, (XmlOptions) null);
        }

        public static DefinitionsDocument parse(File file, XmlOptions options) throws XmlException, IOException {
            return (DefinitionsDocument) XmlBeans.getContextTypeLoader().parse(file, DefinitionsDocument.type, options);
        }

        public static DefinitionsDocument parse(URL u) throws XmlException, IOException {
            return (DefinitionsDocument) XmlBeans.getContextTypeLoader().parse(u, DefinitionsDocument.type, (XmlOptions) null);
        }

        public static DefinitionsDocument parse(URL u, XmlOptions options) throws XmlException, IOException {
            return (DefinitionsDocument) XmlBeans.getContextTypeLoader().parse(u, DefinitionsDocument.type, options);
        }

        public static DefinitionsDocument parse(InputStream is) throws XmlException, IOException {
            return (DefinitionsDocument) XmlBeans.getContextTypeLoader().parse(is, DefinitionsDocument.type, (XmlOptions) null);
        }

        public static DefinitionsDocument parse(InputStream is, XmlOptions options) throws XmlException, IOException {
            return (DefinitionsDocument) XmlBeans.getContextTypeLoader().parse(is, DefinitionsDocument.type, options);
        }

        public static DefinitionsDocument parse(Reader r) throws XmlException, IOException {
            return (DefinitionsDocument) XmlBeans.getContextTypeLoader().parse(r, DefinitionsDocument.type, (XmlOptions) null);
        }

        public static DefinitionsDocument parse(Reader r, XmlOptions options) throws XmlException, IOException {
            return (DefinitionsDocument) XmlBeans.getContextTypeLoader().parse(r, DefinitionsDocument.type, options);
        }

        public static DefinitionsDocument parse(XMLStreamReader sr) throws XmlException {
            return (DefinitionsDocument) XmlBeans.getContextTypeLoader().parse(sr, DefinitionsDocument.type, (XmlOptions) null);
        }

        public static DefinitionsDocument parse(XMLStreamReader sr, XmlOptions options) throws XmlException {
            return (DefinitionsDocument) XmlBeans.getContextTypeLoader().parse(sr, DefinitionsDocument.type, options);
        }

        public static DefinitionsDocument parse(Node node) throws XmlException {
            return (DefinitionsDocument) XmlBeans.getContextTypeLoader().parse(node, DefinitionsDocument.type, (XmlOptions) null);
        }

        public static DefinitionsDocument parse(Node node, XmlOptions options) throws XmlException {
            return (DefinitionsDocument) XmlBeans.getContextTypeLoader().parse(node, DefinitionsDocument.type, options);
        }

        public static DefinitionsDocument parse(XMLInputStream xis) throws XMLStreamException, XmlException {
            return (DefinitionsDocument) XmlBeans.getContextTypeLoader().parse(xis, DefinitionsDocument.type, (XmlOptions) null);
        }

        public static DefinitionsDocument parse(XMLInputStream xis, XmlOptions options) throws XMLStreamException, XmlException {
            return (DefinitionsDocument) XmlBeans.getContextTypeLoader().parse(xis, DefinitionsDocument.type, options);
        }

        public static XMLInputStream newValidatingXMLInputStream(XMLInputStream xis) throws XMLStreamException, XmlException {
            return XmlBeans.getContextTypeLoader().newValidatingXMLInputStream(xis, DefinitionsDocument.type, null);
        }

        public static XMLInputStream newValidatingXMLInputStream(XMLInputStream xis, XmlOptions options) throws XMLStreamException, XmlException {
            return XmlBeans.getContextTypeLoader().newValidatingXMLInputStream(xis, DefinitionsDocument.type, options);
        }

        private Factory() {
        }
    }
}
