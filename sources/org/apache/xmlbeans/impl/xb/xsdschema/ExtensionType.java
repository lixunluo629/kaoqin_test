package org.apache.xmlbeans.impl.xb.xsdschema;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.net.URL;
import javax.xml.namespace.QName;
import javax.xml.stream.XMLStreamReader;
import org.apache.xmlbeans.SchemaType;
import org.apache.xmlbeans.XmlBeans;
import org.apache.xmlbeans.XmlException;
import org.apache.xmlbeans.XmlOptions;
import org.apache.xmlbeans.XmlQName;
import org.apache.xmlbeans.xml.stream.XMLInputStream;
import org.apache.xmlbeans.xml.stream.XMLStreamException;
import org.w3c.dom.Node;

/* loaded from: xmlbeans-3.1.0.jar:org/apache/xmlbeans/impl/xb/xsdschema/ExtensionType.class */
public interface ExtensionType extends Annotated {
    public static final SchemaType type = (SchemaType) XmlBeans.typeSystemForClassLoader(ExtensionType.class.getClassLoader(), "schemaorg_apache_xmlbeans.system.sXMLSCHEMA").resolveHandle("extensiontypeed4ctype");

    GroupRef getGroup();

    boolean isSetGroup();

    void setGroup(GroupRef groupRef);

    GroupRef addNewGroup();

    void unsetGroup();

    All getAll();

    boolean isSetAll();

    void setAll(All all);

    All addNewAll();

    void unsetAll();

    ExplicitGroup getChoice();

    boolean isSetChoice();

    void setChoice(ExplicitGroup explicitGroup);

    ExplicitGroup addNewChoice();

    void unsetChoice();

    ExplicitGroup getSequence();

    boolean isSetSequence();

    void setSequence(ExplicitGroup explicitGroup);

    ExplicitGroup addNewSequence();

    void unsetSequence();

    Attribute[] getAttributeArray();

    Attribute getAttributeArray(int i);

    int sizeOfAttributeArray();

    void setAttributeArray(Attribute[] attributeArr);

    void setAttributeArray(int i, Attribute attribute);

    Attribute insertNewAttribute(int i);

    Attribute addNewAttribute();

    void removeAttribute(int i);

    AttributeGroupRef[] getAttributeGroupArray();

    AttributeGroupRef getAttributeGroupArray(int i);

    int sizeOfAttributeGroupArray();

    void setAttributeGroupArray(AttributeGroupRef[] attributeGroupRefArr);

    void setAttributeGroupArray(int i, AttributeGroupRef attributeGroupRef);

    AttributeGroupRef insertNewAttributeGroup(int i);

    AttributeGroupRef addNewAttributeGroup();

    void removeAttributeGroup(int i);

    Wildcard getAnyAttribute();

    boolean isSetAnyAttribute();

    void setAnyAttribute(Wildcard wildcard);

    Wildcard addNewAnyAttribute();

    void unsetAnyAttribute();

    QName getBase();

    XmlQName xgetBase();

    void setBase(QName qName);

    void xsetBase(XmlQName xmlQName);

    /* loaded from: xmlbeans-3.1.0.jar:org/apache/xmlbeans/impl/xb/xsdschema/ExtensionType$Factory.class */
    public static final class Factory {
        public static ExtensionType newInstance() {
            return (ExtensionType) XmlBeans.getContextTypeLoader().newInstance(ExtensionType.type, null);
        }

        public static ExtensionType newInstance(XmlOptions options) {
            return (ExtensionType) XmlBeans.getContextTypeLoader().newInstance(ExtensionType.type, options);
        }

        public static ExtensionType parse(String xmlAsString) throws XmlException {
            return (ExtensionType) XmlBeans.getContextTypeLoader().parse(xmlAsString, ExtensionType.type, (XmlOptions) null);
        }

        public static ExtensionType parse(String xmlAsString, XmlOptions options) throws XmlException {
            return (ExtensionType) XmlBeans.getContextTypeLoader().parse(xmlAsString, ExtensionType.type, options);
        }

        public static ExtensionType parse(File file) throws XmlException, IOException {
            return (ExtensionType) XmlBeans.getContextTypeLoader().parse(file, ExtensionType.type, (XmlOptions) null);
        }

        public static ExtensionType parse(File file, XmlOptions options) throws XmlException, IOException {
            return (ExtensionType) XmlBeans.getContextTypeLoader().parse(file, ExtensionType.type, options);
        }

        public static ExtensionType parse(URL u) throws XmlException, IOException {
            return (ExtensionType) XmlBeans.getContextTypeLoader().parse(u, ExtensionType.type, (XmlOptions) null);
        }

        public static ExtensionType parse(URL u, XmlOptions options) throws XmlException, IOException {
            return (ExtensionType) XmlBeans.getContextTypeLoader().parse(u, ExtensionType.type, options);
        }

        public static ExtensionType parse(InputStream is) throws XmlException, IOException {
            return (ExtensionType) XmlBeans.getContextTypeLoader().parse(is, ExtensionType.type, (XmlOptions) null);
        }

        public static ExtensionType parse(InputStream is, XmlOptions options) throws XmlException, IOException {
            return (ExtensionType) XmlBeans.getContextTypeLoader().parse(is, ExtensionType.type, options);
        }

        public static ExtensionType parse(Reader r) throws XmlException, IOException {
            return (ExtensionType) XmlBeans.getContextTypeLoader().parse(r, ExtensionType.type, (XmlOptions) null);
        }

        public static ExtensionType parse(Reader r, XmlOptions options) throws XmlException, IOException {
            return (ExtensionType) XmlBeans.getContextTypeLoader().parse(r, ExtensionType.type, options);
        }

        public static ExtensionType parse(XMLStreamReader sr) throws XmlException {
            return (ExtensionType) XmlBeans.getContextTypeLoader().parse(sr, ExtensionType.type, (XmlOptions) null);
        }

        public static ExtensionType parse(XMLStreamReader sr, XmlOptions options) throws XmlException {
            return (ExtensionType) XmlBeans.getContextTypeLoader().parse(sr, ExtensionType.type, options);
        }

        public static ExtensionType parse(Node node) throws XmlException {
            return (ExtensionType) XmlBeans.getContextTypeLoader().parse(node, ExtensionType.type, (XmlOptions) null);
        }

        public static ExtensionType parse(Node node, XmlOptions options) throws XmlException {
            return (ExtensionType) XmlBeans.getContextTypeLoader().parse(node, ExtensionType.type, options);
        }

        public static ExtensionType parse(XMLInputStream xis) throws XMLStreamException, XmlException {
            return (ExtensionType) XmlBeans.getContextTypeLoader().parse(xis, ExtensionType.type, (XmlOptions) null);
        }

        public static ExtensionType parse(XMLInputStream xis, XmlOptions options) throws XMLStreamException, XmlException {
            return (ExtensionType) XmlBeans.getContextTypeLoader().parse(xis, ExtensionType.type, options);
        }

        public static XMLInputStream newValidatingXMLInputStream(XMLInputStream xis) throws XMLStreamException, XmlException {
            return XmlBeans.getContextTypeLoader().newValidatingXMLInputStream(xis, ExtensionType.type, null);
        }

        public static XMLInputStream newValidatingXMLInputStream(XMLInputStream xis, XmlOptions options) throws XMLStreamException, XmlException {
            return XmlBeans.getContextTypeLoader().newValidatingXMLInputStream(xis, ExtensionType.type, options);
        }

        private Factory() {
        }
    }
}
