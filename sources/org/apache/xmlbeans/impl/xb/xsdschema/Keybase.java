package org.apache.xmlbeans.impl.xb.xsdschema;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.net.URL;
import javax.xml.stream.XMLStreamReader;
import org.apache.xmlbeans.SchemaType;
import org.apache.xmlbeans.XmlBeans;
import org.apache.xmlbeans.XmlException;
import org.apache.xmlbeans.XmlNCName;
import org.apache.xmlbeans.XmlOptions;
import org.apache.xmlbeans.impl.xb.xsdschema.FieldDocument;
import org.apache.xmlbeans.impl.xb.xsdschema.SelectorDocument;
import org.apache.xmlbeans.xml.stream.XMLInputStream;
import org.apache.xmlbeans.xml.stream.XMLStreamException;
import org.w3c.dom.Node;

/* loaded from: xmlbeans-3.1.0.jar:org/apache/xmlbeans/impl/xb/xsdschema/Keybase.class */
public interface Keybase extends Annotated {
    public static final SchemaType type = (SchemaType) XmlBeans.typeSystemForClassLoader(Keybase.class.getClassLoader(), "schemaorg_apache_xmlbeans.system.sXMLSCHEMA").resolveHandle("keybase3955type");

    SelectorDocument.Selector getSelector();

    void setSelector(SelectorDocument.Selector selector);

    SelectorDocument.Selector addNewSelector();

    FieldDocument.Field[] getFieldArray();

    FieldDocument.Field getFieldArray(int i);

    int sizeOfFieldArray();

    void setFieldArray(FieldDocument.Field[] fieldArr);

    void setFieldArray(int i, FieldDocument.Field field);

    FieldDocument.Field insertNewField(int i);

    FieldDocument.Field addNewField();

    void removeField(int i);

    String getName();

    XmlNCName xgetName();

    void setName(String str);

    void xsetName(XmlNCName xmlNCName);

    /* loaded from: xmlbeans-3.1.0.jar:org/apache/xmlbeans/impl/xb/xsdschema/Keybase$Factory.class */
    public static final class Factory {
        public static Keybase newInstance() {
            return (Keybase) XmlBeans.getContextTypeLoader().newInstance(Keybase.type, null);
        }

        public static Keybase newInstance(XmlOptions options) {
            return (Keybase) XmlBeans.getContextTypeLoader().newInstance(Keybase.type, options);
        }

        public static Keybase parse(String xmlAsString) throws XmlException {
            return (Keybase) XmlBeans.getContextTypeLoader().parse(xmlAsString, Keybase.type, (XmlOptions) null);
        }

        public static Keybase parse(String xmlAsString, XmlOptions options) throws XmlException {
            return (Keybase) XmlBeans.getContextTypeLoader().parse(xmlAsString, Keybase.type, options);
        }

        public static Keybase parse(File file) throws XmlException, IOException {
            return (Keybase) XmlBeans.getContextTypeLoader().parse(file, Keybase.type, (XmlOptions) null);
        }

        public static Keybase parse(File file, XmlOptions options) throws XmlException, IOException {
            return (Keybase) XmlBeans.getContextTypeLoader().parse(file, Keybase.type, options);
        }

        public static Keybase parse(URL u) throws XmlException, IOException {
            return (Keybase) XmlBeans.getContextTypeLoader().parse(u, Keybase.type, (XmlOptions) null);
        }

        public static Keybase parse(URL u, XmlOptions options) throws XmlException, IOException {
            return (Keybase) XmlBeans.getContextTypeLoader().parse(u, Keybase.type, options);
        }

        public static Keybase parse(InputStream is) throws XmlException, IOException {
            return (Keybase) XmlBeans.getContextTypeLoader().parse(is, Keybase.type, (XmlOptions) null);
        }

        public static Keybase parse(InputStream is, XmlOptions options) throws XmlException, IOException {
            return (Keybase) XmlBeans.getContextTypeLoader().parse(is, Keybase.type, options);
        }

        public static Keybase parse(Reader r) throws XmlException, IOException {
            return (Keybase) XmlBeans.getContextTypeLoader().parse(r, Keybase.type, (XmlOptions) null);
        }

        public static Keybase parse(Reader r, XmlOptions options) throws XmlException, IOException {
            return (Keybase) XmlBeans.getContextTypeLoader().parse(r, Keybase.type, options);
        }

        public static Keybase parse(XMLStreamReader sr) throws XmlException {
            return (Keybase) XmlBeans.getContextTypeLoader().parse(sr, Keybase.type, (XmlOptions) null);
        }

        public static Keybase parse(XMLStreamReader sr, XmlOptions options) throws XmlException {
            return (Keybase) XmlBeans.getContextTypeLoader().parse(sr, Keybase.type, options);
        }

        public static Keybase parse(Node node) throws XmlException {
            return (Keybase) XmlBeans.getContextTypeLoader().parse(node, Keybase.type, (XmlOptions) null);
        }

        public static Keybase parse(Node node, XmlOptions options) throws XmlException {
            return (Keybase) XmlBeans.getContextTypeLoader().parse(node, Keybase.type, options);
        }

        public static Keybase parse(XMLInputStream xis) throws XMLStreamException, XmlException {
            return (Keybase) XmlBeans.getContextTypeLoader().parse(xis, Keybase.type, (XmlOptions) null);
        }

        public static Keybase parse(XMLInputStream xis, XmlOptions options) throws XMLStreamException, XmlException {
            return (Keybase) XmlBeans.getContextTypeLoader().parse(xis, Keybase.type, options);
        }

        public static XMLInputStream newValidatingXMLInputStream(XMLInputStream xis) throws XMLStreamException, XmlException {
            return XmlBeans.getContextTypeLoader().newValidatingXMLInputStream(xis, Keybase.type, null);
        }

        public static XMLInputStream newValidatingXMLInputStream(XMLInputStream xis, XmlOptions options) throws XMLStreamException, XmlException {
            return XmlBeans.getContextTypeLoader().newValidatingXMLInputStream(xis, Keybase.type, options);
        }

        private Factory() {
        }
    }
}
