package org.apache.xmlbeans.impl.jam.internal;

import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.HashSet;
import java.util.Set;
import org.apache.xmlbeans.impl.jam.provider.JamLogger;

/* loaded from: xmlbeans-3.1.0.jar:org/apache/xmlbeans/impl/jam/internal/JamLoggerImpl.class */
public class JamLoggerImpl implements JamLogger {
    private boolean mShowWarnings = true;
    private Set mVerboseClasses = null;
    private PrintWriter mOut = new PrintWriter((OutputStream) System.out, true);

    protected void setOut(PrintWriter out) {
        this.mOut = out;
    }

    @Override // org.apache.xmlbeans.impl.jam.provider.JamLogger
    public boolean isVerbose(Object o) {
        if (this.mVerboseClasses == null) {
            return false;
        }
        for (Class c : this.mVerboseClasses) {
            if (c.isAssignableFrom(o.getClass())) {
                return true;
            }
        }
        return false;
    }

    @Override // org.apache.xmlbeans.impl.jam.provider.JamLogger
    public boolean isVerbose(Class aClass) {
        if (this.mVerboseClasses == null) {
            return false;
        }
        for (Class c : this.mVerboseClasses) {
            if (c.isAssignableFrom(aClass)) {
                return true;
            }
        }
        return false;
    }

    @Override // org.apache.xmlbeans.impl.jam.provider.JamLogger
    public void setVerbose(Class c) {
        if (c == null) {
            throw new IllegalArgumentException();
        }
        if (this.mVerboseClasses == null) {
            this.mVerboseClasses = new HashSet();
        }
        this.mVerboseClasses.add(c);
    }

    @Override // org.apache.xmlbeans.impl.jam.provider.JamLogger
    public void setShowWarnings(boolean b) {
        this.mShowWarnings = b;
    }

    @Override // org.apache.xmlbeans.impl.jam.provider.JamLogger
    public void verbose(String msg, Object o) {
        if (isVerbose(o)) {
            verbose(msg);
        }
    }

    @Override // org.apache.xmlbeans.impl.jam.provider.JamLogger
    public void verbose(Throwable t, Object o) {
        if (isVerbose(o)) {
            verbose(t);
        }
    }

    @Override // org.apache.xmlbeans.impl.jam.provider.JamLogger
    public void verbose(String msg) {
        printVerbosePrefix();
        this.mOut.println(msg);
    }

    @Override // org.apache.xmlbeans.impl.jam.provider.JamLogger
    public void verbose(Throwable t) {
        printVerbosePrefix();
        this.mOut.println();
        t.printStackTrace(this.mOut);
    }

    @Override // org.apache.xmlbeans.impl.jam.provider.JamLogger
    public void warning(Throwable t) {
        if (this.mShowWarnings) {
            this.mOut.println("[JAM] Warning: unexpected exception thrown: ");
            t.printStackTrace();
        }
    }

    @Override // org.apache.xmlbeans.impl.jam.provider.JamLogger
    public void warning(String w) {
        if (this.mShowWarnings) {
            this.mOut.print("[JAM] Warning: ");
            this.mOut.println(w);
        }
    }

    @Override // org.apache.xmlbeans.impl.jam.provider.JamLogger
    public void error(Throwable t) {
        this.mOut.println("[JAM] Error: unexpected exception thrown: ");
        t.printStackTrace(this.mOut);
    }

    @Override // org.apache.xmlbeans.impl.jam.provider.JamLogger
    public void error(String msg) {
        this.mOut.print("[JAM] Error: ");
        this.mOut.println(msg);
    }

    public void setVerbose(boolean v) {
        setVerbose(Object.class);
    }

    @Override // org.apache.xmlbeans.impl.jam.provider.JamLogger
    public boolean isVerbose() {
        return this.mVerboseClasses != null;
    }

    private void printVerbosePrefix() {
        StackTraceElement[] st = new Exception().getStackTrace();
        this.mOut.println("[JAM] Verbose: ");
        this.mOut.print('(');
        this.mOut.print(shortName(st[2].getClassName()));
        this.mOut.print('.');
        this.mOut.print(st[2].getMethodName());
        this.mOut.print(':');
        this.mOut.print(st[2].getLineNumber());
        this.mOut.print(")  ");
    }

    private static String shortName(String className) {
        int index = className.lastIndexOf(46);
        if (index != -1) {
            className = className.substring(index + 1, className.length());
        }
        return className;
    }
}
