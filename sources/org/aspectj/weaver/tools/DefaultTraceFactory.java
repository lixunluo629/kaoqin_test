package org.aspectj.weaver.tools;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;

/* loaded from: aspectjweaver-1.8.14.jar:org/aspectj/weaver/tools/DefaultTraceFactory.class */
public class DefaultTraceFactory extends TraceFactory {
    public static final String ENABLED_PROPERTY = "org.aspectj.tracing.enabled";
    public static final String FILE_PROPERTY = "org.aspectj.tracing.file";
    private boolean tracingEnabled = getBoolean(ENABLED_PROPERTY, false);
    private PrintStream print;

    public DefaultTraceFactory() {
        String filename = System.getProperty(FILE_PROPERTY);
        if (filename != null) {
            File file = new File(filename);
            try {
                this.print = new PrintStream(new FileOutputStream(file));
            } catch (IOException ex) {
                if (debug) {
                    ex.printStackTrace();
                }
            }
        }
    }

    public boolean isEnabled() {
        return this.tracingEnabled;
    }

    @Override // org.aspectj.weaver.tools.TraceFactory
    public Trace getTrace(Class clazz) {
        DefaultTrace trace = new DefaultTrace(clazz);
        trace.setTraceEnabled(this.tracingEnabled);
        if (this.print != null) {
            trace.setPrintStream(this.print);
        }
        return trace;
    }
}
