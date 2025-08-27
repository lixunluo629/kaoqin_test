package org.apache.xmlbeans.impl.soap;

/* loaded from: xmlbeans-3.1.0.jar:org/apache/xmlbeans/impl/soap/Node.class */
public interface Node extends org.w3c.dom.Node {
    String getValue();

    void setParentElement(SOAPElement sOAPElement) throws SOAPException;

    SOAPElement getParentElement();

    void detachNode();

    void recycleNode();

    void setValue(String str);
}
