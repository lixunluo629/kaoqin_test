package org.apache.xmlbeans.impl.soap;

import java.util.Iterator;

/* loaded from: xmlbeans-3.1.0.jar:org/apache/xmlbeans/impl/soap/Detail.class */
public interface Detail extends SOAPFaultElement {
    DetailEntry addDetailEntry(Name name) throws SOAPException;

    Iterator getDetailEntries();
}
