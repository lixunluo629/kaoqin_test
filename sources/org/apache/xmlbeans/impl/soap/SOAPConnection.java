package org.apache.xmlbeans.impl.soap;

/* loaded from: xmlbeans-3.1.0.jar:org/apache/xmlbeans/impl/soap/SOAPConnection.class */
public abstract class SOAPConnection {
    public abstract SOAPMessage call(SOAPMessage sOAPMessage, Object obj) throws SOAPException;

    public abstract void close() throws SOAPException;
}
