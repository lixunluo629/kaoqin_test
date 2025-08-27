package org.apache.xmlbeans;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Reader;
import java.io.Writer;
import javax.xml.stream.XMLStreamReader;
import org.apache.xmlbeans.xml.stream.XMLInputStream;
import org.w3c.dom.Node;
import org.xml.sax.ContentHandler;
import org.xml.sax.SAXException;
import org.xml.sax.ext.LexicalHandler;

/* loaded from: xmlbeans-3.1.0.jar:org/apache/xmlbeans/XmlTokenSource.class */
public interface XmlTokenSource {
    Object monitor();

    XmlDocumentProperties documentProperties();

    XmlCursor newCursor();

    XMLInputStream newXMLInputStream();

    XMLStreamReader newXMLStreamReader();

    String xmlText();

    InputStream newInputStream();

    Reader newReader();

    Node newDomNode();

    Node getDomNode();

    void save(ContentHandler contentHandler, LexicalHandler lexicalHandler) throws SAXException;

    void save(File file) throws IOException;

    void save(OutputStream outputStream) throws IOException;

    void save(Writer writer) throws IOException;

    XMLInputStream newXMLInputStream(XmlOptions xmlOptions);

    XMLStreamReader newXMLStreamReader(XmlOptions xmlOptions);

    String xmlText(XmlOptions xmlOptions);

    InputStream newInputStream(XmlOptions xmlOptions);

    Reader newReader(XmlOptions xmlOptions);

    Node newDomNode(XmlOptions xmlOptions);

    void save(ContentHandler contentHandler, LexicalHandler lexicalHandler, XmlOptions xmlOptions) throws SAXException;

    void save(File file, XmlOptions xmlOptions) throws IOException;

    void save(OutputStream outputStream, XmlOptions xmlOptions) throws IOException;

    void save(Writer writer, XmlOptions xmlOptions) throws IOException;

    void dump();
}
