package org.apache.xmlbeans.xml.stream;

import java.io.IOException;
import java.io.PrintStream;
import java.io.PrintWriter;
import org.apache.xmlbeans.xml.stream.utils.NestedThrowable;

/* loaded from: xmlbeans-3.1.0.jar:org/apache/xmlbeans/xml/stream/XMLStreamException.class */
public class XMLStreamException extends IOException implements NestedThrowable {
    protected Throwable th;

    public XMLStreamException() {
    }

    public XMLStreamException(String msg) {
        super(msg);
    }

    public XMLStreamException(Throwable th) {
        this.th = th;
    }

    public XMLStreamException(String msg, Throwable th) {
        super(msg);
        this.th = th;
    }

    public Throwable getNestedException() {
        return getNested();
    }

    @Override // java.lang.Throwable
    public String getMessage() {
        String msg = super.getMessage();
        if (msg == null && this.th != null) {
            return this.th.getMessage();
        }
        return msg;
    }

    @Override // org.apache.xmlbeans.xml.stream.utils.NestedThrowable
    public Throwable getNested() {
        return this.th;
    }

    @Override // org.apache.xmlbeans.xml.stream.utils.NestedThrowable
    public String superToString() {
        return super.toString();
    }

    @Override // org.apache.xmlbeans.xml.stream.utils.NestedThrowable
    public void superPrintStackTrace(PrintStream ps) {
        super.printStackTrace(ps);
    }

    @Override // org.apache.xmlbeans.xml.stream.utils.NestedThrowable
    public void superPrintStackTrace(PrintWriter pw) {
        super.printStackTrace(pw);
    }

    @Override // java.lang.Throwable
    public String toString() {
        return NestedThrowable.Util.toString(this);
    }

    @Override // java.lang.Throwable
    public void printStackTrace(PrintStream s) {
        NestedThrowable.Util.printStackTrace(this, s);
    }

    @Override // java.lang.Throwable
    public void printStackTrace(PrintWriter w) {
        NestedThrowable.Util.printStackTrace(this, w);
    }

    @Override // java.lang.Throwable
    public void printStackTrace() {
        printStackTrace(System.err);
    }
}
