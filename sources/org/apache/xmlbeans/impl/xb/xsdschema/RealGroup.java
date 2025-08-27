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
import org.apache.xmlbeans.xml.stream.XMLInputStream;
import org.apache.xmlbeans.xml.stream.XMLStreamException;
import org.w3c.dom.Node;

/* loaded from: xmlbeans-3.1.0.jar:org/apache/xmlbeans/impl/xb/xsdschema/RealGroup.class */
public interface RealGroup extends Group {
    public static final SchemaType type = (SchemaType) XmlBeans.typeSystemForClassLoader(RealGroup.class.getClassLoader(), "schemaorg_apache_xmlbeans.system.sXMLSCHEMA").resolveHandle("realgroup1f64type");

    @Override // org.apache.xmlbeans.impl.xb.xsdschema.Group
    All[] getAllArray();

    @Override // org.apache.xmlbeans.impl.xb.xsdschema.Group
    All getAllArray(int i);

    @Override // org.apache.xmlbeans.impl.xb.xsdschema.Group
    int sizeOfAllArray();

    @Override // org.apache.xmlbeans.impl.xb.xsdschema.Group
    void setAllArray(All[] allArr);

    @Override // org.apache.xmlbeans.impl.xb.xsdschema.Group
    void setAllArray(int i, All all);

    @Override // org.apache.xmlbeans.impl.xb.xsdschema.Group
    All insertNewAll(int i);

    @Override // org.apache.xmlbeans.impl.xb.xsdschema.Group
    All addNewAll();

    @Override // org.apache.xmlbeans.impl.xb.xsdschema.Group
    void removeAll(int i);

    @Override // org.apache.xmlbeans.impl.xb.xsdschema.Group
    ExplicitGroup[] getChoiceArray();

    @Override // org.apache.xmlbeans.impl.xb.xsdschema.Group
    ExplicitGroup getChoiceArray(int i);

    @Override // org.apache.xmlbeans.impl.xb.xsdschema.Group
    int sizeOfChoiceArray();

    @Override // org.apache.xmlbeans.impl.xb.xsdschema.Group
    void setChoiceArray(ExplicitGroup[] explicitGroupArr);

    @Override // org.apache.xmlbeans.impl.xb.xsdschema.Group
    void setChoiceArray(int i, ExplicitGroup explicitGroup);

    @Override // org.apache.xmlbeans.impl.xb.xsdschema.Group
    ExplicitGroup insertNewChoice(int i);

    @Override // org.apache.xmlbeans.impl.xb.xsdschema.Group
    ExplicitGroup addNewChoice();

    @Override // org.apache.xmlbeans.impl.xb.xsdschema.Group
    void removeChoice(int i);

    @Override // org.apache.xmlbeans.impl.xb.xsdschema.Group
    ExplicitGroup[] getSequenceArray();

    @Override // org.apache.xmlbeans.impl.xb.xsdschema.Group
    ExplicitGroup getSequenceArray(int i);

    @Override // org.apache.xmlbeans.impl.xb.xsdschema.Group
    int sizeOfSequenceArray();

    @Override // org.apache.xmlbeans.impl.xb.xsdschema.Group
    void setSequenceArray(ExplicitGroup[] explicitGroupArr);

    @Override // org.apache.xmlbeans.impl.xb.xsdschema.Group
    void setSequenceArray(int i, ExplicitGroup explicitGroup);

    @Override // org.apache.xmlbeans.impl.xb.xsdschema.Group
    ExplicitGroup insertNewSequence(int i);

    @Override // org.apache.xmlbeans.impl.xb.xsdschema.Group
    ExplicitGroup addNewSequence();

    @Override // org.apache.xmlbeans.impl.xb.xsdschema.Group
    void removeSequence(int i);

    /* loaded from: xmlbeans-3.1.0.jar:org/apache/xmlbeans/impl/xb/xsdschema/RealGroup$Factory.class */
    public static final class Factory {
        public static RealGroup newInstance() {
            return (RealGroup) XmlBeans.getContextTypeLoader().newInstance(RealGroup.type, null);
        }

