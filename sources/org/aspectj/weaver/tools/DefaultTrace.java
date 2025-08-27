package org.aspectj.weaver.tools;

import java.io.PrintStream;

/* loaded from: aspectjweaver-1.8.14.jar:org/aspectj/weaver/tools/DefaultTrace.class */
public class DefaultTrace extends AbstractTrace {
    private boolean traceEnabled;
    private PrintStream print;

    public DefaultTrace(Class clazz) {
        super(clazz);
        this.traceEnabled = false;
        this.print = System.err;
    }

    @Override // org.aspectj.weaver.tools.Trace
    public boolean isTraceEnabled() {
        return this.traceEnabled;
    }

    @Override // org.aspectj.weaver.tools.Trace
    public void setTraceEnabled(boolean b) {
        this.traceEnabled = b;
    }

    @Override // org.aspectj.weaver.tools.AbstractTrace, org.aspectj.weaver.tools.Trace
    public void enter(String methodName, Object thiz, Object[] args) {
        if (this.traceEnabled) {
            println(formatMessage(">", this.tracedClass.getName(), methodName, thiz, args));
        }
    }

    @Override // org.aspectj.weaver.tools.AbstractTrace, org.aspectj.weaver.tools.Trace
    public void enter(String methodName, Object thiz) {
        if (this.traceEnabled) {
            println(formatMessage(">", this.tracedClass.getName(), methodName, thiz, null));
        }
    }

    @Override // org.aspectj.weaver.tools.AbstractTrace, org.aspectj.weaver.tools.Trace
    public void exit(String methodName, Object ret) {
        if (this.traceEnabled) {
            println(formatMessage("<", this.tracedClass.getName(), methodName, ret, null));
        }
    }

    @Override // org.aspectj.weaver.tools.Trace
    public void exit(String methodName) {
        if (this.traceEnabled) {
            println(formatMessage("<", this.tracedClass.getName(), methodName, null, null));
        }
    }

    @Override // org.aspectj.weaver.tools.AbstractTrace, org.aspectj.weaver.tools.Trace
    public void exit(String methodName, Throwable th) {
        if (this.traceEnabled) {
            println(formatMessage("<", this.tracedClass.getName(), methodName, th, null));
        }
    }

    @Override // org.aspectj.weaver.tools.Trace
    public void event(String methodName, Object thiz, Object[] args) {
        if (this.traceEnabled) {
            println(formatMessage("-", this.tracedClass.getName(), methodName, thiz, args));
        }
    }

    @Override // org.aspectj.weaver.tools.Trace
    public void event(String methodName) {
        if (this.traceEnabled) {
            println(formatMessage("-", this.tracedClass.getName(), methodName, null, null));
        }
    }

    @Override // org.aspectj.weaver.tools.Trace
    public void debug(String message) {
        println(formatMessage("?", message, null));
    }

    @Override // org.aspectj.weaver.tools.Trace
    public void info(String message) {
        println(formatMessage("I", message, null));
    }

    @Override // org.aspectj.weaver.tools.Trace
    public void warn(String message, Throwable th) {
        println(formatMessage("W", message, th));
        if (th != null) {
            th.printStackTrace();
        }
    }

    @Override // org.aspectj.weaver.tools.Trace
    public void error(String message, Throwable th) {
        println(formatMessage("E", message, th));
        if (th != null) {
            th.printStackTrace();
        }
    }

    @Override // org.aspectj.weaver.tools.Trace
    public void fatal(String message, Throwable th) {
        println(formatMessage("X", message, th));
        if (th != null) {
            th.printStackTrace();
        }
    }

    protected void println(String s) {
        this.print.println(s);
    }

    public void setPrintStream(PrintStream printStream) {
        this.print = printStream;
    }
}
