package org.apache.xmlbeans.impl.soap;

import java.util.Locale;
import org.w3c.dom.Document;

/* loaded from: xmlbeans-3.1.0.jar:org/apache/xmlbeans/impl/soap/SOAPBody.class */
public interface SOAPBody extends SOAPElement {
    SOAPFault addFault() throws SOAPException;

    boolean hasFault();

    SOAPFault getFault();

    SOAPBodyElement addBodyElement(Name name) throws SOAPException;

    SOAPFault addFault(Name name, String str, Locale locale) throws SOAPException;

    SOAPFault addFault(Name name, String str) throws SOAPException;

    SOAPBodyElement addDocument(Document document) throws SOAPException;
}
