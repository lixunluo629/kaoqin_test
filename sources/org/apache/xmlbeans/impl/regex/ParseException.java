package org.apache.xmlbeans.impl.regex;

/* loaded from: xmlbeans-3.1.0.jar:org/apache/xmlbeans/impl/regex/ParseException.class */
public class ParseException extends RuntimeException {
    int location;

    public ParseException(String mes, int location) {
        super(mes);
        this.location = location;
    }

    public int getLocation() {
        return this.location;
    }
}
