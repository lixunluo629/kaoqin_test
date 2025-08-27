package org.aspectj.weaver.tools;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/* loaded from: aspectjweaver-1.8.14.jar:org/aspectj/weaver/tools/CommonsTrace.class */
public class CommonsTrace extends AbstractTrace {
    private Log log;
    private String className;

    public CommonsTrace(Class clazz) {
        super(clazz);
        this.log = LogFactory.getLog(clazz);
        this.className = this.tracedClass.getName();
    }

    @Override // org.aspectj.weaver.tools.AbstractTrace, org.aspectj.weaver.tools.Trace
    public void enter(String methodName, Object thiz, Object[] args) {
        if (this.log.isDebugEnabled()) {
            this.log.debug(formatMessage(">", this.className, methodName, thiz, args));
        }
    }

    @Override // org.aspectj.weaver.tools.AbstractTrace, org.aspectj.weaver.tools.Trace
    public void enter(String methodName, Object thiz) {
        if (this.log.isDebugEnabled()) {
            this.log.debug(formatMessage(">", this.className, methodName, thiz, null));
        }
    }

    @Override // org.aspectj.weaver.tools.AbstractTrace, org.aspectj.weaver.tools.Trace
    public void exit(String methodName, Object ret) {
        if (this.log.isDebugEnabled()) {
            this.log.debug(formatMessage("<", this.className, methodName, ret, null));
        }
    }

    @Override // org.aspectj.weaver.tools.AbstractTrace, org.aspectj.weaver.tools.Trace
    public void exit(String methodName, Throwable th) {
        if (this.log.isDebugEnabled()) {
            this.log.debug(formatMessage("<", this.className, methodName, th, null));
        }
    }

    @Override // org.aspectj.weaver.tools.Trace
    public void exit(String methodName) {
        if (this.log.isDebugEnabled()) {
            this.log.debug(formatMessage("<", this.className, methodName, null, null));
        }
    }

    @Override // org.aspectj.weaver.tools.Trace
    public void event(String methodName, Object thiz, Object[] args) {
        if (this.log.isDebugEnabled()) {
            this.log.debug(formatMessage("-", this.className, methodName, thiz, args));
        }
    }

    @Override // org.aspectj.weaver.tools.Trace
    public void event(String methodName) {
        if (this.log.isDebugEnabled()) {
            this.log.debug(formatMessage("-", this.className, methodName, null, null));
        }
    }

    @Override // org.aspectj.weaver.tools.Trace
    public boolean isTraceEnabled() {
        return this.log.isDebugEnabled();
    }

    @Override // org.aspectj.weaver.tools.Trace
    public void setTraceEnabled(boolean b) {
    }

    @Override // org.aspectj.weaver.tools.Trace
    public void debug(String message) {
        if (this.log.isDebugEnabled()) {
            this.log.debug(message);
        }
    }

    @Override // org.aspectj.weaver.tools.Trace
    public void info(String message) {
        if (this.log.isInfoEnabled()) {
            this.log.info(message);
        }
    }

    @Override // org.aspectj.weaver.tools.Trace
    public void warn(String message, Throwable th) {
        if (this.log.isWarnEnabled()) {
            this.log.warn(message, th);
        }
    }

    @Override // org.aspectj.weaver.tools.Trace
    public void error(String message, Throwable th) {
        if (this.log.isErrorEnabled()) {
            this.log.error(message, th);
        }
    }

    @Override // org.aspectj.weaver.tools.Trace
    public void fatal(String message, Throwable th) {
        if (this.log.isFatalEnabled()) {
            this.log.fatal(message, th);
        }
    }
}
