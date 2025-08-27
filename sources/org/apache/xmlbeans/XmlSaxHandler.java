package org.apache.xmlbeans;

import javax.xml.namespace.QName;
import org.apache.xmlbeans.XmlCursor;
import org.xml.sax.ContentHandler;
import org.xml.sax.ext.LexicalHandler;

/* loaded from: xmlbeans-3.1.0.jar:org/apache/xmlbeans/XmlSaxHandler.class */
public interface XmlSaxHandler {
    ContentHandler getContentHandler();

    LexicalHandler getLexicalHandler();

    void bookmarkLastEvent(XmlCursor.XmlBookmark xmlBookmark);

    void bookmarkLastAttr(QName qName, XmlCursor.XmlBookmark xmlBookmark);

    XmlObject getObject() throws XmlException;
}
