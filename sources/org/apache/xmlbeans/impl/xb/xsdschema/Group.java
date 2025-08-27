package org.apache.xmlbeans.impl.xb.xsdschema;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.math.BigInteger;
import java.net.URL;
import javax.xml.namespace.QName;
import javax.xml.stream.XMLStreamReader;
import org.apache.xmlbeans.SchemaType;
import org.apache.xmlbeans.XmlBeans;
import org.apache.xmlbeans.XmlException;
import org.apache.xmlbeans.XmlNCName;
import org.apache.xmlbeans.XmlNonNegativeInteger;
import org.apache.xmlbeans.XmlOptions;
import org.apache.xmlbeans.XmlQName;
import org.apache.xmlbeans.impl.xb.xsdschema.AnyDocument;
import org.apache.xmlbeans.xml.stream.XMLInputStream;
import org.apache.xmlbeans.xml.stream.XMLStreamException;
import org.w3c.dom.Node;

/* loaded from: xmlbeans-3.1.0.jar:org/apache/xmlbeans/impl/xb/xsdschema/Group.class */
public interface Group extends Annotated {
    public static final SchemaType type = (SchemaType) XmlBeans.typeSystemForClassLoader(Group.class.getClassLoader(), "schemaorg_apache_xmlbeans.system.sXMLSCHEMA").resolveHandle("group7ca6type");

    LocalElement[] getElementArray();

    LocalElement getElementArray(int i);

    int sizeOfElementArray();

    void setElementArray(LocalElement[] localElementArr);

    void setElementArray(int i, LocalElement localElement);

    LocalElement insertNewElement(int i);

    LocalElement addNewElement();

    void removeElement(int i);

    GroupRef[] getGroupArray();

    GroupRef getGroupArray(int i);

    int sizeOfGroupArray();

    void setGroupArray(GroupRef[] groupRefArr);

    void setGroupArray(int i, GroupRef groupRef);

    GroupRef insertNewGroup(int i);

    GroupRef addNewGroup();

    void removeGroup(int i);

    All[] getAllArray();

    All getAllArray(int i);

    int sizeOfAllArray();

    void setAllArray(All[] allArr);

    void setAllArray(int i, All all);

    All insertNewAll(int i);

    All addNewAll();

    void removeAll(int i);

    ExplicitGroup[] getChoiceArray();

    ExplicitGroup getChoiceArray(int i);

    int sizeOfChoiceArray();

    void setChoiceArray(ExplicitGroup[] explicitGroupArr);

    void setChoiceArray(int i, ExplicitGroup explicitGroup);

    ExplicitGroup insertNewChoice(int i);

    ExplicitGroup addNewChoice();

    void removeChoice(int i);

    ExplicitGroup[] getSequenceArray();

    ExplicitGroup getSequenceArray(int i);

    int sizeOfSequenceArray();

    void setSequenceArray(ExplicitGroup[] explicitGroupArr);

    void setSequenceArray(int i, ExplicitGroup explicitGroup);

    ExplicitGroup insertNewSequence(int i);

    ExplicitGroup addNewSequence();

    void removeSequence(int i);

    AnyDocument.Any[] getAnyArray();

    AnyDocument.Any getAnyArray(int i);

    int sizeOfAnyArray();

    void setAnyArray(AnyDocument.Any[] anyArr);

    void setAnyArray(int i, AnyDocument.Any any);

    AnyDocument.Any insertNewAny(int i);

    AnyDocument.Any addNewAny();

    void removeAny(int i);

    String getName();

    XmlNCName xgetName();

    boolean isSetName();

    void setName(String str);

    void xsetName(XmlNCName xmlNCName);

    void unsetName();

    QName getRef();

    XmlQName xgetRef();

    boolean isSetRef();

    void setRef(QName qName);

    void xsetRef(XmlQName xmlQName);

    void unsetRef();

    BigInteger getMinOccurs();

    XmlNonNegativeInteger xgetMinOccurs();

    boolean isSetMinOccurs();

    void setMinOccurs(BigInteger bigInteger);

    void xsetMinOccurs(XmlNonNegativeInteger xmlNonNegativeInteger);

    void unsetMinOccurs();

