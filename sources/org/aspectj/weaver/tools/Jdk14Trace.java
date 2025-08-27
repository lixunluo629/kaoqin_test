package org.aspectj.weaver.tools;

import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;

/* loaded from: aspectjweaver-1.8.14.jar:org/aspectj/weaver/tools/Jdk14Trace.class */
public class Jdk14Trace extends AbstractTrace {
    private Logger logger;
    private String name;

    public Jdk14Trace(Class clazz) {
        super(clazz);
        this.name = clazz.getName();
        this.logger = Logger.getLogger(this.name);
    }

    @Override // org.aspectj.weaver.tools.AbstractTrace, org.aspectj.weaver.tools.Trace
    public void enter(String methodName, Object thiz, Object[] args) {
        if (this.logger.isLoggable(Level.FINE)) {
            this.logger.entering(this.name, methodName, formatObj(thiz));
            if (args != null && this.logger.isLoggable(Level.FINER)) {
                this.logger.entering(this.name, methodName, formatObjects(args));
            }
        }
    }

    @Override // org.aspectj.weaver.tools.AbstractTrace, org.aspectj.weaver.tools.Trace
    public void enter(String methodName, Object thiz) {
        enter(methodName, thiz, (Object[]) null);
    }

    @Override // org.aspectj.weaver.tools.AbstractTrace, org.aspectj.weaver.tools.Trace
    public void exit(String methodName, Object ret) {
        if (this.logger.isLoggable(Level.FINE)) {
            this.logger.exiting(this.name, methodName, formatObj(ret));
        }
    }

    @Override // org.aspectj.weaver.tools.AbstractTrace, org.aspectj.weaver.tools.Trace
    public void exit(String methodName, Throwable th) {
        if (this.logger.isLoggable(Level.FINE)) {
            this.logger.exiting(this.name, methodName, th);
        }
    }

    @Override // org.aspectj.weaver.tools.Trace
    public void exit(String methodName) {
        if (this.logger.isLoggable(Level.FINE)) {
            this.logger.exiting(this.name, methodName);
        }
    }

    @Override // org.aspectj.weaver.tools.Trace
    public void event(String methodName, Object thiz, Object[] args) {
        if (this.logger.isLoggable(Level.FINE)) {
            this.logger.logp(Level.FINER, this.name, methodName, "EVENT", formatObj(thiz));
            if (args != null && this.logger.isLoggable(Level.FINER)) {
                this.logger.logp(Level.FINER, this.name, methodName, "EVENT", formatObjects(args));
            }
        }
    }

    @Override // org.aspectj.weaver.tools.Trace
    public void event(String methodName) {
        if (this.logger.isLoggable(Level.FINE)) {
            this.logger.logp(Level.FINER, this.name, methodName, "EVENT");
        }
    }

    @Override // org.aspectj.weaver.tools.Trace
    public boolean isTraceEnabled() {
        return this.logger.isLoggable(Level.FINER);
    }

    @Override // org.aspectj.weaver.tools.Trace
    public void setTraceEnabled(boolean b) throws SecurityException {
        Logger parent;
        if (b) {
            this.logger.setLevel(Level.FINER);
            Handler[] handlers = this.logger.getHandlers();
            if (handlers.length == 0 && (parent = this.logger.getParent()) != null) {
                handlers = parent.getHandlers();
            }
            for (Handler handler : handlers) {
                handler.setLevel(Level.FINER);
            }
            return;
        }
        this.logger.setLevel(Level.INFO);
    }

    @Override // org.aspectj.weaver.tools.Trace
    public void debug(String message) {
        if (this.logger.isLoggable(Level.FINE)) {
            this.logger.fine(message);
        }
    }

    @Override // org.aspectj.weaver.tools.Trace
    public void info(String message) {
        if (this.logger.isLoggable(Level.INFO)) {
            this.logger.info(message);
        }
    }

    @Override // org.aspectj.weaver.tools.Trace
    public void warn(String message, Throwable th) {
        if (this.logger.isLoggable(Level.WARNING)) {
            this.logger.log(Level.WARNING, message, th);
        }
    }

    @Override // org.aspectj.weaver.tools.Trace
    public void error(String message, Throwable th) {
        if (this.logger.isLoggable(Level.SEVERE)) {
            this.logger.log(Level.SEVERE, message, th);
        }
    }

    @Override // org.aspectj.weaver.tools.Trace
    public void fatal(String message, Throwable th) {
        if (this.logger.isLoggable(Level.SEVERE)) {
            this.logger.log(Level.SEVERE, message, th);
        }
    }
}
