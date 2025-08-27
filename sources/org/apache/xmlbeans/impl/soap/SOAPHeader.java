package org.apache.xmlbeans.impl.soap;

import java.util.Iterator;

/* loaded from: xmlbeans-3.1.0.jar:org/apache/xmlbeans/impl/soap/SOAPHeader.class */
public interface SOAPHeader extends SOAPElement {
    SOAPHeaderElement addHeaderElement(Name name) throws SOAPException;

    Iterator examineHeaderElements(String str);

    Iterator extractHeaderElements(String str);

    Iterator examineMustUnderstandHeaderElements(String str);

    Iterator examineAllHeaderElements();

    Iterator extractAllHeaderElements();
}
