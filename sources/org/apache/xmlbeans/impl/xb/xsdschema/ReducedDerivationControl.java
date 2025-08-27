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
import org.apache.xmlbeans.XmlOptions;
import org.apache.xmlbeans.impl.xb.xsdschema.DerivationControl;
import org.apache.xmlbeans.xml.stream.XMLInputStream;
import org.apache.xmlbeans.xml.stream.XMLStreamException;
import org.w3c.dom.Node;

/* loaded from: xmlbeans-3.1.0.jar:org/apache/xmlbeans/impl/xb/xsdschema/ReducedDerivationControl.class */
public interface ReducedDerivationControl extends DerivationControl {
    public static final SchemaType type = (SchemaType) XmlBeans.typeSystemForClassLoader(ReducedDerivationControl.class.getClassLoader(), "schemaorg_apache_xmlbeans.system.sXMLSCHEMA").resolveHandle("reducedderivationcontrole1cbtype");
    public static final DerivationControl.Enum EXTENSION = DerivationControl.EXTENSION;
    public static final DerivationControl.Enum RESTRICTION = DerivationControl.RESTRICTION;
    public static final int INT_EXTENSION = 2;
    public static final int INT_RESTRICTION = 3;

    /* loaded from: xmlbeans-3.1.0.jar:org/apache/xmlbeans/impl/xb/xsdschema/ReducedDerivationControl$Factory.class */
    public static final class Factory {
        public static ReducedDerivationControl newValue(Object obj) {
            return (ReducedDerivationControl) ReducedDerivationControl.type.newValue(obj);
        }

        public static ReducedDerivationControl newInstance() {
            return (ReducedDerivationControl) XmlBeans.getContextTypeLoader().newInstance(ReducedDerivationControl.type, null);
        }

        public static ReducedDerivationControl newInstance(XmlOptions options) {
            return (ReducedDerivationControl) XmlBeans.getContextTypeLoader().newInstance(ReducedDerivationControl.type, options);
        }

        public static ReducedDerivationControl parse(String xmlAsString) throws XmlException {
            return (ReducedDerivationControl) XmlBeans.getContextTypeLoader().parse(xmlAsString, ReducedDerivationControl.type, (XmlOptions) null);
        }

        public static ReducedDerivationControl parse(String xmlAsString, XmlOptions options) throws XmlException {
            return (ReducedDerivationControl) XmlBeans.getContextTypeLoader().parse(xmlAsString, ReducedDerivationControl.type, options);
        }

        public static ReducedDerivationControl parse(File file) throws XmlException, IOException {
            return (ReducedDerivationControl) XmlBeans.getContextTypeLoader().parse(file, ReducedDerivationControl.type, (XmlOptions) null);
        }

        public static ReducedDerivationControl parse(File file, XmlOptions options) throws XmlException, IOException {
            return (ReducedDerivationControl) XmlBeans.getContextTypeLoader().parse(file, ReducedDerivationControl.type, options);
        }

        public static ReducedDerivationControl parse(URL u) throws XmlException, IOException {
            return (ReducedDerivationControl) XmlBeans.getContextTypeLoader().parse(u, ReducedDerivationControl.type, (XmlOptions) null);
        }

        public static ReducedDerivationControl parse(URL u, XmlOptions options) throws XmlException, IOException {
            return (ReducedDerivationControl) XmlBeans.getContextTypeLoader().parse(u, ReducedDerivationControl.type, options);
        }

        public static ReducedDerivationControl parse(InputStream is) throws XmlException, IOException {
            return (ReducedDerivationControl) XmlBeans.getContextTypeLoader().parse(is, ReducedDerivationControl.type, (XmlOptions) null);
        }

        public static ReducedDerivationControl parse(InputStream is, XmlOptions options) throws XmlException, IOException {
            return (ReducedDerivationControl) XmlBeans.getContextTypeLoader().parse(is, ReducedDerivationControl.type, options);
        }

        public static ReducedDerivationControl parse(Reader r) throws XmlException, IOException {
            return (ReducedDerivationControl) XmlBeans.getContextTypeLoader().parse(r, ReducedDerivationControl.type, (XmlOptions) null);
        }

        public static ReducedDerivationControl parse(Reader r, XmlOptions options) throws XmlException, IOException {
            return (ReducedDerivationControl) XmlBeans.getContextTypeLoader().parse(r, ReducedDerivationControl.type, options);
        }

        public static ReducedDerivationControl parse(XMLStreamReader sr) throws XmlException {
            return (ReducedDerivationControl) XmlBeans.getContextTypeLoader().parse(sr, ReducedDerivationControl.type, (XmlOptions) null);
        }

        public static ReducedDerivationControl parse(XMLStreamReader sr, XmlOptions options) throws XmlException {
            return (ReducedDerivationControl) XmlBeans.getContextTypeLoader().parse(sr, ReducedDerivationControl.type, options);
        }

        public static ReducedDerivationControl parse(Node node) throws XmlException {
            return (ReducedDerivationControl) XmlBeans.getContextTypeLoader().parse(node, ReducedDerivationControl.type, (XmlOptions) null);
        }

        public static ReducedDerivationControl parse(Node node, XmlOptions options) throws XmlException {
            return (ReducedDerivationControl) XmlBeans.getContextTypeLoader().parse(node, ReducedDerivationControl.type, options);
        }

        public static ReducedDerivationControl parse(XMLInputStream xis) throws XMLStreamException, XmlException {
            return (ReducedDerivationControl) XmlBeans.getContextTypeLoader().parse(xis, ReducedDerivationControl.type, (XmlOptions) null);
        }

        public static ReducedDerivationControl parse(XMLInputStream xis, XmlOptions options) throws XMLStreamException, XmlException {
            return (ReducedDerivationControl) XmlBeans.getContextTypeLoader().parse(xis, ReducedDerivationControl.type, options);
        }

        public static XMLInputStream newValidatingXMLInputStream(XMLInputStream xis) throws XMLStreamException, XmlException {
            return XmlBeans.getContextTypeLoader().newValidatingXMLInputStream(xis, ReducedDerivationControl.type, null);
        }

        public static XMLInputStream newValidatingXMLInputStream(XMLInputStream xis, XmlOptions options) throws XMLStreamException, XmlException {
            return XmlBeans.getContextTypeLoader().newValidatingXMLInputStream(xis, ReducedDerivationControl.type, options);
        }

        private Factory() {
        }
    }
}
