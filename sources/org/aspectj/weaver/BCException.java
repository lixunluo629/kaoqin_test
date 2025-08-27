package org.aspectj.weaver;

import ch.qos.logback.core.CoreConstants;
import java.io.PrintStream;
import java.io.PrintWriter;
import org.aspectj.bridge.context.CompilationAndWeavingContext;
import org.springframework.jdbc.datasource.init.ScriptUtils;

/* loaded from: aspectjweaver-1.8.14.jar:org/aspectj/weaver/BCException.class */
public class BCException extends RuntimeException {
    Throwable thrown;

    public BCException() {
    }

    public BCException(String s) {
        super(s + ScriptUtils.FALLBACK_STATEMENT_SEPARATOR + CompilationAndWeavingContext.getCurrentContext());
    }

    public BCException(String s, Throwable thrown) {
        this(s);
        this.thrown = thrown;
    }

    @Override // java.lang.Throwable
    public void printStackTrace() {
        printStackTrace(System.err);
    }

    @Override // java.lang.Throwable
    public void printStackTrace(PrintStream s) {
        printStackTrace(new PrintWriter(s));
    }

    @Override // java.lang.Throwable
    public void printStackTrace(PrintWriter s) {
        super.printStackTrace(s);
        if (null != this.thrown) {
            s.print(CoreConstants.CAUSED_BY);
            s.print(this.thrown.getClass().getName());
            String message = this.thrown.getMessage();
            if (null != message) {
                s.print(": ");
                s.print(message);
            }
            s.println();
            this.thrown.printStackTrace(s);
        }
    }
}