    Object getMaxOccurs();

    AllNNI xgetMaxOccurs();

    boolean isSetMaxOccurs();

    void setMaxOccurs(Object obj);

    void xsetMaxOccurs(AllNNI allNNI);

    void unsetMaxOccurs();

    /* loaded from: xmlbeans-3.1.0.jar:org/apache/xmlbeans/impl/xb/xsdschema/Group$Factory.class */
    public static final class Factory {
        public static Group newInstance() {
            return (Group) XmlBeans.getContextTypeLoader().newInstance(Group.type, null);
        }

        public static Group newInstance(XmlOptions options) {
            return (Group) XmlBeans.getContextTypeLoader().newInstance(Group.type, options);
        }

        public static Group parse(String xmlAsString) throws XmlException {
            return (Group) XmlBeans.getContextTypeLoader().parse(xmlAsString, Group.type, (XmlOptions) null);
        }

        public static Group parse(String xmlAsString, XmlOptions options) throws XmlException {
            return (Group) XmlBeans.getContextTypeLoader().parse(xmlAsString, Group.type, options);
        }

        public static Group parse(File file) throws XmlException, IOException {
            return (Group) XmlBeans.getContextTypeLoader().parse(file, Group.type, (XmlOptions) null);
        }

        public static Group parse(File file, XmlOptions options) throws XmlException, IOException {
            return (Group) XmlBeans.getContextTypeLoader().parse(file, Group.type, options);
        }

        public static Group parse(URL u) throws XmlException, IOException {
            return (Group) XmlBeans.getContextTypeLoader().parse(u, Group.type, (XmlOptions) null);
        }

        public static Group parse(URL u, XmlOptions options) throws XmlException, IOException {
            return (Group) XmlBeans.getContextTypeLoader().parse(u, Group.type, options);
        }

        public static Group parse(InputStream is) throws XmlException, IOException {
            return (Group) XmlBeans.getContextTypeLoader().parse(is, Group.type, (XmlOptions) null);
        }

        public static Group parse(InputStream is, XmlOptions options) throws XmlException, IOException {
            return (Group) XmlBeans.getContextTypeLoader().parse(is, Group.type, options);
        }

        public static Group parse(Reader r) throws XmlException, IOException {
            return (Group) XmlBeans.getContextTypeLoader().parse(r, Group.type, (XmlOptions) null);
        }

        public static Group parse(Reader r, XmlOptions options) throws XmlException, IOException {
            return (Group) XmlBeans.getContextTypeLoader().parse(r, Group.type, options);
        }

        public static Group parse(XMLStreamReader sr) throws XmlException {
            return (Group) XmlBeans.getContextTypeLoader().parse(sr, Group.type, (XmlOptions) null);
        }

        public static Group parse(XMLStreamReader sr, XmlOptions options) throws XmlException {
            return (Group) XmlBeans.getContextTypeLoader().parse(sr, Group.type, options);
        }

        public static Group parse(Node node) throws XmlException {
            return (Group) XmlBeans.getContextTypeLoader().parse(node, Group.type, (XmlOptions) null);
        }

        public static Group parse(Node node, XmlOptions options) throws XmlException {
            return (Group) XmlBeans.getContextTypeLoader().parse(node, Group.type, options);
        }

        public static Group parse(XMLInputStream xis) throws XMLStreamException, XmlException {
            return (Group) XmlBeans.getContextTypeLoader().parse(xis, Group.type, (XmlOptions) null);
        }

        public static Group parse(XMLInputStream xis, XmlOptions options) throws XMLStreamException, XmlException {
            return (Group) XmlBeans.getContextTypeLoader().parse(xis, Group.type, options);
        }

        public static XMLInputStream newValidatingXMLInputStream(XMLInputStream xis) throws XMLStreamException, XmlException {
            return XmlBeans.getContextTypeLoader().newValidatingXMLInputStream(xis, Group.type, null);
        }

        public static XMLInputStream newValidatingXMLInputStream(XMLInputStream xis, XmlOptions options) throws XMLStreamException, XmlException {
            return XmlBeans.getContextTypeLoader().newValidatingXMLInputStream(xis, Group.type, options);
        }

        private Factory() {
        }
    }
}
