package org.apache.xmlbeans.impl.soap;

/* loaded from: xmlbeans-3.1.0.jar:org/apache/xmlbeans/impl/soap/SOAPEnvelope.class */
public interface SOAPEnvelope extends SOAPElement {
    Name createName(String str, String str2, String str3) throws SOAPException;

    Name createName(String str) throws SOAPException;

    SOAPHeader getHeader() throws SOAPException;

    SOAPBody getBody() throws SOAPException;

    SOAPHeader addHeader() throws SOAPException;

    SOAPBody addBody() throws SOAPException;
}
