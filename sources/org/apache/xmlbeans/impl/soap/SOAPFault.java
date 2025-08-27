package org.apache.xmlbeans.impl.soap;

import java.util.Locale;

/* loaded from: xmlbeans-3.1.0.jar:org/apache/xmlbeans/impl/soap/SOAPFault.class */
public interface SOAPFault extends SOAPBodyElement {
    void setFaultCode(String str) throws SOAPException;

    String getFaultCode();

    void setFaultActor(String str) throws SOAPException;

    String getFaultActor();

    void setFaultString(String str) throws SOAPException;

    String getFaultString();

    Detail getDetail();

    Detail addDetail() throws SOAPException;

    void setFaultCode(Name name) throws SOAPException;

    Name getFaultCodeAsName();

    void setFaultString(String str, Locale locale) throws SOAPException;

    Locale getFaultStringLocale();
}
