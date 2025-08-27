package org.apache.xmlbeans.impl.common;

import javax.xml.stream.Location;

/* loaded from: xmlbeans-3.1.0.jar:org/apache/xmlbeans/impl/common/InvalidLexicalValueException.class */
public class InvalidLexicalValueException extends RuntimeException {
    private Location _location;

    public InvalidLexicalValueException() {
    }

    public InvalidLexicalValueException(String msg) {
        super(msg);
    }

    public InvalidLexicalValueException(String msg, Throwable cause) {
        super(msg, cause);
    }

    public InvalidLexicalValueException(Throwable cause) {
        super(cause);
    }

    public InvalidLexicalValueException(String msg, Location location) {
        super(msg);
        setLocation(location);
    }

    public InvalidLexicalValueException(String msg, Throwable cause, Location location) {
        super(msg, cause);
        setLocation(location);
    }

    public InvalidLexicalValueException(Throwable cause, Location location) {
        super(cause);
        setLocation(location);
    }

    public Location getLocation() {
        return this._location;
    }

    public void setLocation(Location location) {
        this._location = location;
    }
}
