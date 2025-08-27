package net.sf.jsqlparser;

import java.io.PrintStream;
import java.io.PrintWriter;

/* loaded from: jsqlparser-0.9.1.jar:net/sf/jsqlparser/JSQLParserException.class */
public class JSQLParserException extends Exception {
    private static final long serialVersionUID = -1099039459759769980L;
    private Throwable cause;

    public JSQLParserException() {
        this.cause = null;
    }

    public JSQLParserException(String arg0) {
        super(arg0);
        this.cause = null;
    }

    public JSQLParserException(Throwable arg0) {
        this.cause = null;
        this.cause = arg0;
    }

    public JSQLParserException(String arg0, Throwable arg1) {
        super(arg0);
        this.cause = null;
        this.cause = arg1;
    }

    @Override // java.lang.Throwable
    public Throwable getCause() {
        return this.cause;
    }

    @Override // java.lang.Throwable
    public void printStackTrace() {
        printStackTrace(System.err);
    }

    @Override // java.lang.Throwable
    public void printStackTrace(PrintWriter pw) {
        super.printStackTrace(pw);
        if (this.cause != null) {
            pw.println("Caused by:");
            this.cause.printStackTrace(pw);
        }
    }

    @Override // java.lang.Throwable
    public void printStackTrace(PrintStream ps) {
        super.printStackTrace(ps);
        if (this.cause != null) {
            ps.println("Caused by:");
            this.cause.printStackTrace(ps);
        }
    }
}
