package org.apache.xmlbeans.impl.soap;

/* loaded from: xmlbeans-3.1.0.jar:org/apache/xmlbeans/impl/soap/MimeHeader.class */
public class MimeHeader {
    private String name;
    private String value;

    public MimeHeader(String name, String value) {
        this.name = name;
        this.value = value;
    }

    public String getName() {
        return this.name;
    }

    public String getValue() {
        return this.value;
    }
}
