package org.apache.xmlbeans;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.net.URL;
import javax.xml.namespace.QName;
import javax.xml.stream.XMLStreamReader;
import org.apache.xmlbeans.SchemaAttributeGroup;
import org.apache.xmlbeans.SchemaGlobalAttribute;
import org.apache.xmlbeans.SchemaGlobalElement;
import org.apache.xmlbeans.SchemaIdentityConstraint;
import org.apache.xmlbeans.SchemaModelGroup;
import org.apache.xmlbeans.SchemaType;
import org.apache.xmlbeans.xml.stream.XMLInputStream;
import org.apache.xmlbeans.xml.stream.XMLStreamException;
import org.w3c.dom.DOMImplementation;
import org.w3c.dom.Node;

/* loaded from: xmlbeans-3.1.0.jar:org/apache/xmlbeans/SchemaTypeLoader.class */
public interface SchemaTypeLoader {
    SchemaType findType(QName qName);

    SchemaType findDocumentType(QName qName);

    SchemaType findAttributeType(QName qName);

    SchemaGlobalElement findElement(QName qName);

    SchemaGlobalAttribute findAttribute(QName qName);

    SchemaModelGroup findModelGroup(QName qName);

    SchemaAttributeGroup findAttributeGroup(QName qName);

    boolean isNamespaceDefined(String str);

    SchemaType.Ref findTypeRef(QName qName);

    SchemaType.Ref findDocumentTypeRef(QName qName);

    SchemaType.Ref findAttributeTypeRef(QName qName);

    SchemaGlobalElement.Ref findElementRef(QName qName);

    SchemaGlobalAttribute.Ref findAttributeRef(QName qName);

    SchemaModelGroup.Ref findModelGroupRef(QName qName);

    SchemaAttributeGroup.Ref findAttributeGroupRef(QName qName);

    SchemaIdentityConstraint.Ref findIdentityConstraintRef(QName qName);

    SchemaType typeForSignature(String str);

    SchemaType typeForClassname(String str);

    InputStream getSourceAsStream(String str);

    String compilePath(String str, XmlOptions xmlOptions) throws XmlException;

    String compileQuery(String str, XmlOptions xmlOptions) throws XmlException;

    XmlObject newInstance(SchemaType schemaType, XmlOptions xmlOptions);

    XmlObject parse(String str, SchemaType schemaType, XmlOptions xmlOptions) throws XmlException;

    XmlObject parse(File file, SchemaType schemaType, XmlOptions xmlOptions) throws XmlException, IOException;

    XmlObject parse(URL url, SchemaType schemaType, XmlOptions xmlOptions) throws XmlException, IOException;

    XmlObject parse(InputStream inputStream, SchemaType schemaType, XmlOptions xmlOptions) throws XmlException, IOException;

    XmlObject parse(XMLStreamReader xMLStreamReader, SchemaType schemaType, XmlOptions xmlOptions) throws XmlException;

    XmlObject parse(Reader reader, SchemaType schemaType, XmlOptions xmlOptions) throws XmlException, IOException;

    XmlObject parse(Node node, SchemaType schemaType, XmlOptions xmlOptions) throws XmlException;

    XmlObject parse(XMLInputStream xMLInputStream, SchemaType schemaType, XmlOptions xmlOptions) throws XMLStreamException, XmlException;

    XmlSaxHandler newXmlSaxHandler(SchemaType schemaType, XmlOptions xmlOptions);

    DOMImplementation newDomImplementation(XmlOptions xmlOptions);

    XMLInputStream newValidatingXMLInputStream(XMLInputStream xMLInputStream, SchemaType schemaType, XmlOptions xmlOptions) throws XMLStreamException, XmlException;
}
