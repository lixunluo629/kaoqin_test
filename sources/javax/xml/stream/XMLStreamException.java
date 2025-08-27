package javax.xml.stream;

/* loaded from: stax-api-1.0.1.jar:javax/xml/stream/XMLStreamException.class */
public class XMLStreamException extends Exception {
    protected Throwable nested;
    protected Location location;

    public XMLStreamException() {
    }

    public XMLStreamException(String msg) {
        super(msg);
    }

    public XMLStreamException(Throwable th) {
        this.nested = th;
    }

    public XMLStreamException(String msg, Throwable th) {
        super(msg);
        this.nested = th;
    }

    public XMLStreamException(String msg, Location location, Throwable th) {
        super(new StringBuffer().append("ParseError at [row,col]:[").append(location.getLineNumber()).append(",").append(location.getColumnNumber()).append("]\n").append("Message: ").append(msg).toString());
        this.nested = th;
        this.location = location;
    }

    public XMLStreamException(String msg, Location location) {
        super(new StringBuffer().append("ParseError at [row,col]:[").append(location.getLineNumber()).append(",").append(location.getColumnNumber()).append("]\n").append("Message: ").append(msg).toString());
        this.location = location;
    }

    public Throwable getNestedException() {
        return this.nested;
    }

    public Location getLocation() {
        return this.location;
    }
}
