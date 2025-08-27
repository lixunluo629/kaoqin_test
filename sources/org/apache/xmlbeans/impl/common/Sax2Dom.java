package org.apache.xmlbeans.impl.common;

import java.util.Stack;
import java.util.Vector;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.Comment;
import org.w3c.dom.DOMException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.ProcessingInstruction;
import org.xml.sax.Attributes;
import org.xml.sax.ContentHandler;
import org.xml.sax.Locator;
import org.xml.sax.SAXException;
import org.xml.sax.ext.LexicalHandler;
import org.xml.sax.helpers.DefaultHandler;

/* loaded from: xmlbeans-3.1.0.jar:org/apache/xmlbeans/impl/common/Sax2Dom.class */
public class Sax2Dom extends DefaultHandler implements ContentHandler, LexicalHandler {
    public static final String EMPTYSTRING = "";
    public static final String XML_PREFIX = "xml";
    public static final String XMLNS_PREFIX = "xmlns";
    public static final String XMLNS_STRING = "xmlns:";
    public static final String XMLNS_URI = "http://www.w3.org/2000/xmlns/";
    private Node _root;
    private Document _document;
    private Stack _nodeStk = new Stack();
    private Vector _namespaceDecls = null;

    public Sax2Dom() throws ParserConfigurationException {
        this._root = null;
        this._document = null;
        this._document = DocumentHelper.createDocument();
        this._root = this._document;
    }

    public Sax2Dom(Node root) throws ParserConfigurationException {
        this._root = null;
        this._document = null;
        this._root = root;
        if (root instanceof Document) {
            this._document = (Document) root;
        } else if (root != null) {
            this._document = root.getOwnerDocument();
        } else {
            this._document = DocumentHelper.createDocument();
            this._root = this._document;
        }
    }

    public Node getDOM() {
        return this._root;
    }

    @Override // org.xml.sax.helpers.DefaultHandler, org.xml.sax.ContentHandler
    public void characters(char[] ch2, int start, int length) throws DOMException {
        Node last = (Node) this._nodeStk.peek();
        if (last != this._document) {
            String text = new String(ch2, start, length);
            last.appendChild(this._document.createTextNode(text));
        }
    }

    @Override // org.xml.sax.helpers.DefaultHandler, org.xml.sax.ContentHandler
    public void startDocument() {
        this._nodeStk.push(this._root);
    }

    @Override // org.xml.sax.helpers.DefaultHandler, org.xml.sax.ContentHandler
    public void endDocument() {
        this._nodeStk.pop();
    }

    @Override // org.xml.sax.helpers.DefaultHandler, org.xml.sax.ContentHandler
    public void startElement(String namespace, String localName, String qName, Attributes attrs) throws DOMException {
        Element tmp = this._document.createElementNS(namespace, qName);
        if (this._namespaceDecls != null) {
            int nDecls = this._namespaceDecls.size();
            int i = 0;
            while (i < nDecls) {
                int i2 = i;
                int i3 = i + 1;
                String prefix = (String) this._namespaceDecls.elementAt(i2);
                if (prefix == null || prefix.equals("")) {
                    tmp.setAttributeNS("http://www.w3.org/2000/xmlns/", "xmlns", (String) this._namespaceDecls.elementAt(i3));
                } else {
                    tmp.setAttributeNS("http://www.w3.org/2000/xmlns/", XMLNS_STRING + prefix, (String) this._namespaceDecls.elementAt(i3));
                }
                i = i3 + 1;
            }
            this._namespaceDecls.clear();
        }
        int nattrs = attrs.getLength();
        for (int i4 = 0; i4 < nattrs; i4++) {
            if (attrs.getLocalName(i4) == null) {
                tmp.setAttribute(attrs.getQName(i4), attrs.getValue(i4));
            } else {
                tmp.setAttributeNS(attrs.getURI(i4), attrs.getQName(i4), attrs.getValue(i4));
            }
        }
        Node last = (Node) this._nodeStk.peek();
        last.appendChild(tmp);
        this._nodeStk.push(tmp);
    }

    @Override // org.xml.sax.helpers.DefaultHandler, org.xml.sax.ContentHandler
    public void endElement(String namespace, String localName, String qName) {
        this._nodeStk.pop();
    }

    @Override // org.xml.sax.helpers.DefaultHandler, org.xml.sax.ContentHandler
    public void startPrefixMapping(String prefix, String uri) {
        if (this._namespaceDecls == null) {
            this._namespaceDecls = new Vector(2);
        }
        this._namespaceDecls.addElement(prefix);
        this._namespaceDecls.addElement(uri);
    }

    @Override // org.xml.sax.helpers.DefaultHandler, org.xml.sax.ContentHandler
    public void endPrefixMapping(String prefix) {
    }

    @Override // org.xml.sax.helpers.DefaultHandler, org.xml.sax.ContentHandler
    public void ignorableWhitespace(char[] ch2, int start, int length) {
    }

    @Override // org.xml.sax.helpers.DefaultHandler, org.xml.sax.ContentHandler
    public void processingInstruction(String target, String data) throws DOMException {
        Node last = (Node) this._nodeStk.peek();
        ProcessingInstruction pi = this._document.createProcessingInstruction(target, data);
        if (pi != null) {
            last.appendChild(pi);
        }
    }

    @Override // org.xml.sax.helpers.DefaultHandler, org.xml.sax.ContentHandler
    public void setDocumentLocator(Locator locator) {
    }

    @Override // org.xml.sax.helpers.DefaultHandler, org.xml.sax.ContentHandler
    public void skippedEntity(String name) {
    }

    @Override // org.xml.sax.ext.LexicalHandler
    public void comment(char[] ch2, int start, int length) throws DOMException {
        Node last = (Node) this._nodeStk.peek();
        Comment comment = this._document.createComment(new String(ch2, start, length));
        if (comment != null) {
            last.appendChild(comment);
        }
    }

    @Override // org.xml.sax.ext.LexicalHandler
    public void startCDATA() {
    }

    @Override // org.xml.sax.ext.LexicalHandler
    public void endCDATA() {
    }

    @Override // org.xml.sax.ext.LexicalHandler
    public void startEntity(String name) {
    }

    @Override // org.xml.sax.ext.LexicalHandler
    public void endEntity(String name) {
    }

    @Override // org.xml.sax.ext.LexicalHandler
    public void startDTD(String name, String publicId, String systemId) throws SAXException {
    }

    @Override // org.xml.sax.ext.LexicalHandler
    public void endDTD() {
    }
}
