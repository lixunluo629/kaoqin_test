package org.apache.xmlbeans.impl.soap;

/* loaded from: xmlbeans-3.1.0.jar:org/apache/xmlbeans/impl/soap/SOAPHeaderElement.class */
public interface SOAPHeaderElement extends SOAPElement {
    void setActor(String str);

    String getActor();

    void setMustUnderstand(boolean z);

    boolean getMustUnderstand();
}