        public static RealGroup newInstance(XmlOptions options) {
            return (RealGroup) XmlBeans.getContextTypeLoader().newInstance(RealGroup.type, options);
        }

        public static RealGroup parse(String xmlAsString) throws XmlException {
            return (RealGroup) XmlBeans.getContextTypeLoader().parse(xmlAsString, RealGroup.type, (XmlOptions) null);
        }

        public static RealGroup parse(String xmlAsString, XmlOptions options) throws XmlException {
            return (RealGroup) XmlBeans.getContextTypeLoader().parse(xmlAsString, RealGroup.type, options);
        }

        public static RealGroup parse(File file) throws XmlException, IOException {
            return (RealGroup) XmlBeans.getContextTypeLoader().parse(file, RealGroup.type, (XmlOptions) null);
        }

        public static RealGroup parse(File file, XmlOptions options) throws XmlException, IOException {
            return (RealGroup) XmlBeans.getContextTypeLoader().parse(file, RealGroup.type, options);
        }

        public static RealGroup parse(URL u) throws XmlException, IOException {
            return (RealGroup) XmlBeans.getContextTypeLoader().parse(u, RealGroup.type, (XmlOptions) null);
        }

        public static RealGroup parse(URL u, XmlOptions options) throws XmlException, IOException {
            return (RealGroup) XmlBeans.getContextTypeLoader().parse(u, RealGroup.type, options);
        }

        public static RealGroup parse(InputStream is) throws XmlException, IOException {
            return (RealGroup) XmlBeans.getContextTypeLoader().parse(is, RealGroup.type, (XmlOptions) null);
        }

        public static RealGroup parse(InputStream is, XmlOptions options) throws XmlException, IOException {
            return (RealGroup) XmlBeans.getContextTypeLoader().parse(is, RealGroup.type, options);
        }

        public static RealGroup parse(Reader r) throws XmlException, IOException {
            return (RealGroup) XmlBeans.getContextTypeLoader().parse(r, RealGroup.type, (XmlOptions) null);
        }

        public static RealGroup parse(Reader r, XmlOptions options) throws XmlException, IOException {
            return (RealGroup) XmlBeans.getContextTypeLoader().parse(r, RealGroup.type, options);
        }

        public static RealGroup parse(XMLStreamReader sr) throws XmlException {
            return (RealGroup) XmlBeans.getContextTypeLoader().parse(sr, RealGroup.type, (XmlOptions) null);
        }

        public static RealGroup parse(XMLStreamReader sr, XmlOptions options) throws XmlException {
            return (RealGroup) XmlBeans.getContextTypeLoader().parse(sr, RealGroup.type, options);
        }

        public static RealGroup parse(Node node) throws XmlException {
            return (RealGroup) XmlBeans.getContextTypeLoader().parse(node, RealGroup.type, (XmlOptions) null);
        }

        public static RealGroup parse(Node node, XmlOptions options) throws XmlException {
            return (RealGroup) XmlBeans.getContextTypeLoader().parse(node, RealGroup.type, options);
        }

        public static RealGroup parse(XMLInputStream xis) throws XMLStreamException, XmlException {
            return (RealGroup) XmlBeans.getContextTypeLoader().parse(xis, RealGroup.type, (XmlOptions) null);
        }

        public static RealGroup parse(XMLInputStream xis, XmlOptions options) throws XMLStreamException, XmlException {
            return (RealGroup) XmlBeans.getContextTypeLoader().parse(xis, RealGroup.type, options);
        }

        public static XMLInputStream newValidatingXMLInputStream(XMLInputStream xis) throws XMLStreamException, XmlException {
            return XmlBeans.getContextTypeLoader().newValidatingXMLInputStream(xis, RealGroup.type, null);
        }

        public static XMLInputStream newValidatingXMLInputStream(XMLInputStream xis, XmlOptions options) throws XMLStreamException, XmlException {
            return XmlBeans.getContextTypeLoader().newValidatingXMLInputStream(xis, RealGroup.type, options);
        }

        private Factory() {
        }
    }
}
